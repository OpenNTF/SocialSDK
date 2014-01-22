package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Collection;

import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2CallbackState;
import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.OAuth2Token.Type;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Cache;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2CacheException;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Client;

/**
 * Caching layer for OAuth 2 information.
 * This is just here to satisfy Shindig we don't actually use it for anything.
 */
public class DominoOAuth2Cache implements OAuth2Cache {

	@Override
	public void clearAccessors() throws OAuth2CacheException {}

	@Override
	public void clearClients() throws OAuth2CacheException {}

	@Override
	public void clearTokens() throws OAuth2CacheException {}

	@Override
	public OAuth2Client getClient(String gadgetUri, String serviceName) {
		return null;
	}

	@Override
	public OAuth2Accessor getOAuth2Accessor(OAuth2CallbackState state) {
		return null;
	}

	@Override
	public OAuth2Token getToken(String gadgetUri, String serviceName,
			String user, String scope, Type type) {
		return null;
	}

	@Override
	public boolean isPrimed() {
		return false;
	}

	@Override
	public OAuth2Client removeClient(OAuth2Client client) {
		return null;
	}

	@Override
	public OAuth2Accessor removeOAuth2Accessor(OAuth2Accessor accessor) {
		return null;
	}

	@Override
	public OAuth2Token removeToken(OAuth2Token token) {
		return null;
	}

	@Override
	public void storeClient(OAuth2Client client) throws OAuth2CacheException {}

	@Override
	public void storeClients(Collection<OAuth2Client> clients)
			throws OAuth2CacheException {}

	@Override
	public void storeOAuth2Accessor(OAuth2Accessor accessor) {}

	@Override
	public void storeToken(OAuth2Token token) throws OAuth2CacheException {}

	@Override
	public void storeTokens(Collection<OAuth2Token> tokens)
			throws OAuth2CacheException {}
}
