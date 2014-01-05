package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Error;
import org.apache.shindig.gadgets.oauth2.OAuth2Message;
import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.handler.OAuth2HandlerError;
import org.apache.shindig.gadgets.oauth2.handler.TokenEndpointResponseHandler;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DominoTokenAuthorizationResponseHandler implements TokenEndpointResponseHandler {
	final static String CLASS = DominoTokenAuthorizationResponseHandler.class.getName();
	private DominoOAuth2TokenStore store;
	private Provider<OAuth2Message> oauth2MessageProvider;
	private final Logger log;

	@Inject
	public DominoTokenAuthorizationResponseHandler(Provider<OAuth2Message> oauth2MessageProvider, 
			DominoOAuth2TokenStore store, Logger log) {
		this.oauth2MessageProvider = oauth2MessageProvider;
		this.store = store;
		this.log = log;
	}

	@Override
	public OAuth2HandlerError handleResponse(final OAuth2Accessor oa2Accessor,
			final HttpResponse response) {
		final String method = "handleResponse";
		log.entering(CLASS, method, oa2Accessor);
		OAuth2HandlerError ret = null;

		try {
			if (response == null) {
				ret = getError("response is null");
			}

			if (ret == null && !handlesResponse(oa2Accessor, response)) {
				ret = getError("accessor is invalid " + oa2Accessor);
			}

			DominoOAuth2Accessor accessor = (DominoOAuth2Accessor)oa2Accessor;

			if (ret == null) {
				final int responseCode = response.getHttpStatusCode();
				if (responseCode != HttpResponse.SC_OK) {
					ret = getError("can't handle error response code "
							+ responseCode);
				}

				if (ret == null) {
					final long issuedAt = System.currentTimeMillis();

					final String contentType = response.getHeader("Content-Type");
					final String responseString = response.getResponseAsString();
					final OAuth2Message msg = this.oauth2MessageProvider.get();

					if(log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "Content-Type {0}", contentType);
						log.logp(Level.FINEST, CLASS, method, "Response String {0}", response);
					}

					if (contentType.startsWith("application/json")) {
						// Google does this
						msg.parseJSON(responseString);
					} else {
						// Facebook does this
						msg.parseQuery('?' + responseString);  
					}

					final OAuth2Error error = msg.getError();
					if (error != null) {
						ret = getError("error parsing request", null, msg.getErrorUri(),
								msg.getErrorDescription());
					} else if (error == null) {
						final String accessToken = msg.getAccessToken();
						final String refreshToken = msg.getRefreshToken();
						final String expiresIn = msg.getExpiresIn();
						final String tokenType = msg.getTokenType();
						final String providerName = accessor.getServiceName();
						final String gadgetUri = accessor.getGadgetUri();
						final String scope = accessor.getScope();
						final String user = accessor.getUser();
						final String macAlgorithm = msg.getMacAlgorithm();
						final String macSecret = msg.getMacSecret();
						final Map<String, String> unparsedProperties = msg.getUnparsedProperties();

						if (accessToken != null) {
							final OAuth2Token storedAccessToken = this.store.createToken();
							storedAccessToken.setIssuedAt(issuedAt);
							if (expiresIn != null) {
								storedAccessToken.setExpiresAt(issuedAt + Long.decode(expiresIn) * 1000);
							} else {
								storedAccessToken.setExpiresAt(0);
							}
							storedAccessToken.setGadgetUri(gadgetUri);
							storedAccessToken.setServiceName(providerName);
							storedAccessToken.setScope(scope);
							storedAccessToken.setSecret(accessToken.getBytes("UTF-8"));
							storedAccessToken.setTokenType(tokenType);
							storedAccessToken.setType(OAuth2Token.Type.ACCESS);
							storedAccessToken.setUser(user);
							if (macAlgorithm != null) {
								storedAccessToken.setMacAlgorithm(macAlgorithm);
							}
							if (macSecret != null) {
								storedAccessToken.setMacSecret(macSecret.getBytes("UTF-8"));
							}
							storedAccessToken.setProperties(unparsedProperties);
							this.store.storeAccessToken(accessor.getContainer(), storedAccessToken);
							accessor.setAccessToken(storedAccessToken);
						}

						if (refreshToken != null) {
							final OAuth2Token storedRefreshToken = this.store.createToken();
							storedRefreshToken.setExpiresAt(0);
							storedRefreshToken.setGadgetUri(gadgetUri);
							storedRefreshToken.setServiceName(providerName);
							storedRefreshToken.setScope(scope);
							storedRefreshToken.setSecret(refreshToken.getBytes("UTF-8"));
							storedRefreshToken.setTokenType(tokenType);
							storedRefreshToken.setType(OAuth2Token.Type.REFRESH);
							storedRefreshToken.setUser(user);
							this.store.storeRefreshToken(accessor.getContainer(), storedRefreshToken);
							accessor.setRefreshToken(storedRefreshToken);
						}
					}
				}
			}
		} catch (final Exception e) {
			log.logp(Level.WARNING, CLASS, method,
					"exception thrown handling authorization response", e);
			return getError("exception thrown handling authorization response", e, "", "");
		}
		log.exiting(CLASS,"handleResponse", ret);

		return ret;
	}

	@Override
	public boolean handlesResponse(final OAuth2Accessor accessor, final HttpResponse response) {
		if (accessor == null || !accessor.isValid() || accessor.isErrorResponse() || 
				!(accessor instanceof DominoOAuth2Accessor)) {
			return false;
		}

		return response != null;
	}

	private OAuth2HandlerError getError(final String contextMessage) {
		return getError(contextMessage, null, "", "");
	}

	private OAuth2HandlerError getError(final String contextMessage, final Exception e,
			final String uri, final String description) {
		return new OAuth2HandlerError(OAuth2Error.TOKEN_RESPONSE_PROBLEM, contextMessage, e, uri,
				description);
	}

}
