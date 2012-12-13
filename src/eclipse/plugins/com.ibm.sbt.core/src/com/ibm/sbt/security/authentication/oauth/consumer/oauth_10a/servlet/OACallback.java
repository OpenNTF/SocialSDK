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

package com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAConstants;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.util.OADance;
import com.ibm.sbt.security.authentication.oauth.consumer.store.TokenStore;
import com.ibm.sbt.service.core.handlers.AbstractServiceHandler;
import com.ibm.sbt.services.util.AnonymousCredentialStore;

/**
 * OAuth servlet.
 * <p>
 * This servlet gets the oauth verifier back from the oauth provider
 * </p>
 * @author Philippe Riand
 */
public class OACallback extends AbstractServiceHandler {

    public static final String URL_PATH = "oauth10_cb";

    private static final long serialVersionUID = 1L;

    @Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        OADance dance = readDance(context);
        if(dance==null) {
        	throw new ServletException("Internal Error: Cannot find the OAuth object back from the request");
        }

        // Read the oauth parameters 
        try {
            String oauth_token = (String)context.getRequestParameterMap().get(OAConstants.OAUTH_TOKEN);
            String oauth_verifier = (String)context.getRequestParameterMap().get(OAConstants.OAUTH_VERIFIER);
            AccessToken tk = dance.readToken(oauth_token,oauth_verifier);
            if(tk==null) {
                // should not happen
                throw new ServletException("Missing OAuth token");
            }
            // Store the new key
        	if(!context.isCurrentUserAnonymous()) {
        		TokenStore ts = dance.getTokenStore();
        		if(ts!=null) {
        			// But we store it uniquely if the current user is not anonymous
            		ts.saveAccessToken(tk);
            	}
            } else {
            	AnonymousCredentialStore.storeCredentials(context, tk, dance.getAppId(), dance.getServiceName());
            }
            
            // redirect to the initial page
            String applicationPage = dance.getApplicationPage();
            if(StringUtil.isNotEmpty(applicationPage)) {
            	context.sendRedirect(applicationPage);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }   

    
    protected OADance readDance(Context context) throws ServletException {
        OADance dance = (OADance)context.getSessionMap().get(OADance.OAUTHDANCE_KEY);
        if(dance==null) {
            // should not happen
            throw new ServletException("Missing OAuth context");
        }
        context.getSessionMap().remove(OADance.OAUTHDANCE_KEY);

        return dance;
    }
}
