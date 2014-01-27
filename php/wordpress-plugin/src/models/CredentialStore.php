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
 * Credential Store for authorization tokens
 *
 * @author Lorenzo Boccaccia
 * @author Benjamin Jakobus
 */
class CredentialStore {
	
	// TODO: Encrypt session & cookies
	
	// Storage type
	private $storeType;
	
	// Session name
	private $sessionName;
	
	/**
	 * Load variables from config.php and make them private members of
	 * this class.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		include BASE_PATH . '/config.php';
		
		$this->storeType = $config['credentialStoreType'];
		$this->sessionName = $config['session_name'];
		
		session_name($this->sessionName);
		
		if (!session_id()) {
			session_start();
		}
	}
	
	/**
	 * Stores a value in the credential store.
	 * 
	 * @param string $key
	 * @param unknown $value
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _store($key, $value) {
		$store = null;
	
		if ($this->storeType == 'session') {
			
			if (isset($_SESSION['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_SESSION['__IBM_SBTK_CRED_STORE']);
				
			} else {
				$store = array();
			}
			
			$store[$key] = $value;
			
			$_SESSION['__IBM_SBTK_CRED_STORE'] = serialize($store);
		} else if ($this->storeType == 'cookie') {
			if (isset($_COOKIE['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_COOKIE['__IBM_SBTK_CRED_STORE']);
			} else {
				$store = array();
			}
			$store[$key] = $value;

			// Expiry time is next week
			$nextWeek = time() + (7 * 24 * 60 * 60);
			$ret = setcookie( "__IBM_SBTK_CRED_STORE", serialize($store), $nextWeek);
		}
	}
	
	/**
	 * Returns a value from the credentials store
	 * 
	 * @param string $key
	 * @return 
	 * 
	 * @author Benjamin Jakobus
	 */
	private function _get($key) {
		if ($this->storeType == 'session') {
			
			if (isset($_SESSION['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_SESSION['__IBM_SBTK_CRED_STORE']);
				return (isset($store[$key]) ? $store[$key] : null);
			} 
		} else if ($this->storeType == 'cookie') {
			if (isset($_COOKIE['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_COOKIE['__IBM_SBTK_CRED_STORE']);
				$this->_get($key);
				return (isset($store[$key]) ? $store[$key] : null);
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
	private function _delete($key) {
		$store = null;
		if ($this->storeType == 'session') {
				
			if (isset($_SESSION['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_SESSION['__IBM_SBTK_CRED_STORE']);
		
			} else {
				$store = array();
			}
				
			if (isset($store[$key])) {
				unset($store[$key]);
			}
				
			$_SESSION['__IBM_SBTK_CRED_STORE'] = serialize($store);
				
		} else if ($this->storeType == 'cookie') {
			if (isset($_COOKIE['__IBM_SBTK_CRED_STORE'])) {
				$store = unserialize($_COOKIE['__IBM_SBTK_CRED_STORE']);
			} else {
				$store = array();
			}
			if (isset($store[$key])) {
				unset($store[$key]);
			}
				
			// Expiry time is next week
			$nextWeek = time() + (7 * 24 * 60 * 60);
			setcookie( "__IBM_SBTK_CRED_STORE", serialize($store), $nextWeek);
		} 
	}
	
	public function storeToken($token) {
		$this->_store('__SBTK_TOKEN', $token);
	}
	
	public function storeRequestToken($token) {
		$this->_store('__REQUEST_SBTK_TOKEN', $token);
	}
	
	public function getRequestToken() {
		$token = $this->_get('__REQUEST_SBTK_TOKEN');
		return $token;
	}

	public function getToken() {
		return $this->_get('__SBTK_TOKEN');
	}
	
	public function deleteTokens() {
		$this->_delete('__SBTK_TOKEN');
		$this->_delete('__OAUTH_SBTK_TOKEN');
		$this->_delete('__REQUEST_SBTK_TOKEN');
	}
	
	public function storeOAuthAccessToken($token) {
		$this->_store('__OAUTH_SBTK_TOKEN', $token);
	}
	
	
	public function getOAuthAccessToken() {
		return $this->_get('__OAUTH_SBTK_TOKEN');
	}
	
	public function storeBasicAuthUsername($token) {
		$this->_store('__BASIC_AUTH_SBTK_USERNAME', $token);
	}
	
	
	public function getBasicAuthUsername() {
		return $this->_get('__BASIC_AUTH_SBTK_USERNAME');
	}
	
	public function storeBasicAuthPassword($token) {
		$this->_store('__BASIC_AUTH_SBTK_PASSWORD', $token);
	}
	
	
	public function getBasicAuthPassword() {
		return $this->_get('__BASIC_AUTH_SBTK_PASSWORD');
	}
	
	public function deleteBasicAuthCredentials() {
		$this->_delete('__BASIC_AUTH_SBTK_PASSWORD');
		$this->_delete('__BASIC_AUTH_SBTK_USERNAME');
	}
}