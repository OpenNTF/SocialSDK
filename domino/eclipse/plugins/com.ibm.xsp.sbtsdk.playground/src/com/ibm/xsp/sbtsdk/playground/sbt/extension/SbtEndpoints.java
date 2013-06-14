/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corp. 2004, 2011                                    */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has been      */
/* deposited with the U.S. Copyright Office.                         */
/*                                                                   */
/* ***************************************************************** */

package com.ibm.xsp.sbtsdk.playground.sbt.extension;

import nsf.playground.environments.PlaygroundEnvironment;
import nsf.playground.extension.Endpoints;

import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.DominoBasicEndpoint;
import com.ibm.sbt.services.endpoints.OAuthEndpoint;
import com.ibm.sbt.services.endpoints.SametimeBasicEndpoint;
import com.ibm.sbt.services.endpoints.SmartCloudOAuth2Endpoint;
import com.ibm.sbt.services.endpoints.SmartCloudOAuthEndpoint;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.util.ManagedBeanUtil;



/**
 * Endpoints used by the SBT.
 */
public class SbtEndpoints extends Endpoints {

	public static final SbtEndpoints instance = new SbtEndpoints();
	
	private static final Category[] PROPERTIES = new Category[] {
		new Category("Connections Server", new Property[] {
				new Property("Con_URL", "URL"),
				new Property("Con_OA2_ConsumerKey", "OAuth2 - Consumer Key"),
				new Property("Con_OA2_ConsumerSecret", "OAuth2 - Consumer Secret"),
				new Property("Con_OA2_AuthorizationURL", "OAuth2 - Authorization URL"),
				new Property("Con_OA2_AccessTokenURL", "OAuth2 - Access Token URL"),
		}),
		new Category("SmartCloud Server", new Property[] {
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
		}),
		new Category("Domino Server", new Property[] {
				new Property("Dom_URL", "URL"),
		}),
/*		
		new Category("Sametime", new Property[] {
				new Property("St_URL", "URL"),
		}),
*/		
		new Category("Social Networks", new Property[] {
				//new Property("Twitter_URL", "Twitter URL"),
				new Property("Twitter_OA_ConsumerKey", "Twitter Consumer Key"),
				new Property("Twitter_OA_ConsumerSecret", "Twitter Consumer Secret"),
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
		return "connections,connectionsOA2,smartcloud,smartcloudOA2,sametime,domino,watson";
	}
    
    @Override
	public void prepareEndpoints(PlaygroundEnvironment env) {
    	FacesContextEx context = FacesContextEx.getCurrentInstance();
		
    	pushProperty(context,env,"sbt.endpoint.connections");
    	pushProperty(context,env,"sbt.endpoint.smartcloud");
    	pushProperty(context,env,"sbt.endpoint.domino");
    	pushProperty(context,env,"sbt.endpoint.sametime");
    	
		// Override the beans with the environment definition
		{
			ConnectionsBasicEndpoint ep = (ConnectionsBasicEndpoint)ManagedBeanUtil.getBean(context, "connections");
			if(ep!=null) {
				ep.setUrl(env.getField("Con_URL"));
			}
		}
		{
			ConnectionsOAuth2Endpoint ep = (ConnectionsOAuth2Endpoint)ManagedBeanUtil.getBean(context, "connectionsOA2");
			if(ep!=null) {
				ep.setUrl(env.getField("Con_URL"));
				ep.setConsumerKey(env.getField("Con_OA2_ConsumerKey"));
				ep.setConsumerSecret(env.getField("Con_OA2_ConsumerSecret"));
				ep.setAuthorizationURL(env.getField("Con_OA2_AuthorizationURL"));
				ep.setAccessTokenURL(env.getField("Con_OA2_AccessTokenURL"));
			}
		}
		{
			SmartCloudOAuthEndpoint ep = (SmartCloudOAuthEndpoint)ManagedBeanUtil.getBean(context, "smartcloud");
			if(ep!=null) {
				ep.setUrl(env.getField("Sma_URL"));
				ep.setConsumerKey(env.getField("Sma_OA_ConsumerKey"));
				ep.setConsumerSecret(env.getField("Sma_OA_ConsumerSecret"));
				ep.setRequestTokenURL(env.getField("Sma_OA_RequestTokenURL"));
				ep.setAuthorizationURL(env.getField("Sma_OA_AuthorizationURL"));
				ep.setAccessTokenURL(env.getField("Sma_OA_AccessTokenURL"));
			}
		}
		{
			SmartCloudOAuth2Endpoint ep = (SmartCloudOAuth2Endpoint)ManagedBeanUtil.getBean(context, "smartcloudOA2");
			if(ep!=null) {
				ep.setUrl(env.getField("Sma_URL"));
				ep.setConsumerKey(env.getField("Sma_OA2_ConsumerKey"));
				ep.setConsumerSecret(env.getField("Sma_OA2_ConsumerSecret"));
				ep.setAuthorizationURL(env.getField("Sma_OA2_AuthorizationURL"));
				ep.setAccessTokenURL(env.getField("Sma_OA2_AccessTokenURL"));
			}
		}
		{
			DominoBasicEndpoint ep = (DominoBasicEndpoint)ManagedBeanUtil.getBean(context, "domino");
			if(ep!=null) {
				ep.setUrl(env.getField("Dom_URL"));
			}
		}
		{
			SametimeBasicEndpoint ep = (SametimeBasicEndpoint)ManagedBeanUtil.getBean(context, "sametime");
			if(ep!=null) {
				ep.setUrl(env.getField("St_URL"));
			}
		}
		{
			// Temporarily use OAuth endpoint
			OAuthEndpoint ep = (OAuthEndpoint)ManagedBeanUtil.getBean(context, "twitter");
			if(ep!=null) {
				ep.setConsumerKey(env.getField("Twitter_OA_ConsumerKey"));
				ep.setConsumerSecret(env.getField("Twitter_OA_ConsumerSecret"));
			}
		}
	}    
}
