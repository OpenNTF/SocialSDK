
function endpoint_change() {
	document.getElementById("delete_endpoint").value = "no";
	var myselect = document.getElementById("enpoint_list");
	var selected_endpoint = myselect.options[myselect.selectedIndex].value;

	show_selected_endpoint(selected_endpoint);
	change_authentication_method();
}

function change_authentication_method() {
	var myselect = document.getElementById("authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	
	if (authMethod == "oauth1") {
		document.getElementById("endpoint_name").style.display = 'none';
		document.getElementById("consumer_secret").style.display = 'block';
		document.getElementById("endpoint_url").style.display = 'block';
		document.getElementById("consumer_key").style.display = 'block';
		document.getElementById("authorization_url").style.display = 'block';
		document.getElementById("access_token_url").style.display = 'block';
		document.getElementById("request_token_url").style.display = 'block';
		document.getElementById("basic_auth_username").style.display = 'none';
		document.getElementById("basic_auth_password").style.display = 'none';
		
		document.getElementById("lb_endpoint_name").style.display = 'none';
		document.getElementById("lb_basic_auth_username").style.display = 'none';
		document.getElementById("lb_basic_auth_password").style.display = 'none';
		document.getElementById("lb_consumer_secret").style.display = 'block';
		document.getElementById("lb_endpoint_url").style.display = 'block';
		document.getElementById("lb_consumer_key").style.display = 'block';
		document.getElementById("lb_authorization_url").style.display = 'block';
		document.getElementById("lb_access_token_url").style.display = 'block';
		document.getElementById("lb_request_token_url").style.display = 'block';
		
		document.getElementById("btn_default_smartcloud_values").style.display = 'block';
		document.getElementById("btn_default_connections_values").style.display = 'none';
	} else if (authMethod == "basic") {
		document.getElementById("endpoint_name").style.display = 'none';
		document.getElementById("consumer_secret").style.display = 'none';
		document.getElementById("endpoint_url").style.display = 'block';
		document.getElementById("basic_auth_username").style.display = 'block';
		document.getElementById("basic_auth_password").style.display = 'block';
		document.getElementById("consumer_key").style.display = 'none';
		document.getElementById("authorization_url").style.display = 'none';
		document.getElementById("access_token_url").style.display = 'none';
		document.getElementById("request_token_url").style.display = 'none';
		document.getElementById("btn_default_smartcloud_values").style.display = 'none';
		
		document.getElementById("lb_endpoint_name").style.display = 'none';
		document.getElementById("lb_consumer_secret").style.display = 'none';
		document.getElementById("lb_endpoint_url").style.display = 'block';
		document.getElementById("lb_consumer_key").style.display = 'none';
		document.getElementById("lb_authorization_url").style.display = 'none';
		document.getElementById("lb_access_token_url").style.display = 'none';
		document.getElementById("lb_request_token_url").style.display = 'none';
		document.getElementById("btn_default_connections_values").style.display = 'block';
		document.getElementById("lb_basic_auth_username").style.display = 'block';
		document.getElementById("lb_basic_auth_password").style.display = 'block';
		document.getElementById("btn_default_smartcloud_values").style.display = 'none';
	}
}

function default_values_smartcloud() {
	document.getElementById("consumer_secret").value = document.getElementById("default_smartcloud_consumer_secret").value;
	document.getElementById("endpoint_url").value = document.getElementById("default_smartcloud_endpoint_url").value;
	document.getElementById("consumer_key").value = document.getElementById("default_smartcloud_consumer_key").value;
	document.getElementById("authorization_url").value = document.getElementById("default_smartcloud_authorization_url").value;
	document.getElementById("access_token_url").value = document.getElementById("default_smartcloud_access_token_url").value;
	document.getElementById("request_token_url").value = document.getElementById("default_smartcloud_request_token_url").value;
}

function default_values_connections() {
	document.getElementById("endpoint_url").value = document.getElementById("default_connections_endpoint_url").value;
	
	document.getElementById("consumer_secret").value = "";
	document.getElementById("consumer_key").value = "";
	document.getElementById("authorization_url").value = "";
	document.getElementById("access_token_url").value = "";
	document.getElementById("request_token_url").value = "";
}

function sdk_deploy_default_values() {
	document.getElementById("sdk_deploy_url").value = document.getElementById("default_sdk_deploy_url").value;
}

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
	
	document.getElementById("basic_auth_username").value = document.getElementById(id_str + "_basic_auth_username").value;
	document.getElementById("basic_auth_password").value = document.getElementById(id_str + "_basic_auth_password").value;
}

function delete_selected_endpoint() {
	document.getElementById("delete_endpoint").value = "yes";
	document.getElementById("submit").click();
}

function create_new_endpoint() {
	document.getElementById("delete_endpoint").value = "no";
	document.getElementById("endpoint_name").value = "";
	document.getElementById("consumer_secret").value = "";
	document.getElementById("endpoint_url").value = "";
	document.getElementById("consumer_key").value = "";
	document.getElementById("authorization_url").value = "";
	document.getElementById("access_token_url").value = "";
	document.getElementById("request_token_url").value = "";
}

endpoint_change();
change_authentication_method();