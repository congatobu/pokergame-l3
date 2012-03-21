
var pseudo="";
var pseudo2="";

function connexion_serveur(){
	socket_connect('88.167.230.145', 6667);
}

function deconnexion(){
	socket_disconnect();
	decon();
}

function changer_psd(nouveau, pass){
	if (nouveau.value==''){
		alert("Veuillez entrer votre nouveau pseudo dans le premier champ.")
		nouveau.focus()
	}
	else if (pass.value==''){
		alert("Veuillez entrer votre mot de passe dans le deuxième champ.")
		pass.focus()
	}
	else{
		setaction("ACTPSEUDO");
		connexion_serveur();
		socket_send("ACTPSEUDO@"+pseudo+"@"+pass.value+"@"+nouveau.value);
		pseudo2 = nouveau.value;
	}
}

function changer_mdp(ancien, nouveau, nouveau2){
	if (ancien.value==''){
		alert("Veuillez entrer votre ancien mot de passe dans le premier champ.")
		ancien.focus()
	}
   
	else if (nouveau.value==''){
		alert("Veuillez entrer votre nouveau mot de passe dans le second champ.")
		pass.focus()
	}
	
	else if (nouveau2.value==''){
		alert("Veuillez confirmer votre mot de passe dans le troisième champ.")
		pass2.focus()
	}

	else if (nouveau.value!=nouveau2.value){
		alert("Les deux mots de passe ne condordent pas.")
		pass.select()
	}

	else{
		setaction("ACTPASS");
		connexion_serveur();
		socket_send("ACTPASS@"+pseudo+"@"+ancien.value+"@"+nouveau.value);
	}
}

function creation(psd, pass, pass2){

	if (psd.value==''){
		alert("Veuillez entrer votre pseudo dans le premier champ.")
		psd.focus()
	}
   
	else if (pass.value==''){
		alert("Veuillez entrer votre mot de passe dans le second champ.")
		pass.focus()
	}
	
	else if (pass2.value==''){
		alert("Veuillez confirmer votre mot de passe dans le troisième champ.")
		pass2.focus()
	}

	else if (pass.value!=pass2.value){
		alert("Les deux mots de passe ne condordent pas.")
		pass.select()
	}

	else{
		setaction("CREACPT");
		pseudo = psd.value;
		connexion_serveur();
		socket_send("CREATCPT@"+psd.value+"@"+pass.value);
	}
}

function connexion(psd, pass){
	pseudo = psd.value;
	setaction("CONNECT");
	connexion_serveur();
	socket_send("CONNECT@"+psd.value+"@"+pass.value);
	document.getElementById("pseudocon").value = '';
	document.getElementById("passcon").value = '';
}
