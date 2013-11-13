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

package com.ibm.xsp.extlib.renderkit.html_extended.sbt;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.extlib.component.sbt.UISbtClient;
import com.ibm.xsp.extlib.renderkit.html_extended.FacesRendererEx;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.extlib.sbt.connections.proxy.ConnectionsProxyHandler;

import com.ibm.xsp.resource.DojoModuleResource;
import com.ibm.xsp.resource.ScriptResource;
import com.ibm.xsp.resource.StyleSheetResource;

/**
 * @author Philippe Riand
 */

public class SbtClientRenderer extends FacesRendererEx {

    public static final String PROFILES_SEMANTICTAGSERVLET      = "/profiles/ibm_semanticTagServlet/javascript/semanticTagService.js"; // $NON-NLS-1$
    public static final String COMMUNITIES_DOJO                 = "/communities/javascript/build/dojo/dojo.js"; // $NON-NLS-1$
    //public static final String COMMUNITIES_SEMANTICTAGSERVLET   = "/communities/javascript/build/dojo/semanticTagService.js"; // $NON-NLS-1$
    public static final String COMMUNITIES_SEMANTICTAGSERVLET   = PROFILES_SEMANTICTAGSERVLET;
    public static final String SBTLIBRARY = RuntimeConstants.get().getConstant(RuntimeConstants.LIBRARY_BASEURL);
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        // If we are in partial refresh mode, we assume that they are already loaded
        if(AjaxUtil.isAjaxPartialRefresh(context)) {
            return;
        }
	    
        UISbtClient ctClient = (UISbtClient)component;

        // Ensure that the dojo theme is loaded
        UIViewRootEx2 rootEx = (UIViewRootEx2)context.getViewRoot();
        rootEx.setDojoTheme(true);

        // Make the client enabled
        UISbtClient.enableClient(rootEx, true);
        
        // Find the Connections Endpoint
        String endpointName = ctClient.getEndpoint();
        if(StringUtil.isEmpty(endpointName)) {
            endpointName = EndpointFactory.SERVER_CONNECTIONS;
        }
        Endpoint ctServer = EndpointFactory.getEndpointUnchecked(endpointName);
        if(ctServer==null) {
            //Platform.getInstance().log("Sametime server URL is undefined.");
            return;
        }

        boolean inclProfiles = ctClient.isProfilesBusinessCard(); 
        boolean inclCommunities = ctClient.isCommunitiesBusinessCard(); 
        if(inclProfiles || inclCommunities) {
            // Write the Profiles semantic servlet
            if(inclProfiles) {
            	writeCommunitiesConfig(context, rootEx, ctClient, ctServer, endpointName);
            }

            // Write the Communities semantic servlet
            if(inclCommunities) {
                writeCommunitiesConfig(context, rootEx, ctClient, ctServer, endpointName);
            }
            
            // Add the semantic tag resource
            addSemanticTagResource(context, rootEx, ctClient, ctServer, endpointName, inclProfiles, inclCommunities);
        }

        String sbtlib_dojo = SBTLIBRARY + "?lib=dojo";
        rootEx.addEncodeResource(new ScriptResource(sbtlib_dojo, true));

        // Required by the profiles card code...
        //dijit._Widget,dijit._Templated,dijit._Container,dijit.form.ComboBox                
        rootEx.addEncodeResource(ExtLibResources.dojoI18n); // $NON-NLS-1$
        rootEx.addEncodeResource(ExtLibResources.dojoCookie); // $NON-NLS-1$

        //rootEx.addEncodeResource(new DojoModuleResource("dijit._Widget")); // $NON-NLS-1$
        //rootEx.addEncodeResource(new DojoModuleResource("dijit._Templated")); // $NON-NLS-1$
        rootEx.addEncodeResource(new DojoModuleResource("dijit._Container")); // $NON-NLS-1$
        rootEx.addEncodeResource(new DojoModuleResource("dijit.form.ComboBox")); // $NON-NLS-1$
    }

    //
    // Profiles
    //
    protected void writeProfilesConfig(FacesContext context, UIViewRootEx2 rootEx, UISbtClient ctClient, Endpoint ctServer, String endpointName) throws IOException {
    }
    

    //
    // Communities
    //
    protected void writeCommunitiesConfig(FacesContext context, UIViewRootEx2 rootEx, UISbtClient ctClient, Endpoint ctServer, String endpointName) throws IOException {
        try {
            // Create the configuration object and the corresponding script
            JsonObject semConfig = createCommunitiesSemConfig(context, ctClient, ctServer, endpointName);
            StringBuilder b = new StringBuilder(256);
            b.append("var SemTagSvcConfig=");
            JsonGenerator.toJson(JsonJavaFactory.instance,b,semConfig,true);
            b.append(";\n");
            String onInitSvcConfig = ctClient.getInitSvcConfigScript();
            if(StringUtil.isNotEmpty(onInitSvcConfig)) {
                b.append(onInitSvcConfig);
                b.append("\n");
            }
            ScriptResource configJS = new ScriptResource();
            configJS.setClientSide(true);
            configJS.setContents(b.toString());
            rootEx.addEncodeResource(context,configJS);
        } catch(JsonException ex) {
            throw new FacesExceptionEx(ex);
        }
    }
    protected JsonObject createCommunitiesSemConfig(FacesContext context, UISbtClient ctClient, Endpoint ctServer, String endpointName) throws IOException {
        // Get the proxy object in memory
        JsonObject proxyConfig = new JsonJavaObject();
        String baseUrl = PathUtil.concat(ctServer.getUrl(),"communities",'/');
        proxyConfig.putJsonProperty("baseUrl", baseUrl);
        String proxyUrl = getProxyUrl(context, ctClient, ctServer, endpointName); //PathUtil.concat(ctServer.getUrl(),"communities",'/');
        if(StringUtil.isNotEmpty(proxyUrl)) {
            proxyConfig.putJsonProperty("proxyURL", proxyUrl);
        }
        proxyConfig.putJsonProperty("loadCssFiles", ctClient.isLoadCSS());
        return proxyConfig;
    }
    
    protected String getProxyUrl(FacesContext context, UISbtClient ctClient, Endpoint ctServer, String endpointName) {
    	 Context ctx = Context.getUnchecked();
         return ProxyEndpointService.getProxyUrlForEndpoint(ctx, ConnectionsProxyHandler.URL_PATH,endpointName, null);
    }

    //
    // Semantic tag service
    //
    protected void addSemanticTagResource(FacesContext context, UIViewRootEx2 rootEx, UISbtClient ctClient, Endpoint ctServer, String endpointName, boolean inclProfiles, boolean inclCommunities) throws IOException {
        String connSrvUrl = PathUtil.concat(ctServer.getUrl(),PROFILES_SEMANTICTAGSERVLET,'/');
        StringBuilder b = new StringBuilder(128);
        b.append(connSrvUrl);
//        boolean loadDojo = ctClient.isLoadDojo(); 
//        b.append("?inclDojo=");
//        b.append(loadDojo?"true":"false");
//        boolean loadCss = ctClient.isLoadCSS(); 
//        b.append("?loadCssFiles=");
//        b.append(loadCss?"true":"false");
//        if(inclCommunities) {
//            b.append("&inclComm=true");
//        }
//        if(ctClient.isDebug()) {
//            b.append("&debug=uncompressed");
//        }
        
        // Add the resources
        ScriptResource js = new ScriptResource(b.toString(), true);
        //js.setAttribute("defer", "defer");        
    
        rootEx.addEncodeResource(js);
        
        // Try to load the vcard only CSS which is anyway required, even when OneUI is 
        // already loaded by the application
//        if(!loadCss) {
//            //https://w3.ibm.com/connections/profiles/nav/common/styles/base/standaloneVcard.css        
//            String semtagUrl = PathUtil.concat(ctServer.getUrl(),"/profiles/nav/common/styles/base/semanticTagStyles.css",'/');
//            StyleSheetResource semtag = new StyleSheetResource(semtagUrl);
//            rootEx.addEncodeResource(context,semtag);
//        }
    }
}
