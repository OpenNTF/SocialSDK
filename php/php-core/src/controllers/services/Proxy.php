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
 * @author Benjamin Jakobus
 *
 */
// Load settings object and default configuration
require_once BASE_PATH . '/config.php';
include_once BASE_PATH . '/autoload.php';

class Proxy extends BaseController 
{
	
	/**
	 * Routes requests.
	 * 
	 * @param string server			The URL of the server to which to re-direct the request to. Uses SBTSettings if none given.
	 */
	public function route($server = null) 
	{				
		$this->loadModel('SBTSettings');
		$this->loadModel('SBTCredentialStore');
		
		$store = null;
	
		$store = SBTCredentialStore::getInstance();	
		
		$settings = new SBTSettings();
		
		$endpointName = "connections";
		if (isset($_GET['endpointName'])) {
			$endpointName = $_GET['endpointName'];
		}

		if (!isset($_REQUEST["_redirectUrl"])) {
			// Request to check if the user is authenticated
			if (isset($_REQUEST["isAuthenticated"])) {
				$_REQUEST["_redirectUrl"] = '/files/basic/api/myuserlibrary/feed'; //used to be /connections/files/basic/api/myuserlibrary/feed
				$_SERVER['REQUEST_METHOD'] = 'GET';
			} else if (isset($_REQUEST["basicAuthLogout"])) {
				// Logout request
				$store->deleteBasicAuthCredentials($endpointName);
			} 
			 if (isset($_REQUEST["OAuthLogout"])) {	
			 	$store->deleteOAuthCredentials($endpointName);

				$timestamp = time();
				unset($_COOKIE['IBMSBTKOAuthLogin']);
				setcookie('IBMSBTKOAuthLogin', "", $timestamp - 604800);
				return;
			} else {
				return;
			}
			if (isset($_REQUEST["basicAuthLogout"])) {
				return;
			}
		}
		
		
		
		$url = $_REQUEST["_redirectUrl"];
		$url = str_replace("/connections/", "", $url);

		if (isset($_REQUEST['basicAuthRequest']) && $_REQUEST['basicAuthRequest'] == 'true') {
			$store->storeBasicAuthUsername($_POST['username'], $endpointName);
			$store->storeBasicAuthPassword($_POST['password'], $endpointName);
			$result = array('status' => 200, 'result' => true);
			print_r(json_encode($result));
			return;
		}
	
		$method = $_SERVER['REQUEST_METHOD'];

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
		
		if (isset($options['endpointName'])) {
			unset($options['endpointName']);
		}
			
		$headers = null;
		$response = null;
		$body = file_get_contents('php://input');
		$endpoint = null;
		
		if ($server == null) {
			$server = $settings->getURL($endpointName);
		}
		
		$method = $_SERVER['REQUEST_METHOD'];
		$headers = apache_request_headers();
	
		$forwardHeader = null;
		if (isset($headers['Content-Length'])) {
			$forwardHeader['Content-Length'] = $headers['Content-Length'];
		}
		
		if (isset($headers['Content-Type'])) {
			$forwardHeader['Content-Type'] = $headers['Content-Type'];
		}
		if ($settings->getAuthenticationMethod($endpointName) == "basic") {
			$endpoint = new SBTBasicAuthEndpoint();
		} else if ($settings->getAuthenticationMethod($endpointName) == "oauth2") {
			$endpoint = new SBTOAuth2Endpoint();
		} else if ($settings->getAuthenticationMethod($endpointName) == "oauth1") {	
			$endpoint = new SBTOAuth1Endpoint();
		}

	
		// ...yes, this is ugly
		$containsServerURL = false;
		if (strpos($url, '/https/') == 0) {
			$url = str_replace('/https/', 'https://', $url);
			$result = parse_url($url);
			if (isset($result['scheme'])) {
				$url = str_replace($server, '', $url);
				$containsServerURL = true;
			}
		} else if (strpos($url, '/http/') == 0) {
			$url = str_replace('/http/', 'http://', $url);
			$url = str_replace('/https/', 'https://', $url);
			$result = parse_url($url);
			if (isset($result['scheme'])) {
				$url = str_replace($server, '', $url);
				$containsServerURL = true;
			}
		}
		
 		$response = $endpoint->makeRequest($server, $url, $method, $options, $body, $forwardHeader, $endpointName);

		if ($response->getStatusCode() == 200) {
			if (isset($_REQUEST["isAuthenticated"]) && $settings->getAuthenticationMethod() == "basic") {
				$result = array('status' => $response->getStatusCode(), 'result' => ($response->getStatusCode() == 401 ? false : true));
				print_r(json_encode($result));
			} else {
				foreach ($response->getHeaderLines() as $h) {
					if (strpos($h, "Content-Type") === 0) header($h, TRUE);
				}
				
				header(':', true, $response->getStatusCode());
				header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
				
				if ( (isset($_REQUEST['actionType']) && $_REQUEST['actionType'] == 'download') || (strpos($url, '/media/') != false && strpos($url, '/document/') != false
					&& $containsServerURL) ) {
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
		} else if ($response->getStatusCode() == 302) {
			$headers = $response->getHeaders();
			$this->route($headers['location']);
		} else if ($response->getStatusCode() == 201) {
			$result = array('status' => 201, 'result' => true);
			print_r(json_encode($result));
		} else if ($response->getStatusCode() == 401 || $response->getStatusCode() == '401oauth_token_expired') {
			if (isset($_GET['endpointName'])) {
				$store->deleteOAuthCredentials($_GET['endpointName']);
				$store->deleteBasicAuthCredentials($_GET['endpointName']);
			} else {
				$store->deleteOAuthCredentials();
				$store->deleteBasicAuthCredentials();
			}
			print_r($response->getStatusCode());
		} else {
			print_r($response->getBody(TRUE));
		}
	}
	
	/**
	 * Re-writes the URL for the file download request (because the files API does something funny with the
	 * URL generation) and then routes the requests.
	 *
	 * @author Benjamin Jakobus
	 */
	public function fileOperations() 
	{
	
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