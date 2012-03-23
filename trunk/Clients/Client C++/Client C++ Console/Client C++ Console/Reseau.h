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
	void deconnecterServeur();
	int changerPseudo();
	int communiquerServeur();

private:
	
	char phrase[255];
	const char *getliste;
	const char *pseudomdp;
	const char *rjp;
	string nomcompte;
	string nompartie;
	string mdp;
	string ancienpseudo;
	string nouveaupseudo;
	struct sockaddr_in informations;
	SOCKET socketID;
};