var pseudo="";
var nom_partie="test";
var createur =  false;

$(function(){
	tout_cacher();
	/*$("#lidecon").hide();
	$("#gestionjeu").hide();*/
});

function con(){
	direbonjour();
	effform("formcon");
	$("#formcon").hide();
	/*$(".deconnecte").hide();
	$(".connecte").show();*/
}

function decon(){
	tout_cacher();
	$("#acceuil").text('');
	/*$(".deconnecte").show();
	$(".connecte").hide();*/
}

function tout_cacher(){
	efftoutform();
	$('.cache').hide();
}

function affiche(division, form, champ){
	tout_cacher();
	$("#"+division).show();
	$("#"+form).show("slow");
	$("#"+champ).focus()
}

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
	$("#table").show("slow");
	/*$("#table_defil").show("slow");*/
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
	tout_cacher();
	$("#partie").show("slow");
}

function lister_joueurs(tab){
	var html = "";
	for(i=1;i<tab.length;i++){
		html+="<li id=\'"+tab[i]+"\'><a href=\"javascript:infos_joueur(\'"+tab[i]+"\');\">"+tab[i]+"</a></li>";
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
	$("#infos_joueur").hide();
	$("#info_nom").text(tab[1]);
	$("#info_inscription").text(tab[4]);
	$("#info_gagnees").text(tab[2]);
	$("#info_perdus").text(tab[3]);
	$("#infos_joueur").show('slow');
}

function jetonj(tab){
	var html = "";
	var jetons;
	for(i=1;i<tab.length;i++){
		jetons = tab[i].split('/');
		html += "<p id=\'j" +jetons[0]+ "\'> Jetons totaux: " +jetons[1]+ " Jetons mis�s : " +jetons[2]+ "</p>";
		$("#"+jetons[0]).append(html);
		html = "";
	}
}

