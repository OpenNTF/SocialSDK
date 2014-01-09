/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.xsp.sbtsdk.playground.sbt.extension;

import nsf.playground.environments.PlaygroundEnvironment;
import nsf.playground.extension.Endpoints;

import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.DominoBasicEndpoint;
import com.ibm.sbt.services.endpoints.DropBoxOAuthEndpoint;
import com.ibm.sbt.services.endpoints.SametimeBasicEndpoint;
import com.ibm.sbt.services.endpoints.SmartCloudBasicEndpoint;
import com.ibm.sbt.services.endpoints.SmartCloudOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.SmartCloudOAuthEndpoint;
import com.ibm.sbt.services.endpoints.TwitterOAuthEndpoint;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.util.ManagedBeanUtil;



/**
 * Endpoints used by the SBT.
 */
public class SbtEndpoints extends Endpoints {
	
	public static final String CON_URL = "Con_URL";
	public static final String CON_OA2_CONSUMERKEY = "Con_OA2_ConsumerKey";
	public static final String CON_OA2_CONSUMERSECRET = "Con_OA2_ConsumerSecret";
	public static final String DEFAULT_CONNECTIONS_SERVICE_NAME = "connections";
	public static final String CON_GADGET_OA2_SERVICE_NAME = "Con_Gadget_OA2_Service_Name";
	public static final String CON_GADGET_OA2_CONSUMERKEY = "Con_Gadget_OA2_ConsumerKey";
	public static final String CON_GADGET_OA2_CONSUMERSECRET = "Con_Gadget_OA2_ConsumerSecret";
	public static final String CON_OA2_AUTHORIZATIONURL = "Con_OA2_AuthorizationURL";
	public static final String CON_OA2_ACCESSTOKENURL = "Con_OA2_AccessTokenURL";
	public static final String DEFAULT_GOOGLE_SERVICE_NAME = "googleAPI";
	public static final String GOOGLE_GADGET_OA_SERVICE_NAME = "Google_Gadget_OA_Service_Name";
	public static final String GOOGLE_GADGET_OA_CONSUMERKEY = "Google_Gadget_OA_ConsumerKey";
	public static final String GOOGLE_GADGET_OA_CONSUMERSECRET = "Google_Gadget_OA_ConsumerSecret";
	public static final String SMA_URL = "Sma_URL";
	public static final String DEFAULT_SC_SERVICE_NAME = "SmartCloud";
	public static final String SMA_OA_GADGET_SERVICE = "Sma_OA_Gadget_Service";
	public static final String SMA_OA_COUNSUMERKEY = "Sma_OA_ConsumerKey";
	public static final String SMA_OA_CONSUMERSECRET = "Sma_OA_ConsumerSecret";
	public static final String DROPBOX_GADGET_OA_SERVICE_NAME = "DropBox_Gadget_OA_Service_Name";
	public static final String DROPBOX_OA_CONSUMERKEY = "Dropbox_OA_ConsumerKey";
	public static final String DROPBOX_OA_CONSUMERSECRET = "Dropbox_OA_ConsumerSecret";
	public static final String DEFAULT_DROPBOX_SERVICE_NAME = "DropBox";

	private Category[] PROPERTIES = new Category[] {
		new Category("connections","IBM Connections on Premises","IBM Connections", new Property[] {
				new Property(CON_URL, "URL", "https://[myconnections-server]"),
				new Property(CON_OA2_CONSUMERKEY, "OAuth2 - Consumer Key"),
				new Property(CON_OA2_CONSUMERSECRET, "OAuth2 - Consumer Secret"),
				new Property(CON_GADGET_OA2_SERVICE_NAME, "OpenSocial Gadget OAuth2 Service Name", DEFAULT_CONNECTIONS_SERVICE_NAME),
				new Property(CON_GADGET_OA2_CONSUMERKEY, "OpenSocial Gadget OAuth2 - Consumer Key"),
				new Property(CON_GADGET_OA2_CONSUMERSECRET, "OpenSocial Gadget OAuth2 - Consumer Secret"),
				new Property(CON_OA2_AUTHORIZATIONURL, "OAuth2 - Authorization URL", "https://[myconnections-server]/oauth2/endpoint/connectionsProvider/authorize"),
				new Property(CON_OA2_ACCESSTOKENURL, "OAuth2 - Access Token URL", "https://[myconnections-server]/oauth2/endpoint/connectionsProvider/token"),
		}, new Group[] {
				new Group("Basic Authentication", new String[] {"Con_URL"}, "# Connections default is Basic Auth\n\nsbt.endpoint.connections=connectionsBasic\n"),
				new Group("OAuth 2", new String[] {CON_URL,CON_OA2_CONSUMERKEY,CON_OA2_CONSUMERSECRET,CON_GADGET_OA2_SERVICE_NAME,CON_GADGET_OA2_CONSUMERKEY,CON_GADGET_OA2_CONSUMERSECRET,CON_OA2_AUTHORIZATIONURL,CON_OA2_ACCESSTOKENURL}, "# Connections default is OAuth 2\nsbt.endpoint.connections=connectionsOA2\n", 1),
		}, null),
		new Category("smartcloud","IBM SmartCloud for Social Business", "IBM SmartCloud", new Property[] {
				new Property(SMA_URL, "URL", "https://apps.na.collabserv.com"),
				new Property(SMA_OA_GADGET_SERVICE, "OAuth Service Name For OpenSocial Gadgets", DEFAULT_SC_SERVICE_NAME),
				new Property(SMA_OA_COUNSUMERKEY, "OAuth1 - Consumer Key"),
				new Property(SMA_OA_CONSUMERSECRET, "OAuth1 - Consumer Secret"),
				new Property("Sma_OA_RequestTokenURL", "OAuth1 - Request Token URL", "https://apps.na.collabserv.com/manage/oauth/getRequestToken"),
				new Property("Sma_OA_AuthorizationURL", "OAuth1 - Authorization URL", "https://apps.na.collabserv.com/manage/oauth/authorizeToken"),
				new Property("Sma_OA_AccessTokenURL", "OAuth1 - AccessToken URL", "https://apps.na.collabserv.com/manage/oauth/getAccessToken"),
				new Property("Sma_OA2_ConsumerKey", "OAuth2 - Consumer Key"),
				new Property("Sma_OA2_ConsumerSecret", "OAuth2 - Consumer Secret"),
				new Property("Sma_OA2_AuthorizationURL", "OAuth2 - Authorization URL", "https://apps.na.collabserv.com/manage/oauth2/authorize"),
				new Property("Sma_OA2_AccessTokenURL", "OAuth2 - Access Token URL", "https://apps.na.collabserv.com/manage/oauth2/token"),
		}, new Group[] {
				new Group("Basic Authentication", new String[] {"Sma_URL"}, "# Make SmartCloud Basic the default server for Connections\nsbt.endpoint.connections=smartcloudBasic\nsbt.endpoint.smartcloud=smartcloudBasic"),
				new Group("OAuth 1", new String[] {SMA_URL,SMA_OA_GADGET_SERVICE,SMA_OA_COUNSUMERKEY,SMA_OA_CONSUMERSECRET,"Sma_OA_RequestTokenURL","Sma_OA_AuthorizationURL","Sma_OA_AccessTokenURL"}, "# Make SmartCloud OAuth 1 the default server for Connections\nsbt.endpoint.connections=smartcloudOA\nsbt.endpoint.smartcloud=smartcloudOA"),
				new Group("OAuth 2", new String[] {SMA_URL,"Sma_OA2_ConsumerKey","Sma_OA2_ConsumerSecret","Sma_OA2_AuthorizationURL","Sma_OA2_AccessTokenURL"}, "# Make SmartCloud OAuth 2 the default server for Connections\nsbt.endpoint.connections=smartcloudOA2\nsbt.endpoint.smartcloud=smartcloudOA2",1),
		}, null,
		new PropertyValues[] {
			   new PropertyValues("Test - C1", new String[] {
					"https://apps.na.collabservtest.lotus.com", // Sma_URL
					"", // Sma_OA_ConsumerKey
					"", // Sma_OA_ConsumerSecret
					"https://apps.na.collabservtest.lotus.com/manage/oauth/getRequestToken", // Sma_OA_RequestTokenURL
					"https://apps.na.collabservtest.lotus.com/manage/oauth/authorizeToken", // Sma_OA_AuthorizationURL
					"https://apps.na.collabservtest.lotus.com/manage/oauth/getAccessToken", // Sma_OA_AccessTokenURL
					"", // Sma_OA2_ConsumerKey
					"", // Sma_OA2_ConsumerSecret
					"https://apps.na.collabservtest.lotus.com/manage/oauth2/authorize", // Sma_OA2_AuthorizationURL
					"https://apps.na.collabservtest.lotus.com/manage/oauth2/token" // Sma_OA2_AccessTokenURL
			   })
		}),
		new Category("domino","IBM Domino", "IBM Domino", new Property[] {
				new Property("Dom_URL", "URL"),
		}, new Group[] {
				new Group("Basic Authentication", new String[] {"Dom_URL"}, null),
		}, null),
/*		
		new Category("sametime","Sametime", new Property[] {
				new Property("St_URL", "URL"),
		}, null),
*/		
		new Category("twitter","Twitter","Twitter",  new Property[] {
				//new Property("Twitter_URL", "Twitter URL"),
				new Property("Twitter_OA_AppplicationAccessToken", "Twitter Application Access Token"),
				new Property("Twitter_OA_ConsumerKey", "Twitter Consumer Key"),
				new Property("Twitter_OA_ConsumerSecret", "Twitter Consumer Secret"),
		}, new Group[] {
				new Group("OAuth", new String[] {"Twitter_OA_AppplicationAccessToken","Twitter_OA_ConsumerKey","Twitter_OA_ConsumerSecret"}, null, 1),
		}, null),
		new Category("dropbox","Dropbox","Dropbox", new Property[] {
				new Property(DROPBOX_GADGET_OA_SERVICE_NAME, "OpenSocial Gadgets OAuth 1.0a Service Name",
						DEFAULT_DROPBOX_SERVICE_NAME),
				new Property(DROPBOX_OA_CONSUMERKEY, "Dropbox Consumer Key"),
				new Property(DROPBOX_OA_CONSUMERSECRET, "Dropbox Consumer Secret"),
		}, new Group[] {
				new Group("OAuth", new String[] {DROPBOX_GADGET_OA_SERVICE_NAME, DROPBOX_OA_CONSUMERKEY, DROPBOX_OA_CONSUMERSECRET}, null),
		}, null),
		new Category("google","Google","Google", new Property[] {
				new Property(GOOGLE_GADGET_OA_SERVICE_NAME, "OpenSocial Gadgets OAuth2 Service Name", DEFAULT_GOOGLE_SERVICE_NAME),
				new Property(GOOGLE_GADGET_OA_CONSUMERKEY, "Consumer Key For OpenSocial Gadgets"),
				new Property(GOOGLE_GADGET_OA_CONSUMERSECRET, "Consumer Secret For OpenSocial Gadgets"),
		}, new Group[] {
				new Group("OAuth", new String[] {GOOGLE_GADGET_OA_SERVICE_NAME, GOOGLE_GADGET_OA_CONSUMERKEY, GOOGLE_GADGET_OA_CONSUMERSECRET}, null),
		}, null),
	};

    public SbtEndpoints() {
	}

    @Override
	public Category[] getPropertyList() {
		return PROPERTIES;
	}

	@Override
	public String getEndpointNames() {
		return "connections,connectionsOA2,smartcloud,smartcloudOA2,sametime,domino,watson,twitter,dropbox";
	}
    
    @Override
	public void prepareEndpoints(PlaygroundEnvironment env) {
    	FacesContextEx context = FacesContextEx.getCurrentInstance();
		
    	pushProperty(context,env,"sbt.endpoint.connections");
    	pushProperty(context,env,"sbt.endpoint.smartcloud");
    	pushProperty(context,env,"sbt.endpoint.domino");
    	pushProperty(context,env,"sbt.endpoint.sametime");
    	pushProperty(context,env,"sbt.endpoint.twitter");
    	pushProperty(context,env,"sbt.endpoint.dropbox");
    	
		// Override the beans with the environment definition
		{
			ConnectionsBasicEndpoint ep = (ConnectionsBasicEndpoint)ManagedBeanUtil.getBean(context, "connectionsBasic");
			if(ep!=null) {
				if(env.hasRuntime("connections")) {
					ep.setUrl(env.getField("Con_URL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			ConnectionsOAuth2Endpoint ep = (ConnectionsOAuth2Endpoint)ManagedBeanUtil.getBean(context, "connectionsOA2");
			if(ep!=null) {
				if(env.hasRuntime("connections")) {
					ep.setUrl(env.getField("Con_URL"));
					ep.setConsumerKey(env.getField("Con_OA2_ConsumerKey"));
					ep.setConsumerSecret(env.getField("Con_OA2_ConsumerSecret"));
					ep.setAuthorizationURL(env.getField("Con_OA2_AuthorizationURL"));
					ep.setAccessTokenURL(env.getField("Con_OA2_AccessTokenURL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			SmartCloudBasicEndpoint ep = (SmartCloudBasicEndpoint)ManagedBeanUtil.getBean(context, "smartcloudBasic");
			if(ep!=null) {
				if(env.hasRuntime("smartcloud")) {
					ep.setUrl(env.getField("Sma_URL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			SmartCloudOAuthEndpoint ep = (SmartCloudOAuthEndpoint)ManagedBeanUtil.getBean(context, "smartcloudOA");
			if(ep!=null) {
				if(env.hasRuntime("smartcloud")) {
					ep.setUrl(env.getField("Sma_URL"));
					ep.setConsumerKey(env.getField("Sma_OA_ConsumerKey"));
					ep.setConsumerSecret(env.getField("Sma_OA_ConsumerSecret"));
					ep.setRequestTokenURL(env.getField("Sma_OA_RequestTokenURL"));
					ep.setAuthorizationURL(env.getField("Sma_OA_AuthorizationURL"));
					ep.setAccessTokenURL(env.getField("Sma_OA_AccessTokenURL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			SmartCloudOAuth2Endpoint ep = (SmartCloudOAuth2Endpoint)ManagedBeanUtil.getBean(context, "smartcloudOA2");
			if(ep!=null) {
				if(env.hasRuntime("smartcloud")) {
					ep.setUrl(env.getField("Sma_URL"));
					ep.setConsumerKey(env.getField("Sma_OA2_ConsumerKey"));
					ep.setConsumerSecret(env.getField("Sma_OA2_ConsumerSecret"));
					ep.setAuthorizationURL(env.getField("Sma_OA2_AuthorizationURL"));
					ep.setAccessTokenURL(env.getField("Sma_OA2_AccessTokenURL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			DominoBasicEndpoint ep = (DominoBasicEndpoint)ManagedBeanUtil.getBean(context, "dominoBasic");
			if(ep!=null) {
				if(env.hasRuntime("domino")) {
					ep.setUrl(env.getField("Dom_URL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			SametimeBasicEndpoint ep = (SametimeBasicEndpoint)ManagedBeanUtil.getBean(context, "sametime");
			if(ep!=null) {
				if(env.hasRuntime("sametime")) {
					ep.setUrl(env.getField("St_URL"));
				} else {
					ep.setUrl(null);
				}
			}
		}
		{
			TwitterOAuthEndpoint epd = (TwitterOAuthEndpoint)ManagedBeanUtil.getBean(context, "twitter");
			if(epd!=null) {
				if(env.hasRuntime("twitter")) {
					epd.setApplicationAccessToken(env.getField("Twitter_OA_AppplicationAccessToken"));
					epd.setConsumerKey(env.getField("Twitter_OA_ConsumerKey"));
					epd.setConsumerSecret(env.getField("Twitter_OA_ConsumerSecret"));
				} else {
					epd.setUrl(null);
				}
			}
			DropBoxOAuthEndpoint ept = (DropBoxOAuthEndpoint)ManagedBeanUtil.getBean(context, "dropbox");
			if(ept!=null) {
				if(env.hasRuntime("dropbox")) {
					ept.setConsumerKey(env.getField("Dropbox_OA_ConsumerKey"));
					ept.setConsumerSecret(env.getField("Dropbox_OA_ConsumerSecret"));
				} else {
					ept.setUrl(null);
				}
			}
		}
	}    
}
