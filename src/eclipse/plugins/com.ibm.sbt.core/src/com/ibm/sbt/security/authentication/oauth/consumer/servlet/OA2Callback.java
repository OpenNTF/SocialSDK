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
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth2Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.store.OATokenStoreFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.store.TokenStore;
import com.ibm.sbt.service.core.handlers.AbstractServiceHandler;
import com.ibm.sbt.services.util.AnonymousCredentialStore;

/**
 * Callback servlet for OAuth2.0 Dance
 * 
 * @author mkataria
 */
public class OA2Callback extends AbstractServiceHandler {

	public static final String URL_PATH = "oauth20_cb";
	private static final ProfilerType profilerAcquireToken = new ProfilerType("OAuth2.0: Acquire a token from the service"); //$NON-NLS-1$
	OAuth2Handler oAuthHandler;
	
	private static final String sourceClass = OA2Callback.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	 	Context context = Context.get();
		OAuth2Handler oAuthHandler = (OAuth2Handler)context.getSessionMap().get(Configuration.OAUTH_HANDLER);
		if (oAuthHandler == null) {
		    // this can happen if you access the application using a different hostname
		    // to the one registered as the OAuth2.0 redirect URI
		    StringBuffer requestUrl = request.getRequestURL();
		    String msg = "Unable to retrieve OAuth2.0 handler for redirect request to {0}. Please check you are accessing the application using the same hostname used in the OAuth 2.0 redirect URI.";
		    logger.info(MessageFormat.format(msg, requestUrl));
		    return;
		}
		
		String authcode = extractAuthorizationToken(request);
		oAuthHandler.setAuthorization_code(authcode);
		try {
			oAuthHandler.getAccessTokenForAuthorizedUser(); // This retrieves and sets all authentication information in OAuth2Handler
			AccessToken token = oAuthHandler.createToken(oAuthHandler.getAppId(),oAuthHandler.getServiceName());
            // Store the new key
        	if(!context.isCurrentUserAnonymous()) {
        		TokenStore ts = OATokenStoreFactory.getTokenStore(oAuthHandler.getTokenStore());
        		if(ts!=null) {
            		ts.saveAccessToken(token);
            	}
            } else {
            	// Store the token for anonymous user
            	AnonymousCredentialStore.storeCredentials(context, token, oAuthHandler.getAppId(), oAuthHandler.getServiceName());
            }
			Context.get().sendRedirect(oAuthHandler.getApplicationPage());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	 
	 private String extractAuthorizationToken(HttpServletRequest request){
		 if (Profiler.isEnabled()) {
	            ProfilerAggregator agg = Profiler.startProfileBlock(profilerAcquireToken, "");
	            long ts = Profiler.getCurrentTime();
	            try {
	                return _extractAuthorizationToken(request);
	            } finally {
	                Profiler.endProfileBlock(agg, ts);
	            }
	        } else {
	            return _extractAuthorizationToken(request);
	        }
	 }
	 
	 private String _extractAuthorizationToken(HttpServletRequest request){
		 return request.getParameter("code");
	 }
	 

}