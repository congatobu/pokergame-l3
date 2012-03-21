$(function(){
	tout_cacher();
	$("#lidecon").hide();
	$("#gestionjeu").hide();
});

function decon(){
	tout_cacher();
	$("#acceuil").text('');
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