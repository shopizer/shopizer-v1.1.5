<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/jquery.rating.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.rating.pack.js"></script>

<script type='text/javascript'>

function limitText(limitField, limitCount, textToModify, limitNum) {
	if (document.getElementById(limitField).value.length > limitNum) {
		document.getElementById(limitField).value = document.getElementById(limitField).value.substring(0, limitNum);
	} else {
		limitCount = limitNum - document.getElementById(limitField).value.length;
		newtext = '<s:text name="label.generic.youhave" /> <b>' +  limitCount + ' </b><s:text name="label.generic.characters" />';
		document.getElementById(textToModify).innerHTML=newtext;
	}
}

function submitReview() {


	if(document.getElementById('rating').value==0) {
		document.getElementById('rating').value = 1;
	}

	 
	var field = new LiveValidation( 'reviewText', {validMessage: " ",onlyOnSubmit: true } );
	field.add( Validate.Presence,{failureMessage:'<s:text name="error.messag.review"/>'});
	
	var areAllValid = LiveValidation.massValidate( [field] );
	
	if(areAllValid) {

		document.reviewForm.submit();
	}
}

</script>



<style type="text/css"> 

.formcontent fieldset {
	border:0;
	position: relative;
	left: 0px;
	top:  0px;
}


</style> 


<div id="reviewform" class="formcontent">
        

<fieldset>

<legend><s:text name="label.storefront.reviews.createreview"/></legend><h3><s:text name="label.storefront.reviews.createreview"/></h3>


<s:if test="principal.remoteUser==null">
	<br/><br/>
	<s:text name="message.review.loggedin" />

</s:if>
<s:else>






							<s:form id="reviewForm" enctype="multipart/form-data" action="createReview" method="post" theme="simple"><br/>
							<s:hidden name="product.productId" value="%{product.productId}"/>
							<s:hidden id="rating" name="rating" value="3" />
							<script> 
								jQuery(function(){ 
									jQuery('.auto-submit-star').rating({ 
										callback: function(value, link){ 
											document.getElementById('rating').value=value;
										} 
									}); 
								}); 
							</script> 
							
							
							<table>
								<tr>	
									<td colspan="2">
										<b><s:property value="product.name" /></b><br/><br/>
										<br><img src="<s:property value="product.largeImagePath" />" /><br><br>
									</td>

								</tr>
								<tr>
									<td><s:text name="label.generic.review" /></td>
									<td>
										<div id="review-stars" style="background-color:#ffffff;width:115px;">&nbsp;
											<input name="star" type="radio" class="auto-submit-star" value="1"/> 
											<input name="star" type="radio" class="auto-submit-star" value="2"/> 
											<input name="star" type="radio" class="auto-submit-star" value="3" checked="checked"/> 
											<input name="star" type="radio" class="auto-submit-star" value="4"/>
											<input name="star" type="radio" class="auto-submit-star" value="5"/>
										</div>
									</td>
								</tr>

								<tr>

									<td valign="top" colspan="2">&nbsp;</td>
								</tr>
								<tr>

									<td valign="top">
										<s:text name="label.generic.review" />
									</td>
									<td valign="top">
										<s:textarea name="reviewText" id="reviewText" value="" cols="60" rows="10" onkeydown="javascript:limitText('reviewText',1000,'charsCount',1000);" onkeyup="javascript:limitText('reviewText',1000,'charsCount',1000);"/>
										<br><div id="charsCount"><s:text name="label.generic.youhave" /> <b>1000</b> <s:text name="label.generic.characters" /></div>
									</td>
								</tr>

							  </table>

							  <table width="555">

								<tr>


									<td colspan="2" align="right">
								

										<div class="button-right" style="float:right;">
											<a href="javascript:submitReview();" class="button-t">
												<div class="button-left"><div class="button-right"><s:text name="button.label.submit2" /></div></div>
											</a>
										</div>
									</td>
								</tr>

							 </table>
							  
							  </s:form>
</s:else>

</fieldset>

</div>