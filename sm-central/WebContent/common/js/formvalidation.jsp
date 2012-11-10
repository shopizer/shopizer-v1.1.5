
<script type='text/javascript'>

var submitted = false;
var error = false;


function IsNumeric(sText)
{
   var ValidChars = "0123456789,.";
   var IsNumber=true;
   var Char;

   if (sText == '' || sText.length == 0) {
   	return false;
   }


   for (i = 0; i < sText.length && IsNumber == true; i++) {
      Char = sText.charAt(i);
      if (ValidChars.indexOf(Char) == -1) {
         IsNumber = false;
      }
   }
   return IsNumber;

}


//check if form was already submited
function check_form_submited() {
  if (submitted == true) {
    alert('<s:text name="messages.formalreadysubmited" />');
    return false;
  }
}

function check_is_numeric(error_message,form,field_name,message) {
  if(!check_form_submited()) {
       if (form.elements[field_name] && (form.elements[field_name].type != "hidden")) {
	    var field_value = form.elements[field_name].value;
	    if (!IsNumeric(field_value)) {
	      error_message = error_message + "* " + message + "\n";
	    }
	  }
  }
  return error_message;
}


function check_input(error_message,form,field_name, field_size, message) {
  if(!check_form_submited()) {
       if (form.elements[field_name] && (form.elements[field_name].type != "hidden")) {
	    var field_value = form.elements[field_name].value;
	    if (field_value == '' || field_value.length < field_size) {
	      error_message = error_message + "* " + message + "\n";
	    }
	  }
  }
  return error_message;
}

function check_radio(error_message,form,field_name, message) {
  var isChecked = false;

  if(!check_form_submited()) {
	  if (form.elements[field_name] && (form.elements[field_name].type != "hidden")) {
	    var radio = form.elements[field_name];

	    for (var i=0; i<radio.length; i++) {
	      if (radio[i].checked == true) {
	        isChecked = true;
	        break;
	      }
	    }

	    if (isChecked == false) {
	      error_message = error_message + "* " + message + "\n";
	    }
	  }
  }
  return error_message;
}

function check_select(error_message,form,field_name, field_default, message) {
  if(!check_form_submited()) {
	  if (form.elements[field_name] && (form.elements[field_name].type != "hidden")) {
	    var field_value = form.elements[field_name].value;

	    if (field_value == field_default) {
	      error_message = error_message + "* " + message + "\n";
	    }
	  }
  }
  return error_message;
}

function check_password(error_message,form,field_name_1, field_name_2, field_size, message_1, message_2) {
  if(!check_form_submited()) {
	  if (form.elements[field_name_1] && (form.elements[field_name_1].type != "hidden")) {
	    var password = form.elements[field_name_1].value;
	    var confirmation = form.elements[field_name_2].value;

	    if (password == '' || password.length < field_size) {
	      error_message = error_message + "* " + message_1 + "\n";
	      error = true;
	    } else if (password != confirmation) {
	      error_message = error_message + "* " + message_2 + "\n";
	    }
	  }
  }
  return error_message;
}

function check_password_new(error_message,form,field_name_1, field_name_2, field_name_3, field_size, message_1, message_2, message_3) {
  if(!check_form_submited()) {
	  if (form.elements[field_name_1] && (form.elements[field_name_1].type != "hidden")) {
	    var password_current = form.elements[field_name_1].value;
	    var password_new = form.elements[field_name_2].value;
	    var password_confirmation = form.elements[field_name_3].value;

	    if (password_current == '' || password_current.length < field_size) {
	      error_message = error_message + "* " + message_1 + "\n";
	    } else if (password_new == '' || password_new.length < field_size) {
	      error_message = error_message + "* " + message_2 + "\n";
	    } else if (password_new != password_confirmation) {
	      error_message = error_message + "* " + message_3 + "\n";
	    }
	  }
  }
  return error_message;
}


</script>

