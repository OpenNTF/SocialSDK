***REMOVED***

// require('SBTKClient.php');
// require('GrantType/IGrantType.php');
// require('GrantType/AuthorizationCode.php');

const CLIENT_ID     = 'app_20542369_1392729582739';
const CLIENT_SECRET = 'ad839badd50f5bfff6fe31ae3bbf87e6daa6dd81a7b79e4cd39a0b2f0c43dc54131ae9f014314dd68b6929f0c727e550f5d5c27e88b767bc442562bbe983cf14a0ee5427aacfcf6e4e1377f75a2e27cf3296b4a2442d3ad68cd23b93fa23b0e53130ad405d16416d08851b9e5fb54ee1c51eed2280f66c69a83ddc5dd8954c';

const REDIRECT_URI           = 'https://localhost/wordpress2/wp-content/plugins/ibm-sbtk/core/samples/OAuth2Plugin.php';
const AUTHORIZATION_ENDPOINT = 'https://apps.lotuslive.com/manage/oauth2/authorize';
const TOKEN_ENDPOINT         = 'https://apps.lotuslive.com/manage/oauth2/token';

include '../system/libs/vendor/autoload.php';
use Guzzle\Http\Client;



if (!isset($_GET['code'])) {
	$parameters = array(
			'response_type' => 'code',
			'client_id'     => CLIENT_ID,
			'callback_uri'  => REDIRECT_URI
	);
	$auth_url = AUTHORIZATION_ENDPOINT . '?' . http_build_query($parameters, null, '&');
    header('Location: ' . $auth_url);
    die('Redirect');
} else {
	$parameters = array(
		'callback_uri'  => REDIRECT_URI,
		'code' => $_GET['code'],
		'grant_type' => 'authorization_code',
		'client_id' => CLIENT_ID,
		'client_secret' => CLIENT_SECRET
	);
	$token_url = TOKEN_ENDPOINT . '?' . http_build_query($parameters, null, '&');
	$client = new Client($token_url);
	
	$client->setDefaultOption('verify', false);
	$headers = null;
	$body = null;
	$options = array();
	$response = null;
	try {
		$request = $client->createRequest('GET', $token_url , $headers, $body,  $options);
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
		$client = new Client('https://apps.na.collabserv.com');

		$url = 'https://apps.na.collabserv.com/communities/service/atom/communities/all';

		$request = $client->get('/communities/service/atom/communities/all');
		$request->addHeader('authorization', 'Bearer ' . $info['access_token']);
		$response = $request->send();
		
		foreach ($response->getHeaderLines() as $h) {
			if (strpos($h, "Content-Type") === 0) header($h, TRUE);
		}
		
		header(':', true, $response->getStatusCode());
		header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
	} catch (Guzzle\Http\Exception\BadResponseException $e) {
		$response = $e->getResponse();
	}
	print_r($response->getBody(TRUE));
}
