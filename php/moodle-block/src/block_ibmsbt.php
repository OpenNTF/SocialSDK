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
 * SBTK Moodle block
 *
 * @author Benjamin Jakobus
 */

define('IBM_SBT_MOODLE_BLOCK', 'IBM SBT MOODLE BLOCK');
class block_ibmsbt extends block_base {
	
	/**
	 * Init.
	 */
	public function init() {
		$this->title = get_string('ibmsbt', 'block_ibmsbt');
	}
	
	/**
	 * Returns the block contents.
	 * 
	 * @return stdClass		Block content.
	 */
	public function get_content() {
		if ($this->content !== null) {
			return $this->content;
		}	

		$this->content = new stdClass();
	
		if (!isset($this->config->plugin) || $this->config->plugin == '') {
			return $this->content;
		}
		
		if (isset($this->config->plugin)) {
			ob_start();
			
			if (!defined('BASE_LOCATION')) {
				$autoload = __DIR__ . '/core/autoload.php';
				include $autoload;
			}
			
			$this->loadModel('SBTSettings');
			$this->loadModel('SBTCredentialStore');

			$settings = new SBTSettings();
			$store = SBTCredentialStore::getInstance();	
			
			global $CFG;
			
			$blockPath = $CFG->dirroot . '/blocks/ibmsbt/';
			
			
			// If the user clicked "login", then trigger the OAuth dance
			if (isset($_COOKIE['IBMSBTKOAuthLogin']) && $_COOKIE['IBMSBTKOAuthLogin'] == 'yes' && $store->getOAuthAccessToken($this->config->endpoint) == null) {
				if ($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth1' || 
					$settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2') {
					$store->deleteOAuthCredentials($this->config->endpoint);
					$this->_startOAuthDance($settings);
					$this->content->text = ob_get_clean();
					return;
				}
			}
			
			// Check if token expired. If yes, clear the credential store and load login display
			if (($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth1' 
					|| $settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2') && $store->getOAuthAccessToken($this->config->endpoint) != null) {
				$endpoint = null;
				if ($settings->getAuthenticationMethod($this->config->endpoint) == "oauth2") {
					$endpoint = new SBTOAuth2Endpoint();
				} else if ($settings->getAuthenticationMethod($this->config->endpoint) == "oauth1") {
					$endpoint = new SBTOAuth1Endpoint();
				}
			
				$service = '/files/basic/api/myuserlibrary/feed';
		
				$response = $endpoint->makeRequest($settings->getURL($this->config->endpoint), $service, 'GET', null, null);
				if (is_string($response)) {
					echo response;
					$this->content->text = ob_get_clean();
					return;
				} else if ($response->getStatusCode() == 401) {
					$store->deleteOAuthCredentials($this->config->endpoint);
				}
			}

			echo '<div name="ibm_sbtk_widget">';
			if (($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth1' 
					|| $settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2') && $store->getOAuthAccessToken($this->config->endpoint) == null &&
			(!isset($_COOKIE['IBMSBTKOAuthLogin']) || $_COOKIE['IBMSBTKOAuthLogin'] != 'yes')) {
				if (!isloggedin()) {
					echo "This widget uses single-sign on. Please log into Moodle.";
				} else {
					require $blockPath . '/core/views/oauth-login-display.php';
				}
				echo '</div>';
				$this->content->text = ob_get_clean();
				return;
		
			}
		
			$plugin = new SBTPlugin($this->config->endpoint);
			$plugin->createHeader();
			if (($settings->getAuthenticationMethod($this->config->endpoint) == 'basic' && $store->getBasicAuthUsername($this->config->endpoint) != null 
				&& $store->getBasicAuthPassword($this->config->endpoint) != null) || ($settings->getAuthenticationMethod() == 'oauth1' && $store->getRequestToken($this->config->endpoint) != null)
				|| ($settings->getAuthenticationMethod($this->config->endpoint) == 'basic' && $settings->getBasicAuthMethod($this->config->endpoint) == 'global')
				|| ($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2' && $store->getOAuthAccessToken($this->config->endpoint) != null)) {
				require $this->config->plugin;
				
				if ($settings->getAuthenticationMethod($this->config->endpoint) == 'basic') {
					require $blockPath . '/core/views/endpoint-logout.php';
					echo '<button onclick="ibm_sbt_endpoint_logout()">Logout from this Endpoint</button>';
				}
				
			}

			if ($settings->getAuthenticationMethod($this->config->endpoint) == 'basic' && $settings->getBasicAuthMethod($this->config->endpoint) == 'prompt' 
					&& $store->getBasicAuthUsername($this->config->endpoint) == null ) {
				if (!isloggedin()) {
					echo "This widget uses single-sign on. Please log into Moodle.";
				} else {
					require_once $blockPath . '/core/views/basic-auth-login-display.php';
				}
			} else if ($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth1' 
					|| $settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2') {
	// 			require_once BASE_PATH . '/views/oauth-logout-display.php'; TODO: Uncomment when OAuth logout has been fixed
			}
			

			if (($settings->getAuthenticationMethod($this->config->endpoint) == 'oauth1'
					|| $settings->getAuthenticationMethod($this->config->endpoint) == 'oauth2') && $store->getOAuthAccessToken($this->config->endpoint) != null) {
				require $blockPath . '/core/views/endpoint-logout.php';
				echo '<button onclick="ibm_sbt_endpoint_logout()">Logout from this Endpoint</button>';
			} 
			
			echo '</div>';

			$this->content->text = ob_get_clean();
		}
		return $this->content;
	}

	/**
	 * Block specilization.
	 */
	public function specialization() {
		if (! empty ( $this->config->title )) {
			$this->title = $this->config->title;
		} else {
			$this->config->title = 'Default title ...';
		}
		
		if (empty ( $this->config->text )) {
			$this->config->text = 'Default text ...';
		}
	}
	private function _startOAuthDance($settings) {
		global $USER;
		if (isset($USER->id)) {
			setcookie('ibm-sbt-uid', $USER->id, time() + 604800);
		}
		
		
		$authMethod = $settings->getAuthenticationMethod($this->config->endpoint);
		if ($authMethod == 'oauth1') {
			// Check if we have an access token. If not, re-direct user to authentication page
			$this->loadModel('SBTCredentialStore');
			$store = SBTCredentialStore::getInstance();
			$token = $store->getRequestToken($this->config->endpoint);

			if ($token == null) {
			
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
					$body = $oauth->request($url, BASE_LOCATION . '/index.php?plugin=guzzle&class=SBTOAuth1Endpoint&method=authenticationCallback', 'POST', $this->config->endpoint);
				} else {
					$body = $oauth->request($url, BASE_LOCATION . '/core/index.php?plugin=guzzle&class=SBTOAuth1Endpoint&method=authenticationCallback', 'POST', $this->config->endpoint);
				}
				var_dump($body);
			}
		} else if ($authMethod == 'oauth2') {
			// Check if we have an access token. If not, re-direct user to authentication page
			$this->loadModel('SBTCredentialStore');
			$store = SBTCredentialStore::getInstance();
			$token = $store->getOAuthAccessToken($this->config->endpoint);
				
			if ($token == null) {					
				$parameters = array(
						'response_type' => 'code',
						'client_id'     => $settings->getConsumerKey($this->config->endpoint),
						'callback_uri'  => $settings->getOAuth2CallbackURL($this->config->endpoint)
				);
					
				$authURL = $settings->getAuthorizationURL() . '?' . http_build_query($parameters, null, '&');
		
				if (!headers_sent()) {
					header("Location: " . $authURL);
				} else {
					echo '<script type="text/javascript" language="javascript">window.location = "' . $authURL . '";</script>';
				}
			}
		}
	}
	
	/**
	 * Allow multiple instances of this block.
	 * 
	 * @return boolean		True - we want to allow multiple instances of this
	 * 						block to be used.
	 */
	public function instance_allow_multiple() {
		return true;
	}
	
	/**
	 * Allow global configuration.
	 *
	 * @return boolean		True - we want to allow multiple instances of this
	 * 						block to be used.
	 */
	function has_config() {
		return true;
	}
	
	/**
	 * Loads the specified model.
	 * 
	 * @param string $model			The model name.
	 */
	function loadModel($model) {
		global $CFG;
		$file = $CFG->dirroot . '/blocks/ibmsbt/core/models/' . $model . '.php';
		include_once $file;
	}
}   