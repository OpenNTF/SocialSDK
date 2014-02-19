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

// Define constants
if (!defined('SESSION_NAME')) {
	define('SESSION_NAME', 'ibm_sbtk_session');
}

if (!defined('SESSION_PREFIX')) {
	define('SESSION_PREFIX', 'ibm_sbtk_');
}

if (!defined('CRYPT')) {
	$path = str_replace('core', '', BASE_PATH);
	require_once $path . '/ibm-sbtk-constants.php';
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
		require_once '../../../../wp-includes/formatting.php';
	} else if (file_exists('../../../wp-includes/formatting.php')) {
		require_once '../../../wp-includes/formatting.php';
	}
}

if (file_exists(BASE_PATH . '/core/models/SBTKPHPCookieAdapter.php')) {
	require BASE_PATH . '/core/models/SBTKPHPCookieAdapter.php';
} else {	
	require BASE_PATH . '/models/SBTKCookie.php';
	require BASE_PATH . '/models/SBTKCookieAdapter.php';
	require BASE_PATH . '/models/SBTKMemoryCookieAdapter.php';
	require BASE_PATH . '/models/SBTKPHPCookieAdapter.php';
}

/**
 * Credential Store for authorization tokens.
 *
 * @author Benjamin Jakobus
 */
class CredentialStore {
	
	// Session name
	private $sessionName;
	
	// Encryption key
	private $key;
	
	// Initialization vector
	private $iv;
	
	/**
	 * Constructor.
	 *
	 * @param $cookieAdapter	The cookie adapter to use. Use SBTKMemoryCookieAdapter when 
	 * 							testing; use SBTKPHPCookieAdapter when running live (default).
	 * 
	 * @author Benjamin Jakobus
	 */
	function __construct($cookieAdapter = null) {
		if ($cookieAdapter == null) {
			$cookieAdapter = new SBTKPHPCookieAdapter();
		}
		SBTKCookie::init($cookieAdapter);
		
		// Check if the user has a key. If not, generate one and save it
		if (SBTKCookie::get(SESSION_NAME) != null) {
			
			$encrypted = SBTKCookie::get(SESSION_NAME);
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
	
	/**
	 * Sets the cookie.
	 * 
	 * @author Benjamin Jakobus
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
		SBTKCookie::set(SESSION_NAME, $encrypted, $timestamp + 86400);
			
		$this->key = $pivKey;
		$this->iv = $pivIv;
	}
	
	/**
	 * Stores a value in the credential store.
	 * 
	 * @param string $skey
	 * @param string $value
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _store($skey, $value) {		
		if (SBTKCookie::get(SESSION_NAME) == null) {
			$this->_initCookie();
		}
		
		if (SBTKCookie::get(SESSION_NAME) != null) {
			$encrypted = SBTKCookie::get(SESSION_NAME);
			$crypt = get_option(CRYPT);
			$key = $crypt['key'];
			$iv = $crypt['iv'];
			$iv = base64_decode($iv);
			$data = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key , base64_decode($encrypted), MCRYPT_MODE_CBC, $iv), "\0");
			$data = unserialize($data);
			$key = $data['key'];
			$iv = $data['iv'];
			$iv = base64_decode($iv);
			$sessionID = $data['sessionID'];
	
			// Get session 
			$session = get_option($sessionID);
	
			if ($session === false) {
				return;
			} else {
				// Encrypt data and store key-value pair
				$value = $this->_encrypt($key, $value, $iv);
				$session[$skey] = "$value";
				
				// Update database
				update_option($sessionID, $session);
			}
		}
	}
	
	/**
	 * Returns a value from the credentials store
	 * 
	 * @param string $skey
	 * @return 
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _get($skey) {
		if (SBTKCookie::get(SESSION_NAME) != null) {
			$encrypted = SBTKCookie::get(SESSION_NAME);
			$crypt = get_option(CRYPT);
			$key = $crypt['key'];
			$iv = $crypt['iv'];
			$iv = base64_decode($iv);
			$data = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key , base64_decode($encrypted), MCRYPT_MODE_CBC, $iv), "\0");
			
			$data = unserialize($data);
			$key = $data['key'];
			$iv = $data['iv'];
			$iv = base64_decode($iv);
			$sessionID = $data['sessionID'];

			// Get session
			$session = get_option($sessionID);

			// TODO: If session doesn't exist
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
		}
		
		return null;
	}
	
	/**
	 * Deletes an entry from the credentials store.
	 *
	 * @param string $key
	 *
	 * @author Benjamin Jakobus
	 */
	private function _delete($skey) {
	if (SBTKCookie::get(SESSION_NAME) != null) {
			$encrypted = SBTKCookie::get(SESSION_NAME);
			$crypt = get_option(CRYPT);
			$key = $crypt['key'];
			$iv = $crypt['iv'];
			$iv = base64_decode($iv);
			$data = rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_256, $key, base64_decode($encrypted), MCRYPT_MODE_CBC, $iv), "\0");
			$data = unserialize($data);
			$key = $data['key'];
			$iv = $data['iv'];
			$iv = base64_decode($iv);
			$sessionID = $data['sessionID'];
			
			// Get session 
			$session = get_option($sessionID);
			// TODO: If session doesn't exist
			if ($session === false) {
				return;
			} else {
				// Delete entry and update
				unset($session[$skey]);
				update_option($sessionID, $session);
			}
		}
	}
	
	public function storeToken($token) {
		$this->_store('__SBTK_TOKEN', $token);
	}
	
	/**
	 * Stores the OAuth request token in the database.
	 *
	 * @param string $token			The OAuth request token.
	 *
	 * @author Benjamin Jakobus
	 */
	public function storeRequestToken($token) {
		$this->_store('__REQUEST_SBTK_TOKEN', $token);
	}
	
	/**
	 * Returns a request token.
	 *
	 * @return string 		A request token.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getRequestToken() {
		$token = $this->_get('__REQUEST_SBTK_TOKEN');
		return $token;
	}

	/**
	 * Returns a token.
	 *
	 * @return string 		A token.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getToken() {
		return $this->_get('__SBTK_TOKEN');
	}
	
	/**
	 * Deletes the OAuth tokens.
	 *
	 * @author Benjamin Jakobus
	 */
	public function deleteTokens() {
		$this->_delete('__SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_TOKEN');
		$this->_delete('__REQUEST_SBTK_TOKEN');
	}
	
	/**
	 * Stores the OAuth access token in the database.
	 * 
	 * @param string $token			The OAuth access token.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function storeOAuthAccessToken($token) {
		$this->_store('__OAUTH_SBTK_TOKEN', $token);
	}
	
	
	public function storeTokenSecret($token) {
		$this->_store('__OAUTH_SBTK_TOKEN_SECRET', $token);
	}
	
	public function getTokenSecret() {
		return $this->_get('__OAUTH_SBTK_TOKEN_SECRET');
	}
	
	/**
	 * Returns OAuth access token.
	 *
	 * @return string 		OAuth access token.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getOAuthAccessToken() {
		return $this->_get('__OAUTH_SBTK_TOKEN');
	}
	
	/**
	 * Stores the basic authentication username in the database.
	 *
	 * @param string $username		The username used to perform basic authentication.
	 *
	 * @author Benjamin Jakobus
	 */
	public function storeBasicAuthUsername($username) {
		$this->_store('__BASIC_AUTH_SBTK_USERNAME', $username);
	}
	
	/**
	 * Returns basic auth username.
	 * 
	 * @return string 		Basic auth username.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthUsername() {
		return $this->_get('__BASIC_AUTH_SBTK_USERNAME');
	}
	
	/**
	 * Stores the basic authentication password in the database.
	 * 
	 * @param string $password		The password used to perform basic authentication.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function storeBasicAuthPassword($password) {
		$this->_store('__BASIC_AUTH_SBTK_PASSWORD', $password);
	}
	
	/**
	 * Returns basic auth password.
	 *
	 * @return string 		Basic auth password.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthPassword() {
		return $this->_get('__BASIC_AUTH_SBTK_PASSWORD');
	}
	
	/**
	 * Deletes stored basic authentication credentials.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function deleteBasicAuthCredentials() {
		$this->_delete('__BASIC_AUTH_SBTK_PASSWORD');
		$this->_delete('__BASIC_AUTH_SBTK_USERNAME');
	}
	
	public function storeTokenType($tokenType) {
		$this->_store('__SBTK_TOKEN_TYPE', $tokenType);
	}
	
	public function getTokenType() {
		return $this->_get('__SBTK_TOKEN_TYPE');
	}
	
	
	/**
	 * Generates a random string of given length.
	 *
	 * @param int $length		Desired length of the string.
	 *
	 * @return string			Random string.
	 *
	 * @author Benjamin Jakobus
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
	
		$***REMOVED*** $b - (strlen($data) % $b);
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
	
		***REMOVED***
		***REMOVED***
	
		return substr($data, 0, -$dataPad);
	}
}