import java.io.IOException;import java.io.PrintWriter;import java.net.ServerSocket;import java.util.Vector;import java.util.concurrent.Semaphore;/** * Classe du serveur qui va recevoir les demandes de connection * @author benjamin Maurin */public class PokerServer {			static Vector clientList= new Vector();	static Vector partiesList = new Vector();	static PrintWriter screenOut = new PrintWriter(System.out, true);	static ServerSocket serverSocket = null;	static Semaphore available = new Semaphore(1, true);	static Semaphore availableP = new Semaphore(1, true);	static Runtime rtim = Runtime.getRuntime();	        /**         * Fonction principale serveur qui écoute les demandes de connection         * @author benjamin Maurin         * @param args         * @throws IOException         */        public static void main(String[] args) throws IOException {	        boolean listening = true;        CommandReader tempCommand;        Thread tempThread;        PokerClientThread tempClient;        Integer tempPort;        screenOut.println("");screenOut.println("");        screenOut.println("POKER Server v0.1");        screenOut.println("------------------------------------------------------");        screenOut.println(" Notes:");        screenOut.println("Tapez 'HELP' pour voir les commandes");        screenOut.println("------------------------------------------------------");        screenOut.println("");                // on prend le port en argument ou 6667 par défault        if(args.length >= 1){            tempPort = new Integer(args[0]);        }else{            tempPort = 6667;        }        try{            serverSocket = new ServerSocket(tempPort.intValue());        }catch(IOException e){            System.err.println("Erreur de port "+tempPort);            System.exit(-1);        }        screenOut.println("Le serveur ecoute le port "+tempPort+"\n");        screenOut.print(">");        screenOut.flush();        tempCommand = new CommandReader();        tempCommand.start();        while (listening){            try{                tempClient = new PokerClientThread(serverSocket.accept());                clientList.add(tempClient);                tempThread = new Thread(tempClient);                tempThread.start();	                screenOut.println("Nouveau client");            }            catch (IOException e){                e.printStackTrace();	            }        }        serverSocket.close();    }    /**     * Fonction pour afficher toutes les parties en cours     * @author benjamin Maurin     */    public static void listParties(){        PokerPartie foo;        for (int i=0;i<partiesList.size();++i){            foo = (PokerPartie)partiesList.get(i);            screenOut.println(" Partie "+(i+1)+": "+foo.getEtat());        } 	    }    /**     * Fonction pour afficher tout les clients connectés     * @author benjamin Maurin     */    public static void listClients(){        PokerClientThread foo;        for (int i=0;i<clientList.size();++i){            foo = (PokerClientThread)clientList.get(i);            screenOut.println(" Client "+(i+1)+": "+foo.getClientIP());        } 	    }    /**     * Supprime proprement un client     * @author benjamin Maurin     * @param deadClient     */    public static void deleteClient(PokerClientThread deadClient){        try{            available.acquire();            int num;            num=clientList.indexOf(deadClient);            if(num!=-1){                deadClient = null;                rtim.gc(); // appel au garbage collector                clientList.remove(num);                screenOut.println("ya un client qui deco");                available.release();            }        }catch(Exception e){            e.printStackTrace();        }    }    /**     * Supprime proprement une partie     * @author benjamin Maurin     * @param deadPartie     */    public static void deletePartie(PokerPartie deadPartie){        try{            availableP.acquire();            int num;            num=partiesList.indexOf(deadPartie);            if(num!=-1){            deadPartie = null;            rtim.gc(); // appel au garbage collector            partiesList.remove(num);            screenOut.println("ya une partie qui est détruite");                            }            availableP.release();        }catch(Exception e){            e.printStackTrace();        }    }    /**     * Crée une partie     * @author benjamin Maurin     * @param premClient : le client qui crée la partie     * @param nomP : nom de la partie à créer     * @param maxP : nombre de joueurs maximum de la partie à créer     */    public static void creerPartie(PokerClientThread premClient,String nomP,int maxP){        int existe = 0;        PokerPartie p1;        for (int i=0;i<PokerServer.partiesList.size();++i){            p1 = (PokerPartie)PokerServer.partiesList.get(i);            if(p1.getNom().equals(nomP)){                existe=1;break;            }        }        if(existe==0){            p1 = new PokerPartie(nomP,maxP);            p1.getClientList().add(premClient);            partiesList.add(p1);        }else{            premClient.send("nom partie existe deja");        }    }    /**     * Retourne la liste des parties     * @author benjamin Maurin     * @return Retourne la liste des parties     */    public static String listClientParties(){        String s="<>";        PokerPartie foo;        for (int i=0;i<partiesList.size();++i){                foo = (PokerPartie)partiesList.get(i);                s+="<partie>"+foo.getNom()+"<joueurs>"+foo.getNbJ()+"/"+foo.getMaxPlayers()+"<>";        }         return s;    }    }