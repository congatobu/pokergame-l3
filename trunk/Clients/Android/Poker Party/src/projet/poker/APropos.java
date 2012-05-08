/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.poker;

import android.app.Activity;
import android.os.Bundle;

/**
 * Classe contenant les informations concernant l'application.
 * 
 * @author Mathieu Polizzi
 */
public class APropos extends Activity{
    
    /**
     * Fonction hérité de activity permmettant de gérer l'affichage de la fenêtre A propos.
     * 
     * @author Mathieu Polizzi
     * 
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apropos);       
    }
}