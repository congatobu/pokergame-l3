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
 * Classe permettant de gerer les cartes de jeu.
 *
 * @author Mathieu Polizzi
 */
public class GestionImg {
    
    private static final int FRAME_COLS = 16;
    private static final int FRAME_ROWS = 7;
    
    // Taille des cartes
    private int hauteur = 73;
    private int largeur = 64;
    
    // Les couleurs
    /**
     * Pour selectionner la couleur PIC
     */
    public static final int PIC = 0;
    /**
     * Pour selectionner la couleur COEUR
     */
    public static final int COEUR = 1;
    /**
     * Pour selectionner la couleur TREFLE
     */
    public static final int TREFLE = 2;
    /**
     * Pour selectionner la couleur CARREAU
     */
    public static final int CARREAU = 3;
    
    // Image d'origine
    Bitmap bMapScaled;
    Bitmap bMap;
    

    /**
     * Constructeur de la classe. Permet d'initialiser une image en fonction des ressources de l'applications. On lui passe egalement l'ID de l'image presente dans le dossier resource/drawable.
     * 
     * @author Mathieu Polizzi
     * 
     * @param res - Ressource de l'application android
     * @param id - id de l'image a charger
     */
    public GestionImg(Resources res, int id){
        bMap = BitmapFactory.decodeResource(res, id);
        bMapScaled = Bitmap.createScaledBitmap(bMap, 1024, 512, true);      
    }

    /**
     * Fonction permettant de recupere une image presente sur l'image d'origine. Elle decoupe une partie de l'image d'origine et la retourne.
     * 
     * @author Mathieu Polizzi
     * 
     * @param valeur - valeur de la carte a retourner
     * @param couleur - couleur de la carte a retourner
     * 
     * @return Bitmap - une image decouper de l'image d'origine
     */
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
