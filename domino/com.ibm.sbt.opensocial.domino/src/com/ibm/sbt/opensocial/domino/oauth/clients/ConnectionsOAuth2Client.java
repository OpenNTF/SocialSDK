package com.ibm.sbt.opensocial.domino.oauth.clients;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Client;

/**
 * A {@link DominoOAuth2Client} for Connections.
 *
 */
public class ConnectionsOAuth2Client extends DominoOAuth2Client {

	/**
	 * Creates a new DominoOAuth2Client for Connections.
	 * @param authorizationUrl The Connections OAuth 2.0 authorization URL.
	 * @param tokenUrl The Connections OAuth 2.0 token URL.
	 * @param clientId A Connections OAuth 2.0 client ID.
	 * @param clientSecret A Connections OAuth 2.0 client secret.
	 */
	public ConnectionsOAuth2Client(String authorizationUrl, String tokenUrl, String clientId, String clientSecret) {
		super();
		this.setAuthorizationUrl(authorizationUrl);
		this.setTokenUrl(tokenUrl);
		this.setClientId(clientId);
		this.setClientSecret(clientSecret);
		this.setAllowModuleOverride(true);
		this.setUseAuthorizationHeader(false);
		this.setUseUrlParameter(true);
		this.setGrantType(GrantType.CODE);
	}
}
