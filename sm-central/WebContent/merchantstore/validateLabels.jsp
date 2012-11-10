
<%@taglib prefix="s" uri="/struts-tags" %>
<script type='text/javascript'>

function validate() {



	var title = new LiveValidation('label.title', {validMessage: " ",onlyOnSubmit: true}); 
      title.add( Validate.Exclusion, { within: [ ' ' ], partialMatch: true, failureMessage:'<s:text name="messages.required.nowhitespace"/>'} );
	title.add(Validate.Presence,{failureMessage:'<s:text name="error.message.storefront.portletidrequired"/>'} ); 
	var order = new LiveValidation('label.sortOrder', {validMessage: " ",onlyOnSubmit: true}); 
	order.add(Validate.Numericality,{onlyInteger: true, failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} ); 
	order.add(Validate.Presence,{failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} );
      order.add( Validate.Exclusion, { within: [ ' ' ], partialMatch: true, failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} ); 


	var areAllValid = LiveValidation.massValidate( [title,order] );

	return areAllValid;



} 
</script> 