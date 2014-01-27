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
 * Wordpress Options Controller Class
 *
 * This class object ensures that the Wordpress wp_options table is populated with
 * default values if none exist.
 *
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTKOptions extends BaseController {
	
	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		// Fetch all available endpoints
		$endpoints = get_option('my_endpoints');
		if ($endpoints == null || sizeof($endpoints) <= 0) {
			$this->_populateWithDefault();
		}
	}
	
	/**
	 * Populates the Wordpress wp_options table with default values
	 * for the plugin.
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _populateWithDefault() {
		// Populate with default config info
		$parent = dirname(__FILE__);
		$dir = str_replace('controllers', '/config.php', $parent);
		require_once $dir;
		
		$endpoints = array();
		$endpoint = array();
		$endpoint2 = array();
// 		$endpoint['name'] = $config['name'];
// 		$endpoint['url'] = $config['url'];
// 		$endpoint['consumer_key'] = $config['consumer_key'];
// 		$endpoint['consumer_secret'] = $config['consumer_secret'];
// 		$endpoint['authorization_url'] = $config['authorization_url'];
// 		$endpoint['access_token_url'] = $config['access_token_url'];
// 		$endpoint['request_token_url'] = $config['request_token_url'];
// 		$endpoint['authentication_method'] = $config['authentication_method'];
// 		$endpoint['selected'] = true;
		
// 		$endpoint['basic_auth_method'] = $config['basic_auth_method'];
		
		
		$endpoint['name'] = $config['wp_endpoint_1_name'];
		$endpoint['url'] = $config['wp_endpoint_1_url'];
		$endpoint['consumer_key'] = $config['wp_endpoint_1_consumer_key'];
		$endpoint['consumer_secret'] = $config['wp_endpoint_1_consumer_secret'];
		$endpoint['authorization_url'] = $config['wp_endpoint_1_authorization_url'];
		$endpoint['access_token_url'] = $config['wp_endpoint_1_access_token_url'];
		$endpoint['request_token_url'] = $config['wp_endpoint_1_request_token_url'];
		$endpoint['authentication_method'] = $config['wp_endpoint_1_authentication_method'];
		$endpoint['basic_auth_method'] = $config['wp_endpoint_1_authentication_method'];
		$endpoint['selected'] = false;
		
		$endpoint2['name'] = $config['wp_endpoint_2_name'];
		$endpoint2['url'] = $config['wp_endpoint_2_url'];
		$endpoint2['consumer_key'] = $config['wp_endpoint_2_consumer_key'];
		$endpoint2['consumer_secret'] = $config['wp_endpoint_2_consumer_secret'];
		$endpoint2['authorization_url'] = $config['wp_endpoint_2_authorization_url'];
		$endpoint2['access_token_url'] = $config['wp_endpoint_2_access_token_url'];
		$endpoint2['request_token_url'] = $config['wp_endpoint_2_request_token_url'];
		$endpoint2['authentication_method'] = $config['wp_endpoint_2_authentication_method'];
		$endpoint2['selected'] = true;
		$endpoint2['basic_auth_method'] = $config['wp_endpoint_2_authentication_method'];
		
		// JSON encode the endpoint and store it in the endpoints array (providing that the
		// endpoint isn't supposed to be deleted
		$endpoint = json_encode($endpoint);
		$endpoint2 = json_encode($endpoint2);
		
		$endpoints[$config['wp_endpoint_1_name']] = $endpoint;
		$endpoints[$config['wp_endpoint_2_name']] = $endpoint2;
		
		add_option('my_endpoints',  $endpoints);
		
		// Fetch SDK settings
		$settings = get_option('sbt_sdk_settings');
		
		if ($settings == null || sizeof($settings) <= 0) {
			// Populate with default config info
			require_once $dir;
		
			$settings = array();
			$settings['sdk_deploy_url'] = plugins_url('sbtk-wp') . '/system/libs/js-sdk';
			add_option('sbt_sdk_settings',  $settings);
		
		}
	}
}