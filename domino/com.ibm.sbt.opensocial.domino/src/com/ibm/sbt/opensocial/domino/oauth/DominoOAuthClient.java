package com.ibm.sbt.opensocial.domino.oauth;

import com.google.common.base.Objects;

/**
 * Represents an OAuth 1.0a client.
 *
 */
public class DominoOAuthClient {
	
	/**
	 * Type of encryption used for the OAuth key.
	 */
	public static enum KeyType { HMAC_SYMMETRIC, RSA_PRIVATE, PLAINTEXT }
	
	private String consumerKey;
	private String consumerSecret;
	private KeyType keyType;
	private boolean forceCallbackOverHttps = false;
	
	/**
	 * Gets the OAuth consumer key.
	 * @return The OAuth consumer key.
	 */
	public String getConsumerKey() {
		return consumerKey;
	}
	
	/**
	 * Sets the OAuth consumer key.
	 * @param consumerKey The OAuth consumer key.
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	/**
	 * Gets the OAuth consumer secret.
	 * @return The OAuth consumer secret.
	 */
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	/**
	 * Sets the OAuth consumer secret.
	 * @param consumerSecret The OAuth consumer secret.
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	/**
	 * Gets the OAuth key type for this client. 
	 * @return The OAuth key type for this client.
	 */
	public KeyType getKeyType() {
		return keyType;
	}
	
	/**
	 * Sets the OAuth key type.
	 * @param keyType The OAuth key type.
	 */
	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}
	
	/**
	 * Indicates whether the client will force the OAuth callback to be over HTTPs.
	 * @return True if the client will force the OAuth callback over HTTPs, false otherwise.
	 */
	public boolean isForceCallbackOverHttps() {
		return forceCallbackOverHttps;
	}
	
	/**
	 * Sets the OAuth callback to be over HTTPs.
	 * @param forceCallbackOverHttps True to force the OAuth callback to be over HTTPs, false otherwise.
	 */
	public void setForceCallbackOverHttps(boolean forceCallbackOverHttps) {
		this.forceCallbackOverHttps = forceCallbackOverHttps;
	}

	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof DominoOAuthClient) {
			DominoOAuthClient test = (DominoOAuthClient)o;
			result = consumerKey == null ? consumerKey == test.getConsumerKey() : consumerKey.equals(test.getConsumerKey());
			result &= consumerSecret == null ? consumerSecret == test.getConsumerSecret() : consumerSecret.equals(test.getConsumerSecret());
			result &= keyType == null ? keyType == test.getKeyType() : keyType.equals(test.getKeyType());
			result &= !forceCallbackOverHttps && !test.isForceCallbackOverHttps() ? true : forceCallbackOverHttps && test.isForceCallbackOverHttps();
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(consumerKey, consumerSecret, keyType, forceCallbackOverHttps);
	}
}
