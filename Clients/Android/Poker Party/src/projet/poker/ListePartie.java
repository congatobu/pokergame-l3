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
import projet.GestionConnexion.Connexion;
import projet.GestionConnexion.CreateurTram;

/**
 * Classe affichant la liste des partie.
 * 
 * @author Jessy Bonnotte & Mathieu Polizzi
 */
public class ListePartie extends Activity{    
    // Pour la boite de dialog 
    AlertDialog.Builder                         _adb;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler                      _messageHandler;
    private static Handler                      _resultHandler;
    
    // Ouverture d'une nouvelle activity
    private Intent                              _i;
    
    // Gestion des éléments graphiques
    private Button                              _bouton_Creer;
    private Button                              _bouton_Rafraichir;
    private Button                              _bouton_Deconnecter;
    private EditText                            _editText_ChercherPartie;
    private ListView                            _listView_Parties;
  
    // Liste des partie de type liste de string
    private List<String[]>                      _listeParties;

    // Création de la ArrayList qui nous permettra de remplire la listView
    private ArrayList<HashMap<String, String>>  _listePartiesTransfet = new ArrayList<HashMap<String, String>>();
           
    // On déclare la HashMap qui contiendra les informations pour un item de la liste
    private HashMap<String, String>             _nomPartieTemp;  
    
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
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                _listeParties = new ArrayList<String[]>((List<String[]>)msg.obj);
                Log.v("la partie", "nom : ");
                MAJAffichage();
            }
        };
        
        // on prepare le handler de retour message
        _resultHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Log.v("rejoindre", msg.obj.toString());
                if(msg.obj.toString().equals("REJOK")){
                    _i = new Intent (getApplicationContext(), ListeJoueur.class);
                    startActivity(_i);
                }else if(msg.obj.toString().equals("CREATPOK")){
                    _i = new Intent (getApplicationContext(), ListeJoueur.class);
                    startActivity(_i);
                }else if(msg.obj.toString().equals("Vide")){
                    _listeParties = new ArrayList<String[]>();
                    MAJAffichage();
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
          
        _bouton_Rafraichir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Accueil.createurTram.setTram(CreateurTram.GET_LISTE_PARTIE);
                } catch (IOException ex) {
                    Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //Enfin on met un écouteur d'évènement sur notre listView
        _listView_Parties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String tmp[] = new String[1];
                _nomPartieTemp = new HashMap<String, String>();
                _nomPartieTemp = (HashMap<String, String>)a.getItemAtPosition(position);
                tmp[0] = _nomPartieTemp.get("PartyName");
                try {
                    Accueil.createurTram.setTram(CreateurTram.REJOINDRE_PARTIE, tmp, 1);
                } catch (IOException ex) {
                    Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Accueil.connexion.setActivity(Connexion.LISTE_PARTIE);
        try {
            Accueil.createurTram.setTram(CreateurTram.GET_LISTE_PARTIE);
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
            _bouton_Creer = (Button) findViewById(R.id.create);
            _bouton_Rafraichir = (Button) findViewById(R.id.refresh);
            _bouton_Deconnecter = (Button) findViewById(R.id.disconnect);
            _editText_ChercherPartie =(EditText) findViewById(R.id.findParty);
   
            //Récupération de la listview créée dans le fichier main.xml
            _listView_Parties = (ListView) findViewById(R.id.listviewpartie);
                         
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
            _bouton_Creer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    initDialog();
                    _adb.show();
                }
            });
            
            _bouton_Deconnecter.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    try {
                        Accueil.createurTram.setTram(CreateurTram.DECONNECT);
                    } catch (IOException ex) {
                        Logger.getLogger(ListePartie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Accueil.connexion.dispose();
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
            final TextView afficheNbPlayers = (TextView) alertDialogView.findViewById(R.id.TextView2);
                    
            //Création de l'AlertDialog
            _adb = new AlertDialog.Builder(this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            _adb.setTitle("Créer Partie");

            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.add);

            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            
            nbPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

                    try {

                        afficheNbPlayers.setText("Joueurs max : "+Integer.toString(progress + 2));

                    } catch (Exception e) {

                    }
                }

                public void onStartTrackingTouch(SeekBar arg0) {

                }

                public void onStopTrackingTouch(SeekBar arg0) {

                }
            });
            
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Toast.makeText(getApplicationContext(), ""+(nbPlayer.getProgress()+2), Toast.LENGTH_SHORT).show();
                    
                    if(verifEnv.analyseNomPartie(nomPartie.getText().toString())){
                        String[] arg = new String[2];
                        arg[0] = nomPartie.getText().toString();
                        arg[1] = ""+(nbPlayer.getProgress()+2);
                        Log.v("creation partie", arg[1]);
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
    public static void MAJList(List<String[]> tabPartie){
        Message msg = new Message();
        msg.obj = tabPartie;
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
        _resultHandler.sendMessage(msg);
    }
    
    /**
     * Mise à jour du tableau des parties disponible grace a la liste de tableau de string récupérer grace a l analyseur de trame.
     * 
     * @author Mathieu Polizzi
     */
    private void MAJAffichage(){
        _listePartiesTransfet.clear();
        _nomPartieTemp = new HashMap<String, String>();
        Iterator<String[]> iterator = _listeParties.iterator();
        while (iterator.hasNext()) {
            _nomPartieTemp = new HashMap<String, String>();
            String[] temp= iterator.next();
            //on insère un élément partyname que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
            _nomPartieTemp.put("PartyName", temp[0]);
            //on insère un élément nbplayers que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
            _nomPartieTemp.put("nbPlayers",temp[1]+"/"+temp[2]);
            _listePartiesTransfet.add(_nomPartieTemp);
        }
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), _listePartiesTransfet, R.layout.displaylist,
        new String[] {"nbPlayers", "PartyName"}, new int[] { R.id.nbPlayers, R.id.PartyName});
        _listView_Parties.setAdapter(mSchedule);      
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
