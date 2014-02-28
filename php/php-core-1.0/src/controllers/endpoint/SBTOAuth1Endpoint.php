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
class SBTOAuth1Endpoint extends BaseController implements SBTEndpoint
{
	
	/**
	 * Performs a request to the given request URL.
	 * 
	 * @param string $requestURL		Resource that is to be requested (e.g. https://apps.na.collabserv.com/communities/service/html/mycommunities)
	 * @param string $callbackURL		The callback URL (e.g. http://127.0.0.1:8443/demo/application/OAuthSample.php)
	 * @param string $method			GET, PUT or POST. POST by default
	 */
	public function request($requestURL, $callbackURL, $method = 'POST'){
		$callbackURL = $callbackURL . "&requestMethod=" . $method . "&requestURL=" . urlencode($requestURL);
		$store = SBTCredentialStore::getInstance();
		try
		{	
			//  STEP 1:  If we do not have an OAuth token yet, go get one
			if (empty($_GET["oauth_token"]))
			{							
				$store = SBTCredentialStore::getInstance();
				$settings = new SBTSettings();
		
				$random = mt_rand(0, 999999);
				$nonce = sha1($random);

				$parameters = array(
						'oauth_version' => '1.0',
						'oauth_callback' => $callbackURL,
						'oauth_timestamp'  => time(),
						'oauth_signature' => $settings->getConsumerSecret() . '&' . $settings->getConsumerKey(),
						'oauth_signature_method' => 'PLAINTEXT',
						'oauth_nonce' => $nonce,
						'oauth_consumer_key' => $settings->getConsumerKey()
				);
				
			
				$tokenURL = $settings->getRequestTokenURL() . '?' . http_build_query($parameters, null, '&');
				$client = new Client($tokenURL);
				$client->setDefaultOption('verify', false);
				
				$headers = null;
				$body = null;
				$options = array();
				$response = null;
				
				try {
					$request = $client->createRequest($method, $tokenURL , $headers, $body,  $options);
					if ($settings->forceSSLTrust()) {
						$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
						$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
					}
					$response = $request->send();
				} catch(Guzzle\Http\Exception\BadResponseException $e) {
					$response = $e->getResponse();
					print_r($response->getBody(TRUE));
				}
			
				foreach ($response->getHeaderLines() as $h) {
					if (strpos($h, "Content-Type") === 0) header($h, TRUE);
				}
				
				header(':', true, $response->getStatusCode());
				header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
				
				parse_str($response->getBody(TRUE), $info);
	
				if (!isset($info['oauth_token'])) {
					die('Missing oauth token. Something went wrong - make sure that your client ID and client secret are correct and try again.');
				}
				
				$store->storeRequestToken($info['oauth_token']);
				$store->storeRequestTokenSecret($info['oauth_token_secret']);
					
				if (!headers_sent()) {
					header("Location: " . $settings->getAuthorizationURL() . "?oauth_token=" . $info['oauth_token']);
				} else {
					echo '<script type="text/javascript" language="javascript">window.location = "' . $settings->getAuthorizationURL() . "?oauth_token=" . $info['oauth_token'] . '";</script>';
				}
				
			}
			
		}
		catch(OAuth1Exception2 $e) {
			echo "OAuth1Exception2:  " . $e->getMessage();
		}
	}
	
	/**
	 * The callback function for authenticating the user and then storing the token in the CredentialStore (no content
	 * is being requested).
	 *
	 * @author Benjamin Jakobus
	 */
	public function authenticationCallback() {
	
		if (empty($_GET['oauth_token'])) {
			return;
		}
		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();
		$store->storeRequestToken($_GET['oauth_token']);
		$store->storeVerifierToken($_GET['oauth_verifier']);
		$requestURL = urldecode($_GET['requestURL']);
		
		$this->_getAccessToken();

		header("Location: " . $requestURL);
	}
	
	/**
	 * Gets the access token.
	 */
	private function _getAccessToken() {
		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();
		
		$random = mt_rand(0, 999999);
		$nonce = sha1($random);
		
		$parameters = array(
			'oauth_nonce' => $nonce,
			'oauth_version' => '1.0',
			'oauth_token' => $store->getRequestToken(),
			'oauth_callback' => $callbackURL,
			'oauth_timestamp'  => time(),
			'oauth_signature' => $settings->getConsumerSecret() . '&' . $store->getRequestTokenSecret(),
			'oauth_signature_method' => 'PLAINTEXT',
			'oauth_verifier' => $store->getVerifierToken(),
			'oauth_consumer_key' => $settings->getConsumerKey()
		);
		
		$tokenURL = $settings->getAccessTokenURL() . '?' . http_build_query($parameters, null, '&');
		
		$client = new Client($tokenURL);
		
		$client->setDefaultOption('verify', false);
		
		$headers = null;
		$body = null;
		$options = array();
		$response = null;
		
		try {
			$request = $client->createRequest('GET', $tokenURL , $headers, $body,  $options);
			if ($settings->forceSSLTrust()) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$response = $request->send();
		} catch(Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
			print_r($response->getBody(TRUE));
		}
		
		parse_str($response->getBody(TRUE), $info);
		
		if (!isset($info['oauth_token'])) {
			die('Missing access token. Something went wrong - make sure that your client ID and client secret are correct and try again.');
		}
		
		$store->storeTokenSecret($info["oauth_token_secret"]);
		$store->storeOAuthAccessToken($info['oauth_token']);
		
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
	public function makeRequest($server, $service, $method, $options, $body = null, $headers = null) {
		$store = SBTCredentialStore::getInstance();
		$settings = new SBTSettings();
		
		$random = mt_rand(0, 999999);
		$nonce = sha1($random);
		
		if ($store->getOAuthAccessToken() == null) {
			$this->_getAccessToken();
		}

		$parameters = array(
			'oauth_nonce' => $nonce,
			'oauth_token' => $store->getOAuthAccessToken(),
			'oauth_version' => '1.0',
			'oauth_timestamp'  => time(),
			'oauth_signature' => $settings->getConsumerSecret() . '&' .$store->getRequestTokenSecret(),
			'oauth_signature_method' => 'PLAINTEXT',
			'oauth_consumer_key' => $settings->getConsumerKey()
		);

		$url = $server . '/' . $service;

		$client = new Client($url);
		$client->setDefaultOption('verify', false);

		$options = array();
		$response = null;
		try {			
			$request = $client->createRequest($method, $url , $headers, $body,  $options);
			$request->addHeader('Authorization', 'OAuth oauth_nonce="' . $nonce . '",oauth_version="1.0", oauth_timestamp="' . time() . '",oauth_signature="' . $settings->getConsumerSecret() . '&' .$store->getTokenSecret() 
					. '",oauth_signature_method="PLAINTEXT",oauth_consumer_key="'.$settings->getConsumerKey().'",oauth_token="' . $store->getOAuthAccessToken() . '"');
			if ($settings->forceSSLTrust()) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$response = $request->send();
		} catch(Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
		}
		
		return $response;
	}
}