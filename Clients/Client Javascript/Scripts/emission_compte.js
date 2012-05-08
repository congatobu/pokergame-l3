/**
 * @fileOverview Ce fichier contient les fonctions d'envoi des messages relatifs à la gestion de compte.
 * @author Paul	Mura
 */

var pseudoC = "";

/**
 * Connexion au serveur via une socket.
 * @author Paul	Mura
 */
function connexion_serveur(){
	socket_connect('86.209.234.202', 6667);
}

/**
 * Deconnexion de la socket
 * @author Paul	Mura
 */
function deconnexion(){
	socket_disconnect();
	decon();
}

/**
 * Envoi une demande de changement de pseudo au serveur, après avoir vérifié la validité des variables.
 * @author Paul	Mura
 * @param {HTMLObject} pseudo L'ancien pseudo de l'utilisateur.
 * @param {HTMLObject} nouveau Le nouveau pseudo de l'utilisateur.
 * @param {HTMLObject} pass Le mot de passe de l'utilisateur.
 */
function changer_psd(pseudo, nouveau, pass){
	resetpseudo();
	if (pseudo.value==''){
		$("#labpseudochmt").text("/!\\ entrez votre pseudo.");
		nouveau.focus();
	}
	else if (nouveau.value==''){
		$("#labnouvpseudo").text("/!\\ entrez le nouveau pseudo.");
		nouveau.focus();
	}
	else if (pass.value==''){
		$("#labpasschmtpsd").text("/!\\ entrez votre mot de passe.");
		pass.focus();
	}
	else{
		set_reception("EM_CMPT", "ACTPSEUDO");
		connexion_serveur();
		socket_send("ACTPSEUDO@"+pseudo.value+"@"+pass.value+"@"+nouveau.value);
	}
}

/**
 * Envoi une demande de changement de mot de passe au serveur, après avoir vérifié la validité des variables.
 * @author Paul	Mura
 * @param {HTMLObject} pseudo Le pseudo de l'utilisateur.
 * @param {HTMLObject} ancien Son ancien mot de passe.
 * @param {HTMLObject} nouveau Son nouveau mot de passe.
 * @param {HTMLObject} nouveau2 La confirmation du mot de passe.
 */
function changer_mdp(pseudo, ancien, nouveau, nouveau2){
	resetmdp();
	if (pseudo.value==''){
		$("#labpseudochmt2").text("/!\\ entrez votre pseudo.");
		pseudo.focus();
	}
	
	else if (ancien.value==''){
		$("#labancienmdp").text("/!\\ ancien mot de passe.");
		ancien.focus();
	}
   
	else if (nouveau.value==''){
		$("#labnouveaumdp").text("/!\\ entrez votre nouveau mot de passe.");
		nouveau.focus();
	}
	
	else if (nouveau2.value==''){
		$("#labconfirm").text("/!\\ confirmez mot de passe.");
		nouveau2.focus();
	}

	else if (nouveau.value!=nouveau2.value){
		$("#labnouveaumdp").text("/!\\ les mots de passe diffèrent.");
		$("#labconfirm").text("/!\\");
		nouveau.select();
	}

	else{
		set_reception("EM_CMPT", "ACTPASS");
		connexion_serveur();
		socket_send("ACTPASS@"+pseudo.value+"@"+ancien.value+"@"+nouveau.value);
	}
}

/**
 * Envoi une demande de création de compte, après avoir vérifié la validité des variables.
 * @author Paul	Mura
 * @param {HTMLObject} psd Le pseudo de l'utilisateur.
 * @param {HTMLObject} pass Son mot de passe.
 * @param {HTMLObject} pass2 La confirmation du mot de passe.
 */
function creation(psd, pass, pass2){
	resetcreation();
	if (psd.value==''){
		$("#labpseudocreat").text("/!\\ entrez votre pseudo.");
		psd.focus();
	}
   
	else if (pass.value==''){
		$("#labpasscreat").text("/!\\ entrez votre mot de passe.");
		pass.focus();
	}
	
	else if (pass2.value==''){
		$("#labpass2creat").text("/!\\ confirmez mot de passe.");
		pass2.focus();
	}

	else if (pass.value!=pass2.value){
		$("#labpasscreat").text("/!\\ les mots de passe diffèrent.");
		$("#labpass2creat").text("/!\\");
		pass.select();
	}

	else{
		set_reception("EM_CMPT", "CREATCPT");
		pseudo = psd.value;
		connexion_serveur();
		socket_send("CREATCPT@"+psd.value+"@"+pass.value);
	}
}

/**
 * Envoi une demande de connexion, après avoir vérifié la validité des variables.
 * @author Paul	Mura
 * @param {HTMLObject} psd Le pseudo de l'utilisateur.
 * @param {HTMLObject} pass Son mot de passe.
 */
function connexion(psd, pass){
resetcon();
	if (psd.value==''){
		$("#labpseudocon").text("/!\\ entrez votre pseudo.");
		psd.focus();
	}
   
	else if (pass.value==''){
		$("#labpasscon").text("/!\\ entrez votre mot de passe.");
		pass.focus();
	}
	else{
		setpseudo(psd.value);
		pseudoC = psd.value;
		set_reception("EM_CMPT", "CONNECT");
		connexion_serveur();
		socket_send("CONNECT@"+psd.value+"@"+pass.value);
	}
}

/**
 * Envoi une demande d'informations sur le compte connecté.
 * @author Paul	Mura
 */
function infosperonnelles(){
	set_reception("EM_CMPT", "GETINFO");
	socket_send("GETINFO@"+pseudoC);
}