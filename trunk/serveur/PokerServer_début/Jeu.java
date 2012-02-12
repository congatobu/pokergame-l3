import java.io.*;
import java.util.*;

public class Jeu {

	private Stack<Integer> tas = new Stack<Integer>();
	
	
	
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
	
	
	public int tireUneCarte()
	{
		return tas.pop();
			
	}
	
	public int couleur(int carte){
		
		float reste=carte/4; //si le nombre apres la virgule est de .25 alors c'est pique,.5 c'est coeur, .75 trefle et aucun c'est carreaux
		reste=reste-(int)(carte/4);
		
		if(reste==0.75)return(1);
		else if(reste==0.5)return(2);
		else if(reste==0.25)return(3);
		else return(4);
		
	}
	
	public int valeur(int carte){
		if(((carte+1)/4)==1)return(14);
		else return(carte/4);
			
	}
	
	
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
		int coul=couleur(carte);
		if(coul==1)nom=nom+"Carreaux";
		else if(coul==2)nom=nom+"Trefle";
		else if(coul==3)nom=nom+"Coeur";
		else nom=nom+"Pique";
		
		return nom;
	}
	
	
	public boolean royale(int[] m){
		
		boolean bool=true;
		
		
		
		
		return(bool);
	}
	
	
	public boolean quinteFlush(int[] m){
		
		boolean bool=true;
		
		
		
		
		return(bool);		
		
	}
	
	
	public boolean carre(int[] m){
		
		boolean bool=true;
		
		
		
		
		return(bool);		
		
	}
	
	
	public boolean full(int[] m){
		
		boolean bool=true;
		
		
		
		
		return(bool);		
		
	}
	
	
	public boolean couleur(int[] m){
		
		boolean bool=true;
		
		
		
		
		return(bool);		
		
	}
	
	
	public int quinte(int[] valeur){//il faut lui envoyer un tableau avec les valeur des cartes
		//retourne la valeur de la meilleur quinte(val de la plus grande carte de la suite), si il n'y en a pas retourne 0
		int val=0;
		
		
		for(int i=0;i<7;i++){
			
		}
		
		return(val);		
		
	}
	
	
	public int triplette(int[] valeur){//il faut lui envoyer un tableau avec les valeur des cartes
		//retourne la valeur de la meilleur triplette, si il n'y en a pas retourne 0
		int val=0;
		
		int i=0;
		int j=0;
		
		int cpt=0;//pour connaitre le nombre de carte de meme valeur trouvé
		
		
		
		while(i<7){
			j=i+1;
			cpt=0;
			while(j<7)
			{
				if(valeur[i]==valeur[j]){
					cpt=cpt++;
					if(cpt==2 && val<valeur[i]){
						val=valeur[i];j=7;//on prend la valeur de la triplette et on arrete cette boucle
					}
				}
				else j++;
			}
			i++;
		}
		
		return(val);
		
	}
	
	
	public int[] doublePaire(int[] valeur){//il faut lui envoyer un tableau avec les valeur des cartes
		//retourne les valeurs des deux paires(les meilleures)(la meilleure des 2 en 0), si il n'y a pas 2 paires retourne 0 et 0
		
		int[] val={0,0};
		
		int i=0;
		int j=0;
		int tmp=0;
		
		
		
		
		while(i<7){
			j=i+1;
			
			while(j<7)
			{
				if(valeur[i]==valeur[j]){
					
					if(valeur[i]>val[1]){
						val[1]=valeur[i];
						
						if(val[0]<val[1]){
							tmp=val[0];val[0]=val[1];val[1]=tmp;//on echange val[0] et val[1] pour avoir le plus grand en 0
						}
					}	
					
				}
				else j++;
			}
			i++;
		}
		
		return(val);
		
		
				
		
	}
	
	
	public int paire(int[] valeur){//retourne valeur de la paires,si pas de paire retourne 0,faut lui envoyer un tableau avec les valeur des cartes
		
		
		int i=0;
		
		
		
		while(i<6){
			
			if(valeur[i]==valeur[i+1])return(valeur[i]);
			
			i++;
		}
		
		return(0);
		
				
		
	}
	
	
	
	
	public int valeurMain(int[] m){//m contient la main du joueur+les carte sur la table
	/* 
	paire==1
	2 paires==
	triplette==3
	quinte==4 (5 cartes qui se suivent mais de familles differentes)	
	couleur==5
	paires+triplette==6 (il parait que ca se dit full)
	carré==7
	Quinte Flush==8 (5 cartes de la même famille qui se suivent)
	la royale==9
	*/
	int i=0;
	int[] val=new int[7];
	boolean bool=false;
	int tmp=0;
	
	for(i=0;i<7;i++)val[i]=valeur(m[i]);
	
	while(!bool){
		bool=true;
		for(i=0;i<6;i++){
			if(val[i]>val[i+1]){
				tmp=val[i];
				val[i]=val[i+1];
				val[i+1]=tmp;
				bool=false;
			}
			
		}
	
	}	
	
	//ici on lance toutes les fonction de valeur des cartes
	
	
		return(0);
	}
	
}
