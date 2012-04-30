/**
 * @fileOverview Ce fichier contient les fonctions de modifications dynamiques de la page HTML.
 * @author Paul	Mura
 */

var pseudo="";
var nom_partie="test";
var createur =  false;
var encours =  false;
var partie;
var jetonsTable = 0;
var dico = {"Janvier":"01","Fevrier":"02","Mars":"03","Avril":"04","Mai":"05","Juin":"06","Juillet":"07","Août":"08","Aout":"08","Septembre":"09","Octobre":"10","Novembre":"11","Décembre":"12","Decembre":"12"};

//INITIALISATION

/**
 * Appelle la fonction de message correspondant au domaine au message reçu.
 * @name Initialiser
 * @author Paul	Mura
 */
$(function(){
	tout_cacher();
	$(".connecte").hide();
	$("#gestionjeu").hide();
	$("#acceuil").hide();
	$("#boutons").hide();
	$("#commencer").hide();
});


//AFFICHAGE

/**
 * Désactive l'affichage des éléments HTML appartenant à la classe "cache" et réinitialise les formulaires.
 * @author Paul	Mura
 */
function tout_cacher(){
	efftoutform();
	$('.cache').hide();
}

/**
 * Affiche le formulaire dont l'id est passé en paramètre.
 * @author Paul	Mura
 * @param {String} division Id de la division contenant le formulaire.
 * @param {String} form Id du formulaire.
 * @param {String} champ Id du champ du formulaire activé lors de l'affichage.
 */
function affiche(division, form, champ){
	tout_cacher();
	resetcon();
	resetcreerprt();
	resetcreation();
	resetpseudo();
	resetmdp();
	$("#"+division).show();
	$("#"+form).show("slow");
	$("#"+champ).focus()
}

/**
 * Réinitialise les champs du formulaire passer en paramètre.
 * @author Paul	Mura
 * @param {String} id Id du formulaire.
 */
function effform(id){
	$('#'+id).children(':input')
		.not(':button, :submit, :reset, :hidden')
		.val('');
}

/**
 * Réinitialise les champs de tout les formulaires.
 * @author Paul	Mura
 */
function efftoutform(){
	$('form').children(':input')
		.not(':button, :submit, :reset, :hidden')
		.val('');
}

/**
 * Réinitialise les champs du formulaire passer en paramètre et le rend invisible.
 * @author Paul	Mura
 * @param {String} id Id du formulaire.
 */
function annuler(id){
	effform(id);
	$("#"+id).hide("slow");
}

/**
 * Réinitialise le nom des champs du formulaire de connexion.
 * @author Paul	Mura
 */
function resetcon(){
	$("#labpseudocon").text("Pseudo :");
	$("#labpasscon").text("Mot de Passe :");
}

/**
 * Réinitialise le nom des champs du formulaire de création de partie..
 * @author Paul	Mura
 */
function resetcreerprt(){
	$("#labnompartie").text("Nom Partie : ");
	$("#labnbjoueurs").text("Joueurs Maximun :");
}

/**
 * Réinitialise le nom des champs du formulaire de création de compte.
 * @author Paul	Mura
 */
function resetcreation(){
	$("#labpseudocreat").text("Pseudo :");
	$("#labpasscreat").text("Mot de Passe :");
	$("#labpass2creat").text("Comfirmation :");
}

/**
 * Réinitialise le nom des champs du formulaire de changement de pseudo.
 * @author Paul	Mura
 */
function resetpseudo(){
	$("#labpseudochmt").text("Pseudo Actuel :");
	$("#labnouvpseudo").text("Nouveau Pseudo :");
	$("#labpasschmtpsd").text("Mot de Passe :");
}

/**
 * Réinitialise le nom des champs du formulaire de changement de mot de passe.
 * @author Paul	Mura
 */
function resetmdp(){
	$("#labpseudochmt2").text("Pseudo :");
	$("#labancienmdp").text("Ancien Mot de Passe :");
	$("#labnouveaumdp").text("Nouveau Mot de Passe :");
	$("#labconfirm").text("Confirmation :");
}

/**
 * Affiche un message de bienvenue lors de la connexion.
 * @author Paul	Mura
 */
function direbonjour(){
	$("#acceuil").text("Bonjour "+pseudo);
	$("#acceuil").show('slow');
}


//GESTION DE COMPTE

/**
 * Modisie la variable globale contenant le pseudo de l'utilisateur.
 * @author Paul	Mura
 * @param {String} psd
 */
function setpseudo(psd){
	pseudo = psd;
}

/**
 * Renvoie le pseudo de l'utilisateur.
 * @author Paul	Mura
 * @returns {String} Pseudo de l'utilisateur.
 */
function get_pseudo(){ return pseudo;}

/**
 * Efface le formulaire de connexion, affiche le menu du mode connecté et affiche la div contenant "Bonjour pseudo".
 * @author Paul	Mura
 */
function con(){
	direbonjour();
	effform("formcon");
	$("#formcon").hide();
	$(".deconnecte").hide();
	$(".connecte").show();
}

/**
 * Efface le formulaire de connexion, affiche le menu du mode connecté et affiche la div contenant "Bonjour pseudo".
 * @author Paul	Mura
 */
function decon(){
	tout_cacher();
	$("#acceuil").text('');
	$("#acceuil").hide();
	$(".deconnecte").show();
	$(".connecte").hide();
}

/**
 * Efface le formulaire de création de compte, le cache et affiche le formulaire de connexion.
 * @author Paul	Mura
 */
function creation_ok(){
	effform("formcreat");
	$("#formcreat").hide();
	affiche('formtaille1','formcon', 'pseudocon');
}

/**
 * Efface le formulaire de modification de mot de passe, le cache et notifie l'utilisateur du succès.
 * @author Paul	Mura
 */
function chmt_pass_ok(){
	effform("chmtmdp");
	$("#chmtmdp").hide();
	alert("Votre mot de passe a bien été modifié.");
}

/**
 * Efface le formulaire de modification de pseudo, le cache et notifie l'utilisateur du succès.
 * @author Paul	Mura
 */
function chmt_psd_ok(){
	effform("chmtpsd");
	$("#chmtpsd").hide();
	alert("Votre pseudo a bien été modifié.");
}


//GESTION DES PARTIES

/**
 * Efface le formulaire de création de partie, le cache, règle le domaine de reception à "PARTIE" et appelle entrer_partie().
 * @author Paul	Mura
 */
function creation_partie_ok(){
	domaine= "PARTIE";
	effform("creerpartie");
	$("#creerpartie").hide();
	entrer_partie();
}

/**
 * Affiche la liste des parties.
 * @author Paul	Mura
 * @param {String} message Liste de partie sous la forme: Partie1/nombre_joueurs/nombre_max@Partie2/nombre_joueurs/nombre_max...
 */
function affiche_liste(message){
	var html='';
	var liste = message.split("@");
	var partie;
	
	html += "<TR id=\'titretable\'>"+$("#titretable").html()+"</TR>";
	for(i=1;i<liste.length;i++){
		partie = liste[i].split("/");
		html +='<TR><TD class=\'ligne_partie\' align=\'center\'><a style=\'display : block; text-decoration : none;\' href=\"javascript:rejoindre_partie(\''+partie[0]+'\');\">'+partie[0]+'</a></TD>'
		for(j=1;j<partie.length;j++){
			html +='<TD class=\'ligne_partie\' align=\'center\'>'+partie[j]+'</TD>';
		}
		html +='</TR>';
	}
	
	$("#tableparties").text("");
	$("#tableparties").append(html);
	$("#tableau").show("slow");
}

/**
 * Cache la liste des parties. Règle le domaine dereception à "PARTEI". Appelle entrer_partie().
 * @author Paul	Mura
 * @param {String} message Liste de partie sous la forme: Partie1/nombre_joueurs/nombre_max@Partie2/nombre_joueurs/nombre_max...
 */
function rej_ok(){
	domaine = "PARTIE";
	$("#listeparties").hide();
	entrer_partie();
}
		

//GESTION DE LA PARTIE EN COURS

/**
 * Affecte vrai ou faux à la variable globale créateur.
 * @author Paul	Mura
 * @param {Booleen} bool
 */
function set_createur(bool){
	createur = bool;
}

/**
 * Affecte vrai ou faux à la variable globale encours.
 * @author Paul	Mura
 * @param {Booleen} bool
 */
function set_encours(bool){
	encours = bool;
}

/**
 * Met à jour la variable globale nom_partie.
 * @author Paul	Mura
 * @param {String} nom Nom de la partie en cours.
 */
function set_nom_partie(nom){
	nom_partie = nom;
}

/**
 * Cache tout les menus. Créer une instance de Partie. Affiche les boutons "commencer" et "quitter".
 * @author Paul	Mura
 */
function entrer_partie(){
	tout_cacher();
	partie = new Partie(pseudo);
	$("#menu").hide();
	$("#acceuil").hide();
	$("#partie").show("slow");
	$("#boutons").show("slow");
	$("#chat").show("slow");
	if(createur) $("#commencer").show("slow");
}

/**
 * Cache le bouton "commencer".
 * @author Paul	Mura
 */
function demmarer_partie(){
	$("#commencer").hide();
}

/**
 * Efface le contenu HTML de la division "partie". Affiche le menu et le message d'acceuil.
 * @author Paul	Mura
 */
function sortir_partie(){
	$("#partie").text("");
	$("#boutons").hide();
	$("#chat").hide();
	$("#commencer").hide();
	$("#menu").show('slow');
	$("#acceuil").show('slow');
}

/**
 * Affiche une division contenant les informations relative à un joueur de la partie.
 * @author Paul	Mura
 * @param {String} message Informations sur le joueur.
 */
function afficher_infos_joueur(message){
	var tab = message.split("@");
	var html = "";
	tout_cacher();
	$("#info_nom").text(tab[1]);
	var date = tab[4].split(" ");
	$("#info_inscription").text(date[1]+"/"+dico[date[2]]+"/"+date[3]);
	$("#info_gagnees").text(tab[2]+" / "+tab[3]);
	$("#infos_joueur").show('slow');
}

/**
 * Appelle la fonction partie.miseajourJetons(Array[String[]]).
 * @author Paul	Mura
 * @param {String[]} tab Tableau contenant le nombre de jetons de chaque joueur.
 */
function jetonj(tab){
	var liste = [];
	for(i=1;i<tab.length;i++) liste[i-1] = tab[i].split("/");
	partie.miseajourJetons(liste);
}

/**
 * Détecte les changement dee créateur de partie et appelle la fonction partie.miseajourJoueurs(String[]).
 * @author Paul	Mura
 * @param {String[]} liste Tableau contenant la liste des joueurs de la partie.
 */
function lister_joueurs(liste){
	for(i=1;i<liste.length;i++)
		if(liste[i].substring(0,1)=="$"){
			liste[i]=liste[i].substring(1,liste[i].length);
			if(!encours && liste[i]==pseudo){
				$("#commencer").show("slow"); 
				if(!createur){
					alert("Le créateur a quitté la partie.\nVous devenez le nouveau créateur!");
					createur = true;
				}
			}
		}
	partie.miseajourJoueurs(liste.slice(1,liste.length));
}


/**
 * Appelle la fonction partie.distribuerCarteJoueurs(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant les cartes de l'utilisateur.
 */
function cartem(act){
	partie.distribuerCarteJoueurs(act.slice(2,act.length));
}

/**
 * Appelle la fonction partie.distribuerCarteTable(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant les cartes de la table.
 */
function cartet(act){
	partie.centrerJetons(jetonsTable);
	partie.distribuerCarteTable(act.slice(1,act.length));
}

/**
 * Appelle la fonction partie.setMinMax(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant les mises minimum et maximum.
 */
function joue(act){
	partie.setMinMax(parseInt(act[1]),parseInt(act[2]),act[3]);
}

/**
 * Met à jour la variable globale jetonsTable.
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant les jetons posés sur la table.
 */
function jetont(act){
	jetonsTable = act[1];
}

/**
 * Appelle la fonction partie.montrerCarteJoueurs(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant les cartes des joueurs adverses.
 */
function montrec(act){
	var liste = [,,,,,,,];
	for(i=1;i<act.length;i++) liste[i-1] = act[i].split("/");
	partie.montrerCartesJoueurs(liste);
}

/**
 * Appelle la fonction partie.gagnants(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant la liste des gagnants d'un tour.
 */
function gagnantt(act){
	partie.gagnants(act.slice(1,act.length));	
}

/**
 * Appelle la fonction partie.actionJoueur(String[]).
 * @author Paul	Mura
 * @param {String[]} act Tableau contenant le nom d'un joueur et son choix (se coucher, suivre, relancer).
 */
function jchoix(act){
	partie.actionJoueur(act[1],parseInt(act[2]));
}

/**
 * Appelle la fonction partie.interroger(String).
 * @author Paul	Mura
 * @param {String} act Pseudo de la personne qui doit jouer.
 */
function joueurj(act){
	partie.interroger(act[1]);
}

/**
 * Modification de l'opacité du chat lorsque la sourie passe au-dessus.
 * @author Paul	Mura
 */
function montrer_chat(){
	$('chat').css({ opacity: 0.5 });
	$('#mess').focus();
}