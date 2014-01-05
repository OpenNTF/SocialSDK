package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Map;

import net.oauth.OAuth;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.GadgetException.Code;
import org.apache.shindig.gadgets.oauth.BasicOAuthStore;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreTokenIndex;

import com.google.caja.util.Maps;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointException;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthClient.KeyType;

/**
 * OAuth 1.0a store for OpenSocial gadgets on Domino.
 *
 */
//TODO Right now every container is forced to let every gadget share OAuth tokens.  Some containers may not want that.  We should allow
//containers to specify that in their configuration and handle that here.
public class InternalDominoOAuthStore extends BasicOAuthStore {

	private ContainerExtPointManager extPointManager;
	private Authority authority;
	private String defaultCallbackUrl;
	private final Map<BasicOAuthStoreTokenIndex, TokenInfo> tokens;
	
	/**
	 * Token store for the OpenSocial implementation.  This store just delegates to 
	 * the token stores for the individual containers.
	 */
	public InternalDominoOAuthStore(ContainerExtPointManager extPointManager, Authority authority,
			String defaultCallbackUrl) {
		super();
		this.extPointManager = extPointManager;
		this.authority = authority;
		this.defaultCallbackUrl = defaultCallbackUrl;
		tokens = Maps.newHashMap();
	}

	@Override
	public ConsumerInfo getConsumerKeyAndSecret(SecurityToken securityToken,
			String serviceName, OAuthServiceProvider provider)
			throws GadgetException {
		if(securityToken.isAnonymous() || "@anonymous".equals(securityToken.getViewerId())) {
			throw new GadgetException(Code.INVALID_SECURITY_TOKEN, "Anonymous users cannot use OAuth in gadgets");
		}
		ContainerExtPoint extPoint = getContainerExtPoint(securityToken.getContainer());
		DominoOAuthStore store = null;
		try {
			store = extPoint.getContainerOAuthStore();
		} catch (ContainerExtPointException e) {
			throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR,
					"Exception thrown when getting the DominoOAuthStore for the container " + securityToken.getContainer());
		}
		if(store == null) {
			throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR,
					"No DominoOAuthStore provided for the container " + securityToken.getContainer());
		}
		DominoOAuthClient client = store.getClient(securityToken.getViewerId(), securityToken.getContainer(), 
				serviceName, securityToken.getAppUrl());
		if(client == null) {
			throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR,
					"The Domino OAuth Store for container " + securityToken.getContainer() + " did not return client information for viewer: " + 
			securityToken.getViewerId() + ", service: " + serviceName + " gadget: " + securityToken.getAppUrl());
		}
	    OAuthConsumer consumer;
	    final KeyType keyType = client.getKeyType();
	    if (keyType == KeyType.RSA_PRIVATE) {
	      consumer = new OAuthConsumer(null, client.getConsumerKey(), null, provider);
	      // The oauth.net java code has lots of magic.  By setting this property here, code thousands
	      // of lines away knows that the consumerSecret value in the consumer should be treated as
	      // an RSA private key and not an HMAC key.
	      consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
	      consumer.setProperty(RSA_SHA1.PRIVATE_KEY, client.getConsumerSecret());
	    } else if  (keyType == KeyType.PLAINTEXT) {
	      consumer = new OAuthConsumer(null, client.getConsumerKey(), client.getConsumerSecret(), provider);
	      consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, "PLAINTEXT");
	    } else {
	      consumer = new OAuthConsumer(null, client.getConsumerKey(), client.getConsumerSecret(), provider);
	      consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
	    }
	    String callback = createCallback(client.isForceCallbackOverHttps());

	    return new ConsumerInfo(consumer, null, callback, false);
	}
	
	private String createCallback(boolean forceHttps) {
		String callback = defaultCallbackUrl;

	    if (authority != null) {
	    	callback = callback.replace("%authority%", authority.getAuthority());
	    }
	    if(callback.contains("http")) {
	    	callback = callback.replace(":80", "");
	    }
	    if(callback.contains("https")) {
	    	callback = callback.replace(":443", "");
	    }
	    if(forceHttps) {
	    	if(!callback.contains("https")) {
	    		callback = callback.replace("http", "https");
	    	}
	    }
	    return callback;
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

	private BasicOAuthStoreTokenIndex makeBasicOAuthStoreTokenIndex(
			SecurityToken securityToken, String serviceName, String tokenName) {
		BasicOAuthStoreTokenIndex tokenKey = new BasicOAuthStoreTokenIndex();
		tokenKey.setGadgetUri("");
		tokenKey.setModuleId(securityToken.getModuleId());
		tokenKey.setServiceName(serviceName);
		tokenKey.setTokenName(tokenName);
		tokenKey.setUserId(securityToken.getViewerId());
		return tokenKey;
	}

	@Override
	public TokenInfo getTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo,
			String serviceName, String tokenName) {
		BasicOAuthStoreTokenIndex tokenKey =
				makeBasicOAuthStoreTokenIndex(securityToken, serviceName, tokenName);
		return tokens.get(tokenKey);
	}
	
	@Override
	public void setTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo,
			String serviceName, String tokenName, TokenInfo tokenInfo) {
		BasicOAuthStoreTokenIndex tokenKey =
				makeBasicOAuthStoreTokenIndex(securityToken, serviceName, tokenName);
		tokens.put(tokenKey, tokenInfo);
	}

	@Override
	public void removeToken(SecurityToken securityToken, ConsumerInfo consumerInfo,
			String serviceName, String tokenName) {
		BasicOAuthStoreTokenIndex tokenKey =
				makeBasicOAuthStoreTokenIndex(securityToken, serviceName, tokenName);
		tokens.remove(tokenKey);
	}	
}
