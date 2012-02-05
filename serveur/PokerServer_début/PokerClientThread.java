
import java.net.*;
import java.io.*;
import java.util.*;



////  CLASSE THREAD  (chaque client a un thread)---------------------------------------------

//-------------------------------------------------------------
class PokerClientThread extends Thread {
	PrintWriter screenOut = new PrintWriter(System.out, true);
	private Socket socket = null;
	public String clientIP;
	public BufferedWriter out;
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
				//new PrintWriter(socket.getOutputStream(), true);
			out =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			String inputLine;
			int streamResult;
			
				
				
				 try {
   while ((inputLine = in.readLine()) != null) {

   
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
  } catch (Exception e) {
  
  screenOut.println("ya un bug dans un thread reception message ");
        }
 
				

			
		PokerServer.deleteClient(this);
			in.close();
			out.close();
			socket.close();
		}catch(IOException e){
		screenOut.println("ya un bug dans un thread (bug général)");
		e.printStackTrace();
		}	
			
	}
	
	public void send(String message){
		//out.println(message);
		try{
		out.write(message+"\n");
		out.flush();
		 screenOut.println("Message envoye : "+message);
		 }
		 catch(Exception e){
		 screenOut.println("ya un bug envoi message");
		 }
		
	}
}