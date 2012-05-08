/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.poker;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connexion;
import projet.GestionConnexion.CreateurTram;
 
/**
 * Classe de l'activity paramètre
 * 
 * @author Shyzkanza
 */
public class Parametre extends Activity{
 
    private ListView                _listView_Parametre;
    
    // Handler pour sortir les messages de la partie static
    private static Handler          _messageHandler;
    
    // Pour la boite de dialog 
    private AlertDialog.Builder     _adb;
    
    // Paratmètres de connexion
    private String                  _IP;
    private String                  _PORT;
 
    /** 
     * Fonction appelé lors de la création de l'activity. le super.onCreate() permet de faire les actions de base puis enfin on peut fair eles actions personnels.
     * 
     * @param savedInstanceState - dernière instance de l'application qui a été sauvegardé 
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);
        Accueil.connexion.setActivity(Connexion.PARAMETRE);
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        
        //Récupération de la listview créée dans le fichier main.xml
        _listView_Parametre = (ListView) findViewById(R.id.listviewperso);
 
        //Création de la ArrayList qui nous permettra de remplire la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
 
        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;
 
        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();
        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
        map.put("titre", "Changer de pseudo");
        //on insère un élément description que l'on récupérera dans le textView description créé dans le fichier affichageitem.xml
        map.put("description", "Requiert internet");
        //on insère la référence à l'image (convertit en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml
        map.put("img", String.valueOf(R.drawable.myspace));
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);
 
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Changer de mot de passe");
        map.put("description", "Requiert internet");
        map.put("img", String.valueOf(R.drawable.myspace));
        listItem.add(map);
        
        map = new HashMap<String, String>();
        map.put("titre", "Reglage Reseau");
        map.put("description", "Parametrage du port et de l'IP");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
 
        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichageitem,
               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});
 
        //On attribut à notre listView l'adapter que l'on vient de créer
        _listView_Parametre.setAdapter(mSchedule);
 
        //Enfin on met un écouteur d'évènement sur notre listView
        _listView_Parametre.setOnItemClickListener(new OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                switch(position){
                    case 0:
                        initDialogPseudo();
                        _adb.show();
                        break;
                    case 1:
                        initDialogPass();
                        _adb.show();
                        break;
                    case 2:
                        initDialogIPPort();
                        _adb.show();
                        break;                  
                }
                //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                //HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                //on créer une boite de dialogue
                if(position == 9){
                    
                }
            }
        });
    }
    
    @Override
    public void onResume(){
        super.onResume();
        chargerParamReseau();
    }
    
    /**
     * Charge les parametre reseau enregistré dans le systeme de preference.
     * 
     * @author Jessy Bonnotte
     */
    private void chargerParamReseau(){
        SharedPreferences settings = getSharedPreferences(Accueil.PREFS_CONFIG, 0);        
               
        _IP = settings.getString("ip", "0");
        _PORT = settings.getString("port", "0");
    }
    
    /**
     * Fonction recevant les données reçu du serveur. elles sont envoyé dans le Handler afin qu'elles redeviennent privé et donc utilisable.
     * 
     * @param message - le message reçu du serveur
     */
    public static void finOperation(String message){
        Message msg = new Message();
        msg.obj = message;
        _messageHandler.sendMessage(msg);
    }
    
    /**
     * Initialise la boite de dialogue de changement de mot de passe.
     * 
     * @author Jessy bonnotte
     * 
     * @return boolean - resultat de l'initialisation
     */
    private boolean initDialogPass(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupactualisepassword, null);                 
            //Liaison du xml
            final EditText pseudomdp = (EditText) alertDialogView.findViewById(R.id.pseudomdp);
            final EditText oldmdp = (EditText) alertDialogView.findViewById(R.id.oldmdp);
            final EditText newmdp = (EditText) alertDialogView.findViewById(R.id.newmdp);
            //Création de l'AlertDialog
            _adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            _adb.setTitle("Changer de mot de pass");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.myspace);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Log.v("Parametre", "test");
                    
                    if(verifEnv.analysePseudo(pseudomdp.getText().toString()) && verifEnv.analyseMDP(oldmdp.getText().toString()) && verifEnv.analyseMDP(newmdp.getText().toString())){
                        String[] arg = new String[3];
                        arg[0] = pseudomdp.getText().toString();
                        arg[1] = oldmdp.getText().toString();
                        arg[2] = newmdp.getText().toString();

                        if(Accueil.connexion.init("88.167.230.145", "6667")){
                            try {
                                Accueil.createurTram.setTram(CreateurTram.ACTUALISE_PASSWORD, arg, 3);
                            } catch (IOException ex) {
                                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        dialog.cancel();
                        
                    }else{
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
     * Initialise la boite de dialogue de changement de mot de pseudo.
     * 
     * @author Jessy bonnotte
     * 
     * @return boolean - resultat de l'initialisation
     */
    private boolean initDialogPseudo(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupactualisepseudo, null);                 
            //Liaison du xml
            final EditText oldpseudo = (EditText) alertDialogView.findViewById(R.id.oldpseudo);
            final EditText mdppseudo = (EditText) alertDialogView.findViewById(R.id.mdppseudo);
            final EditText newpseudo = (EditText) alertDialogView.findViewById(R.id.newpseudo);
            //Création de l'AlertDialog
            _adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            _adb.setTitle("Changer de pseudo");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.myspace);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Log.v("Parametre", "test");
                    
                    if(verifEnv.analysePseudo(oldpseudo.getText().toString()) && verifEnv.analyseMDP(mdppseudo.getText().toString()) && verifEnv.analyseMDP(newpseudo.getText().toString())){
                        String[] arg = new String[3];
                        arg[0] = oldpseudo.getText().toString();
                        arg[1] = mdppseudo.getText().toString();
                        arg[2] = newpseudo.getText().toString();

                        if(Accueil.connexion.init("88.167.230.145", "6667")){
                            try {
                                Accueil.createurTram.setTram(CreateurTram.ACTUALISE_PSEUDO, arg, 3);
                            } catch (IOException ex) {
                                Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        dialog.cancel();
                        
                    }else{
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
     * Fonction permettant d'initialiser la boite de dialogue pour la configuration de l'ip et du port
     * 
     * @return boolean - resultat de l'initialisation
     */
    private boolean initDialogIPPort(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupipport, null);                 
            //Liaison du xml
            final EditText ip = (EditText) alertDialogView.findViewById(R.id.ip);
            final EditText port = (EditText) alertDialogView.findViewById(R.id.port);
            //Création de l'AlertDialog
            _adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            _adb.setTitle("Parametrage reseau");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.info);
            
            ip.setText(_IP);
            port.setText(_PORT);
            
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences settings = getSharedPreferences(Accueil.PREFS_CONFIG, 0);        
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("ip", ip.getText().toString());
                    editor.putString("port", port.getText().toString());
                    editor.commit();
                    dialog.cancel();
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
            Log.v("param", e.getMessage());
            return false;
        }
        return true;
    }
}