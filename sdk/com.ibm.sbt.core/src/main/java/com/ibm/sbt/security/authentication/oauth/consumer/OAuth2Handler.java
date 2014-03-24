/****************************************************************************************
 * Copyright 2012 IBM Corp.                                                                   *
 *                                                                                      *
 * Licensed under the Apache License, Version 2.0 (the "License");                      *
 * you may not use this file except in compliance with the License.                     *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0   *
 *                                                                                      *
 * Unless required by applicable law or agreed to in writing, software                  *
 * distributed under the License is distributed on an "AS IS" BASIS,                    *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.             *
 * See the License for the specific language governing permissions and                  *
 * limitations under the License.                                                       *
 ****************************************************************************************/

package com.ibm.sbt.security.authentication.oauth.consumer;

//import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreException;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OA2Callback;
import com.ibm.sbt.service.util.ServiceUtil;
import com.ibm.sbt.services.util.AnonymousCredentialStore;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * @author Manish Kataria
 */

public class OAuth2Handler extends OAuthHandler {

	public static final int EXPIRE_THRESHOLD = 60;  // 60sec = 1min
	
	private String appId;
	private String client_uri;
	private String authorization_code;
	private String accessToken;
	private String refreshToken;
	private Date issuedOn;
	private String expiresIn;
	private String tokenType;
	private String servicename;
	private String authorizationURL;
	private String accessTokenURL;
	private String serviceName;
	private String consumerKey;
	private String consumerSecret;
	private String applicationPage;
	private int expireThreshold;
	private boolean forceTrustSSLCertificate;
	private AccessToken accessTokenObject;
	
	// Type used to store the credentials
	public static final String ACCESS_TOKEN_STORE_TYPE = "OAUTH2_ACCESS_TOKEN_STORE";
	public static final String CONSUMER_TOKEN_STORE_TYPE = "OAUTH2_CONSUMER_TOKEN_STORE";
	
	// Persistence store code
	private boolean storeRead;
	private String credentialStore; 
    private static final ProfilerType profilerLoadCredentialStore = new ProfilerType("OAuth: Load a token from the store"); //$NON-NLS-1$
    private static final ProfilerType profilerAcquireToken = new ProfilerType("OAuth: Acquire a token from the service"); //$NON-NLS-1$
    private static final ProfilerType profilerRenewToken = new ProfilerType("OAuth: Renew a token from the provider"); //$NON-NLS-1$
    private static final ProfilerType profilerDeleteToken = new ProfilerType("OAuth: Delete a token from the store"); //$NON-NLS-1$
    
	//for logging 
	private static final String sourceClass = OAuth1Handler.class.getName();
    private static final Logger logger = Logger.getLogger(sourceClass);

    private boolean usePost = false;
    
	public boolean isUsePost() {
		return usePost;
	}

	public void setUsePost(boolean usePost) {
		this.usePost = usePost;
	}

	public OAuth2Handler() {
	    this.setExpireThreshold(EXPIRE_THRESHOLD);
	}
	
	@Override
	public boolean isInitialized() {
		return accessToken!=null;
	}
	
	/**
	 * Generates the authorization url for fetching the authorization tokens
	 * 
	 * https://apps.lotuslive.com/manage/oauth2/authorize?
	 *                    response_type=code&
	 *                    client_id=<client_id>&
	 *                    callback_uri=<callback_uri>
	 * 
	 * @return URL
	 */
	public String getAuthorizationNetworkUrl() {
		StringBuilder url = new StringBuilder();
		try {
			url.append(getAuthorizationURL());
			url.append('?');
			url.append(Configuration.OAUTH2_RESPONSE_TYPE);
			url.append('=');
			url.append(Configuration.OAUTH2_CODE);
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_ID);
			url.append('=');
			url.append(URLEncoder.encode(consumerKey, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CALLBACK_URI);
			url.append('=');
			url.append(URLEncoder.encode(client_uri, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return url.toString();	
	}
	
	/**
	 ** Authorization: OAuth 
	 *                callback_uri="<callback_uri>", 
	 *                client_secret="<client_secret>",
	 *                client_id="<client_id>", 
	 *                grant_type="authorization_code", 
	 *                code="<authorization_code>"
	 * 
	 * Calls the server URL with Authorization header and gets back following values:
	 *    access_token, refresh_token, issued_on expires_in, token_type.
	 * 
	 * @throws Exception
	 */
	public void getAccessTokenForAuthorizedUser() throws Exception {
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "getAccessTokenForAuthorizedUser", new Object[] { });
        }
		if (usePost) {
			getAccessTokenForAuthorizedUsingPOST();
		} else {
			getAccesTokenForAutherzedUserGET();
		}
	
	}
	
	
	private void getAccesTokenForAutherzedUserGET() throws OAuthException,
			IOException, Exception {
		HttpGet method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			if(forceTrustSSLCertificate)
				client = (DefaultHttpClient)SSLUtil.wrapHttpClient((DefaultHttpClient)client);
			StringBuffer url = new StringBuffer(2048);
			url.append(getAccessTokenURL()).append("?");
			url.append(Configuration.OAUTH2_CALLBACK_URI).append('=').append(URLEncoder.encode(client_uri, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_ID).append('=').append(URLEncoder.encode(consumerKey, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_SECRET).append('=').append(URLEncoder.encode(consumerSecret, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_GRANT_TYPE).append('=').append(Configuration.OAUTH2_AUTHORIZATION_CODE);
			url.append('&');
			url.append(Configuration.OAUTH2_CODE).append('=').append(URLEncoder.encode(authorization_code, "UTF-8"));
			method = new HttpGet(url.toString());
			HttpResponse httpResponse =client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			if (logger.isLoggable(Level.FINEST)) {
	    		logger.log(Level.FINEST, "OAuth2.0 network call to fetch token :" + url, responseCode);
	    	}
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			try {
				responseBody = StreamUtil.readString(reader);
			} finally {
				StreamUtil.close(reader);
			}
		} catch (Exception e) {
			throw new OAuthException(e, "getAccessToken failed with Exception: <br>" + e);
		} finally {
			if(content != null) {
				content.close(); 
			}
		}
		
		if (responseCode != HttpStatus.SC_OK) {
			//this is a leftover of previous code that tried a POST in case of error
			//can't remove it without changing the behavior of installations that run
			//with a bad API level but works because of this fall back mechanism.
			logger.warning("Getting access token failed, retrying with POST. Please configure the right API Level for your installation in sbt.properties or managed-beans.xml");
			getAccessTokenForAuthorizedUsingPOST();
		} else {
			setOAuthData(responseBody); //save the returned data
		}
	}
	
	/*
	 * 
   grant_type
         REQUIRED.  Value MUST be set to "authorization_code".
   code
         REQUIRED.  The authorization code received from the
         authorization server.
   redirect_uri
         REQUIRED, if the "redirect_uri" parameter was included in the
         authorization request as described in Section 4.1.1, and their
         values MUST be identical.
   client_id
         REQUIRED, if the client is not authenticating with the
         authorization server as described in Section 3.2.1.

	 */
	
	public void getAccessTokenForAuthorizedUsingPOST() throws Exception {
		
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "getAccessTokenForAuthorizedUsingPOST", new Object[] { });
        }
		
		HttpPost method = null;
		int responseCode = HttpStatus.SC_OK;
		String responseBody = null;
		InputStream content = null;
		try {
			HttpClient client = new DefaultHttpClient();
			if(forceTrustSSLCertificate)
				client = (DefaultHttpClient)SSLUtil.wrapHttpClient((DefaultHttpClient)client);
			StringBuffer url = new StringBuffer(2048);
			
			// This works for Smartcloud
/*			url.append(getAccessTokenURL()).append("?");
			url.append(Configuration.OAUTH2_CALLBACK_URI).append('=').append(URLEncoder.encode(client_uri, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_ID).append('=').append(URLEncoder.encode(consumerKey, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_CLIENT_SECRET).append('=').append(URLEncoder.encode(consumerSecret, "UTF-8"));
			url.append('&');
			url.append(Configuration.OAUTH2_GRANT_TYPE).append('=').append(Configuration.OAUTH2_AUTHORIZATION_CODE);
			url.append('&');
			url.append(Configuration.OAUTH2_CODE).append('=').append(URLEncoder.encode(authorization_code, "UTF-8"));
			System.err.println("url used here "+url);
			method = new HttpPost(url.toString());*/
			
			
			// This works for connections
			// add parameters to the post method  
			method = new HttpPost(getAccessTokenURL());
			List <NameValuePair> parameters = new ArrayList <NameValuePair>();   
			parameters.add(new BasicNameValuePair(Configuration.OAUTH2_CALLBACK_URI, URLEncoder.encode(client_uri, "UTF-8")));   
			parameters.add(new BasicNameValuePair(Configuration.OAUTH2_CLIENT_ID, URLEncoder.encode(consumerKey, "UTF-8")));   
			parameters.add(new BasicNameValuePair(Configuration.OAUTH2_CLIENT_SECRET, URLEncoder.encode(consumerSecret, "UTF-8")));   
			parameters.add(new BasicNameValuePair(Configuration.OAUTH2_GRANT_TYPE, Configuration.OAUTH2_AUTHORIZATION_CODE));   
			parameters.add(new BasicNameValuePair(Configuration.OAUTH2_CODE, URLEncoder.encode(authorization_code, "UTF-8")));   
			UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);  
			method.setEntity(sendentity);   
			HttpResponse httpResponse =client.execute(method);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			
			if (logger.isLoggable(Level.FINEST)) {
	    		logger.log(Level.FINEST, "OAuth2.0 network call to fetch token :" + getAccessTokenURL(), responseCode);
	    	}
			
			content = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			try {
				responseBody = StreamUtil.readString(reader);
			} finally {
				StreamUtil.close(reader);
			}
		} catch (Exception e) {
			throw new OAuthException(e,"getAccessToken failed with Exception: <br>");
		} finally {
			if(content!=null) {
				content.close();
			}
		}
		if (responseCode != HttpStatus.SC_OK) {
			String exceptionDetail = buildErrorMessage(responseCode, responseBody);
			if (exceptionDetail != null) {
				String msg = "Unable to retrieve access token because \"{0}\". Please check the access token URL is valid, current value: {1}.";
				msg = MessageFormat.format(msg, exceptionDetail, getAccessTokenURL());
				throw new OAuthException(null, msg);
			}
		} else {
			setOAuthData(responseBody); //save the returned data
		}
	}
	

	@Override
	public String createAuthorizationHeader() {
		if (logger.isLoggable(Level.FINEST)) {
    		logger.entering(sourceClass, "createAuthorizationHeader", new Object[] { });
        }
		
		if (logger.isLoggable(Level.FINEST)) {
    		logger.log(Level.FINEST, "Security Header :" + "Bearer "+ accessToken);
    	}
		return "Bearer "+ accessToken;
	}

	protected void setOAuthData(String responseBody) {
		accessToken = getTokenValue(responseBody, Configuration.OAUTH2_ACCESS_TOKEN);
		refreshToken = getTokenValue(responseBody, Configuration.OAUTH2_REFRESH_TOKEN);
		String issuedOnDate = getTokenValue(responseBody, Configuration.OAUTH2_ISSUEDON);
		try{
			issuedOn = new Date(Long.valueOf(issuedOnDate));
		}catch (Exception e) {}
		expiresIn = getTokenValue(responseBody, Configuration.OAUTH2_EXPIRESIN);
		tokenType = getTokenValue(responseBody, Configuration.OAUTH2_TOKENTYPE);
	}

    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }


	public String getClient_uri() {
		return client_uri;
	}

	public void setClient_uri(String client_uri) {
		this.client_uri = client_uri;
	}

	public String getAuthorization_code() {
		return authorization_code;
	}

	public void setAuthorization_code(String authorization_code) {
		this.authorization_code = authorization_code;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public AccessToken getAccessTokenObject() {
		return accessTokenObject;
	}

	public void setAccessTokenObject(AccessToken accessTokenObject) {
		this.accessTokenObject = accessTokenObject;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(Date issuedOn) {
		this.issuedOn = issuedOn;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expireIn) {
		this.expiresIn = expireIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@Override
	public String getAuthType() {
		return Configuration.AUTH_TYPE_OAUTH2;
	}
	
	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	
	public void setAuthorizationURL(String authorizationURL) {
		this.authorizationURL = authorizationURL;
	}

	@Override
	public String getAuthorizationURL() {
		return authorizationURL;
	}
	
	public String getAccessTokenURL() {
		return accessTokenURL;
	}

	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}
	

    private void readConsumerToken() throws OAuthException {
        if(!storeRead) {
        	try {
	        	CredentialStore factory = CredentialStoreFactory.getCredentialStore(getCredentialStore());
	            if(factory!=null) {
	            	ConsumerToken consumerToken = (ConsumerToken) factory.load(getServiceName(), CONSUMER_TOKEN_STORE_TYPE, null);
	                if(consumerToken!=null) {
	                	storeRead = true;
	                    if(StringUtil.isNotEmpty(consumerToken.getConsumerKey())) {
	                        setConsumerKey(consumerToken.getConsumerKey());
	                    }
	                    if(StringUtil.isNotEmpty(consumerToken.getConsumerSecret())) {
	                        setConsumerSecret(consumerToken.getConsumerSecret());
	                    }
	                    if(StringUtil.isNotEmpty(consumerToken.getAuthorizationUri())) {
	                        setAuthorizationURL(consumerToken.getAuthorizationUri());
	                    }
	                    if(StringUtil.isNotEmpty(consumerToken.getAccessTokenUri())) {
	                        setAccessTokenURL(consumerToken.getAccessTokenUri());
	                    }                   
	                }
	            }
        	} catch (CredentialStoreException cse) {
					throw new OAuthException(cse, cse.getMessage());
			}
        }
    }

	public void setCredentialStore(String credentialStore) {
		this.credentialStore = credentialStore;
	}

	public String getCredentialStore() {
		return credentialStore;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	
	//Persistance related code
	
    
    public AccessToken acquireToken() throws OAuthException {
        return acquireToken(false);
    }

    public AccessToken acquireToken(boolean login) throws OAuthException {
        return acquireToken(login, false);
    }
    public AccessToken acquireToken(boolean login, boolean force) throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerAcquireToken, "");
            long ts = Profiler.getCurrentTime();
            try {
                return _acquireToken(login, force);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            return _acquireToken(login, force);
        }
    }
    
    /*
     * This method searches for existing token in store.
     * It can also conditionally trigger the OAuth2 Dance to procure new tokens
     * When parameter force is True, we reperform the Oauth Dance.
     * When login is True, we reperform the Oauth dance only when tokens are not available in store or bean
     */
    
    public AccessToken _acquireToken(boolean login, boolean force) throws OAuthException {
    	Context context = Context.get();
    	AccessToken tk;
    	
        // If force is used, then login must be requested
        if(force) {
            login = true;
        }

        String userId = getUserId();

        // Look for a token in the store
    	// If the user is anonymous, then the token might had been stored in the session
        if(!force) {
        	
        	if (getAccessTokenObject() != null) {
    			// read from the local bean if accesstoken is present
    			tk = getAccessTokenObject();
    		}else{
            	tk = context.isCurrentUserAnonymous() 
    			? (AccessToken)AnonymousCredentialStore.loadCredentials(context,getAppId(),getServiceName()) 
    			: findTokenFromStore(context, userId);
    		}

        	// check if token needs to be renewed
            if(tk!=null) {
                if(shouldRenewToken(tk)) { //based on expiration date, check if token needs to be renewed.
                    return renewToken(tk); 
                }
                return tk;
            }
        }
    	if(login) {
    		deleteToken();
    		setAccessTokenObject(null);
    		performOAuth2Dance();
    	}
        return null; 
    }
    
    protected AccessToken findTokenFromStore(Context context, String userId) throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerLoadCredentialStore, "");
            long ts = Profiler.getCurrentTime();
            try {
                return _findTokenFromStore(context, userId);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            return _findTokenFromStore(context, userId);
        }
    }
    protected AccessToken _findTokenFromStore(Context context, String userId) throws OAuthException {
    	readConsumerToken();
    	
        if(StringUtil.isEmpty(userId)) {
            userId = getUserId();
            // Anonymous is not valid
            if(StringUtil.isEmpty(userId)) {
                return null;
            }
        }
        try {
	        CredentialStore credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
	        if(credStore!=null) {
	            // Find the token for this user
	            AccessToken token = (AccessToken) credStore.load(getServiceName(), ACCESS_TOKEN_STORE_TYPE, userId);
	            if(token!=null) {
	                return token;
	            }
	        }
        } catch (CredentialStoreException cse) {
				throw new OAuthException(cse, "Error finding credentials from the store");
		}
        return null;
    }
    
    
    // ==========================================================
    //  Delete token
    // ==========================================================
    
    public void deleteToken() throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerDeleteToken, "");
            long ts = Profiler.getCurrentTime();
            try {
                _deleteToken(Context.get(), null);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            _deleteToken(Context.get(), null);
        }
    }
    protected void _deleteToken(Context context, String userId) throws OAuthException {
    	readConsumerToken();
    	
        if(StringUtil.isEmpty(userId)) {
            userId = getUserId();
            if(StringUtil.isEmpty(userId)) {
                return;
            }
            
        }
        if(StringUtil.equals(userId, "anonymous"))
		{
        	AnonymousCredentialStore.deleteCredentials(context, getAppId(),getServiceName()); 
		}
        else {
        	 try {
     	        CredentialStore credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
     	        if(credStore!=null) {
     	        	// Find the token for this user
     	        	credStore.remove(getServiceName(), ACCESS_TOKEN_STORE_TYPE, getUserId());
     	        }
        	 } catch (CredentialStoreException cse) {
 				throw new OAuthException(cse, "Error trying to delete Token.");
 			}
        }
    }
    
    //  Renew the token
    // ==========================================================

    public AccessToken renewToken() throws OAuthException {
        return renewToken(null);
    }

    public AccessToken renewToken(AccessToken token) throws OAuthException {
        if (Profiler.isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerRenewToken, "");
            long ts = Profiler.getCurrentTime();
            try {
                return _renewToken(token);
            } finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } else {
            return _renewToken(token);
        }
    }
    
    /*
     * This method uses the existing refresh token and renews the access token from access token url
     */
    protected AccessToken _renewToken(AccessToken token) throws OAuthException {
    		HttpGet method = null;
    		AccessToken renewedtoken = null;
    		int responseCode = HttpStatus.SC_OK;
    		String responseBody = null;
    		InputStream content = null;
    		try {
    			HttpClient client = new DefaultHttpClient();
    			if(forceTrustSSLCertificate)
    				client = (DefaultHttpClient)SSLUtil.wrapHttpClient((DefaultHttpClient)client);
    			StringBuilder url = new StringBuilder();
    			url.append(getAccessTokenURL()).append('?');
    			url.append(Configuration.OAUTH2_CLIENT_ID);
    			url.append('=');
    			url.append(URLEncoder.encode(token.getConsumerKey(), "UTF-8"));
    			url.append('&');
    			url.append(Configuration.OAUTH2_CLIENT_SECRET);
    			url.append('=');
    			url.append(URLEncoder.encode(token.getTokenSecret(), "UTF-8"));
    			url.append('&');
    			url.append(Configuration.OAUTH2_GRANT_TYPE);
    			url.append('=');
    			url.append(Configuration.OAUTH2_REFRESH_TOKEN);
    			url.append('&');
    			url.append(Configuration.OAUTH2_REFRESH_TOKEN);
    			url.append('=');
    			url.append(URLEncoder.encode(token.getRefreshToken(), "UTF-8"));
    			method = new HttpGet(url.toString());
    			HttpResponse httpResponse =client.execute(method);
    			responseCode = httpResponse.getStatusLine().getStatusCode();
    			content = httpResponse.getEntity().getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			try {
    				responseBody = StreamUtil.readString(reader);
    			} finally {
    				StreamUtil.close(reader);
    			}
    		} catch (Exception e) {
    			throw new OAuthException(e ,"refreshAccessToken failed with Exception: <br>" + e);
    		} finally {
    			if (method != null){
					try {
						if(content!=null) {
							content.close();
						}
					} catch (IOException e) {
						throw new OAuthException(e ,"refreshAccessToken failed with Exception: <br>" + e);
					}
    			}
    		}
    		if (responseCode != HttpStatus.SC_OK) {
    				acquireToken(true, true); // Failed to renew token, get a new one
    	            return null;
    		} else {
					setOAuthData(responseBody);
					renewedtoken = createToken(getAppId(),getServiceName()); // Now create a new token and save that in the store    		
					Context context = Context.get();
					setAccessTokenObject(renewedtoken);
					try {
			        	if(!context.isCurrentUserAnonymous()) {
			        		CredentialStore credStore = findCredentialStore();
			        		if (credStore != null) {
								// if the token is already present, and was expired due to which we have fetched a new 
								// token, then we remove the token from the store first and then add this new token.
								deleteToken();
								credStore.store(getServiceName(), ACCESS_TOKEN_STORE_TYPE, getUserId(), token);
							}
			            } else {
			            	AnonymousCredentialStore.storeCredentials(context, token, getAppId(), getServiceName()); // Store the token for anonymous user
			            }
					} catch (CredentialStoreException cse) {
						throw new OAuthException(cse, "Error trying to renew Token.");
					}
				} 
    		return renewedtoken;
    }
    
    public CredentialStore findCredentialStore() throws OAuthException {
    	CredentialStore credStore = null;
		try {
			credStore = CredentialStoreFactory.getCredentialStore(getCredentialStore());
		} catch (CredentialStoreException cse) {
			throw new OAuthException(cse, "Error finding credentials from the store");
		}
		return credStore;
    }

	/*
	 *  This method starts the Oauth2.0 Dance process. 
	 *  1. Make the call to fetch the authorization token.
	 *  2. Host server redirect to OA2Callback.
	 *  3. OA2Callback would try to fetch the access and refresh tokens.
	 *  4. Interceptor from endpoint then takes care of inserting the required security headers 
	 */
	public synchronized void performOAuth2Dance(){
		
		setApplicationPage();
		Context context = Context.get();
		
		String callbackurl="";
		try {
			callbackurl = getCallbackUrl(context);
		} catch (OAuthException e1) {
			Platform.getInstance().log(e1);
		}
		setClient_uri(callbackurl);
		
		// Store the Oauth handler in session object
		context.getSessionMap().put(Configuration.OAUTH2_HANDLER, this);
		
		Object resp = Context.get().getHttpResponse();
		try {
				Context.get().sendRedirect(getAuthorizationNetworkUrl());
		} catch (Exception e) {
			Platform.getInstance().log(e);
		}
	}
	
	
	/*
	 * This method sets the reference of page which initiated the secured call.
	 * We need this to eventually navigate back to calling page
	 */
	
	public void setApplicationPage() {
    	// We just return to the same page
    	Object _req = Context.get().getHttpRequest();
    	if(_req instanceof HttpServletRequest) {
        	HttpServletRequest request = (HttpServletRequest)_req;
    		String url = UrlUtil.getRequestUrl(request);
    		this.applicationPage=url;
    	}
	}
	
	public String getApplicationPage() {
		return applicationPage;
	}
	
	
    public boolean shouldRenewToken() throws OAuthException {
        return shouldRenewToken(null); 
    }
    
    /*
     * This method checks if the active token has expired based on the isvalidupto parameter from OADance,
     * Stored as  Expires parameter in AccessToken
     */
    public boolean shouldRenewToken(AccessToken token) throws OAuthException {
        // We do not automatically renew/acquire it - we just get it from the store
       if(token==null) {
            token = _findTokenFromStore(Context.get(), null);
            if(token==null) {
                throw new OAuthException(null,"No user token is available");
            }
        }
        return token.isExpired(getExpireThreshold());
    }

	public void setExpireThreshold(int expireThreshold) {
		this.expireThreshold = expireThreshold;
	}

	public int getExpireThreshold() {
		return expireThreshold;
	}
	
	
	
	 
    // ==========================================================
    //  Utilities
    // ==========================================================
    /**
     * Read a token from an OAuthResponse
     * @param accessor
     * @param response
     * @return
     * @throws IOException
     * 
     * This method creates a new token
     */
    public AccessToken createToken(String appId, String serviceName) throws OAuthException {
    	try {
    		
    		
            long now = System.currentTimeMillis();
            String expirationinterval = getExpiresIn(); // we get the expires interval from server, convert this to a date object
            Date expiresIn = null;
            expiresIn = new Date(now+Long.parseLong(expirationinterval)*1000);
            return new AccessToken(
                        appId,
                        serviceName,
                        getConsumerKey(),
                        getAccessToken(),
                        getConsumerSecret(),
                        getUserId(),
                        expiresIn,
                        getRefreshToken()
            );
        } catch (Exception e) {
            throw new OAuthException(e);
        }
    }
    
    @Override
	public String getCallbackUrl(Context context) throws OAuthException {
    	Object _req = context.getHttpRequest();
    	if(_req instanceof HttpServletRequest) {
        	HttpServletRequest request = (HttpServletRequest)_req;
            String proxyBaseUrl = PathUtil.concat(ServiceUtil.getProxyUrl(request),OA2Callback.URL_PATH,'/');
            return proxyBaseUrl;
    	}
    	return null;
    }

	@Override
	public void doPreAuthorizationFlow() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPostAuthorizationFlow() throws Exception {
		// TODO Auto-generated method stub
		 
	}
	public boolean isForceTrustSSLCertificate() {
		return forceTrustSSLCertificate;
	}

	public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
		this.forceTrustSSLCertificate = forceTrustSSLCertificate;
	}

}
