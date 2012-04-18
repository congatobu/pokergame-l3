package pokerPackage;


import java.util.concurrent.Semaphore;



/**
 * Classe permettant de d'attendre puis de lancer la partie en kickant les joueurs absents
 * Kick les joueurs pas pret au bout de 5 secondes
 * @author Benjamin Maurin
 */
public class TimerLancePartie extends Thread {
 
    
     private PokerPartie partie = null;
     private Semaphore sem = null;
       public TimerLancePartie(PokerPartie partie,Semaphore sem){
            this.partie = partie;
            this.sem = sem;
                }
       
       
     public void run(){
    {
        try
        {
                Thread.sleep (5000); // En pause pour cinq secondes
                sem.acquire();
            if(partie!=null &&  partie.getEnCours()!=1)
            {
                               // ca kick si ya un seul pas pret
                PokerClientThread foo;
                for (int i=0;i<partie.getClientList().size();++i){

                    foo = (PokerClientThread)partie.getClientList().get(i);
                    if(foo!=null && foo.getPret(i)==0){
                            partie.deleteClient(foo);
                     }
                } 	
                    //kick si ya pas assez de joueurs
                    if(partie.getClientList().size()<=1) partie.deleteClient((PokerClientThread) partie.getClientList().get(0));
                    else{
                //et ca lance
                  partie.setEnCours(1);
                  //lancer dans un nouveau thread la partie
                  ExecPartie e = new ExecPartie(partie);
                  e.start();
                  partie.broadcastClientsPartie("DEBUTPARTIE");
                    }
            }
            sem.release();
        }
        catch (InterruptedException exception){exception.printStackTrace();}
        }
    }
     
     
     
     
}
