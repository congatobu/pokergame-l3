function creer_partie(nom, nb){
	setaction("CREATEPARTIE");
	socket_send("CREATEPARTIE@"+nom.value+"@"+nb.value);
}

function lister_parties(){
	tout_cacher();
	setaction("GETLISTEPARTIE");
	socket_send("GETLISTEPARTIE");
}