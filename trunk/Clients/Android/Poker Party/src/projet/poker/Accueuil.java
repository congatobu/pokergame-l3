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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connection;
import projet.GestionConnexion.CreateurTram;

public class Accueuil extends Activity{
    
    // Gestion de la connexion
    public static Connection        connect;
    
    // Creation de tram puis envoi
    public static CreateurTram      sender;
    
    // Gestion des éléments graphiques
    private Button                  connection;
    private Button                  creerCompte;
    private CheckBox                retenir;
    private EditText                user;
    private EditText                pass;
    
    // Ouverture d'une nouvelle activity
    private Intent                  i;
    
    // Verification pseudo/mot de passe
    private AnalyseurEnvoi          verifEnvoi;  
    
    // gestion des éléments du menu paramètre
    private final int               PARAMETRE = 1;
    private final int               A_PROPOS = 2;
    private final int               QUITTER = 3;
    
    // Pour la boite de dialog 
    AlertDialog.Builder             adb;
    
    // Handler pour sortir les messages de la partie static
    private static Handler messageHandler;
    
    /**
     * Référence vers le texte stocké dans le système de préférences
     */
    public static final String  PREFS_CONNECT = "preferenceConnect"; 
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        sender = new CreateurTram();
        connect = new Connection();
        verifEnvoi = new AnalyseurEnvoi();
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        if(!liaisonXML()){
            Toast.makeText(this, "Problème a la liaison avec le XML", Toast.LENGTH_SHORT).show();
        }
        
        if(!initObjet()){
            Toast.makeText(this, "Problème a l'initialisation des objets", Toast.LENGTH_SHORT).show();
        }
        initZone();
    }
    
    /**
     * 
     */
    @Override
    public void onResume(){
        super.onResume();
        connect.setActivity(Connection.ACCUEUIL);
    }
    
    /** 
     * Surcharge de la fonction onCreateOptionsMenu() afin de créer le menu au clique<br/>
     * de l'utilisateur sur la touche menu
     * 
     * @param menu - le menu qui est demandé
     * 
     * @return true - l'affichage est terminé
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, PARAMETRE, 0, "Paramètre");
        menu.findItem(PARAMETRE).setIcon(R.drawable.settings);
        menu.add(0, A_PROPOS, 0, "A propos");
        menu.findItem(A_PROPOS).setIcon(R.drawable.info);
        menu.add(0, QUITTER, 0, "Quitter");
        menu.findItem(QUITTER).setIcon(R.drawable.exit);
        return true;
    }

    /** 
     * Surcharge de la fonction onOptionsItemSelected() afin d'effectuer les actions<br/>
     * sur lesquels l'utilisateur clique.
     * 
     * @param item - l'élément sur lequel l'utilisateur a cliqué.
     * 
     * @return boolean - retourne true si le parametre existe ou false sinon.
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case PARAMETRE:
            i = new Intent (getApplicationContext(), Parametre.class);
      	    startActivity(i);
            return true;
        case A_PROPOS:
            i = new Intent (getApplicationContext(), APropos.class);
            startActivity(i);
            return true;
        case QUITTER:
            finish();
            return true;
        }
        return false;
    }
    
    private boolean liaisonXML(){
        try{
            pass = (EditText) findViewById(R.id.mdp);
            user = (EditText) findViewById(R.id.pseudo);
            connection = (Button) findViewById(R.id.connect);
            creerCompte = (Button) findViewById(R.id.createcpt);
            retenir = (CheckBox) findViewById(R.id.memorize); 
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    private boolean initDialog(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupcreatecompte, null);

            
            
            //Liaison du xml
            final EditText pseudoCRT = (EditText) alertDialogView.findViewById(R.id.pseudocrt);
            final EditText mdpCRT = (EditText) alertDialogView.findViewById(R.id.mdpcrt);
            
            //Création de l'AlertDialog
            adb = new AlertDialog.Builder(this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            adb.setTitle("Créer compte");

            //On modifie l'icône de l'AlertDialog pour le fun ;)
            adb.setIcon(R.drawable.add);

            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            
            
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    if(verifEnv.analysePseudo(pseudoCRT.getText().toString()) && verifEnv.analyseMDP(mdpCRT.getText().toString())){
                        String[] arg = new String[2];
                        arg[0] = pseudoCRT.getText().toString();
                        arg[1] = mdpCRT.getText().toString();

                        if(connect.init("88.167.230.145", "6667")){
                            try {
                                sender.setTram(CreateurTram.CREATECPT, arg, 2);
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
    
    private boolean initObjet(){
        try{
            connection.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_SHORT).show();
                    if(user.getText().toString().equals("") || pass.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner les champs", Toast.LENGTH_SHORT).show();
                    }else{ 
                        if(retenir.isChecked()){
                            saveZone();
                        }else if(!retenir.isChecked()){
                            ereaseZone();
                        }
                        if(connexion()){
                            String[] arg = new String[2];
                            arg[0] = user.getText().toString();
                            arg[1] = pass.getText().toString();
                            if(connect.init("88.167.230.145", "6667")){
                                try {
                                    sender.setTram(CreateurTram.CONNECT, arg, 2);
                                } catch (IOException ex) {
                                    Logger.getLogger(Accueuil.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Connection échoué", Toast.LENGTH_SHORT).show();
                            }        
                        }else{
                            Toast.makeText(getApplicationContext(), "Mauvais format pseudo/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            
            creerCompte.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    initDialog();
                    adb.show();
                }
            });
            
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    public static void finCreatCompte(String message){
        Message msg = new Message();
        msg.obj = message;
        messageHandler.sendMessage(msg);
        connect.dispose();
    }
    
    public static void finConnectCompte(String message, boolean co){
        if(co){
            Message msg = new Message();
            msg.obj = message;
            messageHandler.sendMessage(msg);
        }else{
            Message msg = new Message();
            msg.obj = message;
            messageHandler.sendMessage(msg);
        }   
    }
    
    /**
     * Fonction permettant de se connecter.
     */
    private boolean connexion(){
        String pseudo = user.getText().toString();
        String password = pass.getText().toString();
        
        boolean retour = verifEnvoi.analysePseudo(pseudo) && verifEnvoi.analyseMDP(password);
        
        return retour;
    }
    
    /** 
     * Fonction permettant de récupérer le texte stocké dans le système de préférences<br/>
     * et de l'insérer dans la zone de texte. Si lors de la premiere utilisation le <br/>
     * système ne comprend aucun texte, on l'initialise avec une valeur par défault.
     * 
     * @author Jessy Bonnotte
     */
    private void initZone(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONNECT, 0);        
        if(!settings.getString("pseudo", "0").equals("0") && settings.getBoolean("memorise", false)){
            user.setText(settings.getString("pseudo", "0"));
            pass.setText(settings.getString("password", "0"));
            retenir.setChecked(settings.getBoolean("memorise", false));
        }    
    }
    
    /** 
     * Fonction permettant d'enregistrer le texte de la zone de texte dans le systeme<br/>
     * de préférences d'android.
     * 
     * @author Jessy Bonnotte
     */
    private void saveZone(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONNECT, 0);        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pseudo", user.getText().toString());
        editor.putString("password", pass.getText().toString());
        editor.putBoolean("memorise", retenir.isChecked());
        editor.commit();
    }
    
    private void ereaseZone(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONNECT, 0);        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pseudo", "0");
        editor.putString("password", "0");
        editor.putBoolean("memorise", false);
        editor.commit();
    }
}
