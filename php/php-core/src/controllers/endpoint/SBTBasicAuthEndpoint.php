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

use Guzzle\Http\Client;

if (file_exists(BASE_PATH . "/core/system/libs/vendor/autoload.php")) {
	require_once BASE_PATH . "/core/system/libs/vendor/autoload.php";
} else {
	require_once BASE_PATH . "/system/libs/vendor/autoload.php";
}

/**
 * OAuth 1.0 Endpoint
 * 
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTBasicAuthEndpoint extends BaseController implements SBTEndpoint
{
	/**
	 * Makes the request to the server.
	 * 
	 * @param string $server	
	 * @param string $service		The rest service to access e.g. /connections/communities/all
	 * @param string $method		GET, POST or PUT
	 * @param string $body
	 * @param string $headers
	 */
	public function makeRequest($server, $service, $method, $options, $body = null, $headers = null, $endpointName = "connections") {
		$store = SBTCredentialStore::getInstance();
		$settings = new SBTSettings();
		
		$token = $store->getToken();
		$response = null;
		
		$client = new Client($server);
		$client->setDefaultOption('verify', false);
		
		// If global username and password is set, then use it; otherwise use user-specific credentials
		if ($settings->getBasicAuthMethod() == 'global' || $settings->getBasicAuthMethod() == 'profile') {
			$user = $settings->getBasicAuthUsername($endpointName);
			$password = $settings->getBasicAuthPassword($endpointName);
		} else {
			$user = $store->getBasicAuthUsername($endpointName) ;
			$password = $store->getBasicAuthPassword($endpointName);
		}
		try {				
			$request = $client->createRequest($method, $service , $headers, $body,  $options);
			if ($settings->forceSSLTrust($endpointName)) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$request->setAuth($user, $password);		
			$response = $request->send();
		} catch (Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
		}

		return $response;
	}
}