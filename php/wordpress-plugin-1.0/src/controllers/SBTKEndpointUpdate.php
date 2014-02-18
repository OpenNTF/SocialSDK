<?php
/*
 * © Copyright IBM Corp. 2014
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
 * This class handles the updating of plugin options / settings. Serialization occurs on two levels (as opposed to just one):
 * [key] ===> [value]
 * 
 * Whereby value is a json-encoded array holding endpoint information. Each value represents one endpoint.
 * 
 * @author Benjamin Jakobus
 */
class SBTKEndpointUpdate {

	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		$this->_performUpdate();
	}
	
	/**
	 * Updates the plugin settings.
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _performUpdate() {
	
		$sdkSettings = array();
		
		$sdkSettings['sdk_deploy_url'] = $_POST['sdk_deploy_url'];
		// If no SDK settings exist, create a new entry. Otherwise just update
		// the existing entries
		if(get_option(SDK_DEPLOY_URL) === FALSE){
			add_option(SDK_DEPLOY_URL,  $sdkSettings );
		} else {
			update_option(SDK_DEPLOY_URL, $sdkSettings );
		}
		
		
		// Fetch the name of the endpoint that is to be updated / created
		if (isset($_POST['endpoint_list']) || $_POST['endpoint_name'] != '') {
			$endpoint_name = "";
			$endpoint_url = "";
			
			$my_endpoints = get_option(ENDPOINTS);
			if($my_endpoints === FALSE){
				$my_endpoints = array();
				add_option(ENDPOINTS,  $my_endpoints);
			}
			
			$endpoint = array();
			
			$endpoint_name = ($_POST['endpoint_name'] != '' ? $_POST['endpoint_name'] : $_POST['endpoint_list']);
		
			// Deslect all other endpoints
			foreach ($my_endpoints as $key => $val) {
				$endpoint = $my_endpoints[$key];
				
				$endpoint = (array)json_decode($endpoint, true);
				$endpoint['selected'] = false;
				$endpoint = json_encode($endpoint);
				$my_endpoints[$key] = $endpoint;
			}
			// Fetch and decode the endpoint
			if (isset($my_endpoints[$endpoint_name])) {
				$endpoint = $my_endpoints[$endpoint_name];
				$endpoint = (array)json_decode($endpoint, true);
			} else {
				$endpoint = array();
			}
			
			// Populate the endpoint with POST data
			$endpoint['name'] = $endpoint_name;
			$endpoint['url'] = (isset($_POST['endpoint_url']) ? $_POST['endpoint_url'] : "");
			$endpoint['consumer_key'] = (isset($_POST['consumer_key']) ? $_POST['consumer_key'] : "");
			$endpoint['consumer_secret'] = (isset($_POST['consumer_secret']) ? $_POST['consumer_secret'] : "");
			$endpoint['authorization_url'] = (isset($_POST['authorization_url']) ? $_POST['authorization_url'] : "");
			$endpoint['access_token_url'] = (isset($_POST['access_token_url']) ? $_POST['access_token_url'] : "");
			$endpoint['request_token_url'] = (isset($_POST['request_token_url']) ? $_POST['request_token_url'] : "");
			$endpoint['authentication_method'] = (isset($_POST['authentication_method']) ? $_POST['authentication_method'] : "");
			$endpoint['selected'] = true;
			$endpoint['basic_auth_username'] = (isset($_POST['basic_auth_username']) ? $_POST['basic_auth_username'] : "");
			$endpoint['basic_auth_password'] = (isset($_POST['basic_auth_password']) ? $_POST['basic_auth_password'] : "");
			$endpoint['basic_auth_method'] = (isset($_POST['basic_auth_method']) ? $_POST['basic_auth_method'] : "");
			$endpoint['server_type'] = (isset($_POST['server_type']) ? $_POST['server_type'] : "");
			$endpoint['force_ssl_trust'] = (isset($_POST['force_ssl_trust']) && $_POST['force_ssl_trust'] == 'force_ssl_trust' ? $_POST['force_ssl_trust'] : "");
			$endpoint['oauth2_callback_url'] = (isset($_POST['callback_url']) ? $_POST['callback_url'] : "");
			
			// If deletion_point is set to "yes", then the endpoint will be deleted.
			// Note: The deletion UI controls will need to be uncommented in
			// sbtk-options.php for this to work
			$deletion = $_POST['delete_endpoint'];
			
			// JSON encode the endpoint and store it in the endpoints array (providing that the
			// endpoint isn't supposed to be deleted
			$endpoint = json_encode($endpoint);
			
			if ($deletion == "yes") {
				unset($my_endpoints[$endpoint_name]);
			} else {
				$my_endpoints[$endpoint_name] = $endpoint;
			}
			
			// Update the existing entries
			update_option(ENDPOINTS, $my_endpoints);
		}
		
		// The JavaScript library to use
		$jsLibrary = $_POST['libraries_list'];

		update_option(JS_LIBRARY, $jsLibrary);
		
		// Direct the user to the settings page and display a success message
		if(is_admin()) {
			$my_settings_page = new SBTKPluginSettings(true);
		}
	}
}