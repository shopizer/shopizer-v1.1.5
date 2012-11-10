<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ page import = "com.salesmanager.core.util.ReferenceUtil" %>


<script type='text/javascript'>
//<![CDATA[ 


<%
MerchantStore store = (MerchantStore)request.getSession().getAttribute("STORE");
%>


/** shopping cart **/
jQuery(document).ready(function(){

	
	/** add to cart init **/
	jQuery("div.addToCart").click(function() {
		jQuery('#ajaxMessage').css('display','none');
		jQuery.blockUI(('<h1><s:text name="catalog.addingtocart" /></h1>'));
		var qty = jQuery('#productQuantity',this).val();
		if(!qty) {
			qty = 1;
		}
		if(qty==0) {
			qty = 1;
		}
		if(isNaN(qty)) {
			jQuery.unblockUI();
			return;
		}
		if(qty>10000) {
			jQuery.unblockUI();
			return;
		}
		var productId = jQuery('#productId',this).val();

		var theForm = document.attributes;
		if(theForm) {
			var theForm = document.attributes;
			var options = getAttributes(theForm);
			ShoppingCart.addProductWithAttributes(productId,qty,options,handleCart);
		} else {
			ShoppingCart.addProductNoAttributes(productId,qty,handleCart);
		}
		handleCart(null);
	});

	/** reset password box init **/
	jQuery("#resetPassword").fancybox({
		'titlePosition'		: 'inside',
		'transitionIn'		: 'none',
		'transitionOut'		: 'none'
	});
});


function removeProduct(productId) {
	jQuery('#ajaxMessage').css("display","none");
	jQuery.blockUI(('<h1><s:text name="label.generic.wait" /></h1>'));
	ShoppingCart.removeProduct(productId,handleCart);
}

function handleCart(data) {
	if(data) {
		if(data.errorMessage!=null) {
			document.getElementById('ajaxMessage').innerHTML=data.errorMessage;
			document.getElementById('ajaxMessage').style.display='block';
		} else {
			//get products
			var products = data.products;//cart

			//init default values;
			document.getElementById('cart-lines').innerHTML='';
			document.getElementById('button-right').style.display='none';
			document.getElementById('total-price').style.display='none';
			document.getElementById('checkout-text').innerHTML='<font class=\"checkout-item\"><s:text name="catalog.cart.empty" /></font></div>';


			if(products && products.length>0) {

				var cartElements = '<div class=\"cart1\"></div><div class=\"cart2\">';
				count = 1;
				for (i=0; i<products.length; i++) {
					if(count==1) {
						cartElements = cartElements + '<div class=\"cart-line cart-first\">';
					} else if(count<products.length) {
						cartElements = cartElements + '<div class=\"cart-line\">';
					} else {
						cartElements = cartElements + '<div class=\"cart-line cart-last\">';
					}
					if(products[i].image!=null && products[i].image!='') {
						cartElements = cartElements + '<div class=\"cart-product\"><img src=\"' + products[i].image  + '\" width=\"<s:property value="#session.STORECONFIGURATION['smallimagewidth']" />\" height=\"<s:property value="#session.STORECONFIGURATION['smallimageheight']" />\" border=\"0\"></div>';
					} else {
						cartElements = cartElements + '<div class=\"cart-product\"></div>';
					}
					cartElements = cartElements + '<div class=\"cart-info\"><div class="cart-name">'+ products[i].productName +'</div>';
					cartElements = cartElements + '<div class=\"cart-price\">'+ products[i].priceText +'</div>';
					cartElements = cartElements + '<div class=\"cart-qty\"><s:text name="label.generic.qty" /> '+ products[i].quantity +'</div>';
					cartElements = cartElements + '<div class=\"cart-instock\"><img src=\"<%=request.getContextPath()%>/common/img/icon_delete.gif\"  border=\"0\" onClick=\"removeProduct(' + products[i].productId +')\"></div>';
					cartElements = cartElements + '</div></div>';
					count++;
				}
				cartElements = cartElements + '</div><div class=\"cart3\"></div>';
				document.getElementById('cart-lines').innerHTML=cartElements;

				var item = '';
				if(data.quantity>1) {
					item = '<s:text name="catalog.items" />';
				} else {
					item = '<s:text name="catalog.item" />';
				}

				document.getElementById('checkout-text').innerHTML='<font class=\"checkout-value\">' + data.quantity + '</font> <font class=\"checkout-item\">' + item + '</font><br><s:text name="catalog.incart" />';
				document.getElementById('button-right').style.display='block';
				document.getElementById('total-price').innerHTML='<span class=\"total-price1\"></span><span class=\"total-price2\"><font class=\"total-label\"><s:text name="label.generic.total" /></font>&nbsp;<font class=\"price-label\">'+ data.total +'&nbsp;<s:property value="#request.STORE.currency"/></font></span><span class=\"total-price3\"></span>';
				document.getElementById('total-price').style.display='block';
			}
			setShoppingCartCookie(COOKIEKEY,data);
		}
	}
	jQuery.unblockUI();
}

//]]> 
</script>