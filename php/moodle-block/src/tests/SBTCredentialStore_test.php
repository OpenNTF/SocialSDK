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
require 'SBTBaseTestCase.php';
class SBTCredentialStore_test extends SBTBaseTestCase 
{
	
	public function testInsert() 
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
		
		$store->storeRequestToken($random, $this->testEndpoint);
		$ret = $store->getRequestToken($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeTokenSecret($random, $this->testEndpoint);
		$ret = $store->getTokenSecret($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeVerifierToken($random, $this->testEndpoint);
		$ret = $store->getVerifierToken($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$store->storeTokenType($random, $this->testEndpoint);
		$ret = $store->getTokenType($this->testEndpoint);
		$this->assertEquals($ret, $random);
		
		$ret = $store->deleteBasicAuthCredentials($this->testEndpoint);
		$this->assertTrue($ret);
		$ret = $store->getBasicAuthUsername($this->testEndpoint);
		$this->assertEquals($ret, null);
		
		$ret = $store->deleteTokens($this->testEndpoint);
		$this->assertTrue($ret);
		$ret = $store->getToken($this->testEndpoint);
		$this->assertEquals($ret, null);
		
		$ret = $store->deleteOAuthCredentials($this->testEndpoint);
		$this->assertTrue($ret);
		$ret = $store->getOAuthAccessToken($this->testEndpoint);
		$this->assertEquals($ret, null);
	}
}
