package com.ibm.sbt.opensocial.domino.oauth.clients;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthClient;

/**
 * An OAuth client for DropBox.
 *
 */
public class DropBoxOAuthClient extends DominoOAuthClient {
	
	/**
	 * Creates a new OAuth client for DropBox.
	 * @param consumerKey A DropBox OAuth 1.0a consumer key.
	 * @param consumerSecret A DropBox OAuth 1.0a consumer secret.
	 */
	public DropBoxOAuthClient(String consumerKey, String consumerSecret) {
		super();
		this.setConsumerKey(consumerKey);
		this.setConsumerSecret(consumerSecret);
		this.setForceCallbackOverHttps(false);
		this.setKeyType(KeyType.HMAC_SYMMETRIC);
	}
}
