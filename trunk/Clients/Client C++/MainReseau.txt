#ifndef DEF_MAINRESEAU
#define DEF_MAINRESEAU

class MainReseau
{
	public:

	MainReseau();	//constructeur
	~MainReseau();	//destructeur
	int connecterServeur();
	void deconnecterServeur();
	int communiquerServeur();
	
	private:
	
	char phrase[255];
	struct sockaddr_in informations;
	SOCKET socketID;
	
};

#endif
