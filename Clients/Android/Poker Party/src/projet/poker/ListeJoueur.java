package projet.poker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connexion;
import projet.GestionConnexion.CreateurTram;

/**
 * Classe affichant la liste des joueurs pour une partie.
 * 
 * @author Mathieu Polizzi & Jessy Bonnotte
 */
public class ListeJoueur extends Activity{    
    // Pour la boite de dialog 
    AlertDialog.Builder                 _adb;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler              _messageHandler;
    private static Handler              _resultatHandler;
    
    // Gestion des éléments graphiques
    private Button                      _bouton_Start;
    private Button                      _bouton_Retour;
    private ListView                    _listView_Joueurs;
    
    // Gestion de la liste des joueurs
    private List<String>                _listeJoueurs;
    private ArrayAdapter<String>        _aa;
    private String                      _nomUtilisateur;
    
    // Demarrage de la nouvelle activity
    private Intent                      _i;

    // Création de la ArrayList qui nous permettra de remplire la listView
    private ArrayList<String>           _listeJoueursTransfert = new ArrayList<String>();
           
    // On déclare une variable de type String qui contiendra les nom a afficher
    private String                      _nomJoueursTemp;  
    
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attentepartie);
        
        initPseudoJoueur();
        
        _aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _listeJoueursTransfert);
        
        // on prepare le handler de retour de liste
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                _listeJoueurs = new ArrayList<String>((List<String>)msg.obj);
                MAJAffichage();
                analyseStart();
            }
        };
        
        _resultatHandler = new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("EXITOK")){
                    finish();
                }else if(msg.obj.toString().equals("AREUREADY")){
                    _i = new Intent (getApplicationContext(), TableauJeu.class);
                    startActivity(_i);
                    finish();
                }
                MAJAffichage();
            }
        };
        
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        if(!liaisonXML()){
            Toast.makeText(this, "Problème a la liaison avec le XML", Toast.LENGTH_SHORT).show();
        }
        
        if(!initObjet()){
            Toast.makeText(this, "Problème a la Création des objets", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    /** Called when the activity is resumed. */
    public void onResume(){
        super.onResume();
        Accueil.connexion.setActivity(Connexion.LISTE_JOUEUR_PARTIE);
        try {
            Accueil.createurTram.setTram(CreateurTram.GET_PLAYER);
        } catch (IOException ex) {
            Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialisation des objets contenu dans le xml.
     * 
     * @author Mathieu Polizzi
     * 
     * @return boolean - resultat de la liaison du code avec le XML
     */
    private boolean liaisonXML(){
        try{
            _bouton_Start = (Button) findViewById(R.id.start);
            _bouton_Retour = (Button) findViewById(R.id.retour);
            
            //Récupération de la listview créée dans le fichier main.xml
            _listView_Joueurs = (ListView) findViewById(R.id.listviewjoueur);
                         
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * 
     */
    private void analyseStart(){
        Iterator<String> iterator = _listeJoueurs.iterator();
        String tmp = iterator.next();
        if(_nomUtilisateur.equals(tmp.substring(1,tmp.length()))){
            _bouton_Start.setEnabled(true);
            _bouton_Start.setVisibility(0);
        }else{
            _bouton_Start.setEnabled(false);
            _bouton_Start.setVisibility(1);
        }
    }
    
    /**
     * Initialise les differents boutons de la fenetre.
     * 
     * @author Mathieu Polizzi
     * 
     * @return boolean - resultat de l'initialisation des objets.
     */
    private boolean initObjet(){
        try{
            _bouton_Start.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    try {
                        Accueil.createurTram.setTram(CreateurTram.DEBUT_PARTIE);
                    } catch (IOException ex) {
                        Logger.getLogger(ListeJoueur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            _bouton_Retour.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        Accueil.createurTram.setTram(CreateurTram.EXIT_PARTIE);
                    } catch (IOException ex) {
                        Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * initialise la boite de dialogue d'affichage des infos de joueurs.
     * 
     * @author  Mathieu Polizzi
     * 
     * @return boolean - resultat de l'initialisation de la dialogBox
     */
    private boolean initDialog(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupcreatepartie, null);
            
            

            //Liaison du xml
            final EditText nomPartie = (EditText) alertDialogView.findViewById(R.id.partyName);
            final SeekBar nbPlayer = (SeekBar) alertDialogView.findViewById(R.id.nbPlayers);
            
            //Création de l'AlertDialog
            _adb = new AlertDialog.Builder(this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            _adb.setTitle("Créer Partie");

            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.add);

            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Toast.makeText(getApplicationContext(), ""+nbPlayer.getProgress(), Toast.LENGTH_SHORT).show();
                    
                    Log.v("Accueuil", "test");
                    
                    if(verifEnv.analyseNomPartie(nomPartie.getText().toString())){
                        String[] arg = new String[2];
                        arg[0] = nomPartie.getText().toString();
                        arg[1] = ""+nbPlayer.getProgress();
                        try {
                            Accueil.createurTram.setTram(CreateurTram.CREATE_PARTIE, arg, 2);
                        } catch (IOException ex) {
                            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //MAJAffichage();
                        dialog.cancel();
                    }else{
                        //MAJAffichage();
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), "Mauvais format d'écriture", Toast.LENGTH_SHORT).show();   
                    }
                } 
            });

            //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
            _adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Lorsque l'on cliquera sur annuler on quittera l'application
                    dialog.cancel();
                } 
            });
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * Fonction appelé par l'analyseur de tram afin de mettre a jour l'affichage. La fonction se sert d'un handler afin de sortir les données de la partie static.
     * 
     * @author Jessy Bonnotte
     * 
     * @param tabPartie - une liste contenant les parties sous forme de tableaux
     *
     */
    public static void MAJList(List<String> tabJoueur){
        Message msg = new Message();
        msg.obj = tabJoueur;
        _messageHandler.sendMessage(msg);
    }
    
    /**
     * Fonction appelé par l'analyseur de tram pour informaer l'utilisateur des infos envoyés par le serveur.
     * 
     * @author Jessy Bonnotte
     * 
     * @param message - le message de retour du serveur
     *
     */
    public static void afficheMessage(String message){
        Message msg = new Message();
        msg.obj = message;
        _resultatHandler.sendMessage(msg);
    }
    
    /**
     * Mise à jour du tableau des parties disponible grace a la liste de tableau de string récupérer grace a l analyseur de trame.
     * 
     * @author Mathieu Polizzi
     */
    private void MAJAffichage(){
        _listeJoueursTransfert.clear();
        Iterator<String> iterator = _listeJoueurs.iterator();
        while (iterator.hasNext()) {
            _listeJoueursTransfert.add(iterator.next());
        }
        _listView_Joueurs.setAdapter(_aa);  
        _aa.notifyDataSetChanged();
    }
    
    /**
     * Deroutement des fonctions de base du bouton MENU.
     * 
     * @author Jessy Bonnotte
     * 
     * @param keyCode le code de la touche pressé
     * @param event l'evenement a faire de la touche pressé
     * 
     * @return boolean - si la touche est a prendre en compte
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Fonction permettant de savoir le pseudo du joueur
     * 
     * @author Jessy Bonnotte
     */
    private void initPseudoJoueur(){
         SharedPreferences settings = getSharedPreferences(Accueil.PREFS_CONNECT, 0);
         _nomUtilisateur = settings.getString("pseudo", "0");
    }
}
