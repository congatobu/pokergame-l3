#pragma once

ref class Reseau
{
	public:

	Reseau(void);	//constructeur
	~Reseau(void);		//destructeur
	int connecterServeur();
	void deconnecterServeur();
	int communiquerServeur();
	
	private:
	
	char phrase[255];
	struct sockaddr_in informations;
	SOCKET socketID;
};
