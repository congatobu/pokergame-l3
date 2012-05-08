package pokerPackage;




import java.util.*;

    /**
	*Classe permettant la manipulation et comparaison des cartes et des mains
	*@author Steve Giner
	*/
public class Jeu {

	private Stack<Integer> tas = new Stack<Integer>();
	private int[][] vc=new int[2][52];
	/**
	*constructeur qui initialise le tableau vc avec les valeur et les couleur des cartes(2 a 14, 0 a 3)
	*@author Steve Giner
	*/
	Jeu(){
		int cpt=0;
		int cpt2=1;
		
		for(int i=0;i<52;i++){
			
			if(cpt>3){cpt=0;cpt2++;}
			vc[0][i]=cpt2;
			vc[1][i]=cpt;
			cpt++;
		}
		
		for(int i=0;i<4;i++)vc[0][i]=14;
		
		//initTasDe52cartes();	
		
		
	}
	
	
	/**
	*initialise le paquet de 52 cartes (0 a 51).
	*@author Steve Giner
	*/
	public void initTasDe52cartes(){
		int[] tab=new int[52];
		int random;
		int tp;
		tas.clear();
		
		for(int i=0;i<52;i++){
			tab[i]=i;

			
		}
		

		
		for(int i=0;i<52;i++)
		{
			random = (int)(Math.random()*(52-i)) + i;
			tp=tab[i];
			tab[i]=tab[random];
			tab[random]=tp;
			
			tas.push(tab[i]);
		 
		 
		}
	}
	
		/**
		*Fonction pour liberer la memoire
		*@author Steve Giner
		*/				
	protected void finalize() throws Throwable{
	 try {
	 tas =null;
         vc=null;
    } catch(Exception e) {e.printStackTrace();}
    finally {
		  super.finalize();
    }
	 
	}
	
	
	/**
	*tire une carte du paquet
	*@return int - retourne une carte(0 a 51)
	*@author Steve Giner
	*/
	public int tireUneCarte()
	{
		return tas.pop();
			
	}
	
	/**
	*retourne la couleur d'une carte, si le chiffre est 3 alors c'est pique,2 c'est coeur, 1 trefle et 0 c'est carreaux
	*@param int carte - int de 0 a 51
	*@return int - couleur d'une carte (0 a 3)
	*@author Steve Giner
	*/
	public int couleurCarte(int carte){
		
		/*float reste=carte/4; //si le nombre apres la virgule est de .25 alors c'est pique,.5 c'est coeur, .75 trefle et aucun c'est carreaux
		reste=reste-(int)(carte/4);
		
		if(reste==0.75)return(1);
		else if(reste==0.5)return(2);
		else if(reste==0.25)return(3);
		else return(4);*/
		
		return(vc[1][carte]);
	}
	
	/**
	*retourne la valeur d'une carte(2 a 14)
	*@param int carte - entier de 0 a 51
	*@return int - valeur de la carte (2 a 14)
	*@author Steve Giner
	*/
	public int valeur(int carte){
		/*if((carte-1)/4<1)return(14);
		else return(((carte-1)/4)+1);*/
		return(vc[0][carte]);	
	}
	
	/**
	*retourne un string representant le nom de la carte(1 a 13)
	*@param int carte - entier de 0 a 51
	*@return String - nom de la carte
	*@author Steve Giner
	*/
	public String nomCarte(int carte){
		
		String nom="";
		
		int val=valeur(carte);
		if(val==14)nom="As";
		else if(val==13)nom="Roi";
		else if(val==12)nom="Dame";
		else if(val==11)nom="Valet";
		else nom=""+val;
		
		
		nom=nom+" de ";
		int coul=couleurCarte(carte);
		if(coul==1)nom=nom+"Carreaux";
		else if(coul==2)nom=nom+"Trefle";
		else if(coul==3)nom=nom+"Coeur";
		else nom=nom+"Pique";
		
		return nom;
	}
	
	
	
	
	/**
	*retourne la valeur de la meilleure carte de la quinteflush
	*@param int[][] coul - tableau avec en 0 la valeur de la carte et 1 la couleur
	*@return int - valeur de la meilleure carte de la quinteflush si il n'y en a pas retourne 0
	*@author Steve Giner
	*/
	private int quinteFlush(int[][] coul){
		
		int val=0;
		int[][] cpt={{0,0,0,0},{0,0,0,0}};//on va prendre la valeur de la carte trouvee la meilleure dans chaque couleur et voir si on arrive a en faire une suite de 5 cartes 

		
		
		
		for(int i=6;i>-1;i--){
			
			if(cpt[1][coul[1][i]]==0){//si on a pas de de suite commencee
				
				cpt[1][coul[1][i]]++;
				cpt[0][coul[1][i]]=coul[0][i];
				
			}
			else{//on a deja une valeur a trouvee
				
				if(cpt[0][coul[1][i]]==coul[0][i]+cpt[1][coul[1][i]]){
					cpt[1][coul[1][i]]++;
				}
				else{
					
					if(coul[0][i]!=2)cpt[1][coul[1][i]]=0;
					
				}
				
			}
			
		}
			
		
		for(int i=0;i<4;i++){

			if(cpt[1][i]>=5 && val < cpt[0][i])
			val=cpt[0][i];
			
		}
		
		
		if(val==0){//pour voir si on a une suite qui commence a As(on regarde donc si il y a une suite qui a 5 comme max)
			for(int i=0;i<4;i++){
				if(cpt[0][i]==5 && cpt[1][i]==4 && val==0){
					for(int j=0;j<7;j++){
						if(coul[j][0]==14 && coul[j][1]==i){
							val=5;
						}
					}
				}		
			}	
		}
			
		
		return(val);	
	}
	
	
	/**
	*retourne la valeur du carre, 0 si il n'y en a pas
	*@param int[] valeur - tableau avec les valeurs des 7 cartes de la main
	*@return int - valeur du carre, 0 si il n'y en a pas
	*@author Steve Giner
	*/
	private int carre(int[] valeur){
		
		int val=0;
		
		int i=0;
	
		
		
		while(i<4){
			
				if(valeur[i]==valeur[i+1] && valeur[i+1]==valeur[i+2] && valeur[i+3]==valeur[i+2]){
										
						if(val<valeur[i])val=valeur[i];
						
						i=i+4;
				}
				else i++;
				
		}
		
		
		return(val);		
		
	}
	
	
	
	
	
	/**
	*retourne la valeur de la meilleure carte de la couleur si elle existe sinon 0
	*@param int[][] coul - tableau avec en 0 la valeur de la carte et 1 la couleur
	*@return int - valeur de la meilleure carte de la couleur si elle existe sinon 0
	*@author Steve Giner
	*/
	private int couleur(int[][] coul){
		
		int val=0;
		int[][] cpt=new int[2][4];
		
		for(int i=0;i<7;i++){
			
			cpt[1][coul[1][i]]++;
			if(cpt[0][coul[1][i]] < coul[0][i]) cpt[0][coul[1][i]]=coul[0][i];
		}		
		
		for(int i=0;i<4;i++)
			if(cpt[1][i]>=5)val=cpt[0][i];
		
		return(val);		
		
	}
	
	
	/**
	*retourne la valeur de la meilleur quinte (val de la plus grande carte de la suite), si il n'y en a pas retourne 0
	*@param int[] valeur - tableau avec les valeurs des 7 cartes de la main
	*@return valeur de la meilleur quinte (val de la plus grande carte de la suite), si il n'y en a pas retourne 0
	*@author Steve Giner
	*/

	private int quinte(int[] valeur){
		
		int val=0;
		int cpt=0;
		
		for(int i=6;i>0;i--){
			if(cpt>=4)i=0;
			else{
				if(valeur[i]==valeur[i-1]+1){
					if(cpt==0){
						val=valeur[i];
					}
					cpt++;
				}
				else if(valeur[i]!=valeur[i-1]+1 && valeur[i]!=valeur[i-1])cpt=0;
			}
		}
			
	
		if(cpt>=4)
		return(val);		
		else{
			if(cpt==3 && val==5 && valeur[6]==14){
				
				
				return 5;
			}
			else
			return 0;
		}
			
			
	}
	
	
	/**
	*retourne la valeur de la meilleur triplette(brelan), si il n'y en a pas retourne 0
	*@param int[] valeur - tableau avec les valeurs des 7 cartes de la main
	*@return valeur de la meilleur triplette, si il n'y en a pas retourne 0
	*@author Steve Giner
	*/
	private int triplette(int[] valeur){
		
		int val=0;
		
		int i=0;
	
		
		
		while(i<5){
			
				if(valeur[i]==valeur[i+1] && valeur[i+1]==valeur[i+2]){
										
						if(val<valeur[i])val=valeur[i];
						
						i=i+3;
				}
				else i++;
				
		}
		
		
		return(val);
		
	}
	
	
	/**
	*retourne les valeurs des deux paires(les meilleures)(la meilleure des 2 avant le '.'), si il n'y a pas 2 paires retourne 0.0
	*@param int[] valeur - tableau avec les valeurs des 7 cartes de la main
	*@return float - valeurs des deux paires(les meilleures)(la meilleure des 2 avant le '.'), si il n'y a pas 2 paires retourne 0.0
	*@author Steve Giner
	*/
	private float doublePaire(int[] valeur){//
		//
		
		int[] val={0,0};
		
		int i=0;
		int tmp=0;
		
		
		
		
		while(i<6){
			
				if(valeur[i]==valeur[i+1]){
					
					if(valeur[i]>val[1]){
						val[1]=valeur[i];
						
						if(val[0]<val[1]){
							tmp=val[0];val[0]=val[1];val[1]=tmp;//on echange val[0] et val[1] pour avoir le plus grand en 0
						}
					}
					
					i=i+2;
				}
				else i++;
		}
			
		if(val[1]!=0)
		
		return((float)val[0]+(float)val[1]/(float)100);
		
		else return (float) 0.0;
				
		
	}
	
	
	/**
	*retourne valeur de la paires,si pas de paire retourne 0,faut lui envoyer un tableau avec les valeur des cartes,n est la taille du tableau
	 * @param int[] valeur - tableau avec les valeurs des 7 cartes de la main 
	 * @param int n - taille du tableau
	 * @return valeur de la paires,si pas de paire retourne 0
	 *@author Steve Giner
	 */
	private int paire(int[] valeur,int n){
		
		
		int i=0;
		
		
		
		while(i<n-1){
			
			if(valeur[i]==valeur[i+1])return(valeur[i]);
			
			i++;
		}
		
		return(0);
		
				
		
	}
	
	
	/**
	*retourne la valeur de la meilleur triplette avant la virgule et celle de la meilleur paire apres la virgule (sans prendre en compte la triplette trouvee)
	*retourne un 0 si une des deux n'est pas trouve(pas de full)
	*@param int[] valeur - tableau avec les valeurs des 7 cartes de la main
	*@return float - valeur de la meilleur triplette avant la virgule et celle de la meilleur paire apres la virgule,0 si une des deux n'est pas trouve(pas de full)
	*@author Steve Giner
	*/
	private float full(int[] valeur){
		
		int[] val=new int[2];
		
		val[0]=triplette(valeur);
		int[] reste=new int[4];
		int i=0;
		int cpt=0;
		
		while(cpt<4){
			
			if(valeur[i]!=val[0]){
				reste[cpt]=valeur[i];cpt++;
			}
			i++;
		}
			val[1]=paire(reste,4);
		
		if(val[1]!=0 && val[0]!=0)	
			
		return(val[0]+val[1]/100);		
		
		else return (float)0.0;
	}
	
	
	
	/**
	*m contient la main du joueur+les carte sur la table
	*retourne la meilleure combinaison de carte du joueur
	*paire==1
	*2 paires==2
	*triplette==3
	*quinte==4 (5 cartes qui se suivent mais de familles differentes)	
	*couleur==5
	*paires+triplette==6 (il parait que ca se dit full)
	*carre==7
	*Quinte Flush==8 (5 cartes de la meme famille qui se suivent)
	*la royale==9
	*et retourne la valeur de la meilleure carte de la combinaison
	*@param int[] m - main du joueur+table(7 cartes)
	*@return float[] - valeur de la combinaison en 0 et meilleure carte de la combinaison en 1, si pas de combinaison alors retourne 0 et la la valeur de la meilleure carte du joueur
	*@author Steve Giner
	*/
	private float[] valeurMain(int[] m){//
	
	int i=0;
	
	int[][] valCoul=new int[2][7];
	
	
	for(i=0;i<7;i++){
		valCoul[0][i]=valeur(m[i]);
		valCoul[1][i]=couleurCarte(m[i]);
		
	}
	
	//ici on lance toutes les fonction de valeur des cartes
	int val=quinteFlush(valCoul);
	float val2;
	if(val==14)
		return(new float[]{9,val});
	
	else if(val!=0)
		return(new float[]{8,val});
	
	else if((val=carre(valCoul[0]))!=0)
		return(new float[]{7,val});
	
	else if((val2=full(valCoul[0]))!=0.0)
		return(new float[]{6,val2});
	
	else if((val=couleur(valCoul))!=0)
		return(new float[]{5,val});
	
	else if((val=quinte(valCoul[0]))!=0)
		return(new float[]{4,val});
	
	else if((val=triplette(valCoul[0]))!=0)
		return(new float[]{3,val});
	
	else if((val2=doublePaire(valCoul[0]))!=0.0)
		return(new float[]{2,val2});
	
	else if((val=paire(valCoul[0],7))!=0)
		return(new float[]{1,val});

	else return(new float[]{0,valCoul[0][6]}); 
	
	
		
	}
	
	
	/**
	*determine qui est le gagnant, retourne un tableau contenant en:
	*0==la valeur de la combinaison
	*1==la valeur de la meilleure carte de la combinaison(si double combinaison(full ou double paire) le nombre apres la virgule indique la valeur des cartes de la deuxieme combinaison) 
	*2 et +==le ou les joueurs gagnants
	*@param int[][] cartes - tableau contenant les main de chaque joueur (2 cartes)
	*@param int[] table - tableau contenant les cartes de la table (5 cartes)
	*@param int nbjoueurs - nombre de joueurs
	*@return float[] - retourne le ou les gagnant avec la combinaisons (0==la valeur de la combinaison,1==la valeur de la meilleure carte de la combinaison(si double combinaison(full ou double paire) le nombre apres la virgule indique la valeur des cartes de la deuxieme combinaison),2 et +==le ou les joueurs gagnants)
	*@author Steve Giner
	*/
	public float[] gagnant(int[][] cartes,int[] table,int nbjoueurs){
		
		
		float[][] valeur=new float[nbjoueurs][2];//on recupere les valeurs des mains de chaque joueur
		
		int[][] m=new int[nbjoueurs][7];
		
		int cpt=0;
		int imax[]=new int[nbjoueurs];
		float max0=-1;
		float max1=-1;
		int b=-1;
		boolean bool=false;
		int tmp=0;
		
		for(int j=0;j<nbjoueurs;j++){
			for(int i=0;i<5;i++){
				m[j][i]=table[i];			
				
			}
			
			m[j][5]=cartes[j][0];
			m[j][6]=cartes[j][1];
			
			//on tri m[j]
			bool=false;
			tmp=0;
			while(!bool){
				bool=true;
				for(int k=0;k<6;k++){
					if(valeur(m[j][k])>valeur(m[j][k+1])){
						tmp=m[j][k];
						m[j][k]=m[j][k+1];
						m[j][k+1]=tmp;
						bool=false;
					}					
				}
			}
			
			
			
			valeur[j]=valeurMain(m[j]);//en 0 on a la valeur de la combinaison et dans 1 celle de la meilleure carte de la combinaison
			
			if(max0<valeur[j][0]){//ici on recupere les cas d'egalites
				
				max0=valeur[j][0];
				max1=valeur[j][1];
				cpt=0;
				imax[0]=j;
			}
			else{
				if(max0==valeur[j][0]){
					if(max1<valeur[j][1]){
						
						max1=valeur[j][1];
						cpt=0;
						imax[0]=j;
						
					}
					else{
						if(max1==valeur[j][1]){
							cpt++;
							imax[cpt]=j;
						}
					}	
				}				
			}
		}				
		
		
		
		if(cpt>0){//si il y a une egalite
			int imaxfinal=0;
			boolean egal=false;
			int[] egalite=new int[cpt+1];
			int cpt2=0;
			
			
			for(int i=1;i<cpt+1;i++){
				b=compareReste(m[imaxfinal],m[i],valeur[i]);
				
				if(b==2){
										
					imaxfinal=i;					
					cpt2=0;
					egalite[cpt2]=i;
				}
				else{
					if(b==0){
								
						egalite[cpt2]=i;
						cpt2++;
					}
											
				}
			}
			
			if(cpt2==0)	
				return (new float[]{max0,max1,imaxfinal});
				else{
					float[] f=new float[cpt2+3];
					f[0]=max0;f[1]=max1;f[2]=imaxfinal;
					for(int i=0;i<cpt2;i++){
						f[i+3]=egalite[i];
					}
					return (f);	
				}
				
			
			
		}
		else{
				
			return (new float[]{max0,max1,imax[0]});	
		}
		
	
	}
	
	/**
	*compare le reste des mains de 2 joueurs,return 0 si egalite,1 si m1 moins bien que m2 et 2 si m1 mieux que m2. error==-1
	*@param int[] m1 - main d'un joueur (7 cartes)
	*@param int[] m2 - main d'un autre joueur
	*@param float[] val - contient en 0 la valeur de la combinaison et en 1 la valeur de la meilleure carte de la combinaison(si double combinaison(full ou double paire) le nombre apres la virgule indique la valeur des cartes de la deuxieme combinaison) 
	*@return int - 0 si egalite,1 si m1 moins bien que m2 et 2 si m1 mieux que m2. error==-1
	*@author Steve Giner
	*/
	private int compareReste(int[] m1, int[] m2, float[] val) {
		int[] r1;
		int[] r2;
		int b=-1;
		int cpt1=0; int cpt2=0;
		
		
		switch ((int)val[0]) 
		{ 
		case 0:	b=compare(m1,m2,7);//pas de combi
				break; 
				
		case 1: r1=new int[5];//paire
				r2=new int[5];
				
				for(int i=0;i<7;i++){
					if(valeur(m1[i])!=val[1]){r1[cpt1]=m1[i];cpt1++;}//on prend les restes
					if(valeur(m2[i])!=val[1]){r2[cpt2]=m2[i];cpt2++;}
				}
				b=compare(r1,r2,5);
				break;
				
		case 2: r1=new int[3];//2 paires
				r2=new int[3]; 
				int a=(int)( ( (float)( (float)val[1]- (int)val[1]) ) * (float)100);
				for(int i=0;i<7;i++){
					
					if(valeur(m1[i])!=(int)val[1] && valeur(m1[i])!=a){r1[cpt1]=m1[i];cpt1++;}
					if(valeur(m2[i])!=(int)val[1] && valeur(m2[i])!=a){r2[cpt2]=m2[i];cpt2++;}
				}
				b=compare(r1,r2,3);
				break;
				
		case 3: r1=new int[4];//triplette
				r2=new int[4]; 
				
				for(int i=0;i<7;i++){
					if(valeur(m1[i])!=(int)val[1]){r1[cpt1]=m1[i];cpt1++;}
					if(valeur(m2[i])!=(int)val[1]){r2[cpt2]=m2[i];cpt2++;}
				}
				b=compare(r1,r2,4);
				break;

				
		case 7: r1=new int[3];//carre
				r2=new int[3]; 
				
				for(int i=0;i<7;i++){
					if(valeur(m1[i])!=(int)val[1]){r1[cpt1]=m1[i];cpt1++;}
					if(valeur(m2[i])!=(int)val[1]){r2[cpt2]=m2[i];cpt2++;}
				}
				b=compare(r1,r2,3);
				break;

		default:break; 
				
		}
			
		
		
		return b;
	}
	
	/**
	*compare le reste des mains de 2 joueurs,return 0 si egalite,1 si m1 moins bien que m2 et 2 si m1 mieux que m2.
	*@param int[] m1 - reste d'une main
	*@param int[] m2 - reste d'une autre main
	*@param taille - taille de m1 et m2
	*@return int - 0 si egalite,1 si m1 moins bien que m2 et 2 si m1 mieux que m2
	*@author Steve Giner
	*/
	private int compare(int[] m1, int[] m2,int taille) {
	
	int b=0;
	int j=taille-1;
	while(b==0 && j>1)
	{
		if(valeur(m1[j])<valeur(m2[j]))b=1;
		else if(valeur(m1[j])>(m2[j]))b=2;
		else j--;
			
	}
		
	return b;
	
	}
	
	
	
		

		
		
	
	
	
	
}
