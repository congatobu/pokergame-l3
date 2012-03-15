/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shyzkanza
 */
public class Connection implements Runnable{
    // Activity courante
    public static final int     ACCUEUIL = 1;
    public static final int     PARAMETRE = 2;
    public static final int     LISTE_PARTIE = 3;
    
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
    private FutureTask<?> theTask = null;
    private long depart = 0;
    private long arrive = 0;
    
    public Connection(){
        
    }
    
    public void setActivity(int a){
        currentActivity = a;
    }
    
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
            sock = new Socket(adresse, port);
            Log.v("Parametre", "avant socket");
            
            //sock.connect(new InetSocketAddress(adresse, port), 3000);
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
        }catch (Exception e) {
            msgvaleur = new Message();
            msgvaleur.obj = "ERREUR";
            hand.sendMessage(msgvaleur);
            return false;
        }
        return true;
    }
    
    public void say(String message) throws IOException{
        bw.write(/*cryptTram.enCrypt(*/message/*)*/+"\n");
        bw.flush();
    }
    
    public boolean dispose(){
        try {
            sock.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public boolean isConnected(){
        if(sock.isConnected()){
            return true;
        }else{
            return false;
        }
    }
    
    private void start(){
        proc.start();
    }
    
    private void stop(){
        proc.stop();
    }

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
