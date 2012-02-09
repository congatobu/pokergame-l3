/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.communication;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shyzkanza
 */
public class Connection implements Runnable{
    String adresse;
    int port;
    
    BufferedReader br;
    BufferedWriter bw;
    
    ListView list;
    
    Socket sock;
    
    Thread proc;
    
    private Handler hand;
    private Message msgvaleur;
    
    public Connection(){
        
    }
    
    public boolean init(String a, String p){
        adresse = a;
        port = Integer.parseInt(p);
        
        hand = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Communication.majListe(""+msg.obj);
            }
        };
        
        try {
            sock = new Socket(adresse, port);
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            proc = new Thread(this);
            proc.start();
        } catch (Exception ex) {
            return false;
        }
        try {
            say("<pseudo> Shyzkanza");
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception e) {
       	}
    }   
}
