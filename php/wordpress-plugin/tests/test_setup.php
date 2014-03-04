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
class Test_Setup extends WP_UnitTestCase {
	function setUp() {
		parent::setUp();
	}
	
	/**
	 * Ensure that the wp_head hook for generating the sbt header config was added.
	 */
	function test_header_creation() {
		$this->assertGreaterThan(0, has_action('wp_head', 'ibm_sbtk_header'));
	}
	
	/**
	 * Test that plugin activation function exists.
	 */
	function test_options_setup() {
		// Check activation
		$this->assertTrue(function_exists('ibm_sbtk_activate_plugin'));
		$this->assertTrue(ibm_sbtk_activate_plugin());
		
		// Check deactivation
		$this->assertTrue(function_exists('ibm_sbtk_deactivate_plugin'));
		$this->assertTrue(ibm_sbtk_deactivate_plugin());
		$this->assertTrue(ibm_sbtk_activate_plugin());
	}
	
	/**
	 * Tests that dependencies are loaded correctly.
	 */
	function test_autoloader() {	
		$sbtk = new SBTPlugin();
		$model = new SBTSettings();
	}
	
	function tearDown() {
		parent::tearDown();
	}
}
