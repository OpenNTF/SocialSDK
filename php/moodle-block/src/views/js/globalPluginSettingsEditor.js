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

window.onload = function() {

	var authType = document.getElementById('id_s__auth_type');
	
	if (authType != null) {
		authType.addEventListener(
			"change",
			 authTypeChange,
		 	false
		);
		authTypeChange();
	}
}

function authTypeChange() {
	var auth_list = document.getElementById("id_s__auth_type");
	var selected_auth = auth_list.options[auth_list.selectedIndex].value;
	var visibleSectionID = '';
	var invisibleSectionID = '';
	if (selected_auth == 'basic') {
		visibleSectionID = 'ibm-sbtk-basic-auth-admin-section';
		invisibleSectionID = 'ibm-sbtk-oauth-admin-section';

		var el = document.getElementById('id_s__server_url').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__basic_auth_username').parentNode;
		el.parentNode.style.display = "block";

		el = document.getElementById('id_s__basic_auth_method').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__basic_auth_password').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__o_auth_server_url').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__consumer_key').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__consumer_secret').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__request_token_url').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__authorization_url').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__access_token_url').parentNode;
		el.parentNode.style.display = "none";

		var labels = document.getElementsByTagName('label');
		for (var i = 0; i < labels.length; i++) {
		    if (labels[i].htmlFor == 'id_s__access_token_url' ||
		    		labels[i].htmlFor == 'id_s__authorization_url'
			  		|| labels[i].htmlFor == 'id_s__request_token_url'
			  		|| labels[i].htmlFor == 'id_s__consumer_secret'
			  		|| labels[i].htmlFor == 'id_s__consumer_key'
			  		|| labels[i].htmlFor == 'id_s__o_auth_server_url') {
		    	labels[i].parentNode.style.display = 'none';
		    } else if (labels[i].htmlFor == 'id_s__server_url' ||
		    		labels[i].htmlFor == 'id_s__basic_auth_username'
				  		|| labels[i].htmlFor == 'id_s__basic_auth_password' 
					  	|| labels[i].htmlFor == 'id_s__basic_auth_method') {
			    	labels[i].parentNode.style.display = 'block';
			} 
		}
		
	} else if (selected_auth == 'oauth1') {
		visibleSectionID = 'ibm-sbtk-oauth-admin-section';
		invisibleSectionID = 'ibm-sbtk-basic-auth-admin-section';
		
		el = document.getElementById('id_s__o_auth_server_url').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__consumer_key').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__consumer_secret').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__request_token_url').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__authorization_url').parentNode;
		el.parentNode.style.display = "block";
		
		el = document.getElementById('id_s__access_token_url').parentNode;
		el.parentNode.style.display = "block";

		var el = document.getElementById('id_s__server_url').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__basic_auth_username').parentNode;
		el.parentNode.style.display = "none";
		
		el = document.getElementById('id_s__basic_auth_password').parentNode;
		el.parentNode.style.display = "none";

		el = document.getElementById('id_s__basic_auth_method').parentNode;
		el.parentNode.style.display = "none";

		var labels = document.getElementsByTagName('label');
		for (var i = 0; i < labels.length; i++) {
		    if (labels[i].htmlFor == 'id_s__access_token_url' ||
		    		labels[i].htmlFor == 'id_s__authorization_url'
			  		|| labels[i].htmlFor == 'id_s__request_token_url'
			  		|| labels[i].htmlFor == 'id_s__consumer_secret'
			  		|| labels[i].htmlFor == 'id_s__consumer_key'
			  		|| labels[i].htmlFor == 'id_s__o_auth_server_url') {
		    	labels[i].parentNode.style.display = 'block';
		    } else if (labels[i].htmlFor == 'id_s__server_url' ||
		    		labels[i].htmlFor == 'id_s__basic_auth_username'
				  		|| labels[i].htmlFor == 'id_s__basic_auth_password'
					  	|| labels[i].htmlFor == 'id_s__basic_auth_method') {
			    	labels[i].parentNode.style.display = 'none';
			} 
		}
		
	}

	var visibleSection = document.getElementById(visibleSectionID).parentNode;
	visibleSection.style.display = "block";

	var invisibleSection = document.getElementById(invisibleSectionID).parentNode;
	invisibleSection.style.display = "none";
}
