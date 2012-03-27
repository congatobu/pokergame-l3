
function creer_partie(nom, nb){
	set_nom_partie(nom);
	set_createur(true);
	set_reception("EM_PRT", "CREATEPARTIE");
	socket_send("CREATEPARTIE@"+nom.value+"@"+nb.value);
}

function lister_parties(){
	set_reception("EM_PRT", "GETLISTEPARTIE");
	socket_send("GETLISTEPARTIE");
	tout_cacher();
}

function rejoindre_partie(nom){
	set_nom_partie(nom);
	set_createur(false);
	set_reception("EM_PRT", "REJP");
	socket_send("REJP@"+nom);
}

function liste_joueurs(){
	set_reception("EM_PRT", "GETPLAYERPARTY");
	socket_send("GETPLAYERPARTY");
}

function quitter_partie(){
	set_reception("EM_PRT", "EXITPARTIE");
	socket_send("EXITPARTIE");
}

function infos_joueur(joueur){
	alert("infos de "+joueur);
	set_reception("EM_PRT", "GETINFO");
	socket_send("GETINFO@"+joueur);
}

function debuter_partie(){
	set_reception("EM_PRT", "DEBUTPARTIE");
	socket_send("DEBUTPARTIE");
}

function choix_joueur(numero, jetons){
	set_reception("EM_PRT", "CHOIX");
	socket_send("CHOIX@"+numero+"@"+jetons);
}