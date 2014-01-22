<?php
	// Autoloader
	require_once '../autoload.php';

	$settings = new SBTKSettings();

	// Init the OAuth options
	$options = array(
		'consumer_key' => $settings->getConsumerKey(),
		'consumer_secret' => $settings->getConsumerSecret(),
		'server_uri' => $settings->getURL(),
		'request_token_uri' => $settings->getRequestTokenURL(),
		'authorize_uri' => $settings->getAuthorizationURL(),
		'access_token_uri' => $settings->getAccessTokenURL()
	);
	
	// Create endpoint
	$oauth = new OAuth1Endpoint($options);

	// Send request
	$body = $oauth->request('https://apps.na.collabserv.com/communities/service/atom/catalog/my?results=10&start=0&sortKey=update_date&sortOrder=desc',
		'https://localhost/core/src/index.php?plugin=guzzle&class=OAuth1Endpoint&method=callback', 'POST');

	var_dump($body);