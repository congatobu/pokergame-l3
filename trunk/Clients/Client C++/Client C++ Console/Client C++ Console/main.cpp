#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#pragma comment(lib, "ws2_32.lib")

#define port 6667

#include "Reseau.h"

using namespace std;

int main()
{
int choix=0;
Reseau mr;
mr.connecterServeur();
while(1)
{
	cout<<"\n 1) pour se connecter\n 2) pour avoir les parties\n 3) pour creer un compte\n 4) pour Changer de pseudo\n 5) pour Changer de mot de passe"<<endl;
	cin>>choix;

if(choix==1)
{
	mr.connecterServeurAMdp();

	cout<<"\n 1) voir les parties en cours\n 2) Creer une partie\n 3) pour rejoindre une partie\n 4) pour Changer de pseudo\n 5) pour Changer de mot de passe"<<endl;
	cin>>choix;

	if(choix==1){mr.getlistepartie();}
	if(choix==2){mr.creerPartie();}
	if(choix==3){mr.rejoindreUnePartie();}

}
else if(choix==2){} //demande les infos sur un joueur
else if(choix==3){mr.creerCompte();}
else if(choix==4){mr.changerPseudo();}
else if(choix==5){mr.changerMdp();}
else{cout<<"mauvais choix !!!!!!!!!!";}
//mr.communiquerServeur();
cout<<"voulez vous vous deconnecter ? 1 oui, 2 non "<<endl;
cin>>choix;
if(choix==1){mr.deconnecterServeur();}
else if(choix==2){main();}
}

return 0;
}