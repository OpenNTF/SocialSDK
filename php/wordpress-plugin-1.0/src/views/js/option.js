var new_endpoint = false;

function show_selected_endpoint(selected_endpoint) {
	var id_str = selected_endpoint.replace(/ /g, "_");

	document.getElementById("endpoint_name").value = document.getElementById(id_str + "_name").value;
	document.getElementById("consumer_secret").value = document.getElementById(id_str + "_consumer_secret").value;
	document.getElementById("endpoint_url").value = document.getElementById(id_str + "_endpoint_url").value;
	document.getElementById("consumer_key").value = document.getElementById(id_str + "_consumer_key").value;
	document.getElementById("authorization_url").value = document.getElementById(id_str + "_authorization_url").value;
	document.getElementById("access_token_url").value = document.getElementById(id_str + "_access_token_url").value;
	document.getElementById("request_token_url").value = document.getElementById(id_str + "_request_token_url").value;
	document.getElementById("authentication_method").value = document.getElementById(id_str + "_authentication_method").value;
	document.getElementById("endpoint_version").value = document.getElementById(id_str + "_endpoint_version").value;
	document.getElementById("basic_auth_username").value = document.getElementById(id_str + "_basic_auth_username").value;
	document.getElementById("basic_auth_password").value = document.getElementById(id_str + "_basic_auth_password").value;
}

function endpoint_change() {
	document.getElementById("delete_endpoint").value = "no";
	var myselect = document.getElementById("enpoint_list");
	if (myselect != null && myselect.options[myselect.selectedIndex] != null) {
		var selected_endpoint = myselect.options[myselect.selectedIndex].value;
		show_selected_endpoint(selected_endpoint);
	} 
}

function sdk_deploy_default_values() {
	document.getElementById("sdk_deploy_url").value = document.getElementById("default_sdk_deploy_url").value;
}


function delete_selected_endpoint() {
	var response = confirm("Do you really want to delete this endpoint?");
	if (response) {
		document.getElementById("delete_endpoint").value = "yes";
		document.getElementById("submit").click();
	}
}

function create_new_endpoint() {
	reset();
	$( "#dialog" ).dialog("open");
	document.getElementById("delete_endpoint").value = "no";
	document.getElementById("new_endpoint").value = "yes";
}

function edit_selected_endpoint() {
	document.getElementById("new_consumer_key").value = document.getElementById("consumer_key").value;
	document.getElementById("new_consumer_secret").value = document.getElementById("consumer_secret").value;
	document.getElementById("new_endpoint_url").value = document.getElementById("endpoint_url").value;
	document.getElementById("new_endpoint_name").value = document.getElementById("endpoint_name").value;
	document.getElementById("new_consumer_key").value = document.getElementById("consumer_key").value;
	document.getElementById("new_authorization_url").value = document.getElementById("authorization_url").value;
	document.getElementById("new_access_token_url").value = document.getElementById("access_token_url").value;
	document.getElementById("new_request_token_url").value = document.getElementById("request_token_url").value;
	document.getElementById("new_basic_auth_username").value = document.getElementById("basic_auth_username").value;
	document.getElementById("new_basic_auth_password").value = document.getElementById("basic_auth_password").value;
	document.getElementById("new_basic_auth_method").value = document.getElementById("basic_auth_method").value;
	document.getElementById("new_force_ssl_trust").checked = document.getElementById("force_ssl_trust").checked;
	document.getElementById("new_authentication_method").value = document.getElementById("authentication_method").value;
	document.getElementById("new_server_type").value = document.getElementById("server_type").value;
	document.getElementById("new_endpoint_version").value = document.getElementById("endpoint_version").value;
	
	new_server_type_change();
	change_new_basic_auth_method();
	$("#dialog").dialog("open");
	document.getElementById("delete_endpoint").value = "no";
	document.getElementById("new_endpoint").value = "no";
	document.getElementById("new_endpoint_name").disabled = true;
}

window.onload = function () {
	document.getElementById("new_callback_url").value = document.getElementById("callback_url").value;
	document.getElementById("submit").value = "Activate Endpoint";
	endpoint_change();
	
	document.getElementById("new_consumer_key").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_consumer_secret").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_endpoint_name").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_endpoint_url").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_authorization_url").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_access_token_url").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_request_token_url").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
	
	document.getElementById("new_basic_auth_username").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);

	document.getElementById("new_basic_auth_password").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
}
