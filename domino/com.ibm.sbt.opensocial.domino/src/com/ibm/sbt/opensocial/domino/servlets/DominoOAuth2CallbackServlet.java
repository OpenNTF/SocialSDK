package com.ibm.sbt.opensocial.domino.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.servlet.HttpUtil;
import org.apache.shindig.common.servlet.InjectedServlet;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Error;
import org.apache.shindig.gadgets.oauth2.OAuth2FetcherConfig;
import org.apache.shindig.gadgets.oauth2.OAuth2Message;
import org.apache.shindig.gadgets.oauth2.OAuth2Module;
import org.apache.shindig.gadgets.oauth2.handler.AuthorizationEndpointResponseHandler;
import org.apache.shindig.gadgets.oauth2.handler.OAuth2HandlerError;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Accessor;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2CallbackState;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2TokenStore;

/**
 * Callback servlet for 3-legged OAuth 2.0 dance.
 *
 */
public class DominoOAuth2CallbackServlet extends InjectedServlet {
	private static final long serialVersionUID = -190882288947178518L;
	private static final String CLASS = DominoOAuth2CallbackServlet.class.getName();

	private transient List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers;
	private transient DominoOAuth2TokenStore store;
	private transient Provider<OAuth2Message> oauth2MessageProvider;
	private transient BlobCrypter stateCrypter;
	private transient boolean sendTraceToClient = false;
	private Logger log;

	// This bit of magic passes the entire callback URL into the opening gadget
	// for later use.
	// gadgets.io.makeRequest (or osapi.oauth) will then pick up the callback URL
	// to complete the
	// oauth dance.
	private static final String RESP_BODY = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
			+ "\"http://www.w3.org/TR/html4/loose.dtd\">\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "<title>Close this window</title>\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "<script type='text/javascript'>\n"
			+ "try {\n"
			+ "  window.opener.gadgets.io.oauthReceivedCallbackUrl_ = document.location.href;\n"
			+ "} catch (e) {\n"
			+ "}\n"
			+ "window.close();\n"
			+ "</script>\n"
			+ "Close this window.\n" + "</body>\n" + "</html>\n";

	private static final String RESP_ERROR_BODY = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
			+ "\"http://www.w3.org/TR/html4/loose.dtd\">\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "<title>OAuth2 Error</title>\n"
			+ "</head>\n"
			+ "<body>\n"
			+ "<p>error = %s</p>"
			+ "<p>error description = %s</p>"
			+ "<p>error uri = %s</p>"
			+ "Close this window.\n"
			+ "</body>\n" + "</html>\n";

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse resp)
			throws IOException {
		final String method = "doGet";
		DominoOAuth2Accessor accessor = null;
		final OAuth2Message msg = this.oauth2MessageProvider.get();
		msg.parseRequest(request);
		if(!isOAuthMsgValid(msg, resp)) {
			return;
		}
		final DominoOAuth2CallbackState state = new DominoOAuth2CallbackState(this.stateCrypter,
				msg.getState());

		try {
			accessor = this.store.getOAuth2Accessor(state);
		} catch (GadgetException e1) {
			log.logp(Level.WARNING, CLASS, method, "Error getting accessor from store.", e1);
		}
		if(accessor == null) {
			sendError(OAuth2Error.CALLBACK_PROBLEM, "OAuth2CallbackServlet accessor is null",
					"OAuth2CallbackServlet accessor is null", "", null, resp,
					null, this.sendTraceToClient);
			return;
		}
		if(!isAccessorValid(accessor, resp)) {
			accessor.invalidate();
			try {
				this.store.removeOAuth2Accessor(accessor);
			} catch (GadgetException e) {
				log.logp(Level.WARNING, CLASS, method, "Error removing invalid accessor.", e);
			}
			return;
		}
		try {
			boolean foundHandler = false;
			for (final AuthorizationEndpointResponseHandler authorizationEndpointResponseHandler : this.authorizationEndpointResponseHandlers) {
				if (authorizationEndpointResponseHandler.handlesRequest(accessor, request)) {
					final OAuth2HandlerError handlerError = authorizationEndpointResponseHandler
							.handleRequest(accessor, request);
					if (handlerError != null) {
						sendError(handlerError.getError(),
								handlerError.getContextMessage(), handlerError.getDescription(),
								handlerError.getUri(), accessor, resp, handlerError.getCause(),
								this.sendTraceToClient);
						return;
					}
					foundHandler = true;
					break;
				}
			}

			if (!foundHandler) {
				sendError(OAuth2Error.NO_RESPONSE_HANDLER,
						"OAuth2Callback servlet couldn't find a AuthorizationEndpointResponseHandler", "",
						"", accessor, resp, null, this.sendTraceToClient);
				return;
			}

			HttpUtil.setNoCache(resp);
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().write(RESP_BODY);
		} catch (final Exception e) {
			sendError(OAuth2Error.CALLBACK_PROBLEM,
					"Exception occurred processing redirect.", "", "", accessor, resp, e,
					this.sendTraceToClient);
			throw new IOException(e);
		} finally {
			try{
				if (!accessor.isErrorResponse()) {
					accessor.invalidate();
					this.store.removeOAuth2Accessor(accessor);
				} else {
					this.store.storeOAuth2Accessor(accessor);
				}
			} catch(GadgetException e) {
				log.logp(Level.WARNING, CLASS, method, "Error storing/removing accessor.", e);
				throw new IOException(e);
			}
		}
	}
	
	/**
	 * Validates the OAuth message.
	 * @param msg The OAuth message.
	 * @param resp The response.  This method should write an error response.
	 * @return True if the OAuth message is valid false otherwise.
	 * @throws IOException Thrown when there is an error writing the error response.
	 */
	protected boolean isOAuthMsgValid(OAuth2Message msg, HttpServletResponse resp) throws IOException {
		boolean result = true;
		final OAuth2Error error = msg.getError();
		if (error != null) {
			sendError(error, "encRequestStateKey is null", msg.getErrorDescription(),
					msg.getErrorUri(), null, resp, null, this.sendTraceToClient);
			result = false;
		} 
		final String encRequestStateKey = msg.getState();
		if (encRequestStateKey == null) {
			sendError(OAuth2Error.CALLBACK_PROBLEM,
					"OAuth2CallbackServlet requestStateKey is null.", "", "", null, resp, null,
					this.sendTraceToClient);

			result = false;
		}
		return result;
	}
	
	/**
	 * Validates the OAuth accessor.
	 * @param accessor The OAuth 2.0 accessor object to validate.
	 * @param resp The response.  This method should write an error response.
	 * @return True if the OAuth accessor is valid, false otherwise.
	 * @throws IOException Thrown when there is an error writing the error response.
	 */
	protected boolean isAccessorValid(DominoOAuth2Accessor accessor, HttpServletResponse resp) throws IOException {
		
		if(!accessor.isValid()) {
			sendError(OAuth2Error.CALLBACK_PROBLEM, "OAuth2CallbackServlet accessor is invalid " + accessor,
					accessor.getErrorContextMessage(), accessor.getErrorUri(), accessor, resp,
					accessor.getErrorException(), this.sendTraceToClient);
			return false;
		}
		if(accessor.isErrorResponse()) {
			sendError(OAuth2Error.CALLBACK_PROBLEM, "OAuth2CallbackServlet accessor isErrorResponse " + accessor,
					accessor.getErrorContextMessage(), accessor.getErrorUri(), accessor, resp,
					accessor.getErrorException(), this.sendTraceToClient);
			return false;
		}
		if (!accessor.isRedirecting()) {
			// Somehow our accessor got lost. We should not proceed.
			sendError(OAuth2Error.CALLBACK_PROBLEM,
					"OAuth2CallbackServlet accessor is not valid, isn't redirecting.", "", "",
					accessor, resp, null, this.sendTraceToClient);
			return false;
		}
		return true;
	}
	
	/**
	 * Sends an error back to the client.
	 * @param error The error that occurred.
	 * @param contextMessage The message to send to the client.
	 * @param description A description of the the error.
	 * @param uri The error URI.
	 * @param accessor The OAuth 2.0 accessor.
	 * @param resp The response object.
	 * @param t A throwable.
	 * @param sendTraceToClient True to send the stack trace to the client false otherwise.
	 * @throws IOException Thrown if there is an error writing the response.
	 */
	protected void sendError(final OAuth2Error error, final String contextMessage,
			final String description, final String uri, final OAuth2Accessor accessor,
			final HttpServletResponse resp, final Throwable t, final boolean sendTraceToClient)
					throws IOException {
		final String method = "sendError";
		log.logp(Level.WARNING, CLASS, method, CLASS + " , callback error "
				+ error + " -  " + contextMessage + " , " + description + " - " + uri);

		if (t != null) {
			if (log.isLoggable(Level.FINEST)) {
				log.logp(Level.FINE, CLASS, method, "callback exception", t);
			}
		}

		HttpUtil.setNoCache(resp);
		resp.setContentType("text/html; charset=UTF-8");

		if (accessor != null) {
			accessor.setErrorResponse(t, error, contextMessage + " , " + description, uri);
		} else {
			// We don't have an accessor to report the error back to the client in the
			// normal manner.
			// Anything is better than nothing, hack something together....
			final String errorResponse;
			if (sendTraceToClient) {
				errorResponse = String.format(RESP_ERROR_BODY, error.getErrorCode(),
						error.getErrorDescription(description), uri);
			} else {
				errorResponse = String.format(RESP_ERROR_BODY, error.getErrorCode(),
						"", "");
			}
			resp.getWriter().write(errorResponse);
			return;
		}
		resp.getWriter().write(RESP_BODY);
	}

	/**
	 * Sets the authorization response handlers.
	 * @param authorizationEndpointResponseHandlers The authorization response handlers.
	 */
	@Inject
	public void setAuthorizationResponseHandlers(
			final List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers) {
		this.authorizationEndpointResponseHandlers = authorizationEndpointResponseHandlers;
	}

	/**
	 * Sets the logger.
	 * @param log The logger.
	 */
	@Inject
	public void setLogger(Logger log) {
		this.log = log;
	}
	
	/**
	 * Indicates if trace information should be sent to the client in the case of an error.
	 * @param sendTraceToClient True to send trace information to the client, false otherwise.
	 */
	@Inject
	public void sendTraceToClient(@Named(OAuth2Module.SEND_TRACE_TO_CLIENT)
	final boolean sendTraceToClient) {
		this.sendTraceToClient = sendTraceToClient;
	}


	/**
	 * Sets the OAuth 2.0 store.
	 * @param store The OAuth 2.0 store.
	 */
	@Inject
	public void setOAuth2Store(final DominoOAuth2TokenStore store) {
		this.store = store;
	}

	/**
	 * Sets the OAuth 2.0 message provider.
	 * @param oauth2MessageProvider The OAuth 2.0 message provider.
	 */
	@Inject
	public void setOAuth2MessageProvider(final Provider<OAuth2Message> oauth2MessageProvider) {
		this.oauth2MessageProvider = oauth2MessageProvider;
	}

	/**
	 * Sets the OAuth 2.0 state crypter.  Used to decrypt the OAuth 2 state.
	 * @param stateCrypter The OAuth 2.0 state crypter.
	 */
	@Inject
	public void setStateCrypter(@Named(OAuth2FetcherConfig.OAUTH2_STATE_CRYPTER)
	final BlobCrypter stateCrypter) {
		this.stateCrypter = stateCrypter;
	}
}
