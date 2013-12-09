package com.ibm.sbt.opensocial.domino.config;

import java.util.Map;

/**
 * Used by the extension point com.ibm.sbt.opensocial.domino.container.config to provide configuration for a container.
 *
 */
public interface OpenSocialContainerConfig {
	
	/**
	 * Gets the collection of properties to be overriden by the container.  If a property is not in this list
	 * than the default value from the OpenSocial implementation will be used.
	 * @return The collection of properties to be overriden by the container.
	 */
	public Map<String, Object> getProperties();
}
