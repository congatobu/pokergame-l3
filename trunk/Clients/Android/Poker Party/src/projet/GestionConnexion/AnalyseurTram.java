/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.poker.*;

/**
 *
 * @author Jessy Bonnotte
 */
public class AnalyseurTram {

    public AnalyseurTram(){
        
    }
    
    public void setTram(String tram, int currentActivity) throws IOException{
        Log.v("Reception", "Analyseur : "+tram+" "+currentActivity);
        
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
                Accueuil.finConnectCompte("Creation faite", true);
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
            }else if(tram.equals("OK")){
                Parametre.finOperation("Opération effectué");
            }
        }else if(currentActivity == Connection.LISTE_PARTIE){
            int index1 = 0;
            int index2 = tram.indexOf("@");
            
            List<String[]> listeArguments = new ArrayList<String[]>();
            //String[] argumentCourant = new String[3]; 
            
            String typeTram;
            
            if(index2 != -1){
                typeTram = tram.substring(index1, index2);
            }else{
                typeTram = new String(tram);
            }
            
            if(typeTram.equals("LISTEPARTIE") && index2 != -1){
                String tmp;
                do{
                    String[] argumentCourant = new String[3];
                    index1 = new Integer(index2);
                    index2 = tram.indexOf("@", index1 + 1);
                    if(index2 == -1){
                        tmp = tram.substring(index1 + 1, tram.length());
                    }else{
                        tmp = tram.substring(index1 + 1, index2);
                    }
                    int ind1 = 0;
                    int ind2 = 0;
                    for (int i = 0; i < 3; i++) {
                        ind2 = tmp.indexOf("/", ind1 +1);
                        if(ind2 != -1){
                            argumentCourant[i] = tmp.substring(ind1, ind2);
                        }else{
                            argumentCourant[i] = tmp.substring(ind1, tmp.length());
                        }
                        ind1 = new Integer(ind2 + 1);
                    }
                    listeArguments.add(argumentCourant.clone());
                }while(index2 != -1);
                ListePartie.MAJList(listeArguments);
            }else if(typeTram.equals("ERROR")){
                ListePartie.afficheMessage("Erreur de chargement");
            }else if(typeTram.equals("CREATPOK")){
                ListePartie.afficheMessage("CREATPOK");
            }else if(typeTram.equals("REJOK")){
                ListePartie.afficheMessage("REJOK");
            }else if(typeTram.equals("PAU")){
                ListePartie.afficheMessage("Nom deja utilise");
            }
        }else if(currentActivity == Connection.LISTE_JOUEUR_PARTIE){
            int index1 = 0;
            int index2 = tram.indexOf("@");
            
            List<String> listeArguments = new ArrayList<String>();
            //String[] argumentCourant = new String[3]; 
            
            String typeTram;
            
            if(index2 != -1){
                typeTram = tram.substring(index1, index2);
            }else{
                typeTram = new String(tram);
            }
            
            if(typeTram.equals("LISTEJOUEURSPARTIE") && index2 != -1){
                String tmp;
                do{
                    index1 = new Integer(index2);
                    index2 = tram.indexOf("@", index1 + 1);
                    
                    if(index2 == -1){
                        tmp = tram.substring(index1 + 1, tram.length());
                    }else{
                        tmp = tram.substring(index1 + 1, index2);
                    }

                    int ind1 = 0;
                    int ind2 = 0;
                    
                    listeArguments.add(tmp);
                }while(index2 != -1);
                ListeJoueur.MAJList(listeArguments);
            }else if(typeTram.equals("AREUREADY")){
                ListeJoueur.afficheMessage("AREUREADY");
            }else if(typeTram.equals("EXITOK")){
                ListeJoueur.afficheMessage("EXITOK");
            }
        }else if(currentActivity == Connection.PARTIE){
            int index1 = 0;
            int index2 = tram.indexOf("@");
            
            List<String[]> listeArguments = new ArrayList<String[]>();
            //String[] argumentCourant = new String[3]; 
            
            String typeTram;
            
            if(index2 != -1){
                typeTram = tram.substring(index1, index2);
            }else{
                typeTram = new String(tram);
            }
            
            if(typeTram.equals("DEBUTPARTIE")){
                
            }else if(typeTram.equals("JETONJ")){
                String tmp;
                do{
                    String[] argumentCourant = new String[3];
                    index1 = new Integer(index2);
                    index2 = tram.indexOf("@", index1 + 1);
                    if(index2 == -1){
                        tmp = tram.substring(index1 + 1, tram.length());
                    }else{
                        tmp = tram.substring(index1 + 1, index2);
                    }
                    int ind1 = 0;
                    int ind2 = 0;
                    for (int i = 0; i < 3; i++) {
                        ind2 = tmp.indexOf("/", ind1 +1);
                        if(ind2 != -1){
                            argumentCourant[i] = tmp.substring(ind1, ind2);
                        }else{
                            argumentCourant[i] = tmp.substring(ind1, tmp.length());
                        }
                        ind1 = new Integer(ind2 + 1);
                    }
                    listeArguments.add(argumentCourant.clone());
                }while(index2 != -1);
                TableauJeu.MAJLIST(TableauJeu.DONNEES_JOUEUR, listeArguments);
            }else if(typeTram.equals("CARTEM")){
                String[] argumentCourant = new String[2];
                
                index1 = new Integer(index2);
                index2 = tram.indexOf("@", index1 + 1);
                
                argumentCourant[0] = tram.substring(index1 + 1, index2);
                
                index1 = new Integer(index2);
                
                argumentCourant[1] = tram.substring(index1 + 1, tram.length());
                
                listeArguments.add(argumentCourant.clone());
                
                TableauJeu.MAJLIST(TableauJeu.CARTE_JOUEUR, listeArguments);
            }else if(typeTram.equals("CARTET")){
                String tmp;
                int i = 0;
                do{
                    String[] argumentCourant = new String[1];
                    index1 = new Integer(index2);
                    index2 = tram.indexOf("@", index1 + 1);
                    if(index2 == -1){
                        tmp = tram.substring(index1 + 1, tram.length());
                    }else{
                        tmp = tram.substring(index1 + 1, index2);
                    }
                    
                    argumentCourant[0] = tmp;
                    
                    listeArguments.add(argumentCourant.clone());
                    i++;
                }while(index2 != -1);
                TableauJeu.MAJLIST(TableauJeu.CARTE_TABLE, listeArguments);
                
            }else if(typeTram.equals("JETONT")){
                String[] argumentCourant = new String[1];
                index1 = new Integer(index2);

                argumentCourant[0] = tram.substring(index1 + 1, tram.length());
                listeArguments.add(argumentCourant.clone());
                TableauJeu.MAJLIST(TableauJeu.JETON_TABLE, listeArguments);
            }else if(typeTram.equals("MESSAGE")){
                String[] argumentCourant = new String[2];
                
                index1 = new Integer(index2);
                index2 = tram.indexOf("@", index1 + 1);    
                argumentCourant[0] = tram.substring(index1 + 1, index2);                
                index1 = new Integer(index2);               
                argumentCourant[1] = tram.substring(index1 + 1, tram.length());
                
                listeArguments.add(argumentCourant.clone());
                
                TableauJeu.MAJLIST(TableauJeu.MESSENGER, listeArguments);
            }else if(typeTram.equals("GAGNANTT")){
                String[] argumentCourant = new String[1];
                index1 = new Integer(index2);

                argumentCourant[0] = tram.substring(index1 + 1, tram.length());
                listeArguments.add(argumentCourant.clone());
                TableauJeu.MAJLIST(TableauJeu.FIN_TOUR, listeArguments);
            }else if(typeTram.equals("EXITOK")){
                
            }else if(typeTram.equals("MESSAGE")){
                
            }
        }        
    }
}
