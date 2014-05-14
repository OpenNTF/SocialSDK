<?php
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
 * Tests the SBTSettings
 * 
 * @author Benjamin Jakobus
 */
require_once 'SBTBaseTestCase.php';
class SBTSettings_test extends SBTBaseTestCase 
{
	private $config = array();
	
	public function setUp() 
	{
		parent::setUp();
		$this->config['type'] = 'create';
		$this->config['allow_client_access'] = true;
		$this->config['server_type'] = "";
		$this->config['api_version'] = "2.0";
		$this->config['force_ssl_trust'] = true;
		$this->config['form_auth_page'] = '';
		$this->config['form_auth_login_page'] = '';
		$this->config['form_auth_cookie_cache'] = '';
		$this->config['basic_auth_method'] = 'global';
		$this->config['basic_auth_password'] = 'passw0rd';
		$this->config['basic_auth_username'] = 'fadams';
		$this->config['auth_type'] = 'oauth1';
		$this->config['authorization_url'] = 'https://apps.na.collabserv.com/manage/oauth/authorizeToken';
		$this->config['oauth2_callback_url'] = '';
		$this->config['request_token_url'] = 'https://apps.na.collabserv.com/manage/oauth/getRequestToken';
		$this->config['client_secret'] = time();
		$this->config['client_id'] = time();
		$this->config['consumer_secret'] = time();
		$this->config['consumer_key'] = time();
		$this->config['access_token_url'] = 'https://apps.na.collabserv.com/manage/oauth/getAccessToken';
		$this->config['server_url'] = 'https://apps.na.collabserv.com';
		$this->config['name'] = 'SampleEndpoint';
	}
	
	/**
	 * Tests create, update and delete operations.
	 */
	public function testCRUD() 
	{
		$this->tearDown();
		$this->setUp();
		$settings = new SBTSettings();
		global $USER;
		global $CFG;
		$USER->id = $this->uid;

		// Create
		$_POST = $this->_prepareCreatePOST();
		require $CFG->dirroot . '/blocks/ibmsbt/endpoint_settings.php';
		
		// Check that endpoint really has been created
		$settings = new SBTSettings();
		
		// Update
		$newValue = $this->_prepareUpdatePOST();
		require $CFG->dirroot . '/blocks/ibmsbt/endpoint_settings.php';
		$settings = new SBTSettings();
		$this->assertEquals($settings->getAPIVersion($this->config['name']), $newValue);
		
 		// Get
		$this->_prepareGet();
		require $CFG->dirroot . '/blocks/ibmsbt/endpoint_settings.php';
		
 		// Delete
		$this->_prepareDeletePOST();
		require $CFG->dirroot . '/blocks/ibmsbt/endpoint_settings.php';
		$settings = new SBTSettings();
		$this->assertEquals($settings->getAPIVersion($this->config['name']), null);
	}
	
	/**
	 * Populates $_POST with necessary values for performing an update operation.
	 */
	private function _prepareDeletePOST()
	{
		$_POST['id'] = 1;
		$_POST['type'] = 'delete';
	}
	
	/**
	 * Populates $_POST with necessary values for performing an update operation.
	 */
	private function _prepareUpdatePOST()
	{
		$this->_prepareCreatePOST();
		$_POST['id'] = 1;
		$_POST['type'] = 'update';
		$newValue = time();
		$_POST['api_version'] = $newValue;
		return $newValue;
	}
	
	/**
	 * Populates $_POST with necessary values for performing a get operation.
	 */
	private function _prepareGet()
	{
		$_POST['type'] = 'get';
		$_POST['id'] = 1;
	}
	
	/**
	 * Populates $_POST with necessary values for performing a create operation.
	 */
	private function _prepareCreatePOST()
	{
		$_POST['type'] = $this->config['type'];
		$_POST['allow_client_access'] = $this->config['allow_client_access'];
		$_POST['server_type'] = $this->config['server_type'];
		$_POST['api_version'] = $this->config['api_version'];
		$_POST['force_ssl_trust'] = $this->config['force_ssl_trust'];
		$_POST['form_auth_page'] = $this->config['form_auth_page'];
		$_POST['form_auth_login_page'] = $this->config['form_auth_login_page'];
		$_POST['form_auth_cookie_cache'] = $this->config['form_auth_cookie_cache'];
		$_POST['basic_auth_method'] = $this->config['basic_auth_method'];
		$_POST['basic_auth_password'] = $this->config['basic_auth_password'];
		$_POST['basic_auth_username'] = $this->config['basic_auth_username'];
		$_POST['auth_type'] = $this->config['auth_type'];
		$_POST['authorization_url'] = $this->config['authorization_url'];
		$_POST['oauth2_callback_url'] = $this->config['oauth2_callback_url'];
		$_POST['request_token_url'] = $this->config['request_token_url'];
		$_POST['client_secret'] = $this->config['client_secret'];
		$_POST['client_id'] = $this->config['client_id'];
		$_POST['consumer_secret'] = $this->config['consumer_secret'];
		$_POST['consumer_key'] = $this->config['consumer_key'];
		$_POST['access_token_url'] = $this->config['access_token_url'];
		$_POST['server_url'] = $this->config['server_url'];
		$_POST['name'] = $this->config['name'];
		return $_POST;
	}
	
	
	
}
