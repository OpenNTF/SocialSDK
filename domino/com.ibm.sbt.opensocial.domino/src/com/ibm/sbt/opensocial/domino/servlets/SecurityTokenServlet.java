package com.ibm.sbt.opensocial.domino.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shindig.auth.AbstractSecurityToken;
import org.apache.shindig.auth.AbstractSecurityToken.Keys;
import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.common.servlet.InjectedServlet;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.domino.osgi.core.context.ContextInfo;
import com.ibm.fiesta.commons.IdUtil;
import com.ibm.fiesta.commons.security.SimpleSecurityToken;
import com.ibm.sbt.opensocial.domino.internal.OpenSocialPlugin;

/**
 * This servlet acts to generate security tokens for authenticated users.
 * Supports GET only. The desired security token contents should be defined as
 * query parameters.
 * 
 * Parameters in the request to this servlet should be keyed by
 * {@link AbstractSecurityToken.Keys}
 * 
 * The response, if there was no error, will be JSON of the form
 * 
 * <code>
 * {
 *  "token":[encrypted token], 
 *  "ttl":[the time to live of the token]
 * }
 * </code>
 */
public class SecurityTokenServlet extends InjectedServlet {
    private static final long serialVersionUID = 1L;
    private static final String RESP_TTL_KEY = "ttl"; //$NON-NLS-1$
	private static final String RESP_TOKEN_KEY = "token"; //$NON-NLS-1$
	private static final String METHOD = "doGet"; //$NON-NLS-1$
	private static final String ANONYMOUS = "@anonymous";
	private static final String CLASSNAME = SecurityTokenServlet.class.getName();
	private final Logger logger = OpenSocialPlugin.getLogger();
	private SecurityTokenCodec codec;

	@Inject
	public void setSecurityTokenCodec(SecurityTokenCodec codec) {
		// Call checkInitialized() as part of the contract with InjectedServlet
		checkInitialized();
		this.codec = codec;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Before we do anything let's log some information about the request
		// for debugging purposes
		logRequestInfo(req);
		
		// Just in case
		if (this.codec == null) {
			sendError(
				req, 
				resp,
				"The codec is null", //$NON-NLS-1$
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR
			);
			return;
		}

		try {
			String userId = null;
			try {
				userId = getUserId();
			} catch (Throwable t) {
				sendError(
					req, 
					resp, 
					"There was an error getting the user id.",
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					t
				);
				
				return;
			}
			SimpleSecurityToken token = generateSecurityToken(req, userId, userId);
			if (this.logger.isLoggable(Level.FINEST)) {
				// Rope this off with an isLoggable so we don't do the work of token.toString() all the time
				this.logger.finest("SecurityTokenServlet -  encoding values: " + token); //$NON-NLS-1$
			}
			String tokenString = this.codec.encodeToken(token);
			this.logger.finest("SecurityTokenServlet - encoded token: " + tokenString); //$NON-NLS-1$

			// It's just the string. No need for JSON or XML
			resp.setContentType("application/json;charset=UTF-8"); //$NON-NLS-1$

			// Keep people from using this outside of an XHR
			resp.setHeader("Content-Disposition", "attachment;filename=foo.txt"); //$NON-NLS-1$ //$NON-NLS-2$

			// The requests coming in will be identical so browsers MUST NOT
			// cached
			resp.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
			resp.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$

			String responseJsonString = buildResponseString(token, tokenString);

			// Last but not least, set the content length
			resp.setContentLength(responseJsonString.length());

			// Write the response
			PrintWriter writer = resp.getWriter();
			writer.print(responseJsonString);
			writer.flush();
		} catch (Exception e) {
			sendError(req, resp, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	
	private String getUserId() throws Throwable {
		if(ContextInfo.isAnonymous() || ContextInfo.getUserSession() == null) {
			return ANONYMOUS;
		} else {
			return getUserIdFromSession(ContextInfo.getUserSession());
		}
		
	}
	
	private String getUserIdFromSession(Session userSession) throws Throwable {
		String userId = null;
		String userName = userSession.getEffectiveUserName();
		try {
			if(StringUtils.containsIgnoreCase(userName, "CN=")) {
				userId = getCanonicalShindigId(userName);
			} else {
				userId = IdUtil.getShindigId(userName);
			}
		} catch (Throwable t) {
			throw t;
		}
		return userId;
	}
	
	private String getCanonicalShindigId(String userName) {
		//An OpenSocial user id can only have alpha numeric characters and periods, underscores, and dashes
		String id = userName.replace("=", ".");
		id = id.replace("/", "_");
		return id;
	}

	// Build a response that includes the token itself and some other metadata,
	// e.g, ttl
	private String buildResponseString(SimpleSecurityToken token, String tokenString) throws JsonException, IOException {
		Map<String, Object> jsonMap = Maps.newHashMap();
		jsonMap.put(RESP_TTL_KEY, token.getTokenTTL());
		jsonMap.put(RESP_TOKEN_KEY, tokenString);
		return JsonGenerator.toJson(JsonJavaFactory.instance, jsonMap, true);
	}

	private void sendError(HttpServletRequest req, HttpServletResponse resp, String message, int code)
			throws IOException {
		this.logger.logp(Level.WARNING, CLASSNAME, METHOD,
				"warn.security.servlet.response", new Object[] { code, message }); //$NON-NLS-1$
		resp.sendError(code, message);
	}
	
	private void sendError(HttpServletRequest req, HttpServletResponse resp, String message, int code, Throwable t)
			throws IOException {
		sendError(req, resp, message, code);
		this.logger.logp(Level.WARNING, CLASSNAME, METHOD, t.getMessage(), t);
	}

	private SimpleSecurityToken generateSecurityToken(HttpServletRequest req, String viewerId, String ownerId) {
		Map<String, String> sanitizedParameters = Maps.newHashMap();
		
		@SuppressWarnings("unchecked")
		Map<String, String[]> inputParameters = req.getParameterMap();
		
		Set<String> inputKeySet = inputParameters.keySet();
		String[] inputValue;
		for (String inputKey : inputKeySet) {
			inputValue = inputParameters.get(inputKey);
			if (inputValue != null && inputValue.length > 0) {
				sanitizedParameters.put(inputKey, inputValue[0]);
			}
		}
		// Ignore the viewer and owner if they were passed on the query string
		// and add it based on the user session
		sanitizedParameters.put(Keys.VIEWER.getKey(), viewerId);
		sanitizedParameters.put(Keys.OWNER.getKey(), ownerId);
		return new DominoSecurityToken(sanitizedParameters);
	}

	private void logRequestInfo(HttpServletRequest req) {
		this.logger.finest("SecurityTokenServlet - remote address: " + req.getRemoteAddr()); //$NON-NLS-1$
		this.logger.finest("SecurityTokenServlet - query string: " + req.getQueryString()); //$NON-NLS-1$
	}
	
	private class DominoSecurityToken extends SimpleSecurityToken {
		private boolean isAnonymous = false;
		public DominoSecurityToken(Map<String, String> params) {
			super(params);
			if(ANONYMOUS.equals(params.get(Keys.VIEWER.getKey()))) {
				isAnonymous = true;
			}
		}
		@Override
		public boolean isAnonymous() {
			return isAnonymous;
		}
	}
}
