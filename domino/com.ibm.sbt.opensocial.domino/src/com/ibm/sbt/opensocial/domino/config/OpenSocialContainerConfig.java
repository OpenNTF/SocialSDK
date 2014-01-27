package com.ibm.sbt.opensocial.domino.config;

import java.util.Map;

/**
 * Used by the extension point com.ibm.sbt.opensocial.domino.container.config to provide configuration for a container.
 *
 */
public interface OpenSocialContainerConfig {
	
	/**
	 * Key to allow untrusted SSL connections.  Set to true or false.
	 */
	public static final String ALLOW_UNTRUSTED_SSL_CONNECTIONS = "domino.allowuntrustedsslconnections";
	
	/**
	 * Gets the collection of properties to be overriden by the container.  If a property is not in this list
	 * than the default value from the OpenSocial implementation will be used.
	 * @return The collection of properties to be overriden by the container.
	 */
	public Map<String, Object> getProperties();
}
