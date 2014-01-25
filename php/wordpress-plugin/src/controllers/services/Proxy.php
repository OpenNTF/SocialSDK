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
defined('SBT_SDK') OR exit('Access denied.');

use Guzzle\Http\Client;
use Guzzle\Http\Message\Response;
use Guzzle\Service\Exception\DescriptionBuilderException;

/**
 * The proxy handles authentication with IBM services.
 * 
 * @author Lorenzo Boccaccia
 * @author Benjamin Jakobus
 *
 */
// Load settings object and default configuration
require_once BASE_PATH . '/config.php';
include_once BASE_PATH . '/autoload.php';
class Proxy extends BaseController {
	
	/**
	 * Routes requests.
	 * 
	 * @param string server			The URL of the server to which to re-direct the request to. Uses SBTKSettings if none given.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function route($server = null) {				
		$this->loadModel('SBTKSettings');
		$this->loadModel('CredentialStore');
		
		$store = new CredentialStore();	
		$settings = new SBTKSettings();

		if (!isset($_REQUEST["_redirectUrl"])) {
			// Request to check if the user is authenticated
			if (isset($_REQUEST["isAuthenticated"])) {
				$_REQUEST["_redirectUrl"] = '/files/basic/api/myuserlibrary/feed'; //used to be /connections/files/basic/api/myuserlibrary/feed
				$_SERVER['REQUEST_METHOD'] = 'GET';
			} else if (isset($_REQUEST["basicAuthLogout"])) {
				// Logout request
				$store->deleteBasicAuthCredentials();
				return;
			} else {
				return;
			}
		}
		
		$url = $_REQUEST["_redirectUrl"];
		
		$url = str_replace("/connections/", "", $url);

		if (isset($_REQUEST['basicAuthRequest']) && $_REQUEST['basicAuthRequest'] == 'true') {
			$store->storeBasicAuthUsername($_POST['username']);
			$store->storeBasicAuthPassword($_POST['password']);
			$result = array('status' => 200, 'result' => true);
			print_r(json_encode($result));
			return;
		}
	
		$method = $_SERVER['REQUEST_METHOD'];

		// Use basic authentication
		if ($settings->getAuthenticationMethod() == "basic") {
			// Load the autoloader for the Guzzle API
			require_once BASE_PATH . '/system/libs/vendor/autoload.php';

			$client = new Client($settings->getURL());
		
			$client->setDefaultOption('verify', false);
				
			$parameters = file_get_contents('php://input');

			$options = $_REQUEST;
			
			// Remove proxy-specific parameters
			// TODO: Refactor -> extract method
			if (isset($options['classpath'])) {
				unset($options['classpath']);
			}
			
			if (isset($options['class'])) {
				unset($options['class']);
			}
			
			if (isset($options['method'])) {
				unset($options['method']);
			}
			
			if (isset($options['_redirectUrl'])) {
				unset($options['_redirectUrl']);
			}
			
			if (isset($options['isAuthenticated'])) {
				unset($options['isAuthenticated']);
			}
			
			if (isset($options['actionType'])) {
				unset($options['actionType']);
			}
			
			$headers = null;
			$body = null;

			$token = $store->getToken();

			// If global username and password is set, then use it; otherwise use user-specific credentials
			if ($settings->getBasicAuthMethod() == 'global' || $settings->getBasicAuthMethod() == 'profile') {
				$user = $settings->getBasicAuthUsername();
				$password = $settings->getBasicAuthPassword();
			} else {
				$user = $store->getBasicAuthUsername() ;
				$password = $store->getBasicAuthPassword();
			}

			$response = null;
			
// 			$headers = apache_request_headers();
			$body = $_POST;

			try {	
				$request = $client->createRequest($method, $url , $headers, $body,  $options);
				$request->setAuth($user, $password);
				
				$response = $request->send();
			} catch (Guzzle\Http\Exception\BadResponseException $e) {
				$response = $e->getResponse();
			}
		
			foreach ($response->getHeaderLines() as $h) {
				if (strpos($h, "Content-Type") === 0) header($h, TRUE);
			}
		
			header(':', true, $response->getStatusCode());
			header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
			
			if (isset($_REQUEST["isAuthenticated"])) {
				$result = array('status' => $response->getStatusCode(), 'result' => ($response->getStatusCode() == 401 ? false : true));
				print_r(json_encode($result));
			} else {
				if (isset($_REQUEST['actionType']) && $_REQUEST['actionType'] == 'download') {
					$headers = $response->getHeaders();
					header('Content-Description: File Transfer');
					header('Content-Type: application/octet-stream');
					header('Content-Disposition: ' . $headers['content-disposition']);
					header('Content-Transfer-Encoding: binary'); //changed to chunked
					header('Expires: 0');
					header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
					header('Pragma: public');
				}
			
				print_r($response->getBody(TRUE));
			}
		} else if ($settings->getAuthenticationMethod() == "oauth1") {	
			require_once BASE_PATH . "/controllers/endpoint/OAuth1Endpoint.php";
			
			//  Init the OAuth options
			$options = array(
					'consumer_key' => $settings->getConsumerKey(),
					'consumer_secret' => $settings->getConsumerSecret(),
					'server_uri' => $settings->getURL(),
					'request_token_uri' => $settings->getRequestTokenURL(),
					'authorize_uri' => $settings->getAuthorizationURL(),
					'access_token_uri' => $settings->getAccessTokenURL()
			);

			$token = $store->getRequestToken();

			if ($token != null) {
				if (is_string($token)) {
					$token = unserialize($token);
				} else {
					$store->deleteTokens();
					$token = null;
				}
				
				if (!isset($token["oauth_token"])) {
					$store->deleteTokens();
					$token = null;
				}
			}
	
			if ($token != null) {
				
				OAuthStore::instance("Session", $options);

				// Get an access token
				$oauthToken = $token["oauth_token"];
				
				$method = $token["requestMethod"];
				
				$tokenResultParams = $token;
				
				$accessToken = $store->getOAuthAccessToken();
			
				if ($accessToken == null || $accessToken == false) {
					try {
						SBTKOAuthRequester::requestAccessToken($settings->getConsumerKey(), $oauthToken, 0, $method, $token);
					}
					catch (OAuth1Exception2 $e)
					{
						
						var_dump($e);
						// Something wrong with the oauth_token.
						// Could be:
						// 1. Was already ok
						// 2. We were not authorized
						return;
					}
					$store->storeOAuthAccessToken(true);
				}
				if ($server == null) {
					$server = $settings->getURL();
				}
				
				// Get headers and request method
				$headers = apache_request_headers();
				$method = $_SERVER['REQUEST_METHOD'];
				
				if ($method == 'POST') {
					$files = null;

					$request = new SBTKOAuthRequester($server . "/" . $url, $method, $tokenResultParams);
				} else {
					$request = new SBTKOAuthRequester($server . "/" . $url, $method, $tokenResultParams);
				}			
				
				$result = $request->doRequest(0);
				
				if ($result['code'] == 200) {
					if (isset($_REQUEST['actionType']) && $_REQUEST['actionType'] == 'download') {
						$headers =$result['headers'];
						header('Content-Description: File Transfer');
						header('Content-Type: application/octet-stream');
						header('Content-Disposition: ' . $headers['content-disposition']);
						header('Content-Transfer-Encoding: binary'); //changed to chunked
						header('Expires: 0');
						header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
						header('Pragma: public');
					}
							
					print_r($result['body']);
				} else if ($result['code'] == 302) {
					$headers = $result['headers'];
					$this->route($headers['location']);
				} else {
					print_r($result);
					echo 'Error';
				}
			} else {
				// Create endpoint
				$oauth = new OAuth1Endpoint($options);
			
	 			// Send request
				$body = $oauth->request($url, 'https://localhost/core/src/index.php?plugin=guzzle&class=OAuth1Endpoint&method=callback');

				print_r($body);
			}
		}
	}
	
	/**
	 * Re-writes the URL for the file download request (because the files API does something funny with the
	 * URL generation) and then routes the requests.
	 *
	 * @author Benjamin Jakobus
	 */
	public function fileOperations() {
	
		if (isset($_GET["_redirectUrl"])) {
			if (strpos($_GET["_redirectUrl"], '/DownloadFile/') !== FALSE) {
				$url = $_GET['_redirectUrl'];
				
				// Extract library ID and file ID
				$keys = parse_url($url); 
				$path = explode("/", $keys['path']);
	
				$libraryID = $path[sizeof($path) - 1];
				$fileID = $path[sizeof($path) - 2];
				
				// Create new URL
				$url = "/files/basic/api/library/" . $libraryID . "/document/" . $fileID . "/media";
			
				// Update request
				$_REQUEST['_redirectUrl'] = $url;
				$_GET['_redirectUrl'] = $url;
				$_REQUEST['actionType'] = 'download';
				
			} else if (strpos($_GET["_redirectUrl"], '/UploadFile/') !== FALSE) {
				// Create new URL
// 				$url = "/files/form/api/myuserlibrary/feed";
				$url = "/files/basic/api/myuserlibrary/feed";
				
				// Update request
				$_REQUEST['_redirectUrl'] = $url;
				$_GET['_redirectUrl'] = $url;
				$_POST['visibility'] = $_GET['visibility'];
			}
			// Route
			$this->route();
		}
	}
}
?>