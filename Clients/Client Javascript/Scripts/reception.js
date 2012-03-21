var action="";

function setaction(act){
	action = act;
}

function on_socket_get(message){
	if(action=="CONNECT"){
		concompte(message);
	}
	else if(action=="CREATCPT"){
		creatcompte(message);
	}
	else if(action=="ACTPASS"){
		actpass(message);
	}
	else if(action=="ACTPSEUDO"){
		actpsd(message);
	}
	else if(action=="CREATEPARTIE"){
		creerpartie(message);
	}
	else if(action=="GETLISTEPARTIE"){
		listerparties(message);
	}
}

function concompte(message){
	if(message == 'CONNECTOK'){
		con();
	}
	
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, connexion impossible.");
		
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else alert("Serveur indisponnible. Veuillez r�essayer ult�rieurement.");
}

function creatcompte(message){
	if(message == 'CREATOK'){
		effform("formcreat");
		$("#formcreat").hide();
		$("#formcon").show();
	}
	else if(message == 'AUPSEUDO')
		alert("Pseudo d�j� utilis�.");
	
	else if(message == 'ERREURBDD')
		alert("Erreur lors de la cr�ation de compte. Veuillez recommencer.");
		
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
		alert("Votre mot de passe a bien �t� modifi�.");
	}
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, changement impossible.");
	
	else if(message == 'WFPASS')
		alert("Mauvais format de mot de passe.");
}

function actpsd(message){
	if(message == 'OK'){
		effform("chmtpsd");
		$("#chmtpsd").hide();
		alert("Votre pseudo a bien �t� modifi�.");
	}
	else if(message == 'WPASS')
		alert("Mot de passe incorrect.");
		
	else if(message == 'WPSEUDO')
		alert("Pseudo introuvable, changement impossible.");
	
	else if(message == 'WFPSEUDO')
		alert("Mauvais format de pseudo.");
	
	else if(message == 'AUPSEUDO')
		alert("Pseudo d�j� utilis�.");
}

function creerpartie(message){
	if(message == 'CREATPOK'){
		effform("creerpartie");
		$("#creerpartie").hide();
		alert("Votre partie a bien �t� cr��e.");
	}
	else if(message == 'PAU')
		alert("Nom de partie deja utilis�.");
		
	else if(message == 'WFP')
		alert("Mauvais format nom de partie.");
	
	else if(message == 'NCON')
		alert("Vous devez �tre connect� pour cr�er une partie.");
}

function listerparties(message){
	if(message=="NCON")
		alert("Vous devez �tre connect� pour voir la liste des parties en cours");

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