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


class SBTSettings {
	
	
	/**
	 * Constructor.
	 */
	function __construct() {
	}
	
	// TODO: Implement for standalone samples
	
	/**
	 * Returns a list of available endpoints.
	 * 
	 * @return array:		Array of available endpoints.
	 */
	public function getEndpoints() {
		return array();
	}
	
	/**
	 * Returns the endpoint URL.
	 * 
	 * @return
	 */
	public function getURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the auth page URL used for form-based authentication page.
	 *
	 * @return
	 */
	public function getFormBasedAuthPage($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the login page URL used for form-based authentication.
	 *
	 * @return
	 */
	public function getFormBasedAuthCookieCache($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the auth page used for form-based authentication login page url.
	 *
	 * @return
	 */
	public function getFormBasedAuthLoginPage($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 */
	public function getConsumerKey($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 */
	public function getConsumerSecret($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 client secret.
	 *
	 * @return
	 */
	public function getClientSecret($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 client ID.
	 *
	 * @return
	 */
	public function getClientID($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 */
	public function getRequestTokenURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns true if force ssl trust on the select endpoint is enabled; false if not.
	 *
	 * @return
	 */
	public function forceSSLTrust($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 */
	public function getAuthorizationURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the API version.
	 *
	 * @return
	 */
	public function getAPIVersion($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 */
	public function getAccessTokenURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 */
	public function getAuthenticationMethod($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the OAuth 2.0 callback URL
	 *
	 * @return
	 */
	public function getOAuth2CallbackURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 */
	public function getSDKDeployURL($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 */
	public function getName($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthUsername($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the server type.
	 *
	 * @return
	 */
	public function getServerType($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns true if client access is allowed; false if not.
	 *
	 * @return
	 */
	public function allowClientAccess($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 */
	public function getBasicAuthPassword($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 */
	public function getBasicAuthMethod($endpoint = "connections") {
		return null;
	}
	
	/**
	 * Returns the JavaScript library to use
	 *
	 * @return string		String indicating which library to use.
	 */
	public function getJSLibrary($endpoint = "connections") {
		return null;
	}
}