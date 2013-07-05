/*
 * © Copyright IBM Corp. 2011
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

package com.ibm.xsp.extlib.renderkit.html_extended.facebook;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.endpoints.OAuthEndpoint;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.extlib.component.facebook.UIFacebookClient;
import com.ibm.xsp.extlib.renderkit.html_extended.FacesRendererEx;
import com.ibm.xsp.extlib.sbt.resources.SBTResources;

import com.ibm.xsp.util.FacesUtil;

/**
 * @author Niklas Heidloff
 */

public class FacebookClientRenderer extends FacesRendererEx {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        // Mark the Facebook client as enabled
        UIViewRootEx2 rootEx = (UIViewRootEx2)context.getViewRoot();
        UIFacebookClient.enableClient(rootEx, true);
        
        UIFacebookClient dialog = (UIFacebookClient)component;

        String endpoint = dialog.getEndpoint();
		Endpoint _bean = EndpointFactory.getEndpoint(endpoint, EndpointFactory.SERVER_FACEBOOK);
		if(!(_bean instanceof OAuthEndpoint)) {
		    throw new FacesExceptionEx("The Facebook endpopint must be a OAuthEndPoint");
		}
		OAuthEndpoint bean = (OAuthEndpoint)_bean;
		
        writeJSSDK(context, component, bean);
    }
	
	protected JsonObject createJSONConfig(OAuthEndpoint bean) throws IOException {
		JsonObject config = new JsonJavaObject();
        try {
            String appId = bean.getConsumerKey();
            config.putJsonProperty("appId", appId);
        } catch (Exception e) {
        	IOException ioe = new IOException("Error while accessing the consumer key");
        	ioe.initCause(e);
            throw ioe;
        }           
        //http://xxxx/xsp/.ibmxspres/.extlib/sbt/facebook/channel.html
        // Note that the URL must be absolute
        String htmlResource = FacesContext.getCurrentInstance().getExternalContext().encodeResourceURL(SBTResources.FACEBOOK_CHANNEL_HTML);
        String channelURL = FacesUtil.makeUrlAbsolute(FacesContext.getCurrentInstance(), htmlResource);
        config.putJsonProperty("channelURL", channelURL);
        
        // Should we make these client parameters?
		config.putJsonProperty("status", true);
		config.putJsonProperty("cookie", true);
		config.putJsonProperty("xfbml", true);
		// This currently fails
        //config.putJsonProperty("oauth", true);
				
		return config;
	}

	public void writeJSSDK(FacesContext context, UIComponent component, OAuthEndpoint bean)
			throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        UIViewRootEx2 rootEx = (UIViewRootEx2)context.getViewRoot();
        if(rootEx.getEncodeProperty("xsp.extlib.facebook.jssdk")==null) {
	        writer.startElement("div", component);
	        writer.writeAttribute("id","fb-root", null);
	        writer.endElement("div");
	
            writer.write("\n");
	        writer.startElement("script", component);
	        writer.writeAttribute("type", "text/javascript", null);
	
	        JsonObject config = createJSONConfig(bean);
	        StringBuilder b = new StringBuilder(256);
            
	        try {
	        	JsonGenerator.toJson(JsonJavaFactory.instance,b,config,true);
        	} 
	        catch(JsonException ex) {
        		throw new FacesExceptionEx(ex);
        	}
	        
            writer.writeText("\nwindow.fbAsyncInit = function() {", null);
	        writer.writeText("\nFB.init(" + b + ");};", null);
	        
	        // We need to add the xmlns:fb to the HTML tag
            writer.writeText("\n(function() {", null);
	        writer.writeText("var elements = document.getElementsByTagName(\"html\");", null);
	        writer.writeText("var htmlElement = elements[0];", null);
	        writer.writeText("var attribute = document.createAttribute(\"xmlns:fb\");", null);
	        writer.writeText("attribute.nodeValue = \"http://www.facebook.com/2008/fbml\";", null);
	        writer.writeText("htmlElement.setAttributeNode(attribute); ", null);
	        writer.writeText("}());", null);
	        // An now we can load the FB script asynchonously
	        // Load the SDK Asynchronously
	        writer.writeText("\n(function(d){",null);
	        writer.writeText("var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}",null);
	        writer.writeText("js = d.createElement('script'); js.id = id; js.async = true;",null);
	        writer.writeText("js.src = \"//connect.facebook.net/en_US/all.js\";",null);
	        writer.writeText("d.getElementsByTagName('head')[0].appendChild(js);",null);
	        writer.writeText("}(document));",null);	        
	         
	        writer.endElement("script");
            writer.write("\n");
        }     
        rootEx.putEncodeProperty("xsp.extlib.facebook.jssdk", Boolean.TRUE);
	}
}
