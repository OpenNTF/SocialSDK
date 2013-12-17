package com.ibm.sbt.opensocial.domino.oauth;

import net.oauth.OAuthServiceProvider;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.GadgetException.Code;
import org.apache.shindig.gadgets.oauth.OAuthStore;

import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointException;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;

/**
 * OAuth 1.0a store for OpenSocial gadgets on Domino.
 *
 */
public class DominoOAuthStore implements OAuthStore {

	private ContainerExtPointManager extPointManager;
	
	/**
	 * Token store for the OpenSocial implementation.  This store just delegates to 
	 * the token stores for the individual containers.
	 */
	public DominoOAuthStore(ContainerExtPointManager extPointManager) {
		this.extPointManager = extPointManager;
	}

	@Override
	public ConsumerInfo getConsumerKeyAndSecret(SecurityToken securityToken,
			String serviceName, OAuthServiceProvider provider)
			throws GadgetException {
		ContainerExtPoint extPoint = getContainerExtPoint(securityToken.getContainer());
		try {
			return extPoint.getContainerOAuthStore().getConsumerKeyAndSecret(securityToken, serviceName, provider);
		} catch (ContainerExtPointException e) {
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, e);
		}
	}

	@Override
	public TokenInfo getTokenInfo(SecurityToken securityToken,
			ConsumerInfo consumerInfo, String serviceName, String tokenName)
			throws GadgetException {
		ContainerExtPoint extPoint = getContainerExtPoint(securityToken.getContainer());
		try {
			return extPoint.getContainerOAuthStore().getTokenInfo(securityToken, consumerInfo, serviceName, tokenName);
		} catch (ContainerExtPointException e) {
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, e);
		}
	}

	@Override
	public void setTokenInfo(SecurityToken securityToken,
			ConsumerInfo consumerInfo, String serviceName, String tokenName,
			TokenInfo tokenInfo) throws GadgetException {
		ContainerExtPoint extPoint = getContainerExtPoint(securityToken.getContainer());
		try {
			extPoint.getContainerOAuthStore().setTokenInfo(securityToken, consumerInfo, serviceName, tokenName, tokenInfo);
		} catch (ContainerExtPointException e) {
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, e);
		}
	}

	@Override
	public void removeToken(SecurityToken securityToken,
			ConsumerInfo consumerInfo, String serviceName, String tokenName)
			throws GadgetException {
		ContainerExtPoint extPoint = getContainerExtPoint(securityToken.getContainer());
		try {
			extPoint.getContainerOAuthStore().removeToken(securityToken, consumerInfo, serviceName, tokenName);
		} catch (ContainerExtPointException e) {
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, e);
		}
	}
	
	private ContainerExtPoint getContainerExtPoint(String container) throws GadgetException {
		ContainerExtPoint extPoint = extPointManager.getExtPoint(container);
		if(extPoint == null) {
			throw new GadgetException(Code.OAUTH_STORAGE_ERROR, 
					"No container extension point could be found for the container with the name " + container + 
					".");
		}
		return extPoint;
	}
}
