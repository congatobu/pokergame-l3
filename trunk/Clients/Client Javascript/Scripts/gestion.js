var pseudo="";
var nom_partie="test";
var createur =  false;

$(function(){
	tout_cacher("");
	$("#lidecon").hide();
	$("#gestionjeu").hide();
});

function con(){
	direbonjour();
	effform("formcon");
	$("#formcon").hide();
	$(".deconnecte").hide();
	$(".connecte").show();
}

function decon(){
	tout_cacher("\'slow\'");
	$("#acceuil").text('');
	$(".deconnecte").show();
	$(".connecte").hide();
}

function tout_cacher(effet){
	efftoutform();
	$('.cache').hide(effet);
}

function affiche(division){
	tout_cacher("\'slow\'");
	$("#"+division).show("slow");
}

function affiche_liste(message){
	var html='';
	var liste = message.split("@");
	var partie;
	for(i=1;i<liste.length;i++){
		partie = liste[i].split("/");
		html +='<TR><TD><a href=\"javascript:rejoindre_partie(\''+partie[0]+'\');\">'+partie[0]+'</a></TD>'
		for(j=1;j<partie.length;j++){
			html +='<TD>'+partie[j]+'</TD>';
		}
		html +='</TR>';
	}
	$("#tableparties").append(html);
	$("#listeparties").show("slow");
}

function annuler(id){
	effform(id);
	$("#"+id).hide("slow");
}

function efftoutform(){
	$('form').children(':input')
		.not(':button, :submit, :reset, :hidden')
		.val('');
}
		
function effform(id){
	$('#'+id).children(':input')
		.not(':button, :submit, :reset, :hidden')
		.val('');
}

function setpseudo(psd){
	pseudo = psd;
}

function direbonjour(){
	$("#acceuil").text("Bonjour "+pseudo);
}

function set_nom_partie(nom){
	nom_partie = nom;
}

function entrer_partie(){
	$("#partie").children("h3").text(nom_partie);
	if(!createur) $("#partie").children("#bdebut").hide();
	$("#partie").show("slow");
}

function lister_joueurs(tab){
	var html = "";
	for(i=1;i<tab.length;i++){
		html+="<li><a href=\"javascript:infos_joueur(\'"+tab[i]+"\');\">"+tab[i]+"</a></li>";
	}
	$("#partie").children("ul").text("");
	$("#partie").children("ul").append(html);
}

function set_createur(bool){
	createur = bool;
}

function afficher_infos_joueur(message){
	var tab = message.split("@");
	var html = "";
	html += "<p>Nom du joueur: "+tab[1]+"</p>";
	html += "<p>Date d'inscription: "+tab[4]+"</p>";
	html += "<p>Parties gagnées: "+tab[2]+"</p>";
	html += "<p>Parties perdus: "+tab[3]+"</p>";
	$("#infos_joueur").hide();
	$("#infos_joueur").text("");
	$("#infos_joueur").append(html);
	$("#infos_joueur").show('slow');
}