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
defined('SBT_SDK') OR exit('Access denied.');

use Guzzle\Http\Client;
use Guzzle\Http\Message\Response;
use Guzzle\Service\Exception\DescriptionBuilderException;

/**
 * The proxy handles authentication with IBM services.
 * 
 * @author Benjamin Jakobus
 */
// Load settings object and default configuration
require_once BASE_PATH . '/config.php';
include_once BASE_PATH . '/autoload.php';

class SBTJsonService
{
	protected $endpointName;
	protected $settings;
	
	function __construct($endpointName) {
		$this->endpointName = $endpointName;
	}
	
	public function makeRequest($method, $service, $header = array(), $body = null, $options = array()) {
		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();

		$server = $settings->getURL($this->endpointName);

		if ($settings->getAuthenticationMethod($this->endpointName) == "basic") {
			$endpoint = new SBTBasicAuthEndpoint();
		} else if ($settings->getAuthenticationMethod($this->endpointName) == "oauth2") {
			$endpoint = new SBTOAuth2Endpoint();
		} else if ($settings->getAuthenticationMethod($this->endpointName) == "oauth1") {
			$endpoint = new SBTOAuth1Endpoint();
		}		
		// Make request
		$response = $endpoint->makeRequest($settings->getURL($this->endpointName), $service, $method, $options, $body, $header, $this->endpointName);
		
		print_r($response->getBody(TRUE));
	}	
}
?>