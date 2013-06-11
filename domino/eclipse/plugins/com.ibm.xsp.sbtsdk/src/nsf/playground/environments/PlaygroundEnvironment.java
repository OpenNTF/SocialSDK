package nsf.playground.environments;

import nsf.playground.beans.OptionsBean;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
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
 * This is an extended environment class holding extra playground specific information
 * @author priand
 *
 */
public class PlaygroundEnvironment extends SBTEnvironment {

	private String noteID;
	private String description;
	
	private String endpoint_Connections;
	private String endpoint_Smartcloud;
	private String endpoint_Domino;
	
	// Connections
	private String con_URL;
	private String con_OA2_ConsumerKey;
	private String con_OA2_ConsumerSecret;
	private String con_OA2_AuthorizationURL;
	private String con_OA2_AccessTokenURL;

	// Smartcloud
	private String sma_URL;
	private String sma_OA_ConsumerKey;
	private String sma_OA_ConsumerSecret;
	private String sma_OA_RequestTokenURL;
	private String sma_OA_AuthorizationURL;
	private String sma_OA_AccessTokenURL;
	private String sma_OA2_ConsumerKey;
	private String sma_OA2_ConsumerSecret;
	private String sma_OA2_AuthorizationURL;
	private String sma_OA2_AccessTokenURL;

	// Domino
	private String dom_URL;
	
	// Sametime
	private String st_URL;
	
	// Twitter
	private String twitter_OA_ConsumerKey;
	private String twitter_OA_ConsumerSecret;

	
	public PlaygroundEnvironment() {
		this(null,null);
	}

	public PlaygroundEnvironment(String name) {
		this(name,null);
	}
	
	public PlaygroundEnvironment(String name, Property[] properties) {
		super(name,
			  createEndpoints(),
			  properties);
	}
	
	private static SBTEnvironment.Endpoint[] createEndpoints() {
		String endpoints = OptionsBean.get().getEndpoints();
		String[] sp = StringUtil.splitString(endpoints, ',', true);
		SBTEnvironment.Endpoint[] result = new SBTEnvironment.Endpoint[sp.length]; 
		for(int i=0; i<sp.length; i++) {
			result[i] = new SBTEnvironment.Endpoint(sp[i],null); 
		}
		return result;
	}

	
	public String getNoteID() {
		return noteID;
	}
	public void setNoteID(String noteID) {
		this.noteID = noteID;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndpoint_Connections() {
		return endpoint_Connections;
	}

	public void setEndpoint_Connections(String endpoint_Connections) {
		this.endpoint_Connections=endpoint_Connections;
	}

	public String getEndpoint_Smartcloud() {
		return endpoint_Smartcloud;
	}

	public void setEndpoint_Smartcloud(String endpoint_Smartcloud) {
		this.endpoint_Smartcloud=endpoint_Smartcloud;
	}

	public String getEndpoint_Domino() {
		return endpoint_Domino;
	}

	public void setEndpoint_Domino(String endpoint_Domino) {
		this.endpoint_Domino=endpoint_Domino;
	}

	public String getCon_URL() {
		return con_URL;
	}
	public void setCon_URL(String con_URL) {
		this.con_URL = con_URL;
	}

	public String getCon_OA2_ConsumerKey() {
		return con_OA2_ConsumerKey;
	}
	public void setCon_OA2_ConsumerKey(String con_OA2_ConsumerKey) {
		this.con_OA2_ConsumerKey = con_OA2_ConsumerKey;
	}
	
	public String getCon_OA2_ConsumerSecret() {
		return con_OA2_ConsumerSecret;
	}
	public void setCon_OA2_ConsumerSecret(String con_OA2_ConsumerSecret) {
		this.con_OA2_ConsumerSecret = con_OA2_ConsumerSecret;
	}

	public String getCon_OA2_AuthorizationURL() {
		return con_OA2_AuthorizationURL;
	}
	public void setCon_OA2_AuthorizationURL(String con_OA2_AuthorizationURL) {
		this.con_OA2_AuthorizationURL = con_OA2_AuthorizationURL;
	}

	public String getCon_OA2_AccessTokenURL() {
		return con_OA2_AccessTokenURL;
	}
	public void setCon_OA2_AccessTokenURL(String con_OA2_AccessTokenURL) {
		this.con_OA2_AccessTokenURL = con_OA2_AccessTokenURL;
	}

	public String getSma_URL() {
		return sma_URL;
	}
	public void setSma_URL(String sma_URL) {
		this.sma_URL = sma_URL;
	}

	public String getSma_OA_ConsumerKey() {
		return sma_OA_ConsumerKey;
	}
	public void setSma_OA_ConsumerKey(String sma_OA_ConsumerKey) {
		this.sma_OA_ConsumerKey = sma_OA_ConsumerKey;
	}

	public String getSma_OA_ConsumerSecret() {
		return sma_OA_ConsumerSecret;
	}
	public void setSma_OA_ConsumerSecret(String sma_OA_ConsumerSecret) {
		this.sma_OA_ConsumerSecret = sma_OA_ConsumerSecret;
	}

	public String getSma_OA_RequestTokenURL() {
		return sma_OA_RequestTokenURL;
	}
	public void setSma_OA_RequestTokenURL(String sma_OA_RequestTokenURL) {
		this.sma_OA_RequestTokenURL = sma_OA_RequestTokenURL;
	}

	public String getSma_OA_AuthorizationURL() {
		return sma_OA_AuthorizationURL;
	}
	public void setSma_OA_AuthorizationURL(String sma_OA_AuthorizationURL) {
		this.sma_OA_AuthorizationURL = sma_OA_AuthorizationURL;
	}

	public String getSma_OA_AccessTokenURL() {
		return sma_OA_AccessTokenURL;
	}
	public void setSma_OA_AccessTokenURL(String sma_OA_AccessTokenURL) {
		this.sma_OA_AccessTokenURL = sma_OA_AccessTokenURL;
	}

	public String getSma_OA2_ConsumerKey() {
		return sma_OA2_ConsumerKey;
	}
	public void setSma_OA2_ConsumerKey(String sma_OA2_ConsumerKey) {
		this.sma_OA2_ConsumerKey = sma_OA2_ConsumerKey; 
	}

	public String getSma_OA2_ConsumerSecret() {
		return sma_OA2_ConsumerSecret;
	}
	public void setSma_OA2_ConsumerSecret(String sma_OA2_ConsumerSecret) {
		this.sma_OA2_ConsumerSecret = sma_OA2_ConsumerSecret;
	}

	public String getSma_OA2_AuthorizationURL() {
		return sma_OA2_AuthorizationURL;
	}
	public void setSma_OA2_AuthorizationURL(String sma_OA2_AuthorizationURL) {
		this.sma_OA2_AuthorizationURL = sma_OA2_AuthorizationURL;
	}

	public String getSma_OA2_AccessTokenURL() {
		return sma_OA2_AccessTokenURL;
	}
	public void setSma_OA2_AccessTokenURL(String sma_OA2_AccessTokenURL) {
		this.sma_OA2_AccessTokenURL = sma_OA2_AccessTokenURL;
	}

	public String getDom_URL() {
		return dom_URL;
	}
	public void setDom_URL(String dom_URL) {
		this.dom_URL = dom_URL;
	}

	public String getSt_URL() {
		return st_URL;
	}
	public void setSt_URL(String st_URL) {
		this.st_URL = st_URL;
	}
	
	public String getTwitter_OA_ConsumerKey() {
		return twitter_OA_ConsumerKey;
	}
	public void setTwitter_OA_ConsumerKey(String twitter_OA_ConsumerKey) {
		this.twitter_OA_ConsumerKey=twitter_OA_ConsumerKey;
	}

	public String getTwitter_OA_ConsumerSecret() {
		return twitter_OA_ConsumerSecret;
	}
	public void setTwitter_OA_ConsumerSecret(String twitter_OA_ConsumerSecret) {
		this.twitter_OA_ConsumerSecret=twitter_OA_ConsumerSecret;
	}

	public void prepareEndpoints() {
		FacesContextEx context = FacesContextEx.getCurrentInstance();
		
		// Set the properties for the default endpoints
		String endpointConnections = getEndpoint_Connections();
		if(StringUtil.isEmpty(endpointConnections)) {endpointConnections = "connections";}
		context.setSessionProperty("sbt.endpoint.connections", endpointConnections);
		
		String endpointSmartcloud = getEndpoint_Smartcloud();
		if(StringUtil.isEmpty(endpointSmartcloud)) {endpointSmartcloud = "smartcloud";}
		context.setSessionProperty("sbt.endpoint.smartcloud", endpointSmartcloud);
		
		String endpointDomino = getEndpoint_Domino();
		if(StringUtil.isEmpty(endpointDomino)) {endpointDomino = "domino";}
		context.setSessionProperty("sbt.endpoint.domino", endpointDomino);
		
		BasicEndpoint ep2 = (BasicEndpoint)ManagedBeanUtil.getBean(context, "watson");
		
		// Override the beans with the environment definition
		{
			ConnectionsBasicEndpoint ep = (ConnectionsBasicEndpoint)ManagedBeanUtil.getBean(context, "connections");
			if(ep!=null) {
				ep.setUrl(getCon_URL());
			}
		}
		{
			ConnectionsOAuth2Endpoint ep = (ConnectionsOAuth2Endpoint)ManagedBeanUtil.getBean(context, "connectionsOA2");
			if(ep!=null) {
				ep.setUrl(getCon_URL());
				ep.setConsumerKey(getCon_OA2_ConsumerKey());
				ep.setConsumerSecret(getCon_OA2_ConsumerSecret());
				ep.setAuthorizationURL(getCon_OA2_AuthorizationURL());
				ep.setAccessTokenURL(getCon_OA2_AccessTokenURL());
			}
		}
		{
			SmartCloudOAuthEndpoint ep = (SmartCloudOAuthEndpoint)ManagedBeanUtil.getBean(context, "smartcloud");
			if(ep!=null) {
				ep.setUrl(getSma_URL());
				ep.setConsumerKey(getSma_OA_ConsumerKey());
				ep.setConsumerSecret(getSma_OA_ConsumerSecret());
				ep.setRequestTokenURL(getSma_OA_RequestTokenURL());
				ep.setAuthorizationURL(getSma_OA_AuthorizationURL());
				ep.setAccessTokenURL(getSma_OA_AccessTokenURL());
			}
		}
		{
			SmartCloudOAuth2Endpoint ep = (SmartCloudOAuth2Endpoint)ManagedBeanUtil.getBean(context, "smartcloudOA2");
			if(ep!=null) {
				ep.setUrl(getSma_URL());
				ep.setConsumerKey(getSma_OA2_ConsumerKey());
				ep.setConsumerSecret(getSma_OA2_ConsumerSecret());
				ep.setAuthorizationURL(getSma_OA2_AuthorizationURL());
				ep.setAccessTokenURL(getSma_OA2_AccessTokenURL());
			}
		}
		{
			DominoBasicEndpoint ep = (DominoBasicEndpoint)ManagedBeanUtil.getBean(context, "domino");
			if(ep!=null) {
				ep.setUrl(getDom_URL());
			}
		}
		{
			SametimeBasicEndpoint ep = (SametimeBasicEndpoint)ManagedBeanUtil.getBean(context, "sametime");
			if(ep!=null) {
				ep.setUrl(getSt_URL());
			}
		}
		{
			// Temporarily use OAuth endpoint
			OAuthEndpoint ep = (OAuthEndpoint)ManagedBeanUtil.getBean(context, "twitter");
			if(ep!=null) {
				ep.setConsumerKey(getTwitter_OA_ConsumerKey());
				ep.setConsumerSecret(getTwitter_OA_ConsumerSecret());
			}
		}
	}
}
