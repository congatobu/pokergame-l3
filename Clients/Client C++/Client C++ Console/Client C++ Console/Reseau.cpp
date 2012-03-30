#include "Reseau.h"

#pragma comment(lib, "ws2_32.lib")

#define PORT 6667

Reseau::Reseau(void)
{
}

Reseau::~Reseau(void)
{
}

int Reseau::connecterServeur(void)
{
WSADATA wsaData;
int res = WSAStartup(MAKEWORD(2, 0), &wsaData);


informations.sin_family = AF_INET;
informations.sin_port = htons(6667);
informations.sin_addr.s_addr = inet_addr("88.167.230.145");

socketID = socket(AF_INET, SOCK_STREAM, 0);

if (socketID == INVALID_SOCKET) 
	{
		perror("socket");
	}

if ((connect(socketID, (sockaddr*) &informations, sizeof(sockaddr_in))) != SOCKET_ERROR) 
	{
		perror("connect");
	}

return 0;
}

int Reseau::communiquerServeur(void)
{
do
	{
		std::cout << "Entrez votre phrase : ";
		fgets(phrase, 255, stdin);
	
		if ((send(socketID, phrase, strlen(phrase), 0)) == 0)
		perror("send");
	
		if (strcmp(phrase, "EXIT") != 0) 
		{
			memset(phrase, 0, 255);
			recv(socketID, phrase, 255, 0);
			std::cout << "Phrase reçue : " << phrase << std::endl;
		}
	} 
while (strcmp(phrase, "EXIT") != 0);
return 0;
}	
int Reseau::creerCompte(void)
{
	pseudomdp="";
	nomcompte="";
	mdp="";
	
	std::cout << "Entrez votre Pseudo : ";
	cin>>nomcompte;

	std::cout << "Entrez votre mot de passe : ";
	cin>>mdp;

	string tmp="CREATCPT@"+nomcompte+"@"+mdp+"\n";

	pseudomdp = tmp.c_str();

	cout<<pseudomdp<<endl;

	if ((send(socketID, pseudomdp, strlen(pseudomdp), 0)) == 0)
		perror("send");
	cout<<sizeof(&pseudomdp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);

	cout<<"phrase :"<<phrase<<endl;

	if(strcmp(phrase,"CREATOK")==0)
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
return 0;
}


int Reseau::connecterServeurAMdp()
{
	pseudomdp="";
	nomcompte="";
	mdp="";

	std::cout << "Entrez votre Pseudo : ";
		cin>>nomcompte;

	std::cout << "Entrez votre mot de passe : ";
		cin>>mdp;

	string tmp="CONNECT@"+nomcompte+"@"+mdp+"\n";

	pseudomdp = tmp.c_str();
		cout<<pseudomdp<<endl;

	if ((send(socketID, pseudomdp, strlen(pseudomdp), 0)) == 0)
		perror("send");
			cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);
		cout<<"phrase :"<<phrase<<endl;

if(strcmp(phrase,"CONNECTOK")==0)
{
	WSADATA wsaData;
	int res = WSAStartup(MAKEWORD(2, 0), &wsaData);

	informations.sin_family = AF_INET;
	informations.sin_port = htons(6667);
	informations.sin_addr.s_addr = inet_addr("88.167.230.145");

	socketID = socket(AF_INET, SOCK_STREAM, 0);

	if (socketID == INVALID_SOCKET) 
	{
		perror("socket");
	}

	if ((connect(socketID, (sockaddr*) &informations, sizeof(sockaddr_in))) != SOCKET_ERROR) 
	{
		perror("connect");
	}
return 0;
}			
else if((strcmp(phrase,"WPASS")==0))
{cout<<"mauvais password utilisé, connexion impossible"<<endl;}
else if((strcmp(phrase,"WPSEUDO")==0))
{cout<<"pseudo introuvable, connexion impossible"<<endl;}

}



int Reseau::getlistepartie()
{
		getliste="GETLISTEPARTIE\n";
		if ((send(socketID, getliste, strlen(getliste), 0)) == 0)
			perror("send");
		memset(phrase, 0, 255);
		recv(socketID, phrase, 255, 0);
		if(strcmp(phrase,"NCON")==0)
		{
			std::cout << " Il faut se connecter avant " << std::endl;
		}
		else
		{
			std::cout << "Nombre de parties : " << phrase << std::endl;
		}

		return 0;
}

int Reseau::rejoindreUnePartie()
{
	cout<<"Donnez le nom de la partie"<<endl;
	cin>>nompartie;

	string tmp="REJP@"+nompartie+"\n";

	rjp = tmp.c_str();

	cout<<rjp<<endl;

	if ((send(socketID, rjp, strlen(rjp), 0)) == 0)
		perror("send");
//	cout<<strlen(&rjp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);

	cout<<"phrase :"<<phrase<<endl;

	if(strcmp(phrase,"REJOK")==0)
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
return 0;
}



int Reseau::changerPseudo()
{
	pseudomdp="";
	ancienpseudo="";
	nouveaupseudo="";
	mdp="";
	
	std::cout << "Entrez votre ancien Pseudo : ";
	cin>>ancienpseudo;

	std::cout << "Entrez votre mot de passe : ";
	cin>>mdp;

	std::cout << "Entrez votre nouveau Pseudo : ";
	cin>>nouveaupseudo;

	string tmp="ACTPSEUDO@"+ancienpseudo+"@"+mdp+"@"+nouveaupseudo+"\n";

	pseudomdp = tmp.c_str();

	cout<<pseudomdp<<endl;

	if ((send(socketID, pseudomdp, strlen(pseudomdp), 0)) == 0)
		perror("send");
	cout<<sizeof(&pseudomdp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);

	cout<<"phrase :"<<phrase<<endl;

	if(strcmp(phrase,"OK")==0)
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
	else if(strcmp(phrase,"WFPSEUDO")==0)
	{
		std::cout << " mauvais format de pseudo " << std::endl;
	}
return 0;	
}

int Reseau::changerMdp()
{
	pseudomdp="";
	ancienmdp="";
	nouveaumdp="";
	pseudo="";
	
	std::cout << "Entrez votre ancien Pseudo : ";
	cin>>pseudo;

	std::cout << "Entrez votre mot de passe : ";
	cin>>ancienmdp;

	std::cout << "Entrez votre nouveau Pseudo : ";
	cin>>nouveaumdp;

	string tmp="ACTPASS@"+pseudo+"@"+ancienmdp+"@"+nouveaumdp+"\n";

	pseudomdp = tmp.c_str();

	cout<<pseudomdp<<endl;

	if ((send(socketID, pseudomdp, strlen(pseudomdp), 0)) == 0)
		perror("send");
	cout<<sizeof(&pseudomdp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);

	cout<<"phrase :"<<phrase<<endl;

	if(strcmp(phrase,"OK")==0)
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
return 0;
}

int Reseau::creerPartie()
{
cout<<"Donnez le nom de la partie"<<endl;
	cin>>nompartie;
cout<<"Donnez le nombre max de joueurs"<<endl;
	cin>>nbjoueursmax;


	string tmp="CREATEPARTIE@"+nompartie+"@"+nbjoueursmax+"\n";

	rjp = tmp.c_str();

	cout<<rjp<<endl;

	if ((send(socketID, rjp, strlen(rjp), 0)) == 0)
		perror("send");
//	cout<<strlen(&rjp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);

	cout<<"phrase :"<<phrase<<endl;

	if(strcmp(phrase,"CREATPOK")==0)
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
return 0;

}

int Reseau::getInfo()
{

	cout<<"Donnez le nom du joueur"<<endl;
	cin>>getinfo;

	string getinfo="GETINFO@"+pseudo+"\n";

	rjp = getinfo.c_str();

	cout<<rjp<<endl;

	if ((send(socketID, rjp, strlen(rjp), 0)) == 0)
		perror("send");
//	cout<<strlen(&rjp)<<endl;
	cout<<"phrase send"<<endl;

	memset(phrase, 0, 255);
	recv(socketID, phrase, 255, 0);
	std::cout << " C'est bon " << phrase << std::endl;
	
}

void Reseau::deconnecterServeur(void)
{
	closesocket(socketID);
	WSACleanup();

}
