/**
 * @fileOverview Ce fichier contient les fonctions de gestion des messages reçus par le serveur;
 * @author Paul	Mura
 */

var domaine="";
var action="";

function test(input){
	effform("test_reception");
	traiter_message(input);
}
function test3(input2){
	tab = input2.split(":");
	domaine=tab[0];
	action=tab[1];
}

/**
 * Cette fonction est appelée lors de l'émission de message pour paramétrer le domaine de reception des messages.
 * @author Paul	Mura
 * @param {String} dr Domaine de réception: compte, connecté ou partie.
 * @param {String} act L'action d'émision éffecutée précédement.
 */
function set_reception(dr, act){
	domaine = dr;
	action = act;
}

/**
 * Appelle la fonction de message correspondant au domaine au message reçu. 
 * @author Paul	Mura
 * @param {String} message Le message reçu depus le serveur.
 */
function traiter_message(message){
	console.log("RECEPTION :   "+message+"  "+domaine+" "+action);
	if(message=="ERROR") alert("Erreur de syntaxe");
	else if(domaine=="EM_CMPT"){
		if(action=="GETINFO"){
			getinfo(message);
		}
		else if(action=="CONNECT"){
			connect(message);
		}
		else if(action=="ACTPASS"){
			actpass(message);
		}
		else if(action=="ACTPSEUDO"){
			actpseudo(message);
		}
		else if(action=="CREATCPT"){
			creatcpt(message);
		}
	}
	else if(domaine=="EM_PRT"){
		if(action=="CREATEPARTIE"){
			createpartie(message);
		}
		else if(action=="GETLISTEPARTIE"){
			getlistepartie(message);
		}
		else if(action=="REJP"){
			rejp(message);
		}
		else if(action=="GETPLAYERPARTY"){
			getplayerparty(message);
		}
		else if(action=="EXITPARTIE"){	
			exitpartie(message);
		}
		else if(action=="DEBUTPARTIE"){
			debutpartie(message);
		}
		else if(action=="CHOIX"){
			choix(message);
		}
	}
	else if(domaine=="PARTIE"){
		var act = message.split("@");

		if(act[0]=="DEBUTPARTIE"){ alert("Début de la partie.");set_encours(true);}
			
		else if(act[0]=="LISTEJOUEURSPARTIE"){lister_joueurs(act);}
		
		else if(act[0]=="JETONJ") jetonj(act);
		
		else if(act[0]=="CARTEM") cartem(act);
		
		else if(act[0]=="CARTET") cartet(act);
		
		else if(act[0]=="JOUE") joue(act);
		
		else if(act[0]=="JETONT") jetont(act);
		
		else if(act[0]=="MONTREC") montrec(act);
		
		else if(act[0]=="GAGNANTT") gagnantt(act);
		
		else if(act[0]=="GAGNANTP") gagnantt(act);
		
		else if(act[0]=="PERDU") ;
		
		else if(act[0]=="AREUREADY"){socket_send("IAMREADY");}
		
		else if(act[0]=="JCHOIX") jchoix(act);
		
		else if(act[0]=="JOUEURJ") joueurj(act);
		
		else if(act[0]=="SETINFO") getinfo(message);
		
		else if(act[0]=="MESSAGE"){
			$("#prompt").append("\n  "+act[1]+" dit : "+act[2]);
			$("#prompt").scrollTop($('#prompt').offset().top);
		}
	}
}


//GESTION DE COMPTE

/**
 * Gère les messages reçus après une demande de connexion. Notifie l'utilisateur en cas d'erreur, appelle la fonction con() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function connect(message){
	if(message == 'CONNECTOK'){
		con();
	}
	else if(message == 'WPSEUDO'){
		resetcon();
		$("#labpseudocon").text("/!\\ pseudo inexistant");
		$("#pseudocon").select();
	}
	else if(message == 'WPASS'){
		resetcon();
		$("#labpasscon").text("/!\\ mot de passe incorrect");
		$("#passcon").select();
	}
	else if(message == 'PDC')
		alert("Vous êtes déjà connecté.");
		
	else alert("Serveur indisponnible. Veuillez réessayer ultérieurement.");
}

/**
 * Gère les messages reçus après une demande de création de compte. Notifie l'utilisateur en cas d'erreur, appelle la fonction creation_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function creatcpt(message){

	if(message == 'CREATOK'){
		creation_ok();
	}
	else if(message == 'AUPSEUDO'){
		resetcreation();
		$("#labpseudocreat").text("/!\\ pseudo déjà utilisé");
		$("#pseudocreat").select();
	}
	else if(message == 'ERREURBDD')
		alert("Erreur lors de la création de compte. Veuillez recommencer.");
		
	else if(message == 'WFPSEUDO'){
		resetcreation();
		$("#labpseudocreat").text("/!\\ mauvais format de pseudo");
		$("#pseudocreat").select();
	}
		
	else if(message == 'WFPASS'){
		resetcreation();
		$("#labpasscreat").text("/!\\ mauvais format de mot de passe.");
		$("#labpass2creat").text("/!\\");
		$("#passcreat").select();
	}
	
	else
		alert("Serveur indisponible;");
}

/**
 * Gère les messages reçus après une demande de changement de mot de passe. Notifie l'utilisateur en cas d'erreur, appelle la fonction chmt_pass_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function actpass(message){
	if(message == 'OK'){
		chmt_pass_ok();
	}
	else if(message == 'WPASS'){
		resetmdp();
		$("#labancienmdp").text("/!\\ mauvais mot de passe.");
		$("#ancienmdp").select();
	}	
	else if(message == 'WPSEUDO'){
		resetmdp();
		$("#labpseudochmt2").text("/!\\ pseudo introuvable.");
		$("#pseudochmt2").select();
	}
	else if(message == 'WFPASS'){
		resetmdp();
		$("#labnouveaumdp").text("/!\\ mauvais format de mot de passe.");
		$("#labconfirm").text("/!\\");
		$("#nouveaumdp").select();
	}
}

/**
 * Gère les messages reçus après une demande de changement de pseudo. Notifie l'utilisateur en cas d'erreur, appelle la fonction chmt_psd_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function actpseudo(message){
	if(message == 'OK'){
		chmt_psd_ok();
	}
	else if(message == 'WPASS'){
		resetpseudo();
		$("#labpasschmtpsd").text("/!\\ mauvais mot de passe.");
		$("#passchmtpsd").select();
	}	
		
	else if(message == 'WPSEUDO'){
		resetpseudo();
		$("#labpseudochmt").text("/!\\ pseudo introuvable.");
		$("#pseudochmt").select();
	}
	
	else if(message == 'WFPSEUDO'){
		resetpseudo();
		$("#labnouvpseudo").text("/!\\ mauvais format de pseudo.");
		$("#nouvpseudo").select();
	}
	
	else if(message == 'AUPSEUDO'){
		resetpseudo();
		$("#labnouvpseudo").text("/!\\ pseudo déjà utilisé.");
		$("#nouvpseudo").select();
	}
}


//GESTION DES PARTIES

/**
 * Gère les messages reçus après une demande de création de partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction creation_partie_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function createpartie(message){
	if(message == 'CREATPOK'){
		creation_partie_ok();
	}
	else if(message == 'PAU'){
		resetcreerprt();
		$("#labnompartie").text("/!\\ nom déjà utilisé.");
		$("#nompartie").select();
	}		
	else if(message == 'WFP'){
		resetcreerprt();
		$("#labnompartie").text("/!\\ mauvais format de nom.");
		$("#nompartie").select();
	}	
	else if(message == 'NCON')
		alert("Vous devez être connecté pour créer une partie.");
}

/**
 * Gère les messages reçus après une demande de liste de parties. Notifie l'utilisateur en cas d'erreur, appelle la fonction affiche_liste(message) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getlistepartie(message){
	if(message=="NCON")
		alert("Vous devez être connecté pour voir la liste des parties en cours");

	else affiche_liste(message);
}

/**
 * Gère les messages reçus après une demande pour rejoindre une partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction rej_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function rejp(message){
	if(message == 'REJOK'){
		rej_ok();
	}
	else if(message == 'PNE')
		alert("La partie n'éxiste pas.");
		
	else if(message == 'TOOMANY')
		alert("Impossible de rejoindre partie. Nombre de joueurs maximum atteint.");
	
	else if(message == 'AIP')
		alert("Vous êtes déjà assis à une table.");
	
	else if(message == 'PEC')
		alert("La partie à déjà commencé.");
	
	else if(message == 'NCON')
		alert("Vous devez être connecté pour rejoindre une partie.");
}


//GESTION DE LA PARTIE EN COURS

/**
 * Gère les messages reçus après la demande des joueurs de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction lister_joueurs(tableau) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getplayerparty(message){
	if(message=="NIP")
		alert("Le joueur n'est assit à aucune table");
	else if(message=="NCON")
		alert("Non connecté.");
	else lister_joueurs(message.split("@"));
}

/**
 * Gère les messages reçus après une demande pour sortir de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction sortir_partie() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function exitpartie(message){
	if(message=="ERROR")
		alert("Erreur lors de la tentative pour quitter la partie");
	else if(message=="EXITOK")
		sortir_partie();
}

/**
 * Gère les messages reçus après une demande d'informations sur un joueur. Notifie l'utilisateur en cas d'erreur, appelle la fonction afficher_infos_joueur(message) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getinfo(message){
	if(message=="PI")
		alert("Joueur introuvable");
	else if(message=="NCON")
		alert("Connexion nécéssaire pour obtenir les infos sur le joueur.");
	else afficher_infos_joueur(message);
}

/**
 * Gère les messages reçus après avoir demander le début de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction demmarer_partie() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function debutpartie(message){
	if(message=="AREUREADY"){
		domaine = "PARTIE";
		socket_send("IAMREADY");
		demmarer_partie();
	}
	else if(message=="NC")
		alert("Le client n'est pas créateur de la partie.");
	else if(message=="PAJ")
		alert("Il n'y a pas assez de joueurs pour commencer la partie.");
	else if(message=="NIP")
		alert("Le client n'est pas dans une partie.");
	else if(message=="PEC")
		alert("La partie à déjà commencer.");
	else if(message=="NCON")
		alert("Lancement de partie impossible, client non connecté.");
}

/**
 * Gère les messages reçus après avoir envoyer le choix de l'utilisateur lors d'un tour de table. Notifie l'utilisateur en cas d'erreur.
 * @author Paul	Mura
 * @param {String} message
 */
function choix(message){
	if(message=="JOK")
		domaine = "PARTIE";
	else if(message=="PAT")
		alert("Ce n'est pas au client de jouer.");
	else if(message=="MJET")
		alert("Mise non valide.");
	else if(message=="MNC")
		alert("Numéro de choix d'action invalide.");
	else if(message=="NIP")
		alert("Le client doit être dans une partie pour faire un choix.");
	else if(message=="NCON")
		alert("Le client doit être connecté pour faire un choix.");
}

