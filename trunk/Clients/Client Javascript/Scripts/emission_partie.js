/**
 * @fileOverview Ce fichier contient les fonctions d'envoi des messages relatifs aux parties.
 * @author Paul	Mura
 */

/**
 * Envoi une demande de création de partie, après avoir vérifié la validité des variables.
 * @author Paul	Mura
 * @param {HTMLObject} nom Le nom de la partie.
 * @param {HTMLObject} nb Le nombre de joueurs maximal dans la partie.
 */
function creer_partie(nom, nb){
	resetcreerprt();
	var reg=new RegExp("^[2-8]{1}$","g");
	
	if (nom.value==''){
		$("#labnompartie").text("/!\\ entrez un nom.");
		nom.focus();
	}
   
	else if (nb.value==''){
		$("#labnbjoueurs").text("/!\\ entrez un nombre de joueurs.");
		nb.focus();
	}
	else if(!reg.test(nb.value)){
		$("#labnbjoueurs").text("/!\\ entrez un nombre entre 2 et 8.");
		nb.focus();
	}
	else{
		set_nom_partie(nom);
		set_createur(true);
		set_reception("EM_PRT", "CREATEPARTIE");
		socket_send("CREATEPARTIE@"+nom.value+"@"+nb.value);
	}

}

/**
 * Envoi une demande d'information sur les parties en cours.
 * @author Paul	Mura
 */
function lister_parties(){
	set_reception("EM_PRT", "GETLISTEPARTIE");
	socket_send("GETLISTEPARTIE");
	tout_cacher();
}

/**
 * Envoi une demande d'information sur les parties en cours.
 * @author Paul	Mura
 * @param {String} nom Le nom de la partie que l'utilisateur veut rejoindre.
 */
function rejoindre_partie(nom){
	set_nom_partie(nom);
	set_createur(false);
	set_reception("EM_PRT", "REJP");
	socket_send("REJP@"+nom);
}

/**
 * Pendant une partie, demande la liste des joueurs présents dans la partie.
 * @author Paul	Mura
 */
function liste_joueurs(){
	set_reception("EM_PRT", "GETPLAYERPARTY");
	socket_send("GETPLAYERPARTY");
}

/**
 * Pendant une partie, envoie une demande de sortie de partie.
 * @author Paul	Mura
 */
function quitter_partie(){
	set_reception("EM_PRT", "EXITPARTIE");
	socket_send("EXITPARTIE");
}

/**
 * Pendant une partie, envoie une demande d'information sur un joueur.
 * @author Paul	Mura
 * @param {String} joueur Le pseudo du joueur concerné.
 */
function infos_joueur(joueur){
	set_reception("PARTIE", "GETINFO");
	socket_send("GETINFO@"+joueur);
}

/**
 * Envoie une demande pour débuter la partie.
 * @author Paul	Mura
 */
function debuter_partie(){
	set_reception("EM_PRT", "DEBUTPARTIE");
	socket_send("DEBUTPARTIE");
}

/**
 * Envoie le choix de l'utilisateur concernant un tour de table
 * @author Paul	Mura
 * @param {String|Number}  numero 1, 2 ou 3: passer le tours, suivre ou relancer
 * @param {String|Number} jetons Le nombre de jetons misés.
 */
function choix_joueur(numero, jetons){
	console.log("jetons de relance:  "+jetons);
	set_reception("EM_PRT", "CHOIX");
	socket_send("CHOIX@"+numero+"@"+jetons);
}


/**
 * Envoie un message sur le chat.
 * @author Paul	Mura
 * @param {HTMLObject} mess Le message à faire suivre.
 */
function chatter(mess){
	if(mess.value!=''){
		socket_send("MESSAGE@"+get_pseudo()+"@"+mess.value);
		$('#mess').val("");
	}
}