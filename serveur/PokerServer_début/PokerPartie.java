
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/* -----------------------------------------------------------------
Classe du serveur qui va gérer les parties
----------------------------------------------------------------
*/
public class PokerPartie {
		
	private Vector clientList= new Vector();
	private PrintWriter screenOut = new PrintWriter(System.out, true);
	private int enCours = 0;
	private int maxPlayers = 8;
	private Semaphore available = new Semaphore(1, true);
	
	/*
	accesseurs
	*/
	public Vector getClientList(){return clientList;}
	public String getEtat(){ return clientList.size()+"/"+maxPlayers+" joueurs dans la partie";}
	
	/*
	Fonction pour envoyer un message à tous les clients de la partie
	*/
	public void broadcastClientsPartie(String m)	{
		int i;
		PokerClientThread foo;
		for (i=0;i<clientList.size();++i){
			foo = (PokerClientThread)clientList.get(i);
			foo.send(m);
										} 	
											}
	/*
	Fonction pour enlever un client de la partie
	*/
	public void deleteClient(PokerClientThread deadClient){
		try{
		available.acquire();
		int num;
		num=clientList.indexOf(deadClient);
		if(num!=-1){
		clientList.remove(num);
					}
						available.release();
		}catch(Exception e){e.printStackTrace();}
													}
	/*
	Fonction pour ajouter un client à la partie
	*/								
	public int addPlayer(PokerClientThread np){
	if(clientList.size()<maxPlayers){
	clientList.add(np);	return 0;}
	else return -1;
											}
											
}