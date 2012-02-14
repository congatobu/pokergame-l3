/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bddpoker;

import java.util.Date;

/**
 * Classe permettant de crer un objet contenant une date.
 * 
 * @author Jessy Bonnotte
 */
public class Dateur {
    Date d;
    int annee = 0;
    int mois = 0;
    int date = 0;
    int jour = 0;

    /**
     * Constructeur initialisant la date a celle qu'il est au moment ou il est appelé.
     * 
     * @author Jessy Bonnotte
     */
    public Dateur(){
        d = new Date();
        jour = d.getDay();
        date = d.getDate();
        mois = d.getMonth() + 1;
        annee = d.getYear() + 1900;
    }
    
    /**
     * Fonction permettant de retourner l'heure dans un format lisible.<br>
     * exemple : Jeudi 15 Fevrier 2006
     * 
     * @author Jessy Bonnotte
     * 
     * @return la date enregistré dans l'objet
     */
    public String getDate(){
        String retour = getJour()+" "+date+" "+getMois()+" "+annee;
        return retour;
    }

    /**
     * Fonctione permettant de convertir le numéro de jour en jour ecrit en <br>
     * toute lettre.
     * 
     * @author Jessy Bonnotte
     * 
     * @return le jour de la semaine en lettres
     */
    private String getJour(){
        String retour = "";

        switch(jour){
            case 1:
                retour = "Lundi";
                break;
            case 2:
                retour = "Mardi";
                break;
            case 3:
                retour = "Mercredi";
                break;
            case 4:
                retour = "Jeudi";
                break;
            case 5:
                retour = "Vendredi";
                break;
            case 6:
                retour = "Samedi";
                break;
            case 7:
                retour = "Dimanche";
                break;
        }
        return retour;
    }

    /**
     * Fonction permettant de convertir le numéro de mois en mois ecrit en <br>
     * toute lettre
     * 
     * @author Jessy bonnotte
     * 
     * @return le mois ecrit en lettres
     */
    private String getMois(){
        String retour = "";

        switch(mois){
            case 1:
                retour = "Janvier";
                break;
            case 2:
                retour = "Février";
                break;
            case 3:
                retour = "Mars";
                break;
            case 4:
                retour = "Avril";
                break;
            case 5:
                retour = "Mai";
                break;
            case 6:
                retour = "Juin";
                break;
            case 7:
                retour = "Juillet";
                break;
            case 8:
                retour = "Aout";
                break;
            case 9:
                retour = "Septembre";
                break;
            case 10:
                retour = "Octobre";
                break;
            case 11:
                retour = "Novembre";
                break;
            case 12:
                retour = "Décembre";
                break;
        }
        return retour;
    }
}
