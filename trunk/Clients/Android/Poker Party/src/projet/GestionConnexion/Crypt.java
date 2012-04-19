/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.GestionConnexion;

/**
 * Classe permmettant de crypter les informations à envoyer au serveur.
 * 
 * @author Jessy Bonnotte
 */
public class Crypt {
    
    private final String cle = "12gh5yt68oi39";
    private int i;
    private int j;
    private String retour;
    
    /**
     * Fonction permettant de crypter la tram a envoyer.
     * 
     * @author Jessy Bonnotte
     * 
     * @param tram la tram a encrypter
     * 
     * @return String - la tram a envoyer encrypté
     */
    public String enCrypt(String tram){
        i = 0;
        j = 0;
        retour = "";
        
        while(i < tram.length()){
            retour += (char)((int)tram.charAt(i) + (int)cle.charAt(j));
            
            i++;
            j++;
            
            if(j == cle.length()){
                j=0;
            }
        }
        return retour;
    }
    
    /**
     * Fonction permettant le décryptage. Elle prend en parametre la tram reçu par le joueur.
     * 
     * @author Jessy Bonnotte
     * 
     * @param tram reçu du client
     * 
     * @return String - la tram reçu décrypté
     */
    public String deCrypt(String tram){
        i = 0;
        j = 0;
        retour = "";
        
        while(i < tram.length()){
            retour += (char)((int)tram.charAt(i) - (int)cle.charAt(j));
            
            i++;
            j++;
            
            if(j == cle.length()){
                j=0;
            }
        }
        return retour;
    }
}
