

import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.Semaphore;


/**
 * Classe du serveur qui va gerer les parties
 * @author benjamin Maurin
 */
public class PokerPartie {
		
    private Vector clientList= new Vector();
    private PrintWriter screenOut = new PrintWriter(System.out, true);
    private int enCours = 0;
    private int maxPlayers = 8;
    private Semaphore available = new Semaphore(1, true);
    private String nomP = "";
    private Jeu jeu;

    /**
    *Constructeur
    * @author benjamin Maurin
    *@param nom : nom de la partie
    * @param max : nombre de joueurs maximum dans la partie
    * 
    */
    public PokerPartie(String nom,int max){
        this.nomP = nom;
        this.maxPlayers= max;
    }
	
    /**
     * accesseur
     * @author benjamin Maurin
     * @return clientList
     */
    public Vector getClientList(){
        return clientList;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nombre joueurs dans la partie
     */
    public String getEtat(){ 
        return clientList.size()+"/"+maxPlayers+" joueurs dans la partie";
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @param m maxPlayeur
     */
    public void setMaxPlayers(int m){
        this.maxPlayers=m;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return maxPlayers
     */
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nombre de joueurs dans la partie
     */
    public int getNbJ(){
        return clientList.size();
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nom de la partie
     */
    public String getNom(){
        return this.nomP;
    }


    /**
     * Fonction pour envoyer un message à tous les clients de la partie
     * @author Benjamin Maurin
     * @param m : message à envoyer
     */
    public void broadcastClientsPartie(String m)	{
        int i;
        PokerClientThread foo;
        for (i=0;i<clientList.size();++i){
            foo = (PokerClientThread)clientList.get(i);
            foo.send(m);
        } 	
    }
    
    /**
     * Fonction pour enlever un client de la partie
     * @author Benjamin Maurin
     * @param deadClient client à supprimer
     */
    public void deleteClient(PokerClientThread deadClient){
        try{
            available.acquire();
            int num;
            num=clientList.indexOf(deadClient);
            if(num!=-1){
                clientList.remove(num);                         
            }                       
            if(clientList.size()<1){
                PokerServer.deletePartie(this);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            finally {
             available.release();
        }
    }


    /**
     * Fonction pour ajouter un client à la partie
     * @author Benjamin Maurin
     * @param np nouveau player
     * @return 0 si c'est possible -1 sinon
     */
    public int addPlayer(PokerClientThread np){
    if(clientList.size()<maxPlayers){
    clientList.add(np);	return 0;}
    else return -1;
                                                                                    }

    /**
    * Fonction pour libérer la mémoire
    * @author Benjamin Maurin
    * @throws Throwable
    */
    @Override
    protected void finalize() throws Throwable{
        try {
            clientList.clear();
            available  =null;
            nomP = null;
            jeu = null;
            screenOut.close();
        }catch(Exception e) {
        
        }
        finally {
            super.finalize();
        } 
    }											
}