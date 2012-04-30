/**
 * @fileOverview Ce fichier contient les fonctions de gestion des messages re�us par le serveur;
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
 * Cette fonction est appel�e lors de l'�mission de message pour param�trer le domaine de reception des messages.
 * @author Paul	Mura
 * @param {String} dr Domaine de r�ception: compte, connect� ou partie.
 * @param {String} act L'action d'�mision �ffecut�e pr�c�dement.
 */
function set_reception(dr, act){
	domaine = dr;
	action = act;
}

/**
 * Appelle la fonction de message correspondant au domaine au message re�u. 
 * @author Paul	Mura
 * @param {String} message Le message re�u depus le serveur.
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

		if(act[0]=="DEBUTPARTIE"){ alert("D�but de la partie.");set_encours(true);}
			
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
 * G�re les messages re�us apr�s une demande de connexion. Notifie l'utilisateur en cas d'erreur, appelle la fonction con() sinon.
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
		alert("Vous �tes d�j� connect�.");
		
	else alert("Serveur indisponnible. Veuillez r�essayer ult�rieurement.");
}

/**
 * G�re les messages re�us apr�s une demande de cr�ation de compte. Notifie l'utilisateur en cas d'erreur, appelle la fonction creation_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function creatcpt(message){

	if(message == 'CREATOK'){
		creation_ok();
	}
	else if(message == 'AUPSEUDO'){
		resetcreation();
		$("#labpseudocreat").text("/!\\ pseudo d�j� utilis�");
		$("#pseudocreat").select();
	}
	else if(message == 'ERREURBDD')
		alert("Erreur lors de la cr�ation de compte. Veuillez recommencer.");
		
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
 * G�re les messages re�us apr�s une demande de changement de mot de passe. Notifie l'utilisateur en cas d'erreur, appelle la fonction chmt_pass_ok() sinon.
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
 * G�re les messages re�us apr�s une demande de changement de pseudo. Notifie l'utilisateur en cas d'erreur, appelle la fonction chmt_psd_ok() sinon.
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
		$("#labnouvpseudo").text("/!\\ pseudo d�j� utilis�.");
		$("#nouvpseudo").select();
	}
}


//GESTION DES PARTIES

/**
 * G�re les messages re�us apr�s une demande de cr�ation de partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction creation_partie_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function createpartie(message){
	if(message == 'CREATPOK'){
		creation_partie_ok();
	}
	else if(message == 'PAU'){
		resetcreerprt();
		$("#labnompartie").text("/!\\ nom d�j� utilis�.");
		$("#nompartie").select();
	}		
	else if(message == 'WFP'){
		resetcreerprt();
		$("#labnompartie").text("/!\\ mauvais format de nom.");
		$("#nompartie").select();
	}	
	else if(message == 'NCON')
		alert("Vous devez �tre connect� pour cr�er une partie.");
}

/**
 * G�re les messages re�us apr�s une demande de liste de parties. Notifie l'utilisateur en cas d'erreur, appelle la fonction affiche_liste(message) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getlistepartie(message){
	if(message=="NCON")
		alert("Vous devez �tre connect� pour voir la liste des parties en cours");

	else affiche_liste(message);
}

/**
 * G�re les messages re�us apr�s une demande pour rejoindre une partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction rej_ok() sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function rejp(message){
	if(message == 'REJOK'){
		rej_ok();
	}
	else if(message == 'PNE')
		alert("La partie n'�xiste pas.");
		
	else if(message == 'TOOMANY')
		alert("Impossible de rejoindre partie. Nombre de joueurs maximum atteint.");
	
	else if(message == 'AIP')
		alert("Vous �tes d�j� assis � une table.");
	
	else if(message == 'PEC')
		alert("La partie � d�j� commenc�.");
	
	else if(message == 'NCON')
		alert("Vous devez �tre connect� pour rejoindre une partie.");
}


//GESTION DE LA PARTIE EN COURS

/**
 * G�re les messages re�us apr�s la demande des joueurs de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction lister_joueurs(tableau) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getplayerparty(message){
	if(message=="NIP")
		alert("Le joueur n'est assit � aucune table");
	else if(message=="NCON")
		alert("Non connect�.");
	else lister_joueurs(message.split("@"));
}

/**
 * G�re les messages re�us apr�s une demande pour sortir de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction sortir_partie() sinon.
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
 * G�re les messages re�us apr�s une demande d'informations sur un joueur. Notifie l'utilisateur en cas d'erreur, appelle la fonction afficher_infos_joueur(message) sinon.
 * @author Paul	Mura
 * @param {String} message
 */
function getinfo(message){
	if(message=="PI")
		alert("Joueur introuvable");
	else if(message=="NCON")
		alert("Connexion n�c�ssaire pour obtenir les infos sur le joueur.");
	else afficher_infos_joueur(message);
}

/**
 * G�re les messages re�us apr�s avoir demander le d�but de la partie. Notifie l'utilisateur en cas d'erreur, appelle la fonction demmarer_partie() sinon.
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
		alert("Le client n'est pas cr�ateur de la partie.");
	else if(message=="PAJ")
		alert("Il n'y a pas assez de joueurs pour commencer la partie.");
	else if(message=="NIP")
		alert("Le client n'est pas dans une partie.");
	else if(message=="PEC")
		alert("La partie � d�j� commencer.");
	else if(message=="NCON")
		alert("Lancement de partie impossible, client non connect�.");
}

/**
 * G�re les messages re�us apr�s avoir envoyer le choix de l'utilisateur lors d'un tour de table. Notifie l'utilisateur en cas d'erreur.
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
		alert("Num�ro de choix d'action invalide.");
	else if(message=="NIP")
		alert("Le client doit �tre dans une partie pour faire un choix.");
	else if(message=="NCON")
		alert("Le client doit �tre connect� pour faire un choix.");
}

