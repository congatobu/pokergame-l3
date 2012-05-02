#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#include <windows.h>
#include <tchar.h>
#include <string>
#pragma comment(lib, "ws2_32.lib")

#define port 6667

#include "Reseau.h"

using namespace std;

void boucleConnecte()//Fonction de jeu lorsque l'utilisateur est connecté
{

	Reseau mr;
	int choix=0;
	mr.connecterServeurAMdp();


	cout<<"\n 1) voir les parties en cours\n 2) Creer une partie\n 3) pour rejoindre une partie\n "<<endl; //liste des choix qui s'offre a l'utilisateur
	cin>>choix;//on recupere le choix de l'utilisateur

	if(choix==1){mr.lancerPartie();}// si le choix de l'utilisateur est 1 on lance la partie
	if(choix==2){mr.creerPartie();}// si le choix de l'utilisateur est 2 on crée une parie
	if(choix==3){mr.rejoindreUnePartie();//  si le choix de l'utilisateur est 3 on rejoind la partie
					cout<<"1) lancer la partie (si vous etes l'hotes)\n 2)lancer le jeu\n ";
					cin>>choix;
					if(choix==1){mr.lancerPartie();}
					if(choix==2){mr.lancerJeu();}
				}
}

void boucleNonConnecte()//Fonction de jeu durant la quelle l'utilisateur n'est pas connecté
{
int choix=0;
Reseau mr;
mr.connecterServeur();

cout<<"\n 1) pour se connecter\n 2) pour avoir les infos sur un joueur \n 3) pour creer un compte\n 4) pour Changer de pseudo\n 5) pour Changer de mot de passe"<<endl;
cin>>choix;

if(choix==1)
{
	mr.connecterServeur();
	boucleConnecte();
}
else if(choix==2){mr.connecterServeur();mr.getInfo();} //demande les infos sur un joueur
else if(choix==3){mr.connecterServeur();mr.creerCompte();}//Créer un compte
else if(choix==4){mr.connecterServeur();mr.changerPseudo();}//Changer le pseudo
else if(choix==5){mr.connecterServeur();mr.changerMdp();}//Permet le changement de mot de passe
else{cout<<"mauvais choix !!!!!!!!!!";}
//mr.communiquerServeur();
cout<<"1) Pour vous deconnecter \n 2) Pour le menu (Non conencté)\n 3) Pour le menu2 (connecté)\n "<<endl;
cin>>choix;
if(choix==1){mr.deconnecterServeur();}//se deconnecte
else if(choix==2){boucleNonConnecte();}//lance la fonction en mode non connecté
else if(choix==2){boucleConnecte();}//lance la fonction en mode connecté

}

int main()
{
	
	boucleNonConnecte();// fonction mode non connecté
    
	return 0;
}