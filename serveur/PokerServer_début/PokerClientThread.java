
import java.net.*;
import java.io.*;
import java.util.*;



////  CLASSE THREAD  (chaque client a un thread)---------------------------------------------

//-------------------------------------------------------------
class PokerClientThread extends Thread {
	private PrintWriter screenOut = new PrintWriter(System.out, true);
	private Socket socket = null;
	private String clientIP;
	private BufferedWriter out;
	private BufferedReader in;
	private PokerPartie partie= null;
	private String pseudo = "";

	/*
	Constructeur
	*/
	public PokerClientThread(Socket socket){
		super("PokerClientThread");
		this.socket = socket;
	}

		/*
	Fonction générale reception messages
	*/
	public void run(){
		clientIP = socket.getInetAddress().getHostAddress();
				try{
				//new PrintWriter(socket.getOutputStream(), true);
			out =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			String inputLine;
			
			
				
				
				 try {
				  // lance le traitement du message reçu
   while ((inputLine = in.readLine()) != null) {
   
			traitements(inputLine);

      }
  } catch (Exception e) {
  
  screenOut.println("ya un bug dans un thread reception message "+e);
  e.printStackTrace();
        }
 
				
			deco();
			
		
		}catch(IOException e){
		screenOut.println("ya un bug dans un thread (bug général)");
		e.printStackTrace();
			deco();
		}	
			
	}
	
		/*
	Fonction pour traitements messages
	*/
	public void traitements(String inputLine){
	 
						try{
						//perroquet test
		screenOut.println("Socket recue : "+inputLine);
		send(clientIP+" : "+inputLine);
   	
						//Reception nom des clients
			if(inputLine.length()>=8 && inputLine.substring(0,8).equals("<pseudo>")){
							this.pseudo = inputLine.substring(8);
							screenOut.println("Pseudo enregistre : "+inputLine);
								}
								
									//Reception messages des clients
			if(inputLine.length()>=9 && inputLine.substring(0,9).equals("<message>")){
							screenOut.println("Message recu : "+inputLine);
								}	
	
						}
						catch(Exception e){
						 screenOut.println("ya un bug traitements message");
						 e.printStackTrace();
						 }
	}
	
	
	/*
	accesseurs
	*/
	public String getClientIP(){ return this.clientIP;}
	public String getPseudo(){ return this.pseudo;}
	
		/*
	Fonction pour déconnecter proprement
	*/
	public void deco(){
	try{
	in.close();
	out.close();
	socket.close();	
	if(partie!=null){partie.deleteClient(this);}
	PokerServer.deleteClient(this);
			}catch(IOException e){
		screenOut.println("ya un bug dans un thread deco"+e);
		e.printStackTrace();
		}	
			
	}

		/*
		Fonction pour libérer la mémoire
		*/				
	protected void finalize() throws Throwable{
	
	 try {
	 socket = null;
	 clientIP=null;
	 out = null;
	 in = null;
	 partie= null;
	 pseudo = null;
      screenOut.close();
       
    } catch(Exception e) {e.printStackTrace();}
    finally {
		  super.finalize();
    }
	 
	}
	
	/*
	Fonction pour rejoindre une partie
	*/
	public void rejoindrePartie(String nomP)
	{	int existe = 0;
		PokerPartie foo = null;
		for (int i=0;i<PokerServer.partiesList.size();++i){
			foo = (PokerPartie)PokerServer.partiesList.get(i);
			if(foo.getNom().equals(nomP)){existe=1;break;}
		}
		
		if(existe==1){
		if(foo.addPlayer(this)==-1){existe = 0;}else{partie=foo;}
		}
		if(existe==0){send("probleme partie trop de joueurs ou partie inconnue");
			}
	}
	
	
	/*
	Fonction pour envoyer un message au client
	*/
		public void send(String message){
		try{
		out.write(message+"\n");
		out.flush();
		 screenOut.println("Message envoye : "+message);
		 }
		 catch(Exception e){
		 screenOut.println("ya un bug envoi message");
		 e.printStackTrace();
		 }
		
	}
}