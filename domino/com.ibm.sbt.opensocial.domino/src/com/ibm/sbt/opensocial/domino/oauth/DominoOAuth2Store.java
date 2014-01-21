package com.ibm.sbt.opensocial.domino.oauth;

/**
 * A store for OAuth 2.0 information.
 *
 */
public interface DominoOAuth2Store {
	
	/**
	 * Gets an OAuth 2.0 client.
	 * @param user The user who rendered the gadget requesting the OAuth client info.
	 * @param service The service from the gadget requesting the OAuth client info.
	 * @param container The container the gadget was rendered in.
	 * @param scope The OAuth 2.0 client scope.
	 * @param gadgetUri The URI of the gadget requesting the OAuth client info.
	 * @return The OAuth 2.0 client.
	 */
	public DominoOAuth2Client getClient(String user, String service, String container, String scope, String gadgetUri);
}
