package com.ibm.sbt.opensocial.domino.oauth.clients;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthClient;

/**
 * A {@link DominoOAuthClient} for SmartCloud.
 *
 */
public class SmartCloudOAuthClient extends DominoOAuthClient {
	/**
	 * Creates a new SmartCloud DominoOAuthClient.
	 * @param consumerKey The SmartCloud OAuth 1.0a consumer key.
	 * @param consumerSecret The SmartCloud OAuth 1.0a consumer secret.
	 */
	public SmartCloudOAuthClient(String consumerKey, String consumerSecret) {
		super();
		this.setConsumerKey(consumerKey);
		this.setConsumerSecret(consumerSecret);
		this.setForceCallbackOverHttps(true);
		this.setKeyType(KeyType.PLAINTEXT);
	}
}
