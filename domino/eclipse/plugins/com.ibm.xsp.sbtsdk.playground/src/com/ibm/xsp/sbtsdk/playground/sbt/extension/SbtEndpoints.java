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
import nsf.playground.extension.Endpoints.Property;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
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
		new Category("Endpoints", new Property[] {
				new Property("Endpoint_Connections", "Endpoint_Connections"),
				new Property("Endpoint_Smartcloud", "Endpoint_Smartcloud"),
				new Property("Endpoint_Domino", "Endpoint_Domino"),
		}),
		new Category("Connections", new Property[] {
				new Property("Con_URL", "Con_URL"),
				new Property("Con_OA2_ConsumerKey", "Con_OA2_ConsumerKey"),
				new Property("Con_OA2_ConsumerSecret", "Con_OA2_ConsumerSecret"),
				new Property("Con_OA2_AuthorizationURL", "Con_OA2_AuthorizationURL"),
				new Property("Con_OA2_AccessTokenURL", "Con_OA2_AccessTokenURL"),
		}),
		new Category("SmartCloud", new Property[] {
				new Property("Sma_URL", "Sma_URL"),
				new Property("Sma_OA_ConsumerKey", "Sma_OA_ConsumerKey"),
				new Property("Sma_OA_ConsumerSecret", "Sma_OA_ConsumerSecret"),
				new Property("Sma_OA_RequestTokenURL", "Sma_OA_RequestTokenURL"),
				new Property("Sma_OA_AuthorizationURL", "Sma_OA_AuthorizationURL"),
				new Property("Sma_OA_AccessTokenURL", "Sma_OA_AccessTokenURL"),
				new Property("Sma_OA2_ConsumerKey", "Sma_OA2_ConsumerKey"),
				new Property("Sma_OA2_ConsumerSecret", "Sma_OA2_ConsumerSecret"),
				new Property("Sma_OA2_AuthorizationURL", "Sma_OA2_AuthorizationURL"),
				new Property("Sma_OA2_AccessTokenURL", "Sma_OA2_AccessTokenURL"),
		}),
		new Category("Domino", new Property[] {
				new Property("Dom_URL", "Dom_URL"),
		}),
		new Category("Sametime", new Property[] {
				new Property("St_URL", "St_URL"),
		}),
		new Category("Twitter", new Property[] {
				new Property("Twitter_OA_ConsumerKey", "Twitter_OA_ConsumerKey"),
				new Property("Twitter_OA_ConsumerSecret", "Twitter_OA_ConsumerSecret"),
		}),
	};

    public SbtEndpoints() {
	}

	public Category[] getPropertyList() {
		return PROPERTIES;
	}
    
    @Override
	public void prepareEndpoints(PlaygroundEnvironment env) {
		FacesContextEx context = FacesContextEx.getCurrentInstance();
		
		// Set the properties for the default endpoints
		String endpointConnections = env.getField("Endpoint_Connections");
		if(StringUtil.isEmpty(endpointConnections)) {endpointConnections = "connections";}
		context.setSessionProperty("sbt.endpoint.connections", endpointConnections);
		
		String endpointSmartcloud = env.getField("Endpoint_Smartcloud");
		if(StringUtil.isEmpty(endpointSmartcloud)) {endpointSmartcloud = "smartcloud";}
		context.setSessionProperty("sbt.endpoint.smartcloud", endpointSmartcloud);
		
		String endpointDomino = env.getField("Endpoint_Domino");
		if(StringUtil.isEmpty(endpointDomino)) {endpointDomino = "domino";}
		context.setSessionProperty("sbt.endpoint.domino", endpointDomino);
		
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
