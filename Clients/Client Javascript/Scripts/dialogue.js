
var action="";

function on_socket_get(message){
	if(action=="CONNECT"){
		if(message == 'WPSEUDO')
			alert("Pseudo introuvable, connexion impossible.");
		
		else if(message == 'WPASS')
			alert("Mot de passe incorrect.");
			
		else if(message == 'CONNECTOK')
			socket_send("GETLISTEPARTIE");
			
		else alert("Serveur indisponnible. Veuillez réessayer ultérieurement.");
	}
	else if(action=="CREATCPT"){
		if(retour == 'CREATOK')
			document.location.href="index2.html";
			
		else if(retour == 'AUPSEUDO')
			alert("Pseudo déjà utilisé.");
		
		else if(retour == 'ERREURBDD')
			alert("Erreur lors de la création de compte. Veuillez recommencer.");
			
		else if(retour == 'WFPSEUDO')
			alert("Mauvais format de pseudo.");
			
		else if(retour == 'WFPASS')
			alert("Mauvais format de mot de passe.");
		
		else
			alert("Serveur indisponible;");
	}
}

function connection_serveur(){
	socket_connect('88.167.230.145', 6667);
}

function connection(pseudo, pass){
	action = "CONNECT";
	socket_send("CONNECT@"+pseudo.value+"@"+pass.value);
	document.getElementById("pseudo").value = '';
	document.getElementById("pass").value = '';
}

function creation(pseudo, pass, pass2){

	if (pseudo.value==''){
		alert("Veuillez entrer votre pseudo dans le premier champ!")
		pseudo.focus()
	}
   
	else if (pass.value==''){
		alert("Veuillez entrer votre mot de passe dans le second champ!")
		pass.focus()
	}
	
	else if (pass2.value==''){
		alert("Veuillez confirmer votre mot de passe dans le troisième champ!")
		pass2.focus()
	}

	else if (pass.value!=pass2.value){
		alert("Les deux mots de passe ne condordent pas")
		pass.select()
	}

	else{
		action = "CREATCPT";
		socket_send("CREATCPT@"+pseudo.value+"@"+pass.value);
	}
}

function changer_mdp(pseudo, pass){

}