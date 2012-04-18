package projet.poker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import projet.GestionConnexion.Connection;
import projet.GestionConnexion.CreateurTram;

public class ListeJoueur extends Activity{
    /** Called when the activity is first created. */
    
    // Pour la boite de dialog 
    AlertDialog.Builder                 adb;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler              messageHandler;
    private static Handler              resultHandler;
    
    // Gestion des éléments graphiques
    private Button                      start;
    private Button                      retour;
    private ListView                    listeJoueurs;
    
    private List<String>                listJoueurs;
    private ArrayAdapter<String>        aa;
    
    // Demarrage de la nouvelle activity
    private Intent                      i;

    // Création de la ArrayList qui nous permettra de remplire la listView
    private ArrayList<String>           listItem = new ArrayList<String>();
           
    // On déclare la HashMap qui contiendra les informations pour un item
    private String                      map;  
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attentepartie);
        
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        
        // on prepare le handler de retour de liste
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                listJoueurs = new ArrayList<String>((List<String>)msg.obj);
                MAJAffichage();
            }
        };
        
        resultHandler = new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("EXITOK")){
                    finish();
                }else if(msg.obj.toString().equals("AREUREADY")){
                    i = new Intent (getApplicationContext(), TableauJeu.class);
                    startActivity(i);
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
    public void onResume(){
        super.onResume();
        Accueuil.connect.setActivity(Connection.LISTE_JOUEUR_PARTIE);
        try {
            Accueuil.sender.setTram(CreateurTram.GET_PLAYER);
        } catch (IOException ex) {
            Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void onStop(){
        
        super.onDestroy();
    }
    
    /**
     * initialisation les objets contenu dans le xml.
     * 
     * @author Mathieu Polizzi
     * 
     * @return {@code boolean} resultat de la liaison du code avec le XML
     */
    private boolean liaisonXML(){
        try{
            start = (Button) findViewById(R.id.start);
            retour = (Button) findViewById(R.id.retour);
            
            //Récupération de la listview créée dans le fichier main.xml
            listeJoueurs = (ListView) findViewById(R.id.listviewjoueur);
                         
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * initialise la fenêtre permettant de créer une partie.
     * 
     * @author Mathieu Polizzi
     * 
     * @return {@code boolean} resultat de l'initialisation des objets.
     */
    private boolean initObjet(){
        try{
            start.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    try {
                        Accueuil.sender.setTram(CreateurTram.DEBUT_PARTIE);
                    } catch (IOException ex) {
                        Logger.getLogger(ListeJoueur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            retour.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        Accueuil.sender.setTram(CreateurTram.EXIT_PARTIE);
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
     * initialise la boite de dialogue de la création de partie.
     * 
     * @author  Mathieu Polizzi
     * 
     * @return {@code boolean} - resultat de l'initialisation de la dialogBox
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
            adb = new AlertDialog.Builder(this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Créer Partie");

            //On modifie l'icône de l'AlertDialog pour le fun ;)
            adb.setIcon(R.drawable.add);

            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Toast.makeText(getApplicationContext(), ""+nbPlayer.getProgress(), Toast.LENGTH_SHORT).show();
                    
                    Log.v("Accueuil", "test");
                    
                    if(verifEnv.analyseNomPartie(nomPartie.getText().toString())){
                        String[] arg = new String[2];
                        arg[0] = nomPartie.getText().toString();
                        arg[1] = ""+nbPlayer.getProgress();
                        try {
                            Accueuil.sender.setTram(CreateurTram.CREATE_PARTIE, arg, 2);
                        } catch (IOException ex) {
                            Logger.getLogger(Accueuil.class.getName()).log(Level.SEVERE, null, ex);
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
            adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
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
     * @author Mathieu Polizzi
     * 
     * @param tabPartie une liste contenant les parties sous forme de tableaux
     *
     */
    public static void MAJList(List<String> tabJoueur){
        Message msg = new Message();
        msg.obj = tabJoueur;
        messageHandler.sendMessage(msg);
     
    }
    
    /**
     * Fonction appelé par l'analyseur de tram pour informaer l'utilisateur des infos envoyés par le serveur.
     * 
     * @author Mathieu Polizzi
     * 
     * @param message - le message de retour du serveur
     *
     */
    public static void afficheMessage(String message){
        Message msg = new Message();
        msg.obj = message;
        resultHandler.sendMessage(msg);
    }
    
    /**
     * Mise à jour du tableau des parties disponible grace a la liste de tableau de string récupérer grace a l analyseur de trame.
     * 
     * @author Mathieu Polizzi
     */
    private void MAJAffichage(){
        listItem.clear();
        Iterator<String> iterator = listJoueurs.iterator();
        while (iterator.hasNext()) {
            listItem.add(iterator.next());
        }
        listeJoueurs.setAdapter(aa);  
        aa.notifyDataSetChanged();
    }
    
    /**
     * Deroutement des fonctions de base du bouton MENU.
     * 
     * @author Jessy Bonnotte
     * 
     * @param keyCode le code de la touche pressé
     * @param event l'evenement a faire de la touche pressé
     * 
     * @return {@code boolean} - si la touche est a prendre en compte
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
