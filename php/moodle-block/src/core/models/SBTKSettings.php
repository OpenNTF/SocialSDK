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
 * Wrapper for the Wordpress options API - encapsulates the notion of an endpoint setting.
 * 
 * @author Benjamin Jakobus
 */

class SBTKSettings {
	
	// Endpoint URL
	private $url;
	
	// Consumer key
	private $consumerKey;
	
	// Consumer secret
	private $consumerSecret;
	
	// Request token URL
	private $requestTokenURL;
	
	// Authorization URL
	private $authorizationURL;
	
	// Access token URL
	private $accessTokenURL;
	
	// Authentication method
	private $authenticationMethod;
	
	// SDK deployment URL
	private $sdkDeployURL;
	
	// Community grid settings
	private $communityGridSettings;
	
	// Files grid settings
	private $filesGridSettings;
	
	// Bookmarks grid settings
	private $bookmarksGridSettings;
	
	// Forums grid settings
	private $forumsGridSettings;
	
	// Endpoint name
	private $name;
	
	// Basic authentication username
	private $basicAuthUsername;
	
	// Basic authentication password
	private $basicAuthPassword;
	
	// Basic authentication method - must be global|prompt|profile
	private $basicAuthMethod;
	
	/**
	 * Constructor.
	 *
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		$this->_init();
	}
	
	/**
	 * Load variables from database and make them private members of
	 * this class.
	 * 
	 * @return
	 * @author Benjamin Jakobus
	 */
	private function _init() {
		global $DB;
		include_once '../../../config.php';

		$records = $DB->get_records('config', array());
		
		$configData = array();

		foreach ($records as $record) {
			$configData[$record->name] = $record->value;
		}
		
		$this->url = ($configData['auth_type'] == 'basic' ? $configData['server_url'] : $configData['o_auth_server_url']);
		$this->consumerKey = $configData['consumer_key'];
		$this->consumerSecret = $configData['consumer_secret'];
		$this->requestTokenURL = $configData['request_token_url'];
		$this->authorizationURL = $configData['authorization_url'];
		$this->accessTokenURL = $configData['access_token_url'];
		$this->authenticationMethod = $configData['auth_type'];
		$this->sdkDeployURL = $configData['sdk_deploy_url'];
		$this->basicAuthUsername = $configData['basic_auth_username'];
		$this->basicAuthPassword = $configData['basic_auth_password'];
		$this->basicAuthMethod = $configData['basic_auth_method'];
		
		$this->name = ''; // Not needed for Moodle
		$config['basic_auth_username'] = $configData['basic_auth_username'];
		$config['basic_auth_username'] = $configData['basic_auth_password'];

		// Community grid default settings
		$this->communityGridSettings['hidePager'] = true;
		$this->communityGridSettings['hideSorter'] = true;
		$this->communityGridSettings['containerType'] = "ul";
		
		// Bookmarks grid default settings
		$this->bookmarksGridSettings['hidePager'] = true;
		$this->bookmarksGridSettings['hideSorter'] = true;
		$this->bookmarksGridSettings['containerType'] = "ul";
		
		// Files grid default settings
		$this->filesGridSettings['hidePager'] = true;
		$this->filesGridSettings['hideSorter'] = true;
		$this->filesGridSettings['containerType'] = "ul";
		
		// Forums grid default settings
		$this->forumsGridSettings['hidePager'] = true;
		$this->forumsGridSettings['hideSorter'] = true;
		$this->forumsGridSettings['containerType'] = "ul";
	}
	
	/**
	 * Returns the endpoint name.
	 * 
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getName() {
		return $this->name;
	}
	
	/**
	 * Returns an array containing various community grid settings.
	 * 
	 * @return array
	 * @author Benjamin Jakobus
	 */
	public function getCommunityGridSettings() {
		return $this->communityGridSettings;
	}
	
	/**
	 * Returns an array containing various bookmarks grid settings.
	 *
	 * @return array
	 * @author Benjamin Jakobus
	 */
	public function getBookmarksGridSettings() {
		return $this->bookmarksGridSettings;
	}
	
	/**
	 * Returns an array containing various files grid settings.
	 *
	 * @return array
	 * @author Benjamin Jakobus
	 */
	public function getFilesGridSettings() {
		return $this->filesGridSettings;
	}
	
	/**
	 * Returns an array containing various forums grid settings.
	 *
	 * @return array
	 * @author Benjamin Jakobus
	 */
	public function getForumsGridSettings() {
		return $this->forumsGridSettings;
	}
	
	/**
	 * Returns the endpoint URL.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function getURL() {
		return $this->url;
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getConsumerKey() {
		return $this->consumerKey;
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getConsumerSecret() {
		return $this->consumerSecret;
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getRequestTokenURL() {
		return $this->requestTokenURL;
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getAuthorizationURL() {
		return $this->authorizationURL;
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @author Benjamin Jakobus
	 */
	public function getAccessTokenURL() {
		return $this->accessTokenURL;
	}
	
	/**
	 * Returns the authentication method.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getAuthenticationMethod() {
		return $this->authenticationMethod;
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getSDKDeployURL() {
		return $this->sdkDeployURL;
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthUsername() {
		return $this->basicAuthUsername;
	}
	
	/**
	 * Returns the authentication method used for basic authentication.
	 *
	 * @return string		global|profile|prompt
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthMethod() {
		return $this->basicAuthMethod;
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthPassword() {
		return $this->basicAuthPassword;
	}
} 