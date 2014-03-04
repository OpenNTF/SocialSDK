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
 * Tests data generated / supplied by the plugin.
 * 
 * @author Benjamin Jakobus
 */
class Test_PluginData extends WP_UnitTestCase {
	function setUp() {
		parent::setUp();
	}
	
	/**
	 * Tests that the plugin meta data (i.e. plugin "about" info) is correct and correctly
	 * displayed.
	 */
	function test_plugin_info() {
		$path = plugin_dir_path( __FILE__ );
		$path = str_replace('tests', '', $path);
		$data = get_plugin_data($path . 'ibm-sbt.php');
	
		$default_headers = array(
				'Name' => 'Social Business Toolkit integration plugin',
				'PluginURI' => 'https://github.com/OpenNTF/SocialSDK/',
				'Description' => 'This plugin provides access to the IBM Social Business Toolkit. <cite>By <a href="https://github.com/OpenNTF/SocialSDK/" title="Visit author homepage">Benjamin Jakobus</a>.</cite>',
				'Author' => '<a href="https://github.com/OpenNTF/SocialSDK/" title="Visit author homepage">Benjamin Jakobus</a>',
				'AuthorURI' => 'https://github.com/OpenNTF/SocialSDK/',
				'Version' => '1.0',
		);

		$this->assertTrue( is_array($data) );
	
		foreach ($default_headers as $name => $value) {
			$this->assertTrue(isset($data[$name]));
			$this->assertEquals($value, $data[$name]);
		}
	}
	

	function tearDown() {
		parent::tearDown();
	}
}
