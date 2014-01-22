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


require_once 'Endpoint.php';
require_once BASE_PATH . "/system/libs/oauth-php/library/OAuthStore.php";
require_once BASE_PATH . "/system/core/SBTKOAuthRequester.php";

/**
 * OAuth 1.0 Endpoint
 * 
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class OAuth1Endpoint extends BaseController
{

	/**
	 * Create a new OAuth 1.0 plugin for SmartCloud
	 *
	 * @param array $config Configuration array containing these parameters:
	 *     - string 'consumer_key'       Consumer key.
	 *     - string 'consumer_secret'    Consumer secret.
	 *     - string 'server_uri'         Server URL (e.g. https://apps.na.collabserv.com)
	 *     - string 'request_token_uri'  Request token URL (e.g. https://apps.na.collabserv.com/manage/oauth/getRequestToken)
	 *     - string 'authorize_uri'      Authorize token URL (e.g. https://apps.na.collabserv.com/manage/oauth/authorizeToken)
	 *     - string 'access_token_uri'   Access token URL (e.g. https://apps.na.collabserv.com/manage/oauth/getAccessToken)
	 *     
	 * @author Benjamin Jakobus
	 */
	public function __construct($config = array())
	{
		$this->options = $config;

		// Note: do not use "Session" storage in production. Prefer a database
		// storage, such as MySQL.
		OAuthStore::instance("Session", $this->options);
	}
	
	/**
	 * Performs a request to the given request URL.
	 * 
	 * @param string $requestURL		Resource that is to be requested (e.g. https://apps.na.collabserv.com/communities/service/html/mycommunities)
	 * @param string $callbackURL		The callback URL (e.g. http://127.0.0.1:8443/demo/application/OAuthSample.php)
	 * @param string $method			GET, PUT or POST. POST by default
	 * 
	 * @author Benjamin Jakobus
	 */
	public function request($requestURL, $callbackURL, $method = 'POST'){
		$callbackURL = $callbackURL . "&requestMethod=" . $method . "&requestURL=" . urlencode($requestURL);

		try
		{	
			//  STEP 1:  If we do not have an OAuth token yet, go get one
			if (empty($_GET["oauth_token"]))
			{			
				$getAuthTokenParams = array(
						'scope' => $requestURL,
						'oauth_callback' => $callbackURL);	

				// get a request token
				$tokenResultParams = SBTKOAuthRequester::requestRequestToken($this->options['consumer_key'], 0, $getAuthTokenParams);
				
				// redirect to the authorization page, they will redirect back
// 				echo '<script type="text/javascript" language="javascript">window.location = "'.$this->options['authorize_uri'] . "?oauth_token=" . $tokenResultParams['token'].'";</script>';
				header("Location: " . $this->options['authorize_uri'] . "?oauth_token=" . $tokenResultParams['token']);
				
			}
			
		}
		catch(OAuth1Exception2 $e) {
			echo "OAuth1Exception2:  " . $e->getMessage();
		}
	}
	
	/**
	 * The callback function.
	 * 
	 * @author Benjamin Jakobus
	 */
	public function callback() {

		if (empty($_GET["oauth_token"])) {
			return;
		}

		// Get an access token
		$oauthToken = $_GET["oauth_token"];
		$requestURL = urldecode($_GET['requestURL']);
		
		$method = $_GET["requestMethod"];
		
		$tokenResultParams = $_GET;

		try {
			SBTKOAuthRequester::requestAccessToken($this->options['consumer_key'], $oauthToken, 0, $method, $_GET);
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
		
		// make the request
		$request = new SBTKOAuthRequester($requestURL, 'GET', $tokenResultParams);
		
		$result = $request->doRequest(0);
		if ($result['code'] == 200) {
			var_dump($result['body']);
		}
		else {
			echo 'Error';
		}
	}
	
	/**
	 * The callback function for authenticating the user and then storing the token in the CredentialStore (no content
	 * is being requested).
	 *
	 * @author Benjamin Jakobus
	 */
	public function authenticationCallback() {
	
		if (empty($_GET["oauth_token"])) {
			return;
		}

		// Store $tokenResultParams
		$this->loadModel('CredentialStore');
		$store = new CredentialStore();
		$store->storeRequestToken(serialize($_GET));
		
		$requestURL = urldecode($_GET['requestURL']);
		
		header("Location: " . $requestURL);
	}
}