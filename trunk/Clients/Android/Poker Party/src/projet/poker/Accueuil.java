package projet.poker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
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
    private Button                  param;
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
    
    // Paratmètres de connexion
    private String                  ip;
    private String                  port;
    
    // Pour la boite de dialog 
    AlertDialog.Builder             adb;
    
    // Handler pour sortir les messages de la partie static
    private static Handler          messageHandler;
    
    /**
     * Référence vers le texte stocké dans le système de préférences
     */
    public static final String      PREFS_CONNECT = "preferenceConnect";
    
    /**
     * Référence vers la configuration ip pour la connexion
     */
    public static final String      PREFS_CONFIG = "preferenceConfig";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        Log.v("starting", "create");
        //i = new Intent (getApplicationContext(), projet.poker.SplashScreen.class);
      	//startActivity(i);
        setContentView(R.layout.main);
        
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("Connexion etabli")){
                    i = new Intent (getApplicationContext(), ListePartie.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
            param.setEnabled(false);
            param.setVisibility(View.INVISIBLE);
        }
        initZone();
    }
    
    /**
     * Fonction appelé lors du cherchement de l'appli.
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void onResume(){
        super.onResume();
        connect.setActivity(Connection.ACCUEUIL); 
        chargerParamReseau();
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
        if ((keyCode == KeyEvent.KEYCODE_MENU) && (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 
     */
    private void chargerParamReseau(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONFIG, 0);        
        if(settings.getString("ip", "0").equals("0") || settings.getString("port", "0").equals("0")){
            initDialogIPPort();
            adb.show();
        }
               
        ip = settings.getString("ip", "0");
        port = settings.getString("port", "0");
        Log.v("connect", ip+" "+port);
    }
    
    /** 
     * Surcharge de la fonction onCreateOptionsMenu() afin de créer le menu au clique<br/>
     * de l'utilisateur sur la touche menu
     * 
     * @author Jessy Bonnotte
     * 
     * @param menu - le menu qui est demandé
     * 
     * @return boolean - resultat de l'affichage
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
     * @author Jessy Bonnotte
     * 
     * @param item - l'élément sur lequel l'utilisateur a cliqué.
     * 
     * @return boolean - retourne true si le parametre existe ou false sinon.
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
    
    /**
     * Fonction permettant de lier le code avec le fichier xml de l'interface.
     * 
     * @author Jessy Bonnotte
     * 
     * @return boolean - resultat de la liaison avec le XML
     */
    private boolean liaisonXML(){
        try{
            pass = (EditText) findViewById(R.id.mdp);
            user = (EditText) findViewById(R.id.pseudo);
            connection = (Button) findViewById(R.id.connect);
            creerCompte = (Button) findViewById(R.id.createcpt);
            retenir = (CheckBox) findViewById(R.id.memorize); 
            param = (Button) findViewById(R.id.param);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    /**
     * Fonction permettant d'initialiser la boite de dialoque pour la création
     * d'un compte.
     * 
     * @author Jessy bonnotte
     * 
     * @return boolean - resultat de l'initialisation de la dialogBox
     */
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

                        if(connect.init(ip, port)){
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
    
    /**
     * Fonction permattant d'initialiser l'action des objets de l'interface
     * 
     * @author Jessy bonnotte
     * 
     * @return boolean - resultat de l'initialisation des objets
     */
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
                            if(connect.init(ip, port)){
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
            
            param.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Accueuil.this.openOptionsMenu();
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
    
    /**
     * Fonction permettant de mettre a jour l'interface depuis la classe connexion.
     * 
     * @author Jessy Bonnotte
     * 
     * @param message - le message a afficher 
     */
    public static void finCreatCompte(String message){
        Message msg = new Message();
        msg.obj = message;
        messageHandler.sendMessage(msg);
        Accueuil.connect.dispose();
    }
    
    /**
     * Fonction permettant a l'analyseur de tram d'actualiser la fenetre pour une demande de connexion.
     * Prends en parametre un boolean qui renseigne si la connexion est effective ou non et le message a afficher.
     * 
     * @author Jessy Bonnotte
     * 
     * @param message - le message a afficher a l'ecran
     * @param co - statut de la connexion
     */
    public static void finConnectCompte(String message, boolean co){
        if(co){
            Message msg = new Message();
            msg.obj = message;
            messageHandler.sendMessage(msg);
        }else{
            Message msg = new Message();
            msg.obj = message;
            messageHandler.sendMessage(msg);
            Accueuil.connect.dispose();
        }   
    }
    
    /**
     * Fonction permettant de verifier les informations de connexions.
     * Verifie le format du pseudo et du mot de pass.
     * 
     * @author Jessy Bonnotte
     * 
     * @return boolean - si le format est le bon ou non
     */
    private boolean connexion(){
        String pseudo = user.getText().toString();
        String password = pass.getText().toString();
        
        boolean retour = verifEnvoi.analysePseudo(pseudo) && verifEnvoi.analyseMDP(password);
        
        return retour;
    }
    
    /** 
     * Fonction permettant de récupérer le texte stocké dans le système de préférences et de l'insérer dans la zone de texte. Si lors de la premiere utilisation le système ne comprend aucun texte, on l'initialise avec une valeur par défault.
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
     * Fonction permettant d'enregistrer le texte de la zone de texte dans le systeme de préférences d'android.
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
    
    /**
     * Fonction permettant de réinitialiser la sauvegarde de mot de pass et du pseudo.
     * 
     * @author Jessy Bonnotte
     */
    private void ereaseZone(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONNECT, 0);        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pseudo", "0");
        editor.putString("password", "0");
        editor.putBoolean("memorise", false);
        editor.commit();
    }
    
     private boolean initDialogIPPort(){
        try{
            //On instancie notre layout en tant que View
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.popupipport, null);                 
            //Liaison du xml
            final EditText ip = (EditText) alertDialogView.findViewById(R.id.ip);
            final EditText port = (EditText) alertDialogView.findViewById(R.id.port);
            //Création de l'AlertDialog
            adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            adb.setTitle("Parametrage reseau");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            adb.setIcon(R.drawable.info);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences settings = getSharedPreferences(Accueuil.PREFS_CONFIG, 0);        
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("ip", ip.getText().toString());
                    editor.putString("port", port.getText().toString());
                    editor.commit();
                    dialog.cancel();
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
