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
 * Base Plugin Controller Class
 *
 * This class object is the super class that every plugin controller in the SDK will be assigned to.
 *
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class BasePluginController extends BaseController {
	
	/**
	 * Constructor.
	 * 
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		$this->loadModel('SBTKSettings');
		$settings = new SBTKSettings();
		$authMethod = $settings->getAuthenticationMethod();

		if ($authMethod == 'oauth1') {	
			// Check if we have an access token. If not, re-direct user to authentication page
			$this->loadModel('CredentialStore');
			$store = new CredentialStore();
			$token = $store->getRequestToken();
			if ($token == null) {
				// Autoloader
				if (file_exists('../../../autoload.php')) {
					include_once '../../../autoload.php';
				} else if (function_exists('plugin_dir_path')) {
					$dir = plugin_dir_path( __FILE__ );
					include_once  $dir . '../../autoload.php';
				}
				
				// Init the OAuth options
				$options = array(
						'consumer_key' => $settings->getConsumerKey(),
						'consumer_secret' => $settings->getConsumerSecret(),
						'server_uri' => $settings->getURL(),
						'request_token_uri' => $settings->getRequestTokenURL(),
						'authorize_uri' => $settings->getAuthorizationURL(),
						'access_token_uri' => $settings->getAccessTokenURL()
				);
				
				// Create endpoint
				$oauth = new OAuth1Endpoint($options);
				
				// Send request to authenticate user (auth token is automatically being stored when callback method = authenticationCallback)
				// find out the domain:
				$domain = $_SERVER['HTTP_HOST'];
				// find out the path to the current file:
				$path = $_SERVER['SCRIPT_NAME'];
				// find out the QueryString:
				$queryString = $_SERVER['QUERY_STRING'];
				
				// put it all together: 
				$protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off' || $_SERVER['SERVER_PORT'] == 443) ? "https://" : "http://";
				$url = $protocol . $domain . $path . "?" . $queryString;
syslog(LOG_INFO, BASE_LOCATION . '/index.php?plugin=guzzle&class=OAuth1Endpoint&method=authenticationCallback');
				$body = $oauth->request($url,
						BASE_LOCATION . '/index.php?plugin=guzzle&class=OAuth1Endpoint&method=authenticationCallback', 'POST');
				
				var_dump($body);
			}
		}
		
	}
	
	/**
	 * Creates the header for the SBTK plugin.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function createHeader() {
		$this->loadModel('SBTKSettings');
		$settings = new SBTKSettings();
	
		$viewData['deploy_url'] = $settings->getSDKDeployURL();
		$viewData['authentication_method'] = $settings->getAuthenticationMethod();
		$viewData['url'] = $settings->getURL();
		$viewData['name'] = $settings->getName();
	
		// Load the header view
		return $this->loadView('includes/header', $viewData);	
	}

}

