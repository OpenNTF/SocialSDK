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
 * Tests that plugin activation and deactivation works as intended.
 * 
 * @author Benjamin Jakobus
 */
class Test_Core_Widgets extends WP_UnitTestCase {
	function setUp() {
		parent::setUp();
	}
	
	/**
	 * Ensures that the widget header is correct.
	 */
	function test_header() {
		// Load mock data
		require 'mock_data.php';
		ibm_sbtk_activate_plugin();
		if (!class_exists('SBTEndpointUpdate')) {
			require BASE_PATH . '/controllers/SBTEndpointUpdate.php';
		}
		
		// Fake post request - populate it with mock data
		$_POST['endpoint_name'] = $config['wp_endpoint_1_name'];
		$_POST['endpoint_url'] = $config['wp_endpoint_1_url'];
		$_POST['consumer_key'] = $config['wp_endpoint_1_consumer_key'];
		$_POST['consumer_secret'] = $config['wp_endpoint_1_consumer_secret'];
		$_POST['authorization_url'] = $config['wp_endpoint_1_authorization_url'];
		$_POST['access_token_url'] = $config['wp_endpoint_1_access_token_url'];
		$_POST['request_token_url'] = $config['wp_endpoint_1_request_token_url'];
		$_POST['authentication_method'] = $config['wp_endpoint_1_authentication_method'];
		$_POST['basic_auth_username'] = $config['wp_endpoint_1_basic_auth_username'];
		$_POST['basic_auth_password'] = $config['wp_endpoint_1_basic_auth_password'];
		$_POST['basic_auth_method'] = $config['wp_endpoint_1_basic_auth_method'];
		$_POST['sdk_deploy_url'] = $config['sdk_deploy_url'];
		$_POST['delete_endpoint'] = 'no';
		$_POST['libraries_list'] = $config['js_library'];
		
		// Update the endpoint
		$update = new SBTEndpointUpdate();

		$this->assertTrue(function_exists('ibm_sbtk_header'));
		
		ob_start();
		ibm_sbtk_header(array());
		$html = ob_get_clean();

		// Check that JS for the header is correct
		//$this->_validateHeader($html);
		
		//  Iterate through all available src attributes and checks that links are valid.
		$this->_validateLinks($html);
	}
	
	/**
	 * Tests the widget generator method.
	 */
	function test_widget_creation() {
		// Register files widget
		register_widget('SBTFilesWidget');

// 		// Register communities widget
		register_widget('SBTCommunitiesWidget');

		
// 		// Register communities widget
		register_widget('SBTFilesViewWidget');
		
		// Load mock data
		require 'mock_data.php';
		
		if (!class_exists('SBTEndpointUpdate')) {
			require BASE_PATH . '/controllers/SBTEndpointUpdate.php';
		}
		
		// Check OAuth re-direct code 
		// Fake post request - populate it with mock data
		$_POST['endpoint_name'] = $config['wp_endpoint_1_name'];
		$_POST['endpoint_url'] = $config['wp_endpoint_1_url'];
		$_POST['consumer_key'] = $config['wp_endpoint_1_consumer_key'];
		$_POST['consumer_secret'] = $config['wp_endpoint_1_consumer_secret'];
		$_POST['authorization_url'] = $config['wp_endpoint_1_authorization_url'];
		$_POST['access_token_url'] = $config['wp_endpoint_1_access_token_url'];
		$_POST['request_token_url'] = $config['wp_endpoint_1_request_token_url'];
		$_POST['authentication_method'] = 'oauth1';
		$_POST['basic_auth_username'] = $config['wp_endpoint_1_basic_auth_username'];
		$_POST['basic_auth_password'] = $config['wp_endpoint_1_basic_auth_password'];
		$_POST['basic_auth_method'] = $config['wp_endpoint_1_basic_auth_method'];
		$_POST['sdk_deploy_url'] = $config['sdk_deploy_url'];
		$_POST['delete_endpoint'] = 'no';
		$_POST['libraries_list'] = $config['js_library'];
		
		// Update the endpoint
		$update = new SBTEndpointUpdate();

 		// Check basic login code
		$_POST['authentication_method'] = 'basic';
		$_POST['basic_auth_method'] = 'prompt';
		
		// Update the endpoint
		$update = new SBTEndpointUpdate();
	}
	
	/**
	 * Checks that the header is formed correctly (i.e. correct endpoint definition, etc).
	 * 
	 * @param string $html		
	 */
	private function _validateHeader($html) {
		if (!class_exists('SBTEndpointUpdate')) {
			require BASE_PATH . '/core/models/SBTSettings.php';
		}
		$settings = new SBTSettings();
		$pattern = '/.*"sbt"\s*:\s*".*"/';
	
		preg_match($pattern, $html, $matches);
		$this->assertFalse(empty($matches));
		
		$pattern = '/.*"sbt\/widget"\s*:\s*".*"/';
		preg_match($pattern, $html, $matches);
		$this->assertFalse(empty($matches));
		
		$pattern = '/sbt.Properties\s*=\s*\{\s*"libraryUrl"\s*:\s*".*",\s*"serviceUrl"\s*:\s*".*",\s*"sbtUrl"\s*:\s*".*"\s*\}\s*;/x';
		preg_match($pattern, $html, $matches);
		$this->assertFalse(empty($matches));
		
		$pattern = '/sbt.Endpoints\s*=\s*\{\s*".*"\s*:\s*new\s*Endpoint\s*\(\s*\{\s*
				"authType"\s*:\s*".*",\s*
				"platform"\s*:\s*".*",\s*
				\s*.*\s*
				\}\s*;/x';
		preg_match($pattern, $html, $matches);
		$this->assertFalse(empty($matches));
		
// 		if ($result === true)
// 			OutputLintHTML($engine);
// 		else
// 			echo '<b>' . htmlentities($result) . '</b>';
		
		// Validate HTML
// 		$dom = new DOMDocument;
// 		$dom->load($html);
// 		$this->assertTrue($dom->validate());
	}
	
	/**
	 * Iterates through all available src attributes and checks that links are valid.
	 */
	private function _validateLinks($html) {
		$dom = new DomDocument;

		$dom->loadHTML($html);
			
		$elems = $dom->getElementsByTagName('*');
			
		foreach ($elems as $elm) {
			if ($elm->hasAttribute('src')) {
				$srcs[] = $elm->getAttribute('src');
				foreach ($srcs as $src) {
					$src = str_replace("//", "http://", $src);
					$ret = wp_remote_get($src);
					
					$this->assertEquals(200, $ret['response']['code']);
				}
			}
		}
	}
	
	/**
	 * Validates the JavaScript that composes the core widgets.
	 */
	function test_core_widget_javascript() {
		// Test JavaScript
		if (file_exists(WP_CONTENT_DIR . '/plugins/ibm-sbtk/tests/lib/jsl/_jsl_online.php')) {
			require('lib/jsl/_jsl_online.php');
			$engine = new JSLEngine('.priv/jsl', '.priv/jsl.server.conf');	
			if ($handle = opendir(WP_CONTENT_DIR . '/plugins/ibm-sbtk/views/widgets/')) {
				while (false !== ($widget = readdir($handle)))
				{
					if ($widget != "." && $widget != ".." && strtolower(substr($widget, strrpos($widget, '.') + 1)) == 'php')
					{
						$js = file_get_contents(WP_CONTENT_DIR . '/plugins/ibm-sbtk/views/widgets/' . $widget);
						$result = $engine->Lint($js);
						$this->assertTrue($result);
					}
				}
				closedir($handle);
			}
		}
	}
	
	function tearDown() {
		parent::tearDown();
	}
}
