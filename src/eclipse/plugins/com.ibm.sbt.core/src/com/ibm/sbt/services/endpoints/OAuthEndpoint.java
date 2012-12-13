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

package com.ibm.sbt.services.endpoints;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAProvider;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler;
import com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.servlet.OAClientAuthentication;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;

/**
 * Bean that provides authentication via OAuth.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class OAuthEndpoint extends AbstractEndpoint {
    
    private OAProvider oaProvider = new OAProvider();

    public OAuthEndpoint() {
    }

    @Override
	public void checkValid() throws SBTException {
    	super.checkValid();
    	if(StringUtil.isEmpty(oaProvider.getConsumerKey())) {
    		throw new SBTException(null,"The Endpoint consumer key is empty, class {0}",getClass());
    	}
    	if(StringUtil.isEmpty(oaProvider.getConsumerSecret())) {
    		throw new SBTException(null,"The Endpoint consumer secret is empty, class {0}",getClass());
    	}
    	if(StringUtil.isEmpty(oaProvider.getAuthorizationURL())) {
    		throw new SBTException(null,"The Endpoint authorization URL is empty, class {0}",getClass());
    	}
    	if(StringUtil.isEmpty(oaProvider.getRequestTokenURL())) {
    		throw new SBTException(null,"The Endpoint request token URL is empty, class {0}",getClass());
    	}
    	if(StringUtil.isEmpty(oaProvider.getAccessTokenURL())) {
    		throw new SBTException(null,"The Endpoint access token URL is empty, class {0}",getClass());
    	}
    }

    public OAProvider getOAuthProvider() {
        return oaProvider;
    }

    @Override
    public void setUrl(String url) {
        super.setUrl(url);
        // Make the URL the service name if not already set
        if(StringUtil.isEmpty(oaProvider.getServiceName())) {
            oaProvider.setServiceName(url);
        }
    }

    public String getConsumerKey() {
        return oaProvider.getConsumerKey();
    }
    public void setConsumerKey(String consumerKey) {
        oaProvider.setConsumerKey(consumerKey);
    }

    public String getConsumerSecret() {
        return oaProvider.getConsumerSecret();
    }
    public void setConsumerSecret(String consumerSecret) {
        oaProvider.setConsumerSecret(consumerSecret);
    }

    public String getTokenStore() {
        return oaProvider.getTokenStore();
    }
    public void setTokenStore(String tokenStore) {
        oaProvider.setTokenStore(tokenStore);
    }

    public String getAppId() {
        return oaProvider.getAppId();
    }
    public void setAppId(String appId) {
        oaProvider.setAppId(appId);
    }

    public String getServiceName() {
        return oaProvider.getServiceName();
    }
    public void setServiceName(String serviceName) {
        oaProvider.setServiceName(serviceName);
    }

    public String getRequestTokenURL() {
        return oaProvider.getRequestTokenURL();
    }
    public void setRequestTokenURL(String requestTokenURL) {
        oaProvider.setRequestTokenURL(requestTokenURL);
    }

    public String getAuthorizationURL() {
        return oaProvider.getAuthorizationURL();
    }
    public void setAuthorizationURL(String authorizationURL) {
        oaProvider.setAuthorizationURL(authorizationURL);
    }

    public String getAccessTokenURL() {
        return oaProvider.getAccessTokenURL();
    }
    public void setAccessTokenURL(String accessTokenURL) {
        oaProvider.setAccessTokenURL(accessTokenURL);
    }

    public String getSignatureMethod() {
        return oaProvider.getSignatureMethod();
    }
    public void setSignatureMethod(String signatureMethod) {
        oaProvider.setSignatureMethod(signatureMethod);
    }
    
	@Override
	public String getAuthType() {
		return "oauth1.0a";
	}

    @Override
	public boolean isForceTrustSSLCertificate() {
        return oaProvider.getForceTrustSSLCertificate();
    }
    
    @Override
	public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
    	oaProvider.setForceTrustSSLCertificate(forceTrustSSLCertificate);
    }
    
    @Override
    public JSReference getAuthenticator(String endpointName) {
    	Context ctx = Context.get();
    	JSReference reference = new JSReference("sbt/authenticator/OAuth10");
    	StringBuilder b = new StringBuilder();
    	RuntimeConstants.get().appendBaseProxyUrl(b, ctx);
        b.append("/");
    	b.append(OAClientAuthentication.URL_PATH);
    	b.append('/');
    	b.append(endpointName);
    	String url = b.toString();
    	reference.getProperties().put("url", url);
    	return reference;
    }

    @Override
	public boolean isAuthenticated() throws ClientServicesException {
        try {
            return oaProvider.acquireToken()!=null;
        } catch(OAuthException ex) {
            throw new ClientServicesException(ex);
        }
    }

    @Override
	public void authenticate(boolean force) throws ClientServicesException {
        try {
            oaProvider.acquireToken(true,force);
        } catch(OAuthException ex) {
            throw new ClientServicesException(ex);
        }
    }
    
    @Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
        try {
        	AccessToken token = oaProvider.acquireToken(false);
            if(token!=null) {
	            HttpRequestInterceptor oauthInterceptor = new OAuthInterceptor(token);
	            httpClient.addRequestInterceptor(oauthInterceptor, 0);
            }
        } catch(OAuthException ex) {
            throw new ClientServicesException(ex);
        }
    }


    private static class OAuthInterceptor implements HttpRequestInterceptor {
        
        private AccessToken token;

        public OAuthInterceptor(AccessToken token) {
            this.token = token;
        }

        @Override
		public void process(HttpRequest request, HttpContext context)throws HttpException, IOException {
        	
        	Context contextForHandler = Context.get();
        	OAuthHandler oaHandler = (OAuthHandler) contextForHandler.getSessionMap().get(Configuration.OAUTH_HANDLER);
        	String authorizationheader =  oaHandler.createAuthorizationHeader();
        	request.addHeader("Authorization", authorizationheader);
        	}
    }


}
