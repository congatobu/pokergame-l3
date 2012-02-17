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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shyzkanza
 */
public class Connection implements Runnable{
    // Activity courante
    public static final int     ACCUEUIL = 1;
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
        Log.v("Accueuil", "11111");
        try {
            // create new task
            theTask = new FutureTask<Object>(new Runnable() {
                public void run() {
                    try {
                        sock = new Socket(adresse, port);
                        br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                        proc = new Thread(this);
                        proc.start();
                        msgvaleur = new Message();
                        msgvaleur.obj = "CONNECT OK";
                        hand.sendMessage(msgvaleur);
                    } catch (Exception ex) {
                        
                    }
                }
            }, null);
            Log.v("Accueuil", "22222");
            // start task in a new thread
            new Thread(theTask).start();
            try {
                // wait for the execution to finish, timeout after 10 secs 
                theTask.get(4L, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (TimeoutException e) {
            msgvaleur = new Message();
            msgvaleur.obj = "TIMEOUT";
            hand.sendMessage(msgvaleur);
            return false;
        }
        return true;
    }
    
    public void say(String message) throws IOException{
        bw.write(message+"\n");
        bw.flush();
    }
    
    public boolean dispose(){
        try {
            proc.stop();
            br.close();
            bw.close();
            sock.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
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
                    msgvaleur.obj = ligne;
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
