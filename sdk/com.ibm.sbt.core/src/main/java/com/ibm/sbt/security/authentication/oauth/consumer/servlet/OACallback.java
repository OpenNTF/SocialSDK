/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.security.authentication.oauth.consumer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAConstants;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.service.core.handlers.AbstractServiceHandler;
import com.ibm.sbt.services.util.AnonymousCredentialStore;

/**
 * OAuth servlet.
 * <p>
 * This servlet gets the oauth verifier back from the oauth handler
 * </p>
 * 
 * @author Philippe Riand
 * @author Vimal Dhupar
 */
public class OACallback extends AbstractServiceHandler {

	public static final String URL_PATH = "oauth_cb";

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// We should here find the right mode based on the URL
		execHttpSession();
	}

	// =========================================================================================
	//
	// Mode where an HTTP session exists
	//
	// =========================================================================================

	public void execHttpSession() throws ServletException, IOException {
		Context context = Context.get();

		// Find the OAuth dance object being used
		OAuth1Handler oAuthHandler = (OAuth1Handler)context.getSessionMap().get(Configuration.OAUTH1_HANDLER);
		if (oAuthHandler == null) {
			throw new ServletException(
					"Internal Error: Cannot find the OAuth object back from the request");
		}

		// Read the oauth parameters
		try {
			String oauth_token = (String) context.getRequestParameterMap().get(OAConstants.OAUTH_TOKEN);
			String oauth_verifier = (String) context.getRequestParameterMap().get(OAConstants.OAUTH_VERIFIER);
			
			oAuthHandler.setAccessToken(oauth_token);
			oAuthHandler.setVerifierCode(oauth_verifier);
			
			AccessToken tk = oAuthHandler.readToken(oauth_token, oauth_verifier);
			if (tk == null) {
				// should not happen
				throw new ServletException("Missing OAuth token");
			}
			// Store the new key
			oAuthHandler.setAccessTokenObject(tk); 
			if (!context.isCurrentUserAnonymous()) {
				CredentialStore cs = CredentialStoreFactory.getCredentialStore(oAuthHandler.getCredentialStore());
				if (cs != null) {
					// But we store it uniquely if the current user is not anonymous
					cs.store(oAuthHandler.getServiceName(), OAuth1Handler.ACCESS_TOKEN_STORE_TYPE, context.getCurrentUserId(), tk);
				}
			} else {
				AnonymousCredentialStore.storeCredentials(context, tk,
						oAuthHandler.getAppId(), oAuthHandler.getServiceName());
			}

			// redirect to the initial page
			String applicationPage = oAuthHandler.getApplicationPage();
			if (StringUtil.isNotEmpty(applicationPage)) {
				context.sendRedirect(applicationPage);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
