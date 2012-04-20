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
	int analTram();

	int connecterServeur();
	int connecterServeurAMdp();
	int creerCompte();
	int getlistepartie();
	int rejoindreUnePartie();
	int creerPartie();
	void deconnecterServeur();
	int changerPseudo();
	int changerMdp();
	int getInfo();
	int lancerPartie();
	int lancerJeu();
	int communiquerSessrveur();

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