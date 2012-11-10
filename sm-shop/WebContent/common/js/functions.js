/** synchronize cart cookie **/

function setShoppingCartCookie(key, data) {

			if(data != null && data.jsonShoppingCart!=null) {
				jQuery.cookie(key,data.jsonShoppingCart, { expires: 365,path: '/'});
			} else {
				if(jQuery.cookie(key)!=null) {
					jQuery.cookie(key,null,{ path: '/'});
				}
			}
}

/** product price **/

function setPrice() {

	var theForm = document.attributes;

	var options = getAttributes(theForm);

	if(options && options.length>0) {

		var id = document.getElementById("product.productId").value;
		Catalog.setPrice(options,id,setPriceCallback);

	}

}

/** options / attributes **/

function getAttributes(theForm) {
	//gather all options
	
	var products = new Array();

	var count = 0;

	for(i=0; i<theForm.elements.length; i++){

		if(theForm.elements[i].type == "button") {
			continue;
		}
		if(theForm.elements[i].type == "text" || theForm.elements[i].type == "textarea"){
			
			if(theForm.elements[i].value!='') {
				var attr = new Object();
				attr.name = theForm.elements[i].value;
				attr.value = theForm.elements[i].value;
				attr.stringValue = true;
				attr.textValue = value;
				products[count] = attr;
				count++;
			}
						
		}else if(theForm.elements[i].type == "checkbox"){
			if(theForm.elements[i].checked) {
				var attr = new Object();
				attr.name = theForm.elements[i].value;
				attr.value = theForm.elements[i].value;
				products[count] = attr;
				count++;
			}

		}else if(theForm.elements[i].type == "select-one"){
			var attr = new Object();
			attr.name = theForm.elements[i].value;
			attr.value = theForm.elements[i].options[theForm.elements[i].selectedIndex].value;
			products[count] = attr;
			count++;

		} else if(theForm.elements[i].type == "radio") {
			var radios = document.getElementsByName(theForm.elements[i].name);

			if(theForm.elements[i].checked) {
				var attr = new Object();
				attr.name = theForm.elements[i].value;
				attr.value = theForm.elements[i].value;
				products[count] = attr;
				count++;
			}
		}
	}

	return products;

}

function setPriceCallback(data) {
	if(data!=null && data!='') {
		document.getElementById('price').innerHTML=data;
	}

}



function submitFORM(id) {
	var formObj = document.getElementById(id);
	formObj.submit();
}

function toggleTAB(num) {

	for ( var i=0; i<tabCount; i++ ) {
		var tabObj = document.getElementById("tab_"+i);
		if(tabObj) {
			var contentObj = document.getElementById("content_"+i);
			if ( i == num ) {
				tabObj.className = "tab-box tab-selected";
				contentObj.style.display = "block";
			} else {
				tabObj.className = "tab-box";
				contentObj.style.display = "none";
			}
		}
	}
}



function whichBrs() {
var agt=navigator.userAgent.toLowerCase();
if (agt.indexOf("opera") != -1) return 'Opera';
if (agt.indexOf("staroffice") != -1) return 'Star Office';
if (agt.indexOf("webtv") != -1) return 'WebTV';
if (agt.indexOf("beonex") != -1) return 'Beonex';
if (agt.indexOf("chimera") != -1) return 'Chimera';
if (agt.indexOf("netpositive") != -1) return 'NetPositive';
if (agt.indexOf("phoenix") != -1) return 'Phoenix';
if (agt.indexOf("firefox") != -1) return 'Firefox';
if (agt.indexOf("safari") != -1) return 'Safari';
if (agt.indexOf("skipstone") != -1) return 'SkipStone';
if (agt.indexOf("msie") != -1) return 'Internet Explorer';
if (agt.indexOf("netscape") != -1) return 'Netscape';
if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla';
if (agt.indexOf('\/') != -1) {
if (agt.substr(0,agt.indexOf('\/')) != 'mozilla') {
return navigator.userAgent.substr(0,agt.indexOf('\/'));}
else return 'Netscape';} else if (agt.indexOf(' ') != -1)
return navigator.userAgent.substr(0,agt.indexOf(' '));
else return navigator.userAgent;
}

var browser = whichBrs();

if ( browser == "Firefox" ) {
	document.write("<style>");
	document.write(".input-box2 { padding-top: 6px; height: 20px; }");
	document.write(".login-box2 { padding-top: 3px; height: 17px; }");
	document.write(".qty-box2 { padding-top: 3px; height: 17px; }");
	document.write("</style>");
}












