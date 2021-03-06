var mainActiveId='home';

function locationUrl(url,activeId){
	if(mainActiveId != null && mainActiveId != "" && activeId != null && activeId != ""){
		$("#"+mainActiveId).removeAttr("class");
		$("#"+activeId).attr("class", "active");
		mainActiveId = activeId;
	}
	goUrl(url,null);
}

var xmlhttp = new getXMLObject();
function goUrl(url,params) {
	if(xmlhttp) {
		xmlhttp.open("POST",url,true);
		xmlhttp.onreadystatechange = handleServerResponse;
		xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
		xmlhttp.send(params);
	}
}

//XML OBJECT
function getXMLObject() {
	var xmlHttp = false;
	try {
		xmlHttp = new ActiveXObject("Msxml2.XMLHTTP") // For Old Microsoft
														// Browsers
	} catch (e) {
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP") // For Microsoft
																// IE 6.0+
		} catch (e2) {
			xmlHttp = false // No Browser accepts the XMLHTTP Object then false
		}
	}
	if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
		xmlHttp = new XMLHttpRequest(); // For Mozilla, Opera Browsers
	}
	return xmlHttp; // Mandatory Statement returning the ajax object created
}

function handleServerResponse() {
	if (xmlhttp.readyState == 4) {
		//document.getElementById("mainSection").innerHTML =xmlhttp.responseText;
		var text = xmlhttp.responseText;
		if(text.indexOf("<title>Favorites error Page</title>") >= 0){
			window.location.href="/404.html";
		}else{
			$("#content").html(xmlhttp.responseText);
		}
	}
}
