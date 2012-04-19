/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe permettant de gérer la connection du client.
 * 
 * @author Jessy Bonnotte
 */
public class Connection implements Runnable{
    // Activity courante
    /**
     * Pour selectionner l'activity accueuil.
     */
    public static final int     ACCUEUIL = 1;
    /**
     * Pour selectionner l'activity parametre.
     */
    public static final int     PARAMETRE = 2;
    /**
     * Pour selectionner l'activity liste partie.
     */
    public static final int     LISTE_PARTIE = 3;
    /**
     * Pour selectionner l'activity liste joueur partie.
     */
    public static final int     LISTE_JOUEUR_PARTIE = 4;
    /**
     * Pour selectionner l'activity tableau jeu.
     */
    public static final int     PARTIE = 5;
    
    private int                 currentActivity = 0;
    
    // Propriétés de comunication
    private String              adresse;
    private int                 port;
    
    // Gestion des flux de lecture et d'écriture
    private BufferedReader      br;
    private BufferedWriter      bw;
    
    // La socket de comunication
    private Socket              sock;
    
    // Le thread d'écoute
    private Thread              proc;
    
    // Comunication entre les thread
    private Handler             hand;
    private Message             msgvaleur;
    
    // Décriptage de la tram
    private Crypt               cryptTram;
    
    // Analyse de la tram
    private AnalyseurTram       analTram;
    
    // Timeout de connexion
    private FutureTask<?>       theTask = null;
    private long                depart = 0;
    private long                arrive = 0;
 
    /**
     * Constructeur classe de connexion.
     */
    public Connection(){
        
    }
    /**
     * Fonction permettant de definir l'activity courante.
     * 
     * @author Jessy Bonnotte
     * 
     * @param a - numéro de l'activity courante
     */
    public void setActivity(int a){
        currentActivity = a;
    }
    
    /**
     * Fonction permettant d' initialiser la connexion.
     * 
     * @author Jessy Bonnotte
     * 
     * @param a - adresse du serveur.
     * @param p - port du serveur.
     * 
     * @return boolean - true si connection ok ou false si échouée.
     */
    public boolean init(String a, String p){
        adresse = a;
        cryptTram = new Crypt();
        port = Integer.parseInt(p);
        analTram = new AnalyseurTram();

        hand = new Handler(){
            @Override
            public void handleMessage(Message msg){
                try {
                    analTram.setTram(msg.obj.toString(), currentActivity);
                } catch (IOException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        try {
            Log.v("connexion", "avant la socket : "+adresse+"  "+port);

            //sock = new Socket(adresse, port);

            Log.v("Parametre", "avant socket");
            sock = new Socket();
            sock.connect(new InetSocketAddress(adresse, port), 5000);
            Log.v("Parametre", "apres socket");
            if(!sock.isConnected()){
                msgvaleur = new Message();
                msgvaleur.obj = "TIMEOUT";
                hand.sendMessage(msgvaleur);
                return false;
            }
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            proc = new Thread(this);
            proc.start();                   
            /*}catch (IOException e) {

            }*/
            return true;
        } catch (UnknownHostException ex) {
            Log.v("Parametre", "ici");
            msgvaleur = new Message();
            msgvaleur.obj = "ERREUR";
            hand.sendMessage(msgvaleur);

            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        } catch (IOException ex) {
            Log.v("Parametre", ex.getMessage());
            msgvaleur = new Message();
            msgvaleur.obj = "ERREUR";
            hand.sendMessage(msgvaleur);

            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }
    }
    
    /**
     * Fonction permettant d'envoyer un message au serveur.
     * 
     * @author Mathieu Polizzi
     * 
     * @param message - chaine a envoyer au serveur
     * 
     * @throws IOException - errerur au niveau du buffer d'envoi
     */
    public void say(String message) throws IOException{
        bw.write(/*cryptTram.enCrypt(*/message/*)*/+"\n");
        bw.flush();
    }
    
    /**
     * Fonction permmettant la fermeture de la connection.
     * 
     * @author Jessy Bonnotte
     * 
     * @return boolean - true si fermeture ok ou false si échouée.
     */
    public boolean dispose(){
        try {
            sock.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    /**
     * Fonction permettant de tester si la connexion est active.
     * 
     * @author Jessy Bonnotte
     * 
     * @return boolean - true si connexion active ou false si échouée.
     */
    public boolean isConnected(){
        if(sock.isConnected()){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Fonction permettant de lancer le processus d'ecoute.
     * 
     * @author Jessy Bonnotte
     */
    private void start(){
        proc.start();
    }
    
    /**
     * Fonction permettant de fermer le processus d'ecoute.
     * 
     * @author Jessy Bonnotte
     */
    private void stop(){
        proc.stop();
    }
    
    /**
     * Thread d'écoute. Fonction run surchargée de la classe runnable permmettant d'avoir un thread d'écoute en parrallèle.
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void run() {
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                if(!ligne.equals("")){
                    msgvaleur = new Message();
                    msgvaleur.obj = /*cryptTram.deCrypt(*/ligne/*)*/;
                    hand.sendMessage(msgvaleur);
                }
            }
            msgvaleur = new Message();
            msgvaleur.obj = "CONNEXION CLOSE";
            hand.sendMessage(msgvaleur);
            dispose();
        } catch (Exception e) {
            
       	}
    }   
}
