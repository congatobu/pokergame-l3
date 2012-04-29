//------------------------------ Paramètres Globaux------------------------------
var joueurs=["","","","","","","",""];
var caves=[-1,-1,-1,-1,-1,-1,-1,-1];

var mh=600; //hauteur totale du cadre
var mw=1100; //largeur totale du cadre
var coordonneesjoueurs=[
[mh/3.43,0],
[mh/6-50,mw/3.75],
[mh/6-50,mw/1.63],
[mh/3.43,mw/1.16],
[mh/2,mw/1.16],
[mh/1.71,mw/1.63],
[mh/1.71,mw/3.75],
[mh/2,0]
]; 
var pointorigine=[
[mh/6,mw/2],
[mh/3.43,mw/4.89],
[mh/3.43,-mw/8.8],
[mh/6,-mw/2.75],
[-mh/24,-mw/2.75],
[-mh/8,-mw/8.8],
[-mh/8,mw/4.89],
[-mh/24,mw/2],
[mh/8,mw/6],
[mh/2-50,mw/2+100]
];

//------------------------------ Fonctions ------------------------------
function sep(){
	//Function renvoyant le caractère de séparation en fonction de l'OS
	var V=navigator.appVersion;
	if (V.indexOf("Mac",0)>0) {return "/";}
	if (V.indexOf("Linux",0)>0) {return "/";}
	if (V.indexOf("Win",0)>0) {return "\\";}
};
function disableselect(e){
return false
}; // desactiver la selection sur la page
function reEnable(){
return true
};
function chargerSection(){
	var partie =  new Partie("Alain");
};
function ejs_nodroit(){return(false);}; // desactiver clique droit
if (window.sidebar){
document.onmousedown=disableselect
document.onclick=reEnable
};
document.onselectstart=new Function ("return false");
document.oncontextmenu = ejs_nodroit;
//------------------------------ Objets ------------------------------
function Div(id,height,width,top,left){
	//Objet contenant comme attribut une balise html "div" (non accessible hors de l'objet). 
	//Celle-ci est manipulable par les différentes méthodes fournis.
	//Cet objet permet de garder le code des autres objets intact si l'attribut de l'Objet Div venait à changer.
	//De plus, cette objet peut facilement être réutilisé dans d'autre application javascript manipulant une page html et un fichier css.
	
	//------------------------------ Attributs ------------------------------
	var div=document.createElement("div");
	
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return div;};
	this.getId=function(){return div.id;};
	this.getTop=function(){return div.style.top;};
	this.getLeft=function(){return div.style.left;};
	this.getHeight=function(){return div.style.height;};
	this.getWidth=function(){return div.style.width;};
	
	this.setDiv=function(div){div=div;};
	this.setId=function(newid){div.id=newid;};
	this.setTop=function(t){div.style.top=t+"px";};
	this.setLeft=function(l){div.style.left=l+"px";};
	this.setHeight=function(h){div.style.height=h+"px";};
	this.setWidth=function(w){div.style.width=w+"px";};
	this.setBackground=function(url,position,repeat,color){
    	div.style.backgroundImage= "url("+url+")";
   	 	div.style.backgroundRepeat=repeat;
   		div.style.backgroundPosition= position;
   		div.style.backgroundColor= color; 
	};
	this.setBorder=function(width,color,radius){
		div.style.border= width+"px solid "+color;
    	div.style.borderRadius= radius;
	};
	this.setColor=function(color){div.style.color=color;};
	this.setZIndex=function(h){div.style.zIndex=h;};
	this.setCursor=function(c){div.style.cursor=c;};
	this.setPadding=function(p){div.style.padding=p;};
	this.setMargin=function(t,l){
		div.style.marginTop=t;
		div.style.marginLeft=l;
	};
	this.setOnClick=function(action){div.onclick=action;};
	this.setOnMouseOver=function(action){div.onmouseover=action;};
	this.setOnMouseOut=function(action){div.onmouseout=action;};
	this.setOnMouseMove=function(action){div.onmousemove=action;};
	this.setOnMouseDown=function(action){div.onmousedown=action;};
	this.setOnMouseUp=function(action){div.onmouseup=action;};
	this.setOpacity=function(val){div.style.opacity=val;};
	this.setVisibility=function(bool){
		if(bool) div.style.display="block";
		else div.style.display="none";
	};
	
	this.initialiser=function(id,height,width,top,left){
		div.id= id;
		div.style.height= height;
		div.style.width= width;
		div.style.top= top;
		div.style.left= left;
		div.style.color= "white";
		div.style.position= "absolute";
	};
	
	this.ajouterFils=function(f){
		div.appendChild(f.getDiv());
	};
	this.supprimerFils=function(f){
		var fils=f.getDiv();
		if (fils!=null){
			div.removeChild(fils);
		}
		else {
			alert(f+" n'existe pas !");
			return;
		}
		
	};
	this.ajouterTexte=function(text){
		div.appendChild(document.createTextNode(text));
	};
	this.ajouterElement=function(elt){
		div.appendChild(elt);
	};
	this.vider=function(){div.innerHTML="";};
	
	//------------------------------ Initialisation ------------------------------
	this.initialiser(id,height,width,top,left);
};
function Input(id,type,height,width,top,left){	

    //------------------------------ Attributs ------------------------------
	var input=document.createElement("input");
	
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return input;};
	this.getId=function(){return input.id;};
	this.getType=function(){return input.type;};
	this.getValue=function(){return input.value;};
	this.getTop=function(){return input.style.top;};
	this.getLeft=function(){return input.style.left;};
	this.getHeight=function(){return input.style.height;};
	this.getWidth=function(){return input.style.width;};
	
	this.setId=function(newid){input.id=newid;};
	this.setType=function(newtype){input.type=newtype;};
	this.setValue=function(newvalue){input.value=newvalue;};
	this.setTop=function(t){input.style.top=t+"px";};
	this.setLeft=function(l){input.style.left=l+"px";};
	this.setHeight=function(h){input.style.height=h+"px";};
	this.setWidth=function(w){input.style.width=w+"px";};
	this.setColor=function(color){input.style.color=color;};
	this.setZIndex=function(h){input.style.zIndex=h;};
	this.setCursor=function(c){input.style.cursor=c;};
	this.setMargin=function(t,l){
		input.style.marginTop=t;
		input.style.marginLeft=l;
	};
	this.setOnClick=function(action){input.onclick=action;};
	this.setOnMouseOver=function(action){input.onmouseover=action;};
	this.setOnMouseOut=function(action){input.onmouseout=action;};
	this.setOnMouseMove=function(action){input.onmousemove=action;};
	this.setOnMouseDown=function(action){input.onmousedown=action;};
	this.setOnMouseUp=function(action){input.onmouseup=action;};
	this.setOpacity=function(val){input.style.opacity=val;};
	this.setVisibility=function(bool){
		if(bool) input.style.display="block";
		else input.style.display="none";
	};
	this.disabled=function(bool){input.disabled=bool;};
	
	this.initialiser=function(id,type,height,width,top,left){
		input.id=id;
		input.type=type;
		input.style.height= height;
		input.style.width= width;
		input.style.top= top;
		input.style.left= left;
		input.style.color= "White";
		input.style.position= "absolute";
	};
	
	//------------------------------ Initialisation ------------------------------
	this.initialiser(id,type,height,width,top,left);
}
function Partie(u){
	//------------------------------ Attributs ------------------------------
	var ref=this;
	var h,ii,j,misemin,misemax,boolrelance;
	var listejoueurs,action,utilisateur;
	var div,table,divcartessurtable,ligne,cartessustable,bar;
	var slide;
	var mise;
	var passer,suivre,relancer,divrelancer;
	var montantjoueurs, montantjoueurstour;
	
	this.principal;
	 
	//------------------------------ Méthodes ------------------------------
	this.getCarte=function(num){return cartessurtable[num];}
	this.getJoueur=function(num){return listejoueurs[num];};
	this.getSlide=function(){return slide;};
	this.getIndiceJoueur=function(pseudo){
		for(var i=0;i<8;i++){
			if(joueurs[i]==pseudo){
				return i;
			}
		}	
		return -1;
	};
	this.getDivCartes=function(){return divcartessurtable;}
	this.getDivRelancer=function(){return divrelancer;};
	this.getMise=function(){return mise};
	this.getPrincipal=function(){return this.principal;};
	this.getMiseMin=function(){return misemin;};
	
	this.setMinMax=function(newmisemin,newmisemax,relance){misemin=newmisemin; misemax=newmisemax; boolrelance=relance;};
	this.setCarte=function(num,val){cartesurtable[num]=val;};
	
	this.initialiser=function(u){
	    joueurs=["","","","","","","",""];
        caves=[-1,-1,-1,-1,-1,-1,-1,-1];
		montantjoueurs = [0,0,0,0,0,0,0,0];
		montantjoueurstour = [0,0,0,0,0,0,0,0];
		utilisateur=u;
		listejoueurs=[null,null,null,null,null,null,null,null];
		action=[null,null,null,null,null,null,null,null];
		div=new Div("principale",mh,mw,"50%","50%");
		table=new Div("table",mh/3.2,mw/1.4,mh/5,mw/9);
		divcartessurtable=new Div("divcartessurtable",50,140,mh/3,mw/2.29);
		ligne=new Div("","70%","90%","15%","5%");
		mise=new Mise();
		cartessurtable=[null,null,null,null,null];
		h=ii=j=0;
		boolrelance=true;
		
		div.setMargin(-mh/2,-mw/2);
		table.setBorder(30,"RGBA(50,30,10,0.9)",1000);
		table.setBackground("","center","repeat","RGBA(20,80,40,0.9)");
		ligne.setBorder(1,"RGBA(255,255,255,0.5)",1000);
		
		div.ajouterFils(table);
		div.ajouterFils(mise);
		div.ajouterFils(divcartessurtable);
		table.ajouterFils(ligne);
		
		document.getElementById("partie").appendChild(div.getDiv());
		
	    this.initialiserBoutons();
	};
	
	//----- méthodes relatives à la gestion des joueurs -----
	this.asseoirJoueur=function(pseudo,place){
		listejoueurs[place]=new Joueur(pseudo,place);
		div.ajouterFils(listejoueurs[place]);
	};
	this.leverJoueur=function(place){
		if(listejoueurs[place]!=null){
		    listejoueurs[place].jeterCartes();
			div.supprimerFils(listejoueurs[place]);
			listejoueurs[place]=null;
		}
	};
	this.miseajourJoueurs=function(liste){
		var present,k;
		for(var i=0;i<liste.length;i++){
			present=false;
			k=0;
			for(var j=0;j<8;j++){
				if(liste[i]==joueurs[j]) {
						present=true;
				}	
			}
			if(!present) {
				while(k<8 && joueurs[k]!=""){
					k++;
				}
				joueurs[k]=liste[i];
				this.asseoirJoueur(joueurs[k],k);
			}
		}
		for(var i=0;i<8;i++){
			present=false;
			for(var j=0;j<liste.length;j++){
				if(liste[j]==joueurs[i]) {
						present=true;
				}	
			}
			if(!present){
				joueurs[i]="";
				caves[i]=-1;
				this.leverJoueur(i);
			}
		}
		this.principal=this.getIndiceJoueur(utilisateur);
	};
	this.miseajourJetons=function(liste){
		for(var i=0;i<liste.length;i++){
			
			var indice=this.getIndiceJoueur(liste[i][0]);
			if(indice!=-1){
				montantjoueurstour[indice]=parseInt(liste[i][2]);
				listejoueurs[indice].setJetons(parseInt(liste[i][1]));
				mise.poserJetons(indice,parseInt(liste[i][2])-montantjoueurs[indice]);
			}
		}
	};
	this.montrerCartesJoueurs=function(liste){
		for(var i=0;i<liste.length;i++){
			var indice=this.getIndiceJoueur(liste[i][0]);
			if(indice!=-1) {
				listejoueurs[indice].montrerCartes(liste[i][1],0);
				listejoueurs[indice].montrerCartes(liste[i][2],1);
			}
		}
	};
	this.gagnants=function(liste){
		for(var i=0;i<liste.length;i++){
			var indice=this.getIndiceJoueur(liste[i]);
			if(indice!=-1) {
				listejoueurs[indice].estGagnant(true);
			}
		}
		if (mise!=null) {
		    div.supprimerFils(mise);
		    mise.initialiser();
		    div.ajouterFils(mise);
		}
		setTimeout(function(){
			ref.enleverCarteJoueurs();
			ref.enleverCarteTable();
			for(var i=0;i<8;i++){
			    if(listejoueurs[i]!=null) {
			        listejoueurs[i].estGagnant(false);
			        listejoueurs[i].setCartes(0,null);
			        listejoueurs[i].setCartes(1,null);
			        listejoueurs[i].getDivCartes().vider();
			    }
			}
		    montantjoueurs = [0,0,0,0,0,0,0,0];
		    montantjoueurstour = [0,0,0,0,0,0,0,0];
		},500);
	};
	//----- méthodes relatives à la gestion des cartes & Jetons -----
	this.distribuerCarteJoueurs=function(c){
		if(listejoueurs[ii]!=null && ii<8){
			
			listejoueurs[ii].donnerCartes(h);
			ii++;
			setTimeout(function(){
			    ref.distribuerCarteJoueurs(c);
			},200);
			return;
		}
		if(ii<8 && listejoueurs[ii]==null) {
			ii++;
			this.distribuerCarteJoueurs(c);
			return;
		}
		if(ii==8 && h<1){
			listejoueurs[this.principal].montrerCartes(c[h],h);
			ii=0;
			h=1;
			setTimeout(function(){ref.distribuerCarteJoueurs(c);},10);
			return;
		}
		if(ii==8 && h==1){
			listejoueurs[this.principal].montrerCartes(c[h],h);
			ii=h=0;
		}
	};
	this.distribuerCarteTable=function(liste){
		var left=[7,30,53,83,113];
		for(var i=0;i<liste.length;i++){
			if(cartessurtable[i]==null){
				cartessurtable[i]=new Carte(left[i],8);
				divcartessurtable.ajouterFils(cartessurtable[i]);
				cartessurtable[i].setNum(liste[i]);
				cartessurtable[i].donner();	
				cartessurtable[i].montrer();
			}
		}
	};
	this.enleverCarteJoueurs=function(){
		for(var j=0;j<8;j++){
			if (listejoueurs[j]!=null){
				listejoueurs[j].getDivCartes().vider();
				listejoueurs[j].setCartes(0,null);
				listejoueurs[j].setCartes(1,null);
			}
		}
	};
	this.enleverCarteTable=function(){
	    divcartessurtable.vider();
		cartessurtable=[null,null,null,null,null];
	};
	this.centrerJetons=function(montant){
	    for(var i=0;i<8;i++){
		    montantjoueurs[i] = montantjoueurstour[i];
		}
		this.getMise().centrerJetons(montant);	
	};
	
	//----- méthodes relatives à la gestion de l'interrogation des joueurs -----
	this.interroger=function(pseudo){
		var indice=this.getIndiceJoueur(pseudo);
		if(indice==-1) return;
		listejoueurs[indice].nouveauTimer();
		if(pseudo==utilisateur) this.afficherBoutons(true);
	};
	this.actionJoueur=function(pseudo,choix){
		var indice=this.getIndiceJoueur(pseudo);
		if(indice==-1) return;
		if(choix==1){listejoueurs[indice].jeterCartes();}
		listejoueurs[indice].supprimerTimer();
		this.afficherBoutons(false);
	};
	
	this.initialiserBoutons=function(){
		bar=new Div("baroutils",50,325,"75%","30%");
		passer=new Input("passer","button","","",-10,"10%");
		suivre=new Input("suivre","button","","",-10,"50%");
		relancer=new Input("relancer","button","","",15,50);
		divrelancer=new Div("relancer",70,210,-10,"90%");
		slide=new Slide(5,5);
		
		passer.setValue("Passer");
		suivre.setValue("Parole/Suivre");
		relancer.setValue("Relancer");
		
		bar.setBorder(0,"white","10px 40px 10px 40px");
		bar.setBackground("","","","RGBA(20,80,40,0.9)");
		divrelancer.setBorder(1,"black","10px 40px 10px 40px");
		divrelancer.setBackground("","","","RGBA(20,80,40,0.9)");
		
		passer.setOnClick(function(){
			ref.actionJoueur(utilisateur,"1");
			choix_joueur("1","0");
		});
		suivre.setOnClick(function(){
		   ref.actionJoueur(utilisateur,"2");
			choix_joueur("2","0");
		});
		relancer.setOnClick(function(){
			ref.actionJoueur(utilisateur,"3");
			choix_joueur("3",ref.getSlide().getValue());
		});
		
		divrelancer.ajouterFils(relancer);
		divrelancer.ajouterFils(slide);
		bar.ajouterFils(passer);
		bar.ajouterFils(suivre);
		bar.ajouterFils(divrelancer);
		div.ajouterFils(bar);
		
		this.afficherBoutons(false);
	};
	this.afficherBoutons=function(bool){
		if(bool) slide.setMinMax(misemin,misemax);
		bar.setVisibility(bool);
		divrelancer.setVisibility(bool && boolrelance);	
	};
	//------------------------------ Initialisation ------------------------------
	this.initialiser(u);
};
function Joueur(pseudo,siege){
	//------------------------------ Attributs ------------------------------
	var ref=this;
	var r,v,t,dt,w,dw;
	var div,divpseudo,divjetons,divcartes,gagnant;
	var bartimer,timer;
	var cartesjoueur;
	
	this.pseudo;
	this.jetons;
	this.siege;
	
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return div.getDiv();}
	this.getCarte=function(num){return cartesjoueur[num]};
	this.getDivCartes=function(){return divcartes;}
	
	this.setCartes=function(num,val){cartesjoueur[num]=val;}
	this.setPseudo=function(newpseudo){this.pseudo=newpseudo;};
	this.setJetons=function(newcave){
		divjetons.vider();
		divjetons.ajouterTexte(newcave);};
	
	this.initialiser=function(pseudo,siege){
		this.pseudo=pseudo;
		this.jetons=-1;
		this.siege=siege;
		w=50;
		dw=w/102;
		r=t=0;
		v=255;
		dt=10000/102;
		div=new Div("joueur"+siege,"","",coordonneesjoueurs[siege][0],coordonneesjoueurs[siege][1]);
		divpseudo=new Div("pseudo"+siege,20,100,0,0);
		divjetons=new Div("jetons"+siege,20,100,27,0);
		divcartes=new Div("jetons"+siege,50,57,23,85);
		gagnant=new Div("gagnant",20,60,50,10);
		cartesjoueur=[null,null];
	
		if (pseudo.length<13)
			divpseudo.ajouterTexte(pseudo);
		else {
			var temp="";
			for (var i=0;i<10;i++){temp+=pseudo.charAt(i);}
			divpseudo.ajouterTexte(temp+"...");
		}
		
		gagnant.setBorder(0,"","5px 15px 5px 15px");
		gagnant.setBackground("","center","repeat","RGBA(84,25,00,0.9)");
		gagnant.ajouterTexte("Gagnant");
		gagnant.setVisibility(false);
		divpseudo.setBorder(0,"white","5px 15px 5px 15px");
		divpseudo.setBackground("","center","no-repeat","RGBA(20,80,40,0.9)");
		divpseudo.setCursor("pointer");
		divpseudo.setOnMouseDown(function(){
		    infos_joueur(ref.pseudo);
		});
		divpseudo.setOnMouseOut(function(){
		    $("#infos_joueur").hide("slow");
		});
		divjetons.setBorder(0,"white","5px 15px 5px 15px");
		divjetons.setBackground("","center","no-repeat","RGBA(20,80,40,0.9)");
		divcartes.setBorder(0,"white","5px 15px 5px 15px");
		divcartes.setBackground("","center","repeat","RGBA(50,50,50,0.9)");
		div.ajouterFils(gagnant);
		div.ajouterFils(divpseudo);
		div.ajouterFils(divjetons);
		div.ajouterFils(divcartes);
	};
	
	this.donnerCartes=function(h){
		var left;
		if (h==0) {left=5;}
		else {left=30;}
		cartesjoueur[h]=new Carte(left,this.siege);
		divcartes.ajouterFils(cartesjoueur[h]);
		cartesjoueur[h].donner();
	};
	this.jeterCartes=function(){
	    if (cartesjoueur[0]==null || cartesjoueur[1]==null) {
	        divcartes.vider();
	        cartesjoueur[0]=null;
	        cartesjoueur[1]=null;
	        return;
	    }
	    this.cacherCartes();
		
		setTimeout(function(){
			if(ref.getCarte(0)!=null) ref.getCarte(0).jeter();
			if(ref.getCarte(1)!=null) ref.getCarte(1).jeter();
			},500);
	};
	this.montrerCartes=function(val,h){
		cartesjoueur[h].setNum(val);
		cartesjoueur[h].montrer();
	};
	this.cacherCartes=function(){
		cartesjoueur[0].cacher();
		cartesjoueur[1].cacher();
	};
	
	
	this.nouveauTimer=function(){
		bartimer=new Div("bartimer",10,50,50,10);
		bartimer.setBorder(1,"grey","5px 15px 5px 15px");
		
		timer=new Div("timer",10,50,0,0);
		timer.setBorder(0,"","5px 15px 5px 15px");
		timer.setBackground("","center","repeat","RGB("+r+","+v+",0)");
		
		bartimer.ajouterFils(timer);
		div.ajouterFils(bartimer);
		
		this.lancerTimer();
	};
	this.lancerTimer=function(){
		if (r<255){
			r+=5;
			timer.setBackground("","center","repeat","RGB("+r+","+v+",0)");
			w-=dw;
			timer.setWidth(w);
		}
		if(255==r && 0<v)
		{
			v-=5;
			timer.setBackground("","center","repeat","RGB("+r+","+v+",0)");
			w-=dw;
			timer.setWidth(w);
		}
		if(255==r && v==0) { 
		    this.supprimerTimer();
		    return;
		}
		t=setTimeout(function(){ref.lancerTimer();},dt);
	};
	this.supprimerTimer=function(){
		if(bartimer!=null){
		    clearTimeout(t);
			div.supprimerFils(bartimer);
			bartimer=null;
		    r=t=0;
		    v=255;
		    w=50;
		}
	};
	this.estGagnant=function(bool){gagnant.setVisibility(bool);};	
	//------------------------------ Initialisation ------------------------------
	this.initialiser(pseudo,siege);
};
function Slide(top,left){
	//------------------------------ Attributs ------------------------------
	var ref=this;
	var div,plus,moins,tapis,textarea;
	var min,max,value,dvalue;
	
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return div.getDiv();};
	this.getMin=function(){return min;};
	this.getMax=function(){return max;};
	this.getValue=function(){return value;};
	this.getDvalue=function(){return dvalue;};
	this.getMoins=function(){return moins;};
	this.getPlus=function(){return plus;};
	this.getTapis=function(){return tapis;};
	
	this.setValue=function(v){
		if(v<min) value=min;
		else if (max<v) value=max;
		else value=v;
		textarea.setValue(value);
	};
	this.setVisibility=function(bool){div.setVisibility(bool);};
	this.setMinMax=function(newmin,newmax){
		min=value=newmin;
		max=newmax;
		this.setValue(min);
	};
	
	this.initialiser=function(top,left){
		min=max=value=0;
		dvalue=10;
		div=new Div("slide","","",top,left);
		moins=new Input("moins","button",20,20,-25,0);
		plus=new Input("plus","button",20,20,-25,25);
		tapis=new Input("tapis","button",20,50,-25,135);
		textarea=new Input("textarea","text","",75,5,60);
		
		moins.setValue("-");
		plus.setValue("+");
		tapis.setValue("tapis");
		textarea.setValue(min);
		textarea.setColor("black");
		textarea.disabled(true);
		
		plus.setOnClick(function(){
			ref.setValue(ref.getValue()+ref.getDvalue());
		});
		tapis.setOnClick(function(){
			ref.setValue(ref.getMax());
		});
		moins.setOnClick(function(){
			ref.setValue(ref.getValue()-ref.getDvalue());
		});
		
		div.ajouterFils(moins);
		div.ajouterFils(plus);
		div.ajouterFils(textarea);
		div.ajouterFils(tapis);
	};
	
	//------------------------------ Initialisation ------------------------------
	this.initialiser(top,left);
};
function Mise(){
	//------------------------------ Attributs ------------------------------
	var ref=this;
	var div,sousdiv;
	var coordonnees=[
	[-mh/9,-mw/2.85],
	[-mh/5,-mw/5],
	[-mh/5,mw/6],
	[-mh/9,mw/3.15],
	[mh/50,mw/3.15],
	[mh/12,mw/6],
	[mh/12,-mw/5],
	[mh/50,-mw/2.85],
	[0,0]
	];
	var t,l,dt,dl,compteur;
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return div.getDiv();};
	
	this.initialiser=function(){
		div=new Div("div","","",mh/2-50,mw/2);
		sousdiv=[null,null,null,null,null,null,null,null,null];
		dt=[];dl=[];
		t=[];l=[];
		compteur=[0,0,0,0,0,0,0,0,0];
		div.setZIndex(10);
		
		for(var i=0;i<8;i++){
			dt[i]=coordonnees[i][0]/100;
			dl[i]=coordonnees[i][1]/100;
		}
	};
	
	this.poserJetons=function(siege,montant){
		if(montant<=0)	return;
		if (sousdiv[siege]!=null) {
			sousdiv[siege].vider();
			sousdiv[siege]=null;
		}
		var img=new Image();
		
		img.src="Images"+sep()+"jeton.png";
		img.height=25;
		img.width=25;
		var divmontant=new Div("montant"+siege,"","",25,0);
		sousdiv[siege]=new Div("mise"+siege,"","",coordonnees[siege][0],coordonnees[siege][1]);
		t[siege]=coordonnees[siege][0];
		l[siege]=coordonnees[siege][1];
		divmontant.ajouterTexte(montant);
		sousdiv[siege].ajouterElement(img);
		sousdiv[siege].ajouterFils(divmontant);
		div.ajouterFils(sousdiv[siege]);
	};
	this.centrerJetons=function(montant){
		for(var i=0;i<8;i++){
			if(sousdiv[i]!=null)
				this.deplacerJetons(i,true);
		}
		setTimeout(function(){
			ref.poserJetons(8,montant);
		},500);
	};
	this.deplacerJetons=function(siege,sens){
		if(sens){	
			if(compteur[siege]<100){
				compteur[siege]++;
				t[siege]-=dt[siege];
				l[siege]-=dl[siege];
				sousdiv[siege].setTop(t[siege]);
				sousdiv[siege].setLeft(l[siege]);
				setTimeout(function(){ref.deplacerJetons(siege,true);},1);
			}
			else{
				if (sousdiv[siege]!=null) {
					div.supprimerFils(sousdiv[siege]);
					sousdiv[siege]=null;
				}
				compteur[siege]=0;
			}
		}
		else {
			if(compteur[siege]<100){
				compteur[siege]++;
				t[siege]+=dt[siege];
				l[siege]+=dl[siege];
				sousdiv[8].setTop(t[siege]);
				sousdiv[8].setLeft(l[siege]);
				setTimeout(function(){ref.deplacerJetons(siege,false);},1);
			}
			else{
				if (sousdiv[8]!=null) {
					div.supprimerFils(sousdiv[8]);
					sousdiv[8]=null;
				}
				compteur[8]=0;
			}
		}
	};
	
	//------------------------------ Initialisation ------------------------------
	this.initialiser();
};
function Carte(pos,siege){
	//------------------------------ Attributs ------------------------------
	var ref=this;
	var ot,ol,t,l,dt,dl,left,compteur,compteur2;
	var retourne;
	var div;
	var carte;
	
	this.siege;
	this.num;
		
	//------------------------------ Méthodes ------------------------------
	this.getDiv=function(){return div.getDiv();}
	this.setNum=function(newnum){this.num=newnum;};
	this.setCarteSize=function(h,w){carte.height=h;carte.width=w;};
	
	this.initialiser=function(pos,siege){
		this.siege=siege;
		
		left=pos;
		carte=new Image();
		carte.src="Cartes"+sep()+"verso.png";
		carte.height=0;
		carte.width=0;
		div=new Div(this.num,"","",pointorigine[this.siege][0],pointorigine[this.siege][1]);
		div.setZIndex(10);
		div.ajouterElement(carte);
		
		ot=5;
		ol=left;
		t=pointorigine[this.siege][0];
		l=pointorigine[this.siege][1];
		dt=(t-ot)/100;
		dl=(l-ol)/100;
		compteur=0;
		compteur2=0;
		
		retourne=true;
	};
	
	this.donner=function(){
		if(div!=null && compteur<100){
			t-=dt;
			l-=dl;
			div.setTop(t);
			div.setLeft(l);
			compteur++;
			compteur2++;
			if(compteur2%10==0){
				if(carte.height<40) carte.height+=4;
				if(carte.width<20) carte.width+=2;
			}
			setTimeout(function(){ref.donner();},1);
		}
	};
	this.jeter=function(){
		if(div!=null && compteur>0){
			t+=dt;
			l+=dl;
			div.setTop(t);
			div.setLeft(l);
			compteur--;
			compteur2--;
			if(compteur2%10==0){
				carte.height-=4;
				carte.width-=2;
			}
			setTimeout(function(){ref.jeter();},1);
		}
	};
	this.montrer=function(){
		if(div!=null && (this.num==-1 || carte.src=="Cartes"+sep()+this.num+".png")) return;
		if(retourne && carte.width>0){
			carte.width-=1;
			setTimeout(function(){ref.montrer();},1);
			return;
		}
		retourne=false;
		if(!retourne && carte.width<20){
			carte.src="Cartes"+sep()+this.num+".png";
			carte.width+=1;
			setTimeout(function(){ref.montrer();},1);
			return;
		}
	};
	this.cacher=function(){
		if(div!=null && (!retourne && carte.src=="Cartes"+sep()+"verso.png")) return;
		if(!retourne && carte.width>0){
			carte.width-=1;
			setTimeout(function(){ref.cacher();},1);
			return;
		}
		retourne=true;
		if(retourne && carte.width<20){
			carte.src="Cartes"+sep()+"verso.png";
			carte.width+=1;
			setTimeout(function(){ref.cacher();},1);
			return;
		}
	};
	
	//------------------------------ Initialisation ------------------------------
	this.initialiser(pos,siege);
};