var domaine="";
var action="";

function test(input){
	var tab = input.split(":");
	effform("test_reception");
	domaine = tab[0];
	action = tab[1];
	traiter_message(tab[2]);
}

function set_reception(dr, act){
	domaine = dr;
	action = act;
}

function traiter_message(message){
	//if(message=="ERROR") alert("Erreur de syntaxe");
	/*else*/ if(domaine=="EM_CMPT"){
		if(action=="CONNECT"){
			connect(message);
		}
		else if(action=="CREATCPT"){
			creatcpt(message);
		}
		else if(action=="ACTPASS"){
			actpass(message);
		}
		else if(action=="ACTPSEUDO"){
			actpseudo(message);
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
		else if(action=="GETINFO"){
			getinfo(message);
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
		
		if(act[0]=="DEBUTPARTIE") debutpartie(act);
			
		else if(act[0]=="LISTEJOUEURSPARTIE") lister_joueurs(act);
		
		else if(act[0]=="JETONJ") jetonj(act);
		
		else if(act[0]=="CARTEM") cartem(act);
		
		else if(act[0]=="CARTET") cartet(act);
		
		else if(act[0]=="JOUE") joue(act);
		
		else if(act[0]=="RELANCER") relancer(act);
		
		else if(act[0]=="JETONT") jetont(act);
		
		else if(act[0]=="MONTREC") montrec(act);
		
		else if(act[0]=="GAGNANTT") gagnantt(act);
		
		else if(act[0]=="GAGNANTP") gagnantp(act);
		
		else if(act[0]=="PERDU") perdu(act);
	}
}

//gestion de compte
function connect(message){
	if(message == 'CONNECTOK'){
		con();
	}
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, connexion impossible.");
		
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else if(message == 'PDC')
		alert("Vous êtes déjà connecté.");
		
	else alert("Serveur indisponnible. Veuillez réessayer ultérieurement.");
}

function creatcpt(message){
	if(message == 'CREATOK'){
		effform("formcreat");
		$("#formcreat").hide();
		$("#formcon").show();
	}
	else if(message == 'AUPSEUDO')
		alert("Pseudo déjà utilisé.");
	
	else if(message == 'ERREURBDD')
		alert("Erreur lors de la création de compte. Veuillez recommencer.");
		
	else if(message == 'WFPSEUDO')
		alert("Mauvais format de pseudo.");
		
	else if(message == 'WFPASS')
		alert("Mauvais format de mot de passe.");
	
	else
		alert("Serveur indisponible;");
}

function actpass(message){
	if(message == 'OK'){
		effform("chmtmdp");
		$("#chmtmdp").hide();
		alert("Votre mot de passe a bien été modifié.");
	}
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, changement impossible.");
	
	else if(message == 'WFPASS')
		alert("Mauvais format de mot de passe.");
}

function actpseudo(message){
	if(message == 'OK'){
		effform("chmtpsd");
		$("#chmtpsd").hide();
		alert("Votre pseudo a bien été modifié.");
	}
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, changement impossible.");
	
	else if(message == 'WFPSEUDO')
		alert("Mauvais format de pseudo.");
	
	else if(message == 'AUPSEUDO')
		alert("Pseudo déjà utilisé.");
}

//gestion des envois relatifs au partie
function createpartie(message){
	if(message == 'CREATPOK'){
		effform("creerpartie");
		$("#creerpartie").hide();
		entrer_partie();
		alert("Votre partie a bien été créée.");
	}
	else if(message == 'PAU')
		alert("Nom de partie deja utilisé.");
		
	else if(message == 'WFP')
		alert("Mauvais format nom de partie.");
	
	else if(message == 'NCON')
		alert("Vous devez être connecté pour créer une partie.");
}

function getlistepartie(message){
	if(message=="NCON")
		alert("Vous devez être connecté pour voir la liste des parties en cours");

	else affiche_liste(message);
}

function rejp(message){
	if(message == 'REJOK'){
		domaine = "PARTIE";
		$("#listeparties").hide();
		entrer_partie();
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

function getplayerparty(message){
	if(message=="NIP")
		alert("Le joueur n'est assit à aucune table");
	else if(message=="NCON")
		alert("Non connecté.");
	else lister_joueurs(message.split("@"));
}

function exitpartie(message){
	if(message=="ERROR")
		alert("Erreur lors de la tentative pour quitter la partie");
	else if(message=="EXITOK")
		sortir_partie();
}

function getinfo(message){
	if(message=="PI")
		alert("Joueur introuvable");
	else if(message=="NCON")
		alert("Connexion nécéssaire pour obtenir les infos sur le joueur.");
	else afficher_infos_joueur(message);
}

function debutpartie(message){
	if(message=="DEBUTPARTIE")
		demarrer_partie();
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

function choix(message){
	if(message=="JOK")
		alert("Choix effectué");
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

//gestion des réceptions relatives à une partie