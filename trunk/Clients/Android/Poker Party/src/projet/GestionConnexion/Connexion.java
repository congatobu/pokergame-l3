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
public class Connexion implements Runnable{
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
    private String              _adresse;
    private int                 _port;
    
    // Gestion des flux de lecture et d'écriture
    private BufferedReader      _buffuredReader;
    private BufferedWriter      _buffuredWriter;
    
    // La socket de comunication
    private Socket              _socket;
    
    // Le thread d'écoute
    private Thread              _processus;
    
    // Comunication entre les thread
    private Handler             _handler;
    private Message             _messageHandler;
    
    // Décriptage de la tram
    private Crypt               _crypteur;
    
    // Analyse de la tram
    private AnalyseurTram       _analyseurTram;
 
    /**
     * Constructeur classe de connexion.
     * 
     * @author Jessy Bonnotte
     */
    public Connexion(){
        
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
        _adresse = a;
        _crypteur = new Crypt();
        _port = Integer.parseInt(p);
        _analyseurTram = new AnalyseurTram();

        _handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                try {
                    _analyseurTram.setTram(msg.obj.toString(), currentActivity);
                } catch (IOException ex) {
                    Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        try {
            Log.v("connexion", "avant la socket : "+_adresse+"  "+_port);

            //sock = new Socket(adresse, port);

            Log.v("Parametre", "avant socket");
            _socket = new Socket();
            _socket.connect(new InetSocketAddress(_adresse, _port), 5000);
            Log.v("Parametre", "apres socket");
            if(!_socket.isConnected()){
                _messageHandler = new Message();
                _messageHandler.obj = "TIMEOUT";
                _handler.sendMessage(_messageHandler);
                return false;
            }
            _buffuredReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _buffuredWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            _processus = new Thread(this);
            _processus.start();                   
            /*}catch (IOException e) {

            }*/
            return true;
        } catch (UnknownHostException ex) {
            Log.v("Parametre", "ici");
            _messageHandler = new Message();
            _messageHandler.obj = "ERREUR";
            _handler.sendMessage(_messageHandler);

            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        } catch (IOException ex) {
            Log.v("Parametre", ex.getMessage());
            _messageHandler = new Message();
            _messageHandler.obj = "ERREUR";
            _handler.sendMessage(_messageHandler);

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
        _buffuredWriter.write(/*cryptTram.enCrypt(*/message/*)*/+"\n");
        _buffuredWriter.flush();
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
            _socket.close();
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
        if(_socket.isConnected()){
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
        _processus.start();
    }
    
    /**
     * Fonction permettant de fermer le processus d'ecoute.
     * 
     * @author Jessy Bonnotte
     */
    private void stop(){
        _processus.stop();
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
            while ((ligne = _buffuredReader.readLine()) != null) {
                if(!ligne.equals("")){
                    _messageHandler = new Message();
                    _messageHandler.obj = /*cryptTram.deCrypt(*/ligne/*)*/;
                    _handler.sendMessage(_messageHandler);
                }
            }
            _messageHandler = new Message();
            _messageHandler.obj = "CONNEXION CLOSE";
            _handler.sendMessage(_messageHandler);
            dispose();
        } catch (Exception e) {
            
       	}
    }   
}
