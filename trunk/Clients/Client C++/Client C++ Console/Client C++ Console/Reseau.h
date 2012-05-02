#pragma once
#include "Reseau.h"
//#include "stdafx.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <windows.h>


#include <errno.h>
#include <string>

using namespace std;


class Reseau
{
public:
	Reseau(void);
	~Reseau(void);
	int analTram();// fonction qui annalyse les trames envoyées par le serveur
	DWORD WINAPI ecoute(LPVOID arg);// fonction qui ecoute a l'aide d'un thread en permanance le serveur
	int connecterServeur();// connecter au serveur sans mot de passe
	int connecterServeurAMdp();// connecter au serveur avec mot de passe
	int creerCompte();// creer un compte
	int getlistepartie();// permet d'avoir les parties en cours
	int rejoindreUnePartie();// rejoindre une partie en cours
	int creerPartie();// créer une partie
	void deconnecterServeur();// se deconnecter du serveur
	int changerPseudo();// changer le pseudo
	int changerMdp();// changer le mot de passe
	int getInfo();// permet d'avoir des infos sur un joueur
	int lancerPartie();// lancer une partie si vous etes l'hote
	int lancerJeu();// lancer le jeu
	int communiquerSessrveur();// chat qui permet de connecter avec le serveur

private:
	
	char phrase[255];
	const char *getliste;
	const char *pseudomdp;
	const char *rjp;
	string getinfo;
	string nomcompte;
	string nompartie;
	string nbjoueursmax;
	string mdp;
	string pseudo;
	string ancienpseudo;
	string nouveaupseudo;
	string ancienmdp;
	string nouveaumdp;
	struct sockaddr_in informations;
	SOCKET socketID;
};