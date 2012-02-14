/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptagetram;

/**
 *
 * @author Shyzkanza
 */
public class CryptageTram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Crypt test = new Crypt();
        String coucou = "C";
        String Phrase = "Alors les gens on ecoutes plus le monde?";
        
        System.out.println(coucou+" : "+coucou.length());
        System.out.println(Phrase+" : "+Phrase.length()+"\n");
        
        String result1 = test.enCrypt(coucou);
        String result2 = test.enCrypt(Phrase);
        
        System.out.println(result1+" : "+result1.length());
        System.out.println(result2+" : "+result2.length()+"\n");
        
        result1 = test.deCrypt(result1);
        result2 = test.deCrypt(result2);
        
        System.out.println(result1+" : "+result1.length());
        System.out.println(result2+" : "+result2.length()+"\n");
    }
}
