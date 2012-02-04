package projet.parametre;
 
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


 
/**
 * Classe de l'activity paramètre
 * 
 * @author Shyzkanza
 */
public class parametre extends Activity{
 
    private ListView maListViewPerso;
    ColorPickerDialog cpd;
 
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
        
        //Récupération de la listview créée dans le fichier main.xml
        maListViewPerso = (ListView) findViewById(R.id.listviewperso);
 
        //Création de la ArrayList qui nous permettra de remplire la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
 
        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;
 
        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();
        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
        map.put("titre", "Choix 1");
        //on insère un élément description que l'on récupérera dans le textView description créé dans le fichier affichageitem.xml
        map.put("description", "Aperçu choix 1");
        //on insère la référence à l'image (convertit en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml
        map.put("img", String.valueOf(R.drawable.info));
        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);
 
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 2");
        map.put("description", "Aperçu choix 2");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 3");
        map.put("description", "Aperçu choix 3");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 4");
        map.put("description", "Aperçu choix 4");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 5");
        map.put("description", "Aperçu choix 5");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 6");
        map.put("description", "Aperçu choix 6");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 7");
        map.put("description", "Aperçu choix 7");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 8");
        map.put("description", "Aperçu choix 8");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Choix 9");
        map.put("description", "Aperçu choix 9");
        map.put("img", String.valueOf(R.drawable.info));
        listItem.add(map);
        
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
        map = new HashMap<String, String>();
        map.put("titre", "Couleur texte");
        map.put("description", "Changer la couleur du texte");
        map.put("img", String.valueOf(R.drawable.color));
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
                //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                Toast.makeText(getApplicationContext(), "Choix "+String.valueOf(position + 1), Toast.LENGTH_SHORT).show();
                //HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                //on créer une boite de dialogue
                if(position == 9){
                    valideCouleur();
                }
            }
        });
    }
    
    /**
     * Permet d'ouvrir une popup afin de sélectionner la couleur du texte de la page<br/>
     * et lors de la validation celle-ci est sauvegardé dans le système de préférence.
     */
    private void valideCouleur(){
        cpd  = new ColorPickerDialog(this, new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                SharedPreferences settings = getSharedPreferences(Parametres.PREFS_TEXTE_COLOR, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("couleur_texte", String.valueOf(color));
                editor.commit();
            }
        }, Color.BLACK);
        cpd.show();
    }
}