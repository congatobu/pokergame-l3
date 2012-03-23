var domaine = "EM_PRT";

function creer_partie(nom, nb){
	set_nom_partie(nom);
	set_createur(true);
	set_reception(domaine, "CREATEPARTIE");
	socket_send("CREATEPARTIE@"+nom.value+"@"+nb.value);
}

function lister_parties(){
	tout_cacher("\'slow\'");
	set_reception(domaine, "GETLISTEPARTIE");
	socket_send("GETLISTEPARTIE");
}

function rejoindre_partie(nom){
	set_nom_partie(nom);
	set_createur(false);
	set_reception(domaine, "REJP");
	socket_send("REJP@"+nom);
}

function liste_joueurs(){
	set_reception(domaine, "GETPLAYERPARTY");
	socket_send("GETPLAYERPARTY");
}

function quitter_partie(){
	set_reception(domaine, "EXITPARTIE");
	socket_send("EXITPARTIE");
}

function infos_joueur(joueur){
	alert("infos de "+joueur);
	set_reception(domaine, "GETINFO");
	socket_send("GETINFO@"+joueur);
}

function debuter_partie(){
	set_reception(domaine, "DEBUTPARTIE");
	socket_send("DEBUTPARTIE");
}

function choix_joueur(numero, jetons){
	set_reception(domaine, "CHOIX");
	socket_send("CHOIX@"+numero+"@"+jetons);
}