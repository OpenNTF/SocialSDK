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
 * Tests the views.
 * 
 * @author Benjamin Jakobus
 */
class Test_Views extends WP_UnitTestCase {
	
	function setUp() {
		parent::setUp();
	}
	
	function test_js_libraries() {
		// Load mock data
		require 'mock_data.php';
		
		// Load settings
		if (!class_exists('SBTSettings')) {
			require BASE_PATH . '/core/models/SBTSettings.php';
		}
		$settings = new SBTSettings();
		
		$libs = $config['js_libraries'];
		
		foreach($libs as $lib) {
			$viewData['deploy_url'] = $settings->getSDKDeployURL();
			$viewData['authentication_method'] = $settings->getAuthenticationMethod();
			$viewData['js_library'] = $lib;
			$viewData['url'] = $settings->getURL();
			$viewData['name'] = $settings->getName();
			
			$file = '../views/includes/header.php';
			@include $file;
		}
	}
	
	function tearDown() {
		parent::tearDown();
	}
}