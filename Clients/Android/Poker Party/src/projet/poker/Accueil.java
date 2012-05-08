package projet.poker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.AnalyseurEnvoi;
import projet.GestionConnexion.Connexion;
import projet.GestionConnexion.CreateurTram;

/**
 * Classe d'accueil de l'application. C'est l'activity qui est appeler au démarrage de l'application. Cette option est définis dans le manifest xml.
 * 
 * @author Jessy Bonnotte & Mathieu Polizzi
 */
public class Accueil extends Activity{
    
    /**
     * Gestion de la connexion 
     */
    public static Connexion         connexion;
     
    /**
     * Variable static servant a creer les tram
     */
    public static CreateurTram      createurTram;
    
    // Gestion des éléments graphiques
    private Button                  _bouton_Parametre;
    private Button                  _bouton_Connexion;
    private Button                  _bouton_CreerCompte;
    private CheckBox                _checkBox_Retenir;
    private EditText                _editText_Utilisateur;
    private EditText                _editText_Password;
    
    // Ouverture d'une nouvelle activity
    private Intent                  _i;
    
    // Verification pseudo/mot de passe
    private AnalyseurEnvoi          _analyseurEnvoi;  
    
    // gestion des éléments du menu paramètre
    private final int               _PARAMETRE = 1;
    private final int               _A_PROPOS = 2;
    private final int               _QUITTER = 3;
    
    // Paratmètres de connexion
    private String                  _ip;
    private String                  _port;
    
    // Pour la boite de dialog 
    private AlertDialog.Builder     _adb;
    
    // Handler pour sortir les messages de la partie static
    private static Handler          _messageHandler;
    
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
        
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("Connexion etabli")){
                    _i = new Intent (getApplicationContext(), ListePartie.class);
                    startActivity(_i);
                }else{
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        createurTram = new CreateurTram();
        connexion = new Connexion();
        _analyseurEnvoi = new AnalyseurEnvoi();
        getWindow().setBackgroundDrawableResource(R.drawable.fond);
        
        if(!liaisonXML()){
            Toast.makeText(this, "Problème a la liaison avec le XML", Toast.LENGTH_SHORT).show();
        }
        
        if(!initObjet()){
            Toast.makeText(this, "Problème a l'initialisation des objets", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
            _bouton_Parametre.setEnabled(false);
            _bouton_Parametre.setVisibility(View.INVISIBLE);
        }
        initZone();
    }
    
    /**
     * Fonction appelé lors du chargement de l'appli.
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void onResume(){
        super.onResume();
        connexion.setActivity(Connexion.ACCUEUIL); 
        chargerParamReseau();
    }
    
    /**
     * Deroutement des fonctions de base du bouton MENU.
     * 
     * @author Jessy Bonnotte
     * 
     * @param keyCode - le code de la touche pressé
     * @param event - l'evenement a faire de la touche pressé
     * 
     * @return boolean - si la touche est a prendre en compte ou pas.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_MENU) && (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1)) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Fonction permettant de charger les parametres reseaux. La fonction lit le systeme de preference et recupere l'adresse et le port du serveur necessaire a la connexion.
     * 
     * @author Mathieu Polizzi
     */
    private void chargerParamReseau(){
        SharedPreferences settings = getSharedPreferences(PREFS_CONFIG, 0);        
        if(settings.getString("ip", "0").equals("0") || settings.getString("port", "0").equals("0")){
            initDialogIPPort();
            _adb.show();
        }
               
        _ip = settings.getString("ip", "0");
        _port = settings.getString("port", "0");
        Log.v("connect", _ip+" "+_port);
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
        menu.add(0, _PARAMETRE, 0, "Paramètre");
        menu.findItem(_PARAMETRE).setIcon(R.drawable.settings);
        menu.add(0, _A_PROPOS, 0, "A propos");
        menu.findItem(_A_PROPOS).setIcon(R.drawable.info);
        menu.add(0, _QUITTER, 0, "Quitter");
        menu.findItem(_QUITTER).setIcon(R.drawable.exit);
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
        case _PARAMETRE:
            _i = new Intent (getApplicationContext(), Parametre.class);
      	    startActivity(_i);
            return true;
        case _A_PROPOS:
            _i = new Intent (getApplicationContext(), APropos.class);
            startActivity(_i);
            return true;
        case _QUITTER:
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
            _editText_Password = (EditText) findViewById(R.id.mdp);
            _editText_Utilisateur = (EditText) findViewById(R.id.pseudo);
            _bouton_Connexion = (Button) findViewById(R.id.connect);
            _bouton_CreerCompte = (Button) findViewById(R.id.createcpt);
            _checkBox_Retenir = (CheckBox) findViewById(R.id.memorize); 
            _bouton_Parametre = (Button) findViewById(R.id.param);
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
            _adb = new AlertDialog.Builder(this);

            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);

            //On donne un titre à l'AlertDialog
            _adb.setTitle("Créer compte");

            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.add);

            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
             
            //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
            _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    if(verifEnv.analysePseudo(pseudoCRT.getText().toString()) && verifEnv.analyseMDP(mdpCRT.getText().toString())){
                        String[] arg = new String[2];
                        arg[0] = pseudoCRT.getText().toString();
                        arg[1] = mdpCRT.getText().toString();

                        if(connexion.init(_ip, _port)){
                            try {
                                createurTram.setTram(CreateurTram.CREATECPT, arg, 2);
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
     * Fonction permattant d'initialiser l'action des objets de l'interface
     * 
     * @author Jessy bonnotte
     * 
     * @return boolean - resultat de l'initialisation des objets
     */
    private boolean initObjet(){
        try{
            _bouton_Connexion.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_SHORT).show();
                    if(_editText_Utilisateur.getText().toString().equals("") || _editText_Password.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner les champs", Toast.LENGTH_SHORT).show();
                    }else{ 
                        //if(checkBox_Retenir.isChecked()){
                            saveZone();
                        /*}else if(!checkBox_Retenir.isChecked()){
                            ereaseZone();
                        }*/
                        if(connexion()){
                            String[] arg = new String[2];
                            arg[0] = _editText_Utilisateur.getText().toString();
                            arg[1] = _editText_Password.getText().toString();
                            if(connexion.init(_ip, _port)){
                                try {
                                    createurTram.setTram(CreateurTram.CONNECT, arg, 2);
                                } catch (IOException ex) {
                                    Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
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
            
            _bouton_Parametre.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    Accueil.this.openOptionsMenu();
                }
            });
            
            _bouton_CreerCompte.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {  
                    initDialog();
                    _adb.show();
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
        _messageHandler.sendMessage(msg);
        Accueil.connexion.dispose();
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
            _messageHandler.sendMessage(msg);
        }else{
            Message msg = new Message();
            msg.obj = message;
            _messageHandler.sendMessage(msg);
            Accueil.connexion.dispose();
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
        String pseudo = _editText_Utilisateur.getText().toString();
        String password = _editText_Password.getText().toString();
        
        boolean retour = _analyseurEnvoi.analysePseudo(pseudo) && _analyseurEnvoi.analyseMDP(password);
        
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
            _editText_Utilisateur.setText(settings.getString("pseudo", "0"));
            _editText_Password.setText(settings.getString("password", "0"));
            _checkBox_Retenir.setChecked(settings.getBoolean("memorise", false));
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
        editor.putString("pseudo", _editText_Utilisateur.getText().toString());
        editor.putString("password", _editText_Password.getText().toString());
        editor.putBoolean("memorise", _checkBox_Retenir.isChecked());
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
            _adb = new AlertDialog.Builder(this);
            //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
            _adb.setView(alertDialogView);
            //On donne un titre à l'AlertDialog
            _adb.setTitle("Parametrage reseau");
            //On modifie l'icône de l'AlertDialog pour le fun ;)
            _adb.setIcon(R.drawable.info);
            final AnalyseurEnvoi verifEnv = new AnalyseurEnvoi();
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
            return false;
        }
        return true;
    }
}
