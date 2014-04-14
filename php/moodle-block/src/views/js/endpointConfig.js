/**	
 * (C) Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * @author Benjamin Jakobus 
 */

function ibm_sbt_manage_endpoints() {
	$('#ibm-sbt-endpoint-manager').dialog('open');
}

function endpoint_change() {
	var myselect = document.getElementById("enpoint_list");
	if (myselect != null && myselect.options[myselect.selectedIndex] != null) {
		var selected_endpoint = myselect.options[myselect.selectedIndex].value;
		show_selected_endpoint(selected_endpoint);
	} 
}

function ibm_sbt_reset() {
	document.getElementById("new_endpoint_name").disabled = false;
	document.getElementById("tr_new_consumer_secret").style.display = 'none';
	document.getElementById("tr_new_consumer_key").style.display = 'none';
	document.getElementById("tr_new_callback_url").style.display = 'none';
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
	document.getElementById("new_endpoint_version").value = '';
	document.getElementById("new_consumer_secret").value = '';
	document.getElementById("new_consumer_key").value = '';
	document.getElementById("new_basic_auth_username").value = '';
	document.getElementById("new_basic_auth_password").value = '';
	document.getElementById("new_basic_auth_method").value = 'choose';
	document.getElementById("new_authentication_method").value = 'choose';
	document.getElementById("new_server_type").value = document.getElementById("new_server_type").value = 'choose';
	document.getElementById("new_endpoint_save").setAttribute("disabled", "disabled");
}


function ibm_sbt_new_endpoint() {
	action_type = 'create';
	ibm_sbt_reset();
	$("#dialog").dialog("open");
}
