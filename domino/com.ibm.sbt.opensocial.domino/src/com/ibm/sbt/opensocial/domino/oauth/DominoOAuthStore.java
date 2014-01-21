package com.ibm.sbt.opensocial.domino.oauth;

/**
 * Stores OAuth 1.0a information.
 *
 */
public interface DominoOAuthStore {
	
	/**
	 * Gets OAuth 1.0a client information.
	 * @param user The user who rendered the gadget requesting the OAuth client information.
	 * @param container The container the gadget rendered in.
	 * @param service The OAuth service being requested by the gadget.
	 * @param gadgetUri The gadget URI of the gadget requesting the OAuth client information.
	 * @return The OAuth client information.
	 */
	public DominoOAuthClient getClient(String user, String container, String service, String gadgetUri);
}
