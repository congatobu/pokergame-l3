package projet.hello;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Hello extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // On reli le textView du XML vers le code. final pour qu'il puisse etre accessible depuis le listener
        final TextView texte = (TextView) findViewById(R.id.texte);
        
        // On reli le bouton du XML vers le code
        Button couleur = (Button) findViewById(R.id.bouton);
        
        // On place un listener sur le bouton
        couleur.setOnClickListener(new View.OnClickListener() {
            
            // On créé l'action du clique sur le bouton
            public void onClick(View arg0) {
                
                // On créé nos trois composante RVB de maniere aléatoire
                int Rrouge = (int)(Math.random() * 255);
                int Vvert = (int)(Math.random() * 255);
                int Bbleu = (int)(Math.random() * 255);
                
                // Ceci permet d'afficher une popup a l'ecran avec le texte voulu dedans
                Toast.makeText(getApplicationContext(), "Coucou je suis un Toast", Toast.LENGTH_SHORT).show();
                
                // Ici on change la couleur du texte view avec les couleur pré réglé précedemment
                texte.setTextColor(Color.rgb(Rrouge, Vvert, Bbleu));
            }
        });
    }
}
