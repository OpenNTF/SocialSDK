package com.ibm.sbt.opensocial.domino.oauth.clients;

import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Client;

/**
 * A {@link DominoOAuth2Client} for Google.
 *
 */
public class GoogleOAuth2Client extends DominoOAuth2Client {
	
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String GOOGLE_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
	
	/**
	 * Creates a new {@link DominoOAuth2Client} for Google.
	 * @param clientId A Google client ID.
	 * @param clientSecret A Google client secret.
	 */
	public GoogleOAuth2Client(String clientId, String clientSecret) {
		super();
		this.setClientId(clientId);
		this.setClientSecret(clientSecret);
		this.setAuthorizationUrl(GOOGLE_AUTH_URL);
		this.setTokenUrl(GOOGLE_TOKEN_URL);
		this.setAllowModuleOverride(true);
		this.setUseAuthorizationHeader(false);
		this.setUseUrlParameter(true);
		this.setGrantType(GrantType.CODE);
	}
}
