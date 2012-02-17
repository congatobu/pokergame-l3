/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Selecteur;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import projet.poker.APropos;
import projet.poker.Parametre;
import projet.poker.R;

public class ListParty extends Activity
{
    
    
    private Button connection;
    private CheckBox retenir;
    private EditText user;
    private EditText pass;
    private Intent i;
    
    private final int PARAMETRE = 1;
    private final int A_PROPOS = 2;
    private final int QUITTER = 3;
    
    /**
     * Référence vers le texte stocké dans le système de préférences
     */
    public static final String PREFS_CONNECT = "preferenceConnect"; 
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
            retenir = (CheckBox) findViewById(R.id.memorize); 
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
                    if(user.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Veuillez renseigner le nom d'utilisateur", Toast.LENGTH_SHORT).show();
                    }else if(retenir.isChecked()){
                        saveZone();
                    }else if(!retenir.isChecked()){
                        ereaseZone();
                    }
                }
            });
        }catch(Exception e){
            return false;
        }
        return true;
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

