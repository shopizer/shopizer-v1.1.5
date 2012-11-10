	
	function getLoginInfo(xml) {
		var name = '';
		jQuery(xml).find('customer').each(function(){
			var customerFirstName = jQuery(this).find('customerFirstname').text();
			var customerLastname = jQuery(this).find('customerLastname').text();
			name = customerFirstName + ' ' + customerLastname;
		});
		return name;
	
	}
	
	function fillBillingInfo(xml){
		jQuery(xml).find('customer').each(function(){

			//shipping
			var customerShippingCountry = jQuery(this).find('customerCountryId').text();
			jQuery('#country2').val(customerShippingCountry);

			//billing
			var customerCountry = jQuery(this).find('customerBillingCountryId').text();
			jQuery('#country').val(customerCountry);


			var customerId = jQuery(this).find('customerId').text();
			jQuery('input[name="customerId"]').val(customerId);

			var customerFirstName = jQuery(this).find('customerFirstname').text();
			jQuery('input[name="customer.customerFirstname"]').val(customerFirstName);


			var customerEmailAddress = jQuery(this).find('customerEmailAddress').text();
			jQuery('input[name="customer.customerEmailAddress"]').val(customerEmailAddress);

			var customerLastname = jQuery(this).find('customerLastname').text();
			jQuery('input[name="customer.customerLastname"]').val(customerLastname);

			var customerCompany = jQuery(this).find('customerCompany').text();
			jQuery('input[name="customer.customerCompany"]').val(customerCompany);


			var customerBillingStreetAddress = jQuery(this).find('customerBillingStreetAddress').text();
			jQuery('input[name="customer.customerBillingStreetAddress"]').val(customerBillingStreetAddress);

			var customerStreetAddress = jQuery(this).find('customerStreetAddress').text();
			jQuery('input[name="customer.customerStreetAddress"]').val(customerStreetAddress);

			var customerBillingCity = jQuery(this).find('customerBillingCity').text();
			jQuery('input[name="customer.customerBillingCity"]').val(customerBillingCity);

			var customerCity = jQuery(this).find('customerCity').text();
			jQuery('input[name="customer.customerCity"]').val(customerCity);

			var customerPostalCode = jQuery(this).find('customerPostalCode').text();
			jQuery('input[name="customer.customerPostalCode"]').val(customerPostalCode);

			var customerBillingPostalCode = jQuery(this).find('customerBillingPostalCode').text();
			jQuery('input[name="customer.customerBillingPostalCode"]').val(customerBillingPostalCode);

			var customerTelephone = jQuery(this).find('customerTelephone').text();
			jQuery('input[name="customer.customerTelephone"]').val(customerTelephone);

			var customerBillingFirstName = jQuery(this).find('customerBillingFirstName').text();
			jQuery('input[name="customer.customerBillingFirstName"]').val(customerBillingFirstName);

			var customerBillingLastname = jQuery(this).find('customerBillingLastName').text();
			jQuery('input[name="customer.customerBillingLastName"]').val(customerBillingLastname);


			if(document.getElementById('shippingCountryState')) {

				if(jQuery('input[name="shippingCountryState"]').val()!='lock') {
					updateShippingZonesCombo();
				}
			}

			if(jQuery('input[name="formstate2"]').val()=='list') {

				var customerShippingZone = jQuery(this).find('customerZoneId').text();
				jQuery('#states2').val(customerShippingZone);

				statesfielddefaultvalue = customerShippingZone;

			} else {

				var customerShippingZoneText = jQuery(this).find('customerState').text();
				jQuery('input[name="customer.customerState"]').val(customerShippingZoneText);

				statesfielddefaultvalue = customerShippingZoneText;

			}

			if(document.getElementById('shippingCountryState')) {

				if(jQuery('input[name="shippingCountryState"]').val()!='lock') {

					updateShippingZonesCombo();
				}

			}


			updateBillingZonesCombo();


			if(jQuery('input[name="formstate"]').val()=='list') {

				var customerBillingZone = jQuery(this).find('customerBillingZoneId').text();
				jQuery('#states').val(customerBillingZone);
				states2fielddefaultvalue = customerBillingZone;

			} else {

				var customerBillingZoneText = jQuery(this).find('customerBillingState').text();
				//alert('text B ' + customerBillingZoneText);
				jQuery('input[name="customer.customerBillingState"]').val(customerBillingZoneText);
				states2fielddefaultvalue = customerBillingZoneText;


			}

			updateBillingZonesCombo();



		});
	}