package pokerPackage;











/**
 * Classe permettant de ne pas bloquer sur un choix de joueur
 * passe au joueur suivant au bout de 15 sec
 * @author Benjamin Maurin 
 */
public class TimerChoixJoueur extends Thread {
 
    
     private PokerPartie partie = null;
     private PokerClientThread client = null;
     private int machin = 1;
       public TimerChoixJoueur(PokerPartie partie,PokerClientThread client){
            this.partie = partie;
            this.client=client;
                }
       
       public void stopeux()
       {
    	   machin=0;
       }
       
     public void run(){
     {
        try
        {
                Thread.sleep (15000); // En pause pour 15 secondes
          if(machin==1){
            if(partie!=null)
            {
                   String s=partie.jouage(1, 0, client);
                   if(client!=null){
                	   
                	   client.send(s);
                   }
            }
          }
        }
        catch (InterruptedException exception){exception.printStackTrace();}
        }
    }

	
     
     
     
     
}


