#include "Reseau.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#include "StdAfx.h"

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

if ((connect(socketID, (struct sockaddr*) &informations, sizeof(struct sockaddr_in))) != SOCKET_ERROR) 
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
	
		
void Reseau::deconnecterServeur(void)
{
	shutdown(socketID, 2);
	WSACleanup();

}
