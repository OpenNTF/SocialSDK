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

if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbt_session');
}

if (!defined('BASIC_AUTH_USERNAME')) {
	define('BASIC_AUTH_USERNAME', 'basicauthusername');
}

if (!defined('BASIC_AUTH_PASSWORD')) {
	define('BASIC_AUTH_PASSWORD', 'basicauthpassword');
}

if (!defined('TOKEN')) {
	define('TOKEN', 'token');
}

if (!defined('REQUEST_TOKEN')) {
	define('REQUEST_TOKEN', 'requesttoken');
}

if (!defined('TOKEN_TYPE')) {
	define('TOKEN_TYPE', 'tokentype');
}

if (!defined('OAUTH_TOKEN')) {
	define('OAUTH_TOKEN', 'oauthtoken');
}

if (!defined('OAUTH_TOKEN_SECRET')) {
	define('OAUTH_TOKEN_SECRET', 'oauthtokensecret');
}

if (!defined('OAUTH_VERIFIER_TOKEN')) {
	define('OAUTH_VERIFIER_TOKEN', 'oauthverifiertoken');
}

if (!defined('OAUTH_REQUEST_TOKEN')) {
	define('OAUTH_REQUEST_TOKEN', 'oauthrequesttoken');
}

if (!defined('OAUTH_REQUEST_TOKEN_SECRET')) {
	define('OAUTH_REQUEST_TOKEN_SECRET', 'oauthrequesttokensecret');
}

/**
 * Credential Store for authorization tokens.
 *
 * @author Benjamin Jakobus
 */
class SBTCredentialStore {
	
	// Session name
	private $sessionName;
	
	// Encryption key
	private $key;
	
	// Initialization vector
	private $iv;
	
	private static $uid;
	
	private static $instance = null;
	
	public static function getInstance() {
		if (self::$instance == null) {
			self::$instance = new SBTCredentialStore();
		}
		return self::$instance;
	}
	
	// TODO: Implement this class for standalone samples
	
	/**
	 * Constructor.
	 */
	private function __construct() {
	}
	
	/**
	 * Stores a value in the credential store.
	 * 
	 * @param string $skey
	 * @param string $value
	 */
	private function _store($skey, $endpoint, $value) {
		// TODO
	}
	
	/**
	 * Returns a value from the credentials store
	 * 
	 * @param string $skey
	 * @param string $endpoint
	 * 
	 * @return 
	 */
	private function _get($skey, $endpoint) {
		// TODO
	}
	/**
	 * Deletes an entry from the credentials store.
	 *
	 * @param string $key
	 */
	private function _delete($skey, $endpoint) {
		// TODO
	}
	
	/**
	 * Destroys the credential store (i.e. removes the session from the database).
	 *
	 * @param string $key
	 */
	public function destroyStore($endpoint = "connections") {
		// TODO
	}
	
	public function storeToken($token, $endpoint = "connections") {
		$this->_store(TOKEN, $endpoint, $token);
	}
	
	/**
	 * Stores the OAuth request token in the database.
	 *
	 * @param string $token			The OAuth request token.
	 */
	public function storeRequestToken($token, $endpoint = "connections") {
		$this->_store(REQUEST_TOKEN, $endpoint, $token);
	}
	
	/**
	 * Returns a request token.
	 *
	 * @return string 		A request token.
	 */
	public function getRequestToken($endpoint = "connections") {
		$token = $this->_get(REQUEST_TOKEN, $endpoint);
		return $token;
	}

	/**
	 * Returns a token.
	 *
	 * @param string $endpoint The endpoint associated with this token.
	 * @return string 		A token.
	 */
	public function getToken($endpoint = "connections") {
		return $this->_get(TOKEN, $endpoint);
	}
	
	/**
	 * Deletes the OAuth tokens.
	 * 
	 * @param string $endpoint The endpoint associated with the tokens.
	 */
	public function deleteTokens($endpoint = "connections") {
		$this->_delete(TOKEN, $endpoint);
		$this->_delete(TOKEN_TYPE, $endpoint);
		$this->_delete(OAUTH_TOKEN, $endpoint);
		$this->_delete(OAUTH_TOKEN_SECRET, $endpoint);
		$this->_delete(REQUEST_TOKEN, $endpoint);
		$this->_delete(OAUTH_VERIFIER_TOKEN, $endpoint);
		$this->_delete(OAUTH_REQUEST_TOKEN_SECRET, $endpoint);
	}
	
	/**
	 * Stores the OAuth access token in the database.
	 * 
	 * @param string $token			The OAuth access token.
	 * @param string $endpoint The endpoint associated with this token.
	 */
	public function storeOAuthAccessToken($token, $endpoint = "connections") {
		$this->_store(OAUTH_TOKEN, $endpoint, $token);
	}
	
	
	public function storeTokenSecret($token, $endpoint = "connections") {
		$this->_store(OAUTH_TOKEN_SECRET, $endpoint, $token);
	}
	
	public function storeVerifierToken($token, $endpoint = "connections") {
		$this->_store(OAUTH_VERIFIER_TOKEN, $endpoint, $token);
	}
	
	public function getTokenSecret($endpoint = "connections") {
		return $this->_get(OAUTH_TOKEN_SECRET, $endpoint);
	}
	
	/**
	 * Returns OAuth access token.
	 *
	 * @param string $endpoint The endpoint associated with this token.
	 * @return string 		OAuth access token.
	 */
	public function getOAuthAccessToken($endpoint = "connections") {
		return $this->_get(OAUTH_TOKEN, $endpoint);
	}
	
	/**
	 * Returns the verifier token.
	 *
	 * @return string 		OAuth verifier token.
	 * @param string $endpoint The endpoint associated with this token.
	 */
	public function getVerifierToken($endpoint = "connections") {
		return $this->_get(OAUTH_VERIFIER_TOKEN, $endpoint);
	}
	
	/**
	 * Stores the basic authentication username in the database.
	 *
	 * @param string $username		The username used to perform basic authentication.
	 * @param string $endpoint 
	 */
	public function storeBasicAuthUsername($username, $endpoint = "connections") {
		$this->_store(BASIC_AUTH_USERNAME, $endpoint, $username);
	}
	
	/**
	 * Returns basic auth username.
	 * 
	 * @param string $endpoint 
	 * @return string 		Basic auth username.
	 */
	public function getBasicAuthUsername($endpoint = "connections") {
		return $this->_get(BASIC_AUTH_USERNAME, $endpoint);
	}
	
	/**
	 * Stores the OAuth 1.0 request token secret
	 *
	 * @param string $token		The OAuth 1.0 request token secret
	 */
	public function storeRequestTokenSecret($token, $endpoint = "connections") {
		$this->_store(OAUTH_REQUEST_TOKEN_SECRET, $endpoint, $token);
	}
	
	/**
	 * Returns basic auth username.
	 * 
	 * @param string $endpoint The endpoint associated with this token.
	 * @return string 		
	 */
	public function getRequestTokenSecret($endpoint = "connections") {
		return $this->_get(OAUTH_REQUEST_TOKEN_SECRET, $endpoint);
	}
	
	/**
	 * Stores the basic authentication password in the database.
	 * 
	 * @param string $password		The password used to perform basic authentication.
	 * @param string $endpoint 
	 */
	public function storeBasicAuthPassword($password, $endpoint = "connections") {
		$this->_store(BASIC_AUTH_PASSWORD, $endpoint, $password);
	}
	
	/**
	 * Returns basic auth password.
	 *
	 * @param string $endpoint The endpoint associated with this token.
	 * @return string 		Basic auth password.
	 */
	public function getBasicAuthPassword($endpoint = "connections") {
		return $this->_get(BASIC_AUTH_PASSWORD, $endpoint);
	}
	
	/**
	 * Deletes stored basic authentication credentials.
	 * 
	 * @param string $endpoint The endpoint associated with the credentials to delete.
	 */
	public function deleteBasicAuthCredentials($endpoint = "connections") {
		$this->_delete(BASIC_AUTH_PASSWORD, $endpoint);
		$this->_delete(BASIC_AUTH_USERNAME, $endpoint);
	}
	
	/**
	 * Deletes stored OAuth credentials.
	 * @param string $endpoint The endpoint associated with the credentials to delete.
	 */
	public function deleteOAuthCredentials($endpoint = "connections") {
		$this->_delete(TOKEN_TYPE, $endpoint);
		$this->_delete(OAUTH_TOKEN, $endpoint);
		$this->_delete(OAUTH_TOKEN_SECRET, $endpoint);
		$this->_delete(OAUTH_REQUEST_TOKEN, $endpoint);
		$this->_delete(TOKEN, $endpoint);
		$this->_delete(OAUTH_VERIFIER_TOKEN, $endpoint);
		$this->_delete(OAUTH_REQUEST_TOKEN_SECRET, $endpoint);
		$this->_delete(REQUEST_TOKEN, $endpoint);
	}
	
	public function storeTokenType($tokenType, $endpoint = "connections") {
		$this->_store(TOKEN_TYPE, $endpoint, $tokenType);
	}
	
	public function getTokenType($endpoint = "connections") {
		return $this->_get(TOKEN_TYPE, $endpoint);
	}
	
	
	/**
	 * Generates a random string of given length.
	 *
	 * @param int $length		Desired length of the string.
	 *
	 * @return string			Random string.
	 */
	private function gen_string($length) {
		return null;
	}
	
	private function _encrypt($key, $data, $iv){
		return null;
	}
	
	private function _decrypt($key, $encryptedData, $iv) {
		return null;
	}
	

}
