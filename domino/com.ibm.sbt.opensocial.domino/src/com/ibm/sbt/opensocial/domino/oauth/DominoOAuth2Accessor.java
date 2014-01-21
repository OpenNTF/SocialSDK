package com.ibm.sbt.opensocial.domino.oauth;

import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;

/**
 * An {@link OAuth2Accessor} with the container ID.
 *
 */
public interface DominoOAuth2Accessor extends OAuth2Accessor {
	
	/**
	 * Gets the container ID.
	 * @return The container ID.
	 */
	public String getContainer();
	
	/**
	 * Sets the container ID.
	 * @param container The container ID.
	 */
	public void setContainer(String container);
}
