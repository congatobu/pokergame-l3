function PopupWindow(source, strWindowToOpen){ 
document.getElementById('creation').visibility=true;
	var strWindowFeatures = "toolbar=no,resize=no,titlebar=no,"; 
	strWindowFeatures = strWindowFeatures + "menubar=no,width=413,height=299,maximize=null"; 
	window.open(strWindowToOpen, '', strWindowFeatures); 
} 
