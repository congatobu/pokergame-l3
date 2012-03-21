var action="";

function setaction(act){
	action = act;
}

function on_socket_get(message){
	if(action=="CONNECT"){
		if(message == 'CONNECTOK'){
			$("#acceuil").text("Bonjour "+pseudo);
			$("#formcon").hide();
			$("#licon").hide();
			$("#lidecon").show();
			$("#gestionjeu").show();
		}
			
		else if(message == 'WPSEUDO')
			alert("Pseudo introuvable, connexion impossible.");
		
		else if(message == 'WPASS')
			alert("Mot de passe incorrect.");

		else alert("Serveur indisponnible. Veuillez r�essayer ult�rieurement.");
	}
	else if(action=="CREATCPT"){
		if(message == 'CREATOK'){
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
	else if(action=="ACTPASS"){
		if(message == 'OK'){
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
	else if(action=="ACTPSEUDO"){
		if(message == 'OK'){
			$("#chmtpsd").hide();
			pseudo = pseudo2;
			$("#acceuil").text("Bonjour "+pseudo);
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
	else if(action=="CREATEPARTIE"){
		if(message == 'CREATPOK'){
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
	else if(action=="GETLISTEPARTIE"){
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
}