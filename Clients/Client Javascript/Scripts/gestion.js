var pseudo="";

$(function(){
	tout_cacher();
	$("#lidecon").hide();
	$("#gestionjeu").hide();
});

function con(){
	direbonjour();
	effform("formcon");
	$("#formcon").hide();
	$("#licon").hide();
	$("#licreat").hide();
	$("#lipsd").hide();
	$("#limdp").hide();
	$("#lidecon").show();
	$("#gestionjeu").show();
}

function decon(){
	tout_cacher();
	$("#acceuil").text('');
	$("#licon").show();
	$("#licreat").show();
	$("#lipsd").show();
	$("#limdp").show();
	$("#lidecon").hide();
	$("#gestionjeu").hide();
}

function tout_cacher(){
	efftoutform();
	$("#formcon").hide();
	$("#formcreat").hide();
	$("#chmtmdp").hide();
	$("#chmtpsd").hide();
	$("#creerpartie").hide();
	$("#listeparties").hide();
}
	
function affiche(division){
	tout_cacher();
	$("#"+division).show();
}

function annuler(id){
	effform(id);
	$("#"+id).hide();
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