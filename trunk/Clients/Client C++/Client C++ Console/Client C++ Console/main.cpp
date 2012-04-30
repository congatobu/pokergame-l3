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
using namespace System;
using namespace System::Threading;

void boucleConnecte()
{

	Reseau mr;
	int choix=0;
	mr.connecterServeurAMdp();


	cout<<"\n 1) voir les parties en cours\n 2) Creer une partie\n 3) pour rejoindre une partie\n "<<endl;
	cin>>choix;

	if(choix==1){mr.lancerPartie();}
	if(choix==2){mr.creerPartie();}
	if(choix==3){
		mr.rejoindreUnePartie();
		cout<<"1) lancer la partie (si vous etes l'hotes)\n 2)lancer le jeu\n ";
		cin>>choix;
			if(choix==1){mr.lancerPartie();}
			if(choix==2){mr.lancerJeu();}
		}
}

void boucleNonConnecte()
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
else if(choix==3){mr.connecterServeur();mr.creerCompte();}
else if(choix==4){mr.connecterServeur();mr.changerPseudo();}
else if(choix==5){mr.connecterServeur();mr.changerMdp();}
else{cout<<"mauvais choix !!!!!!!!!!";}
//mr.communiquerServeur();
cout<<"1) Pour vous deconnecter \n 2) Pour le menu (Non conencté)\n 3) Pour le menu2 (connecté)\n "<<endl;
cin>>choix;
if(choix==1){mr.deconnecterServeur();}
else if(choix==2){boucleNonConnecte();}
else if(choix==2){boucleConnecte();}

}

int main()
{
	
	boucleNonConnecte();
    
	return 0;
}