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
 * Tests the SBTCredentialStore
 * 
 * @author Benjamin Jakobus
 */
require 'bootstrap.php';
class CredentialStore_test extends PHPUnit_Framework_TestCase 
{
	
	private $uid;
	private $testEndpoint;
	
	public function test_insert() 
	{	
		$store = SBTCredentialStore::getInstance($this->uid);
		$random = rand();
		$random = $random . "str";
		
		$store->storeBasicAuthUsername($random, $this->testEndpoint);
		$ret = $store->getBasicAuthUsername($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeBasicAuthPassword($random, $this->testEndpoint);
		$ret = $store->getBasicAuthPassword($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeOAuthAccessToken($random, $this->testEndpoint);
		$ret = $store->getOAuthAccessToken($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeRequestToken($random, $this->testEndpoint);
		$ret = $store->getRequestToken($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeToken($random, $this->testEndpoint);
		$ret = $store->getToken($this->testEndpoint);
		$this->assertEquals($ret, $random);
	}
	
	function tearDown() 
	{
		parent::tearDown();
		dropTables();
	}
	
	function setUp() 
	{
		parent::setUp();
		$this->uid = 1;
		$this->testEndpoint = "sampleEndpoint" . time();
	}
}
