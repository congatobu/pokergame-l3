import java.io.*;
import java.util.*;

public class Jeu {

	private Stack<Integer> tas = new Stack<Integer>();
	
	/**
	*initialise le paquet de 52 cartes
	*@author Steve Giner
	*/
	public void initTasDe52cartes(){
		int[] tab=new int[52];
		int random;
		int tp;
		
		for(int i=1;i<53;i++)
			tab[i-1]=i;
		
		for(int i=0;i<54;i++)
		{
			random = (int)(Math.random()*(52-i)) + i;
			tp=tab[i];
			tab[i]=tab[random];
			tab[random]=tp;
			
			tas.push(tab[i]);
		 
		 
		}
	}
	
		/**
		*Fonction pour libérer la mémoire
		*@author Steve Giner
		*/				
	protected void finalize() throws Throwable{
	 try {
	 tas =null;
    } catch(Exception e) {e.printStackTrace();}
    finally {
		  super.finalize();
    }
	 
	}
	
	
	/**
	*tire une carte du paquet
	*@author Steve Giner
	*/
	public int tireUneCarte()
	{
		return tas.pop();
			
	}
	
	/**
	*retourne la couleur d'une carte, si le chiffre est 3 alors c'est pique,2 c'est coeur, 1 trefle et 4 c'est carreaux
	*@author Steve Giner
	*/
	public int couleurCarte(int carte){
		
		float reste=carte/4; //si le nombre apres la virgule est de .25 alors c'est pique,.5 c'est coeur, .75 trefle et aucun c'est carreaux
		reste=reste-(int)(carte/4);
		
		if(reste==0.75)return(1);
		else if(reste==0.5)return(2);
		else if(reste==0.25)return(3);
		else return(4);
		
	}
	
	/**
	*retourne la valeur d'une carte(2 a 14)
	*@author Steve Giner
	*/
	public int valeur(int carte){
		if((carte-1)/4<1)return(14);
		else return(((carte-1)/4)+1);
			
	}
	
	/**
	*retourne un string representant le nom de la carte(1 a 13)
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
		float reste=carte/4; //si le nombre apres la virgule est de .25 alors c'est pique,.5 c'est coeur, .75 trefle et aucun c'est carreaux
		reste=reste-(int)(carte/4);
		int coul=couleurCarte(carte);
		if(coul==1)nom=nom+"Carreaux";
		else if(coul==2)nom=nom+"Trefle";
		else if(coul==3)nom=nom+"Coeur";
		else nom=nom+"Pique";
		
		return nom;
	}
	
	
	/**
	*retourne unt string representant le nom de la carte(1 a 13)
	*@author Steve Giner
	*/
	public boolean royale(int[] coul){
		
		boolean bool=true;
		
		
		
		
		return(bool);
	}
	
	
	/**
	*retourne unt string representant le nom de la carte(1 a 13)
	*@author Steve Giner
	*/
	public int quinteFlush(int[][] coul){
		
		int val=0;
		int[][] cpt={{0,0,0,0},{0,0,0,0}};//on va prendre la valeur de la carte trouvée la meilleure dans chaque couleur et voir si on arrive a en faire une suite de 5 cartes 
		int c=-1;
		int r=-1;
		
		
		
		for(int i=6;i>0;i++){
			
			if(cpt[1][coul[1][i]]==0){
				
				cpt[1][coul[1][i]]++;
				cpt[0][coul[1][i]]=coul[0][i];
				
			}
			else{//on a deja une valeur a trouvée
				
				if(cpt[0][coul[1][i]]==coul[0][i-1]+1){
					cpt[1][coul[1][i]]++;
					cpt[0][coul[1][i]]=coul[0][i-1];
				}
				else{
					cpt[1][coul[1][i]]=0;
					
				}
				
			}
			
		}
			
		
		for(int i=0;i<4;i++){
			
			if(cpt[1][coul[1][i]]>=5){
				if(val<cpt[1][coul[1][i]]+cpt[0][coul[1][i]]-1)
				val=cpt[1][coul[1][i]]+cpt[0][coul[1][i]]-1;
				
			}
			
		}
		
			
		
		return(val);	
	}
	
	
	/**
	*retourne unt string representant le nom de la carte(1 a 13)
	*@author Steve Giner
	*/
	public int carre(int[] valeur){
		
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
	*@author Steve Giner
	*/
	public int couleur(int[][] coul){
		
		int val=0;
		int[][] cpt=new int[2][4];
		
		for(int i=0;i<7;i++){
			
			cpt[1][coul[1][i]-1]++;
			if(cpt[0][coul[i][1]-1]<coul[0][i])cpt[0][coul[i][1]-1]=coul[0][i];
		}		
		
		for(int i=0;i<4;i++)
			if(cpt[1][i]>=5)val=cpt[0][i];
		
		return(val);		
		
	}
	
	
	/**
	*il faut lui envoyer un tableau avec les valeur des cartes
	*retourne la valeur de la meilleur quinte (val de la plus grande carte de la suite), si il n'y en a pas retourne 0
	*@author Steve Giner
	*/
	public int quinte(int[] valeur){
		
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
				else if(valeur[i]!=valeur[i-1]+1)cpt=0;
			}
		}
			
	
		
		
		return(val);		
		
	}
	
	
	/**
	*il faut lui envoyer un tableau avec les valeur des cartes
	*retourne la valeur de la meilleur triplette, si il n'y en a pas retourne 0
	*@author Steve Giner
	*/
	public int triplette(int[] valeur){
		
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
	*il faut lui envoyer un tableau avec les valeur des cartes
	*retourne les valeurs des deux paires(les meilleures)(la meilleure des 2 en 0), si il n'y a pas 2 paires retourne 0 et 0
	*@author Steve Giner
	*/
	public int[] doublePaire(int[] valeur){//
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
			
		
		
		return(val);
		
		
				
		
	}
	
	
	/**
	*retourne valeur de la paires,si pas de paire retourne 0,faut lui envoyer un tableau avec les valeur des cartes,n est la taille du tableau
	*@author Steve Giner
	*/
	public int paire(int[] valeur,int n){
		
		
		int i=0;
		
		
		
		while(i<6){
			
			if(valeur[i]==valeur[i+1])return(valeur[i]);
			
			i++;
		}
		
		return(0);
		
				
		
	}
	
	
	/**
	*retourne la valeur de la meilleur triplette en 0 et celle de la meilleur paire en 1 (sans prendre en compte la triplette trouvée)
	*retourne un 0 si une des deux n'est pas trouvé
	*@author Steve Giner
	*/
	public int[] full(int[] valeur){
		
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
		
			
			
		return(val);		
		
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
	*carré==7
	*Quinte Flush==8 (5 cartes de la même famille qui se suivent)
	*la royale==9
	*@author Steve Giner
	*/
	public int valeurMain(int[] m){//
	
	int i=0;
	boolean bool=false;
	int tmp=0;
	int[][] valCoul=new int[2][7];
	
	
	while(!bool){
		bool=true;
		for(i=0;i<6;i++){
			if(m[i]>m[i+1]){
				tmp=m[i];
				m[i]=m[i+1];
				m[i+1]=tmp;
				bool=false;
			}
			
		}
	
	}	
	
	for(i=0;i<7;i++){
		valCoul[0][i]=valeur(m[i]);
		valCoul[1][i]=couleurCarte(m[i]);
		
	}
	
	
	
	//ici on lance toutes les fonction de valeur des cartes
	
	
		return(0);
	}
	
}
