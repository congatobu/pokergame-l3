/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.poker;

import android.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;




/**
 *
 * @author Masss
 */
public class GestionImg {
    
    private static final int FRAME_COLS = 16;
    private static final int FRAME_ROWS = 7;
    
    // Taille des cartes
    private int hauteur = 73;
    private int largeur = 64;
    
    // Les couleurs
    public static final int PIC = 0;
    public static final int COEUR = 1;
    public static final int TREFLE = 2;
    public static final int CARREAU = 3;
    
    // Image d'origine
    Bitmap bMapScaled;
    Bitmap bMap;
    

    public GestionImg(Resources res, int id){

        bMap = BitmapFactory.decodeResource(res, id);
        bMapScaled = Bitmap.createScaledBitmap(bMap, 1024, 512, true);

       
    }

    public Bitmap getImage(int valeur, int couleur){
        Bitmap retour = null;
        
        int valTemp = valeur;
        if(valTemp == 0){
            valTemp = 13;
        }
        
        int x = 832 - (largeur * valTemp);
        int y = hauteur * couleur;
        
       
        retour = Bitmap.createBitmap(bMapScaled, x, y, 64, 73);
        
        return retour;
    }
    
}
