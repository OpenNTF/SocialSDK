***REMOVED***
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
class SBTOAuth2Endpoint extends BaseController implements SBTEndpoint
{
	
	/**
	 * The callback function for authenticating the user and then storing the token in the CredentialStore (no content
	 * is being requested).
	 */
	public function authenticationCallback() {
	
		if (!isset($_GET['code'])) {
			return;
		}

		$store = SBTCredentialStore::getInstance();
		$settings = new SBTSettings();
		
		$endpointName = "connections";
		if (isset($_GET['endpointName'])) {
			$endpointName = $_GET['endpointName'];
		}

		$parameters = array(
				'callback_uri'  => $settings->getOAuth2CallbackURL($endpointName),
				'code' => $_GET['code'],
				'grant_type' => 'authorization_code',
				'client_id' => $settings->getClientId($endpointName),
				'client_secret' => $settings->getClientSecret($endpointName)
		);
		
		$tokenURL = $settings->getAccessTokenURL($endpointName) . '?' . http_build_query($parameters, null, '&');
		$client = new Client($tokenURL);
		$client->setDefaultOption('verify', false);
		
		$headers = null;
		$body = null;
		$options = array();
		$response = null;
		
		try {
			$request = $client->createRequest('GET', $tokenURL , $headers, $body,  $options);
			if ($settings->forceSSLTrust($endpointName)) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$response = $request->send();
		
			foreach ($response->getHeaderLines() as $h) {
				if (strpos($h, "Content-Type") === 0) header($h, TRUE);
			}
		
			header(':', true, $response->getStatusCode());
			header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
		
			parse_str($response->getBody(TRUE), $info);
		
			if (!isset($info['access_token'])) {
				die('Missing access token. Something went wrong - make sure that your client ID and client secret are correct and try again.');
			}
			
			$store->storeOAuthAccessToken($info['access_token'], $endpointName);
			
			$requestURL = $_COOKIE['IBMSBTKOAuthOrigin'];
			header("Location: " . $requestURL);
			
		} catch(Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
			print_r($response->getBody(TRUE));
		}
	}
	
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
		$token = $store->getOAuthAccessToken($endpointName);

		$store = SBTCredentialStore::getInstance();
		$settings = new SBTSettings();
		$response = null;
		if ($options == null) {
			$options = array();
		}
		try {
			$client = new Client($server);
			$request = $client->createRequest($method, $service , $headers, $body,  $options);
			$request->addHeader('authorization', 'Bearer ' . $token);
			if ($settings->forceSSLTrust($endpointName)) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$response = $request->send();
		} catch (Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
		}
	
		return $response;
	}
}