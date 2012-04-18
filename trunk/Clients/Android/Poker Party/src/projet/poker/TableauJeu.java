package projet.poker;

import android.app.TabActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet.GestionConnexion.Connection;
import projet.GestionConnexion.CreateurTram;

public class TableauJeu extends TabActivity
{
    // type de données
    public static final int             MESSENGER = 0;
    public static final int             DONNEES_JOUEUR = 1;
    public static final int             CARTE_JOUEUR = 2;
    public static final int             CARTE_TABLE = 3;
    public static final int             JETON_TABLE = 4;
    
    // variable d'affichage
    private ImageView[][]               cartes;
    private TextView[][]                infoPlayers; 
    private ImageView[]                 cartesCentre;
    private TextView                    pot;
    private GestionImg                  img;
    private ListView                    maList;
    private EditText                    textAEnvoyer;
    
    // Tous les boutons
    private Button                      chat;
    private Button                      exitChat;
    private Button                      exitPartie;
    private Button                      effaceMessenger;
    private Button                      envoiMessage;
    
    // Le tabhost d'affichage
    private TabHost                     mTabHost;
    
    // Contient le message temporaire
    private String[]                    messageCourant = new String[2];
    
    // Liste des données
    private List<String[]>              listDonnees;
    private int[]                       carteJoueur = new int[2];
    private String                      nomUtil;
    private int                         posJoueur = 0;
    private int[]                       carteTable = new int[5];
    private int                         potText;

    // Création de la ArrayList qui nous permettra de remplire la listView
    ArrayList<HashMap<String, String>>  listItem = new ArrayList<HashMap<String, String>>();
           
    // On déclare la HashMap qui contiendra les informations pour un item
    HashMap<String, String>             map;
    
    // Handlers permettant de sortir les message serveur des fonctions static
    private static Handler              messageHandler;
    private static Handler              resultHandler;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableau);
   
        // On informe la connexion qu'on a chargé une nouvelle activity courante
        Accueuil.connect.setActivity(Connection.PARTIE);
        
        // On initialise le pseudo du joueur
        initPseudoJoueur();
        
        // on prepare le handler de retour de liste
        messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.arg1 == MESSENGER){
                    messageCourant[0] = ((List<String[]>) msg.obj).get(0)[0];
                    messageCourant[1] = ((List<String[]>) msg.obj).get(0)[1];
                    MAJMessenger();
                }else if(msg.arg1 == DONNEES_JOUEUR){
                    listDonnees = ((List<String[]>) msg.obj);
                    ActDonneesJoueurs();
                }else if(msg.arg1 == CARTE_JOUEUR){
                    carteJoueur[0] = Integer.parseInt(((List<String[]>) msg.obj).get(0)[0]) - 1;
                    carteJoueur[1] = Integer.parseInt(((List<String[]>) msg.obj).get(0)[1]) - 1;
                    initManche();
                    ActCartesUtilisateur();
                }else if(msg.arg1 == CARTE_TABLE){
                    for (int i = 0; i < ((List<String[]>) msg.obj).size(); i++) {
                        carteTable[i] = Integer.parseInt(((List<String[]>) msg.obj).get(i)[0]) -1;
                    }
                    ActCarteTable();
                }else if(msg.arg1 == JETON_TABLE){
                    potText = Integer.parseInt(((List<String[]>) msg.obj).get(0)[0]);
                    ActPotTable();
                }
                //listPartie = new ArrayList<String[]>((List<String[]>)msg.obj);
                //MAJAffichage();
            }
        };
        
        // on prepare le handler de retour message
        resultHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("EXITOK")){
                    finish();
                }else if(msg.obj.toString().equals("DEBUTPARTIE")){
                    for (int i = 0; i < 5; i++) {
                        carteTable[i] = -1;
                    }
                }
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
            }
        };
        
        getWindow().setBackgroundDrawableResource(R.drawable.table);
        
        cartes = new ImageView[8][2];
        infoPlayers=new TextView[8][3];
        
        cartesCentre = new ImageView[5];

        img = new GestionImg(getResources(), R.drawable.cartemodif);
        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("test 1").setContent(R.id.rel1));
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("test 1").setContent(R.id.rel2));
        // mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Onglet 1").setContent(R.id.rel1));
        // mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Onglet 2").setContent(R.id.rel1));
        mTabHost.setCurrentTab(0);
        
        // Ouverture du chat
        chat = (Button) findViewById(R.id.chat1);  
        chat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mTabHost.setCurrentTab(1);
            }
        });
        
        // Fermeture du chat
        exitChat = (Button) findViewById(R.id.retour);
        exitChat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mTabHost.setCurrentTab(0);
            }
        });
        
        // Envoi du message
        textAEnvoyer = (EditText) findViewById(R.id.message);
        envoiMessage = (Button) findViewById(R.id.send);
        envoiMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if(!textAEnvoyer.getText().toString().equals("")){
                    String[] arg = new String[2];
                    arg[0] = nomUtil;
                    arg[1] = textAEnvoyer.getText().toString();
                    try {
                        Accueuil.sender.setTram(CreateurTram.MESSENGER, arg, 2);
                    } catch (IOException ex) {
                        Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    textAEnvoyer.setText("");
                }
            }
        });
      
        // Quitter partie
        exitPartie = (Button) findViewById(R.id.quit1);
        exitPartie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    Accueuil.sender.setTram(CreateurTram.EXIT_PARTIE);
                } catch (IOException ex) {
                    Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
                }
                finish();
            }
        });
        
        // Effacer messenger
        effaceMessenger = (Button) findViewById(R.id.effacer);
        effaceMessenger.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                listItem.clear();
            }
        });
    
        initCartes();
        initInfo();
        try {
            Accueuil.sender.setTram(CreateurTram.PRET);
        } catch (IOException ex) {
            Logger.getLogger(TableauJeu.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private void initManche(){
        for (int i = 0; i < 5; i++) {
            carteTable[i] = -1;
        }
    }
    
    /**
     * 
     */
    private void MAJMessenger(){
        map = new HashMap<String, String>();
        //on insère un élément partyname que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
        map.put("pseudo", messageCourant[0]);
        //on insère un élément nbplayers que l'on récupérera dans le textView titre créé dans le fichier displaylist.xml
        map.put("message", messageCourant[1]);
        listItem.add(map);
        
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.messengeritem,
        new String[] {"pseudo", "message"}, new int[] { R.id.pseudomsg, R.id.messagemsg});
        maList.setAdapter(mSchedule);  
    }
    
    /**
     * 
     */
    private void initCartes(){
        cartes[0][0] = (ImageView) findViewById(R.id.c1p1);
        cartes[0][1] = (ImageView) findViewById(R.id.c2p1);
        
        cartes[1][0] = (ImageView) findViewById(R.id.c1p2);
        cartes[1][1] = (ImageView) findViewById(R.id.c2p2);
        
        cartes[2][0] = (ImageView) findViewById(R.id.c1p3);
        cartes[2][1] = (ImageView) findViewById(R.id.c2p3);
        
        cartes[3][0] = (ImageView) findViewById(R.id.c1p4);
        cartes[3][1] = (ImageView) findViewById(R.id.c2p4);
        
        cartes[4][0] = (ImageView) findViewById(R.id.c1p5);
        cartes[4][1] = (ImageView) findViewById(R.id.c2p5);
        
        cartes[5][0] = (ImageView) findViewById(R.id.c1p6);
        cartes[5][1] = (ImageView) findViewById(R.id.c2p6);
        
        cartes[6][0] = (ImageView) findViewById(R.id.c1p7);
        cartes[6][1] = (ImageView) findViewById(R.id.c2p7);
        
        cartes[7][0] = (ImageView) findViewById(R.id.c1p8);
        cartes[7][1] = (ImageView) findViewById(R.id.c2p8);
        
        cartesCentre[0] = (ImageView) findViewById(R.id.ct1);
        cartesCentre[1] = (ImageView) findViewById(R.id.ct2);
        cartesCentre[2] = (ImageView) findViewById(R.id.ct3);
        cartesCentre[3] = (ImageView) findViewById(R.id.ct4);
        cartesCentre[4] = (ImageView) findViewById(R.id.ct5);
    }
    
    private void initInfo(){
        infoPlayers[0][0] = (TextView) findViewById(R.id.pseudop1);
        infoPlayers[0][1] = (TextView) findViewById(R.id.cashp1);
        infoPlayers[0][2] = (TextView) findViewById(R.id.betp1);
        
        infoPlayers[1][0] = (TextView) findViewById(R.id.pseudop2);
        infoPlayers[1][1] = (TextView) findViewById(R.id.cashp2);
        infoPlayers[1][2] = (TextView) findViewById(R.id.betp2);
        
        infoPlayers[2][0] = (TextView) findViewById(R.id.pseudop3);
        infoPlayers[2][1] = (TextView) findViewById(R.id.cashp3);
        infoPlayers[2][2] = (TextView) findViewById(R.id.betp3);
        
        infoPlayers[3][0] = (TextView) findViewById(R.id.pseudop4);
        infoPlayers[3][1] = (TextView) findViewById(R.id.cashp4);
        infoPlayers[3][2] = (TextView) findViewById(R.id.betp4);
        
        infoPlayers[4][0] = (TextView) findViewById(R.id.pseudop5);
        infoPlayers[4][1] = (TextView) findViewById(R.id.cashp5);
        infoPlayers[4][2] = (TextView) findViewById(R.id.betp5);
        
        infoPlayers[5][0] = (TextView) findViewById(R.id.pseudop6);
        infoPlayers[5][1] = (TextView) findViewById(R.id.cashp6);
        infoPlayers[5][2] = (TextView) findViewById(R.id.betp6);
        
        infoPlayers[6][0] = (TextView) findViewById(R.id.pseudop7);
        infoPlayers[6][1] = (TextView) findViewById(R.id.cashp7);
        infoPlayers[6][2] = (TextView) findViewById(R.id.betp7);
        
        infoPlayers[7][0] = (TextView) findViewById(R.id.pseudop8);
        infoPlayers[7][1] = (TextView) findViewById(R.id.cashp8);
        infoPlayers[7][2] = (TextView) findViewById(R.id.betp8);
        
        pot = (TextView) findViewById(R.id.pot);
        
        for (int i = 0; i < 8; i++) {
            infoPlayers[i][0].setTextColor(Color.WHITE);
            infoPlayers[i][1].setTextColor(Color.BLACK);
            infoPlayers[i][2].setTextColor(Color.RED);
            
            infoPlayers[i][0].setTypeface(null, Typeface.BOLD_ITALIC);
            infoPlayers[i][1].setTypeface(null, Typeface.BOLD_ITALIC);
            infoPlayers[i][2].setTypeface(null, Typeface.BOLD_ITALIC);
        }
        
        pot = (TextView) findViewById(R.id.pot);
    }
    
    /**
     * Fonction appelé par l'analyseur de tram pour informaer l'utilisateur des infos envoyés par le serveur.
     * 
     * @author Mathieu Polizzi
     * 
     * @param message - le message de retour du serveur
     *
     */
    public static void MAJLIST(int typeListe, List<String[]> donnees){
        Message msg = new Message();
        msg.arg1 = typeListe;
        msg.obj = donnees;
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
    
    private void initPseudoJoueur(){
         SharedPreferences settings = getSharedPreferences(Accueuil.PREFS_CONNECT, 0);
         nomUtil = settings.getString("pseudo", "0");
    }
    
    private void ActDonneesJoueurs(){
        for (int i = 0; i < 8; i++) {
            if(i < listDonnees.size()){
                infoPlayers[i][0].setText(listDonnees.get(i)[0]);
                if(listDonnees.get(i)[0].equals(nomUtil)){
                    posJoueur = new Integer(i);
                }
                infoPlayers[i][1].setText(listDonnees.get(i)[1]);
                infoPlayers[i][2].setText(listDonnees.get(i)[2]);
            }else{
                infoPlayers[i][0].setText("");
                infoPlayers[i][1].setText("");
                infoPlayers[i][2].setText("");
            }
        }
    }
    
    private void ActCartesUtilisateur(){
        cartes[posJoueur][0].setImageBitmap(img.getImage(carteJoueur[0] / 4, carteJoueur[0] % 4));
        cartes[posJoueur][1].setImageBitmap(img.getImage(carteJoueur[1] / 4, carteJoueur[1] % 4));
    }
    
    private void ActCarteTable(){
        int i = 0;
        while(carteTable[i] != -1 && i < 5){
            cartesCentre[i].setImageBitmap(img.getImage(carteTable[i] / 4, carteTable[i] % 4));
            i++;
        }
    }
    
    private void ActPotTable(){
        pot.setText("pot : "+potText+"$");
    }
}
