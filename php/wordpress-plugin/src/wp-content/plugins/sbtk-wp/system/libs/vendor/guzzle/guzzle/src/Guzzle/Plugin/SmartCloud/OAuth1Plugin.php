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

require_once "../externals/oauth-php/library/OAuthStore.php";
require_once "../externals/oauth-php/library/OAuthRequester.php";

class OAuth1Plugin 
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
	 */
	public function request($requestURL, $callbackURL) {
		
		$callbackURL = $callbackURL . "&requestURL=" . urlencode($requestURL);
		
		try
		{
			//'https://apps.na.collabserv.com/communities/service/html/mycommunities'
			//  STEP 1:  If we do not have an OAuth token yet, go get one
			if (empty($_GET["oauth_token"]))
			{
				$getAuthTokenParams = array(
						'scope' => $requestURL,
						'oauth_callback' => $callbackURL);
		
				// get a request token
				$tokenResultParams = OAuthRequester::requestRequestToken($this->options['consumer_key'], 0, $getAuthTokenParams);
		
				// redirect to the authorization page, they will redirect back
				header("Location: " . $this->options['authorize_uri'] . "?oauth_token=" . $tokenResultParams['token']);
			}
		}
		catch(OAuth1Exception2 $e) {
			echo "OAuth1Exception2:  " . $e->getMessage();
		}
	}
	
	public function callback() {
		
		if (empty($_GET["oauth_token"])) {
			return;
		}

		// Get an access token
		$oauthToken = $_GET["oauth_token"];
		$requestURL = urldecode($_GET['requestURL']);
		
		$tokenResultParams = $_GET;
		
		try {
			OAuthRequester::requestAccessToken($this->options['consumer_key'], $oauthToken, 0, 'POST', $_GET);
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
		$request = new OAuthRequester($requestURL, 'GET', $tokenResultParams);
		$result = $request->doRequest(0);
		if ($result['code'] == 200) {
			var_dump($result['body']);
		}
		else {
			echo 'Error';
		}
	}
}