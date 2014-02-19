<?php

/**
 * OAuthSession is a really *dirty* storage. It's useful for testing and may 
 * be enough for some very simple applications, but it's not recommended for
 * production use.
 * 
 * @version $Id: OAuthStoreSession.php 153 2010-08-30 21:25:58Z brunobg@corollarium.com $
 * @author BBG
 * 
 * The MIT License
 * 
 * Copyright (c) 2007-2008 Mediamatic Lab
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
if (file_exists(BASE_PATH . '/core/models/CredentialStore.php')) {
	require_once BASE_PATH . '/core/models/CredentialStore.php';
} else {
	require_once BASE_PATH . '/models/CredentialStore.php';
}


if (file_exists(BASE_PATH . '/core/models/SBTKSettings.php')) {
	require_once BASE_PATH . '/core/models/SBTKSettings.php';
} else {
	require_once BASE_PATH . '/models/SBTKSettings.php';
}

require_once dirname(__FILE__) . '/OAuthStoreAbstract.class.php';
class OAuthStoreSBTK extends OAuthStoreAbstract
{
	private $store; 
	private $settings;

	/*
	 * Takes two options: consumer_key and consumer_secret
	 */
	public function __construct( $options = array() )
	{
		$this->store = new CredentialStore();
		$this->settings = new SBTKSettings();
	}

	public function getSecretsForVerify ( $consumer_key, $token, $token_type = 'access' ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getSecretsForSignature ( $uri, $user_id ) 
	{
		$session['consumer_key'] = $this->settings->getConsumerKey();
		$session['consumer_secret'] = $this->settings->getConsumerSecret();
		$session['signature_methods'] = array('PLAINTEXT');
		$session['token_type'] = $this->store->getTokenType();
		$session['server_uri'] = $this->settings->getURL();
		$session['request_token_uri'] = $this->settings->getRequestTokenURL();
		$session['authorize_uri'] = $this->settings->getAuthorizationURL();
		$session['access_token_uri'] = $this->settings->getAccessTokenURL();
		$session['token_secret'] = $this->store->getTokenSecret();
		$session['token'] = $this->store->getToken();
		return $session;
	}

	public function getServerTokenSecrets ( $consumer_key, $token, $token_type, $user_id, $name = '') 	
	{ 
		if ($consumer_key != $this->settings->getConsumerKey()) {
			return array();
		} 
		return array(
			'consumer_key' => $consumer_key,
			'consumer_secret' => $this->settings->getConsumerSecret(),
			'token' => $token,
			'token_secret' => $this->store->getTokenSecret(),
			'token_name' => $name,
			'signature_methods' => array('PLAINTEXT'),
			'server_uri' => $this->settings->getURL(),
			'request_token_uri' => $this->settings->getRequestTokenURL(),
			'authorize_uri' => $this->settings->getAuthorizationURL(),
			'access_token_uri' => $this->settings->getAccessTokenURL(),
			'token_ttl' => 3600,
		);
	}
	
	public function addServerToken ( $consumer_key, $token_type, $token, $token_secret, $user_id, $options = array() ) 
	{
		if ($token_type == 'request') {
			$this->store->storeRequestToken($token);
		}
		$this->store->storeTokenType($token_type);
		$this->store->storeToken($token);
		$this->store->storeTokenSecret($token_secret);
	}

	public function deleteServer ( $consumer_key, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getServer( $consumer_key, $user_id, $user_is_admin = false ) { 
		return array( 
			'id' => 0,
			'user_id' => $user_id,
			'consumer_key' => $this->settings->getConsumerKey(),
			'consumer_secret' => $this->settings->getConsumerSecret(),
			'signature_methods' => array('PLAINTEXT'),
			'server_uri' => $this->settings->getURL(),
			'request_token_uri' => $this->settings->getRequestTokenURL(),
			'authorize_uri' => $this->settings->getAuthorizationURL(),
			'access_token_uri' => $this->settings->getAccessTokenURL()
		);
	}
	
	public function getServerForUri ( $uri, $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function listServerTokens ( $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function countServerTokens ( $consumer_key ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getServerToken ( $consumer_key, $token, $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function deleteServerToken ( $consumer_key, $token, $user_id, $user_is_admin = false ) {
		// TODO 
	}

	public function setServerTokenTtl ( $consumer_key, $token, $token_ttl )
	{
		//This method just needs to exist. It doesn't have to do anything!
	}
	
	public function listServers ( $q = '', $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function updateServer ( $server, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }

	public function updateConsumer ( $consumer, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function deleteConsumer ( $consumer_key, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getConsumer ( $consumer_key, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getConsumerStatic () { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }

	public function addConsumerRequestToken ( $consumer_key, $options = array() ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getConsumerRequestToken ( $token ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function deleteConsumerRequestToken ( $token ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function authorizeConsumerRequestToken ( $token, $user_id, $referrer_host = '' ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function countConsumerAccessTokens ( $consumer_key ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function exchangeConsumerRequestForAccessToken ( $token, $options = array() ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function getConsumerAccessToken ( $token, $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function deleteConsumerAccessToken ( $token, $user_id, $user_is_admin = false ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function setConsumerAccessTokenTtl ( $token, $ttl ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	
	public function listConsumers ( $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function listConsumerApplications( $begin = 0, $total = 25 )  { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function listConsumerTokens ( $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }

	public function checkServerNonce ( $consumer_key, $token, $timestamp, $nonce ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	
	public function addLog ( $keys, $received, $sent, $base_string, $notes, $user_id = null ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	public function listLog ( $options, $user_id ) { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }
	
	public function install () { throw new OAuthException2("OAuthStoreSession doesn't support " . __METHOD__); }		
}

?>