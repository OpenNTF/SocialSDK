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

// Define constants
if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbtk_session');
}

if (!defined('SESSION_PREFIX')) {
	define('SESSION_PREFIX', 'ibm_sbtk_');
}

if (!defined('CRYPT')) {
	$path = str_replace('core', '', BASE_PATH);
	require_once $path . '/ibm-sbt-constants.php';
}

// If the CredentialStore file is called outside the wordpress context, then
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

if (!function_exists('sanitize_option')) {
	if (file_exists('../../../../wp-includes/formatting.php')) {
		require_once '../../../../wp-includes/meta.php';
		require_once '../../../../wp-includes/plugin.php';
		require_once '../../../../wp-includes/user.php';
		require_once '../../../../wp-includes/capabilities.php';
		require_once '../../../../wp-includes/load.php';
		require_once '../../../../wp-includes/functions.php';
		require_once '../../../../wp-includes/pluggable.php';
		require_once '../../../../wp-includes/formatting.php';
	} else if (file_exists('../../../wp-includes/formatting.php')) {
		
		require_once '../../../wp-includes/meta.php';
		require_once '../../../wp-includes/plugin.php';
		require_once '../../../wp-includes/user.php';
		require_once '../../../wp-includes/capabilities.php';
		require_once '../../../wp-includes/load.php';
		require_once '../../../wp-includes/functions.php';
		require_once '../../../wp-includes/pluggable.php';
		require_once '../../../wp-includes/formatting.php';
	}
}
if (file_exists(BASE_PATH . '/core/models/SBTPHPCookieAdapter.php')) {
	require BASE_PATH . '/core/models/SBTCookie.php';
	require BASE_PATH . '/core/models/SBTCookieAdapter.php';
	require BASE_PATH . '/core/models/SBTMemoryCookieAdapter.php';
	require BASE_PATH . '/core/models/SBTPHPCookieAdapter.php';
} else {	
	require BASE_PATH . '/models/SBTCookie.php';
	require BASE_PATH . '/models/SBTCookieAdapter.php';
	require BASE_PATH . '/models/SBTMemoryCookieAdapter.php';
	require BASE_PATH . '/models/SBTPHPCookieAdapter.php';
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
	
	// User ID
	private $userID = 0;
	
	private static $instance = null;
	
	public static function getInstance($cookieAdapter = null) {
		if ($cookieAdapter == null) {
			$cookieAdapter = new SBTPHPCookieAdapter();
		}
		SBTCookie::init($cookieAdapter);
		if (self::$instance == null) {
			self::$instance = new SBTCredentialStore($cookieAdapter);
		}
		return self::$instance;
	}
	
	/**
	 * Constructor.
	 *
	 * @param $cookieAdapter	The cookie adapter to use. Use SBTMemoryCookieAdapter when 
	 * 							testing; use SBTPHPCookieAdapter when running live (default).
	 */
	private function __construct($cookieAdapter) {
		// Check if the user has a key. If not, generate one and save it
		
		if ($this->_isUserLoggedIn()) {
			$data = get_user_meta($this->userID, SESSION_NAME, true);

			if (isset($data['key'])) {
				$this->key = $data['key'];
				$this->iv = $data['iv'];
			} else {
				$this->_initProfileSession();
			}
		} else {
			if (SBTCookie::get(SESSION_NAME) != null) {
				$encrypted = SBTCookie::get(SESSION_NAME);
				$crypt = get_option(CRYPT);
				$key = $crypt['key'];
				$iv = $crypt['iv'];
				$iv = base64_decode($iv);
				$data = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key , base64_decode($encrypted), MCRYPT_MODE_CBC, $iv), "\0");
				$data = unserialize($data);
				$this->key = $data['key'];
				$this->iv = $data['iv'];
				
			} else {
				$this->_initCookie();
			}
		}
	}
	
	/**
	 * Sets the cookie.
	 */
	private function _initCookie() {
		$pivKey = $this->gen_string(32);
		$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
		$pivIv = mcrypt_create_iv($iv_size, MCRYPT_RAND);
			
		$sessionID = SESSION_PREFIX . uniqid();
		$timestamp = time();
		while (get_option($sessionID) !== false) {
			$sessionID = SESSION_PREFIX . uniqid();
		}
			
		$sessions = get_option(USER_SESSIONS);
		
		array_push($sessions, array(
			'id' => $sessionID,
			'created' => $timestamp)
		);
		update_option(USER_SESSIONS, $sessions);
		
		$etimestamp = $this->_encrypt($pivKey, $timestamp, $pivIv);
		$sessionData = array('created' => $etimestamp);
		add_option($sessionID, $sessionData);
		
		$pivIv = base64_encode($pivIv);
		$data = array(
				'iv' => $pivIv,
				'key' => $pivKey,
				'sessionID' => $sessionID
		);
			
		$data = serialize($data);
			
		$crypt = get_option(CRYPT);
		
		$key = $crypt['key'];
		$iv = $crypt['iv'];
		$iv = base64_decode($iv);
			
		$encrypted = base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, $key, $data, MCRYPT_MODE_CBC, $iv));
			
		// Expire in one day
		SBTCookie::set(SESSION_NAME, $encrypted, $timestamp + 86400);
			
		$this->key = $pivKey;
		$this->iv = $pivIv;
	}
	
	private function _initProfileSession() {
		$pivKey = $this->gen_string(32);
		$iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
		$pivIv = mcrypt_create_iv($iv_size, MCRYPT_RAND);
			
		$sessionID = SESSION_PREFIX . uniqid();
		$timestamp = time();
		while (get_option($sessionID) !== false) {
			$sessionID = SESSION_PREFIX . uniqid();
		}
			
		$sessions = get_option(USER_SESSIONS);
	
		array_push($sessions, array(
			'id' => $sessionID,
			'created' => $timestamp)
		);
		update_option(USER_SESSIONS, $sessions);
	
		$etimestamp = $this->_encrypt($pivKey, $timestamp, $pivIv);
		$sessionData = array('created' => $etimestamp);
		add_option($sessionID, $sessionData);
	
		$pivIv = base64_encode($pivIv);
		$data = array(
				'iv' => $pivIv,
				'key' => $pivKey,
				'sessionID' => $sessionID
		);
		update_user_meta($this->userID, SESSION_NAME, $data);

		$this->key = $pivKey;
		$this->iv = $pivIv;
	}
	
	/**
	 * Stores a value in the credential store.
	 * 
	 * @param string $skey
	 * @param string $value
	 */
	private function _store($skey, $value) {

		$data = null;
		if ($this->_isUserLoggedIn()) {
			$data = $this->_getSessionInfoFromProfile();
		} else {
			$data = $this->_getSessionInfoFromCookie();
		}
		
		$key = $data['key'];
		$iv = $data['iv'];
		$iv = base64_decode($iv);
		$sessionID = $data['sessionID'];
		// Get session
		$session = get_option($sessionID);
		
		if ($session === false) {
			$timestamp = time();
		
			$sessions = get_option(USER_SESSIONS);
		
			array_push($sessions, array(
				'id' => $sessionID,
				'created' => $timestamp)
			);
			update_option(USER_SESSIONS, $sessions);

			$sessionData = array();
			add_option($sessionID, $sessionData);
			$session = get_option($sessionID);
		} 
		
		// Encrypt data and store key-value pair
		$value = $this->_encrypt($key, $value, $iv);
		$session[$skey] = "$value";
		
		// Update database
		update_option($sessionID, $session);
		
	}
	
	private function _getSessionInfoFromProfile() {
		$data = get_user_meta($this->userID, SESSION_NAME, true);
		return $data;
	}
	
	private function _getSessionInfoFromCookie() {
		if (SBTCookie::get(SESSION_NAME) == null) {
			$this->_initCookie();
		}
		
		if (SBTCookie::get(SESSION_NAME) != null) {
			$encrypted = SBTCookie::get(SESSION_NAME);
			$crypt = get_option(CRYPT);
			$key = $crypt['key'];
			$iv = $crypt['iv'];
			$iv = base64_decode($iv);
			$data = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key , base64_decode($encrypted), MCRYPT_MODE_CBC, $iv), "\0");
			$data = unserialize($data);
			return $data;
		}
		return null;
	}
	
	/**
	 * Returns a value from the credentials store
	 * 
	 * @param string $skey
	 * @return 
	 */
	private function _get($skey) {
		$data = null;
		if ($this->_isUserLoggedIn()) {
			$data = $this->_getSessionInfoFromProfile();
		} else {
			$data = $this->_getSessionInfoFromCookie();
		}
		$key = $data['key'];
		$iv = $data['iv'];
		$iv = base64_decode($iv);
		$sessionID = $data['sessionID'];
		
		// Get session
		$session = get_option($sessionID);
		
		if ($session === false) {
			return;
		} else {
			// Get value, decrypt and return
			if (!isset($session[$skey]) || !$session[$skey]) {
				return null;
			}
			$value = $session[$skey];
			if ($value == "" || $value == null) {
				return null;
			}
			$value = $this->_decrypt($key, $value, $iv);
			return $value;
		}
		
		return null;
	}
	/**
	 * Deletes an entry from the credentials store.
	 *
	 * @param string $key
	 */
	private function _delete($skey) {

		$data = null;
		if ($this->_isUserLoggedIn()) {
			$data = $this->_getSessionInfoFromProfile();
		} else {
			$data = $this->_getSessionInfoFromCookie();
		}
		
		$key = $data['key'];
		$iv = $data['iv'];
		$iv = base64_decode($iv);
		$sessionID = $data['sessionID'];
		
		// Get session
		$session = get_option($sessionID);
		
		if ($session === false) {
			return;
		} else {
			// Delete entry and update
			unset($session[$skey]);
			update_option($sessionID, $session);
		}
	}
	
	/**
	 * Destroys the credential store (i.e. removes the session from the database).
	 *
	 * @param string $key
	 */
	public function destroyStore($endpointName = "connections") {
		
		if ($this->_isUserLoggedIn()) {
			$data = get_user_meta($this->userID, SESSION_NAME, true);
			delete_option($data['sessionID']);
			delete_user_meta($this->userID, SESSION_NAME);
		} else {
			$data = $this->_getSessionInfoFromCookie();
			
			$sessionID = $data['sessionID'];
			delete_option($sessionID);
			
			$sessions = get_option(USER_SESSIONS);
				
			foreach ($sessions as $session) {
				if ($session['id'] == $sessionID) {
					unset($session['id']);
					unset($session['created']);
				}
			}
			update_option(USER_SESSIONS, $sessions);
		}
	}
	
	public function storeToken($token, $endpointName = "connections") {
		$this->_store('__SBTK_TOKEN', $token);
	}
	
	/**
	 * Stores the OAuth request token in the database.
	 *
	 * @param string $token			The OAuth request token.
	 */
	public function storeRequestToken($token, $endpointName = "connections") {
		$this->_store('__REQUEST_SBTK_TOKEN', $token);
	}
	
	/**
	 * Returns a request token.
	 *
	 * @return string 		A request token.
	 */
	public function getRequestToken($endpointName = "connections") {
		$token = $this->_get('__REQUEST_SBTK_TOKEN');
		return $token;
	}

	/**
	 * Returns a token.
	 *
	 * @return string 		A token.
	 */
	public function getToken($endpointName = "connections") {
		return $this->_get('__SBTK_TOKEN');
	}
	
	/**
	 * Deletes the OAuth tokens.
	 */
	public function deleteTokens($endpointName = "connections") {
		$this->_delete('__SBTK_TOKEN');
		$this->_delete('__SBTK_TOKEN_TYPE');
		$this->_delete('__OAUTH_SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_TOKEN_SECRET');
		$this->_delete('__REQUEST_SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_VERIFIER_TOKEN');
		$this->_delete('__OAUTH_REQUEST_TOKEN_SECRET');
	}
	
	/**
	 * Stores the OAuth access token in the database.
	 * 
	 * @param string $token			The OAuth access token.
	 */
	public function storeOAuthAccessToken($token, $endpointName = "connections") {
		$this->_store('__OAUTH_SBTK_TOKEN', $token);
	}
	
	
	public function storeTokenSecret($token, $endpointName = "connections") {
		$this->_store('__OAUTH_SBTK_TOKEN_SECRET', $token);
	}
	
	public function storeVerifierToken($token, $endpointName = "connections") {
		$this->_store('__OAUTH_SBTK_VERIFIER_TOKEN', $token);
	}
	
	public function getTokenSecret($endpointName = "connections") {
		return $this->_get('__OAUTH_SBTK_TOKEN_SECRET');
	}
	
	/**
	 * Returns OAuth access token.
	 *
	 * @return string 		OAuth access token.
	 */
	public function getOAuthAccessToken($endpointName = "connections") {
		return $this->_get('__OAUTH_SBTK_TOKEN');
	}
	
	/**
	 * Returns the verifier token.
	 *
	 * @return string 		OAuth verifier token.
	 */
	public function getVerifierToken($endpointName = "connections") {
		return $this->_get('__OAUTH_SBTK_VERIFIER_TOKEN');
	}
	
	/**
	 * Stores the basic authentication username in the database.
	 *
	 * @param string $username		The username used to perform basic authentication.
	 */
	public function storeBasicAuthUsername($username, $endpointName = "connections") {
		$this->_store('__BASIC_AUTH_SBTK_USERNAME', $username);
	}
	
	/**
	 * Returns basic auth username.
	 * 
	 * @return string 		Basic auth username.
	 */
	public function getBasicAuthUsername($endpointName = "connections") {
		return $this->_get('__BASIC_AUTH_SBTK_USERNAME');
	}
	
	/**
	 * Stores the OAuth 1.0 request token secret
	 *
	 * @param string $token		The OAuth 1.0 request token secret
	 */
	public function storeRequestTokenSecret($token, $endpointName = "connections") {
		$this->_store('__OAUTH_REQUEST_TOKEN_SECRET', $token);
	}
	
	/**
	 * Returns basic auth username.
	 *
	 * @return string 		Basic auth username.
	 */
	public function getRequestTokenSecret($endpointName = "connections") {
		return $this->_get('__OAUTH_REQUEST_TOKEN_SECRET');
	}
	
	
	/**
	 * Stores the basic authentication password in the database.
	 * 
	 * @param string $password		The password used to perform basic authentication.
	 */
	public function storeBasicAuthPassword($password, $endpointName = "connections") {
		$this->_store('__BASIC_AUTH_SBTK_PASSWORD', $password);
	}
	
	/**
	 * Returns basic auth password.
	 *
	 * @return string 		Basic auth password.
	 */
	public function getBasicAuthPassword($endpointName = "connections") {
		return $this->_get('__BASIC_AUTH_SBTK_PASSWORD');
	}
	
	/**
	 * Deletes stored basic authentication credentials.
	 */
	public function deleteBasicAuthCredentials($endpointName = "connections") {
		$this->_delete('__BASIC_AUTH_SBTK_PASSWORD');
		$this->_delete('__BASIC_AUTH_SBTK_USERNAME');
		// Destroy cookie
		SBTCookie::set(SESSION_NAME, "", time() - 3600);
	}
	
	/**
	 * Deletes stored OAuth credentials.
	 */
	public function deleteOAuthCredentials($endpointName = "connections") {
		$this->_delete('__SBTK_TOKEN_TYPE');
		$this->_delete('__OAUTH_SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_TOKEN_SECRET');
		$this->_delete('__REQUEST_SBTK_TOKEN');
		$this->_delete('__SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_VERIFIER_TOKEN');
		$this->_delete('__OAUTH_REQUEST_TOKEN_SECRET');

		// Destroy cookie
		SBTCookie::set(SESSION_NAME, "", time() - 3600);
	}
	
	public function storeTokenType($tokenType) {
		$this->_store('__SBTK_TOKEN_TYPE', $tokenType);
	}
	
	public function getTokenType() {
		return $this->_get('__SBTK_TOKEN_TYPE');
	}
	
	private function _isUserLoggedIn() {
		$data = SBTCookie::get(WP_SESSION_INDICATOR);
		$this->userID  = $data;

		return $this->userID > 0;
	}
	
	
	/**
	 * Generates a random string of given length.
	 *
	 * @param int $length		Desired length of the string.
	 *
	 * @return string			Random string.
	 */
	private function gen_string($length) {
		$str = "";
		for ($i = 0; $i < $length; $i++) {
			$str .= chr(mt_rand(33, 126));
		}
		return $str;
	}
	
	private function _encrypt($key, $data, $iv){
		$b = mcrypt_get_block_size(MCRYPT_RIJNDAEL_256, MCRYPT_MODE_CBC);
		$enc = mcrypt_module_open(MCRYPT_RIJNDAEL_256, '', MCRYPT_MODE_CBC, '');
		mcrypt_generic_init($enc, $key, $iv);
	
		$dataPad = $b - (strlen($data) % $b);
		$data .= str_repeat(chr($dataPad), $dataPad);
	
		$encrypted_data = mcrypt_generic($enc, $data);
	
		mcrypt_generic_deinit($enc);
		mcrypt_module_close($enc);
	
		return addslashes(base64_encode($encrypted_data));
	}
	
	private function _decrypt($key, $encryptedData, $iv) {
		$encryptedData = stripslashes($encryptedData);

		$enc = mcrypt_module_open(MCRYPT_RIJNDAEL_256, '', MCRYPT_MODE_CBC, '');
		mcrypt_generic_init($enc, $key, $iv);
	
		$encryptedData = base64_decode($encryptedData);
		$data = mdecrypt_generic($enc, $encryptedData);
		mcrypt_generic_deinit($enc);
		mcrypt_module_close($enc);
	
		// PKCS7 Padding from: https://gist.github.com/1077723
		$dataPad = ord($data[strlen($data)-1]);
	
		return substr($data, 0, -$dataPad);
	}
}
