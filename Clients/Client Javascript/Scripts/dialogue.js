
var action="";
var pseudo="";

function on_socket_get(message){
	if(action=="CONNECT"){
		if(message == 'CONNECTOK')
			document.getElementById("acceuil").innerHTML = "Bonjour "+pseudo;
			
		else if(message == 'WPSEUDO')
			alert("Pseudo introuvable, connexion impossible.");
		
		else if(message == 'WPASS')
			alert("Mot de passe incorrect.");

		else alert("Serveur indisponnible. Veuillez réessayer ultérieurement.");
	}
	else if(action=="CREATCPT"){
		if(message == 'CREATOK'){
			tout_cacher();
			afficher("formcon");
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
	else if(action=="ACTPASS"){
		if(message == 'OK'){
			tout_cacher();
			alert("Votre mot de passe a bien été modifié.");
		}
		else if(message == 'WPASS')
			alert("Mot de passe incorrect.");
			
		else if(message == 'WPSEUDO')
			alert("Pseudo introuvable, changement impossible.");
		
		else if(message == 'WFPASS')
			alert("Mauvais format de mot de passe.");
	}
	else if(action=="ACTPSEUDO"){
		if(message == 'OK'){
			tout_cacher();
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
	else if(action=="CREATEPARTIE"){
		if(message == 'CREATPOK'){
			tout_cacher();
			alert("Votre partie a bien été créée.");
		}
		else if(message == 'PAU')
			alert("Nom de partie deja utilisé.");
			
		else if(message == 'WFP')
			alert("Mauvais format nom de partie.");
		
		else if(message == 'NCON')
			alert("Vous devez être connecté pour créer une partie.");
	}
	else if(action=="GETLISTEPARTIE"){
		if(message=="NCON")
			alert("Vous devez être connecté pour voir la liste des parties en cours");
		else{
			affiche("listeparties");
			var liste = message.split("@");
			var partie;
			for(i=1;i<liste.length;i++){
				partie = liste[i].split("/");
				$("#tableparties").append("<TR>");
				for(j=0;j<partie.length;j++){
					$("#tableparties").append("<TD>"+partie[j]+"</TD>");
				}
				$("#tableparties").append("</TR>");
			}
		}
	}
}

function connexion_serveur(){
	socket_connect('192.168.43.76', 6667);
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
		action = "ACTPSEUDO";
		socket_send("ACTPSEUDO@"+pseudo+"@"+pass.value+"@"+nouveau.value);
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
		action = "ACTPASS";
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
		action = "CREACPT";
		pseudo = psd.value;
		socket_send("CREATCPT@"+psd.value+"@"+pass.value);
	}
}

function connexion(psd, pass){
	pseudo = psd.value;
	action = "CONNECT";
	socket_send("CONNECT@"+psd.value+"@"+pass.value);
	document.getElementById("pseudocon").value = '';
	document.getElementById("passcon").value = '';
}

function creer_partie(nom, nb){
	action = "CREATEPARTIE";
	socket_send("CREATEPARTIE@"+nom.value+"@"+nb.value);
}

function lister_parties(){
	tout_cacher();
	action = "GETLISTEPARTIE";
	socket_send("GETLISTEPARTIE");
}