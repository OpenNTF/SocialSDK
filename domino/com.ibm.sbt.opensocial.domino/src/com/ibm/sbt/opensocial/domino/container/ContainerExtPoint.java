package com.ibm.sbt.opensocial.domino.container;

import org.apache.shindig.gadgets.oauth.OAuthStore;

import com.ibm.sbt.opensocial.domino.config.OpenSocialContainerConfig;

/**
 * Applications wishing to register their container should implement this class and use the ContainerExtPointManager to 
 * add their containers to the OpenSocial implementation.
 *
 */
public interface ContainerExtPoint {
	
	/**
	 * The ID of the container.  This should be unique among all other containers.
	 * @return The ID of the container.
	 */
	public String getId();
	
	/**
	 * Gets the container configuration object for this container.
	 * @return The container configuration object for this container.
	 */
	public OpenSocialContainerConfig getContainerConfig();
	
	/**
	 * Gets the OAuth 1.0a store for the container.
	 * @return The OAuth 1.0a store for the container.
	 */
	public OAuthStore getContainerOAuthStore();
}
