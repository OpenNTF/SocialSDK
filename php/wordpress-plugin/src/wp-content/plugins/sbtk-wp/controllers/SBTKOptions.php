<?php
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
		require_once 'config.php';
		
		$endpoints = array();
		$endpoint = array();
		$endpoint['name'] = $config['name'];
		$endpoint['url'] = $config['url'];
		$endpoint['consumer_key'] = $config['consumer_key'];
		$endpoint['consumer_secret'] = $config['consumer_secret'];
		$endpoint['authorization_url'] = $config['authorization_url'];
		$endpoint['access_token_url'] = $config['access_token_url'];
		$endpoint['request_token_url'] = $config['request_token_url'];
		$endpoint['authentication_method'] = $config['authentication_method'];
		$endpoint['selected'] = true;
		
		// JSON encode the endpoint and store it in the endpoints array (providing that the
		// endpoint isn't supposed to be deleted
		$endpoint = json_encode($endpoint);
		$endpoints[$config['name']] = $endpoint;
		
		add_option('my_endpoints',  $endpoints);
		
		// Fetch SDK settings
		$settings = get_option('sbt_sdk_settings');
		
		if ($settings == null || sizeof($settings) <= 0) {
			// Populate with default config info
			require_once 'config.php';
		
			$settings = array();
			$settings['sdk_deploy_url'] = $config['sdk_deploy_url'];
			add_option('sbt_sdk_settings',  $settings);
		
		}
	}
}

