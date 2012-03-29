#pragma once
#include "Reseau.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#include <string>

using namespace std;

class Reseau
{
public:
	Reseau(void);
	~Reseau(void);
	int connecterServeur();
	int connecterServeurAMdp();
	int creerCompte();
	void getlistepartie();
	int rejoindreUnePartie();
	int creerPartie();
	void deconnecterServeur();
	int changerPseudo();
	int changerMdp();
	int communiquerServeur();

private:
	
	char phrase[255];
	const char *getliste;
	const char *pseudomdp;
	const char *rjp;
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