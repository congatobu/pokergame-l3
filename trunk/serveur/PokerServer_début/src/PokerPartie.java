

import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe du serveur qui va gerer les parties
 * @author benjamin Maurin
 */
public class PokerPartie {
		
    private Vector<PokerClientThread> clientList= new Vector<PokerClientThread>();
    private PrintWriter screenOut = new PrintWriter(System.out, true);
    private int enCours = 0; // 0 non 1 oui
    private int maxPlayers = 8;
    private Semaphore available = new Semaphore(1, true);
    private Semaphore attenteDeChoix = new Semaphore(0, true);
    private String nomP = "";
    private Jeu je=new Jeu();
    private PokerClientThread createur;
    private int choixJ=0;
    private int jetJ=0;
    
    
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
     * @return createur
     */
    public PokerClientThread getCreateur(){
        return createur;
    }
    
      /**
     * accesseur
     * @author benjamin Maurin
     * @return encours
     */
    public int getEnCours(){
        return enCours;
    }
    
        /**
     * accesseur
     * @author benjamin Maurin
     */
    public void setEnCours(int a){
       enCours = a;
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
	  deadClient.setPartie(null);
          broadcastClientsPartie(listeJoueursPartie());
          
            if(clientList.size()<1){
                PokerServer.deletePartie(this);
            }
            
            if(deadClient==createur)
            {
                createur=clientList.get(0);
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
            if(clientList.get(i)!=createur)
            s+="@"+clientList.get(i).getPseudo();  
            else
            s+="@$"+clientList.get(i).getPseudo(); 
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
           je = null;   
           // screenOut.close();
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
    	//init jetons
    	for(i=0;i<getNbJ();i++){
	    	
	    	clientList.get(i).setJetonsTotaux(200);
	    	clientList.get(i).setJetonsPoses(0);
	    	
    	}
    	i=0;
    	
    	while(g==-1){
    		je.initTasDe52cartes();
    		

        	
        	envoiJetonsJ();
        	
    		if(i==getNbJ())i=0;
    		else if(i>getNbJ())i=1;
    			
    		deroulement(i);
    		i=i+2;
    		g=gagnant();
    		
    	}
    	
    	envoiGagnantP(g);
    	return g;
    }
    
    /**
     * procedure permettant l'envoi des jetons de tous les joueurs
     * @author steve giner
     */
    private void envoiJetonsJ() {
		
		String m="JETONJ";
		
		for(int i=0;i<getNbJ();i++){
			
			m=m+"@"+clientList.get(i).getPseudo()+"/"+clientList.get(i).getJetonsTotaux()+"/"+clientList.get(i).getJetonsPoses();
			
		}
    	
    	broadcastClientsPartie(m);
	}

    
    /**
     * envoi des cartes de la main
     * @author steve giner
     */
    private void envoiCarteM(int joueur) {
     
    	clientList.get(joueur).send("CARTEM@"+clientList.get(joueur).getCartes()[0]+"@"+clientList.get(joueur).getCartes()[1]);
    
    }
    
    
    /**
     * envoi des cartes de la table
     * @author steve giner
     */
    private void envoiCarteT(int nbc,int[] table) {
    	
    	String m="CARTET";
    	for(int i=0;i<nbc;i++){
    		
    		m=m+"@"+table[i];
    		
    	}
    	broadcastClientsPartie(m);
    }
    
    
   
    /**
     *  envoi joue et les parametres pour savoir ses choix
     * @author steve giner
     */
    private void envoiJoue(int joueur,int jetonsMin,int jetonsMax,String bool) {
    	clientList.get(joueur).send("JOUE@"+jetonsMin+"@"+jetonsMax+"@"+bool);
    }
    
    /**
     *  envoi la valeur que doit prendre le bouton relancer (on ou off)
     * @author steve giner
     */
    private void envoiRelancer(String bool) {
    	broadcastClientsPartie("RELANCER@"+bool);

    }
    
    /**
     *  envoi des jetons de la table
     * @author steve giner
     */
    private void envoiJetonsT(int jetons) {
    	broadcastClientsPartie("JETONT@"+jetons);

    }
    
    
    
    /**
     *  envoi des cartes de ceux qui ne sont pas couch�s
     * @author steve giner
     */
    private void envoiMontreC() {
    
     String m="MONTREC";
     for(int i=0;i<getNbJ();i++){
    	 
    	 if(clientList.get(i).getAttente()!=1){
    		 
    		 m=m+"@"+clientList.get(i).getPseudo()+clientList.get(i).getCartes()[0]+clientList.get(i).getCartes()[1];
    		 
    	 }
    	 
     }
     
     broadcastClientsPartie(m);
     
    }
    
 

  /**
   *  envoi le gagnant du tour (celui qui ramasse des jetons)
   * @author steve giner
   */
  private void envoiGagnantT(int[] t) {
   
	String m="GAGNANTT";  
	  
	for(int i=0;i<t.length;i++)
		m=m+"@"+clientList.get(t[i]).getPseudo();
	
	 broadcastClientsPartie(m);
  }
    
  
  /**
   * envoi le gagnant de la partie (celui qui gagne une victoire++)
   * @author steve giner
   */
  private void envoiGagnantP(int joueur) {
	  
	  broadcastClientsPartie("GAGNANTP@"+clientList.get(joueur).getPseudo());
	  
  
  }

  
	

  /**
   * envoi un perdant de la partie (celui qui gagne une defaite++)
   * @author steve giner
   */
  private void envoiPerdu(int joueur) {
	  broadcastClientsPartie("PERDU@"+clientList.get(joueur).getPseudo());
  }
  
  /**
   * determine si il ya un gagnant et donc si le tournoi et termine
   * @author steve giner
   */
	private int gagnant() {
		
    	int cpt=0;int j=-1;
    	for(int i=0;i<getNbJ();i++){
    	
    		if(clientList.get(i).getAttente()==-1){
    			cpt++;
    		}
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
    	
    	int jetons;//variable contenant les jetons poses max
    	int jetTable=0;//jetons au milieu de la table
    	
    	
    	
    	//envoi de 2 cartes a chaque joueurs 
    	for(int i=0;i<getNbJ();i++){
    		if(clientList.get(i).getAttente()!=-1){
	    	clientList.get(i).setCartes(je.tireUneCarte(),je.tireUneCarte());
	    	envoiCarteM(i);    	
                
    		}
    	}
    	
    	
    	
    	//j0 mise la blind et j1 la surblind
    	mise(b,5,jetTable);
    	
    	if(b+1<getNbJ())mise(b+1,10,jetTable);
    	else mise(0,10,jetTable);
    	
    	jetons=10;
    	

    	
    	//premiere enchere (choix: se coucher(1), suivre(2) ou relancer(3))
    	
    	int dernierRelance=b+1;
    	
    	enchere(b+2,dernierRelance,jetons,jetTable);
    	
  
    	
    	//fin du premier tour d'enchere
    	
    	
		int[] table=new int[5];
		table[0]=je.tireUneCarte();
		table[1]=je.tireUneCarte();
		table[2]=je.tireUneCarte();
		
		envoiCarteT(3, table);
		//envoi de la table aux clients
		
		//debut deuxieme tour
		
		//envoi de leur choix possibles (passer(1),ouvrir(2))
		
		int joueur=b;
		boolean bool=true;
		//int jetMis=0;
		//int cpt=0;
		
				
		bool=premPhase(joueur,dernierRelance,jetons, jetTable);
		
		if(bool){
		//debut 2nd phase
			enchere(joueur,dernierRelance,jetons,jetTable);
		//fin 2nd phase
		}
		//fin 2e enchere
		table[3]=je.tireUneCarte();
		envoiCarteT(4, table);
		//debut 3e enchere
		
		joueur=b;
		
		bool=premPhase(joueur,dernierRelance,jetons,jetTable);
		
		if(bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,jetTable);
			//fin 2nd phase
			}
		
		//debut 4e enchere
		
		table[4]=je.tireUneCarte();
		envoiCarteT(5, table);
		
		bool=premPhase(joueur,dernierRelance,jetons, jetTable);
		
		if(bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,jetTable);
			//fin 2nd phase
			}
		
		int nbjoueurs=0;
		int[][] cartes=new int[getNbJ()][2];
		int[] cl=new int[getNbJ()];
		
		
		
		
		for(int i=0;i<getNbJ();i++){
			
			if(clientList.get(i).getAttente()!=1 && clientList.get(i).getAttente()!=-1){
				cartes[nbjoueurs]=clientList.get(i).getCartes();
				cl[nbjoueurs]=i;
				nbjoueurs++;
			}
			
		}
		
		
		if(nbjoueurs>1){
			float[] g=je.gagnant(cartes,table,nbjoueurs);
			repartionDesGains(jetons,g);
				
			
			
			
		}
		else{
			if(clientList.get(cl[0]).getAttente()==0)
				clientList.get(cl[0]).setJetonsTotaux(clientList.get(cl[0]).getJetonsTotaux()+jetTable);
			else{
				for(int i=0;i<getNbJ();i++){
					
					clientList.get(cl[0]).setJetonsTotaux(clientList.get(cl[0]).getJetonsTotaux()+(min(clientList.get(i).getJetonsPoses(),clientList.get(cl[0]).getJetonsPoses())));
					clientList.get(i).setJetonsTotaux(clientList.get(i).getJetonsTotaux()+clientList.get(i).getJetonsPoses()-clientList.get(cl[0]).getJetonsPoses());
					
					
				}
				
				
			}
		}
		
		
    	envoiJetonsJ();
    	jetTable=0;
    	envoiJetonsT(jetTable);
		
	//on regarde si il y a des nouveaux perdants
		
		for(int i=0;i<getNbJ();i++){
			
			if(clientList.get(i).getAttente()!=-1){
				
				if(clientList.get(i).getJetonsTotaux()<=10){
					
					clientList.get(i).setAttente(-1);
					envoiPerdu(i);
				}
				
			}
			
		}
		
		
    }
    
    
	private int min(int a, int b) {
		
		if(a<b)return a;else return b;
		
		
	}

	private void repartionDesGains(int jetons, float[] g) {
		
		if(g.length==3){
			
			if(clientList.get((int)g[2]).getAttente()==0){// si il n'a pas fait tapis
				clientList.get((int)g[2]).setJetonsTotaux(clientList.get((int)g[2]).getJetonsTotaux()+jetons);
			}
			else{//si il a fait tapis
				for(int i=0;i<getNbJ();i++){
					
					clientList.get((int)g[2]).setJetonsTotaux(clientList.get((int)g[2]).getJetonsTotaux()+(min(clientList.get(i).getJetonsPoses(),clientList.get((int)g[2]).getJetonsPoses())));
					clientList.get(i).setJetonsTotaux(clientList.get(i).getJetonsTotaux()+clientList.get(i).getJetonsPoses()-clientList.get((int)g[2]).getJetonsPoses());
					
					
				}
			
			}
		
		}
		else{//si cas d'egali
			// a reflechir...
			
			int gain=jetons/(g.length-2);
			//int[][] att=new int[2][g.length-2];int latt0=0;int latt2=0;
			int d=g.length-2;
			int reste=jetons;
			
			
			for(int i=2;i<g.length;i++){
				
				if(gain>jetons-clientList.get(i).getPot()){
					
					
				}
				
				
				
			}
			
			
			/*for(int j=2;j<g.length;j++){
				if(clientList.get((int)g[j]).getAttente()==0){
					att[0][latt0]=j;latt0++;
				}
				else{
					att[1][latt2]=j;latt2++;
						
					}
				}
			int reste=jetons;	
			
			
			for(int i=0;i<latt2;i++){
				
				if()
				
			}
			
			
			if(latt0==0){
				
				
				
				
				
			}
			else{
				
				
				
				
				
			}*/
			
			
			
			}	
			
		
	}

	
	
	/**
     * fonction qui retournera le choix du joueur
     * @author steve giner
	 * @param jetMis jetons mise par le joueur
	 * @param joueur 
	 * @param jetons jetons max pose sur la table par un joueur
	 * @param relancer true si droit de relancer, false sinon
     */
    private int choix(int jetons, int jetMis, int joueur,boolean relancer) {
        
         //envoi min et max jetons possible de miser pour relancer
            //envoi de la demande de choix
    	
    	
    	int min=jetons-clientList.get(joueur).getJetonsPoses();
    	int max=clientList.get(joueur).getJetonsTotaux();
    	
    	if(min>=max)
    		envoiJoue(joueur, min, max, "false");
    	else envoiJoue(joueur, min, max, ""+relancer);
    	
    	clientList.get(joueur).setJoue(1);
    	
        try {
            attenteDeChoix.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(PokerPartie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(choixJ==3 && relancer){
        	choixJ=2;
        	
        	
        }
        
        if(choixJ==1){
        	
        	jetMis=0;
        	
        }
        else{
        	if(choixJ==2){
        		
        		jetMis=min;
        		
        		
        	}
        	else{
        		
        		jetMis=jetJ;
        		
        	}
        	
        	
        }
    	
		return choixJ;
	}

    
    
/**
 * 
 * @param choix  le numero de l'action choisie
 * @param jetons le nombre de jetons joués
 * @author Maurin Benjamin , Giner Steve
 * @return 
 */
    public String jouage(int choix,int jetons,PokerClientThread client)
    { // ne pas oublier les tests
        String retour;
    	if(choix<0 || choix>3){
    		choixJ=1;
    		jetJ=0;
    		retour= "MNC";
    		
    	}
    	else{
    		if(jetons<0 || jetons>client.getJetonsTotaux()){
    			
    			retour= "MJET";
    		}

    	
    	else{
    		choixJ=choix;
    		jetJ=jetons;

	        retour= "JOK";
	    	}
    	}
    	
        attenteDeChoix.release();
        client.setJoue(0);
        return retour;
    }
    
	/**
    * procedure permettant de changer les jetons d'un joueurs
    * @author steve giner
    * @param jetons nb de jetons a enlever
	 * @param jetTable 
    */
	private void mise(int joueur, int jetons, int jetTable) {
		
		clientList.get(joueur).setJetonsTotaux(clientList.get(joueur).getJetonsTotaux()-jetons);
		clientList.get(joueur).setJetonsPoses(clientList.get(joueur).getJetonsPoses()+jetons);
		
		if(clientList.get(joueur).getJetonsTotaux()==0)clientList.get(joueur).setAttente(2);
		clientList.get(joueur).send("JETONS:"+clientList.get(joueur).getJetonsTotaux());
		jetTable=jetTable+jetons;
		
    	envoiJetonsJ();
    	envoiJetonsT(jetTable);
	}
    

	
	/**
	* procedure permettant d'effectuer la premiere phase d'un tour d'enchere
	* @author steve giner
	* @param nbJ nombre de joueurs qu'il reste
	*/
	private boolean premPhase(int joueur,int dernierRelance,int jetons,int jetTable){
		boolean bool=true;
		int cpt=0;
		int jetMis=0;
		
		//envoi de leur choix possibles (passer(1),ouvrir(2))
	while(bool && cpt<getNbJ()){
				
				if(joueur==getNbJ())joueur=0;
				
				if(clientList.get(joueur).getAttente()==0){
					
		   			if(choix(jetons,jetMis, joueur, true)==3){
		   				mise(joueur,jetMis,jetTable);
		   				jetons=clientList.get(joueur).getJetonsPoses(); 
		   				majPot(joueur,jetMis);
		   				dernierRelance=joueur;
		   				bool=false;
		   			}	
			   	}
				joueur++;cpt++;	
			}
	
		return bool;
	}
	
	
	
	/**
	* procedure permettant d'effectuer un tour d'enchere
	* @author steve giner
	* @param nbJ nombre de joueurs qu'il reste
	*/
	private void enchere(int joueur,int dernierRelance,int jetons,int jetTable) {
    
		
    	int jetMis=0;
		if(joueur==getNbJ())joueur=0;
		else if(joueur>getNbJ())joueur=1;
		boolean bool=true;
		boolean relan=true;
		int rel=0;//nombre de relances
		String m="";
		
		while(bool)
    	{
    		if(joueur==getNbJ())joueur=0;
    		
    		if(joueur==dernierRelance)bool=false;
    		else{
	    		if(clientList.get(joueur).getAttente()==0){
			    	m="JOUES";
	    			 clientList.get(joueur).send(m);
	    			switch(choix(jetons,jetMis,joueur, relan)){
			    	case 1:
			    		clientList.get(joueur).setAttente(1);
			    		break;
			    	
			    	case 2:			    		
				    		mise(joueur,jetMis,jetTable);
				    		majPot(joueur,jetMis);
			    			break;
			    	
			    	case 3:
			    		mise(joueur,jetMis,jetTable);
			    		rel++;
			    		jetons=clientList.get(joueur).getJetonsPoses();
			    		majPot(joueur,jetMis);
			    		dernierRelance=joueur;
			    		if(rel==3){
			    			relan =false;
			    		}	    	
				    	break;		
			    	}
	    		}
    		joueur++;
    		}
    	}
		
	}

	private void majPot(int joueur,int jetMis) {
		
		for(int i=0;i<getNbJ();i++){
			
			if(clientList.get(i).getAttente()==2 && clientList.get(joueur).getJetonsPoses()>clientList.get(i).getJetonsPoses()){
				
				clientList.get(i).ajoutePot(jetMis);
				
			}
			
		}
		
		
	}
    
	
	
	
}