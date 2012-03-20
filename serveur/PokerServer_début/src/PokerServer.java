import java.io.IOException;import java.io.PrintWriter;import java.net.ServerSocket;import java.util.Vector;import java.util.concurrent.Semaphore;/** * Classe du serveur qui va recevoir les demandes de connection * @author benjamin Maurin */public class PokerServer {			static Vector clientList= new Vector();	static Vector partiesList = new Vector();	static PrintWriter screenOut = new PrintWriter(System.out, true);	static ServerSocket serverSocket = null;	static Semaphore available = new Semaphore(1, true);	static Semaphore availableP = new Semaphore(1, true);	static Runtime rtim = Runtime.getRuntime();	static ClientBDD bd = new ClientBDD();        /**         * Fonction principale serveur qui écoute les demandes de connection         * @author benjamin Maurin         * @param args         * @throws IOException         */        public static void main(String[] args) throws IOException {	        boolean listening = true;        CommandReader tempCommand;        Thread tempThread;        PokerClientThread tempClient;        Integer tempPort;        screenOut.println("");screenOut.println("");        screenOut.println("POKER Server v0.1");        screenOut.println("------------------------------------------------------");        screenOut.println(" Notes:");        screenOut.println("Tapez 'HELP' pour voir les commandes");        screenOut.println("------------------------------------------------------");        screenOut.println("");                // on prend le port en argument ou 6667 par défault        if(args.length >= 1){            tempPort = new Integer(args[0]);        }else{            tempPort = 6667;        }        try{            serverSocket = new ServerSocket(tempPort.intValue());        }catch(IOException e){            System.err.println("Erreur de port "+tempPort);            System.exit(-1);        }        screenOut.println("Le serveur ecoute le port "+tempPort+"\n");        screenOut.print(">");        screenOut.flush();        tempCommand = new CommandReader();        tempCommand.start();        try{        bd.Ouverture();        }        catch(Exception e){        screenOut.println("Erreur ouverture base de données à BDD\\Client.xml\n");        listening = false;        }                while (listening){            try{                tempClient = new PokerClientThread(serverSocket.accept());                clientList.add(tempClient);                tempThread = new Thread(tempClient);                tempThread.start();	                screenOut.println("Nouveau client");            }            catch (IOException e){                e.printStackTrace();	            }        }        serverSocket.close();    }    /**     * Fonction pour afficher toutes les parties en cours     * @author benjamin Maurin     */    public static void listParties(){        PokerPartie foo;        for (int i=0;i<partiesList.size();++i){            foo = (PokerPartie)partiesList.get(i);            screenOut.println(" Partie "+(i+1)+": "+foo.getEtat());        } 	    }    /**     * Fonction pour afficher tout les clients connectés     * @author benjamin Maurin     */    public static void listClients(){        PokerClientThread foo;        for (int i=0;i<clientList.size();++i){            foo = (PokerClientThread)clientList.get(i);            screenOut.println(" Client "+(i+1)+": "+foo.getClientIP());        } 	    }    /**     * Supprime proprement un client     * @author benjamin Maurin     * @param deadClient     */    public static void deleteClient(PokerClientThread deadClient){        try{            available.acquire();            int num;            num=clientList.indexOf(deadClient);            if(num!=-1){                deadClient = null;                rtim.gc(); // appel au garbage collector                clientList.remove(num);                screenOut.println("ya un client qui deco");            }        }catch(Exception e){            e.printStackTrace();        }            finally {             available.release();        }    }    /**     * Supprime proprement une partie     * @author benjamin Maurin     * @param deadPartie     */    public static void deletePartie(PokerPartie deadPartie){        try{            availableP.acquire();            int num;            num=partiesList.indexOf(deadPartie);            if(num!=-1){            deadPartie = null;            rtim.gc(); // appel au garbage collector            partiesList.remove(num);            screenOut.println("Une partie est detruite");                            }                  }catch(Exception e){            e.printStackTrace();        }           finally {             availableP.release();        }    }            /**     * rejoint une partie     * @author benjamin Maurin     * @param client : le client qui tente de rejoindre la partie     * @param nomP : nom de la partie à rejoindre     * @return PNE : partie n'existe pas     * @return PEC : partie deja en cours     * @return TOOMANY : trop de joueurs dans la partie     * @return REJOK: le joueur a bien rejoint la partie     */    public static String rejoindrePartie(PokerClientThread client,String nomP){         int existe = 0;     PokerPartie p1 = null;       try{            availableP.acquire();              for (int i=0;i<PokerServer.partiesList.size();++i){            p1 = (PokerPartie)PokerServer.partiesList.get(i);            if(p1.getNom().equals(nomP)){                existe=1;break;            }                }                      }catch(Exception e){            e.printStackTrace();        }                   if(existe==0){                  availableP.release();        return "PNE";            }            else{                if(p1.getEnCours()==1)                {                     return "PEC";                }                                                if(p1.addPlayer(client)==-1){                      availableP.release();         return "TOOMANY";}                else{                      availableP.release();                    return "REJOK";                }                    }    }           /**     * Fonction permettant de vérifier qu'une partie est dans un bon format.<br>     * @author Benjamin Maurin     * @param s la partie à vérifier     * @return {@code boolean} retourne vrai si le format est correct     */    private static boolean verifFormatP(String s){	String bonnes_lettres = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789éèàâêîôûäëïöüù _-()'";        String A_Test = s;        if(s.length()<3 || s.length()>15)        {            return false;        }	while(A_Test.length()>0)	{		if(bonnes_lettres.indexOf(A_Test.substring(0,1))==-1)		{			return false;		}		A_Test=A_Test.substring(1);	}		return true;}                    /**     * Crée une partie     * @author benjamin Maurin     * @param premClient : le client qui crée la partie     * @param nomP : nom de la partie à créer     * @param maxP : nombre de joueurs maximum de la partie à créer     * @return CREATPOK : la partie s'est bien crée     * @return PAU : nom de partie deja utilisé     * @return WFP : nom de partie au mauvais format     */    public static String creerPartie(PokerClientThread premClient,String nomP,int maxP){        int existe = 0;        PokerPartie p1;         if(!verifFormatP(nomP)){return "WFP";}  try{            availableP.acquire();        for (int i=0;i<PokerServer.partiesList.size();++i){            p1 = (PokerPartie)PokerServer.partiesList.get(i);            if(p1.getNom().equals(nomP)){                existe=1;break;            }        }             }catch(Exception e){            e.printStackTrace();        }                if(existe==0){            if(maxP>8)maxP=8;            p1 = new PokerPartie(nomP,maxP,premClient);            partiesList.add(p1);              availableP.release();            return "CREATPOK";        }else{              availableP.release();            return "PAU";        }    }    /**     * Retourne la liste des parties qui ne sont pas en cours     * @author benjamin Maurin     * @return Retourne la liste des parties     */    public static String listClientParties(){        String s="LISTEPARTIE";        PokerPartie foo;        for (int i=0;i<partiesList.size();++i){                foo = (PokerPartie)partiesList.get(i);                if(foo.getEnCours()==0)                s+="@"+foo.getNom()+"/"+foo.getNbJ()+"/"+foo.getMaxPlayers();        }         return s;    }          /**     * Retourne des infos sur un joueur     * @author benjamin Maurin     * @param le pseudo du joueur duquel on veut les informations     * @return des informations sur un joueur     */    public static String getInfoJoueur(String pseudo){        String s="SETINFO@";        String[] i =bd.getInfo(pseudo);        if(!i[0].equals("0")){       s+=pseudo+"@"+i[2]+"@"+i[3]+"@"+i[1];        }        else s ="PI";             return s;    }  }