
import java.net.*;
import java.io.*;
import java.util.*;



////  CLASSE THREAD  (chaque client a un thread)---------------------------------------------

//-------------------------------------------------------------
class PokerClientThread extends Thread {
	PrintWriter screenOut = new PrintWriter(System.out, true);
	private Socket socket = null;
	public String clientIP;
	public PrintWriter out;
	public BufferedReader in;
	public int partieClient;
	public String pseudo = "";



	public PokerClientThread(Socket socket){
		super("PokerClientThread");
		this.socket = socket;
	}

	
	public void run(){
		clientIP = socket.getInetAddress().getHostAddress();
				try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			String inputLine;
			int streamResult;
			char buf[] = new char[1];
			do{
		
				inputLine = "";
				do{
				
					streamResult = in.read(buf, 0, 1);
					inputLine += buf[0];
				} while (buf[0] != '\u0000');
				
					
					
					
					
					
				if(inputLine.length()!=1){
				/*
				C'est ici qu'on va traiter les messages reçus
				*/
				
			
				
						//Reception nom des clients
			if(inputLine.length()>=5 && inputLine.substring(0,5).equals("<nom>")){
							this.pseudo = inputLine.substring(5);
								}
				
				
				
				
				
				
				
					 }
				
				
			} while (streamResult != -1);
			
			
		
			
		PokerServer.deleteClient(this);
			in.close();
			out.close();
			socket.close();
		}catch(IOException e){
		screenOut.println("ya un bug dans un thread ");
		e.printStackTrace();
		}	
			
	}
	
	public void send(String message){
		out.println(message);
		
	}
}