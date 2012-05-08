/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.util.Log;
import java.io.IOException;
import projet.poker.Accueil;

/**
 * Classe permettant de créer les trams à envoyer au serveur.
 * 
 * @author Jessy Bonnotte && Mathieu Polizzi
 */
public class CreateurTram {
    public static final int CONNECT = 1;
    public static final int CREATECPT = 2;
    public static final int ACTUALISE_PASSWORD = 3;
    public static final int ACTUALISE_PSEUDO = 4;
    public static final int GET_LISTE_PARTIE = 5;
    public static final int GET_LISTE_PARTIE_NOMPARTIE = 6;
    public static final int DECONNECT = 7;
    public static final int CREATE_PARTIE = 8;
    public static final int GET_PLAYER = 9;
    public static final int EXIT_PARTIE = 10;
    public static final int REJOINDRE_PARTIE = 11;
    public static final int PRET = 12;
    public static final int DEBUT_PARTIE = 13;
    public static final int MESSENGER = 14;
    public static final int JOUER = 15;
    public static final int CHECK= 16;
    public static final int CALL= 17;
    public static final int FOLD= 18;

    /**
     * Constructeur du créateur de tram
     * 
     * @author Jessy bonnotte
     */
    public CreateurTram(){
        
    }
    
    /**
     * Fonction permettant de créer des tram avec paramètre. l'utilisateur choisis le type de tram et donnes les parametre et la tram est ensuite créé puis envoyé au serveur.
     * 
     * @author Jessy bonnotte
     * 
     * @param type - le type de tram à creer
     * @param arg - les arguments à passé à la tram
     * @param nbArg - le nombre d'arguments passé à la tram
     * 
     * @throws IOException - exception levé s'il y a un problème lors de l'envoi
     */
    public void setTram(int type, String[] arg, int nbArg) throws IOException{
        String tram = "";
        switch(type){
            case CONNECT:
                tram += "CONNECT";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueil.connexion.say(tram);
                break;
            case CREATECPT:
                tram += "CREATCPT";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueil.connexion.say(tram);
                break;
            case ACTUALISE_PASSWORD:
                tram += "ACTPASSWORD";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                tram += "@";
                tram += arg[2];
                Accueil.connexion.say(tram);
                break;
            case ACTUALISE_PSEUDO:
                tram += "ACTPSEUDO";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                tram += "@";
                tram += arg[2];
                Accueil.connexion.say(tram);
                break;
            case GET_LISTE_PARTIE_NOMPARTIE:
                tram += "GETLISTEPARTIE";
                tram += "@";
                tram += arg[0];
                Accueil.connexion.say(tram);
                break;  
            case CREATE_PARTIE:
                tram += "CREATEPARTIE";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueil.connexion.say(tram);
                break;
            case REJOINDRE_PARTIE:
                tram += "REJP";
                tram += "@";
                tram += arg[0];
                Accueil.connexion.say(tram);
                break;
            case MESSENGER:
                tram += "MESSAGE";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueil.connexion.say(tram);
                break;             
            case JOUER://// CHOIX@3@NombreJeton
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                 Log.v("createur","createur : "+tram);
                Accueil.connexion.say(tram);
                break;
            case CALL:
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                Log.v("createur","createur : "+tram);
                Accueil.connexion.say(tram);
                break;
            case FOLD:
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                Log.v("createur","createur : "+tram);
                Accueil.connexion.say(tram);
                break;
        }
    }
    
   /**
    * Focntion permettant de créer une tram simple et l'envoi ensuite au serveur
    * 
    * @author Jessy Bonnotte && Mathieu Polizzi
    *
    * @param type - le type de tram à créer
    */
    public void setTram(int type) throws IOException{
        String tram = "";
        switch(type){
            case GET_LISTE_PARTIE:
                tram += "GETLISTEPARTIE";
                Accueil.connexion.say(tram);
                break;
            case DECONNECT:
                tram += "DECONNECT";
                Accueil.connexion.say(tram);
                break;
            case GET_PLAYER:
                tram += "GETPLAYERPARTY";
                Accueil.connexion.say(tram);
                break;
            case EXIT_PARTIE:
                tram += "EXITPARTIE";
                Accueil.connexion.say(tram);
                break;
            case PRET:
                tram += "IAMREADY";
                Accueil.connexion.say(tram);
                break;
            case DEBUT_PARTIE:
                tram += "DEBUTPARTIE";
                Accueil.connexion.say(tram);
                break;
            case CHECK:
                tram+="CHOIX";
                tram+="@";
                tram+="2";
                tram+="@";
                tram+="0";
                Accueil.connexion.say(tram);
                break;
        }
    }
}
