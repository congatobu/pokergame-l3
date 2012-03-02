#ifndef MAINRESEAU_H
#define MAINRESEAU_H

class MainReseau
{
public:
    MainReseau();
    ~MainReseau();	//destructeur
    int connecterServeur();
    void deconnecterServeur();
    int communiquerServeur();
    //int creerCompte();

private:

    char phrase[255];
    struct sockaddr_in informations;
    SOCKET socketID;
};

#endif // MAINRESEAU_H
