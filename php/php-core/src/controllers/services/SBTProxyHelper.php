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


/**
 * Helper object for the proxy.
 * 
 * @author Benjamin Jakobus
 *
 */

class SBTProxyHelper extends BaseController
{
	/**
	 * Construct header for forwarding request.
	 *
	 * @param unknown $method
	 * @return multitype:unknown
	 */
	public function getHeader($method) 
	{
		$headers = apache_request_headers();
	
		$forwardHeader = array();

// 		foreach ($headers as $key => $value) {
// 			$forwardHeader[$key] = $value;
// 		}
		
		if (isset($headers['Content-Length'])) {
			$forwardHeader['Content-Length'] = $headers['Content-Length'];
		}
	
		if (isset($headers['Slug'])) {
			$forwardHeader['Slug'] = $headers['Slug'];
		} else if (isset($_GET['slug'])) {
			$forwardHeader['Slug'] = $_GET['slug'];
		}
	
		if (isset($headers['X-Update-Nonce'])) {
			$forwardHeader['x-update-nonce'] = $headers['X-Update-Nonce'];
		}
	
		if (isset($headers['Transfer-Encoding'])) {
			$forwardHeader['Transfer-Encoding'] = $headers['Transfer-Encoding'];
		}
	
		if (isset($headers['Content-Type'])) {
			$forwardHeader['Content-Type'] = $headers['Content-Type'];
		}
		
		if (isset($headers['Content-Language'])) {
			$forwardHeader['Content-Language'] = $headers['Content-Language'];
		}
	
		if ($method == 'PUT' || $method == 'POST') {
			if (!isset($forwardHeader['X-Update-Nonce'])) {
				$random = mt_rand(0, 999999);
				$nonce = sha1($random);
				$forwardHeader['x-update-nonce'] = $nonce;
			}
		}
	
		if (isset($headers['page'])) {
			$forwardHeader['page'] = $headers['page'];
		}
		
		if (isset($headers['sortBy'])) {
			$forwardHeader['sortBy'] = $headers['sortBy'];
		}
		
		if (isset($headers['sortOrder'])) {
			$forwardHeader['sortOrder'] = $headers['sortOrder'];
		}
		
		if (isset($headers['creator'])) {
			$forwardHeader['creator'] = $headers['creator'];
		}
	
		return $forwardHeader;
	}
	
	/**
	 * Prepares the URL for forwarding (i.e. cleans it and appends any set $_GET parameters).
	 * 
	 * @param string $url		The URL to clean
	 * @param unknown $server
	 * @return string
	 */
	public function cleanURL($url, $server) 
	{
		$url = str_replace('https', '', $url);
		$url = str_replace('http', '', $url);
		$url = str_replace($server, '', $url);
		
		$server = str_replace('https://', '', $server);
		$server = str_replace('http://', '', $server);
		$url = str_replace($server, '', $url);
		$url = str_replace('//', '', $url);
		
		// Check if the URL already contains GET parameters
		if (strpos($url, '?') === false) {
			$url .= "?";
		}
		
		foreach ($_GET as $key => $value) { 
			if ($key == 'class' || $key == 'classpath' || $key == 'method' || $key == 'endpointName' || $key == '_redirectUrl' || $key == 'uid') {
				continue;
			}
			
			if ($url[strlen($url) - 1] == '?') {
				$url .= "$key=$value";
			} else {
				$url .= "&$key=$value";
			}
		}
		
		// Remove trailing '?' if no GET parameters were appended
		if ($url[strlen($url) - 1] == '?') {
			$url = rtrim($url, '?');
		}
		
		return $url;
	}
	
	/**
	 * Get $_REQUEST options to forward.
	 * 
	 * @return unknown
	 */
	public function getOptions() 
	{
		$options = $_REQUEST;
			
		// Remove proxy-specific parameters
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
	
		if (isset($options['uid'])) {
			unset($options['uid']);
		}
	
		if (isset($options['slug'])) {
			unset($options['slug']);
		}
	
		if (isset($options['X-Endpoint-name'])) {
			unset($options['X-Endpoint-name']);
		}
		
		return $options;
	}
	
	/**
	 * Get the name of the endpoint to use.
	 * 
	 * @return Ambigous <string, unknown>
	 */
	public function determineEndpoint() 
	{
		$headers = apache_request_headers();
		
		$endpointName = "connections";
		if (isset($_GET['endpointName'])) {
			$endpointName = $_GET['endpointName'];
		} else if (isset($headers['X-Endpoint-name'])) {
			$endpointName = $headers['X-Endpoint-name'];
		}
		return $endpointName;
	}
	
	/**
	 * Prints the response from SmartCloud or IBM Connections on Premise.
	 *
	 * @param unknown $response
	 */
	public function outputResponse($response, $url) 
	{
		$store = SBTCredentialStore::getInstance();
	
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
	
				if ( (isset($_REQUEST['actionType']) && $_REQUEST['actionType'] == 'download') || (strpos($url, '/media/') != false && strpos($url, '/document/') != false) ) {
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
		} else if ($response->getStatusCode() == 400) {
			echo "400 - Bad Request";
			print_r($response->getBody(TRUE));
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
	
}
?>
