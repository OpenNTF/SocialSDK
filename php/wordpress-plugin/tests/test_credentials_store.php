<?php ob_start();
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
 * Tests that plugin activation and deactivation works as intended.
 * 
 * @author Benjamin Jakobus
 */

class Test_Credentials_Store extends WP_UnitTestCase {
	function setUp() {
		parent::setUp();
		ibm_sbtk_activate_plugin();
	}
	
	/**
	 * Tests the storage of the user credentials for basic authentication.
	 */
	function test_basic_auth_credential_storage() {
		if (!class_exists('SBTMemoryCookieAdapter')) {
			require BASE_PATH . '/core/models/SBTMemoryCookieAdapter.php';
		}
		
		$username = "";
		$password = "";
		
		ibm_sbtk_activate_plugin();
		
		$mockAdapter = new SBTMemoryCookieAdapter();
		$store = SBTCredentialStore::getInstance($mockAdapter);
		
		// Store user credentials
		$store->storeBasicAuthUsername($username);
		$store->storeBasicAuthPassword($password);
		
		// Retrieve stored user credentials
		$retUsername = $store->getBasicAuthUsername();
		$retPassword = $store->getBasicAuthPassword();
		
		// Check that stored credentials are the same than the original
		// credentials
		$this->assertEquals($retUsername, $username);
		$this->assertEquals($retPassword, $password);
	}
	
	/**
	 * Tests the storage of the OAuth tokens.
	 */
	function test_oauth_token_storage() {
		$token = "";
		ibm_sbtk_activate_plugin();
		if (!class_exists('SBTMemoryCookieAdapter')) {
			require BASE_PATH . '/core/models/SBTMemoryCookieAdapter.php';
		}
	
		$mockAdapter = new SBTMemoryCookieAdapter();
		$store = SBTCredentialStore::getInstance($mockAdapter);
	
		// Store tokens
		$store->storeOAuthAccessToken($token);
		$store->storeRequestToken($token);
		$store->storeToken($token);
	
		// Retrieve stored user credentials
		$retToken1 = $store->getOAuthAccessToken();
		$retToken2 = $store->getRequestToken();
		$retToken3 = $store->getToken();
	
		// Check that stored tokens are the same than the original
		// token
		$this->assertEquals($token, $retToken1);
		$this->assertEquals($token, $retToken2);
		$this->assertEquals($token, $retToken3);
	}
	
	function tearDown() {
		parent::tearDown();
	}
}
