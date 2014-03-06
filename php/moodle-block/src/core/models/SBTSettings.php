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
 * Wrapper for the configuration file - encapsulates the notion of an endpoint setting.
 * 
 * @author Benjamin Jakobus
 *
 */

if (!isset($CFG) || !isset($CFG->wwwroot)) {
	$path = str_replace('blocks/ibmsbt/core/models', '', __DIR__);
	include_once $path . '/config.php';
}

class SBTSettings {
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
	
	// Force SSL trust (true or false)
	private $forceSSLTrust;
	
	// API version
	private $apiVersion;
	
	// Server type (connections or smartcloud)
	private $serverType;
	
	// Allow client access (true or false)
	private $allowClientAccess;
	
	// OAuth 2.0 client ID
	private $clientID;
	
	// OAuth 2.0 client secret
	private $clientSecret;
	
	/**
	 * Constructor.
	 */
	function __construct() {
		
		global $DB;
		$records = $DB->get_records('config', array());
		
		$configData = array();

		foreach ($records as $record) {
			$configData[$record->name] = $record->value;
		}
		
		$this->url = ($configData['auth_type'] == 'basic' ? $configData['server_url'] : $configData['o_auth_server_url']);
		$this->consumerKey = $configData['consumer_key'];
		$this->consumerSecret = (isset($configData['consumer_secret']) ? $configData['consumer_secret'] : "");
		$this->clientID = (isset($configData['client_id']) ? $configData['client_id'] : "");
		$this->clientSecret = (isset($configData['client_secret']) ? $configData['client_secret'] : "");
		$this->requestTokenURL = (isset($configData['request_token_url']) ? $configData['request_token_url'] : "");
		$this->authorizationURL = (isset($configData['authorization_url']) ? $configData['authorization_url'] : "");
		$this->accessTokenURL = (isset($configData['access_token_url']) ? $configData['access_token_url'] : "");
		$this->authenticationMethod = (isset($configData['auth_type']) ? $configData['auth_type'] : "");
		$this->sdkDeployURL = (isset($configData['sdk_deploy_url']) ? $configData['sdk_deploy_url'] : "");
		$this->basicAuthUsername = (isset($configData['basic_auth_username']) ? $configData['basic_auth_username'] : "");
		$this->basicAuthPassword = (isset($configData['basic_auth_password']) ? $configData['basic_auth_password'] : "");
		$this->basicAuthMethod = (isset($configData['basic_auth_method']) ? $configData['basic_auth_method'] : "");
		
		$this->forceSSLTrust = (isset($configData['force_ssl_trust']) ? $configData['force_ssl_trust'] : true);
		
		$this->apiVersion = (isset($configData['endpoint_version']) ? $configData['endpoint_version'] : "");
		$this->oauth2CallbackURL = (isset($configData['oauth2_callback_url']) ? $configData['oauth2_callback_url'] : "");
		$this->serverType = (isset($configData['server_type']) ? $configData['server_type'] : "");
		
		$this->allowClientAccess = (isset($configData['allow_client_access']) ? $configData['allow_client_access'] : true);
		
		$this->name = (isset($configData['name']) ? $configData['name'] : ""); 
	}
	
	/**
	 * Returns the endpoint URL.
	 * 
	 * @return
	 */
	public function getURL() {
		return $this->url;
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 */
	public function getConsumerKey() {
		return $this->consumerKey;
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 */
	public function getConsumerSecret() {
		return $this->consumerSecret;
	}
	
	/**
	 * Returns the OAuth 2.0 client secret.
	 *
	 * @return
	 */
	public function getClientSecret() {
		return $this->clientSecret;
	}
	
	/**
	 * Returns the OAuth 2.0 client ID.
	 *
	 * @return
	 */
	public function getClientID() {
		return $this->clientID;
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 */
	public function getRequestTokenURL() {
		$this->requestTokenURL;
	}
	
	/**
	 * Returns true if force ssl trust on the select endpoint is enabled; false if not.
	 *
	 * @return
	 */
	public function forceSSLTrust() {
		return $this->forceSSLTrust;
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 */
	public function getAuthorizationURL() {
		return $this->authorizationURL;
	}
	
	/**
	 * Returns the API version.
	 *
	 * @return
	 */
	public function getAPIVersion() {
		return $this->apiVersion;
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 */
	public function getAccessTokenURL() {
		return $this->accessTokenURL;
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 */
	public function getAuthenticationMethod() {
		return $this->authenticationMethod;
	}
	
	/**
	 * Returns the OAuth 2.0 callback URL
	 *
	 * @return
	 */
	public function getOAuth2CallbackURL() {
		return $this->oauth2CallbackURL;
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 */
	public function getSDKDeployURL() {
		return $this->sdkDeployURL;
	}
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 */
	public function getName() {
		return $this->name;
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthUsername() {
		return $this->basicAuthUsername;
	}
	
	/**
	 * Returns the server type.
	 *
	 * @return
	 */
	public function getServerType() {
		return $this->serverType;
	}
	
	/**
	 * Returns true if client access is allowed; false if not.
	 *
	 * @return
	 */
	public function allowClientAccess() {
		return $this->allowClientAccess;
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthPassword() {
		return $this->basicAuthPassword;
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 */
	public function getBasicAuthMethod() {
		return $this->basicAuthMethod;
	}
	
	/**
	 * Returns the JavaScript library to use
	 *
	 * @return string		String indicating which library to use.
	 */
	public function getJSLibrary() {
		return $this->jsLibrary;
	}
}