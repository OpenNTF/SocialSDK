package com.ibm.sbt.opensocial.domino.container;

import com.ibm.sbt.opensocial.domino.config.OpenSocialContainerConfig;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Store;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthStore;

/**
 * Applications wishing to register their container should implement this class and use the ContainerExtPointManager to 
 * add their containers to the OpenSocial implementation.
 *
 */
public interface ContainerExtPoint {
	
	/**
	 * The ID of the container.  This should be unique among all other containers.
	 * A container ID MUST
	 * <ul>
	 * 	<li>be URL encoded</li>
	 *  <li>NOT contain a colon</li>
	 * </ul>
	 * @return The ID of the container.
	 * @throws ContainerExtPointException Thrown when there is an error generating a container ID.
	 */
	public String getId() throws ContainerExtPointException;
	
	/**
	 * Gets the container configuration object for this container.
	 * @return The container configuration object for this container.
	 */
	public OpenSocialContainerConfig getContainerConfig();
	
	/**
	 * Gets the OAuth 1.0a store for the container.
	 * @return The OAuth 1.0a store for the container.
	 * @throws ContainerExtPointException Thrown when there is an error getting an OAuth store.
	 */
	public DominoOAuthStore getContainerOAuthStore() throws ContainerExtPointException;
	
	/**
	 * Gets the OAuth 2.0 store for the container.
	 * @return The OAuth 2.0 store for the container.
	 * @throws ContainerExtPointException Thrown when there is an error getting an OAuth 2 store.
	 */
	public DominoOAuth2Store getContainerOAuth2Store() throws ContainerExtPointException;
}
