package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Collections;
import java.util.Set;

import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.OAuth2Token.Type;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Client;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2PersistenceException;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Persister;

import com.google.caja.util.Sets;


/**
 * Persistence layer for OpenSocial OAuth2 information.
 * This implementation doesn't do anything, it is just here to satisfy Shindig.
 */
public class DominoOAuth2Persister implements OAuth2Persister {

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
		return Sets.newHashSet();
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
