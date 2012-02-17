/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

/**
 * Classe permettant d'analyser les message envoyé par le joueur et de vérifier les formats. (Pseudo, Mot de passe, texte)
 *
 * @author Jessy Bonnotte
 */
public class AnalyseurEnvoi {
    
    String caracChaine = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN123456789éèàâêîôûäëïöüù";
    int nbCar = 0;
    
    public AnalyseurEnvoi(){
        nbCar = caracChaine.length();
    }
    
    // Caractere autorisé : azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN123456789éèàâêîôûäëïöüù
    
    /**
     * Fonction servant a vérifier les caractères du pseudo.
     * 
     * @author Jessy Bonnotte
     * 
     * @param pseudo
     * 
     * @return 
     */
    public boolean analysePseudo(String pseudo){
        boolean retour = true;
        if(pseudo.length() > 1){
            for (int i = 0; i < pseudo.length() && retour; i++) {
                for (int j = 0; j < nbCar; j++) {
                    if (pseudo.charAt(i) == caracChaine.charAt(j)) {
                        retour = true;
                        break;
                    }else{
                        retour = false;
                    }
                }
            }
        }else{
            retour = false;
        }
        return retour;
    }
    
    /**
     * Fonction servant a vérifier les caractères du mot de passe.
     * 
     * @author Jessy Bonnotte;
     * 
     * @param password le mot de passe a vérifier.
     * 
     * @return true si le mot de passes contient des caractere correcte sinno false.
     */
    public boolean analyseMDP(String password){
        boolean retour = true;
        if(password.length() > 1){
            for (int i = 0; i < password.length() && retour; i++) {
                for (int j = 0; j < nbCar; j++) {
                    if (password.charAt(i) == caracChaine.charAt(j)) {
                        retour = true;
                        break;
                    }else{
                        retour = false;
                    }
                }
            }
        }else{
            retour = false;
        }
        return retour;
    }
}
