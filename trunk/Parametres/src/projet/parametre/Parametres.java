package projet.parametre;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Classe principal du programme
 * 
 * @author Shyzkanza
 */
public class Parametres extends Activity
{
    /**
     * Référence vers le texte stocké dans le système de préférences
     */
    public static final String PREFS_NAME = "preferenceTaux";  
    /**
     * Référence vers pour la couleur du texte dans le système de préférences
     */
    public static final String PREFS_TEXTE_COLOR = "preferenceCouleur";
    
    final int PARAMETRE = 1;
    final int A_PROPOS = 2;
    final int QUITTER = 3;
    
    Button load; 
    Button save;
    Button init;
    EditText texte;
    
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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        load = (Button) findViewById(R.id.load);
        save = (Button) findViewById(R.id.save);
        init = (Button) findViewById(R.id.init);
        texte = (EditText) findViewById(R.id.texte);
        
        initCouleur();   
        
        load.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                initTexte();
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                saveTexte();
            }
        });
        
        init.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                texte.setText("texte de test");
            }
        });
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
        menu.findItem(PARAMETRE).setIcon(R.drawable.setting);
        menu.add(0, A_PROPOS, 0, "A propos");
        menu.findItem(A_PROPOS).setIcon(R.drawable.info);
        menu.add(0, QUITTER, 0, "Quitter");
        menu.findItem(QUITTER).setIcon(R.drawable.quit);
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
            Intent i = new Intent (getApplicationContext(), parametre.class);
      	    startActivity(i);
            return true;
        case A_PROPOS:
            //newGame();
            return true;
        case QUITTER:
            finish();
            return true;
        }
        return false;
    }
    
    /** 
     * Fonction appelé lors de la restauration de l'activity. le super.onResume()<br/>
     * permet de faire les actions de base puis enfin on peut fair eles actions <br/>
     * personnels
     * 
     * @author Jessy Bonnotte
     */
    @Override
    public void onResume(){
        super.onResume();
        initCouleur();  
    }
    
    /** 
     * Fonction permettant de récupérer le texte stocké dans le système de préférences<br/>
     * et de l'insérer dans la zone de texte. Si lors de la premiere utilisation le <br/>
     * système ne comprend aucun texte, on l'initialise avec une valeur par défault.
     * 
     * @author Jessy Bonnotte
     */
    private void initTexte(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);        
        if(settings.getString("texte", "0").equals("0")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("texte", "texte de test");
            editor.commit();
        }
        String zTexte = settings.getString("texte", "0");
        
        texte.setText(zTexte);
    }
    
    /** 
     * Fonction permettant d'enregistrer le texte de la zone de texte dans le systeme<br/>
     * de préférences d'android.
     * 
     * @author Jessy Bonnotte
     */
    private void saveTexte(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("texte", texte.getText().toString());
        editor.commit();
    }
    
    /** 
     * Fonction étant appelé pour initialisé la couleur du texte de la zone de texte.<br/>
     * On vérifie si le systeme de préférence à déjà été configuré et s'il est vide,<br/>
     * on met le rouge dedans par default. Ensuite la valeur est charger dans le <br/>
     * TextView.
     * 
     * @author Jessy Bonnotte
     */
    private void initCouleur(){
        SharedPreferences settings = getSharedPreferences(PREFS_TEXTE_COLOR, 0);        
        if(settings.getString("couleur_texte", "0").equals("0")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("couleur_texte", String.valueOf(Color.RED));
            editor.commit();
        }
        String couleur = settings.getString("couleur_texte", "0");
        
        texte.setTextColor(Integer.parseInt(couleur));
    }
}
