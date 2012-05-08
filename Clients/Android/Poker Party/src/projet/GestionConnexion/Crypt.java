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
    
    private final String            _cle = "12gh5yt68oi39";
    private int                     _i;
    private int                     _j;
    private String                  _retour;
    
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
        _i = 0;
        _j = 0;
        _retour = "";
        
        while(_i < tram.length()){
            _retour += (char)((int)tram.charAt(_i) + (int)_cle.charAt(_j));
            
            _i++;
            _j++;
            
            if(_j == _cle.length()){
                _j=0;
            }
        }
        return _retour;
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
        _i = 0;
        _j = 0;
        _retour = "";
        
        while(_i < tram.length()){
            _retour += (char)((int)tram.charAt(_i) - (int)_cle.charAt(_j));
            
            _i++;
            _j++;
            
            if(_j == _cle.length()){
                _j=0;
            }
        }
        return _retour;
    }
}
