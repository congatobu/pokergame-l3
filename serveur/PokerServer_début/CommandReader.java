
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


/**
 * Classe pour lire les commandes
 * @author Ben maurin
 */
class CommandReader extends Thread{
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
     * Fonction qui lit les commandes
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
                if (command.compareTo("HELP")==0){
                    valid = true;
                    screenOut.println("\n LIST  - liste des clients connectes");
                    screenOut.println(" LISTP - liste des parties");
                    screenOut.println(" HELP  - liste des commandes");
                    screenOut.println(" QUIT  - deconnecte les clients et quitte le serveur \n");
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