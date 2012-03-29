
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe du thread par partie qui execute la partie
 * @author Benjamin Maurin
 */
class ExecPartie extends Thread {
    private PokerPartie partie = null;
    
       public ExecPartie(PokerPartie partie){
            this.partie = partie;
                }

       
    /**
     * Fonction qui lance la partie
     * @author Benjamin Maurin
     */
    @Override
    public void run(){
        
        partie.tournoi();
        
        
    }
       
       
}