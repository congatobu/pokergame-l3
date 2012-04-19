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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connection;
import projet.GestionConnexion.CreateurTram;

/**
 * Classe affichant la liste des partie.
 * 
 * @author Jessy Bonnotte & Mathieu Polizzi
 */
public class ListePartie extends Activity{    
    // Pour la boite de dialog 
    AlertDialog.Builder                 adb;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler              messageHandler;
    private static Handler              resultHandler;
    
    // Ouverture d'une nouvelle activity
    private Intent                      i;
    
    // Gestion des éléments graphiques
    private Button                      create;
    private Button                      refresh;
    private Button                      disconnect;
    private EditText                    findParty;
    private ListView                    maList;
  
    // Liste des partie de type liste de string
    private List<String[]>              listPartie;

    // Création de la ArrayList qui nous permettra de remplire la listView
    ArrayList<HashMap<String, String>>  listItem = new ArrayList<HashMap<String, String>>();
           
    // On déclare la HashMap qui contiendra les informations pour un item
    HashMap<String, String>             map;  
    
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listepartie);       
    }
    
    @Override
    /**
     * Initialisation de la fenetre.
     * 
     * @auhor Jessy Bonnotte
     */
    public void onResume(){
        super.onResume();
        
        // on prepare le handler de retour de liste
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                listPartie = new ArrayList<String[]>((List<String[]>)msg.obj);
                MAJAffichage();
            }
        };
        
        // on prepare le handler de retour message
        resultHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Log.v("rejoindre", msg.obj.toString());
                if(msg.obj.toString().equals("REJOK")){
                    i = new Intent (getApplicationContext(), ListeJoueur.class);
                    startActivity(i);
                }else if(msg.obj.toString().equals("CREATPOK")){
                    i = new Intent (getApplicationContext(), ListeJoueur.class);
                    startActivity(i);
                }
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
            }
        };
        
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        if(!liaisonXML()){
            Toast.makeText(this, "Problème a la liaison avec le XML", Toast.LENGTH_SHORT).show();
        }
        
        if(!initObjet()){
            Toast.makeText(this, "Problème a la Création des objets", Toast.LENGTH_SHORT).show();
        }
          
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Accueuil.sender.setTram(CreateurTram.GET_LISTE_PARTIE);
                } catch (IOException ex) {
                    Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //Enfin on met un écouteur d'évènement sur notre listView
        maList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String tmp[] = new String[1];
                map = new HashMap<String, String>();
                map = (HashMap<String, String>)a.getItemAtPosition(position);
                tmp[0] = map.get("PartyName");
                try {
                    Accueuil.sender.setTram(CreateurTram.REJOINDRE_PARTIE, tmp, 1);
                } catch (IOException ex) {
                    Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Accueuil.connect.setActivity(Connection.LISTE_PARTIE);
        try {
            Accueuil.sender.setTram(CreateurTram.GET_LISTE_PARTIE);
        } catch (IOException ex) {
            Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * initialisation les objets contenu dans le xml.
     * 
     * @author Mathieu Polizzi
     * 
     * @return boolean - resultat de la liaison du code avec le XML
     */
    private boolean liaisonXML(){
        try{
            create = (Button) findViewById(R.id.create);
            refresh = (Button) findViewById(R.id.refresh);
            disconnect = (Button) findViewById(R.id.disconnect);
            findParty =(EditText) findViewById(R.id.findParty);
   
            //Récupération de la listview créée dans le fichier main.xml
            maList = (ListView) findViewById(R.id.listviewpartie);
                         
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * initialise les boutons de l'activity.
     * 
     * @author Mathieu Polizzi
     * 
     * @return boolean - resultat de l'initialisation des objets.
     */
    private boolean initObjet(){
        try{
            create.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    initDialog();
                    adb.show();
                }
            });
            
            disconnect.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        Accueuil.sender.setTram(CreateurTram.DECONNECT);
                    } catch (IOException ex) {
                        Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Accueuil.connect.dispose();
                    finish();
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
     * @author Mathieu Polizzi
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
     * @author Jessy Bonnotte
     * 
     * @param tabPartie - une liste contenant les parties sous forme de tableaux
     *
     */
    public static void MAJList(List<String[]> tabPartie){
        Message msg = new Message();
        msg.obj = tabPartie;
        messageHandler.sendMessage(msg);
     
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
        resultHandler.sendMessage(msg);
    }
    
    /**
     * Mise à jour du tableau des parties disponible grace a la liste de tableau de string récupérer grace a l analyseur de trame.
     * 
     * @author Mathieu Polizzi
     */
    private void MAJAffichage(){
        listItem.clear();
        Iterator<String[]> iterator = listPartie.iterator();
        while (iterator.hasNext()) {
            map = new HashMap<String, String>();
            String[] temp= iterator.next();
            //on insère un élément partyname que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
            map.put("PartyName", temp[0]);
            //on insère un élément nbplayers que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
            map.put("nbPlayers",temp[1]+"/"+temp[2]);
            listItem.add(map);
        }
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.displaylist,
        new String[] {"nbPlayers", "PartyName"}, new int[] { R.id.nbPlayers, R.id.PartyName});
        maList.setAdapter(mSchedule);      
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
}
