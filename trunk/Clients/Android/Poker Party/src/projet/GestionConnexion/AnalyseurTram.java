/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.util.Log;
import java.io.IOException;
import projet.poker.Accueuil;

/**
 *
 * @author Jessy Bonnotte
 */
public class AnalyseurTram {
    public static final int OK = 1;
    public static final int WPSEUDO = 2;
    public static final int WPASS = 3;
    public static final int TIMEOUT = 4;
    
    
    public AnalyseurTram(){
        
    }
    
    public void setTram(String tram, int currentActivity) throws IOException{
        Log.v("Accueuil", "Analyseur : "+tram+" "+currentActivity);
        if(tram.equals("TIMEOUT") && currentActivity == Connection.ACCUEUIL){
            Accueuil.finConnectCompte("Connexion TimeOut", false);
        }else if(tram.equals("CONNECT OK") && currentActivity == Connection.ACCUEUIL){
            Accueuil.finConnectCompte("Connexion etabli", true);
        }else if(tram.equals("WPSEUDO") && currentActivity == Connection.ACCUEUIL){
            Accueuil.finConnectCompte("Pseudo inexistant", false);
        }else if(tram.equals("WPASS") && currentActivity == Connection.ACCUEUIL){
            Accueuil.finConnectCompte("Mot de passe invalide", false);
        }
    }
}
