

import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.Semaphore;


/**
 * Classe du serveur qui va gerer les parties
 * @author benjamin Maurin
 */
public class PokerPartie {
		
    private Vector<PokerClientThread> clientList= new Vector<PokerClientThread>();
    private PrintWriter screenOut = new PrintWriter(System.out, true);
    private int enCours = 0;
    private int maxPlayers = 8;
    private Semaphore available = new Semaphore(1, true);
    private String nomP = "";
    private Jeu jeu;
    private PokerClientThread createur;
    /**
    *Constructeur
    * @author benjamin Maurin
    *@param nom : nom de la partie
    * @param max : nombre de joueurs maximum dans la partie
     * @param createur : createur de la partie
    * 
    */
    public PokerPartie(String nom,int max,PokerClientThread createur){
        this.nomP = nom;
        this.maxPlayers= max;
        this.createur=createur;
        addPlayer(createur);
    }
	
    /**
     * accesseur
     * @author benjamin Maurin
     * @return clientList
     */
    public Vector getClientList(){
        return clientList;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nombre joueurs dans la partie
     */
    public String getEtat(){ 
        return "    "+nomP+" : "+clientList.size()+"/"+maxPlayers+" joueurs dans la partie";
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @param m maxPlayeur
     */
    public void setMaxPlayers(int m){
        this.maxPlayers=m;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return maxPlayers
     */
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nombre de joueurs dans la partie
     */
    public int getNbJ(){
        return clientList.size();
    }
    
    /**
     * accesseur
     * @author benjamin Maurin
     * @return nom de la partie
     */
    public String getNom(){
        return this.nomP;
    }


    /**
     * Fonction pour envoyer un message à tous les clients de la partie
     * @author Benjamin Maurin
     * @param m : message à envoyer
     */
    public void broadcastClientsPartie(String m)	{
        int i;
        PokerClientThread foo;
        for (i=0;i<clientList.size();++i){
            foo = (PokerClientThread)clientList.get(i);
            foo.send(m);
        } 	
    }
    
    /**
     * Fonction pour enlever un client de la partie
     * @author Benjamin Maurin
     * @param deadClient client à supprimer
     */
    public void deleteClient(PokerClientThread deadClient){
        try{
            available.acquire();
            int num;
            num=clientList.indexOf(deadClient);
            if(num!=-1){
                clientList.remove(num);                         
            }               
          broadcastClientsPartie(listeJoueursPartie());  
            if(clientList.size()<1){
                PokerServer.deletePartie(this);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            finally {
             available.release();
        }
    }

       /**
     * Fonction pour donner les joueurs d'une partie
     * @author Benjamin Maurin
     * @return le string de la liste des joueurs dans la partie
     */
    public String listeJoueursPartie(){
        String s = "LISTEJOUEURSPARTIE";
        for(int i=0; i <clientList.size();i++)
        {
            s+="@"+clientList.get(i).getPseudo();     
        }
        return s;
                                             }
    

    /**
     * Fonction pour ajouter un client à la partie
     * @author Benjamin Maurin
     * @param np nouveau player
     * @return 0 si c'est possible -1 sinon
     */
    public int addPlayer(PokerClientThread np){
    if(clientList.size()<maxPlayers){       
    clientList.add(np);	
    np.setPartie(this);
    return 0;
    }
    else return -1;
                                             }

    /**
    * Fonction pour libérer la mémoire
    * @author Benjamin Maurin
    * @throws Throwable
    */
    @Override
    protected void finalize() throws Throwable{
        try {
            clientList.clear();
            available  =null;
            nomP = null;
            jeu = null;
            screenOut.close();
        }catch(Exception e) {
        
        }
        finally {
            super.finalize();
        } 
    }
    
    
    /**
    * procedure permettant le deroulement d'un tournoi
    * @author steve giner
    */
    public int tournoi(){
    	int i=0;
    	int g=-1;
    	while(g==-1){
    		
    		if(i==getNbJ())i=0;
    		else if(i>getNbJ())i=1;
    			
    		deroulement(i);
    		i=i+2;
    		g=gagnant();
    		
    	}
    	
    	return g;
    }
    
    
    private int gagnant() {
		
    	int cpt=0;int j=-1;
    	for(int i=0;i<getNbJ();i++){
    	
    		if(clientList.elementAt(i).getJetonsTotaux()==0)cpt++;
    		else j=i;
    	}
    	if(cpt==getNbJ()-1)
		return j;
    	else return -1;
	}

	/**
    * procedure permettant le deroulement d'une partie
    * @author steve giner
    */
    public void deroulement(int b){
    	
    	//initialisation du paquet de cartes
    	Jeu j=new Jeu();
    	int jetons;//variable contenant les jetons poses max
    	int nbJ=getNbJ();
    	//envoi de 2 cartes a chaque joueurs et init des jetons
    	for(int i=0;i<getNbJ();i++){
	    	clientList.elementAt(i).setCartes(j.tireUneCarte(),j.tireUneCarte());
	    	clientList.elementAt(i).setJetonsTotaux(50);
	    	clientList.elementAt(i).setJetonsPoses(0);
    	}
    	
    	
    	
    	//j0 mise la blind et j1 la surblind
    	mise(b,5);
    	
    	if(b+1<getNbJ())mise(b+1,10);
    	else mise(0,10);
    	
    	jetons=10;
    	
    	//premiere enchre (choix: passer(1), suivre(2) ou relancer(3))
    	
    	int dernierRelance=b+1;
    	
    	enchere(b+2,dernierRelance,jetons,nbJ);
    	
    	//fin du premier tour d'enchere
    	
    	
		int[] table=new int[5];
		table[0]=j.tireUneCarte();
		table[1]=j.tireUneCarte();
		table[2]=j.tireUneCarte();
		
		//envoi de la table aux clients
		
		//debut deuxieme tour
		
		//envoi de leur choix possibles (passer(1),ouvrir(2))
		
		int joueur=b;
		boolean bool=true;
		int jetMis=0;
		int cpt=0;
		
		while(bool && cpt<getNbJ()){
			
			if(joueur==getNbJ())joueur=0;
			
			if(clientList.elementAt(joueur).getAttente()==0){
				
	   			if(choix(jetMis)==2){
	   				mise(joueur,jetMis);
	   				jetons=clientList.elementAt(joueur).getJetonsPoses(); 
	   				dernierRelance=joueur;
	   				bool=false;
	   			}	
		   	}
			joueur++;cpt++;	
		}
		
		if(bool){
		//debut 2nd phase
			enchere(joueur,dernierRelance,jetons,nbJ);
		//fin 2nd phase
		}
		//fin 2e enchere
		table[3]=j.tireUneCarte();
		
		//debut 3e enchere
		
		joueur=b;
		bool=true;
		
		cpt=0;
		
		while(bool && cpt<getNbJ()){
			
			if(joueur==getNbJ())joueur=0;
			
			if(clientList.elementAt(joueur).getAttente()==0){
				
	   			if(choix(jetMis)==2){
	   				mise(joueur,jetMis);
	   				jetons=clientList.elementAt(joueur).getJetonsPoses(); 
	   				dernierRelance=joueur;
	   				bool=false;
	   			}	
		   	}
			joueur++;cpt++;	
		}
		
		if(bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,nbJ);
			//fin 2nd phase
			}
		
		//debut 4e enchere
		
		table[4]=j.tireUneCarte();
		
		while(bool && cpt<getNbJ()){
			
			if(joueur==getNbJ())joueur=0;
			
			if(clientList.elementAt(joueur).getAttente()==0){
				
	   			if(choix(jetMis)==2){
	   				mise(joueur,jetMis);
	   				jetons=clientList.elementAt(joueur).getJetonsPoses(); 
	   				dernierRelance=joueur;
	   				bool=false;
	   			}	
		   	}
			joueur++;cpt++;	
		}
		
		if(bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,nbJ);
			//fin 2nd phase
			}
		
		int nbjoueurs=0;
		int[][] cartes=new int[getNbJ()][2];
		int[] cl=new int[getNbJ()];
		
		for(int i=0;i<getNbJ();i++){
			
			if(clientList.elementAt(i).getAttente()!=1){
				cartes[nbjoueurs]=clientList.elementAt(i).getCartes();
				cl[nbjoueurs]=i;
				nbjoueurs++;
			}
			
		}
		
		
		if(nbjoueurs>1){
			float[] g=j.gagnant(cartes,table,nbjoueurs);
			repartionDesGains(jetons,g);
				
			
			
			
		}
		else{
			if(clientList.elementAt(cl[0]).getAttente()==0)
				clientList.elementAt(cl[0]).setJetonsTotaux(clientList.elementAt(cl[0]).getJetonsTotaux()+jetons);
			else{
				for(int i=0;i<getNbJ();i++){
					
					clientList.elementAt(cl[0]).setJetonsTotaux(clientList.elementAt(cl[0]).getJetonsTotaux()+(min(clientList.elementAt(i).getJetonsPoses(),clientList.elementAt(cl[0]).getJetonsPoses())));
					clientList.elementAt(i).setJetonsTotaux(clientList.elementAt(i).getJetonsTotaux()+clientList.elementAt(i).getJetonsPoses()-clientList.elementAt(cl[0]).getJetonsPoses());
					
					
				}
				
				
			}
		}
		
		
		
		
    }
    
    
	private int min(int a, int b) {
		
		if(a<b)return a;else return b;
		
		
	}

	private void repartionDesGains(int jetons, float[] g) {
		
		if(g.length<4){
			
			if(clientList.elementAt((int)g[2]).getAttente()==0){
				clientList.elementAt((int)g[2]).setJetonsTotaux(clientList.elementAt((int)g[2]).getJetonsTotaux()+jetons);
			}
			else{
				for(int i=0;i<getNbJ();i++){
					
					clientList.elementAt((int)g[2]).setJetonsTotaux(clientList.elementAt((int)g[2]).getJetonsTotaux()+(min(clientList.elementAt(i).getJetonsPoses(),clientList.elementAt((int)g[2]).getJetonsPoses())));
					clientList.elementAt(i).setJetonsTotaux(clientList.elementAt(i).getJetonsTotaux()+clientList.elementAt(i).getJetonsPoses()-clientList.elementAt((int)g[2]).getJetonsPoses());
					
					
				}
			
			}
		
		}
		else{
			// a reflechir...
			for(int j=2;j<g.length;j++){
				if(clientList.elementAt((int)g[j]).getAttente()==0){
					clientList.elementAt((int)g[j]).setJetonsTotaux(clientList.elementAt((int)g[j]).getJetonsTotaux()+jetons);
				}
				else{
					for(int i=0;i<getNbJ();i++){
						
						clientList.elementAt((int)g[j]).setJetonsTotaux(clientList.elementAt((int)g[j]).getJetonsTotaux()+(min(clientList.elementAt(i).getJetonsPoses(),clientList.elementAt((int)g[j]).getJetonsPoses())));
						clientList.elementAt(i).setJetonsTotaux(clientList.elementAt(i).getJetonsTotaux()+clientList.elementAt(i).getJetonsPoses()-clientList.elementAt((int)g[j]).getJetonsPoses());
						
						
					}
				}
			}	
			
		}
	}

	
	
	/**
     * fonction qui retournera le choix du joueur
     * @author steve giner
	 * @param jetMis jetons mise par le joueur
     */
    private int choix(int jetMis) {
		
    	//envoi min et max jetons possible de miser
    	//envoi de la demande de choix
    	
    	
    	
		return 0;
	}

	/**
    * procedure permettant de changer les jetons d'un joueurs
    * @author steve giner
    */
	private void mise(int joueur, int jetons) {
		
		clientList.elementAt(joueur).setJetonsTotaux(clientList.elementAt(joueur).getJetonsTotaux()-jetons);
		clientList.elementAt(joueur).setJetonsPoses(clientList.elementAt(joueur).getJetonsPoses()+jetons);
		if(clientList.elementAt(joueur).getJetonsTotaux()==0)clientList.elementAt(joueur).setAttente(2);
		
	}
    

	/**
	* procedure permettant d'effectuer un tour d'enchere
	* @author steve giner
	* @param nbJ nombre de joueurs qu'il reste
	*/
	private void enchere(int joueur,int dernierRelance,int jetons, int nbJ) {
    
		
    	int jetMis=0;
		if(joueur==getNbJ())joueur=0;
		else if(joueur>getNbJ())joueur=1;
		boolean bool=true;
		int rel=0;//nombre de relances
		
		
		while(bool && nbJ>2)
    	{
    		if(joueur==getNbJ())joueur=0;
    			    		
    		if(clientList.elementAt(joueur).getAttente()==0){
		    	
    			 //envoi d'un message pour dire au joueur de jouer
    			switch(choix(jetMis)){
		    	case 1:
		    		if(joueur==dernierRelance)bool=false;
		    		else clientList.elementAt(joueur).setAttente(1);nbJ--;
		    		break;
		    	
		    	case 2:
		    		if(joueur!=dernierRelance)
			    		mise(joueur,jetons-clientList.elementAt(joueur).getJetonsPoses());
		    		else bool=false;
		    		break;
		    	
		    	case 3:
		    		mise(joueur,jetMis);
		    		rel++;
		    		jetons=clientList.elementAt(joueur).getJetonsPoses();
		    		dernierRelance=joueur;
		    		if(rel==3){
		    			//envoyer un message pour dire qu'ils ne peuvent plus relancer 
		    		}	    	
			    	break;		
		    	}
    		}
    		joueur++;
    	}
		
	}
    
}