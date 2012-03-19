$(function(){
	tout_cacher();
});
	
function tout_cacher(){
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