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
 * Wrapper for the configuration file - encapsulates the notion of an endpoint setting.
 * 
 * @author Benjamin Jakobus
 *
 */
if (!defined('ENDPOINTS')) {
	$pluginBasePath = str_replace('core', '', BASE_PATH);
	include $pluginBasePath . 'ibm-sbt-constants.php';
}
class SBTSettings {
	
	// The endpoint to which the user is currently connecting
	private $selectedEndpoint; 
	// Misc SDK settings
	private $sdkSettings;
	
	// Community grid settings
	private $communityGridSettings;
	
	// Files grid settings
	private $filesGridSettings;
	
	// Forums grid settings
	private $forumsGridSettings;
	
	// Bookmarks grid settings
	private $bookmarksGridSettings;
	
	// Basic authentication method - must be global|prompt|profile
	private $basicAuthMethod;
	
	// The JavaScript library to use
	private $jsLibrary;
	
	/**
	 * Constructor.
	 */
	function __construct() {
		
		// If the SBTKSettings file is called outside the wordpress context, then
		// we need to load the Wordpress API and associated dependencies manually
		// in order to access the required settings
		if (!function_exists('get_option')) {
			define('SHORTINIT', true);
			if (file_exists('../../../../wp-load.php')) {
				require_once '../../../../wp-load.php';
			} else if (file_exists('../../../wp-load.php')) {
				require_once '../../../wp-load.php';
			}
		}
		
		// Fetch all available endpoints
		$endpoints = get_option(ENDPOINTS);
		$this->sdkSettings = get_option(SDK_DEPLOY_URL);
		$this->jsLibrary = get_option(JS_LIBRARY);

		// Find selected endpoint
		if ($endpoints) {
			foreach ($endpoints as $val) {
				$endpoint = (array)json_decode($val, true);
				if ($endpoint['selected']) {
					$this->selectedEndpoint = $endpoint;
					break;
				}
			}
		}
	}
	
	/**
	 * Returns the endpoint URL.
	 * 
	 * @return
	 */
	public function getURL($endpointName = "connections") {
		return $this->selectedEndpoint['url'];	
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 */
	public function getConsumerKey($endpointName = "connections") {
		return $this->selectedEndpoint['consumer_key'];
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 */
	public function getConsumerSecret($endpointName = "connections") {
		return $this->selectedEndpoint['consumer_secret'];
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 */
	public function getRequestTokenURL($endpointName = "connections") {
		return $this->selectedEndpoint['request_token_url'];
	}
	
	/**
	 * Returns true if force ssl trust on the select endpoint is enabled; false if not.
	 *
	 * @return
	 */
	public function forceSSLTrust($endpointName = "connections") {
		return ($this->selectedEndpoint['force_ssl_trust'] == 'force_ssl_trust');
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 */
	public function getAuthorizationURL($endpointName = "connections") {
		return $this->selectedEndpoint['authorization_url'];
	}
	
	/**
	 * Returns the API version.
	 *
	 * @return
	 */
	public function getAPIVersion($endpointName = "connections") {
		return $this->selectedEndpoint['endpoint_version'];
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 */
	public function getAccessTokenURL($endpointName = "connections") {
		return $this->selectedEndpoint['access_token_url'];
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 */
	public function getAuthenticationMethod($endpointName = "connections") {
		return $this->selectedEndpoint['authentication_method'];
	}
	
	/**
	 * Returns the OAuth 2.0 callback URL
	 *
	 * @return
	 */
	public function getOAuth2CallbackURL($endpointName = "connections") {
		return $this->selectedEndpoint['oauth2_callback_url'];
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 */
	public function getSDKDeployURL($endpointName = "connections") {
		return $this->sdkSettings['sdk_deploy_url'];
	}
	
	
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 */
	public function getName($endpointName = "connections") {
		return  $this->selectedEndpoint['name'];
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthUsername($endpointName = "connections") {
		return $this->selectedEndpoint['basic_auth_username'];
	}
	
	/**
	 * Returns the server type.
	 *
	 * @return
	 */
	public function getServerType($endpointName = "connections") {
		return $this->selectedEndpoint['server_type'];
	}
	
	/**
	 * Returns true if client access is allowed; false if not.
	 *
	 * @return
	 */
	public function allowClientAccess($endpointName = "connections") {
		return $this->selectedEndpoint['allow_client_access'];
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthPassword($endpointName = "connections") {
		return $this->selectedEndpoint['basic_auth_password'];
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 */
	public function getBasicAuthMethod($endpointName = "connections") {
		return $this->selectedEndpoint['basic_auth_method'];
	}
	
	/**
	 * Returns the JavaScript library to use
	 *
	 * @return string		String indicating which library to use.
	 */
	public function getJSLibrary($endpointName = "connections") {
		return $this->jsLibrary;
	}
	
	// TODO: Implement
	public function getEndpoints() {
		return array();
	}
	
	public function getClientSecret($endpointName = "connections") {
		return $this->getConsumerSecret($endpointName);
	}
	
	public function getClientId($endpointName = "connections") {
		return $this->getConsumerKey($endpointName);
	}
}