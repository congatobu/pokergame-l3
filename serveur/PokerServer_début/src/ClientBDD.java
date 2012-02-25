
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Classe permettant de gérer la base de données joueur. Cette base de données<br>
 * contient les infos relatives aux joueurs inscrits.
 * 
 * @author Jessy Bonnotte, @author Benjamin Maurin
 */
public class ClientBDD {
    private Document document;
    private Element racine;
    private Dateur date;
    private Semaphore available = new Semaphore(1, true);
    /**
     * Fonction permettant d'ouvrir le fichier. Elle initialise la variable
     * document avec le fichier XML complet et racine avec l'element racine du
     * document XML.
     * 
     * @author Jessy Bonnotte
     */
    public void Ouverture(){
        SAXBuilder sxb = new SAXBuilder();
        try{
            document = sxb.build(new File("BDD\\Client.xml"));
        }catch (Exception e){

        }
        racine = document.getRootElement();
    }

    /**
     * Enregistre le fichier XML.
     * 
     * @author Jessy Bonnotte
     */
    private void Enregistre(){
        try{
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, new FileOutputStream("BDD\\Client.xml"));
        }catch (java.io.IOException e){
            System.err.println(e.toString());
        }
    }
    
    /**
     * Fonction permettant d'ajouter un joueur dans la base de données. Elle prend en parametre un pseudo et un password.<br> 
     * La date du jour de création est ajouté automatiquement pour chaque utilisateur créé.<br><br>
     * 
     * Retourne :<br>
     * <ul>
     * <li> <b>CREATOK</b> : opération effectué </li>
     * <li> <b>AUPSEUDO</b> : pseudo déjà utilisé, ajout impossible </li>
     * <li> <b>ERREURBDD</b> : erreur a l'ajout, veuillez recommencer </li>
     * <li> <b>WFPSEUDO</b> : mauvais format de pseudo </li>
     * <li> <b>WFPASS</b> : mauvais format de password </li>
     * </ul>
     * @author Maurin Benjamin
     * @author Jessy Bonnotte
     * 
     * @param pseudo pseudo du joueur a ajouter dans la base
     * @param password mot de passe du jour a ajouter dans la base
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String ajouteJoueur(String pseudo, String password){
        Ouverture();
        try{
         available.acquire();
        }
         catch(Exception e){
            e.printStackTrace();
        }
        if(!verifFormat(pseudo)){ 
             available.release();
            return "WFPSEUDO";}
        if(!verifFormat(password)){ available.release();return "WFPASS";}
        if(!verifPseudo(pseudo)){
          available.release();  return "AUPSEUDO";
        }
        try{
            date = new Dateur();
            Element enfant = new Element("joueur");
            enfant.setAttribute("numero", getNumeroJoueur());
            enfant.setAttribute("pseudo", pseudo);

            Element temp;

            temp = new Element("password");
            temp.setText(password);
            enfant.addContent(temp);
            
            temp = new Element("dateInscription");
            temp.setText(date.getDate());
            enfant.addContent(temp);
            
            temp = new Element("partieGagne");
            temp.setText("0");
            enfant.addContent(temp);

            temp = new Element("partiePerdu");
            temp.setText("0");
            enfant.addContent(temp);
            
            racine.addContent(enfant);
            Enregistre();
        }catch(Exception e){
         available.release();   return "ERREURBDD";
        }
    available.release();    return "CREATOK";
    }
    
    /**
     * Fonction permettant de récupérer les informations relativent à un joueur passé en paramètre.<br><br>
     * 
     * retourne :<br>
     * <ul>
     * <li> <b>tab[0]</b> : numero unique du joueur </li>
     * <li> <b>tab[1]</b> : date d'inscription du joueur </li>
     * <li> <b>tab[2]</b> : nombre de partie gagné par le joueur </li>
     * <li> <b>tab[3]</b> : nombre de partie perdu par le joueur </li>
     * </ul>
     * 
     * @author Jessy Bonnotte
     * 
     * @param pseudo le pseudo du joueur dont on souhaite les informations
     * 
     * @return {@code String[]} tableau de String contanant les info d'un joueur
     */
    public String[] getInfo(String pseudo){
        String[] retour = new String[4];
        retour[0] = "0";
        
        List element = racine.getChildren("joueur");
        Element temp;
        
        for (int i = 0; i < element.size(); i++) {
            temp = (Element) element.get(i);
            if(temp.getAttributeValue("pseudo").equals(pseudo)){
                retour[0] = temp.getAttributeValue("numero");
                retour[1] = temp.getChildText("dateInscription");
                retour[2] = temp.getChildText("partieGagne");
                retour[3] = temp.getChildText("partiePerdu");
                break;
            }
        }
        return retour;
    }
    
    /**
     * Fonction permettant de vérifier les informations de connexion du joueur.<br><br>
     * 
     * Retourne :<br>
     * <ul>
     * <li> <b>CONNECTOK</b> : opération effectué </li>
     * <li> <b>WPASS</b> : mauvais password utilisé, connexion impossible </li>
     * <li> <b>WPSEUDO</b> : pseudo introuvable, connexion impossible </li>
     * </ul>
     * 
     * @author Jessy Bonnotte @author Maurin Benjamin
     * 
     * @param pseudo pseudo du joueur voulant se connecter
     * @param motDePasse mot de passe du joueur voulant se connecter
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String verifPassword(String pseudo, String motDePasse){
        List element = racine.getChildren("joueur");
        Element temp;
        
        for (int i = 0; i < element.size(); i++) {
            temp = (Element) element.get(i);
            if(temp.getAttributeValue("pseudo").equals(pseudo)){
                if(temp.getChildText("password").equals(motDePasse)){
                    return "CONNECTOK";
                }else{
                    return "WPASS";
                }
            }
        }
        return "WPSEUDO";
    }
    
    /**
     * Fonction permettant d'incrémenter de 1 le nombre de partie gagnées.<br>
     * La fonction prend en paramètre le pseudo du joueur en question.
     * 
     * @author Jessy bonnotte
     * @author Benjamin Maurin
     * @param pseudo le pseudo du joueur qui a gagné
     */
    public void gagnePartie(String pseudo){
        List listeJoueur = racine.getChildren("joueur");
        Iterator i = listeJoueur.iterator();
          try {
            available.acquire();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
        while(i.hasNext()){
            Element courant = (Element) i.next();
            if(courant.getAttributeValue("pseudo").equals(pseudo)){
                courant.getChild("partieGagne").setText(String.valueOf(Integer.parseInt(courant.getChildText("partieGagne")+1)));
                Enregistre();
                break;
            }
        }
        available.release();
    }
    
    /**
     * Fonction permettant d'incrémenter de 1 le nombre de partie perdues.<br>
     * La fonction prend en paramètre le pseudo du joueur en question.
     * 
     * @author Jessy bonnotte
     * @author Benjamin Maurin
     * @param pseudo le pseudo du joueur qui a perdu
     */
    public void perdPartie(String pseudo){
        List listeJoueur = racine.getChildren("joueur");
        Iterator i = listeJoueur.iterator();
        try {
            available.acquire();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
        
        while(i.hasNext()){
            Element courant = (Element) i.next();
            if(courant.getAttributeValue("pseudo").equals(pseudo)){
                courant.getChild("partiePerdu").setText(String.valueOf(Integer.parseInt(courant.getChildText("partiePerdu")+1)));
                Enregistre();
                break;
            }
        }
        available.release();
    }
      
    /**
     * Fonction permmettant de changer le mot de passe.<br> 
     * La fonction prend le pseudonyme ainsi que le mot de passe actuel puis le nouveau mot de passe.<br><br>
     * 
     * Retourne :<br>
     * <ul>
     * <li> <b>OK</b> : opération effectué </li>
     * <li> <b>WPASS</b> : mauvais password utilisé, changement impossible </li>
     * <li> <b>WPSEUDO</b> : pseudo introuvable, changement impossible </li>
     * <li> <b>WFPASS</b> : mauvais format de nouveau password </li>
     * </ul>
     *  
     * @author Jessy Bonnotte @author Maurin Benjamin
     * 
     * @param pseudo le pseudo du joueur
     * @param ancienMotDePasse l'ancien mot de passe du joueur
     * @param nouveauMotDePasse le nouveau mot de passe du joueur
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String changeMotDePasse(String pseudo, String ancienMotDePasse, String nouveauMotDePasse){
        List element = racine.getChildren("joueur");
        Element temp;
        
        try {
            available.acquire();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
        
        if(!verifFormat(nouveauMotDePasse)){  available.release();return "WFPASS";}
        
        for (int i = 0; i < element.size(); i++) {
            temp = (Element) element.get(i);
            if(temp.getAttributeValue("pseudo").equals(pseudo)){
                if(temp.getChildText("password").equals(ancienMotDePasse)){  
                    temp.getChild("password").setText(nouveauMotDePasse);
                    Enregistre();
                     available.release();
                    return "OK";

                }else{
                     available.release();
                    return "WPASS";
                }
            }
        }
         available.release();
        return "WPSEUDO";
    }
    
    /**
     * Fonction permettant de changer de pseudo.<br> 
     * Elle prend en parametre le pseudo et le mot de passe actuel ainsi que le nouveau pseudo.<br><br>
     * 
     * Retourne :<br>
     * <ul>
     * <li> <b>OK</b> : opération effectué </li>
     * <li> <b>WPASS</b> : mauvais password utilisé, changement impossible </li>
     * <li> <b>WPSEUDO</b> : pseudo introuvable </li>
     * <li> <b>AUPSEUDO</b> : pseudo déjà utilisé </li>
     * <li> <b>WFPSEUDO</b> : mauvais format de pseudo </li>
     * </ul>
     * @author Maurin Benjamin
     * @author Jessy Bonnotte
     * 
     * @param ancienPseudo le pseudo a changer
     * @param motDePasse le mot de passe du joueur
     * @param nouveauPseudo le nouveau pseudo
     * 
     * @return {@code String} contenant le resultat de l'opération
     */
    public String changePseudo(String ancienPseudo, String motDePasse, String nouveauPseudo){
        List element = racine.getChildren("joueur");
        Element temp;
        
              try {
            available.acquire();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
              
          if(!verifFormat(nouveauPseudo)){  available.release(); return "WFPSEUDO";}
        
        for (int i = 0; i < element.size(); i++) {
            temp = (Element) element.get(i);
            if(temp.getAttributeValue("pseudo").equals(ancienPseudo)){
                if(temp.getChildText("password").equals(motDePasse)){
                    if(verifPseudo(nouveauPseudo)){
                  
                        temp.setAttribute("pseudo", nouveauPseudo);
                        Enregistre();
                          available.release();
                        return "OK";
                    
                    }else{
                        available.release();  return "AUPSEUDO";
                    }
                }else{
                    available.release();  return "WPASS";
                }
                    
            }
        }
          available.release();
        return "WPSEUDO";
    }
    
    /**
     * Fonction permettant d'effacer la base
     */
    public void effaceBase(){
        racine.removeContent();
        Enregistre();
    }
    
    /**
     * Fonction permettant d'effacer un joueur de la base de données.<br>
     * Elle prend en parametre le pseudo du joueur a supprimer.<br><br>
     * 
     * Retourne :<br>
     * <ul>
     * <li> <b>OK</b> : opération effectué </li>
     * <li> <b>WPASS</b> : mauvais password utilisé, suppression impossible </li>
     * <li> <b>WPSEUDO</b> : pseudo introuvable, suppression impossible </li>
     * </ul>
     * @author Maurin Benjamin
     * @author Jessy Bonnotte
     * 
     * @param pseudo le pseudo du joueur a supprimer
     * 
     * @return {@code String} le résultats de l'opération
     */
    public String effaceJoueur(String pseudo, String password){
        List listeJoueur = racine.getChildren("joueur");
        Iterator i = listeJoueur.iterator();
        
      try {
            available.acquire();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }
        
        while(i.hasNext()){
            Element courant = (Element) i.next();
            if(courant.getAttributeValue("pseudo").equals(pseudo)){
                if(courant.getChildText("password").equals(password)){
                    racine.removeContent(courant);
                    Enregistre();
                  available.release();  return "OK";
                }else{
                  available.release();  return "WPASS";
                }  
            }
        }
        available.release();
        return "WPSEUDO"; 
    }
    
    /**
     * Fonction permettant de récupérer le nombre de joueur inscrits dans la base de données.
     * 
     * @return {@code integer} le nombre de joueurs inscrits dans la base de données
     */
    public int getNombreJoueur(){
        return racine.getChildren().size();
    }
    
    
    
    
      /**
     * Fonction permettant de vérifier qu'un pseudo/password/partie est dans un bon format.<br>
     * 
     *
     * @author Benjamin Maurin
     * 
     * @param s le pseudo/password/partie à vérifier
     * 
     * @return {@code boolean} retourne vrai si le format est correct
     */
    public boolean verifFormat(String s){
	String bonnes_lettres = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789éèàâêîôûäëïöüù";
        String A_Test = s;
        if(s.length()<3 || s.length()>15)
        {
            return false;
        }
	while(A_Test.length()>0)
	{
		if(bonnes_lettres.indexOf(A_Test.substring(0,1))==-1)
		{
			return false;
		}
		A_Test=A_Test.substring(1);
	}
	
	return true;
}
  
    
    
    
    
    /**
     * Fonction permettant de vérifier qu'un pseudo est présent dans la base.<br>
     * elle prend en paramtre le pseudo du joueur à chercher.
     * 
     * @author Jessy Bonnotte
     * 
     * @param s le pseudo du joueur a rechercher
     * 
     * @return {@code boolean} retourne vrai si le pseudo n'est pas présent sinon faux
     */
    private boolean verifPseudo(String s){
        List element = racine.getChildren("joueur");
        Element temp;
        
        for (int i = 0; i < element.size(); i++) {
            temp = (Element) element.get(i);
            if(temp.getAttributeValue("pseudo").equals(s)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Fonction permettant de générer un identifiant unique pour chaque jours dans la base de données.
     * 
     * @author Jessy Bonnotte
     * 
     * @return {@code String} retournant le premier numéro libre suivant le dernier dans la base de données.
     */
    private String getNumeroJoueur(){
        String ret;
        List element = racine.getChildren("joueur");
        try{
            Element temp = (Element)element.get(element.size()-1);
            ret = String.valueOf(Integer.parseInt(temp.getAttributeValue("numero")) + 1);
        }catch(Exception e){
            ret = "1";
        }
        return ret;
    }
    
    
      /**
     * Fonction pour optimiser la suppression de l'objet
     * @author Benjamin Maurin
     * @throws Throwable 
     */
    @Override
    protected void finalize() throws Throwable{
        try {
      document = null;
      racine = null;
      date = null;
        } catch(Exception e) {
        
        }
        finally {
            super.finalize();
        }
    }
    
    
}
