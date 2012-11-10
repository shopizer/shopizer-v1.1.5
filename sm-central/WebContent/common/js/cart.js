
var cartLineCount = 0;
var lock = 0;


// Manages error messages from adding items in the shopping cart
function setErrorMessage(message) {
	
	jQuery('#ajaxMessage').html(message);
    jQuery('#ajaxMessage').css('display', 'block'); 

}

function initCart() {


    jQuery('.ajaxMessage').html('');
    jQuery('#ajaxMessage').css('display', 'none'); 
	jQuery('.duenow').remove();
	jQuery('.subtotal').remove();
	jQuery('.tax').remove();
	jQuery('.shipping').remove();
	jQuery('.recursive').remove();
	jQuery('.total').remove();

}



function removeFromCart(lineId) {
	AddProduct.removeProduct(lineId);
	calculate();
    stripe();
	return false;
}

function removeAttribute(productId,lineId) {
	AddProduct.removeAttributes(productId,lineId,resetProduct);

}

function resetProduct(data) {


	jQuery('#price-'+ data.lineId).val(data.priceText);
	calculate();
    stripe();


}


function resetShipping() {



	jQuery('#shippingMethodId').val('');
	jQuery('#removeShipping').hide();
    jQuery('#addShipping').show();

}

function addKeyPress() {
		//ie 6 does not like 2 input fields in the same class div invoking the same event, so i switched to blur event instead of change
		jQuery('.quantity input').bind('keypress', function(event) {

    			if (event.charCode && (event.charCode < 48 || event.charCode > 57)) {
     			 	event.preventDefault();
    			}

  		}).blur(function() {
      		//jQuery('#shippingMethodId').val('');
      		return false;//prevent submission
		});

		jQuery('.price input').bind('keypress', function(event) {

    			if (event.charCode && (event.charCode < 48 || event.charCode > 57)) {
     			 	event.preventDefault();
    			}

  		}).change(function() {
			//jQuery('#shippingMethodId').val('');
			calculate();
			return false;//prevent submission
		});


}


function addBindings() {

  //var count = 0;

  jQuery('#cart tbody tr').each(function() {

   var lineId = jQuery('.item input',this).val();

    if(! jQuery("#remove_" + lineId).length > 0 ) {
		    	$deleteButton = jQuery('<img />').attr({
      	'width': '16',
      	'height': '16',
      	'src': '../common/img/icon_delete.gif',
      	'alt': 'X',
      	'title': 'remove from cart',
		'id': 'remove_' + lineId,
      	'class': 'clickable'
    	}).click(function() {//REMOVE PRODUCT
      		//var id = jQuery('.item input',this).val();
            	var id = jQuery(this).parents('tr').find('.item input').val();
      		var pid = '#productid-' + id;
			var productId = jQuery(pid).val();
			jQuery(this).parents('tr').remove();
			removeFromCart(id);



    	});

    	jQuery('<td></td>').insertAfter(jQuery('td:nth-child(2)', this)).append($deleteButton);

    }//end if


    jQuery('#removeOptions-'+lineId).bind('click',function() {//REMOVE OPTIONS

		//get line id
		var id = jQuery(this).parents('tr').find('.item input').val();
		var pid = '#productid-' + id;
		var pname = '#productname-' + id;
		var name = jQuery(pname).val();
		var productId = jQuery(pid).val();
		jQuery(this).parents('tr').find('#productText').html(name);
		jQuery(this).parents('tr').find('#addOptionsLink').show();
		jQuery(this).parents('tr').find('#removeOptionsLink').hide();
		removeAttribute(productId,id);
		return false;

      });


   });



}


//calculates the shopping cart
function calculate() {
    //alert('calculate');
    var totalQuantity = 0;
    var totalCost = 0;
    var count = 0;
    lock = 0;
    var items=new Array()
    jQuery('#cart tbody tr').each(function() {



      	var quantity = jQuery('.quantity input', this).val();
		if(isNaN(quantity)) {
			alert(invalidQuantity);
			jQuery('.quantity input', this).val(0);
		}
		quantity = isNaN(quantity) ? 0 : parseInt(quantity);

		var item = new Object();

		//item lineId
		var id=jQuery('.item input', this).val();
		jQuery('#pmessage-'+ id).html('');
		item.lineId = id;
		//item productId
		var pid = '#productid-' + id;
		item.productId = jQuery(pid, this).val();

		item.priceText=jQuery('.price input', this).val();
		item.productQuantity = quantity;
		items[count] = item;
		count++;
    });


    //submit
    if(items.length>0) {
    		var shipping = new Object();

		var shippingMethodId = jQuery('#shippingMethodId').val();
		if(shippingMethodId) {
			shipping.shippingMethodId = shippingMethodId;
    	}
	AddProduct.calculate(items,shipping,setCalculate);
    } else {
    	initCart();
    }

  }

function setCalculate(data) {

	initCart();



	//init price lines


	if(data.errorMessage) {
		setErrorMessage(data.errorMessage);
		return false;
	}

	var opa = data.orderProducts;
	//var hasShipping = 0;
	//check if validation Error

		for (i=0; i<opa.length; i++) {

			if(opa[i].priceErrorMessage) {
				alert(opa[i].priceErrorMessage);
				lock = 2;
				var lineId = opa[i].lineId;


				//set error message and price to 0

				jQuery('#price-'+ opa[i].lineId).val(0);
				jQuery('#pmessage-'+ opa[i].lineId).html('<b><font color=\"red\">*</font></b>');

				var cost = opa[i].costText;
				jQuery('.cost-'+ opa[i].lineId).text(cost);

			}

    	}



	    //display lines
	    for (i=0; i<opa.length; i++) {

			jQuery('#price-'+ opa[i].lineId).val(opa[i].priceText);
			jQuery('.cost-'+ opa[i].lineId).text(opa[i].costText);
			jQuery('#quantity-'+ opa[i].lineId).val(opa[i].productQuantity);
    	}

		//due now
		var dueNow = data.otherDueNowAmounts;
		if(dueNow) {
			for (i=0; i<dueNow.length; i++) {
				var dueNowLine = '<tr class=\"duenow\"><td></td><td colspan=\"3\" class=\"desc\">'+dueNow[i].text+'</td><td class=\"cost\">'+dueNow[i].costFormated+'</td></tr>';
				jQuery(dueNowLine).insertBefore(".footerspace");
    			}

		}

		//due now credits
		var credits = data.dueNowCredits;
		if(credits) {
			for (i=0; i<credits.length; i++) {
				var creditLine = '<tr class=\"duenow\"><td></td><td colspan=\"3\" class=\"desc\">'+credits[i].text+'</td><td class=\"cost\"><font color=\"red\">('+credits[i].costFormated+')</font></td></tr>';
				jQuery(creditLine).insertBefore(".footerspace");
    			}

		}

		//subtotal
		var subTotal = '<tr class=\"subtotal\"><td>'+subtotalText+'</td><td colspan=\"3\" class=\"desc\"></td><td class=\"cost\">'+data.oneTimeSubTotalText+'</td></tr>';
		jQuery(subTotal).insertBefore(".footerspace");

		//shipping
		if(data.shipping) {
				var methodId = '';
				var method = '';
				var addShippingDivStyle = 'style=\"display:block\"';
				var removeShippingDivStyle = 'style=\"display:none\"';
				var shippingMessage = shippingText;

				if(data.shippingLine  && data.shippingLine.shippingMethod) {

					shippingMessage = '<b>'+data.shippingLine.shippingMethod+'</b>';
				}
				if(data.shippingLine && data.shippingLine.shippingMethodId) {

					//display remove shipping

					addShippingDivStyle = 'style=\"display:none\"';
					removeShippingDivStyle = 'style=\"display:block\"';
					methodId = data.shippingLine.shippingMethodId;

				} else {
					//display add shipping
					addShippingDivStyle = 'style=\"display:block\"';
					removeShippingDivStyle = 'style=\"display:none\"';
				}
				var addShippingUrl = '<div id=\"addShipping\" ' +addShippingDivStyle+ '><a href=\"' + shippingUrl + '?placeValuesBeforeTB_=savedValues&TB_iframe=true&height=300&width=400&modal=true\" title=\"'+ shippingText +'\" class=\"thickbox\">'+shippingText+'</a></div>';
				var removeShippingUrl = '<div id=\"removeShipping\" '+ removeShippingDivStyle + '><a href=\"\" id=\"removeShippingUrl\">'+removeShippingText+'</a></div>';
				var shippingLine = '<tr class=\"shipping\"><td>'+shippingMessage+'</td><td colspan=\"3\" class=\"desc\"><input type=\"hidden\" id=\"shipping-method\" name=\"shipping-method\" value=\"'+method+'\"><input type=\"hidden\" id=\"shippingMethodId\" name=\"shippingMethodId\" value=\"'+methodId+'\"><input type=\"hidden\" name=\"shipping-cost\" value=\"0\">'+addShippingUrl+ ' ' + removeShippingUrl+ '</td><td class=\"cost\"><div id=\"shipping-cost-text\">'+data.shippingTotalText+'</div></td></tr>';
				jQuery(shippingLine).insertBefore(".footerspace");

				jQuery('#removeShippingUrl').bind('click',function() {//REMOVE SHIPPING

						resetShipping();
						calculate();
						return false;

      			});

		}

		//tax
		var tax = data.taxAmounts;
		if(tax) {
			for (i=0; i<tax.length; i++) {
				var taxLine = '';
				if(i==0) {
					taxLine = '<tr class=\"tax\"><td>'+taxText+'</td><td colspan=\"3\" class=\"desc\">'+tax[i].text+'</td><td class=\"cost\">'+tax[i].costFormated+'</td></tr>';
				} else {
					taxLine = '<tr class=\"tax\"><td></td><td colspan=\"3\" class=\"desc\">'+tax[i].text+'</td><td class=\"cost\">'+tax[i].costFormated+'</td></tr>';
				}
				jQuery(taxLine).insertBefore(".footerspace");
    		}
		}

		//total
		var total = '<tr class=\"total\"><td>'+totalText+'</td><td colspan=\"3\" class=\"desc\"></td><td class=\"cost\" nowrap>'+data.totalText+'</td></tr>';
		jQuery(total).insertBefore(".footerspace");

		//recursive
		var recur = data.recursiveAmounts;
		if(recur) {
			var recursiveTitle = '<tr class=\"recursive\"><td></td><td colspan=\"3\" class=\"desc\"></td><td class=\"cost\"></td></tr><tr class=\"recursive\"><td>'+recursive+'</td><td colspan=\"3\" class=\"desc\"></td><td class=\"cost\"></td></tr>';
			jQuery(recursiveTitle).insertBefore(".footerspace");
			for (i=0; i<recur.length; i++) {

				var recursiveLine = '<tr class=\"recursive\"><td></td><td colspan=\"3\" class=\"desc\">'+recur[i].text+'</td><td class=\"cost\">'+recur[i].costFormated+'</td></tr>';

				jQuery(recursiveLine).insertBefore(".footerspace");
			}
		}

		//recursive credits
		var recurCredits = data.recursiveCredits;
		if(recurCredits ) {
			for (i=0; i<recurCredits.length; i++) {

				var recursiveCreditLine = '<tr class=\"recursive\"><td></td><td colspan=\"3\" class=\"desc\">'+recurCredits[i].text+'</td><td class=\"cost\"><font color=\"red\">('+recurCredits[i].costFormated+')</font></td></tr>';

				jQuery(recursiveCreditLine).insertBefore(".footerspace");
			}
		}

		tb_init('a.thickbox,area.thickbox,input.thickbox');



}

function stripe() {
    jQuery('#cart tbody tr:visible:even').removeClass('odd').addClass('even');
    jQuery('#cart tbody tr:visible:odd').removeClass('even').addClass('odd');
}

function setShippingModule(moduleId) {

	jQuery('#shippingMethodId').val(moduleId);
	jQuery('#removeShipping').show();
    	jQuery('#addShipping').hide();

	calculate();
}


function setAttributes(data) {




	//initCart();

	if(data.errorMessage) {
		setErrorMessage(data.errorMessage);
		return false;
	}

	var lineId = data.lineId;
	var search = "cartlineid-" + lineId;

	var pname = '#productname-' + lineId;
	var name = jQuery(pname).val();

	var attrs = '<input type=\"hidden\" id="attributes-'+ lineId + '\" name=\"attributes-\"'+ lineId  + '\" value=\"OPA-'+ lineId +'\">';
	var cartId = '#cartlineid-' + lineId;
	jQuery(cartId).parents('tr').find('#productText').html(name + " " + attrs + "<br>" + data.attributesLine);

	jQuery('#price-'+ lineId).val(data.priceText);
	var cost = data.priceText * data.productQuantity;
	jQuery('.cost-'+ data.lineId).text('$' + cost.toFixed(2));

	jQuery(cartId).parents('tr').find('#addOptionsLink').hide();
	jQuery(cartId).parents('tr').find('#removeOptionsLink').show();

	calculate();


}

function addCartLine(productId,productName) {


   //initCart();


   if(document.getElementById('customer.customerId').value==0) {
		setErrorMessage(customerRequiredText);
		return false;
   }



   var productexist=0;
   jQuery('#cart tbody tr').each(function() {


		//get the product id
		var id=jQuery('.item input', this).val();
		var pid = '#productid-' + id;
		var product = jQuery(pid, this).val();
		if(product==productId) {
			//if no attributes
			var attrid = '#attributes-' + id;
			var value = jQuery(attrid, this).val();
			if(value) {
				//do nothing for now

			} else {
				var quantity = parseInt(jQuery('.quantity input', this).val());
				document.getElementById('quantity-' + id).value=quantity+1;

				calculate();
				productexist=1;
				return;

			}
		}

	});

	if(productexist==0){
		var newLineCount = cartLineCount+1;
		AddProduct.addProduct(productId,newLineCount ,setCartLine);
	}

}

//add a line to the shopping cart
function setCartLine(data) {



	if(data.errorMessage) {
		setErrorMessage(data.errorMessage);
		return false;
	}

	cartLineCount ++;


	//properties
	var prop = "";
	if(data.attributes) {
		var addPropUrl = "<div id=\"addOptionsLink\"><a href=\"" + attributesUrl + data.productId + "&lineId=" +data.lineId+ "&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=300&width=400&modal=true\" title=\""+ attributesText +"\" class=\"thickbox\">"+attributesText+"</a></div>";
		var removePropUrl = "<div id=\"removeOptionsLink\" style=\"display:none\"><a href=\"\" id=\"removeOptions-"+data.lineId+"\">"+removeAttributesText+"</a></div>";
		prop="<br>" + addPropUrl + " " + removePropUrl;
	}
	opening = "<tr><td class=\"item\"><input type=\"hidden\" name =\"cartlineid-"+data.lineId+"\" id=\"cartlineid-" +data.lineId+ "\" value=\"" + data.lineId+ "\"><input type=\"hidden\" name=\"ids["+data.lineId+ "]\" value=\""+data.lineId+"\"> <input type=\"hidden\" name=\"productid-"+data.lineId+"\" id=\"productid-" +data.lineId+ "\" value=\"" + data.productId + "\"><input type=\"hidden\" name=\"productname-"+data.lineId+"\" id=\"productname-" +data.lineId+ "\" value=\"" + data.productName + "\"><div id=\"productText\">" + data.productName + " </div> " + prop + "</td><td class=\"quantity\">";
	tfield="<div id=\"qmessage-"+data.lineId+"\"></div><input type=\"text\" name=\"quantity-" +data.lineId+ "\" value=\"1\" id=\"quantity-" +data.lineId+ "\" maxlength=\"10\" />";
	closing = "</td><td class=\"price\"><div id=\"pmessage-"+data.lineId+"\"></div><input type=\"text\" name=\"price-" +data.lineId+ "\" value=\"" + data.priceText + "\" id=\"price-" +data.lineId+ "\" size=\"5\" /></td><td align=\"right\" style=\"text-align: right;\" class=\"cost-"+data.lineId+"\">" + data.priceFormated + "</td></tr>";
	line = opening + tfield + closing;
    	jQuery(line).appendTo( "#cart" );
	//alert(line);
	stripe();
	addKeyPress();
	addBindings();
	calculate();
	tb_init('a.thickbox,area.thickbox,input.thickbox');
	return false;
}



/***************************************
   init shopping cart
-------------------------------------- */

jQuery(document).ready(function() {

  lock = 0;
  cartLineCount = 0;

  stripe();

  jQuery('#recalculate').hide();


  jQuery('<th>&nbsp;</th>').insertAfter('#cart thead th:nth-child(2)');


  var calculateLines = function() {
    var count = 0;

  	jQuery('#cart tbody tr').each(function() {

    	count++;

   	});
	return count;
  };




  //invoked during display
  cartLineCount  = calculateLines();
  addBindings();
  calculate();
  addKeyPress();


  jQuery("form:first")
	.submit(function(){
		if(lock==0) {
			lock=0;
			calculate();
			return false;
		} else {
			return true;
		}
	});//prevent form submission when enter key is pressed
  jQuery("#postitems").click(function(){
		if(lock==0) {//no error
			lock =1;//ready to submit
			jQuery("form:first").submit();
		}
   }); //submit the form only when submit is pressed



  jQuery('<td>&nbsp;</td>').insertAfter('#cart tfoot td:nth-child(2)');






});