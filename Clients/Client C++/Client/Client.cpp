#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
//#pragma comment(lib, "ws2_32.lib")
#define PORT 6667

using namespace std;
int main()
{
/*WSADATA wsaData;
int res = WSAStartup(MAKEWORD(2, 0), &wsaData);*/
char phrase[255];
struct sockaddr_in informations;

SOCKET socketID;

informations.sin_family = AF_INET;
informations.sin_port = htons(6667);
informations.sin_addr.s_addr = inet_addr("88.167.230.145");

socketID = socket(AF_INET, SOCK_STREAM, 0);

if (socketID == INVALID_SOCKET) 
{
	perror("socket");
	exit (-1);
}

if ((connect(socketID, (struct sockaddr*) &informations, sizeof(struct sockaddr_in))) != SOCKET_ERROR) 
{
	perror("connect");
}

do
{
	cout << "Entrez votre phrase : ";
	fgets(phrase, 255, stdin);

	if ((send(socketID, phrase, strlen(phrase), 0)) == 0)
	perror("send");

	if (strcmp(phrase, "EXIT") != 0) 
	{
		memset(phrase, 0, 255);
		recv(socketID, phrase, 255, 0);
		cout << "Phrase re�ue : " << phrase << endl;
	}

} 
while (strcmp(phrase, "EXIT") != 0);

shutdown(socketID, 2);

return 0;
}