

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe du thread par client qui gère les messages avec son client
 * @author Benjamin Maurin
 */
class PokerClientThread extends Thread {
    private PrintWriter screenOut = new PrintWriter(System.out, true);
    private Socket socket = null;
    private String clientIP;
    private BufferedWriter out;
    private BufferedReader in;
    private PokerPartie partie= null;
    private String pseudo = "";
    private Crypt crypt;
    private boolean lecture = true;
    private boolean connecte = false;
    private int[] jetons = new int[2];
    private int[] cartes = new int[2];
    private int attente = 0;
    
    public PokerClientThread(Socket socket){
        super("PokerClientThread");
        this.socket = socket;
        //jetons[0] == jetons totaux , jetons[1] jetons posées
        jetons[0] = -1;jetons[1] = -1;
        cartes[0] = -1;cartes[1] = -1;
        System.err.println();
    }

    /**
     * Fonction qui receptionne les messages
     * @author Benjamin Maurin
     */
    @Override
    public void run(){
        clientIP = socket.getInetAddress().getHostAddress();
        try{
            //new PrintWriter(socket.getOutputStream(), true);
            out =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            String inputLine;

            try {
                // lance le traitement du message reçu
                while (lecture && ((inputLine = in.readLine()) != null) ) {
                    //crypt.deCrypt(inputLine)
                    traitements(inputLine);
                }
              
            } catch (Exception e) {
                screenOut.println("ya un bug dans un thread reception message "+e);
            }
            
            deco();
        }catch(IOException e){
            screenOut.println("ya un bug dans un thread (bug général)");
            deco();
        }	
    }


     /**
     * @author benjamin Maurin
     * @return attente
     */
    public int getAttente(){ 
        return this.attente;
    }
    
       /**
     * @author benjamin Maurin
     */
    public void setAttente(int a){ 
       this.attente = a;
    }
    
       /**
     * @author benjamin Maurin
     */
    public void setPartie(PokerPartie a){ 
       this.partie = a;
    }
         /**
     * @author benjamin Maurin
     * @return cartes du joueur
     */
    public int[] getCartes(){ 
        return this.cartes;
    }
    
     /**
     * @author benjamin Maurin
     */
    public void setCartes(int a, int b){ 
        this.cartes[0]=a; this.cartes[1]=b;
    }
     /**
     * @author benjamin Maurin
     * @return Jetons totaux du joueur
     */
    public int getJetonsTotaux(){ 
        return this.jetons[0];
    }
    
      /**
     * @author benjamin Maurin
     * @return Jetons posés du joueur
     */
    public int getJetonsPoses(){ 
        return this.jetons[1];
    }
    
     /**
     * @author benjamin Maurin
     */
    public void setJetonsTotaux(int a){ 
       this.jetons[0] = a;
    }
    
     /**
     * @author benjamin Maurin
     */
    public void setJetonsPoses(int a){ 
       this.jetons[1] = a;
    }

    /**
     * @author benjamin Maurin
     * @return clientIP
     */
    public String getClientIP(){ 
        return this.clientIP;
    }
    
    /**
     * @author benjamin Maurin
     * @return pseudo
     */
    public String getPseudo(){ 
        return this.pseudo;
    }

    /**
     * Fonction pour une deconnection propre
     * @author benjamin Maurin
     */
    public void deco(){
        try{
            lecture = false;
            socket.close();	
            in.close();
            out.close();
            if(partie!=null){
                partie.deleteClient(this);
            }
            PokerServer.deleteClient(this);
        }catch(IOException e){
            screenOut.println("ya un bug dans un thread deco"+e);
        }	
    }
	
    /**
     * Fonction pour optimiser la suppression de l'objet
     * @author benjamin Maurin
     * @throws Throwable 
     */
    @Override
    protected void finalize() throws Throwable{
        try {
            socket = null;
            clientIP=null;
            out = null;
            in = null;
            partie= null;
            pseudo = null;
            crypt = null;
            jetons =null;
            cartes = null;
            screenOut=null;
        } catch(Exception e) {
        
        }
        finally {
            super.finalize();
        }
    }

    /**
     * Tente de rejoindre une partie, sinon envoi un message d'erreur au client
     * @author benjamin Maurin
     * @param nomP : nom de la partie à rejoindre
     */
    public void rejoindrePartie(String nomP){	
        int existe = 0;
        PokerPartie foo = null;
        for (int i=0;i<PokerServer.partiesList.size();++i){
            foo = (PokerPartie)PokerServer.partiesList.get(i);
            if(foo.getNom().equals(nomP)){
                existe=1;break;
            }
        }

        if(existe==1){
            if(foo.addPlayer(this)==-1){
                existe = 0;
            }else{
                partie=foo;
            }
        }
        if(existe==0){
            send("probleme partie trop de joueurs ou partie inconnue");
        }
    }

    /**
     * Envoi un message au client
     * @author Benjamin Maurin
     * @param message : message à envoyer
     */
    public void send(String message){
        try{//crypt.enCrypt(message
            out.write(message+"\n");
            out.flush();
            screenOut.println("Message envoye : "+message);
        }catch(Exception e){
            screenOut.println("ya un bug envoi message");
        }
    }
    
   
    
        /**
     * Fonction qui traite le message en fonction de ses balises
     * @author benjamin Maurin
     * @param inputLine : le message à traiter
     * @param   *1 : message traite *0: type de message inconnu *-1 : erreur dans le traitement
     */
    private int traitements(String inputLine){
        try{
            //perroquet test
            screenOut.println("Socket recue : "+inputLine+"\n");
         //   send(clientIP+" : "+inputLine);

            StringTokenizer  st = new StringTokenizer(inputLine,"@");
            String cmd = st.nextToken();
            String test1 = "";
            
            //Demande de connection
            if(cmd.equals("CONNECT")){
                    screenOut.println("demande de connection\n");
                    test1=st.nextToken();    
                    cmd=PokerServer.bd.verifPassword(test1,st.nextToken());
                    send(cmd);
                    if(!cmd.equals("CONNECTOK")){ 
                        lecture=false;      
                    }
                    else  {connecte = true;pseudo = test1;}
                    return 1;
            }
            //Demandes des parties en cours
            if(cmd.equals("GETLISTEPARTIE")){
                    screenOut.println("Liste des parties envoyes a un joueur\n");
                    if(connecte)
                    send(PokerServer.listClientParties());
                    else {send("NCON"); }
                    return 1;
            }
            //Creation de compte
            if(cmd.equals("CREATCPT"))
            {
                screenOut.println("nouveau compte créé\n");
                send(PokerServer.bd.ajouteJoueur(st.nextToken(), st.nextToken()));
                lecture=false;
                return 1;
            }
             //Changer de pseudo
              if(cmd.equals("ACTPSEUDO"))
            {
                 screenOut.println("pseudo actualisé\n");
                send(PokerServer.bd.changePseudo(st.nextToken(), st.nextToken(), st.nextToken()));
                lecture=false;
                return 1;
            }
              //Changer de mot de passe
               if(cmd.equals("ACTPASS"))
            {
                screenOut.println("mot de passe actualisé\n");
                send(PokerServer.bd.changePseudo(st.nextToken(), st.nextToken(), st.nextToken()));
                lecture=false;
                return 1;
            }
                //Creer une partie // IF CONNECTE
               if(cmd.equals("CREATEPARTIE"))
            {
                screenOut.println("creation de partie en cours...\n");
                 if(connecte){
                     if(partie==null){
                    send(PokerServer.creerPartie(this, st.nextToken(),Integer.parseInt(st.nextToken())));
                     }
                     else {send("AIP"); }
                 }
                 else {send("NCON"); }
                return 1;
            }
                    //rejoindre une partie
               if(cmd.equals("REJP"))
            {
                screenOut.println("tentative de rejoindre une partie...\n");
                if(connecte){
                    if(partie==null){
                    send(PokerServer.rejoindrePartie(this, st.nextToken()));
                    }
                     else {send("AIP"); }
                }
                else {send("NCON"); }
                return 1;
            }
                return 0;
                
        }catch(Exception e){
            screenOut.println("ya un bug traitements message");
            e.printStackTrace();
            send("ERROR");
            lecture=false;
            return -1;
        }
    }
    
}