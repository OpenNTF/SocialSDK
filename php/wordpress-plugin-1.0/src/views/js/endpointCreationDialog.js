$(function() {
    $( "#dialog" ).dialog({
      autoOpen: false,
      modal: true,
      width: 660
    });
});

function new_server_type_change() {
	var server_type = document.getElementById("new_server_type");

	var selected_type = server_type.options[server_type.selectedIndex].value;
	var auth_methods = document.getElementById("new_authentication_method");

	if (selected_type == "connections") {
		document.getElementById("new_oauth1").disabled = true;
	} else {
		document.getElementById("new_oauth1").disabled = false;
	}
	
	auth_methods.style.display = 'block';
	
	change_new_authentication_method();
}

function change_new_authentication_method() {
	var myselect = document.getElementById("new_authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	var server_type = document.getElementById("new_server_type");
	var selected_type = server_type.options[server_type.selectedIndex].value;

	if (authMethod == "oauth1") {
		document.getElementById("tr_new_consumer_secret").style.display = 'block';
		document.getElementById("tr_new_consumer_key").style.display = 'block';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		document.getElementById("tr_new_basic_auth_method").style.display = 'none';
		document.getElementById("tr_new_basic_auth_username").style.display = 'none';
		document.getElementById("tr_new_basic_auth_password").style.display = 'none';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("lb_endpoint_url").value = strSmartcloudEndpointURL;
		if (document.getElementById("new_endpoint_url").value == '') {
			document.getElementById("new_endpoint_url").value = 'https://apps.na.collabserv.com';
		}
		document.getElementById("new_authorization_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/authorizeToken';
		document.getElementById("new_access_token_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/getAccessToken';
		document.getElementById("new_request_token_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/getRequestToken';
	} else if (authMethod == "basic") {
		document.getElementById("lb_endpoint_url").value = 'URL to the Connections server';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("tr_new_basic_auth_method").style.display = 'block';
		document.getElementById("tr_new_consumer_secret").style.display = 'none';
		document.getElementById("tr_new_consumer_key").style.display = 'none';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		if (document.getElementById("new_endpoint_url").value == '') {
			document.getElementById("new_endpoint_url").value = 'https://[my-server]';
		}
	} else if (authMethod == "oauth2" && selected_type == "smartcloud") {
		document.getElementById("tr_new_consumer_secret").style.display = 'block';
		document.getElementById("tr_new_consumer_key").style.display = 'block';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		document.getElementById("tr_new_basic_auth_method").style.display = 'none';
		document.getElementById("tr_new_basic_auth_username").style.display = 'none';
		document.getElementById("tr_new_basic_auth_password").style.display = 'none';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("lb_endpoint_url").value = strSmartcloudEndpointURL;
		if (document.getElementById("new_endpoint_url").value == '') {
			document.getElementById("new_endpoint_url").value = 'https://apps.na.collabserv.com';
		}
		document.getElementById("new_authorization_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/authorizeToken';
		document.getElementById("new_access_token_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/getRequestToken';
		document.getElementById("new_request_token_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth/getAccessToken';
		
		document.getElementById("lb_new_consumer_secret").value = 'ClientSecret';
		document.getElementById("lb_new_consumer_key").value = 'ClientID';
	} else if (authMethod == "oauth2" && selected_type == "connections") {
		document.getElementById("tr_new_consumer_secret").style.display = 'block';
		document.getElementById("tr_new_consumer_key").style.display = 'block';
		document.getElementById("tr_new_authorization_url").style.display = 'block';
		document.getElementById("tr_new_access_token_url").style.display = 'block';
		document.getElementById("tr_new_request_token_url").style.display = 'block';
		document.getElementById("tr_new_basic_auth_method").style.display = 'none';
		document.getElementById("tr_new_basic_auth_username").style.display = 'none';
		document.getElementById("tr_new_basic_auth_password").style.display = 'none';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("lb_endpoint_url").value = strConnectionsEndpointURL;
		document.getElementById("lb_new_consumer_secret").value = 'ConsumerSecret';
		document.getElementById("lb_new_consumer_key").value = 'ConsumerKey';
	} 
}

function reset() {
	document.getElementById("new_endpoint_name").disabled = false;
	document.getElementById("tr_new_consumer_secret").style.display = 'none';
	document.getElementById("tr_new_consumer_key").style.display = 'none';
	document.getElementById("tr_new_authorization_url").style.display = 'none';
	document.getElementById("tr_new_access_token_url").style.display = 'none';
	document.getElementById("tr_new_request_token_url").style.display = 'none';
	document.getElementById("tr_new_basic_auth_method").style.display = 'none';
	document.getElementById("tr_new_basic_auth_username").style.display = 'none';
	document.getElementById("tr_new_basic_auth_password").style.display = 'none';
	document.getElementById("tr_force_ssl_trust").style.display = 'block';
	document.getElementById("new_endpoint_url").value = '';
	document.getElementById("new_endpoint_name").value = '';
	document.getElementById("new_authorization_url").value = '';
	document.getElementById("new_access_token_url").value = '';
	document.getElementById("new_request_token_url").value = '';
	document.getElementById("new_consumer_key").value = '';
	document.getElementById("new_consumer_secret").value = '';
	document.getElementById("new_consumer_key").value = '';
	document.getElementById("new_basic_auth_username").value = '';
	document.getElementById("new_basic_auth_password").value = '';
	document.getElementById("new_basic_auth_method").value = 'choose';
	document.getElementById("new_authentication_method").value = 'choose';
	document.getElementById("new_server_type").value = document.getElementById("new_server_type").value = 'choose';
}

function change_new_basic_auth_method() {
	var myselect = document.getElementById("new_authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	if (authMethod != 'basic') {
		return;
	}
	var authMethodSelect = document.getElementById("new_basic_auth_method");
	
	if (authMethodSelect.options[authMethodSelect.selectedIndex]) {
		var selectedAuthMethod = authMethodSelect.options[authMethodSelect.selectedIndex].value;
		
		if (selectedAuthMethod == "prompt") {
			document.getElementById('tr_new_basic_auth_password').style.display = "none";
			document.getElementById('tr_new_basic_auth_username').style.display = "none";
		} else {
			document.getElementById('tr_new_basic_auth_password').style.display = "block";
			document.getElementById('tr_new_basic_auth_username').style.display = "block";
		}
	}
}

function cancel_new_endpoint() {
	document.getElementById("new_endpoint_url").setAttribute("style", "");
	document.getElementById("new_endpoint_name").setAttribute("style", "");
	$( "#dialog" ).dialog('close');
}

function save_new_endpoint() {
	// Disallow spaces
	if (document.getElementById("new_endpoint_name").value.indexOf(' ') >= 0) {
		alert(strEndpointNameError);
		document.getElementById("new_endpoint_name").setAttribute("style", "border-color: red;");
		return;
	}
	if (document.getElementById("new_force_ssl_trust").checked && document.getElementById("new_endpoint_url").value.indexOf('https') < 0) {
		alert(strSSLTrustError);
		document.getElementById("new_endpoint_url").setAttribute("style", "border-color: red;");
		return;
	}
	
	document.getElementById("new_endpoint_url").setAttribute("style", "");
	document.getElementById("new_endpoint_name").setAttribute("style", "");
	document.getElementById("consumer_key").value = document.getElementById("new_consumer_key").value;
	document.getElementById("consumer_secret").value = document.getElementById("new_consumer_secret").value;
	document.getElementById("endpoint_url").value = document.getElementById("new_endpoint_url").value;
	document.getElementById("endpoint_name").value = document.getElementById("new_endpoint_name").value;
	document.getElementById("consumer_key").value = document.getElementById("new_consumer_key").value;
	document.getElementById("authorization_url").value = document.getElementById("new_authorization_url").value;
	document.getElementById("access_token_url").value = document.getElementById("new_access_token_url").value;
	document.getElementById("request_token_url").value = document.getElementById("new_request_token_url").value;
	document.getElementById("basic_auth_username").value = document.getElementById("new_basic_auth_username").value;
	document.getElementById("basic_auth_password").value = document.getElementById("new_basic_auth_password").value;
	document.getElementById("basic_auth_method").value = document.getElementById("new_basic_auth_method").value;
	document.getElementById("force_ssl_trust").value = document.getElementById("new_force_ssl_trust").value;
	document.getElementById("authentication_method").value = document.getElementById("new_authentication_method").value;
	document.getElementById("server_type").value = document.getElementById("new_server_type").value;
	document.getElementById("force_ssl_trust").checked = document.getElementById("new_force_ssl_trust").checked;
	document.getElementById("submit").click();
	$("#dialog").dialog('close');
}