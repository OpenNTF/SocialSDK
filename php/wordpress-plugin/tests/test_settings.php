***REMOVED***
/*
 * © Copyright IBM Corp. 2014
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
 * Tests the plugin's setting management.
 *  
 * @author Benjamin Jakobus
 */
class Test_Settings extends WP_UnitTestCase {
	
	/**
	 * Tests whether settings are saved correctly by simulating post
	 * requests using mock data.
	 */
	function test_endpoint_save_settings() {
		// Load mock data
		require 'mock_data.php';
		
		if (!class_exists('SBTEndpointUpdate')) {
			require BASE_PATH . '/controllers/SBTEndpointUpdate.php';
		}
		
		// Fake post request - populate it with mock data
		$_POST['endpoint_name'] = $config['wp_endpoint_2_name'];
		$_POST['endpoint_url'] = $config['wp_endpoint_2_url'];
		$_POST['consumer_key'] = $config['wp_endpoint_2_consumer_key'];
		$_POST['consumer_secret'] = $config['wp_endpoint_2_consumer_secret'];
		$_POST['authorization_url'] = $config['wp_endpoint_2_authorization_url'];
		$_POST['access_token_url'] = $config['wp_endpoint_2_access_token_url'];
		$_POST['request_token_url'] = $config['wp_endpoint_2_request_token_url'];
		$_POST['authentication_method'] = $config['wp_endpoint_2_authentication_method'];
		$_POST['basic_auth_username'] = $config['wp_endpoint_2_basic_auth_username'];
		$_POST['basic_auth_password'] = $config['wp_endpoint_2_basic_auth_password'];
		$_POST['basic_auth_method'] = $config['wp_endpoint_2_basic_auth_method'];
		$_POST['sdk_deploy_url'] = $config['sdk_deploy_url'];
		$_POST['delete_endpoint'] = 'no';
		$_POST['libraries_list'] = $config['js_library'];
		
		// Update the endpoint
		$update = new SBTEndpointUpdate();
		
		// Load settings
		if (!class_exists('SBTSettings')) {
			require BASE_PATH . '/core/models/SBTSettings.php';
		}
		$settings = new SBTSettings();
		
		// Check that settings have been saved
		$this->assertEquals($config['wp_endpoint_2_name'], $settings->getName());
		$this->assertEquals($config['wp_endpoint_2_url'], $settings->getURL());
		$this->assertEquals($config['wp_endpoint_2_consumer_key'], $settings->getConsumerKey());
		$this->assertEquals($config['wp_endpoint_2_consumer_secret'], $settings->getConsumerSecret());
		$this->assertEquals($config['wp_endpoint_2_authorization_url'], $settings->getAuthorizationURL());
		$this->assertEquals($config['wp_endpoint_2_access_token_url'], $settings->getAccessTokenURL());
		$this->assertEquals($config['wp_endpoint_2_request_token_url'], $settings->getRequestTokenURL());
		$this->assertEquals($config['wp_endpoint_2_authentication_method'], $settings->getAuthenticationMethod());
		$this->assertEquals($config['wp_endpoint_2_basic_auth_username'], $settings->getBasicAuthUsername());
		$this->assertEquals($config['wp_endpoint_2_basic_auth_password'], $settings->getBasicAuthPassword());
		$this->assertEquals($config['wp_endpoint_2_basic_auth_method'], $settings->getBasicAuthMethod());
		$this->assertEquals($config['sdk_deploy_url'], $settings->getSDKDeployURL());
		$this->assertEquals($config['js_library'], $settings->getJSLibrary());
		
		// Now delete the endpoint
		$_POST['delete_endpoint'] = 'yes';
		
		// Perform update
		$update = new SBTEndpointUpdate();
		
		// Make sure that the endpoint has been deleted
		$settings = new SBTSettings();
		$this->assertNotEquals($config['wp_endpoint_2_name'], $settings->getName());
	}
	
	/**
	 * Tests SBTPluginSettings, the class responsible for generating and populating
	 * the plugin settings interface.
	 */
	function test_plugin_settings_ui() {
		ibm_sbtk_load_language_files();
		
		$settings = new SBTPluginSettings();

		$settings->addPluginPage();
		$settings->createSettingsPage();
		$settings->endpointListCallback();
		$settings->hiddenEndpointFieldsCallback();
		$settings->jsLibraryCallback();
		$settings->pageInit();
		$settings->pluginOptionsTabs();
		$settings->printHiddenSettings();
		$settings->printSdkSectionInfo();
		$settings->printSectionInfo();
		$settings->sdkDeployCallback();
		
		// With update message
		$settings = new SBTPluginSettings(true);
	}
	
	/**
	 * Tests that the plugin settings page (and accompanying tabs) is created / exists.
	 */
	function test_settings_page() {
		// Create plugin settings page
		if (!class_exists('SBTPluginSettings')) {
			require BASE_PATH . '/controllers/SBTPluginSettings.php';
		}
		$settings = new SBTPluginSettings();
	
		$current_user = get_current_user_id();
		wp_set_current_user($this->factory->user->create(array('role' => 'administrator')));
	
		$expected['settings'] = get_site_url() . '/wp-admin/options-general.php?page=sbtk-sdk-settings-page';
		$expected['widget_tab'] = get_site_url() . '/wp-admin/options-general.php?page=sbtk-sdk-settings-page&tab=sbtk-plugin-settings';
		$expected['endpoint_tab'] = get_site_url() . '/wp-admin/options-general.php?page=sbtk-sdk-settings-page&tab=sbtk-sdk-settings-page';
	
		// Test that pages exist
		foreach ($expected as $key => $url) {
			$response = wp_remote_get($url);
			$this->assertEquals(200, $response['response']['code']);
		}
	
		wp_set_current_user( $current_user );
	}
	
}