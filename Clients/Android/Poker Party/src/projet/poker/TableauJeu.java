package projet.poker;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.Connexion;
import projet.GestionConnexion.CreateurTram;

public class TableauJeu extends TabActivity
{
    // type de données
    public static final int                     MESSENGER = 0;
    public static final int                     DONNEES_JOUEUR = 1;
    public static final int                     CARTE_JOUEUR = 2;
    public static final int                     CARTE_TABLE = 3;
    public static final int                     JETON_TABLE = 4;
    public static final int                     FIN_TOUR = 5;
    public static final int                     MON_TOUR = 6;
    public static final int                     JOUEUR_COURANT =7;
    
    // variable d'affichage
    private ImageView[][]                       _imageView_TableauCarteJoueurs;
    private TextView[][]                        _textView_TableauInfosJoueurs; 
    private ImageView[]                         _imageView_TableauCartesCentre;
    private TextView                            _textView_Pot;
    private GestionImg                          _generateurImage;
    private ListView                            _listView_Messages;
    private EditText                            _editText_TexteAEnvoyer;
    private SeekBar                             _seekBar_BarChoixNBJeton;
    private EditText                            _editText_ChoixNBJeton;
    
    // Tous les boutons
    private Button                              _bouton_AfficherChat;
    private Button                              _bouton_FermerChat;
    private Button                              _bouton_QuitterPartie;
    private Button                              _bouton_EffacerMessenger;
    private Button                              _bouton_EnvoyerMessage;
    
    // Boutons de jeux
    private Button                              _bouton_Check;
    private Button                              _bouton_Bet;
    private Button                              _bouton_Call;
    private Button                              _bouton_Fold;

    // Le tabhost d'affichage
    private TabHost                             _tabHost_OngletsAffichage;
        
    // Contient le message temporaire
    private String[]                            _messageCourant = new String[2];
    
    // Liste des données
    private List<String[]>                      _tableauInfosJoueurs;
    private int[]                               _tableauCartesJoueur = new int[2];
    private String                              _nomUtilisateur;
    private int                                 _positionJoueurTableau = 0;
    private int[]                               _tableauCartesCentre = new int[5];
    private int                                 _montantPot;
    private String[]                            _choixPossibleJoueur = new String[3];
    private String                              _joueurCourant;
    
    //popup
    private AlertDialog.Builder                 _adb;

    // Création de la ArrayList qui nous permettra de remplire la listView
    private ArrayList<HashMap<String, String>>  _listeMessagesTransfert = new ArrayList<HashMap<String, String>>();
           
    // On déclare la HashMap qui contiendra les informations pour un item du messenger
    private HashMap<String, String>             _messageTemp;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler                      _messageHandler;
    private static Handler                      _resultHandler;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableau);
   
        // Init du generateur d'image
        _generateurImage = new GestionImg(getResources(), R.drawable.cartemodif);
        _imageView_TableauCarteJoueurs = new ImageView[8][2];
        _textView_TableauInfosJoueurs=new TextView[8][3];
        _imageView_TableauCartesCentre = new ImageView[5];
        
        initCartes();
        initInfo();
        effaceAffichage();
        
        // On informe la connexion qu'on a chargé une nouvelle activity courante
        Accueil.connexion.setActivity(Connexion.PARTIE);
        
        // On initialise le pseudo du joueur
        initPseudoJoueur();
        
        // on prepare le handler de retour de liste
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.arg1 == MESSENGER){
                    _messageCourant[0] = ((List<String[]>) msg.obj).get(0)[0];
                    _messageCourant[1] = ((List<String[]>) msg.obj).get(0)[1];
                    MAJMessenger();
                }else if(msg.arg1 == DONNEES_JOUEUR){
                    _tableauInfosJoueurs = ((List<String[]>) msg.obj);
                    ActDonneesJoueurs();
                }else if(msg.arg1 == CARTE_JOUEUR){
                    _tableauCartesJoueur[0] = Integer.parseInt(((List<String[]>) msg.obj).get(0)[0]);
                    _tableauCartesJoueur[1] = Integer.parseInt(((List<String[]>) msg.obj).get(0)[1]);
                    ActCartesUtilisateur();
                }else if(msg.arg1 == CARTE_TABLE){
                    for (int i = 0; i < ((List<String[]>) msg.obj).size(); i++) {
                        _tableauCartesCentre[i] = Integer.parseInt(((List<String[]>) msg.obj).get(i)[0]);
                    }
                    ActCarteTable();
                }else if(msg.arg1 == JETON_TABLE){
                    _montantPot = Integer.parseInt(((List<String[]>) msg.obj).get(0)[0]);
                    ActPotTable();
                }else if(msg.arg1 == FIN_TOUR){
                    Toast.makeText(getApplicationContext(), "Gagnant : "+((List<String[]>) msg.obj).get(0)[0], Toast.LENGTH_SHORT).show();
                    initManche();
                    ActCarteTable();
                    ActPotTable();
                    ActCartesUtilisateur();
                }else if(msg.arg1 == MON_TOUR){
                    _choixPossibleJoueur[0] = ((List<String[]>) msg.obj).get(0)[0]; // jeton min
                    _choixPossibleJoueur[1] = ((List<String[]>) msg.obj).get(0)[1]; // jeton max
                    _choixPossibleJoueur[2] = ((List<String[]>) msg.obj).get(0)[2]; // relancé? true ou false
                    enableButton();
                    Toast.makeText(getApplicationContext(),"a toi !!!", Toast.LENGTH_SHORT).show();
                }else if(msg.arg1 == JOUEUR_COURANT){
                    _joueurCourant = ((List<String[]>) msg.obj).get(0)[0];
                    setJoueurCourant();
                }
                
                //listPartie = new ArrayList<String[]>((List<String[]>)msg.obj);
                //MAJAffichage();
            }
        };
        
        // on prepare le handler de retour message
        _resultHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("EXITOK")){
                    finish();
                }else if(msg.obj.toString().equals("DEBUTPARTIE")){
                    for (int i = 0; i < 5; i++) {
                        _tableauCartesCentre[i] = -1;
                    }
                }else if(msg.obj.toString().equals("JOK")){
                    dislableButton();
                }else if(msg.obj.toString().equals("EXITOK")){
                    finish();
                }
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        
        getWindow().setBackgroundDrawableResource(R.drawable.table);

        _tabHost_OngletsAffichage = getTabHost();
        _tabHost_OngletsAffichage.addTab(_tabHost_OngletsAffichage.newTabSpec("tab1").setIndicator("test 1").setContent(R.id.rel1));
        _tabHost_OngletsAffichage.addTab(_tabHost_OngletsAffichage.newTabSpec("tab1").setIndicator("test 1").setContent(R.id.rel2));
        // mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Onglet 1").setContent(R.id.rel1));
        // mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Onglet 2").setContent(R.id.rel1));
        _tabHost_OngletsAffichage.setCurrentTab(0);
        
        // Ouverture du chat
        _bouton_AfficherChat = (Button) findViewById(R.id.chat1);  
        _bouton_AfficherChat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                _tabHost_OngletsAffichage.setCurrentTab(1);
                getWindow().setBackgroundDrawableResource(R.drawable.fond);
            }
        });
        
        
        // Fermeture du chat
        _bouton_FermerChat = (Button) findViewById(R.id.retour);
        _bouton_FermerChat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                _tabHost_OngletsAffichage.setCurrentTab(0);
                getWindow().setBackgroundDrawableResource(R.drawable.table);
            }
        });
        
        // Envoi du message
        _editText_TexteAEnvoyer = (EditText) findViewById(R.id.message);
        _bouton_EnvoyerMessage = (Button) findViewById(R.id.send);
        _bouton_EnvoyerMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if(!_editText_TexteAEnvoyer.getText().toString().equals("")){
                    String[] arg = new String[2];
                    arg[0] = _nomUtilisateur;
                    arg[1] = _editText_TexteAEnvoyer.getText().toString();
                    try {
                        Accueil.createurTram.setTram(CreateurTram.MESSENGER, arg, 2);
                    } catch (IOException ex) {
                        Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    _editText_TexteAEnvoyer.setText("");
                }
            }
        });
      
        // Quitter partie
        _bouton_QuitterPartie = (Button) findViewById(R.id.quit1);
        _bouton_QuitterPartie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    Accueil.createurTram.setTram(CreateurTram.EXIT_PARTIE);
                } catch (IOException ex) {
                    Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // Effacer messenger
        _bouton_EffacerMessenger = (Button) findViewById(R.id.effacer);
        _bouton_EffacerMessenger.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                _listeMessagesTransfert.clear();
                _listeMessagesTransfert.notifyAll();
            }
        });
    
        try {
            Accueil.createurTram.setTram(CreateurTram.PRET);
        } catch (IOException ex) {
            Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        initManche();
        _listView_Messages = (ListView) findViewById(R.id.listviewmessenger);
        
        
        //check.
        _bouton_Check=(Button) findViewById(R.id.check);
        _bouton_Check.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    Accueil.createurTram.setTram(CreateurTram.CHECK);
                    dislableButton();
                } catch (IOException ex) {
                    Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        _bouton_Call=(Button) findViewById(R.id.call);
        _bouton_Call.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                     String[] arg = new String[2];
            
                arg[0]="2";
                arg[1] ="0";
                    Accueil.createurTram.setTram(CreateurTram.CALL,arg,2);
                    dislableButton();
                } catch (IOException ex) {
                    Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        
        _bouton_Fold=(Button) findViewById(R.id.fold);
        _bouton_Fold.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                String[] arg = new String[2];            
                arg[0]="1";
                arg[1] ="0";
                    Accueil.createurTram.setTram(CreateurTram.FOLD,arg,2);
                    dislableButton();
                } catch (IOException ex) {
                    Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
     
        
        
        _bouton_Bet=(Button) findViewById(R.id.bet);
        _bouton_Bet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                initBet();
                _adb.show();
            }
        });
    }
    
    /**
     * Fonction permettant de changer la couleur du joueur courant.
     * 
     * @author Jessy Bonnotte
     */
    private void setJoueurCourant(){
        for (int i = 0; i < 8; i++) {
            if(_textView_TableauInfosJoueurs[i][0].getText().toString().equals(_joueurCourant)){
                _textView_TableauInfosJoueurs[i][0].setTextColor(Color.GREEN);
            }else{
                _textView_TableauInfosJoueurs[i][0].setTextColor(Color.RED);
            }       
        }
    }
    
    /**
     * Fonction permettant d'activer les boutons en fonction des choix possibles
     * 
     * @author Jessy Bonnotte && Polizzi Mathieu
     */   
    private void dislableButton(){
        _bouton_Bet.setEnabled(false);
        _bouton_Call.setEnabled(false);
        _bouton_Check.setEnabled(false);
        _bouton_Fold.setEnabled(false);
        _bouton_Bet.setVisibility(1);
        _bouton_Call.setVisibility(1);
        _bouton_Check.setVisibility(1);
        _bouton_Fold.setVisibility(1);
    }
    
    /**
     * Fonction permettant de désactiver les boutons en fonction des choix possibles
     * 
     * @author Jessy Bonnotte && Polizzi Mathieu
     */   
    private void enableButton(){
       if(_choixPossibleJoueur[2].equals("true")){  
            _bouton_Bet.setEnabled(true);
            _bouton_Check.setEnabled(true);
            _bouton_Fold.setEnabled(true);
            _bouton_Call.setEnabled(true);
            _bouton_Bet.setVisibility(0);
            _bouton_Check.setVisibility(0);
            _bouton_Fold.setVisibility(0);
            _bouton_Call.setVisibility(0);
        }else{
            _bouton_Check.setEnabled(true);
            _bouton_Call.setEnabled(true);
            _bouton_Fold.setEnabled(true);
            _bouton_Check.setVisibility(0);
            _bouton_Call.setVisibility(0);
            _bouton_Fold.setVisibility(0);
        }     
    }
    
    /**
     * Fonction permettant d'initialiser la manche. Elleremet tout les tableau et les variables de la partie a 0.
     * 
     * @author Jessy Bonnotte
     */
    private void initManche(){
        for (int i = 0; i < 5; i++) {
            _tableauCartesCentre[i] = -1;
        }
        _montantPot = 0;
        _tableauCartesJoueur[0] = -1;
        _tableauCartesJoueur[1] = -1;
    }
    
    /**
     * Fonction permettant de mettre a jour le messenger avec les messages reçu.
     * 
     * @author Jessy bonnotte
     */
    private void MAJMessenger(){
        _messageTemp = new HashMap<String, String>();
        //on insère un élément partyname que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
        _messageTemp.put("pseudo", _messageCourant[0]);
        //on insère un élément nbplayers que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
        _messageTemp.put("message", _messageCourant[1]);
        _listeMessagesTransfert.add(_messageTemp);
        
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), _listeMessagesTransfert, R.layout.messengeritem,
        new String[] {"pseudo", "message"}, new int[] { R.id.pseudomsg, R.id.messagemsg});
        _listView_Messages.setAdapter(mSchedule);  
    }
    
    /**
     * Fonction permettant de lier les carte du XML avec le code
     * 
     * @author Jessy Bonnotte
     */
    private void initCartes(){
        _imageView_TableauCarteJoueurs[0][0] = (ImageView) findViewById(R.id.c1p1);
        _imageView_TableauCarteJoueurs[0][1] = (ImageView) findViewById(R.id.c2p1);
        
        _imageView_TableauCarteJoueurs[1][0] = (ImageView) findViewById(R.id.c1p2);
        _imageView_TableauCarteJoueurs[1][1] = (ImageView) findViewById(R.id.c2p2);
        
        _imageView_TableauCarteJoueurs[2][0] = (ImageView) findViewById(R.id.c1p3);
        _imageView_TableauCarteJoueurs[2][1] = (ImageView) findViewById(R.id.c2p3);
        
        _imageView_TableauCarteJoueurs[3][0] = (ImageView) findViewById(R.id.c1p4);
        _imageView_TableauCarteJoueurs[3][1] = (ImageView) findViewById(R.id.c2p4);
        
        _imageView_TableauCarteJoueurs[4][0] = (ImageView) findViewById(R.id.c1p5);
        _imageView_TableauCarteJoueurs[4][1] = (ImageView) findViewById(R.id.c2p5);
        
        _imageView_TableauCarteJoueurs[5][0] = (ImageView) findViewById(R.id.c1p6);
        _imageView_TableauCarteJoueurs[5][1] = (ImageView) findViewById(R.id.c2p6);
        
        _imageView_TableauCarteJoueurs[6][0] = (ImageView) findViewById(R.id.c1p7);
        _imageView_TableauCarteJoueurs[6][1] = (ImageView) findViewById(R.id.c2p7);
        
        _imageView_TableauCarteJoueurs[7][0] = (ImageView) findViewById(R.id.c1p8);
        _imageView_TableauCarteJoueurs[7][1] = (ImageView) findViewById(R.id.c2p8);
        
        _imageView_TableauCartesCentre[0] = (ImageView) findViewById(R.id.ct1);
        _imageView_TableauCartesCentre[1] = (ImageView) findViewById(R.id.ct2);
        _imageView_TableauCartesCentre[2] = (ImageView) findViewById(R.id.ct3);
        _imageView_TableauCartesCentre[3] = (ImageView) findViewById(R.id.ct4);
        _imageView_TableauCartesCentre[4] = (ImageView) findViewById(R.id.ct5);
    }
    
    /**
     * Fonction permettant de lier les objets du XML avec le code
     * 
     * @author Jessy Bonnotte
     */
    private void initInfo(){
        _textView_TableauInfosJoueurs[0][0] = (TextView) findViewById(R.id.pseudop1);
        _textView_TableauInfosJoueurs[0][1] = (TextView) findViewById(R.id.cashp1);
        _textView_TableauInfosJoueurs[0][2] = (TextView) findViewById(R.id.betp1);
        
        _textView_TableauInfosJoueurs[1][0] = (TextView) findViewById(R.id.pseudop2);
        _textView_TableauInfosJoueurs[1][1] = (TextView) findViewById(R.id.cashp2);
        _textView_TableauInfosJoueurs[1][2] = (TextView) findViewById(R.id.betp2);
        
        _textView_TableauInfosJoueurs[2][0] = (TextView) findViewById(R.id.pseudop3);
        _textView_TableauInfosJoueurs[2][1] = (TextView) findViewById(R.id.cashp3);
        _textView_TableauInfosJoueurs[2][2] = (TextView) findViewById(R.id.betp3);
        
        _textView_TableauInfosJoueurs[3][0] = (TextView) findViewById(R.id.pseudop4);
        _textView_TableauInfosJoueurs[3][1] = (TextView) findViewById(R.id.cashp4);
        _textView_TableauInfosJoueurs[3][2] = (TextView) findViewById(R.id.betp4);
        
        _textView_TableauInfosJoueurs[4][0] = (TextView) findViewById(R.id.pseudop5);
        _textView_TableauInfosJoueurs[4][1] = (TextView) findViewById(R.id.cashp5);
        _textView_TableauInfosJoueurs[4][2] = (TextView) findViewById(R.id.betp5);
        
        _textView_TableauInfosJoueurs[5][0] = (TextView) findViewById(R.id.pseudop6);
        _textView_TableauInfosJoueurs[5][1] = (TextView) findViewById(R.id.cashp6);
        _textView_TableauInfosJoueurs[5][2] = (TextView) findViewById(R.id.betp6);
        
        _textView_TableauInfosJoueurs[6][0] = (TextView) findViewById(R.id.pseudop7);
        _textView_TableauInfosJoueurs[6][1] = (TextView) findViewById(R.id.cashp7);
        _textView_TableauInfosJoueurs[6][2] = (TextView) findViewById(R.id.betp7);
        
        _textView_TableauInfosJoueurs[7][0] = (TextView) findViewById(R.id.pseudop8);
        _textView_TableauInfosJoueurs[7][1] = (TextView) findViewById(R.id.cashp8);
        _textView_TableauInfosJoueurs[7][2] = (TextView) findViewById(R.id.betp8);
        
        _textView_Pot = (TextView) findViewById(R.id.pot);
        
        for (int i = 0; i < 8; i++) {
            _textView_TableauInfosJoueurs[i][0].setTextColor(Color.WHITE);
            _textView_TableauInfosJoueurs[i][1].setTextColor(Color.BLACK);
            _textView_TableauInfosJoueurs[i][2].setTextColor(Color.RED);
            
            _textView_TableauInfosJoueurs[i][0].setTypeface(null, Typeface.BOLD_ITALIC);
            _textView_TableauInfosJoueurs[i][1].setTypeface(null, Typeface.BOLD_ITALIC);
            _textView_TableauInfosJoueurs[i][2].setTypeface(null, Typeface.BOLD_ITALIC);
        }
        
        _textView_Pot = (TextView) findViewById(R.id.pot);
    }
    
    /**
     * Fonction appelé par l'analyseur de tram pour informaer l'utilisateur des infos envoyés par le serveur.
     * 
     * @author Mathieu Polizzi
     * 
     * @param message - le message de retour du serveur
     */
    public static void MAJLIST(int typeListe, List<String[]> donnees){
        Message msg = new Message();
        msg.arg1 = typeListe;
        msg.obj = donnees;
        _messageHandler.sendMessage(msg);
    }
    
    /**
     * Fonction appelé par l'analyseur de tram pour informaer l'utilisateur des infos envoyés par le serveur.
     * 
     * @author Mathieu Polizzi
     * 
     * @param message - le message de retour du serveur
     */
    public static void afficheMessage(String message){
        Message msg = new Message();
        msg.obj = message;
        _resultHandler.sendMessage(msg);
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
    
    /**
     * Fonction permettant d'actualiser les données des joueurs. (Pseudo/jeton/pot)
     * 
     * @author Jessy Bonnotte
     */
    private void ActDonneesJoueurs(){
        for (int i = 0; i < 8; i++) {
            if(i < _tableauInfosJoueurs.size()){
                _textView_TableauInfosJoueurs[i][0].setText(_tableauInfosJoueurs.get(i)[0]);
                if(_tableauInfosJoueurs.get(i)[0].equals(_nomUtilisateur)){
                    _positionJoueurTableau = new Integer(i);
                }
                _textView_TableauInfosJoueurs[i][1].setText(_tableauInfosJoueurs.get(i)[1]);
                _textView_TableauInfosJoueurs[i][2].setText(_tableauInfosJoueurs.get(i)[2]);
                if(i  != _positionJoueurTableau){
                    _imageView_TableauCarteJoueurs[i][0].setImageResource(R.drawable.cards);
                    _imageView_TableauCarteJoueurs[i][1].setImageResource(R.drawable.cards);
                }
            }else{
                _textView_TableauInfosJoueurs[i][0].setText("");
                _textView_TableauInfosJoueurs[i][1].setText("");
                _textView_TableauInfosJoueurs[i][2].setText("");
            }
        }
    }
    
    /**
     * Per met d'actualiser les carte du joueur
     * 
     * @author Jessy Bonnotte
     */
    private void ActCartesUtilisateur(){
        if(_tableauCartesJoueur[0] > -1 && _tableauCartesJoueur[1] > -1){
            _imageView_TableauCarteJoueurs[_positionJoueurTableau][0].setImageBitmap(_generateurImage.getImage(_tableauCartesJoueur[0] / 4, _tableauCartesJoueur[0] % 4));
            _imageView_TableauCarteJoueurs[_positionJoueurTableau][1].setImageBitmap(_generateurImage.getImage(_tableauCartesJoueur[1] / 4, _tableauCartesJoueur[1] % 4));
        }else{
            _imageView_TableauCarteJoueurs[_positionJoueurTableau][0].setImageResource(R.drawable.cards);
            _imageView_TableauCarteJoueurs[_positionJoueurTableau][1].setImageResource(R.drawable.cards);
        }
    }
    
    /**
     * Fonction qui permet d'actualiser les cartes de la table.
     * 
     * @author Jessy Bonnotte
     */
    private void ActCarteTable(){
      /*  int i = 0;
        while(carteTable[i] != -1 && i < 5){
            cartesCentre[i].setImageBitmap(img.getImage(carteTable[i] / 4, carteTable[i] % 4));
            i++;
        }*/
        for(int i=0; i<5;i++){
            if(_tableauCartesCentre[i]!=-1){
                Log.v("remplissage","remplissage : "+_tableauCartesCentre[i]+"");
                _imageView_TableauCartesCentre[i].setImageBitmap(_generateurImage.getImage(_tableauCartesCentre[i]/4,_tableauCartesCentre[i]%4));
            }else {
                _imageView_TableauCartesCentre[i].setImageResource(R.drawable.cards);
            }
        }
    }
    
    /**
     * Fonction permettant de mettre a jour le montant du pot.
     * 
     * @author Jessy Bonnotte
     */
    private void ActPotTable(){
        _textView_Pot.setText("pot : "+_montantPot+"$");
    }
    
    /**
     * Fonction servant a initialiser la fenêtre popup du bouton relancer et gérer la relance.
     * 
     * @author Mathieu Polizzi
     */
    private void initBet(){
        
        //On instancie notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.relancer, null);

        final int borne = Integer.parseInt(_choixPossibleJoueur[0]);

        //Liaison du xml
        final TextView printJetonSelect = (TextView) alertDialogView.findViewById(R.id.printJetonSelect);
        final TextView printJetonPlayer = (TextView) alertDialogView.findViewById(R.id.myjetons);
        final SeekBar seekJetons = (SeekBar) alertDialogView.findViewById(R.id.seekJetons);
        printJetonPlayer.setText(_tableauInfosJoueurs.get(_positionJoueurTableau)[1].toString());
        printJetonSelect.setText("Relance à:  " );
        seekJetons.setMax(Integer.parseInt(_tableauInfosJoueurs.get(_positionJoueurTableau)[1]));
        seekJetons.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

                try {
                        
                    printJetonSelect.setText("Relance à: "+Integer.toString(progress + borne));

                } catch (Exception e) {

                }
            }

            public void onStartTrackingTouch(SeekBar arg0) {

            }

            public void onStopTrackingTouch(SeekBar arg0) {

            }
        });

        //Création de l'AlertDialog
        _adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        _adb.setView(alertDialogView);

        //On donne un titre à l'AlertDialog
        _adb.setTitle("Relancer");

        //On modifie l'icône de l'AlertDialog pour le fun ;)
        _adb.setIcon(R.drawable.add);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        _adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String[] arg = new String[2];
                // CHOIX@3@NombreJeton
                arg[0]="3";
                arg[1] =""+seekJetons.getProgress();

                 
                try {
                    Accueil.createurTram.setTram(CreateurTram.JOUER, arg, 2);
                    dislableButton();
                } catch (IOException ex) {
                    Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    }
    
    private void effaceAffichage(){
        for (int i = 0; i < 8; i++) {
            // On efface les données des joueurs
            _textView_TableauInfosJoueurs[i][0].setText("");
            _textView_TableauInfosJoueurs[i][1].setText("");
            _textView_TableauInfosJoueurs[i][2].setText("");
            
            // On efface les données de la table
            _textView_Pot.setText("");
            
            // On efface les carte joueur
            _imageView_TableauCarteJoueurs[i][0].setImageBitmap(_generateurImage.getImage(-1, 0));
            _imageView_TableauCarteJoueurs[i][1].setImageBitmap(_generateurImage.getImage(-1, 0));
        }
        
        for (int i = 0; i < 5; i++) {
            // On efface les carte du jeu
            _imageView_TableauCartesCentre[i].setImageBitmap(_generateurImage.getImage(-1, 0));
        }
    }
}

