/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.util.Log;
import java.io.IOException;
import projet.poker.Accueuil;
import projet.poker.Parametre;

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
        
        if(tram.equals("MARCO")){
            Accueuil.connect.say("POLO");
        }else if(currentActivity == Connection.ACCUEUIL){
            if(tram.equals("ERREUR")){
                Accueuil.finConnectCompte("Erreur de connexion", false);
            }else if(tram.equals("TIMEOUT")){
                Parametre.finOperation("Connexion timeOut");
            }else if(tram.equals("CONNECTOK")){
                Accueuil.finConnectCompte("Connexion etabli", true);
            }else if(tram.equals("CREATOK")){
                Accueuil.finConnectCompte("Connexion etabli", true);
            }else if(tram.equals("WPSEUDO")){
                Accueuil.finConnectCompte("Pseudo inexistant", false);
            }else if(tram.equals("WPASS")){
                Accueuil.finConnectCompte("Mot de passe invalide", false);
            }else if(tram.equals("WFPSEUDO")){
                Accueuil.finConnectCompte("Mauvais format pseudo", false);
            }else if(tram.equals("AUPSEUDO")){
                Accueuil.finConnectCompte("Pseudo deja utilisé", false);
            }else if(tram.equals("WFPASS")){
                Accueuil.finConnectCompte("Mauvais format password", false);
            }else if(tram.equals("ERREURBDD")){
                Accueuil.finConnectCompte("Ajout impossible", false);
            }else if(tram.equals("CONNEXION CLOSE")){
                Accueuil.connect.dispose();
            }
        }else if(currentActivity == Connection.PARAMETRE){
            if(tram.equals("ERREUR")){
                Parametre.finOperation("Erreur de connexion");
            }else if(tram.equals("TIMEOUT")){
                Parametre.finOperation("Connexion timeOut");
            }else if(tram.equals("CONNECTOK")){
                Parametre.finOperation("Connexion etabli");
            }else if(tram.equals("WPSEUDO")){
                Parametre.finOperation("Pseudo inexistant");
            }else if(tram.equals("WPASS")){
                Parametre.finOperation("Mot de passe invalide");
            }else if(tram.equals("WFPSEUDO")){
                Parametre.finOperation("Mauvais format pseudo");
            }else if(tram.equals("AUPSEUDO")){
                Parametre.finOperation("Pseudo deja utilisé");
            }else if(tram.equals("WFPASS")){
                Parametre.finOperation("Mauvais format password");
            }else if(tram.equals("ERREURBDD")){
                Parametre.finOperation("Ajout impossible");
            }else if(tram.equals("CONNEXION CLOSE")){
                Accueuil.connect.dispose();
            }
        }        
    }
}
