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


class SBTSettings
{
	// Misc SDK settings
	private $sdkDeployURL;
	
	private $jsLibrary;
	
	// The settings - keyed by endpoint name
	private $_settings = array();
	
	
	/**
	 * Constructor.
	 */
	function __construct() 
	{
		$this->_settings['SmartCloud']['name'] 					= "SmartCloud";
		$this->_settings['SmartCloud']['url'] 					= 'https://apps.na.collabserv.com';
		//TODO: Set for Your environment
		$this->_settings['SmartCloud']['consumer_key'] 			= '';
		$this->_settings['SmartCloud']['consumer_secret']		= '';
		$this->_settings['SmartCloud']['request_token_url']		= 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
		$this->_settings['SmartCloud']['authorization_url']		= 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
		$this->_settings['SmartCloud']['access_token_url']		= 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
		$this->_settings['SmartCloud']['authentication_method'] = "basic"; //basic or oauth1
		$this->_settings['SmartCloud']['force_ssl_trust']		= true;
		$this->_settings['SmartCloud']['basic_auth_method']		= 'global';
		//TODO: Set For Your Environment
		$this->_settings['SmartCloud']['basic_auth_password']	= '';
		$this->_settings['SmartCloud']['basic_auth_username']	= '';
	}
	
	
	/**
	 * Returns a list of available endpoints.
	 *
	 * @return array:		Array of available endpoints.
	 */
	public function getEndpoints() 
	{
		return $this->_settings;
	}
	
	
	/**
	 * Returns the endpoint URL.
	 *
	 * @return
	 */
	public function getURL($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['url'];
	}
	
	/**
	 * Returns the auth page URL used for form-based authentication page.
	 *
	 * @return
	 */
	public function getFormBasedAuthPage($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['form_auth_page'];
	}
	
	/**
	 * Returns the login page URL used for form-based authentication.
	 *
	 * @return
	 */
	public function getFormBasedAuthCookieCache($endpoint = "connections") 
	{
			return $this->_settings[$endpoint]['form_auth_cookie_cache'];
	}
	
	/**
	 * Returns the auth page used for form-based authentication login page url.
	 *
	 * @return
	 */
	public function getFormBasedAuthLoginPage($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['form_auth_login_page'];
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 */
	public function getConsumerKey($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['consumer_key'];
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 */
	public function getConsumerSecret($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['consumer_secret'];
	}
	
	/**
	 * Returns the OAuth 2.0 client secret.
	 *
	 * @return
	 */
	public function getClientSecret($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['client_secret'];
	}
	
	/**
	 * Returns the OAuth 2.0 client ID.
	 *
	 * @return
	 */
	public function getClientId($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['client_id'];
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 */
	public function getRequestTokenURL($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['request_token_url'];
	}
	
	/**
	 * Returns true if force ssl trust on the select endpoint is enabled; false if not.
	 *
	 * @return
	 */
	public function forceSSLTrust($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['force_ssl_trust'];
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 */
	public function getAuthorizationURL($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['authorization_url'];
	}
	
	/**
	 * Returns the API version.
	 *
	 * @return
	 */
	public function getAPIVersion($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['api_version'];
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 */
	public function getAccessTokenURL($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['access_token_url'];
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 */
	public function getAuthenticationMethod($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['authentication_method'];
	}
	
	/**
	 * Returns the OAuth 2.0 callback URL
	 *
	 * @return
	 */
	public function getOAuth2CallbackURL($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['oauth2_callback_url'];
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 */
	public function getSDKDeployURL($endpoint = "connections") 
	{
		return $this->sdkDeployURL;
	}
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 */
	public function getName($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['name'];
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthUsername($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['basic_auth_username'];
	}
	
	/**
	 * Returns the server type.
	 *
	 * @return
	 */
	public function getServerType($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['server_type'];
	}
	
	/**
	 * Returns true if client access is allowed; false if not.
	 *
	 * @return
	 */
	public function allowClientAccess($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['allow_client_access'];
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthPassword($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['basic_auth_password'];
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 */
	public function getBasicAuthMethod($endpoint = "connections") 
	{
		return $this->_settings[$endpoint]['basic_auth_method'];
	}
	
	/**
	 * Returns the JavaScript library to use
	 *
	 * @return string		String indicating which library to use.
	 */
	public function getJSLibrary($endpoint = "connections") 
	{
		return $this->jsLibrary;
	}
}
