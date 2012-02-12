package projet.poker_V100;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author Jessy Bonnotte
 */
public class Poker extends Activity
{
    
    static Connection connect;
    
    private Button connection;
    private CheckBox retenir;
    private EditText user;
    private EditText pass;
    
    /**
     * Référence vers le texte stocké dans le système de préférences
     */
    public static final String PREFS_CONNECT = "preferenceConnect"; 
    
    /** Called when the activity is first created.
     * @param savedInstanceState 
     */
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
