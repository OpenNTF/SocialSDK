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
class SBTKSettings {
	
	// The endpoint to which the user is currently connecting
	private $selectedEndpoint; 
	// Misc SDK settings
	private $sdkSettings;
	// Plugin settings
	private $pluginSettings;
	
	// Community grid settings
	private $communityGridSettings;
	
	// Files grid settings
	private $filesGridSettings;
	
	// Forums grid settings
	private $forumsGridSettings;
	
	// Bookmarks grid settings
	private $bookmarksGridSettings;
	
	/**
	 * Constructor.
	 *
	 * @author Benjamin Jakobus
	 */
	function __construct() {
		
		// If the SBTKSettings file is called outside the wordpress context, then
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
		
		// Fetch all available endpoints
		$endpoints = get_option('my_endpoints');
		$this->sdkSettings = get_option('sbt_sdk_settings');
		
		// Fetch plugin settings
		$this->pluginSettings = get_option('my_plugin_settings');
		
		// Fetch community grid settings
		$grid = get_option('community_grid_settings');
		$this->communityGridSettings['hidePager'] = ($grid['community_grid_pager'] == 'yes' ? 0 : 1);
		$this->communityGridSettings['hideSorter'] = ($grid['community_grid_sorter'] == 'yes' ? 0 : 1);
		$this->communityGridSettings['containerType'] = $grid['community_grid_container_type'];
		
		$this->bookmarksGridSettings['hidePager'] = ($grid['bookmark_grid_pager'] == 'yes' ? 0 : 1);
		$this->bookmarksGridSettings['hideSorter'] = ($grid['bookmark_grid_sorter'] == 'yes' ? 0 : 1);
		$this->bookmarksGridSettings['containerType'] = $grid['bookmark_grid_container_type'];
		
		$this->filesGridSettings['hidePager'] = ($grid['files_grid_pager'] == 'yes' ? 0 : 1);
		$this->filesGridSettings['hideSorter'] = ($grid['files_grid_sorter'] == 'yes' ? 0 : 1);
		$this->filesGridSettings['containerType'] = $grid['files_grid_container_type'];
		
		$this->forumsGridSettings['hidePager'] = ($grid['forums_grid_pager'] == 'yes' ? 0 : 1);
		$this->forumsGridSettings['hideSorter'] = ($grid['forums_grid_sorter'] == 'yes' ? 0 : 1);
		$this->forumsGridSettings['containerType'] = $grid['forums_grid_container_type'];
		
		// Find selected endpoint
		foreach ($endpoints as $val) {
			$endpoint = (array)json_decode($val, true);
			if ($endpoint['selected']) {
				$this->selectedEndpoint = $endpoint;
				break;
			}
		}
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
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getURL() {
		return $this->selectedEndpoint['url'];	
	}
	
	/**
	 * Returns the consumer key.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getConsumerKey() {
		return $this->selectedEndpoint['consumer_key'];
	}
	
	/**
	 * Returns the consumer secret.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getConsumerSecret() {
		return $this->selectedEndpoint['consumer_secret'];
	}
	
	/**
	 * Returns the request token URL.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getRequestTokenURL() {
		return $this->selectedEndpoint['request_token_url'];
	}
	
	/**
	 * Returns the authorization URL.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getAuthorizationURL() {
		return $this->selectedEndpoint['authorization_url'];
	}
	
	/**
	 * Returns the access token URL.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getAccessTokenURL() {
		return $this->selectedEndpoint['access_token_url'];
	}
	
	/**
	 * Returns the authentication method
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getAuthenticationMethod() {
		return $this->selectedEndpoint['authentication_method'];
	}
	
	/**
	 * Returns the URL that points to where the SDK is deployed.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getSDKDeployURL() {
		return $this->sdkSettings['sdk_deploy_url'];
	}
	
	/**
	 * Returns the endpoint name.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getName() {
		return  $this->selectedEndpoint['name'];
	}
	
	/**
	 * Returns the grid type to display.
	 * @return string community|bookmarks
	 * @author Benjamin Jakobus
	 */
	public function getGridGroup() {
		return $this->pluginSettings['grid_group'];
	}
	
	/**
	 * Returns the username used for basic authentication.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthUsername() {
		return $this->selectedEndpoint['basic_auth_username'];
	}
	
	/**
	 * Returns the password used for basic authentication.
	 *
	 * @return
	 * @author Benjamin Jakobus
	 */
	public function getBasicAuthPassword() {
		return $this->selectedEndpoint['basic_auth_password'];
	}
}