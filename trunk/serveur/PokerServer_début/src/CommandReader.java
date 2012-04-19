package pokerPackage;






import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;



/**
 * Classe pour lire les commandes
 * @author Benjamin Maurin
 */
public class CommandReader extends Thread{
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter screenOut = new PrintWriter(System.out, true);
    String command;

    /**
     * Constructeur
     */
    public CommandReader(){
        super("CommandReader");
    }

    /**
     * Fonction qui lit et traite les commandes
     * @author Benjamin Maurin
     */
    @Override
    public void run(){
        boolean valid;
        try{
            while ((command = in.readLine()) != null){
                valid = false;
                command = command.trim().toUpperCase();
                if (command.compareTo("QUIT")==0){
                    valid = true;
                    PokerClientThread foo;
                    for (int i=0;i<PokerServer.clientList.size();++i){
                        foo= (PokerClientThread) PokerServer.clientList.get(i);
                        foo.deco();
                    }
                    PokerServer.serverSocket.close();
                    System.exit(0);
                }
                if (command.compareTo("LIST")==0){
                    valid = true;
                    screenOut.println("");
                    PokerServer.listClients();
                    screenOut.println("");
                }
                if (command.compareTo("LISTP")==0){
                    valid = true;
                    screenOut.println("");
                    PokerServer.listParties();
                    screenOut.println("");
                }
                if (command.startsWith("KILL ")){

                    try{
                        int i = Integer.parseInt(command.substring(5));
                        valid = true;
                        if(PokerServer.clientList.size()>=i)
                        {
                             PokerClientThread foo;
                           foo = (PokerClientThread) PokerServer.clientList.get(i-1);
                                    foo.deco();
                            screenOut.println("\n Client "+i+" deconnecte.");
                        }
                        else{screenOut.println("\n Ce numero de client n'existe pas.");}
                    }
                    catch(Exception e){
                    screenOut.println("\n Erreur syntaxe KILL.");
                    e.printStackTrace();
                    }
                }
                if (command.compareTo("HELP")==0){
                    valid = true;
                    screenOut.println("\n LIST   - liste des clients connectes");
                    screenOut.println(" LISTP  - liste des parties");     
                    screenOut.println(" LISTDB - liste des valeurs dans la BDD");
                    screenOut.println(" KILL i - deconnecte le client numero i");
                    screenOut.println(" HELP   - liste des commandes");
                    screenOut.println(" CREDIT - credits du projet");
                    screenOut.println(" QUIT   - deconnecte les clients et quitte le serveur \n");
                }
                       if (command.compareTo("LISTDB")==0){
                    valid = true;
                    screenOut.println(PokerServer.bd.listeClient());
                  
                }
                    if (command.compareTo("CREDIT")==0){
                    valid = true;
                    screenOut.println("\n Projet poker (2012) : \n");
                    screenOut.println("        **Serveur:**");  
                    screenOut.println("");
                    screenOut.println("          Maurin Benjamin"); 
                    screenOut.println("          Giner Steve");
                     screenOut.println("");
                    screenOut.println("        **Client android:**");
                     screenOut.println("");
                    screenOut.println("          Bonotte Jessy"); 
                    screenOut.println("          Polizzi Mathieu");
                     screenOut.println("");
                    screenOut.println("        **Client C++:**");
                     screenOut.println("");
                    screenOut.println("          Agret Clement"); 
                    screenOut.println("          Legoc Renaud");
                     screenOut.println("");
                    screenOut.println("        **Client JS:**");
                     screenOut.println("");
                    screenOut.println("          Mura Paul"); 
                    screenOut.println("          Lamsec Yohann");
                    screenOut.println("");
                }
                if (!valid){
                    screenOut.println("\nError: tapez 'HELP' pour voir les commandes supportees\n");
                }
                screenOut.print(">");
                screenOut.flush();
            }
        }
        catch (IOException e){
        e.printStackTrace();
        }

    }
};