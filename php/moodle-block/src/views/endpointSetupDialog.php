
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />
<script type="text/javascript">
	var strConnectionsEndpointURL = 'URL to the Connections server';
	var strSmartcloudEndpointURL = 'URL to the Smartcloud server';
	var strSSLTrustError = 'You are forcing SSL trust, but your server URL does not use HTTPS. Please correct this.';
	var strEndpointNameError = 'The name of your endpoint should not contain any spaces';
</script>

<script type="text/javascript">
var action_type = 'create';
window.onload = function () {


	
// 	document.getElementById("new_callback_url").value = document.getElementById("callback_url").value;
// 	document.getElementById("submit").value = "Activate Endpoint";
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

	document.getElementById("new_form_auth_page").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);

	document.getElementById("new_form_auth_login_page").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);

	document.getElementById("new_form_auth_cookie_cache").addEventListener('keyup', function (e) {
		completeFieldCheck();
	}, false);
}

$(function() {
    $( "#dialog" ).dialog({
      autoOpen: false,
      modal: true,
      width: 660
    });

    $( "#ibm-sbt-endpoint-manager" ).dialog({
        autoOpen: false,
        modal: true,
        width: 660
      });
});

function new_server_type_change() {
	var server_type = document.getElementById("new_server_type");

	var selected_type = server_type.options[server_type.selectedIndex].value;
	var auth_methods = document.getElementById("new_authentication_method");

	if (selected_type == 'choose') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} 

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

	if (authMethod == 'choose' || selected_type == 'choose') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} 

	if (authMethod == "oauth1") {
		document.getElementById("tr_new_callback_url").style.display = 'none';
		document.getElementById("tr_new_consumer_secret").style.display = 'block';
		document.getElementById("tr_new_consumer_key").style.display = 'block';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		document.getElementById("tr_new_basic_auth_method").style.display = 'none';
		document.getElementById("tr_new_basic_auth_username").style.display = 'none';
		document.getElementById("tr_new_basic_auth_password").style.display = 'none';
		document.getElementById("tr_new_form_auth_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_login_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_cookie_cache").style.display = 'none';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("lb_endpoint_url").value = strSmartcloudEndpointURL;
		if (document.getElementById("new_endpoint_url").value == '') {
			document.getElementById("new_endpoint_url").value = 'https://apps.na.collabserv.com';
		}
		document.getElementById("new_authorization_url").value = '/manage/oauth/authorizeToken';
		document.getElementById("new_access_token_url").value = '/manage/oauth/getAccessToken';
		document.getElementById("new_request_token_url").value = '/manage/oauth/getRequestToken';
		
		oauthSmartcloudFieldCheck();
	} else if (authMethod == "basic") {
		document.getElementById("tr_new_callback_url").style.display = 'none';
		document.getElementById("lb_endpoint_url").value = 'URL to the Connections server';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("tr_new_basic_auth_method").style.display = 'block';
		document.getElementById("tr_new_consumer_secret").style.display = 'none';
		document.getElementById("tr_new_consumer_key").style.display = 'none';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		document.getElementById("tr_new_form_auth_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_login_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_cookie_cache").style.display = 'none';
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
		document.getElementById("tr_new_form_auth_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_login_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_cookie_cache").style.display = 'none';
		document.getElementById("tr_new_callback_url").style.display = 'block';
		document.getElementById("tr_force_ssl_trust").style.display = 'block';
		document.getElementById("lb_endpoint_url").value = strSmartcloudEndpointURL;
		if (document.getElementById("new_endpoint_url").value == '') {
			document.getElementById("new_endpoint_url").value = 'https://apps.na.collabserv.com';
		}
		
		document.getElementById("new_authorization_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth2/authorize';
		document.getElementById("new_access_token_url").value = document.getElementById("new_endpoint_url").value + '/manage/oauth2/token';
		document.getElementById("new_request_token_url").value = document.getElementById("new_endpoint_url").value + '';
		
		document.getElementById("lb_new_consumer_secret").innerHTML = 'ClientSecret';
		document.getElementById("lb_new_consumer_key").innerHTML = 'ClientID';
		oauthSmartcloudFieldCheck();
		
	} else if (authMethod == "oauth2" && selected_type == "connections") {
		document.getElementById("tr_new_callback_url").style.display = 'block';
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

		document.getElementById("tr_new_form_auth_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_login_page").style.display = 'none';
		document.getElementById("tr_new_form_auth_cookie_cache").style.display = 'none';
		
		oauth2ConnectionsFieldCheck();
	} else if (authMethod == "form") {
		document.getElementById("tr_new_form_auth_page").style.display = 'block';
		document.getElementById("tr_new_form_auth_login_page").style.display = 'block';
		document.getElementById("tr_new_form_auth_cookie_cache").style.display = 'block';
		document.getElementById("tr_new_callback_url").style.display = 'none';
		document.getElementById("tr_new_consumer_secret").style.display = 'none';
		document.getElementById("tr_new_consumer_key").style.display = 'none';
		document.getElementById("tr_new_authorization_url").style.display = 'none';
		document.getElementById("tr_new_access_token_url").style.display = 'none';
		document.getElementById("tr_new_request_token_url").style.display = 'none';
		document.getElementById("tr_new_basic_auth_method").style.display = 'none';
		document.getElementById("tr_new_basic_auth_username").style.display = 'none';
		document.getElementById("tr_new_basic_auth_password").style.display = 'none';
		formFieldCheck();
	}
}

function formFieldCheck() {
	if (!isCorrectDropDownValueSelected() || document.getElementById("new_form_auth_page").value == ''
		|| document.getElementById("new_form_auth_login_page").value == '' || document.getElementById("new_form_auth_cookie_cache").value == '') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} else {
		document.getElementById("new_endpoint_save").removeAttribute("disabled");
	}
}

function isCorrectDropDownValueSelected() {
	var myselect = document.getElementById("new_authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	var serverType = document.getElementById("new_server_type");
	var selectedType = serverType.options[serverType.selectedIndex].value;

	return (authMethod != 'choose' && selectedType != 'choose');
}

function oauth2ConnectionsFieldCheck() {
	if (!isCorrectDropDownValueSelected() || document.getElementById('new_callback_url').value == '' || document.getElementById('new_consumer_secret').value == ''
		|| document.getElementById('new_consumer_key').value == '' || document.getElementById('new_endpoint_name').value == '' 
		|| document.getElementById('new_endpoint_url').value == '' || document.getElementById('new_authorization_url').value == ''
		|| document.getElementById('new_access_token_url').value == '' || document.getElementById('new_request_token_url').value == '') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} else {
		document.getElementById("new_endpoint_save").removeAttribute("disabled");
	}
}

function oauthSmartcloudFieldCheck() {
	if (!isCorrectDropDownValueSelected() || document.getElementById('new_consumer_secret').value == ''
		|| document.getElementById('new_consumer_key').value == '' || document.getElementById('new_endpoint_name').value == '' 
		|| document.getElementById('new_endpoint_url').value == '' ) {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} else {
		document.getElementById("new_endpoint_save").removeAttribute("disabled");
	}
}

function change_new_basic_auth_method() {
	var myselect = document.getElementById("new_authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	
	if (authMethod == 'choose') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} 
	
	if (authMethod != 'basic') {
		return;
	}
	var authMethodSelect = document.getElementById("new_basic_auth_method");
	
	if (authMethodSelect.options[authMethodSelect.selectedIndex]) {
		var selectedAuthMethod = authMethodSelect.options[authMethodSelect.selectedIndex].value;
		
		if (selectedAuthMethod == "prompt") {
			document.getElementById('tr_new_basic_auth_password').style.display = "none";
			document.getElementById('tr_new_basic_auth_username').style.display = "none";
			if (document.getElementById('new_endpoint_url').value == '' || document.getElementById('new_endpoint_name').value == '') {
				document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
				return;
			}
			document.getElementById("new_endpoint_save").removeAttribute("disabled");
		} else {
			document.getElementById('tr_new_basic_auth_password').style.display = "block";
			document.getElementById('tr_new_basic_auth_username').style.display = "block";
			
			if (document.getElementById('new_basic_auth_password').value == '' || document.getElementById('new_basic_auth_username').value == '') {
				document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
			} else {
				if (document.getElementById('new_endpoint_url').value == '' || document.getElementById('new_endpoint_name').value == '') {
					document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
					return;
				}
				document.getElementById("new_endpoint_save").removeAttribute("disabled");
			}
		}
	}
}

function basicAuthFieldCheck() {
	var authMethodSelect = document.getElementById("new_basic_auth_method");
	var selectedAuthMethod = authMethodSelect.options[authMethodSelect.selectedIndex].value;

	if (document.getElementById('new_endpoint_url').value == '' || document.getElementById('new_endpoint_name').value == '') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
		return;
	}
	
	if (selectedAuthMethod == "global") {
		if (document.getElementById('new_basic_auth_password').value == '' || document.getElementById('new_basic_auth_username').value == '') {
			document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
		} else {
			document.getElementById("new_endpoint_save").removeAttribute("disabled");
		}
	} else if (selectedAuthMethod == 'choose') {
		document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
	} else if (selectedAuthMethod == "prompt") {
		document.getElementById("new_endpoint_save").removeAttribute("disabled");
	}
}

function completeFieldCheck() {
	var serverType = document.getElementById("new_server_type");
	var selectedType = serverType.options[serverType.selectedIndex].value;
	var myselect = document.getElementById("new_authentication_method");
	var authMethod = myselect.options[myselect.selectedIndex].value;
	if (authMethod == 'oauth2'  && selectedType == "connections") {
		oauth2ConnectionsFieldCheck();
	} else if ((authMethod == 'oauth1' || authMethod == 'oauth2') && selectedType != "connections") {
		oauthSmartcloudFieldCheck();
	} else if (authMethod == 'form') {
		formFieldCheck();
	} else {
		basicAuthFieldCheck();
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

	var endpoint_id = -1;

	if (document.getElementById("new_authorization_url").value == '') {
		document.getElementById("new_authorization_url").value = '/manage/oauth/authorizeToken';
	}

	if (document.getElementById("new_access_token_url").value == '') {
		document.getElementById("new_access_token_url").value = '/manage/oauth/getAccessToken';
	}
	if (document.getElementById("new_request_token_url").value == '') {
		document.getElementById("new_request_token_url").value = '/manage/oauth/getRequestToken';
	}


	if (action_type == 'update') {
		var endpoint_list = document.getElementById("endpoint_list");
		endpoint_id = endpoint_list.options[endpoint_list.selectedIndex].value;
	}
	
	var formData = {
		id: endpoint_id,
		name : document.getElementById("new_endpoint_name").value,
		consumer_secret: document.getElementById("new_consumer_secret").value,
		consumer_key: document.getElementById("new_consumer_key").value,
		api_version: document.getElementById("new_endpoint_version").value,
		access_token_url: document.getElementById("new_endpoint_url").value + document.getElementById("new_access_token_url").value,
		authorization_url: document.getElementById("new_endpoint_url").value + document.getElementById("new_authorization_url").value,
		access_token_url: document.getElementById("new_endpoint_url").value + document.getElementById("new_access_token_url").value,
		request_token_url: document.getElementById("new_endpoint_url").value + document.getElementById("new_request_token_url").value,
		basic_auth_username: document.getElementById("new_basic_auth_username").value,
		basic_auth_password: document.getElementById("new_basic_auth_password").value,
		force_ssl_trust: document.getElementById("new_force_ssl_trust").checked,
		auth_type: document.getElementById("new_authentication_method").value,
		server_type: document.getElementById("new_server_type").value,
		allow_client_access: document.getElementById("new_allow_client_access").checked,
		basic_auth_method: document.getElementById("new_basic_auth_method").value,
		oauth2_callback_url: document.getElementById("new_callback_url").value,
		server_url: document.getElementById("new_endpoint_url").value,
		auth_type: document.getElementById("new_authentication_method").value,
		form_auth_page: document.getElementById("new_form_auth_page").value,
		form_auth_login_page: document.getElementById("new_form_auth_login_page").value,
		form_auth_cookie_cache: document.getElementById("new_form_auth_cookie_cache").value,
		type : action_type
	};
	
	$.ajax({
	    url : "***REMOVED*** global $CFG; echo $CFG->wwwroot . '/blocks/ibmsbt/endpoint_settings.php'; ?>",
	    type: "POST",
	    data : formData,
	    success: function(data, textStatus, jqXHR)
	    {
		    if (action_type == 'create') {
		    	var endpoint_list = document.getElementById('endpoint_list');
		    	var opt = document.createElement('option');
		        opt.value = textStatus;
		        opt.innerHTML = document.getElementById("new_endpoint_name").value + '(' + document.getElementById("new_endpoint_url").value + ')';
		        endpoint_list.appendChild(opt);

		        var endpoint_list2 = document.getElementById('id_config_endpoint');
		        var opt2 = document.createElement('option');
		        opt2.value = textStatus;
		        opt2.innerHTML = document.getElementById("new_endpoint_name").value;
		        endpoint_list2.appendChild(opt2);
		    }
	        
	    	$("#dialog").dialog('close');
	    },
	    error: function (jqXHR, textStatus, errorThrown)
	    {
	 		console.log(textStatus);
	    }
	});
}

function ibm_sbt_remove_endpoint() {
	var endpoint_list = document.getElementById("endpoint_list");
	var endpoint_id = endpoint_list.options[endpoint_list.selectedIndex].value;

	
	var formData = {
		id : endpoint_id,
		type : 'delete'
	};
	
	$.ajax({
	    url : "***REMOVED*** global $CFG; echo $CFG->wwwroot . '/blocks/ibmsbt/endpoint_settings.php'; ?>",
	    type: "POST",
	    data : formData,
	    success: function(data, textStatus, jqXHR)
	    {
	    	var endpoint_list = document.getElementById("endpoint_list");
	    	var endpoint = endpoint_list.options[endpoint_list.selectedIndex];
	    	endpoint_list.removeChild(endpoint);	
	    	
	    	$("#dialog").dialog('close');
	    },
	    error: function (jqXHR, textStatus, errorThrown)
	    {
	 		console.log(textStatus);
	    }
	});
}

function ibm_sbt_edit_endpoint() {
	action_type = 'update';
	var endpoint_list = document.getElementById("endpoint_list");
	var endpoint_id = endpoint_list.options[endpoint_list.selectedIndex].value;
	
	var formData = {
		id : endpoint_id,
		type : 'get'
	};
	
	$.ajax({
	    url : "***REMOVED*** global $CFG; echo $CFG->wwwroot . '/blocks/ibmsbt/endpoint_settings.php'; ?>",
	    type: "POST",
	    data : formData,
	    success: function(data, textStatus, jqXHR)
	    {
	    	var ret = JSON.parse(data);
	    	document.getElementById("new_endpoint_name").value = ret['name'];
			document.getElementById("new_consumer_secret").value = ret['consumer_secret'];
			document.getElementById("new_consumer_key").value = ret['consumer_key'];
			document.getElementById("new_endpoint_version").value = ret['api_version'];
			document.getElementById("new_access_token_url").value = ret['access_token_url'];
			document.getElementById("new_authorization_url").value = ret['authorization_url'];
			document.getElementById("new_access_token_url").value = ret['access_token_url'];
			document.getElementById("new_request_token_url").value = ret['request_token_url'];
			document.getElementById("new_basic_auth_username").value = ret['basic_auth_username'];
			document.getElementById("new_basic_auth_password").value = ret['basic_auth_password'];
			document.getElementById("new_force_ssl_trust").checked = ret['force_ssl_trust'];
			document.getElementById("new_server_type").value = ret['server_type'];
			document.getElementById("new_allow_client_access").checked = ret['allow_client_access'];
			document.getElementById("new_basic_auth_method").value = ret['basic_auth_method'];
			document.getElementById("new_callback_url").value = ret['oauth2_callback_url'];
			document.getElementById("new_endpoint_url").value = ret['server_url'];
			document.getElementById("new_authentication_method").value = ret['auth_type'];
			document.getElementById("new_form_auth_page").value = ret['form_auth_page'];
			document.getElementById("new_form_auth_login_page").value = ret['form_auth_login_page'];
			document.getElementById("new_form_auth_cookie_cache").value = ret['form_auth_cookie_cache'];
			
			new_server_type_change();
			change_new_basic_auth_method();
			$("#dialog").dialog("open");
	    },
	    error: function (jqXHR, textStatus, errorThrown)
	    {
	 		console.log(textStatus);
	    }
	});

}
</script>

<div id="ibm-sbt-endpoint-manager" title="Manage your endpoints" style="column-width:300px; display: none;">
	<select id="endpoint_list" multiple="multiple" style="width: 100%;">
		***REMOVED*** 
			$settings = new SBTSettings();
			$endpoints = $settings->getEndpoints();
			
			foreach ($endpoints as $endpoint) {
				echo '<option value="' . $endpoint->id . '">' . $endpoint->name . ' ('. $endpoint->server_url .')</option>';
			}
		?>
	</select><button onclick="ibm_sbt_new_endpoint();">Add</button> <button onclick="ibm_sbt_edit_endpoint()">Edit</button> <button onclick="ibm_sbt_remove_endpoint();">Remove</button>
</div>

<div id="dialog" title="Create a new endpoint" style="column-width:300px; display: none;">
	<table>
		<tr>
			<td style="width: 200px;">
				What server do you want to connect to?
			</td>
			<td>
				<select onchange="new_server_type_change();" id="new_server_type" name="new_server_type">
					<option value="choose">Choose one...</option>
					<option value="connections">IBM Connections (On Premises)</option>
					<option value="smartcloud">IBM SmartCloud for Social Business</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				What authentication method do you want to use?
			</td>
			<td>
				<select id="new_authentication_method" name="new_authentication_method" onchange="change_new_authentication_method();">
					<option value="choose">Choose one...</option>
					<option id="new_oauth1" value="oauth1">OAuth 1.0</option>
					<option value="oauth2">OAuth 2.0</option>
					<option value="basic">Basic</option>
					<option value="form">Form-based</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				Endpoint name
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_name" name="new_endpoint_name" value="" />
			</td>
		</tr>
		<tr>
			<td style="width: 200px;">
				API version:
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_version" name="new_endpoint_version" value="" />(optional)
			</td>
		</tr>
		<tr style="display: none;" id="tr_force_ssl_trust">
			<td style="width: 200px;">
				<input type="checkbox" checked="checked" id="new_force_ssl_trust" name="new_force_ssl_trust" value="force_ssl_trust" />&nbsp;Force SSL trust
			</td>
			<td style="width: 200px;">
				<input type="checkbox" checked="checked" id="new_allow_client_access" name="new_allow_client_access" value="allow_client_access" />&nbsp;Allow client access
			</td>
		</tr>

		<tr>
			<td style="width: 200px;" id="lb_endpoint_url">
				Endpoint URL
			</td>
			<td>
				<input size="50" type="text" id="new_endpoint_url" name="new_endpoint_url" value="https://[my-server]" />
			</td>
		</tr>
	</table>
	<table>
		<tr style="display: none;" id="tr_new_consumer_key">
			<td style="width: 200px;" id="lb_new_consumer_key">
				Consumer Key
			</td>
			<td>
				<input size="50" type="text" id="new_consumer_key" name="new_consumer_key" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_consumer_secret">
			<td style="width: 200px;" id="lb_new_consumer_secret">
				Consumer Secret
			</td>
			<td>
				<input size="50" type="text" id="new_consumer_secret" name="new_consumer_secret" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_authorization_url">
			<td style="width: 200px;">
				Authorization URL
			</td>
			<td>
				<input size="50" type="text" id="new_authorization_url" name="new_authorization_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_request_token_url">
			<td style="width: 200px;">
				Request Token URL
			</td>
			<td>
				<input size="50" type="text" id="new_request_token_url" name="new_request_token_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_access_token_url">
			<td style="width: 200px;">
				Access Token URL
			</td>
			<td>
				<input size="50" type="text" id="new_access_token_url" name="new_access_token_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_basic_auth_method">
			<td style="width: 200px;">
				Specify user name and password
			</td>
			<td>
				<select id="new_basic_auth_method" name="new_basic_auth_method" onchange="change_new_basic_auth_method();">
					<option value="choose">Choose one...</option>
					<option value="global">Global user credentials</option>
		    		<option value="prompt">Prompt for user credentials</option>
				</select>
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_callback_url">
			<td style="width: 200px;">
				Callback URL
			</td>
			<td>
				<input size="50" type="text" id="new_callback_url" name="new_callback_url" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_basic_auth_username">
			<td style="width: 200px;">
				Username
			</td>
			<td>
				<input size="50" type="text" id="new_basic_auth_username" name="new_basic_auth_username" value="" />
			</td>
		</tr>
		<tr style="display: none;" id="tr_new_form_auth_page">
			<td style="width: 200px;">
				Authentication page URL
			</td>
			<td>
				<input size="50" type="text" id="new_form_auth_page" name="new_form_auth_page" value="" />
			</td>
		</tr> 	
		<tr style="display: none;" id="tr_new_form_auth_login_page">
			<td style="width: 200px;">
				Login page URL
			</td>
			<td>
				<input size="50" type="text" id="new_form_auth_login_page" name="new_form_auth_login_page" value="" />
			</td>
		</tr> 	
		<tr style="display: none;" id="tr_new_form_auth_cookie_cache">
			<td style="width: 200px;">
				Cookie cache
			</td>
			<td>
				<input size="50" type="text" id="new_form_auth_cookie_cache" name="new_form_auth_cookie_cache" value="" />
			</td>
		</tr> 	
		<tr style="display: none;" id="tr_new_basic_auth_password">
			<td style="width: 200px;">
				Password
			</td>
			<td>
				<input size="50" type="password" id="new_basic_auth_password" name="new_basic_auth_password" value="" />
			</td>
		</tr>
	</table>
	<button disabled="disabled" onclick="save_new_endpoint();" id="new_endpoint_save">Save</button> 
	<button onclick="cancel_new_endpoint();" id="new_endpoint_cancel">Cancel</button>
</div>