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

if (!isset($CFG) || !isset($CFG->wwwroot)) {
	$path = str_replace('blocks/ibmsbt/core/models', '', __DIR__);
	include_once $path . '/config.php';
}

if (!defined('ENDPOINTS')) {
	define('ENDPOINTS', 'ibm_sbt_endpoints');
}

class SBTSettings {
	// Misc SDK settings
	private $sdkDeployURL;
	
	private $jsLibrary;
	
	
	/**
	 * Constructor.
	 */
	function __construct() {
		
		// Check if endpoints table exists. If not, create it
		global $DB;
		global $USER;
		
		$dbman = $DB->get_manager();
		
		$table = new xmldb_table(ENDPOINTS);

		if (!$dbman->table_exists($table)) {
			$this->_createTable($table, $dbman);
		}
		
		global $DB;
		$records = $DB->get_records('config', array());
		
		$configData = array();

		foreach ($records as $record) {
			$configData[$record->name] = $record->value;
		}
		
		$this->sdkDeployURL = (isset($configData['sdk_deploy_url']) ? $configData['sdk_deploy_url'] : "");
		$this->jsLibrary = (isset($configData['js_library']) ? $configData['js_library'] : "");
	}
	
	private function _createTable($table, $dbman) {
		global $DB;
		global $USER;
		
		
		$table->add_field('id', XMLDB_TYPE_INTEGER, '10', null, XMLDB_NOTNULL, XMLDB_SEQUENCE, null);
		$table->add_field('name', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('server_url', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('consumer_key', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('consumer_secret', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('client_id', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('client_secret', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('request_token_url', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('oauth2_callback_url', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('authorization_url', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('access_token_url', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('auth_type', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('basic_auth_username', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('basic_auth_password', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('basic_auth_method', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('force_ssl_trust', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('api_version', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('server_type', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('allow_client_access', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('form_auth_page', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('form_auth_login_page', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		$table->add_field('form_auth_cookie_cache', XMLDB_TYPE_TEXT, 'big', null, null, null, null);
		
		$table->add_field('created_by_user_id', XMLDB_TYPE_INTEGER, '10', null, null, null, null);
		$table->add_key('primary', XMLDB_KEY_PRIMARY, array('id'));
		$dbman->create_table($table);
	}
	
	/**
	 * Returns a list of available endpoints.
	 * 
	 * @return array:		Array of available endpoints.
	 */
	public function getEndpoints() {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		return $endpoints;
	}
	
	
	/**
	 * Returns the endpoint URL.
	 * 
	 * @return
	 */
	public function getURL($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->server_url;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the auth page URL used for form-based authentication page.
	 *
	 * @return
	 */
	public function getFormBasedAuthPage($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->form_auth_page;
			}
		}
	
		return null;
	}
	
	/**
	 * Returns the login page URL used for form-based authentication.
	 *
	 * @return
	 */
	public function getFormBasedAuthCookieCache($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->form_auth_cookie_cache;
			}
		}
	
		return null;
	}
	
	/**
	 * Returns the auth page used for form-based authentication login page url.
	 *
	 * @return
	 */
	public function getFormBasedAuthLoginPage($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->form_auth_login_page;
			}
		}
	
		return null;
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 */
	public function getConsumerKey($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->consumer_key;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 */
	public function getConsumerSecret($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->consumer_secret;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 client secret.
	 *
	 * @return
	 */
	public function getClientSecret($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->client_secret;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 client ID.
	 *
	 * @return
	 */
	public function getClientId($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->client_id;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 */
	public function getRequestTokenURL($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->request_token_url;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns true if force ssl trust on the select endpoint is enabled; false if not.
	 *
	 * @return
	 */
	public function forceSSLTrust($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->force_ssl_trust;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 */
	public function getAuthorizationURL($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->authorization_url;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the API version.
	 *
	 * @return
	 */
	public function getAPIVersion($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->api_version;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 */
	public function getAccessTokenURL($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->access_token_url;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 */
	public function getAuthenticationMethod($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->auth_type;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 callback URL
	 *
	 * @return
	 */
	public function getOAuth2CallbackURL($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->oauth2_callback_url;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 */
	public function getSDKDeployURL($endpoint = "connections") {
		return $this->sdkDeployURL;
	}
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 */
	public function getName($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->name;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthUsername($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->basic_auth_username;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the server type.
	 *
	 * @return
	 */
	public function getServerType($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->server_type;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns true if client access is allowed; false if not.
	 *
	 * @return
	 */
	public function allowClientAccess($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->allow_client_access;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthPassword($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->basic_auth_password;
			}
		}

		return null;
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 */
	public function getBasicAuthMethod($endpoint = "connections") {
		global $DB;
		$endpoints = $DB->get_records(ENDPOINTS);
		foreach($endpoints as $e) {
			if ($e->name == $endpoint) {
				return $e->basic_auth_method;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the JavaScript library to use
	 *
	 * @return string		String indicating which library to use.
	 */
	public function getJSLibrary($endpoint = "connections") {
		return $this->jsLibrary;
	}
}