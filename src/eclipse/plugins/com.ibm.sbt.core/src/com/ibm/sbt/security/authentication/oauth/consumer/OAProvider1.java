
/* * © Copyright IBM Corp. 2012
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
 

package com.ibm.sbt.security.authentication.oauth.consumer;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.core.configuration.Configuration;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.security.credential.store.CredentialStore;
import com.ibm.sbt.security.credential.store.CredentialStoreException;
import com.ibm.sbt.security.credential.store.CredentialStoreFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OACallback;
import com.ibm.sbt.security.authentication.oauth.consumer.util.OADance;
import com.ibm.sbt.service.util.ServiceUtil;
import com.ibm.sbt.services.util.AnonymousCredentialStore;
import com.ibm.sbt.util.SBTException;

*//**
 * Encapsulate an OAuth service.
 * <p>
 * This holds both the application identifier and the URL to reach the service.
 * Not that it does not hold the token specific to a user.<br>
 * Such a service should be added as a bean, which can be application specific
 * or shared between all the applications.
 * </p>
 * 
 * @author Philippe Riand /////// TEMP //////// Sample values for Greenhouse
 *         (https://greenhouse.lotus.com/vulcan/security/provider/register.jsp?
 *         serviceProvider=vulcanToolkit) consumerKey:
 *         eecaba0d-136c-4677-a614-0b41612a2430 consumerSecret:
 *         IaL348_VDilHbgWSKm5Z64gvI0AIPWwObY0aVh1xKUx7m7VMrH65nnlxXfSU0vRzGqIjg8lHgP8etdlp8DuHGA
 *         requestTokenURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider
 *         /requestToken authorizationURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider/accessToken
 *         accessTokenURL:
 *         https://greenhouse.lotus.com:443/vulcan/security/provider/authorize
 *//*
public class OAProvider1 implements Serializable {

	private static final ProfilerType profilerLoadCredentialStore = new ProfilerType("OAuth: Load a token from the store"); //$NON-NLS-1$
	private static final ProfilerType profilerAcquireToken = new ProfilerType("OAuth: Acquire a token from the service"); //$NON-NLS-1$
	private static final ProfilerType profilerRenewToken = new ProfilerType("OAuth: Renew a token from the provider"); //$NON-NLS-1$
	private static final ProfilerType profilerDeleteToken = new ProfilerType("OAuth: Delete a token from the store"); //$NON-NLS-1$

	public static final int EXPIRE_THRESHOLD = 60; // 60sec = 1min
	// Store Type used to store the Tokens in the Credential Store
	public static final String ACCESS_TOKEN_STORE_TYPE = "OAUTH1_ACCESS_TOKEN_STORE";
	public static final String CONSUMER_TOKEN_STORE_TYPE = "OAUTH1_CONSUMER_TOKEN_STORE";
	
	// for logging
	private static final String sourceClass = OAProvider1.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	private static final long serialVersionUID = 1L;

	private boolean storeRead;
	private int expireThreshold;

	private String appId;
	private String serviceName;
	private String credentialStore;

	private String consumerKey;
	private String consumerSecret;
	private String requestTokenURL;
	private String authorizationURL;
	private String accessTokenURL;
	private String signatureMethod;
	private boolean forceTrustSSLCertificate;
	public String applicationAccessToken;

	
	public OAuthHandler oauthHandler = new OAuth1Handler(this);

	public OAProvider1() {
		this.expireThreshold = EXPIRE_THRESHOLD;
	}

	
}
*/