/*
 * � Copyright IBM Corp. 2013
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
import com.ibm.sbt.services.endpoints.SmartCloudOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.SmartCloudOAuthEndpoint;
import com.ibm.sbt.services.endpoints.TwitterOAuthEndpoint;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.util.ManagedBeanUtil;



/**
 * Endpoints used by the SBT.
 */
public class SbtEndpoints extends Endpoints {

	public static final SbtEndpoints instance = new SbtEndpoints();
	
	private static final Category[] PROPERTIES = new Category[] {
		new Category("connections","IBM Connections on Premises","IBM Connections", new Property[] {
				new Property("Con_URL", "URL"),
				new Property("Con_OA2_ConsumerKey", "OAuth2 - Consumer Key"),
				new Property("Con_OA2_ConsumerSecret", "OAuth2 - Consumer Secret"),
				new Property("Con_OA2_AuthorizationURL", "OAuth2 - Authorization URL"),
				new Property("Con_OA2_AccessTokenURL", "OAuth2 - Access Token URL"),
		}, new Group[] {
				new Group("IBM Connections using Basic Authentication", new String[] {"Con_URL"}),
				new Group("IBM Connections using OAuth 2", new String[] {"Con_URL","Con_OA2_ConsumerKey","Con_OA2_ConsumerSecret","Con_OA2_AuthorizationURL","Con_OA2_AccessTokenURL"}),
		}),
		new Category("smartcloud","IBM SmartCloud for Social Business", "IBM SmartCloud", new Property[] {
				new Property("Sma_URL", "URL"),
				new Property("Sma_OA_ConsumerKey", "Consumer Key"),
				new Property("Sma_OA_ConsumerSecret", "OAuth1 - Consumer Secret"),
				new Property("Sma_OA_RequestTokenURL", "OAuth1 - Request Token URL"),
				new Property("Sma_OA_AuthorizationURL", "OAuth1 - Authorization URL"),
				new Property("Sma_OA_AccessTokenURL", "OAuth1 - AccessToken URL"),
				new Property("Sma_OA2_ConsumerKey", "OAuth2 - Consumer Key"),
				new Property("Sma_OA2_ConsumerSecret", "OAuth2 - Consumer Secret"),
				new Property("Sma_OA2_AuthorizationURL", "OAuth2 - Authorization URL"),
				new Property("Sma_OA2_AccessTokenURL", "OAuth2 - Access Token URL"),
		}, new Group[] {
				new Group("IBM Connections using OAuth 1", new String[] {"Sma_URL","Sma_OA_ConsumerKey","Sma_OA_ConsumerSecret","Sma_OA_RequestTokenURL","Sma_OA_AuthorizationURL","Sma_OA_AccessTokenURL"}),
				new Group("IBM Connections using OAuth 2", new String[] {"Sma_URL","Sma_OA2_ConsumerKey","Sma_OA2_ConsumerSecret","Sma_OA2_AuthorizationURL","Sma_OA2_AccessTokenURL"}),
		}),
		new Category("domino","IBM Domino", "IBM Domino", new Property[] {
				new Property("Dom_URL", "URL"),
		}, new Group[] {
				//new Group("IBM Domino using Basic Authentication", new String[] {"Dom_URL"}),
		}),
/*		
		new Category("sametime","Sametime", new Property[] {
				new Property("St_URL", "URL"),
		}),
*/		
		new Category("twitter","Twitter","Twitter",  new Property[] {
				//new Property("Twitter_URL", "Twitter URL"),
				new Property("Twitter_OA_AppplicationAccessToken", "Twitter Application Access Token"),
				new Property("Twitter_OA_ConsumerKey", "Twitter Consumer Key"),
				new Property("Twitter_OA_ConsumerSecret", "Twitter Consumer Secret"),
		}, new Group[] {
				
		}),
		new Category("dropbox","Dropbox","Dropbox", new Property[] {
				new Property("Dropbox_OA_ConsumerKey", "Dropbox Consumer Key"),
				new Property("Dropbox_OA_ConsumerSecret", "Dropbox Consumer Secret"),
		}, new Group[] {
				
		}),
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
