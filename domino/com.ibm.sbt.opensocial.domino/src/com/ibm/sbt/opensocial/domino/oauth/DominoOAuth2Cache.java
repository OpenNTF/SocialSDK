package com.ibm.sbt.opensocial.domino.oauth;

import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2CallbackState;
import org.apache.shindig.gadgets.oauth2.OAuth2Token.Type;
import org.apache.shindig.gadgets.oauth2.persistence.sample.InMemoryCache;

//TODO Should we consider not using an in memory cache?
/**
 * Caching layer for OAuth 2 information.
 */
public class DominoOAuth2Cache extends InMemoryCache {
	 @Override
	  protected String getClientKey(String gadgetUri, String serviceName) {
	    //By default the key consists of the gadget URI and the service name
	    return serviceName;
	  }

	  @Override
	  protected String getTokenKey(String gadgetUri, String serviceName, String user, String scope,
	          Type type) {
	    if(serviceName == null || user == null) {
	      return null;
	    }
	    String s = scope == null ? "" : scope;
	    String t = type.name();
	    return serviceName + ":" + user + ":" + s + ":" + t;
	  }

	  //TODO Remove this once we get the proper visibility in MapCache
	  @Override
	  protected String getAccessorKey(OAuth2CallbackState state) {
	    return this.getAccessorKey(state.getGadgetUri(), state.getServiceName(), state.getUser(),
	            state.getScope());
	  }

	  //TODO Remove this once we get the proper visibility in MapCache
	  @Override
	  protected String getAccessorKey(OAuth2Accessor accessor) {
	    return this.getAccessorKey(accessor.getGadgetUri(), accessor.getServiceName(),
	            accessor.getUser(), accessor.getScope());
	  }
	  
	  protected String getAccessorKey(final String gadgetUri, final String serviceName,
	          final String user, final String scope) {
	    if (serviceName == null || user == null) {
	      return null;
	    }
	    final String s = scope == null ? "" : scope;
	    return serviceName + ":" + user + ":" + s;
	  }
}
