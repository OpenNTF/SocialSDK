***REMOVED***
/*
 * © Copyright IBM Corp. 2013
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
 * This class handles the updating of endpoint options / settings. Serialization occurs on two levels (as opposed to just one):
 * [key] ===> [value]
 * 
 * Whereby value is a json-encoded array holding endpoint information. Each value represents one endpoint.
 * 
 * @author Benjamin Jakobus
 */
class EndpointUpdate {

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
		$endpointName = "";
		
		$myEndpoints = get_option('my_endpoints');
		
		$endpoint = array();
		$sdkSettings = array();
		
		// If no endpoints exist, then create a new array
		if ($myEndpoints === FALSE) {
			$myEndpoints = array();
		}
		
		// Fetch the name of the endpoint that is to be created / updated
		$endpointName = $_POST['endpoint_list'];
		$sdkSettings['sdk_deploy_url'] = $_POST['sdk_deploy_url'];
		// If no SDK settings exist, create a new entry. Otherwise just update
		// the existing entries
		if(get_option('sbt_sdk_settings') === FALSE){
			add_option('sbt_sdk_settings',  $sdkSettings );
		} else {
			update_option('sbt_sdk_settings', $sdkSettings );
		}
		
		
		// Deslect all other endpoints
		foreach ($myEndpoints as $key => $val) {
			$endpoint = $myEndpoints[$key];
		
			$endpoint = (array)json_decode($endpoint, true);
			$endpoint['selected'] = false;
			$endpoint = json_encode($endpoint);
			$myEndpoints[$key] = $endpoint;
		}
		
		// Fetch and decode the endpoint
		$endpoint = $myEndpoints[$endpointName];
		$endpoint = (array)json_decode($endpoint, true);
		
		// Populate the endpoint with POST data
		$endpoint['name'] = $endpointName;
		$endpoint['url'] = $_POST['endpoint_url'];
		$endpoint['consumer_key'] = $_POST['consumer_key'];
		$endpoint['consumer_secret'] = $_POST['consumer_secret'];
		$endpoint['authorization_url'] = $_POST['authorization_url'];
		$endpoint['access_token_url'] = $_POST['access_token_url'];
		$endpoint['request_token_url'] = $_POST['request_token_url'];
		$endpoint['authentication_method'] = $_POST['authentication_method'];
		$endpoint['selected'] = true;
		$endpoint['basic_auth_username'] = $_POST['basic_auth_username'];
		$endpoint['basic_auth_password'] = $_POST['basic_auth_password'];
		$endpoint['basic_auth_method'] = $_POST['basic_auth_method'];
		
		
		// If deletion_point is set to "yes", then the endpoint will be deleted.
		// Note: The deletion UI controls will need to be uncommented in
		// sbtk-options.php for this to work
		$deletion = $_POST['delete_endpoint'];
		
		// JSON encode the endpoint and store it in the endpoints array (providing that the
		// endpoint isn't supposed to be deleted
		$endpoint = json_encode($endpoint);
		
		if ($deletion == "yes") {
			unset($myEndpoints[$endpointName]);
		} else {
			$myEndpoints[$endpointName] = $endpoint;
		}
		
		// If no endpoints exist, create a new entry. Otherwise just update
		// the existing entries
		if(get_option('my_endpoints') === FALSE){
			add_option('my_endpoints',  $myEndpoints );
		} else {
			update_option('my_endpoints', $myEndpoints );
		}
		
		// Direct the user to the settings page and display a success message
		if(is_admin()) {
			$mySettingsPage = new SBTKPluginSettings(true);
		}
	}
}