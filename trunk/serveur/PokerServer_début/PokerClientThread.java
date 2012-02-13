

import java.io.*;
import java.net.Socket;

/**
 * Classe du thread par client qui gère les messages avec son client
 * @author ben Maurin
 */
class PokerClientThread extends Thread {
    private PrintWriter screenOut = new PrintWriter(System.out, true);
    private Socket socket = null;
    private String clientIP;
    private BufferedWriter out;
    private BufferedReader in;
    private PokerPartie partie= null;
    private String pseudo = "";

    public PokerClientThread(Socket socket){
        super("PokerClientThread");
        this.socket = socket;
        System.err.println();
    }

    /**
     * Fonction qui receptionne les messages
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
                while ((inputLine = in.readLine()) != null) {
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
     * Fonction qui traite le message en fonction de ses balises
     * @param inputLine : le message à traiter
     */
    private void traitements(String inputLine){
        try{
            //perroquet test
            screenOut.println("Socket recue : "+inputLine);
            send(clientIP+" : "+inputLine);

            //Reception nom des clients
            if(inputLine.length()>=8 && inputLine.substring(0,8).equals("<pseudo>")){
                this.pseudo = inputLine.substring(8);
                screenOut.println("Pseudo enregistre : "+inputLine);
            }

            //Reception messages des clients
            if(inputLine.length()>=9 && inputLine.substring(0,9).equals("<message>")){
                screenOut.println("Message recu : "+inputLine);
            }	

        }catch(Exception e){
            screenOut.println("ya un bug traitements message");
        }
    }

    /**
     * 
     * @return clientIP
     */
    public String getClientIP(){ 
        return this.clientIP;
    }
    
    /**
     * 
     * @return pseudo
     */
    public String getPseudo(){ 
        return this.pseudo;
    }

    /**
     * Fonction pour une deconnection propre
     */
    public void deco(){
        try{
            in.close();
            out.close();
            socket.close();	
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
            screenOut.close();
        } catch(Exception e) {
        
        }
        finally {
            super.finalize();
        }
    }

    /**
     * Tente de rejoindre une partie, sinon envoi un message d'erreur au client
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
     * @param message : message à envoyer
     */
    public void send(String message){
        try{
            out.write(message+"\n");
            out.flush();
            screenOut.println("Message envoye : "+message);
        }catch(Exception e){
            screenOut.println("ya un bug envoi message");
        }
    }
}