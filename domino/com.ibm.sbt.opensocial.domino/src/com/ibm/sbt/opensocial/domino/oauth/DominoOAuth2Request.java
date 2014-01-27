package com.ibm.sbt.opensocial.domino.oauth;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shindig.auth.AnonymousSecurityToken;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.http.HttpResponseBuilder;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Arguments;
import org.apache.shindig.gadgets.oauth2.OAuth2Error;
import org.apache.shindig.gadgets.oauth2.OAuth2Message;
import org.apache.shindig.gadgets.oauth2.OAuth2Request;
import org.apache.shindig.gadgets.oauth2.OAuth2RequestException;
import org.apache.shindig.gadgets.oauth2.OAuth2RequestParameterGenerator;
import org.apache.shindig.gadgets.oauth2.OAuth2ResponseParams;
import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.OAuth2Utils;
import org.apache.shindig.gadgets.oauth2.handler.AuthorizationEndpointResponseHandler;
import org.apache.shindig.gadgets.oauth2.handler.ClientAuthenticationHandler;
import org.apache.shindig.gadgets.oauth2.handler.GrantRequestHandler;
import org.apache.shindig.gadgets.oauth2.handler.OAuth2HandlerError;
import org.apache.shindig.gadgets.oauth2.handler.ResourceRequestHandler;
import org.apache.shindig.gadgets.oauth2.handler.TokenEndpointResponseHandler;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DominoOAuth2Request implements OAuth2Request {
	private static final String CLASS = DominoOAuth2Request.class.getName();

	private static final short MAX_ATTEMPTS = 3;

	private DominoOAuth2Accessor internalAccessor;

	private OAuth2Arguments arguments;

	private final List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers;

	private final List<ClientAuthenticationHandler> clientAuthenticationHandlers;

	private final HttpFetcher fetcher;

	private final List<GrantRequestHandler> grantRequestHandlers;

	private HttpRequest realRequest;

	private final List<ResourceRequestHandler> resourceRequestHandlers;

	private OAuth2ResponseParams responseParams;

	private SecurityToken securityToken;

	private final DominoOAuth2TokenStore tokenStore;

	private final List<TokenEndpointResponseHandler> tokenEndpointResponseHandlers;

	private final boolean sendTraceToClient;

	private final OAuth2RequestParameterGenerator requestParameterGenerator;

	private short attemptCounter = 0;

	private final boolean viewerAccessTokensEnabled;

	private final Logger log;

	/**
	 * @param fetcherConfig
	 *          configuration options for the fetcher
	 * @param fetcher
	 *          fetcher to use for actually making requests
	 */
	@Inject
	public DominoOAuth2Request(final DominoOAuth2TokenStore tokenStore, final HttpFetcher fetcher,
			final List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers,
			final List<ClientAuthenticationHandler> clientAuthenticationHandlers,
			final List<GrantRequestHandler> grantRequestHandlers,
			final List<ResourceRequestHandler> resourceRequestHandlers,
			final List<TokenEndpointResponseHandler> tokenEndpointResponseHandlers,
			final boolean sendTraceToClient,
			final OAuth2RequestParameterGenerator requestParameterGenerator,
			@Named("shindig.oauth2.viewer-access-tokens-enabled") final boolean viewerAccessTokensEnabled,
			Logger log) {
		this.viewerAccessTokensEnabled = viewerAccessTokensEnabled;
		this.log = log;
		this.tokenStore = tokenStore;
		this.fetcher = fetcher;
		this.authorizationEndpointResponseHandlers = authorizationEndpointResponseHandlers;
		this.clientAuthenticationHandlers = clientAuthenticationHandlers;
		this.grantRequestHandlers = grantRequestHandlers;
		this.resourceRequestHandlers = resourceRequestHandlers;
		this.tokenEndpointResponseHandlers = tokenEndpointResponseHandlers;
		this.sendTraceToClient = sendTraceToClient;
		this.requestParameterGenerator = requestParameterGenerator;
	}

	public HttpResponse fetch(final HttpRequest request) {
		final String method = "fetch";
		log.entering(DominoOAuth2Request.CLASS, method, request);

		DominoOAuth2Accessor accessor = null;

		HttpResponse response = null;
		this.responseParams = new OAuth2ResponseParams();

		try {
			// First step is to get an OAuth2Accessor for this request
			if (request == null || request.getSecurityToken() == null) {
				// Any errors before we have an accessor are special cases
				response = this.sendErrorResponse(null, OAuth2Error.MISSING_FETCH_PARAMS,
						"no request or security token");
			} else {
				this.realRequest = request;
				this.securityToken = request.getSecurityToken();
				if(securityToken.isAnonymous() || "@anonymous".equals(securityToken.getViewerId())) {
					return this.sendErrorResponse(null, OAuth2Error.GET_OAUTH2_ACCESSOR_PROBLEM, "Anonymous users cannot use OAuth 2 within gadgets.");
				}
				
				this.arguments = this.realRequest.getOAuth2Arguments();

				if (this.arguments == null) {
					// Any errors before we have an accessor are special cases
					return this.sendErrorResponse(null, OAuth2Error.FETCH_INIT_PROBLEM,
							"no responseParams or arguments");
				}

				accessor = this.getAccessor();

				if (accessor == null) {
					// Any errors before we have an accessor are special cases
					response = this.sendErrorResponse(null, OAuth2Error.FETCH_INIT_PROBLEM,
							"accessor is null");
				} else {
					accessor.setRedirecting(false);

					final Map<String, String> requestParams = this.requestParameterGenerator
							.generateParams(this.realRequest);
					accessor.setAdditionalRequestParams(requestParams);

					HttpResponseBuilder responseBuilder = null;
					if (!accessor.isErrorResponse()) {
						responseBuilder = this.attemptFetch(accessor);
					}

					response = this.processResponse(accessor, responseBuilder);
				}
			}
		} catch (final Throwable t) {
			log.logp(Level.WARNING, CLASS, method, "exception occurred during fetch", t);
			if (accessor == null) {
				accessor = new BasicDominoOAuth2Accessor();
				accessor.setErrorResponse(t, OAuth2Error.FETCH_PROBLEM, "exception occurred during fetch", "");
			} else {
				accessor.setErrorResponse(t, OAuth2Error.FETCH_PROBLEM, "exception occurred during fetch",
						"");
			}
			response = this.processResponse(accessor, this.getErrorResponseBuilder(t,
					OAuth2Error.FETCH_PROBLEM, "exception occurred during fetch"));
		} finally {
			if (accessor != null) {
				if (!accessor.isRedirecting()) {
					if (log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "accessor is not redirecting, remove it", accessor);
					}
					accessor.invalidate();
					try {
						this.tokenStore.removeOAuth2Accessor(accessor);
					} catch (GadgetException e) {
						log.logp(Level.WARNING, CLASS, method, "Error removing OAuth2Accessor from store.", e);
					}
					this.internalAccessor = null;
				} else {
					if (!accessor.isValid()) {
						if (log.isLoggable(Level.FINEST)) {
							log.logp(Level.FINEST, CLASS, method, "accesssor is not valid", accessor);
						}
					} else if (accessor.isErrorResponse()) {
						if (log.isLoggable(Level.FINEST)) {
							log.logp(Level.FINEST, CLASS, method, "accessor isErrorResponse",
									accessor.getErrorContextMessage());
						}
					}
					try {
						this.tokenStore.storeOAuth2Accessor(accessor);
					} catch (GadgetException e) {
						log.logp(Level.WARNING, CLASS, method, "Error removing OAuth2 accessor from store.");
					}
				}
			}
		}

		log.exiting(CLASS, method, response);
		return response;
	}

	private HttpResponseBuilder attemptFetch(final DominoOAuth2Accessor accessor) {
		final String method = "attemptFetch";
		log.entering(CLASS, method, accessor);
		if (this.attemptCounter > DominoOAuth2Request.MAX_ATTEMPTS) {
			if (log.isLoggable(Level.FINEST)) {
				log.logp(Level.FINEST, CLASS, method, "MAX_ATTEMPTS exceeded {0}", this.attemptCounter);
				// This can be useful to diagnose the recursion
				final StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
				String stack = "";
				for (final StackTraceElement element : stackElements) {
					stack = stack + element.toString() + "\n";
				}
				log.logp(Level.FINEST, CLASS, method, "MAX_ATTEMPTS stack = {0}", stack);
			}
			return this.fetchData(accessor, true);
		}

		this.attemptCounter++;

		if (log.isLoggable(Level.FINEST)) {
			log.logp(Level.FINEST, CLASS, method, "attempt number {0}", this.attemptCounter);
		}

		HttpResponseBuilder ret = null;

		if (accessor.isErrorResponse()) {
			// If there's an error in the accessor don't continue.
			return this.getErrorResponseBuilder(accessor.getErrorException(), accessor.getError(),
					accessor.getErrorContextMessage(), accessor.getErrorUri(), accessor.getErrorContextMessage());
		}
		
		if(!DominoOAuth2Request.haveAccessToken(accessor) && !DominoOAuth2Request.haveRefreshToken(accessor)) {
			if (!accessor.isRedirecting() && this.checkCanAuthorize(accessor)) {
				final String completeAuthUrl = this.authorize(accessor);
				if (completeAuthUrl != null) {
					// Send a response to redirect to the authorization url
					this.responseParams.setAuthorizationUrl(completeAuthUrl);
					accessor.setRedirecting(true);
				} else {
					// This wasn't a redirect type of authorization. try again
					ret = this.attemptFetch(accessor);
				}
			}
		}
		
		if (DominoOAuth2Request.haveAccessToken(accessor)) {
			// We have an access_token, use it and stop!
			// Don't try more than three times
			ret = this.fetchData(accessor, this.attemptCounter > DominoOAuth2Request.MAX_ATTEMPTS);
		} else if (DominoOAuth2Request.haveRefreshToken(accessor)) {
			// We don't have an access token, we need to try and get one.
			// First step see if we have a refresh token
			ret = refreshAccessToken(accessor);
		}


		if (ret == null) {
			if (accessor.isRedirecting()) {
				// Send redirect response to client
				ret = new HttpResponseBuilder().setHttpStatusCode(HttpResponse.SC_OK).setStrictNoCache();
			} else {
				accessor.setAccessToken(null);
				ret = this.attemptFetch(accessor);
			}
		}


		log.exiting(CLASS, method, ret);

		return ret;
	}
	
	
	protected HttpResponseBuilder refreshAccessToken(DominoOAuth2Accessor accessor) {
		final String method = "refreshAccessToken";
		HttpResponseBuilder ret = null;
		boolean attempt = false;
		final String internedAccessor = getAccessorKey(accessor).intern();
		// This syncrhonized block is less than ideal.
		// It is needed because if a gadget has multiple makeRequests that triggers
		// multiple refreshes they can end up clobbering each other, and cause
		// temporary failures until the gadget is refreshed.
		// Syncrhonizing on the internedAccessor helps.  It is not cluster safe
		// and could be problematic having so much code synchd.
		// TODO : https://issues.apache.org/jira/browse/SHINDIG-1871
		synchronized (internedAccessor) {
			final OAuth2Accessor acc = this.getAccessorInternal();
			if (DominoOAuth2Request.haveAccessToken(acc)) {
				// Another refresh must have won
				if (log.isLoggable(Level.FINEST)) {
					log.logp(Level.FINEST, CLASS, method, "found an access token from another refresh",
							new Object[] {});
				}
				attempt = true;
			} else {
				final OAuth2HandlerError handlerError = this.refreshToken(accessor);
				if (handlerError == null) {
					// No errors refreshing, attempt the fetch again.
					attempt = true;
					if (log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "no refresh errors reported");
					}
				} else {
					if (log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "refresh errors reported");
					}
					// There was an error refreshing, stop.
					final OAuth2Error error = handlerError.getError();
					ret = this.getErrorResponseBuilder(handlerError.getCause(), error,
							handlerError.getContextMessage(), handlerError.getUri(),
							handlerError.getDescription());
				}
			}
		}
		if (attempt) {
			if (log.isLoggable(Level.FINEST)) {
				log.logp(Level.FINEST, CLASS, method, "going to re-attempt with a clean accesor");
			}
			try {
				this.tokenStore.removeOAuth2Accessor(this.internalAccessor);
			} catch (GadgetException e) {
				log.logp(Level.WARNING, CLASS, method, "Error while removing accessor.", e);
			}
			this.internalAccessor = null;
			ret = this.attemptFetch(this.getAccessor());
		}
		return ret;
	}

	private String authorize(final OAuth2Accessor accessor) {
		final String method = "authorize";
		log.entering(CLASS, method, accessor);

		String ret = null;

		final String grantType = accessor.getGrantType();

		GrantRequestHandler grantRequestHandlerUsed = null;
		for (final GrantRequestHandler grantRequestHandler : this.grantRequestHandlers) {
			if (grantRequestHandler.getGrantType().equalsIgnoreCase(grantType)) {
				grantRequestHandlerUsed = grantRequestHandler;
				break;
			}
		}

		if (grantRequestHandlerUsed == null) {
			accessor.setErrorResponse(null, OAuth2Error.AUTHENTICATION_PROBLEM,
					"no grantRequestHandler found for " + grantType, "");
		} else {
			String completeAuthUrl = null;
			try {
				completeAuthUrl = grantRequestHandlerUsed.getCompleteUrl(accessor);
			} catch (final OAuth2RequestException e) {
				log.logp(Level.WARNING, CLASS, method, "error getting complete url", e);
			}
			if (grantRequestHandlerUsed.isRedirectRequired()) {
				ret = completeAuthUrl;
			} else {
				final OAuth2HandlerError error = this.authorize(accessor, grantRequestHandlerUsed,
						completeAuthUrl);
				if (error != null) {
					accessor.setErrorResponse(error.getCause(), OAuth2Error.AUTHENTICATION_PROBLEM,
							error.getContextMessage() + " , " + error.getDescription(), error.getUri());
				}
			}
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private OAuth2HandlerError authorize(final OAuth2Accessor accessor,
			final GrantRequestHandler grantRequestHandler, final String completeAuthUrl) {
		final String method = "authorize";
		log.entering(CLASS, method, new Object[] {
					accessor, grantRequestHandler, completeAuthUrl });
			
		OAuth2HandlerError ret = null;

		HttpRequest authorizationRequest;
		try {
			authorizationRequest = grantRequestHandler.getAuthorizationRequest(accessor, completeAuthUrl);
		} catch (final OAuth2RequestException e) {
			authorizationRequest = null;
			ret = new OAuth2HandlerError(e.getError(), e.getErrorText(), e);
		}

		if (authorizationRequest != null) {
			HttpResponse authorizationResponse;
			try {
				authorizationResponse = this.fetcher.fetch(authorizationRequest);
			} catch (final GadgetException e) {
				log.logp(Level.WARNING, CLASS, method, "Exception while making authorizating request", e);
				authorizationResponse = null;
				ret = new OAuth2HandlerError(OAuth2Error.AUTHORIZE_PROBLEM,
						"exception thrown fetching authorization", e);
			}

			if (authorizationResponse != null) {
				if (grantRequestHandler.isAuthorizationEndpointResponse()) {
					for (final AuthorizationEndpointResponseHandler authorizationEndpointResponseHandler : this.authorizationEndpointResponseHandlers) {
						if (authorizationEndpointResponseHandler.handlesResponse(accessor,
								authorizationResponse)) {
							ret = authorizationEndpointResponseHandler.handleResponse(accessor,
									authorizationResponse);
							if (ret != null) {
								// error occurred stop processing
								break;
							}
						}
					}
				}

				if (ret == null) {
					if (grantRequestHandler.isTokenEndpointResponse()) {
						for (final TokenEndpointResponseHandler tokenEndpointResponseHandler : this.tokenEndpointResponseHandlers) {
							if (tokenEndpointResponseHandler.handlesResponse(accessor, authorizationResponse)) {
								ret = tokenEndpointResponseHandler.handleResponse(accessor, authorizationResponse);
								if (ret != null) {
									// error occurred stop processing
									break;
								}
							}
						}
					}
				}
			}
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private String buildRefreshTokenUrl(final OAuth2Accessor accessor) {
		final String method = "buildRefreshTokenUrl";
		log.entering(CLASS, method, accessor);

		String ret = null;

		final String refreshUrl = accessor.getTokenUrl();
		if (refreshUrl != null) {
			ret = DominoOAuth2Request.getCompleteRefreshUrl(refreshUrl);
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private boolean checkCanAuthorize(final OAuth2Accessor accessor) {
		final String method = "checkCanAuthorize";
		log.entering(CLASS, method, accessor);
		boolean ret = true;
		final String pageOwner = this.securityToken.getOwnerId();
		final String pageViewer = this.securityToken.getViewerId();

		if (pageOwner == null || pageViewer == null) {
			accessor.setErrorResponse(null, OAuth2Error.AUTHORIZE_PROBLEM,
					"pageOwner or pageViewer is null", "");
			ret = false;
		} else if (!this.viewerAccessTokensEnabled && !pageOwner.equals(pageViewer)) {
			accessor.setErrorResponse(null, OAuth2Error.AUTHORIZE_PROBLEM, "pageViewer is not pageOwner",
					"");
			ret = false;
		}
		log.exiting(CLASS, method, ret);

		return ret;
	}

	private HttpResponseBuilder fetchData(final DominoOAuth2Accessor accessor, final boolean lastAttempt) {
		final String method = "fetchData";
		log.entering(CLASS, method, accessor);
		HttpResponseBuilder ret = null;
		try {
			final HttpResponse response = this.fetchFromServer(accessor, this.realRequest, lastAttempt);
			if (response != null) {
				ret = new HttpResponseBuilder(response);

				if (response.getHttpStatusCode() != HttpResponse.SC_OK && this.sendTraceToClient) {
					this.responseParams.addRequestTrace(this.realRequest, response);
				}
			}
		} catch (final OAuth2RequestException e) {
			ret = this.getErrorResponseBuilder(e, e.getError(), e.getErrorText(), e.getErrorUri(),
					e.getErrorDescription());
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private HttpResponse fetchFromServer(final DominoOAuth2Accessor accessor, final HttpRequest request,
			final boolean lastAttempt) throws OAuth2RequestException {
		final String method = "fetchFromServer";
		log.entering(CLASS, method, new Object[] { accessor, lastAttempt });
		HttpResponse ret;
		final long currentTime = System.currentTimeMillis();
		OAuth2Token accessToken = accessor.getAccessToken();
		if (accessToken != null) {
			final long expiresAt = accessToken.getExpiresAt();
			if (expiresAt != 0) {
				if (currentTime >= expiresAt) {
					if (log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "accessToken has expired at {0}", new Object[]{expiresAt});
					}
					try {
						this.tokenStore.removeAccessToken(accessor);
					} catch (final GadgetException e) {
						throw new OAuth2RequestException(OAuth2Error.MISSING_SERVER_RESPONSE,
								"error removing access_token", null);
					}
					accessToken = null;
					accessor.setAccessToken(null);
					if (!lastAttempt) {
						return null;
					}
				}
			}
		}

		OAuth2Token refreshToken = accessor.getRefreshToken();
		if (refreshToken != null) {
			final long expiresAt = refreshToken.getExpiresAt();
			if (expiresAt != 0) {
				if (currentTime >= expiresAt) {
					if (log.isLoggable(Level.FINEST)) {
						log.logp(Level.FINEST, CLASS, method, "refreshToken has expired at {0}", new Object[]{expiresAt});
					}
					try {
						this.tokenStore.removeRefreshToken(accessor);
					} catch (final GadgetException e) {
						throw new OAuth2RequestException(OAuth2Error.MISSING_SERVER_RESPONSE,
								"error removing refresh_token", null);
					}
					refreshToken = null;
					accessor.setRefreshToken(null);
					if (!lastAttempt) {
						return null;
					}
				}
			}
		}

		if (accessToken != null) {
			final boolean isAllowed = isUriAllowed(request.getUri(), accessor.getAllowedDomains());
			if (isAllowed) {
				String tokenType = accessToken.getTokenType();
				if (tokenType == null || tokenType.length() == 0) {
					tokenType = OAuth2Message.BEARER_TOKEN_TYPE;
				}

				for (final ResourceRequestHandler resourceRequestHandler : this.resourceRequestHandlers) {
					if (tokenType.equalsIgnoreCase(resourceRequestHandler.getTokenType())) {
						resourceRequestHandler.addOAuth2Params(accessor, request);
					}
				}
			} else {
				log.logp(Level.WARNING, CLASS, method,
						"Gadget {0} attempted to send OAuth2 Token to an unauthorized domain: {1}.",
						new Object[] { accessor.getGadgetUri(), request.getUri() });
				throw new OAuth2RequestException(OAuth2Error.SERVER_REJECTED_REQUEST, 
						"The accessor is not allowed to be sent to the domain of the request.", null);
			}
		}

		try {
			ret = this.fetcher.fetch(request);
		} catch (final GadgetException e) {
			throw new OAuth2RequestException(OAuth2Error.MISSING_SERVER_RESPONSE,
					"GadgetException fetchFromServer", e);
		}

		final int responseCode = ret.getHttpStatusCode();

		if (log.isLoggable(Level.FINEST)) {
			log.logp(Level.FINEST, CLASS, method, "responseCode = {0}", new Object[]{responseCode});
		}

		if (responseCode == HttpResponse.SC_UNAUTHORIZED) {
			if (accessToken != null) {
				try {
					this.tokenStore.removeAccessToken(accessor);
				} catch (final GadgetException e) {
					throw new OAuth2RequestException(OAuth2Error.MISSING_SERVER_RESPONSE,
							"error removing access_token", null);
				}
				accessor.setAccessToken(null);
			}

			if (!lastAttempt) {
				ret = null;
			}
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private DominoOAuth2Accessor getAccessorInternal() {
		DominoOAuth2Accessor ret = tokenStore.getOAuth2Accessor(this.securityToken, this.arguments,
					this.realRequest.getGadget());
		return ret;
	}

	private DominoOAuth2Accessor getAccessor() {
		if (this.internalAccessor == null || !this.internalAccessor.isValid()) {
			this.internalAccessor = this.getAccessorInternal();
		}

		return this.internalAccessor;
	}

	private static String getCompleteRefreshUrl(final String refreshUrl) {
		return OAuth2Utils.buildUrl(refreshUrl, null, null);
	}

	private HttpResponseBuilder getErrorResponseBuilder(final Throwable t, final OAuth2Error error,
			final String contextMessage) {
		return getErrorResponseBuilder(t, error, contextMessage, null, null);
	}

	private HttpResponseBuilder getErrorResponseBuilder(final Throwable t, final OAuth2Error error,
			final String contextMessage, final String errorUri, final String errorDescription) {
		final String method = "getErrorResponseBuilder";
		log.entering(CLASS, method, new Object[] { t, error, contextMessage, errorUri, errorDescription });

		final HttpResponseBuilder ret = new HttpResponseBuilder().setHttpStatusCode(
				HttpResponse.SC_FORBIDDEN).setStrictNoCache();

		if (t != null && this.sendTraceToClient) {
			final StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			final String message = sw.toString();
			this.responseParams.addDebug(message);
		}

		if (this.sendTraceToClient) {
			this.responseParams.addToResponse(ret, error.getErrorCode(),
					error.getErrorDescription(contextMessage) + " , " + errorDescription, errorUri,
					error.getErrorExplanation());
		} else {
			this.responseParams.addToResponse(ret, error.getErrorCode(), "", "",
					error.getErrorExplanation());
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private String getRefreshBody(final OAuth2Accessor accessor) {
		final String method = "getRefreshBody";
		log.entering(CLASS, method, accessor);

		String ret = "";

		Map<String, String> queryParams;
		try {
			queryParams = Maps.newHashMap();
			queryParams.put(OAuth2Message.GRANT_TYPE, OAuth2Message.REFRESH_TOKEN);
			queryParams.put(OAuth2Message.REFRESH_TOKEN, new String(accessor.getRefreshToken()
					.getSecret(), "UTF-8"));
			if (accessor.getScope() != null && accessor.getScope().length() > 0) {
				queryParams.put(OAuth2Message.SCOPE, accessor.getScope());
			}

			final String clientId = accessor.getClientId();
			final byte[] secret = accessor.getClientSecret();
			queryParams.put(OAuth2Message.CLIENT_ID, clientId);
			queryParams.put(OAuth2Message.CLIENT_SECRET, new String(secret, "UTF-8"));

			ret = OAuth2Utils.buildUrl(ret, queryParams, null);

			final char firstChar = ret.charAt(0);
			if (firstChar == '?' || firstChar == '&') {
				ret = ret.substring(1);
			}
		} catch (final UnsupportedEncodingException e) {
			log.logp(Level.WARNING, CLASS, method, "error generating refresh body", e);
				ret = null;
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private HttpResponse processResponse(final OAuth2Accessor accessor,
			final HttpResponseBuilder responseBuilder) {
		final String method = "processResponse";
		log.entering(CLASS, method,new Object[] { accessor, responseBuilder });

		if (accessor.isErrorResponse() || responseBuilder == null) {
			return this.sendErrorResponse(accessor.getErrorException(), accessor.getError(),
					accessor.getErrorContextMessage(), accessor.getErrorUri(), "");
		}

		if (this.responseParams.getAuthorizationUrl() != null) {
			responseBuilder.setMetadata(OAuth2ResponseParams.APPROVAL_URL,
					this.responseParams.getAuthorizationUrl());
			accessor.setRedirecting(true);
		} else {
			accessor.setRedirecting(false);
		}

		final HttpResponse ret = responseBuilder.create();
		log.exiting(CLASS, method);
		return ret;
	}

	private OAuth2HandlerError refreshToken(final DominoOAuth2Accessor accessor) {
		final String method = "refreshToken";
		log.entering(CLASS, method, new Object[] { accessor });
		OAuth2HandlerError ret = null;
		String refershTokenUrl;
		refershTokenUrl = buildRefreshTokenUrl(accessor);

		if (log.isLoggable(Level.FINEST)) {
			log.logp(Level.FINEST, CLASS, method, "refershTokenUrl = {0}", new Object[]{refershTokenUrl});
		}

		if (refershTokenUrl != null) {
			HttpResponse response = null;
			final HttpRequest request = new HttpRequest(Uri.parse(refershTokenUrl));
			request.setSecurityToken(new AnonymousSecurityToken("", 0L, accessor.getGadgetUri()));
			request.setMethod("POST");
			request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			for (final ClientAuthenticationHandler clientAuthenticationHandler : this.clientAuthenticationHandlers) {
				if (clientAuthenticationHandler.geClientAuthenticationType().equalsIgnoreCase(
						accessor.getClientAuthenticationType())) {
					clientAuthenticationHandler.addOAuth2Authentication(request, accessor);
				}
			}

			try {
				final byte[] body = getRefreshBody(accessor).getBytes("UTF-8");
				request.setPostBody(body);
			} catch (final Exception e) {
				log.logp(Level.WARNING, CLASS, method, "Error while getting body for refresh request.", e);
				ret = new OAuth2HandlerError(OAuth2Error.REFRESH_TOKEN_PROBLEM,
						"error generating refresh body", e);
			}

			if (!isUriAllowed(request.getUri(), accessor.getAllowedDomains())) {
				ret = new OAuth2HandlerError(OAuth2Error.REFRESH_TOKEN_PROBLEM,
						"error fetching refresh token - domain not allowed", null);
			}

			if (ret == null) {
				try {
					response = this.fetcher.fetch(request);
				} catch (final GadgetException e) {
					log.logp(Level.WARNING, CLASS, method, "Error while making refresh request.", e);
					ret = new OAuth2HandlerError(OAuth2Error.REFRESH_TOKEN_PROBLEM,
							"error fetching refresh token", e);
				}

				if (log.isLoggable(Level.FINEST)) {
					log.logp(Level.FINEST, CLASS, method, "response = {0}", new Object[] {response});
				}

				if (ret == null) {
					// response is not null..
					final int statusCode = response.getHttpStatusCode();
					if (statusCode == HttpResponse.SC_UNAUTHORIZED
							|| statusCode == HttpResponse.SC_BAD_REQUEST) {
						try {
							this.tokenStore.removeRefreshToken(accessor);
						} catch (final GadgetException e) {
							ret = new OAuth2HandlerError(OAuth2Error.REFRESH_TOKEN_PROBLEM,
									"failed to remove refresh token", e);
						}
						accessor.setRefreshToken(null);
						if (log.isLoggable(Level.FINEST)) {
							log.logp(Level.FINEST, CLASS, method,
									"received {0} from provider, removed refresh token.  response = {1}",
									new Object[] { statusCode, response.getResponseAsString() });
						}
						return ret;
					} else if (statusCode != HttpResponse.SC_OK) {
						ret = new OAuth2HandlerError(OAuth2Error.REFRESH_TOKEN_PROBLEM,
								"bad response from server : " + statusCode, null, "",
								response.getResponseAsString());
					}

					if (ret == null) {
						for (final TokenEndpointResponseHandler tokenEndpointResponseHandler : this.tokenEndpointResponseHandlers) {
							if (tokenEndpointResponseHandler.handlesResponse(accessor, response)) {
								final OAuth2HandlerError error = tokenEndpointResponseHandler.handleResponse(
										accessor, response);
								if (error != null) {
									try {
										this.tokenStore.removeRefreshToken(accessor);
									} catch (GadgetException e) {
										log.logp(Level.WARNING, CLASS, method, 
												"There was an error removing the refresh token after an error occured processing the token refresh response.", e);
									}
									accessor.setRefreshToken(null);
									return error;
								}
							}
						}
					}
				}
			}
		}
		log.exiting(CLASS, method, ret);
		return ret;
	}

	private HttpResponse sendErrorResponse(final Throwable t, final OAuth2Error error,
			final String contextMessage) {
		final HttpResponseBuilder responseBuilder = this.getErrorResponseBuilder(t, error,
				contextMessage);
		return responseBuilder.create();
	}

	private HttpResponse sendErrorResponse(final Throwable t, final OAuth2Error error,
			final String contextMessage, final String errorUri, final String errorDescription) {
		final HttpResponseBuilder responseBuilder = this.getErrorResponseBuilder(t, error,
				contextMessage, errorUri, errorDescription);
		return responseBuilder.create();
	}

	private static boolean haveAccessToken(final OAuth2Accessor accessor) {
		OAuth2Token token = accessor.getAccessToken();
		return token != null && DominoOAuth2Request.validateAccessToken(token);
	}

	private static boolean haveRefreshToken(final OAuth2Accessor accessor) {
		OAuth2Token token = accessor.getRefreshToken();
		return token != null && DominoOAuth2Request.validateRefreshToken(token);
	}

	private static boolean isUriAllowed(final Uri uri, final String[] allowedDomains) {
		if (allowedDomains == null || allowedDomains.length == 0) {
			// if white list is not specified, allow client to access any domain
			return true;
		}
		String host = uri.getAuthority();
		final int pos = host.indexOf(':');
		if (pos != -1) {
			host = host.substring(0, pos);
		}
		for (String domain : allowedDomains) {
			if (domain != null) {
				domain = domain.trim();
				if (domain.startsWith(".") && host.endsWith(domain) || domain.equals(host)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean validateAccessToken(final OAuth2Token accessToken) {
		return accessToken != null;
	}

	private static boolean validateRefreshToken(final OAuth2Token refreshToken) {
		return refreshToken != null;
	}

	private static String getAccessorKey(final OAuth2Accessor accessor) {
		if (accessor != null) {
			return "accessor:" + accessor.getGadgetUri() + ':' + accessor.getServiceName() + ':'
					+ accessor.getUser() + ':' + accessor.getScope();
		}

		return null;
	}

}
