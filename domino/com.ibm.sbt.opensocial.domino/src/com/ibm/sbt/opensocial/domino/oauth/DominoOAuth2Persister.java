package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.OAuth2Token.Type;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Client;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Encrypter;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2PersistenceException;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Persister;

import com.google.caja.util.Lists;
import com.google.caja.util.Sets;
import com.google.inject.Inject;
import com.ibm.sbt.services.endpoints.OAuth2Endpoint;
import com.ibm.xsp.model.domino.DominoUtils;

/*
 * TODO The current implementation is a work in progresss.
 * We need to understand a few things better about the SDK to make this stable.
 * l.  How so we access endpoint information for a given environment?
 * 2.  There are serveral properties about and OAuth2 client the Shindig expects but are not 
 * part of the endpoints.  One thought would be to add these to GadgetOAuth2Endpoint and GadgetOAuthEndpoint.
 * However some of this information will not be needed when the application using the SDK is not a container.
 * 3.  We need a way to get the callback URL for the application in a consistent way.
 * 4.  This persistence layer should most likely point to an instance of the CredentialStore interface in the
 * SDK.
 * 5.  Can we share this implementation between the J2EE playground and the XPage playground?
 */

/**
 * Persistence layer for OpenSocial OAuth2 information.
 *
 */
public class DominoOAuth2Persister implements OAuth2Persister {
	
	private OAuth2Encrypter encrypter;
	private Authority authority;
	private String globalRedirectUri;
	
	@Inject
	public DominoOAuth2Persister(final OAuth2Encrypter encrypter, final Authority authority,
          final String globalRedirectUri) {
		this.encrypter = encrypter;
		this.authority = authority;
		this.globalRedirectUri = globalRedirectUri;
	}

	public OAuth2Client findClient(String gadgetUri, String serviceName)
	          throws OAuth2PersistenceException {
	    return null;
	  }

	public OAuth2Token findToken(String gadgetUri, String serviceName, String user, String scope,
	          Type type) throws OAuth2PersistenceException {
	    return null;
	  }
	
	public void insertToken(OAuth2Token token) throws OAuth2PersistenceException {
	}

	public Set<OAuth2Client> loadClients() throws OAuth2PersistenceException {
		//TODO Temporary solution until we can load the endpoints from the application
		Set<OAuth2Client> clients = Sets.newHashSet();
		for(OAuth2Endpoint e : getEndpoints()) {
			clients.add(createClient(e));
		}
		
		return clients;
	}
	
	private OAuth2Client createClient(OAuth2Endpoint endpoint) throws OAuth2PersistenceException {
		OAuth2Client client = new OAuth2Client(this.encrypter);
		client.setAuthorizationUrl(endpoint.getAuthorizationURL());
		client.setTokenUrl(endpoint.getAccessTokenURL());
		//TODO The next three properties are things Shindig requires but are not properties
		//of the Endpoint.  Should we just leave these standard values?
		client.setClientAuthenticationType("STANDARD");
		client.setAuthorizationHeader(false);
		client.setUrlParameter(true);
		//TODO giant hack!!  Can we generate this in a more reliable way?  Can we inject the context root?
		//Can we inject the entire callback URL?
		String redirectUri = DominoUtils.getEnvironmentString("playground-os-oauth2-callback");
		client.setRedirectUri(redirectUri);
		try {
			client.setEncryptedSecret(endpoint.getConsumerSecret().getBytes("UTF-8"));
		} catch (Exception e) {
			throw new OAuth2PersistenceException(e);
		} 
		client.setClientId(endpoint.getConsumerKey());
		
		//TODO More properties that Shindig requires that are not in the Endpoint, what
		//do we want to do with these?
		client.setGrantType("code");
		client.setType(OAuth2Accessor.Type.CONFIDENTIAL);
		
		//We are not keying off the gadget URI when storing tokens so no need to set it
		client.setGadgetUri(null);
		client.setServiceName(endpoint.getServiceName());
		
		//TODO Another property required by Shindig but in in the Endpoint
		client.setAllowModuleOverride(true);
	
		return client;
	}
	
	private List<OAuth2Endpoint> getEndpoints() {
		List<OAuth2Endpoint> endpoints = Lists.newArrayList();
		endpoints.add(createGoogleClient());
		endpoints.add(createConnectionsClient());
		return endpoints;
	}
	
	private OAuth2Endpoint createGoogleClient() {
		OAuth2Endpoint endpoint = new OAuth2Endpoint();
		endpoint.setAuthorizationURL("https://accounts.google.com/o/oauth2/auth");
		endpoint.setAccessTokenURL("https://accounts.google.com/o/oauth2/token");
		endpoint.setConsumerKey(DominoUtils.getEnvironmentString("playground_os_google_key"));
		endpoint.setConsumerSecret(DominoUtils.getEnvironmentString("playground_os_google_secret"));
		endpoint.setServiceName("googleAPI");
		return endpoint;
	}
	
	private OAuth2Endpoint createConnectionsClient() {
		OAuth2Endpoint endpoint = new OAuth2Endpoint();
		endpoint.setAccessTokenURL(DominoUtils.getEnvironmentString("playground_os_conx_host_name") + "/oauth2/endpoint/connectionsProvider/token");
		endpoint.setAuthorizationURL(DominoUtils.getEnvironmentString("playground_os_conx_host_name") + "/oauth2/endpoint/connectionsProvider/authorize");
		endpoint.setConsumerKey(DominoUtils.getEnvironmentString("playground_os_conx_key"));
		endpoint.setConsumerSecret(DominoUtils.getEnvironmentString("playground_os_conx_secret"));
		endpoint.setServiceName("connections_service");
		return endpoint;
	}

	public Set<OAuth2Token> loadTokens() throws OAuth2PersistenceException {
		return Collections.emptySet();
	}

	public boolean removeToken(String gadgetUri, String serviceName, String user, String scope,
	          Type type) throws OAuth2PersistenceException {
	    return false;
	  }

	public void updateToken(OAuth2Token arg0) throws OAuth2PersistenceException {
	}
}
