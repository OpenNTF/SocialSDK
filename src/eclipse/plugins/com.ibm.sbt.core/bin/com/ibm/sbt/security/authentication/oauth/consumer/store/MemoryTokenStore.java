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

package com.ibm.sbt.security.authentication.oauth.consumer.store;

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.authentication.oauth.consumer.AccessToken;
import com.ibm.sbt.security.authentication.oauth.consumer.ConsumerToken;


/**
 * Memory based OAuth user token store.
 * @author Philippe Riand
 */
public class MemoryTokenStore implements TokenStore {
    
    private Map<String,ConsumerToken> appTokens = new HashMap<String, ConsumerToken>();
    private Map<String,AccessToken> userTokens = new HashMap<String, AccessToken>();
    
    public MemoryTokenStore() {
    }

    
    //
    // App tokens
    //
    
    @Override
	public ConsumerToken loadConsumerToken(String application, String provider) throws OAuthException {
        String key = getAppKey(application, provider);
        ConsumerToken tk = appTokens.get(key);
        if(tk==null) {
        	tk = createDefaultConsumerToken(application, provider);
        }
        return tk;
    }

    @Override
	public void saveConsumerToken(String application, String provider, ConsumerToken token) throws OAuthException {
        String key = getAppKey(application, provider);
        appTokens.put(key,token);
    }
    
    protected String getAppKey(String application, String provider) {
    	StringBuilder sb = new StringBuilder( 200 );
		sb.append( application ).append( '|' ).append( provider );
		return sb.toString();
    }
    
    protected ConsumerToken createDefaultConsumerToken(String application, String provider) throws OAuthException {
    	return null;
    }

    
    //
    // User tokens
    //

    @Override
	public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String userId) throws OAuthException {
    	return loadAccessToken(appId, serviceName, consumerKey, null, null, userId);
    }
    
    @Override
	public AccessToken loadAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId) throws OAuthException {
    	String key = getUserKey( appId, serviceName, consumerKey, moduleId, tokenName, userId );
        AccessToken tk = userTokens.get(key);
        if(tk==null) {
        	tk = createDefaultAccessToken(appId, serviceName, consumerKey, moduleId, tokenName, userId);
        }
        return tk;
    }
    
    protected AccessToken createDefaultAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId) throws OAuthException {
    	return null;
    }

    @Override
	public void saveAccessToken(AccessToken token) throws OAuthException {
        String key = getUserKey(token.getAppId(), token.getServiceName(), token.getConsumerKey(), token.getModuleId(), token.getTokenName(), token.getUserId());
        userTokens.put(key,token);
    }
    
    @Override
	public void deleteAccessToken(String appId, String serviceName, String consumerKey, String userId) throws OAuthException {
    	deleteAccessToken(appId, serviceName, consumerKey, null, null, userId);
    }
    
    @Override
	public void deleteAccessToken(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId ) throws OAuthException {
    	String key = getUserKey( appId, serviceName, consumerKey, moduleId, tokenName, userId );
    	userTokens.remove(key);
    }
     
	protected String getUserKey(String appId, String serviceName, String consumerKey, String moduleId, String tokenName, String userId ) { 
		StringBuilder sb = new StringBuilder( 200 );
		sb.append( appId ).append( '|' ).
		   append( serviceName ).append( '|' ).
		   append( consumerKey ).append( '|' ).
		   append( userId ).append( '|').
		   append( StringUtil.getNonNullString(moduleId) ).append( '|' ).
		   append( StringUtil.getNonNullString(tokenName) );
		return sb.toString();
	}
}
