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
use Guzzle\Http\Client;
class BasePluginController extends BaseController {
	
	protected $endpointName;
	
	/**
	 * Constructor.
	 */
	function __construct($endpointName = "connections") {
		$this->endpointName = $endpointName;
		$this->loadModel('SBTSettings');
		$settings = new SBTSettings();
		$authMethod = $settings->getAuthenticationMethod();

		global $USER;
		if (isset($USER->id)) {
			setcookie('ibm-sbt-uid', $USER->id, time() + 604800);
		}

		if ($authMethod == 'oauth1') {	
			// Check if we have an access token. If not, re-direct user to authentication page
			$this->loadModel('SBTCredentialStore');
			$store = SBTCredentialStore::getInstance();
			$token = $store->getRequestToken();
			
			if ($token == null) {
				// Autoloader
				if (file_exists('../../../autoload.php')) {
					include_once '../../../autoload.php';
				} else if (function_exists('plugin_dir_path')) {
					$dir = plugin_dir_path( __FILE__ );
					include_once  $dir . '../../autoload.php';
				}
				
				if (file_exists(BASE_PATH . '/core/controllers/endpoint/SBTOAuth1Endpoint.php')) {
					include BASE_PATH . '/core/controllers/endpoint/SBTOAuth1Endpoint.php';
				} 
				
				// Create endpoint
				$oauth = new SBTOAuth1Endpoint();
		
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

				$body = null;
				if (strpos(BASE_LOCATION, 'core') !== FALSE) {
					$body = $oauth->request($url, BASE_LOCATION . '/index.php?plugin=guzzle&class=SBTOAuth1Endpoint&method=authenticationCallback', 'POST', $endpointName);
				} else {
					$body = $oauth->request($url, BASE_LOCATION . '/core/index.php?plugin=guzzle&class=SBTOAuth1Endpoint&method=authenticationCallback', 'POST', $endpointName);
				}
				var_dump($body);
			}
		} else if ($authMethod == 'oauth2') {
			// Check if we have an access token. If not, re-direct user to authentication page
			$this->loadModel('SBTCredentialStore');
			$store = SBTCredentialStore::getInstance();
			$token = $store->getOAuthAccessToken($endpointName);
			
			if ($token == null) {
				// Autoloader
				if (file_exists('../../../autoload.php')) {
					include_once '../../../autoload.php';
				} else if (function_exists('plugin_dir_path')) {
					$dir = plugin_dir_path( __FILE__ );
					include_once  $dir . '../../autoload.php';
				}
			
				$parameters = array(
						'response_type' => 'code',
						'client_id'     => $settings->getClientId($endpointName),
						'callback_uri'  => urlencode($settings->getOAuth2CallbackURL($endpointName))
				); 
			die($settings->getClientId($endpointName));
				$authURL = $settings->getAuthorizationURL($endpointName) . '?' . http_build_query($parameters, null, '&');
				
				if (!headers_sent()) {
					header("Location: " . $authURL);
				} else {
					echo '<script type="text/javascript" language="javascript">window.location = "' . $authURL . '";</script>';
				}
			}
		}
		
	}
	
	/**
	 * Creates the header for the SBTK plugin.
	 */
	public function createHeader() {
		$this->loadModel('SBTSettings');
		$settings = new SBTSettings();
		
		$viewData['deploy_url'] = $settings->getSDKDeployURL($this->endpointName);
		$viewData['authentication_method'] = $settings->getAuthenticationMethod($this->endpointName);
		$viewData['js_library'] = $settings->getJSLibrary($this->endpointName);
		
		$viewData['url'] = $settings->getURL($this->endpointName);
		$viewData['name'] = $settings->getName($this->endpointName);
		$viewData['api_version'] = $settings->getAPIVersion($this->endpointName);
		$viewData['type'] = $settings->getServerType($this->endpointName);
		$viewData['allow_client_access'] = $settings->allowClientAccess($this->endpointName);
		
		$viewData['endpoints'] = $settings->getEndpoints();
		// Load the header view
		return $this->loadView('includes/header', $viewData);	
	}

}

