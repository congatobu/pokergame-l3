/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import java.io.IOException;
import projet.poker.Accueuil;

/**
 *
 * @author Shyzkanza
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
    
    public CreateurTram(){
        
    }
    
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
        }
    }
    
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
        }
    }
}
