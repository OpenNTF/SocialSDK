package com.ibm.sbt.opensocial.domino.oauth;

import com.google.common.base.Objects;

/**
 * Represents an OAuth 2.0 client.
 *
 */
public class DominoOAuth2Client {
	
	/**
	 * Authentication types for OAuth 2.0 clients.
	 *
	 */
	public enum AuthenticationType { 
		BASIC("Basic"), 
		STANDARD("Standard");
		
		private String name;
		
		AuthenticationType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	};
	
	/**
	 * Grant types for OAuth 2.0 clients.
	 *
	 */
	public enum GrantType {
		CLIENTCREDENTIALS("client_credentials"),
		CODE("code");
		
		private String name;
		
		GrantType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	private String authorizationUrl;
	private AuthenticationType clientAuthenticationType;
	private boolean authorizationHeader = true;
	private boolean urlParameter = false;
	private String clientId;
	private String clientSecret;
	private GrantType grantType;
	private String tokenUrl;
	private boolean allowModuleOverride = true;
	
	/**
	 * Allows the module (gadget) to override certain information for the client.  The OpenSocial
	 * 2.x specs allow gadgets to specify things like authorization and token URLs within the gadget
	 * XML.  If allow module override is set to true the OAuth client will use the information specified
	 * in the gadget if present rather than the information within this object.
	 * @return True if OAuth 2.0 client information within a gadget should be used, false otherwise.
	 */
	public boolean isAllowModuleOverride() {
		return allowModuleOverride;
	}
	
	/**
	 * Sets the allow module override property to allow OAuth 2.0 information to be specified within a gadget.
	 * @param allowModuleOverride True if the client should use information within a gadget, false otherwise.
	 */
	public void setAllowModuleOverride(boolean allowModuleOverride) {
		this.allowModuleOverride = allowModuleOverride;
	}
	
	/**
	 * Gets the authorization URL.
	 * @return The authorization URL.
	 */
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}
	
	/**
	 * Sets the authorization URL.
	 * @param authorizationUrl The authorization URL.
	 */
	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}
	
	/**
	 * Gets the client authentication type.
	 * @return The client authentication type.
	 */
	public AuthenticationType getClientAuthenticationType() {
		return clientAuthenticationType;
	}
	
	/**
	 * Sets the client authentication type.
	 * @param clientAuthenticationType The client authentication type.
	 */
	public void setClientAuthenticationType(AuthenticationType clientAuthenticationType) {
		this.clientAuthenticationType = clientAuthenticationType;
	}
	
	/**
	 * Whether to use an authorization header when getting the bearer token.
	 * @return True if the client should use an authorization header, false otherwise.
	 */
	public boolean useAuthorizationHeader() {
		return authorizationHeader;
	}
	
	/**
	 * Tells the client to use an authorization header when getting the bearer token.
	 * @param authorizationHeader True to use an authorization header, false otherwise.
	 */
	public void setUseAuthorizationHeader(boolean authorizationHeader) {
		this.authorizationHeader = authorizationHeader;
	}
	
	/**
	 * Whether to use a URL parameter when getting the bearer token.
	 * @return True if the client should use a URL parameter, false otherwise.
	 */
	public boolean useUrlParameter() {
		return urlParameter;
	}
	
	/**
	 * Tells the client to use a URL parameter when getting the bearer token.
	 * @param urlParameter True to use a URL parameter, false otherwise.
	 */
	public void setUseUrlParameter(boolean urlParameter) {
		this.urlParameter = urlParameter;
	}
	
	/**
	 * Gets the client ID.
	 * @return The client ID.
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Sets the client ID.
	 * @param clientId The client ID.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * Gets the client secret.
	 * @return The client secret.
	 */
	public String getClientSecret() {
		return clientSecret;
	}
	
	/**
	 * Sets the client secret.
	 * @param clientSecret The client secret.
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	/**
	 * Gets the grant type.
	 * @return The grant type.
	 */
	public GrantType getGrantType() {
		return grantType;
	}
	
	/**
	 * Sets the grant type.
	 * @param grantType The grant type.
	 */
	public void setGrantType(GrantType grantType) {
		this.grantType = grantType;
	}
	
	/**
	 * Gets the token URL.
	 * @return The token URL.
	 */
	public String getTokenUrl() {
		return tokenUrl;
	}
	
	/**
	 * Sets the token URL.
	 * @param tokenUrl The token URL.
	 */
	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if(o instanceof DominoOAuth2Client) {
			DominoOAuth2Client testClient = (DominoOAuth2Client)o;
			result = !allowModuleOverride && !testClient.isAllowModuleOverride() ? true : allowModuleOverride && testClient.isAllowModuleOverride();
			result &= !authorizationHeader && !testClient.useAuthorizationHeader() ? true : authorizationHeader && testClient.useAuthorizationHeader();
			result &= authorizationUrl == null ? authorizationUrl == testClient.getAuthorizationUrl() : authorizationUrl.equals(testClient.getAuthorizationUrl());
			result &= clientAuthenticationType == null ? clientAuthenticationType == testClient.getClientAuthenticationType() : 
				clientAuthenticationType.equals(testClient.getClientAuthenticationType());
			result &= clientId == null ? clientId == testClient.getClientId() : clientId.equals(testClient.getClientId());
			result &= clientSecret == null ? clientSecret == testClient.getClientSecret() : clientSecret.equals(testClient.getClientSecret());
			result &= grantType == null ? grantType == testClient.getGrantType() : grantType.equals(testClient.getGrantType());
			result &= tokenUrl == null ? tokenUrl == testClient.getTokenUrl() : tokenUrl.equals(testClient.getTokenUrl());
			result &= !urlParameter && !testClient.useUrlParameter() ? true : urlParameter && testClient.useUrlParameter();
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(allowModuleOverride, authorizationHeader, authorizationUrl, clientAuthenticationType, 
				clientId, clientSecret, grantType, tokenUrl, urlParameter);
	}
}
