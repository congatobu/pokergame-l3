/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import java.io.IOException;
import projet.poker.Accueuil;

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

    public CreateurTram(){
        
    }
    /**
 * crée la tram a envoyer au serveur en fonction du type envoyer par l'action du joueurs.
 * 
 * @author Jessy Bonnotte && Mathieu Polizzi
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
                Accueuil.connect.say(tram);
                break;
            case CREATECPT:
                tram += "CREATCPT";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueuil.connect.say(tram);
                break;
            case ACTUALISE_PASSWORD:
                tram += "ACTPASSWORD";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                tram += "@";
                tram += arg[2];
                Accueuil.connect.say(tram);
                break;
            case ACTUALISE_PSEUDO:
                tram += "ACTPSEUDO";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                tram += "@";
                tram += arg[2];
                Accueuil.connect.say(tram);
                break;
            case GET_LISTE_PARTIE_NOMPARTIE:
                tram += "GETLISTEPARTIE";
                tram += "@";
                tram += arg[0];
                Accueuil.connect.say(tram);
                break;  
            case CREATE_PARTIE:
                tram += "CREATEPARTIE";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueuil.connect.say(tram);
                break;
            case REJOINDRE_PARTIE:
                tram += "REJP";
                tram += "@";
                tram += arg[0];
                Accueuil.connect.say(tram);
                break;
            case MESSENGER:
                tram += "MESSAGE";
                tram += "@";
                tram += arg[0];
                tram += "@";
                tram += arg[1];
                Accueuil.connect.say(tram);
                break;             
            case JOUER://// CHOIX@3@NombreJeton
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                Accueuil.connect.say(tram);
                break;
            case CALL:
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                Accueuil.connect.say(tram);
                case FOLD:
                tram+="CHOIX";
                tram+="@";
                tram+=arg[0];
                tram+="@";
                tram+=arg[1];
                Accueuil.connect.say(tram);
        }
    }
    
   /**
    *crée la tram a envoyer au serveur en fonction du type envoyer par l'action du joueurs
    * 
    * @author Jessy Bonnotte && Mathieu Polizzi
    */
    
    public void setTram(int type) throws IOException{
        String tram = "";
        switch(type){
            case GET_LISTE_PARTIE:
                tram += "GETLISTEPARTIE";
                Accueuil.connect.say(tram);
                break;
            case DECONNECT:
                tram += "DECONNECT";
                Accueuil.connect.say(tram);
                break;
            case GET_PLAYER:
                tram += "GETPLAYERPARTY";
                Accueuil.connect.say(tram);
                break;
            case EXIT_PARTIE:
                tram += "EXITPARTIE";
                Accueuil.connect.say(tram);
                break;
            case PRET:
                tram += "IAMREADY";
                Accueuil.connect.say(tram);
                break;
            case DEBUT_PARTIE:
                tram += "DEBUTPARTIE";
                Accueuil.connect.say(tram);
                break;
            case CHECK:
                tram+="CHOIX";
                tram+="@";
                tram+="2";
                tram+="@";
                tram+="0";
                Accueuil.connect.say(tram);

                        

        }
    }
}
