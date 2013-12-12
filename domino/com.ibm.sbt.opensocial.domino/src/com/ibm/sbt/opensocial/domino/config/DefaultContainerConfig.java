package com.ibm.sbt.opensocial.domino.config;

import java.util.Map;

import org.apache.shindig.auth.BlobCrypterSecurityTokenCodec;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.common.crypto.Crypto;
import org.apache.shindig.common.util.CharsetUtil;
import org.apache.shindig.config.ContainerConfig;

import com.google.caja.util.Maps;
import com.google.common.base.Objects;

/**
 * A default implementation of OpenSocialContainerConfig.  This implementation contains properties and
 * values that almost every container will need to override.  Almost all container should extend this
 * class and override the {@link #getProperties()} methods if they need to override
 * additional container properties.
 *
 */
public class DefaultContainerConfig implements OpenSocialContainerConfig {
	private static final String DOMINO_CONTAINER_NAME = "domino";
	private static final int DEFAULT_ST_TTL = 3600; //in seconds
	
	protected Map<String, Object> props;
	
	public DefaultContainerConfig() {
		this.props = Maps.newHashMap();
		props.put(BlobCrypterSecurityTokenCodec.SECURITY_TOKEN_KEY, 
				CharsetUtil.newUtf8String(Crypto.getRandomBytes(20)));
		props.put(ContainerConfig.PARENT_KEY, DOMINO_CONTAINER_NAME);
		props.put(SecurityTokenCodec.SECURITY_TOKEN_TTL_CONFIG, DEFAULT_ST_TTL);
	}
	
	@Override
	public Map<String, Object> getProperties() {
		return props;
	}

	@Override
	public boolean equals(Object o) {
		boolean equal = false;
		if(o instanceof OpenSocialContainerConfig) {
			OpenSocialContainerConfig test = (OpenSocialContainerConfig)o;
			equal = this.getProperties().keySet().size() == test.getProperties().size();
			//Do not verify the security token keys match, there is no guarantee they will
			equal &= this.getProperties().get(ContainerConfig.PARENT_KEY).equals(test.getProperties().get(ContainerConfig.PARENT_KEY)) &&
					this.getProperties().get(SecurityTokenCodec.SECURITY_TOKEN_TTL_CONFIG).equals(test.getProperties().get(SecurityTokenCodec.SECURITY_TOKEN_TTL_CONFIG));
		}
		return equal;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(props);
	}
}
