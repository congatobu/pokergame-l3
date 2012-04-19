package pokerPackage;









/**
 * Classe du thread par partie qui execute la partie
 * @author Benjamin Maurin
 */
public class ExecPartie extends Thread {
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