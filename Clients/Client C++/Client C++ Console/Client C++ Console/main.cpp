#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#include <windows.h>
#include <tchar.h>
#pragma comment(lib, "ws2_32.lib")

#define port 6667

#include "Reseau.h"

using namespace std;


using namespace System::Threading;

void ecoute()
{
	while(phrase!=0)
	{
	recv(socketID, phrase, 255, 0);
		cout<<"phrase :"<<phrase<<endl;
	if(strcmp(phrase,"CONNECTOK")==0)
	{
		std::cout << " Connexion établie " << std::endl;
	}
	else if(strcmp(phrase,"REJOK")==0)
	{
		std::cout << " C'est bon " << std::endl;
	}
	else if(strcmp(phrase,"PNE")==0)
	{
		std::cout << "  La partie n'existe pas " << std::endl;
	}
	else if(strcmp(phrase,"TOOMANY")==0)
	{
		std::cout << " trop de joueurs dans la partie " << std::endl;
	}
	else if(strcmp(phrase,"AIP")==0)
	{
		std::cout << "Le joueur est deja dans une partie" << std::endl;
	}
	else if(strcmp(phrase,"PEC")==0)
	{
		std::cout << " La partie est en cours " << std::endl;
	}
	else if(strcmp(phrase,"NCON")==0)
	{
		std::cout << " Il faut se connecter avant " << std::endl;
	}
	else if(strcmp(phrase,"CREATOK")==0)
	{
		std::cout << " Compte Créé " << std::endl;
	}
	else if(strcmp(phrase,"AUPSEUDO")==0)
	{
		std::cout << " Pseudo déjà utilisé, ajout impossible " << std::endl;
	}
	else if(strcmp(phrase,"ERREURBDD")==0)
	{
		std::cout << " erreur a l'ajout, veuillez recommencer " << std::endl;
	}
	else if(strcmp(phrase,"WFPSEUDO")==0)
	{
		std::cout << "mauvais format de pseudo" << std::endl;
	}
	else if(strcmp(phrase,"WFPASS")==0)
	{
		std::cout << " mauvais format de password " << std::endl;
	}
	else if(strcmp(phrase,"NCON")==0)
	{
		std::cout << " Il faut se connecter avant " << std::endl;
	}
	else if(strcmp(phrase,"OK")==0)
	{
		std::cout << " Pseudo changé " << std::endl;
	}
	else if(strcmp(phrase,"WPASS")==0)
	{
		std::cout << " mauvais password utilisé, changement impossible " << std::endl;
	}
	else if(strcmp(phrase,"WPSEUDO")==0)
	{
		std::cout << " pseudo introuvable " << std::endl;
	}
	else if(strcmp(phrase,"AUPSEUDO")==0)
	{
		std::cout << " Pseudo deja utilisé " << std::endl;
	}
	else if(strcmp(phrase,"OK")==0)
	{
		std::cout << " Mot de passe changé " << std::endl;
	}
	else if(strcmp(phrase,"WPASS")==0)
	{
		std::cout << " mauvais password utilisé, changement impossible " << std::endl;
	}
	else if(strcmp(phrase,"WPSEUDO")==0)
	{
		std::cout << " pseudo introuvable " << std::endl;
	}
	else if(strcmp(phrase,"WFPASS")==0)
	{
		std::cout << " Mot de passe incorect mauvais format " << std::endl;
	}
	else if(strcmp(phrase,"WFPSEUDO")==0)
	{
		std::cout << " mauvais format de pseudo " << std::endl;
	}
	
	else if(strcmp(phrase,"CREATPOK")==0)
	{
		std::cout << " C'est bon la partie est créée " << std::endl;
	}
	else if(strcmp(phrase,"PAU")==0)
	{
		std::cout << "  La partie déjà utilisé " << std::endl;
	}
	else if(strcmp(phrase,"WFP")==0)
	{
		std::cout << " mauvais format nom de partie " << std::endl;
	}
	else if(strcmp(phrase,"AIP")==0)
	{
		std::cout << "Le joueur est deja dans une partie" << std::endl;
	}
	else if(strcmp(phrase,"NCON")==0)
	{
		std::cout << " Il faut se connecter avant " << std::endl;
	}
	else if(strcmp(phrase,"DEBUTPARTIE")==0)
	{
		std::cout << " C'est bon " << std::endl;
	}
	else if(strcmp(phrase,"WFPSEUDO")==0)
	{
		std::cout << " mauvais format de pseudo " << std::endl;
	}

	}
cout<<"vous avez déco";

}


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
   
   Thread ^trd;
ThreadStart ^myThreadDelegate = gcnew ThreadStart(this,ecoute,NULL);
    trd = gcnew Thread(myThreadDelegate);
    trd->Start();
    
	return 0;
}






