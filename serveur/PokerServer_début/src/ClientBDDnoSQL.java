package pokerPackage;



import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


    
    /**
 * Classe permettant de gérer la base de données joueur. Cette base de données<br>
 * contient les infos relatives aux joueurs inscrits.
 * 
 *@author Benjamin Maurin
 */
public class ClientBDDnoSQL {

    //collection
    private DBCollection coll;
    private DB db;
    
    
    /**
     * Fonction pour se connecter à la base de données et choisir la collection
     * @author Maurin Benjamin
     */
    public void Ouverture(){
            //connection
Mongo m = null;
        try {
            m = new Mongo( "localhost" , 27017 );
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ClientBDDnoSQL.class.getName()).log(Level.SEVERE, null, ex);
        }


        //choix de la bdd
db = m.getDB("pokerBDD");
//choix de la collection (environ table)
 coll = db.getCollection("pokerCollection");


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
     * 
     * @param pseudo pseudo du joueur a ajouter dans la base
     * @param password mot de passe du jour a ajouter dans la base
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String ajouteJoueur(String pseudo, String password){
       
 
        if(!verifFormat(pseudo)){ 
            return "WFPSEUDO";}
        if(!verifFormat(password)){ return "WFPASS";}
        if(!verifPseudo(pseudo)){
          return "AUPSEUDO";
        }
        try{
            Dateur date = new Dateur();
            
            
            BasicDBObject doc = new BasicDBObject();

        doc.put("numero", getNumeroJoueur());
        doc.put("pseudo", pseudo);
        doc.put("password", password);
        doc.put("dateInscription", date.getDate());
        doc.put("partieGagne", "0");
        doc.put("partiePerdu", "0");
            
        coll.insert(doc);
        }catch(Exception e){
          return "ERREURBDD";
        }
        return "CREATOK";
    }
    
    /**
     * Fonction permettant de récupérer les informations relativent à un joueur passé en paramètre.<br><br>
     * 
     * retourne :<br>
     * <ul>
     * <li> <b>tab[0]</b> : numero unique du joueur (0 si le joueur n'existe pas) </li>
     * <li> <b>tab[1]</b> : date d'inscription du joueur </li>
     * <li> <b>tab[2]</b> : nombre de partie gagné par le joueur </li>
     * <li> <b>tab[3]</b> : nombre de partie perdu par le joueur </li>
     * </ul>
     * 
     * @author Maurin Benjamin
     * 
     * @param pseudo le pseudo du joueur dont on souhaite les informations
     * 
     * @return {@code String[]} tableau de String contanant les info d'un joueur
     */
    public String[] getInfo(String pseudo){
        String[] retour = new String[4];
        retour[0] = "0";
        
        DBCursor cur; 
        DBObject courant;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
                
                
        cur = coll.find(query);

        while(cur.hasNext()) {
            courant = cur.next();
                retour[0] = (String) courant.get("numero");
                retour[1] = (String) courant.get("dateInscription");
                retour[2] = (String) courant.get("partieGagne");
                retour[3] = (String) courant.get("partiePerdu");
                break;
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
     * @author Maurin Benjamin
     * 
     * @param pseudo pseudo du joueur voulant se connecter
     * @param motDePasse mot de passe du joueur voulant se connecter
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String verifPassword(String pseudo, String motDePasse){
          DBCursor cur; 
        DBObject courant;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
        cur = coll.find(query);
        
       while(cur.hasNext()) {
            courant = cur.next();
            if(courant.get("password").equals(motDePasse)){
                
                    return "CONNECTOK";
                }else{
                    return "WPASS";
                }
            }
        return "WPSEUDO";
    }
    
    /**
     * Fonction permettant d'incrémenter de 1 le nombre de partie gagnées.<br>
     * La fonction prend en paramètre le pseudo du joueur en question.
     * 
     * @author Benjamin Maurin
     * @param pseudo le pseudo du joueur qui a gagné
     */
    public void gagnePartie(String pseudo){
      
  
                DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
        cur = coll.find(query);
       String change = null;
       
        //récupération ancien nombre
       while(cur.hasNext()) {
           courant = cur.next();
           change = (String) courant.get("partieGagne");
       }
       //incrémentation
       change = String.valueOf(Integer.parseInt(change)+1);
       
       //update
        BasicDBObject newDocument = new BasicDBObject().append("$set",new BasicDBObject().append("partieGagne", change));
	coll.update(new BasicDBObject().append("pseudo",pseudo), newDocument);
       
            
   
    }
    
    /**
     * Fonction permettant d'incrémenter de 1 le nombre de partie perdues.<br>
     * La fonction prend en paramètre le pseudo du joueur en question.
     * 
     * @author Benjamin Maurin
     * @param pseudo le pseudo du joueur qui a perdu
     */
    public void perdPartie(String pseudo){
              DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
        cur = coll.find(query);
       String change = null;
       
        //récupération ancien nombre
       while(cur.hasNext()) {
           courant = cur.next();
           change = (String) courant.get("partiePerdu");
       }
       //incrémentation
       change = String.valueOf(Integer.parseInt(change)+1);
       
       //update
        BasicDBObject newDocument = new BasicDBObject().append("$set",new BasicDBObject().append("partiePerdu", change));
	coll.update(new BasicDBObject().append("pseudo",pseudo), newDocument);
       
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
     * @author Maurin Benjamin
     * 
     * @param pseudo le pseudo du joueur
     * @param ancienMotDePasse l'ancien mot de passe du joueur
     * @param nouveauMotDePasse le nouveau mot de passe du joueur
     * 
     * @return {@code String} contenant le résultat de l'opération
     */
    public String changeMotDePasse(String pseudo, String ancienMotDePasse, String nouveauMotDePasse){

        
        
        if(!verifFormat(nouveauMotDePasse)){ return "WFPASS";}
        
        
                DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
        cur = coll.find(query);
      
       
     
       
       while(cur.hasNext()) {
           courant = cur.next();
           if(courant.get("password").equals(ancienMotDePasse)){
               
               //update
            BasicDBObject newDocument = new BasicDBObject().append("$set",new BasicDBObject().append("password", nouveauMotDePasse));
            coll.update(new BasicDBObject().append("pseudo",pseudo), newDocument);
               
                    return "OK";

                }else{
                    return "WPASS";}

       }
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
     * 
     * @param ancienPseudo le pseudo a changer
     * @param motDePasse le mot de passe du joueur
     * @param nouveauPseudo le nouveau pseudo
     * 
     * @return {@code String} contenant le resultat de l'opération
     */
    public String changePseudo(String ancienPseudo, String motDePasse, String nouveauPseudo){
     
              
           
        //verifie format
        if(!verifFormat(nouveauPseudo)){ return "WFPSEUDO";}
        
        
                DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", ancienPseudo);
        cur = coll.find(query);
      
       
     
       
       while(cur.hasNext()) {
           courant = cur.next(); // si bon mdp
           if(courant.get("password").equals(motDePasse)){

         
                    if(verifPseudo(nouveauPseudo)){ // si le nouveau pseudo n'existe pas
                  
                       
                         //update
            BasicDBObject newDocument = new BasicDBObject().append("$set",new BasicDBObject().append("pseudo", nouveauPseudo));
            coll.update(new BasicDBObject().append("pseudo",ancienPseudo), newDocument);
                        
                        
                        return "OK";
                    
                    }else{
                         return "AUPSEUDO";
                    }
                }else{
                    return "WPASS";
                }
                    
            }

        return "WPSEUDO";
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
     * 
     * @param pseudo le pseudo du joueur a supprimer
     * 
     * @return {@code String} le résultats de l'opération
     */
    public String effaceJoueur(String pseudo, String password){
        

                DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", pseudo);
        cur = coll.find(query);
      
       
       while(cur.hasNext()) {
        courant = cur.next();
        
      
        if(courant.get("password").equals(password)){
        
       
	coll.remove(query);
        
                    return "OK";
                }else{
                   return "WPASS";
                }  
            
        }
       
        return "WPSEUDO"; 
    }
    
    /**
     * Fonction permettant de récupérer le nombre de joueur inscrits dans la base de données.
     * 
     * @return {@code integer} le nombre de joueurs inscrits dans la base de données
     */
    public int getNombreJoueur(){
        return (int) coll.count();
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
     * @author Maurin Benjamin
     * 
     * @param s le pseudo du joueur a rechercher
     * 
     * @return {@code boolean} retourne vrai si le pseudo n'est pas présent sinon faux
     */
    private boolean verifPseudo(String s){
      
                DBCursor cur; 
        DBObject courant = null;
       BasicDBObject query = new BasicDBObject();
        query.put("pseudo", s);
        cur = coll.find(query);
      
       
       while(cur.hasNext()) {
           return false;
       }
       
        return true;
    }
    
    /**
     * Fonction permettant de générer un identifiant unique pour chaque jours dans la base de données.
     * 
     * @author Maurin Benjamin
     * 
     * @return {@code String} retournant le premier numéro libre suivant le dernier dans la base de données.
     */
    private String getNumeroJoueur(){
        String ret;
         DBCursor cur; 
        DBObject courant = null;
        cur = coll.find();
      
       
       while(cur.hasNext()) {
        courant = cur.next();
       }
        if(courant!=null)
            ret = String.valueOf(Integer.parseInt((String)courant.get("numero")) + 1);
        else
            ret = "1";
        
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
      coll = null;

      db= null;
        } catch(Exception e) {
        
        }
        finally {
            super.finalize();
        }
    }
    
    
     /**
     * Fonction permettant de retourner la liste des données dans la BDD.
     * 
     * @author Maurin Benjamin
     * 
     * @return {@code String} retournant liste des données dans la BDD.
     */
    public String listeClient(){
     
                DBCursor cur; 
       String s = "Liste des donnees : \n\n";
        cur = coll.find();

       while(cur.hasNext()) {
           s+=cur.next()+"\n\n";
                }
       
       return s;
       }
    
    
    
}
