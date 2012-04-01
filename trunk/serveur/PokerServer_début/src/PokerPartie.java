

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
    private int enCours = 0; // 0 non 1 oui 2 transition
    private int maxPlayers = 8;
    private Semaphore available = new Semaphore(1, true);
    private Semaphore attenteDeChoix = new Semaphore(0, true);
    private Semaphore availableLancementPartie = new Semaphore(1, true);
    private String nomP = "";
    private Jeu je=new Jeu();
    private PokerClientThread createur;
    private int choixJ=0;
    private int jetJ=0;
    //int[] enJeu=new int[getNbJ()];

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
     * @return availableLancementPartie
     */
    public Semaphore getSem(){
        return this.availableLancementPartie;
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
    
    public int getNbJReel(){
    	int cpt=0;
    	
    	for(int i=0;i<getNbJ();i++){
    		
    		if(clientList.get(i)!=null)cpt++;
    		
    	}
    	
    	return cpt;
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
            if(foo!=null)
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
                if(enCours==0 || enCours==2)clientList.remove(num);
                else clientList.set(num, null);
            }               
	  deadClient.setPartie(null);
         
         
            if(getNbJReel()<1){
                PokerServer.deletePartie(this);
            }
            
            if(deadClient==createur)
            {
            int i=0;
            	while(i!=-1){
                
            		if(clientList.get(i)!=null){
            			createur=clientList.get(i);
            			i=-1;
            		}
            		else
            		i++;
            	}
            	 
            }
            broadcastClientsPartie(listeJoueursPartie());   
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
        	if(clientList.get(i)!=null){
	            if(clientList.get(i)!=createur)
	            s+="@"+clientList.get(i).getPseudo();  
	            else
	            s+="@$"+clientList.get(i).getPseudo(); 
        	}
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
           availableLancementPartie = null;
           // screenOut.close();
        }catch(Exception e) {
        
        }
        finally {
            super.finalize();
        } 
    }
    
       /**
    * procedure permettant de tester qui sont les joueurs pret avant de lancer la partie
    * Kick les joueurs pas pret au bout de 5 secondes
    * @author Maurin Benjamin
    */
    public int testPret(){
        
        //pour pas que yai plusieurs lancement en même temps
        try {
            availableLancementPartie.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(PokerPartie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //si quelqu'un a lancé la partie juste avant on abandonne l'idée de la relancer
        if(enCours==1)return -1;
        
        // ca quite si ya un seul pas pret
              int i;
        PokerClientThread foo;
        for (i=0;i<clientList.size();++i){
        	
            foo = (PokerClientThread)clientList.get(i);
            if(foo!=null && foo.getPret(i)==0){
                availableLancementPartie.release();
                return -1;
            }
        } 	
        
        //sinon ca lance
          enCours=1;
          //lancer dans un nouveau thread la partie
          ExecPartie e = new ExecPartie(this);
          e.start();
          broadcastClientsPartie("DEBUTPARTIE");
          availableLancementPartie.release();
        return 1;
    }
    
          
     /**
    * Met tous les joueurs à "pas pret" pour être en attente qu'ils envoi un message pour prouver qu'ils sont pret
    * @author Maurin Benjamin
    */
    public void pasPret(){
               
             int i;
        PokerClientThread foo;
        for (i=0;i<clientList.size();++i){
        	
            foo = (PokerClientThread)clientList.get(i);
            if(foo!=null)
            foo.setPret(0);
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
	    	if(clientList.get(i)!=null){
		    	clientList.get(i).setJetonsTotaux(200);
		    	clientList.get(i).setJetonsPoses(0);
	    	}else clientList.remove(i);
	    	
    	}
    	i=0;//contient le joueur qui pose la blind
    	boolean bool=true;
    	while(g==-1){
    		je.initTasDe52cartes();
    		
    		for(int j=0;j<getNbJ();j++){//suppression des client null
    	    	
    	    	if(clientList.get(j)==null)clientList.remove(j);
    	    	    	    	
        	}
    	       	
        	envoiJetonsJ();
        	bool=true;
	        	while(bool){//choix du blindeur 
	        	
	        		
		    		if(i==getNbJ())i=0;
		    		
		    		if(clientList.get(i)==null)i++;
		    		else{
		    			
		    			if(clientList.get(i).getAttente()!=-1)
		    			bool=false;
		    			else i++;
		    		}
		    			
	        	}
	        	
	    	deroulement(i);
	    	i++;
	    	g=gagnant();
    		
    	}
    	
    	if(g!=-2){
    	envoiGagnantP(g);
    	return g;
    	}
    	else{screenOut.println("erreur gagnant");return -1;}
    }
    
    /**
     * procedure permettant l'envoi des jetons de tous les joueurs
     * @author steve giner
     */
    private void envoiJetonsJ() {
		
		String m="JETONJ";
		
		for(int i=0;i<getNbJ();i++){
			if(clientList.get(i)!=null)
			m=m+"@"+clientList.get(i).getPseudo()+"/"+clientList.get(i).getJetonsTotaux()+"/"+clientList.get(i).getJetonsPoses();
			
		}
    	
    	broadcastClientsPartie(m);
	}

    
    /**
     * envoi des cartes de la main
     * @author steve giner
     */
    private void envoiCarteM(int joueur) {
    	
    	if(clientList.get(joueur)!=null)clientList.get(joueur).send("CARTEM@"+clientList.get(joueur).getCartes()[0]+"@"+clientList.get(joueur).getCartes()[1]);
    
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
     *  envoi joue et les parametres pour savoir ses choix,bool:envoi la valeur que doit prendre le bouton relancer (on ou off)
     * @author steve giner
     */
    private void envoiJoue(int joueur,int jetonsMin,int jetonsMax,String bool) {
    	if(clientList.get(joueur)!=null)clientList.get(joueur).send("JOUE@"+jetonsMin+"@"+jetonsMax+"@"+bool);
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
    	 if(clientList.get(i)!=null){
	    	 if(clientList.get(i).getAttente()!=1 && clientList.get(i).getAttente()!=-1){
	    		 
	    		 m=m+"@"+clientList.get(i).getPseudo()+clientList.get(i).getCartes()[0]+clientList.get(i).getCartes()[1];
	    		 
	    	 }
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
		if(clientList.get(t[i])!=null)m=m+"@"+clientList.get(t[i]).getPseudo();
	
	 broadcastClientsPartie(m);
  }
    
  
  /**
   * envoi le gagnant de la partie (celui qui gagne une victoire++)
   * @author steve giner
   */
  private void envoiGagnantP(int joueur) {
	  
	  if(clientList.get(joueur)!=null)broadcastClientsPartie("GAGNANTP@"+clientList.get(joueur).getPseudo());
	  
  
  }

  
	

  /**
   * envoi un perdant de la partie (celui qui gagne une defaite++)
   * @author steve giner
   */
  private void envoiPerdu(int joueur) {
	  if(clientList.get(joueur)!=null)broadcastClientsPartie("PERDU@"+clientList.get(joueur).getPseudo());
  }
  
  /**
   * determine si il ya un gagnant et donc si le tournoi et termine
   * @author steve giner
   */
	private int gagnant() {
		
    	int cpt=0;int j=-1;
    	for(int i=0;i<getNbJ();i++){
    	
    		if(clientList.get(i)!=null){
	    		if(clientList.get(i).getAttente()==0){
	    			cpt++;
	    			j=i;
	    		}
	    		
    		}
    	}
    	
    	if(cpt<=1){
    		if(cpt==1)return j;
    		else return -2;
    	}
    	else return -1;
	
	}

	
	/**
    * procedure permettant le deroulement d'une partie
    * @author steve giner
    */
    public void deroulement(int b){
    	
    	//initialisation du paquet de cartes
    	
    	int jetons=0;//variable contenant les jetons poses max
    	int jetTable=0;//jetons au milieu de la table
    	int nbJoueur=getNbJReel();//nombre de joueurs en jeu (si ==1 faut finir)
    	int joueur=b;
    	int nbTapis=0;
    	
    	//envoi de 2 cartes a chaque joueurs 
    	for(int i=0;i<getNbJ();i++){
    		if(clientList.get(i)!=null){
	    		if(clientList.get(i).getAttente()!=-1){
	    			clientList.get(i).setAttente(0);
	    			clientList.get(i).setJetonsPoses(0);
			    	clientList.get(i).setCartes(je.tireUneCarte(),je.tireUneCarte());
			    	clientList.get(i).setPot(0);
			    	envoiCarteM(i);    	
	                
	    		}else nbJoueur--;
    		}
    	}
    	
    	
    	
    	//j0 mise la blind et j1 la surblind
    	mise(joueur,5,jetTable,nbTapis);
    	joueur++;
    	if(joueur<getNbJ()) mise(joueur,10,jetTable,nbTapis);
    	else{joueur=0; mise(joueur,10,jetTable,nbTapis);}
    	
    	jetons=10;
    	

    	
    	//premiere enchere (choix: se coucher(1), suivre(2) ou relancer(3))
    	
    	int dernierRelance=b+1;
    	joueur++;
    	premiereEnchere(joueur,-1,jetons,jetTable,nbJoueur,nbTapis);
    	
  
    	
    	//fin du premier tour d'enchere
    	
    	
		int[] table=new int[5];
		table[0]=je.tireUneCarte();
		table[1]=je.tireUneCarte();
		table[2]=je.tireUneCarte();
		
		envoiCarteT(3, table);
		//envoi de la table aux clients
		
		//debut deuxieme tour
		
		//envoi de leur choix possibles (passer(1),ouvrir(2))
		
		
		boolean bool=true;
		//int jetMis=0;
		//int cpt=0;
		
				
		bool=premPhase(joueur,dernierRelance,jetons, jetTable,nbJoueur,nbTapis);
		
		if(!bool){
		//debut 2nd phase
			enchere(joueur,dernierRelance,jetons,jetTable, nbJoueur,nbTapis );
		//fin 2nd phase
		}
		//fin 2e enchere
		table[3]=je.tireUneCarte();
		envoiCarteT(4, table);
		//debut 3e enchere
		
		joueur=b;
		
		bool=premPhase(joueur,dernierRelance,jetons,jetTable, nbJoueur,nbTapis);
		
		if(!bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,jetTable, nbJoueur,nbTapis);
			//fin 2nd phase
			}
		
		//debut 4e enchere
		
		table[4]=je.tireUneCarte();
		envoiCarteT(5, table);
		
		bool=premPhase(joueur,dernierRelance,jetons, jetTable, nbJoueur,nbTapis);
		
		if(!bool){
			//debut 2nd phase
				enchere(joueur,dernierRelance,jetons,jetTable, nbJoueur,nbTapis);
			//fin 2nd phase
			}
		
		
		int[][] cartes=new int[nbJoueur][2];
		int[] cl=new int[nbJoueur];//indice du joueur
		int cpt=0;
		
		
		
		for(int i=0;i<getNbJ();i++){
			if(clientList.get(i)!=null){
				if(clientList.get(i).getAttente()==0 || clientList.get(i).getAttente()==2){
					cartes[cpt]=clientList.get(i).getCartes();
					cl[cpt]=i;
					
				}
			}
		}
		
		
		if(nbJoueur>1){
			float[] g=je.gagnant(cartes,table,nbJoueur);
			repartionDesGains(jetTable,g,g.length,true);
				
			
			
			
		}
		else{
			if(clientList.get(cl[0])!=null){
				if(clientList.get(cl[0]).getAttente()==0)
					clientList.get(cl[0]).setJetonsTotaux(clientList.get(cl[0]).getJetonsTotaux()+jetTable);
			}
		}
		
		
    	envoiJetonsJ();
    	jetTable=0;
    	envoiJetonsT(jetTable);
		
	//on regarde si il y a des nouveaux perdants
		
		for(int i=0;i<getNbJ();i++){
			
			if(clientList.get(i)!=null){
				if(clientList.get(i).getAttente()!=-1){
					
					if(clientList.get(i).getJetonsTotaux()<=10){
						
						clientList.get(i).setAttente(-1);
						envoiPerdu(i);
					}
					
				}
				
			}
		}
		
    }
    
    


	private int min(int a, int b) {
		
		if(a<b)return a;else return b;
		
		
	}

	private void repartionDesGains(int jetTable, float[] g,int taille,boolean testReste) {
		
		if(taille==3){
		
			if(clientList.get((int)g[2]).getAttente()==0){// si il n'a pas fait tapis
				clientList.get((int)g[2]).setJetonsTotaux(clientList.get((int)g[2]).getJetonsTotaux()+jetTable);
			}
		}
		else{
		
			int reste=jetTable;
			int gain=0;
			int jreste=taille-2;
			boolean[] r=new boolean[jreste];//sert a savoir si on lui a donn assez de jetons
			for(int i=0;i<jreste;i++)r[i]=false;
				
				
			
			
			gain=reste/jreste;
			
			while(gain>0 && jreste>0){
				
			
			
				for(int i=2;i<taille;i++){
					
					if(clientList.get((int)g[i])!=null && !r[i-2]){
						
							if(gain>=jetTable-clientList.get((int)g[i]).getPot()){
								
								clientList.get((int)g[i]).setJetonsTotaux(clientList.get((int)g[i]).getJetonsTotaux()+jetTable-clientList.get((int)g[i]).getPot());
								jreste--;
								r[i-2]=true;
								clientList.get((int)g[i]).ajoutePot(gain);
								reste=reste-(jetTable-clientList.get((int)g[i]).getPot());
							}
							else{//gain<
								
								clientList.get((int)g[i]).setJetonsTotaux(clientList.get((int)g[i]).getJetonsTotaux()+gain);
								clientList.get((int)g[i]).ajoutePot(gain);
								reste=reste-gain;
							}					
					}
				}
				
				gain=reste/jreste;
			}
			
			if(testReste){
				
				if(reste!=0){//on rend ca au(x) perdant(s) pas couch(s)
					
					float[] reste1=new float[getNbJ()-taille+2];
					reste1[0]=0;reste1[1]=0;
					boolean exist=false;
					int cpt=2;
					for(int i=2;i<getNbJ();i++){
						
						if(clientList.get(i)!=null){
							if(clientList.get(i).getAttente()!=1 && clientList.get(i).getAttente()!=-1){
							
								for(int j=2;j<taille;j++){
									
									if(i==g[j])exist=true;
									
								}
								if(!exist){
									reste1[cpt]=i;
									cpt++;
								}
							}
							
						}
					
					}
					
					repartionDesGains(reste, reste1, cpt, false);
					
				}
			}
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
    	
    	if(clientList.get(joueur)!=null){
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
        if(client!=null){
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
        else {
        	choixJ=1;return("JOK");
        }
    }
    
	/**
    * procedure permettant de changer les jetons d'un joueurs
    * @author steve giner
    * @param joueur qui mise
    * @param jetons nb de jetons a enlever au joueur et a mettre sur la table
	 * @param jetTable 
    */
	private void mise(int joueur, int jetons, int jetTable,int nbTapis) {
		
		if(clientList.get(joueur)!=null){
			clientList.get(joueur).setJetonsTotaux(clientList.get(joueur).getJetonsTotaux()-jetons);
			clientList.get(joueur).setJetonsPoses(clientList.get(joueur).getJetonsPoses()+jetons);
			
			if(clientList.get(joueur).getJetonsTotaux()==0){clientList.get(joueur).setAttente(2);nbTapis++;}
			clientList.get(joueur).send("JETONS:"+clientList.get(joueur).getJetonsTotaux());
			jetTable=jetTable+jetons;
			
	    	envoiJetonsJ();
	    	envoiJetonsT(jetTable);
		}
	}
    

	
	/**
	* procedure permettant d'effectuer la premiere phase d'un tour d'enchere
	* @author steve giner
	* @param nbJ nombre de joueurs qu'il reste
	*/
	private boolean premPhase(int joueur,int dernierRelance,int jetons,int jetTable,int nbJoueur,int nbTapis){
		boolean bool=true;
		int cpt=0;
		int jetMis=0;
		
		
	while(bool && cpt<getNbJ() && nbJoueur-nbTapis>1){
				
		if(joueur==getNbJ())joueur=0;
		
		if(clientList.get(joueur)!=null){
    		if(clientList.get(joueur).getAttente()==0){
		    	

    			switch(choix(jetons,jetMis,joueur, true)){
		    	case 1:
		    		if(clientList.get(joueur)!=null){
		    		clientList.get(joueur).setAttente(1);
		    		}
		    		nbJoueur--;
		    		
		    		break;
		    	
		    	case 3:
		    		if(clientList.get(joueur)!=null){
		    		mise(joueur,jetMis,jetTable,nbTapis);
		    		
		    		jetons=clientList.get(joueur).getJetonsPoses();
		    		majPot(joueur,jetMis);
		    		dernierRelance=joueur;
		    		bool=false;
		    		}
			    	break;
		    	default: break;
		    	}
    		}
				joueur++;cpt++;	
			
		}
	}
		return bool;
	}
	
	
	
	/**
	* procedure permettant d'effectuer le premier tour d'enchere
	* @author steve giner
	 * @param nbJoueur 
	* @param nbJ nombre de joueurs qu'il reste
	*/
	private void premiereEnchere(int joueur, int dernierRelance, int jetons,int jetTable, int nbJoueur,int nbTapis) {
    	int jetMis=0;
    	int SB=joueur-1;
		if(joueur==getNbJ())joueur=0;
		else if(joueur>getNbJ())joueur=1;
		boolean bool=true;
		boolean relan=true;
		int rel=0;//nombre de relances
		
		
		while(bool && nbJoueur-nbTapis>1)
    	{
    		if(joueur==getNbJ())joueur=0;
    		
    		if(joueur==dernierRelance)bool=false;
    		else{
    			if(clientList.get(joueur)!=null){
		    		if(clientList.get(joueur).getAttente()==0){
				    	
		    			
		    			switch(choix(jetons,jetMis,joueur, relan)){
				    	case 1:
			    			if(dernierRelance==-1 && joueur==SB){
			    				dernierRelance=SB;
			    				bool=false;
			    			}
			    			if(clientList.get(joueur)!=null)
				    		clientList.get(joueur).setAttente(1);
				    		nbJoueur--;
				    		break;
				    	
				    	case 2:	
				    			if(dernierRelance==-1 && joueur==SB){
				    				dernierRelance=SB;
				    				bool=false;
				    			}
				    			if(clientList.get(joueur)!=null){
						    		mise(joueur,jetMis,jetTable,nbTapis);
						    		majPot(joueur,jetMis);
				    			}else nbJoueur--;
				    			break;
				    	
				    	case 3:
				    		if(clientList.get(joueur)!=null){
					    		mise(joueur,jetMis,jetTable,nbTapis);
					    		rel++;
					    		jetons=clientList.get(joueur).getJetonsPoses();
					    		majPot(joueur,jetMis);
					    		dernierRelance=joueur;
					    		if(rel==3){
					    			relan =false;
					    		}
				    		}else nbJoueur--;
					    	break;	
				    	default: break;
				    	}
		    		}
    			}
    		joueur++;
    		}
    	}
		
	}
	
	/**
	* procedure permettant d'effectuer un tour d'enchere.
	* @author steve giner
	* @param nbJ nombre de joueurs qu'il reste
	* * @param nbTapis 
	*/
	private void enchere(int joueur,int dernierRelance,int jetons,int jetTable,int nbJoueur, int nbTapis) {
    
		
    	int jetMis=0;
    	
		if(joueur==getNbJ())joueur=0;
		else if(joueur>getNbJ())joueur=1;
		boolean bool=true;
		boolean relan=true;
		int rel=0;//nombre de relances
		
		
		
		while(bool && nbJoueur-nbTapis>1)
    	{
    		if(joueur==getNbJ())joueur=0;
    		
    		if(joueur==dernierRelance)bool=false;
    		else{
    			if(clientList.get(joueur)!=null){
		    		if(clientList.get(joueur).getAttente()==0){
				    	
	
		    			switch(choix(jetons,jetMis,joueur, relan)){
				    	case 1:
				    		if(clientList.get(joueur)!=null)
				    			clientList.get(joueur).setAttente(1);
				    		nbJoueur--;
				    		break;
				    	
				    	case 2:	if(clientList.get(joueur)!=null){		    		
						    		mise(joueur,jetMis,jetTable,nbTapis);
						    		majPot(joueur,jetMis);
				    			}else nbJoueur--;
				    			break;
				    	
				    	case 3:
				    		if(clientList.get(joueur)!=null){
					    		mise(joueur,jetMis,jetTable,nbTapis);
					    		rel++;
					    		jetons=clientList.get(joueur).getJetonsPoses();
					    		majPot(joueur,jetMis);
					    		dernierRelance=joueur;
					    		if(rel==3){
					    			relan =false;
					    		}
				    		}else nbJoueur--;
					    	break;
				    	default: break;
				    	}
		    		}
    			}
    		joueur++;
    		}
    	}
		
	}

	private void majPot(int joueur,int jetMis) {
		
		for(int i=0;i<getNbJ();i++){
			if(clientList.get(i)!=null && clientList.get(joueur)!=null){
				if(clientList.get(i).getAttente()==2 && clientList.get(joueur).getJetonsPoses()>clientList.get(i).getJetonsPoses()){
					
					clientList.get(i).ajoutePot(jetMis);
					
				}
			}
		}
		
		
	}
    
	
	
	
}