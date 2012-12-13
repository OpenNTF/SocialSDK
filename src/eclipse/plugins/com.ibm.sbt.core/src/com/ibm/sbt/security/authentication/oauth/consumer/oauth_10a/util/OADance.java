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
package com.ibm.sbt.security.authentication.oauth.consumer.oauth_10a.util;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.plugin.SbtCoreLogger;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.OAConstants;
import com.ibm.sbt.security.authentication.oauth.consumer.OAProvider;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuth1Handler;
import com.ibm.sbt.security.authentication.oauth.consumer.OAuthHandler;
import com.ibm.sbt.security.authentication.oauth.consumer.store.TokenStore;

/**
 * Encapsulate the different steps in the OAuth "dance".
 * <p> 
 * This object is kept in the user session while "playing the dance". In a full J2EE environment,
 * it will need to be serializable. But, as Domino doesn't serialize the session yet, this is
 * not a requirement. 
 * </p>
 * @author Philippe Riand
 */
public class OADance implements Serializable {

    private static final ProfilerType profilerReadTempToken = new ProfilerType("OAuth: Acquire a temporary token from the provider"); //$NON-NLS-1$
    private static final ProfilerType profilerReadToken = new ProfilerType("OAuth: Read a token with verifier from the provider"); //$NON-NLS-1$
    
    public static final String OAUTHDANCE_KEY = "sbt.oauth.dance";

    private static final long serialVersionUID = 1L;
    
    // This should be serializable - find a solution
    private transient TokenStore store;
    
    private String appId;
    private String serviceName;
    private String userId;
    private String applicationPage;
    private String callback;
    private String signatureMethod;
    private OAuth1Handler oauthHandler;
    private OAProvider oaProvider;

    public OADance(OAProvider service, String appId, String serviceName, String userId, String callback, String applicationPage) throws OAuthException {
        this.store = service.findTokenStore();
        this.appId = appId;
        this.serviceName = serviceName;
        this.userId = userId;
        this.applicationPage = applicationPage;
        this.callback = callback;
        this.signatureMethod = service.getSignatureMethod();
        this.oauthHandler = (OAuth1Handler) Context.get().getSessionMap().get(Configuration.OAUTH_HANDLER);
        this.oaProvider = service;
    }

    public String getApplicationPage() {
        return applicationPage;
    }
    public String getRequestTokenUri() {
        return oaProvider.getRequestTokenURL();
    }
    
    public String getConsumerKey() {
        return oaProvider.getConsumerKey();
    }
    
    public TokenStore getTokenStore() {
        return store;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getUserId() {
        return userId;
    }
        

    // =================================================================
    // Requesting a token
    // =================================================================

    /**
     * Request a temporary token for the application and redirect to a callback page.
     */
    public void perform3LegsDance(Context context) throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerReadTempToken, "");
            long ts = Profiler.getCurrentTime();
            try {
                _perform3LegsDance(context);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            _perform3LegsDance(context);
        }
    }
    protected void _perform3LegsDance(Context context) throws OAuthException {       
        try {
        	// Call the OAuth1Handler's method to get the Request token by making network call.
        	oauthHandler.getRequestTokenFromServer();
        	
            // Store the OAuthDance
            context.getSessionMap().put(OADance.OAUTHDANCE_KEY,this);
            
            String redirectUrl = oaProvider.getAuthorizationURL() + "?" + OAConstants.OAUTH_TOKEN + "=" + oauthHandler.getRequestToken();
            // tbd: is there a better way to handle this DropboxFiles specific parameter?
            if (redirectUrl.contains("dropbox")) {
            	redirectUrl = redirectUrl + "&" + OAConstants.OAUTH_CALLBACK + "=" + callback;
        	}
            
            // Look if there are paththough parameters
            String pass = (String)context.getRequestParameterMap().get("oaredirect");
            if(StringUtil.isNotEmpty(pass)) {
            	//Commenting this logic of modification of URL, as this creates a URL for OAuth1.0 which includes unsupported parameters, and error is thrown by Smartcloud. 
            	//Testing this removal of code, to check if it breaks anything. Preliminary testing results in OK status(things work without this code). 
            	
            	//redirectUrl = redirectUrl + "&oaredirect=" + URLEncoder.encode(pass,"utf-8");
            }
            
            // URL redirection
            context.sendRedirect(redirectUrl);
            //throw new RedirectSignal();
            //throw new RedirectSignal(redirectUrl);
        } catch (IOException e) {
            throwOAuthException(e, "_perform3LegsDance", "Failed to get request token.");
        } catch (OAuthException e) {
            throwOAuthException(e, "_perform3LegsDance", "Failed to get request token.");
        } catch (URISyntaxException e) {
            throwOAuthException(e, "_perform3LegsDance", "Failed to get request token.");
        } catch (Exception e) {
        	throwOAuthException(e, "_perform3LegsDance", "Failed to get request token.");
		}   
    }
    

    
    /**
     * Read the OAuth token from the verifier.
     */
    public AccessToken readToken(String token, String verifier) throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerReadToken, "");
            long ts = Profiler.getCurrentTime();
            try {
                return _readToken(token, verifier);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            return _readToken(token, verifier);
        }
    }
    protected AccessToken _readToken(String token, String verifier) throws OAuthException {
    	// first we set the Verifier which will be used to get the Access Token
    	oauthHandler.setVerifierCode(verifier);
        try {        	
        	oauthHandler.getAccessTokenFromServer();
        } catch (IOException e) {
            throwOAuthException(e, "_readToken", "Failed to get access token.");
        } catch (OAuthException e) {
            throwOAuthException(e, "_readToken", "Failed to get access token.");
        } catch (URISyntaxException e) {
            throwOAuthException(e, "_readToken", "Failed to get access token.");
        } catch (Exception e) {
        	throwOAuthException(e, "_readToken", "Failed to get access token.");
		}
        
        return oaProvider.createToken(getAppId(), getServiceName(), oauthHandler, getUserId());
    }
    /*
     * If the OAuth dance fails we want to provide as much info as possible about the OAuth information we are using. Often we will be dealing with
     * non Lotus services so we want to make sure the admin has enough info to start diagnosing what is wrong.
     */
    private void throwOAuthException(Exception e, String method, String message) throws OAuthException{
        String callback = null;
        String secret = null;
        String key = null;
        String requestUrl = null;
        String authorizeUrl = null;
        String accessUrl = null;
        boolean shortKey = false;
        boolean shortSecret = false;
        Context context = Context.get();
        if(oaProvider != null){
            callback = oaProvider.getCallbackUrl(context);
            secret = oaProvider.getConsumerSecret();
            if(StringUtil.isNotEmpty(secret)){
                int pre = 0;
                if(secret.length() > 12){
                    pre = 4;
                }
                else if(secret.length() > 9){
                    pre = 3;
                }
                else{
                    shortSecret = true;
                    if(secret.length() > 6){
                        pre = 2;
                    }
                    else{
                        secret = StringUtil.format("secret is too short to display, {0} characters long", secret.length());
                    }
                }
                if(pre >= 2){
                    String tmp = secret.substring(0, pre);
                    tmp = tmp + "....";
                    tmp = tmp + secret.substring(secret.length() - pre);
                    secret = tmp;
                }
            }
            key = oaProvider.getConsumerKey();
            if(StringUtil.isNotEmpty(key)){
                int pre = 0;
                if(key.length() > 12){
                    pre = 4;
                }
                else if(key.length() > 9){
                    pre = 3;
                }
                else {
                    shortKey = true;
                    if(key.length() > 6){
                        pre = 2;
                    }
                    else{
                        key = StringUtil.format("key is too short to display, {0} characters long", key.length());
                    }
                }
                if(pre >= 2){
                    String tmp = key.substring(0, pre);
                    tmp = tmp + "....";
                    tmp = tmp + key.substring(key.length() - pre);
                    key = tmp;
                }
            }
            if(oaProvider != null){
                requestUrl = oaProvider.getRequestTokenURL();
                authorizeUrl =oaProvider.getAuthorizationURL();
                accessUrl = oaProvider.getAccessTokenURL();
            }
        }
        String formattedString = StringUtil.format(" requestUrl:{0}, authorizeUrl: {1}, accessUrl: {2}, callback: {3}, truncated key:{4}, truncated secret:{5}.", requestUrl, authorizeUrl, accessUrl, callback, key, secret);
        StringBuffer extraInfo = new StringBuffer(" ");
        if(StringUtil.isEmpty(requestUrl)){
            extraInfo.append("OAuth requestUrl is empty, please check your application definition in the the web security store is correct.\n");
        }
        if(StringUtil.isEmpty(authorizeUrl)){
            extraInfo.append("OAuth authorizeUrl is empty, please check your application definition in the the web security store is correct.\n");
        }
        if(StringUtil.isEmpty(accessUrl)){
            extraInfo.append("OAuth accessUrl is empty, please check your application definition in the the web security store is correct.\n");
        }
        if(StringUtil.isEmpty(callback)){
            extraInfo.append("OAuth callback is empty, please check with your application vendor to ensure a callback is not required.\n");
        }
        if(shortKey){
            extraInfo.append("The value supplied for the OAuth user key is short (less than 9 characters), please ensure it has been entered correctly.\n");
        }
        if(shortSecret){
            extraInfo.append("The value supplied for the OAuth user secret is short (less than 9 characters) please ensure it has been entered correctly.\n");
        }
        if(SbtCoreLogger.SBT.isErrorEnabled()){
            SbtCoreLogger.SBT.errorp(this, method, e, message + formattedString + extraInfo.toString());
        }
        
        throw new OAuthException(e, message + formattedString + extraInfo.toString());
    }
}
