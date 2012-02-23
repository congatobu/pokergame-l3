/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.poker;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connection;
import projet.GestionConnexion.CreateurTram;


 
/**
 * Classe de l'activity paramètre
 * 
 * @author Shyzkanza
 */
public class Parametre extends Activity{
 
    private ListView maListViewPerso;
    
    // Handler pour sortir les messages de la partie static
    private static Handler messageHandler;
    
    // Pour la boite de dialog 
    AlertDialog.Builder             adb;
 
    /** 
     * Fonction appelé lors de la création de l'activity. le super.onCreate()<br/>
     * permet de faire les actions de base puis enfin on peut fair eles actions <br/>
     * personnels 
     * 
     * @param savedInstanceState - dernière instance de l'application qui a été sauvegardé 
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);
        Accueuil.connect.setActivity(Connection.PARAMETRE);
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        
        //Récupération de la listview créée dans le fichier main.xml
        maListViewPerso = (ListView) findViewById(R.id.listviewperso);
 
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
 
        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichageitem,
               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});
 
        //On attribut à notre listView l'adapter que l'on vient de créer
        maListViewPerso.setAdapter(mSchedule);
 
        //Enfin on met un écouteur d'évènement sur notre listView
        maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                switch(position){
                    case 0:
                        initDialogPseudo();
                        adb.show();
                        break;
                    case 1:
                        initDialogPass();
                        adb.show();
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
    
    public static void finOperation(String message){
        Message msg = new Message();
        msg.obj = message;
        messageHandler.sendMessage(msg);
    }
    
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
            adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            adb.setTitle("Changer de mot de pass");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            adb.setIcon(R.drawable.myspace);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Log.v("Parametre", "test");
                    
                    if(verifEnv.analysePseudo(pseudomdp.getText().toString()) && verifEnv.analyseMDP(oldmdp.getText().toString()) && verifEnv.analyseMDP(newmdp.getText().toString())){
                        String[] arg = new String[3];
                        arg[0] = pseudomdp.getText().toString();
                        arg[1] = oldmdp.getText().toString();
                        arg[2] = newmdp.getText().toString();

                        if(Accueuil.connect.init("88.167.230.145", "6667")){
                            try {
                                Accueuil.sender.setTram(CreateurTram.ACTUALISE_PASSWORD, arg, 3);
                            } catch (IOException ex) {
                                Logger.getLogger(Accueuil.class.getName()).log(Level.SEVERE, null, ex);
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
            adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            adb.setTitle("Changer de pseudo");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            adb.setIcon(R.drawable.myspace);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    Log.v("Parametre", "test");
                    
                    if(verifEnv.analysePseudo(oldpseudo.getText().toString()) && verifEnv.analyseMDP(mdppseudo.getText().toString()) && verifEnv.analyseMDP(newpseudo.getText().toString())){
                        String[] arg = new String[3];
                        arg[0] = oldpseudo.getText().toString();
                        arg[1] = mdppseudo.getText().toString();
                        arg[2] = newpseudo.getText().toString();

                        if(Accueuil.connect.init("88.167.230.145", "6667")){
                            try {
                                Accueuil.sender.setTram(CreateurTram.ACTUALISE_PSEUDO, arg, 3);
                            } catch (IOException ex) {
                                Logger.getLogger(Accueuil.class.getName()).log(Level.SEVERE, null, ex);
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
}