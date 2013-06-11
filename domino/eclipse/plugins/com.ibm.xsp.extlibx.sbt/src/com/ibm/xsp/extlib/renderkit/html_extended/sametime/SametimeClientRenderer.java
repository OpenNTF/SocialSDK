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

package com.ibm.xsp.extlib.renderkit.html_extended.sametime;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.endpoints.SSOEndpoint;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.extlib.component.sametime.UISametimeClient;
import com.ibm.xsp.extlib.renderkit.html_extended.FacesRendererEx;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.extlib.sbt.resources.SBTResources;
import com.ibm.xsp.resource.ScriptResource;
import com.ibm.xsp.resource.StyleSheetResource;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 * @author Philippe Riand
 */

public class SametimeClientRenderer extends FacesRendererEx {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        // If we are in partial refresh mode, we assume that they are already loaded
        if(AjaxUtil.isAjaxPartialRefresh(context)) {
            return;
        }
	    
        UISametimeClient stClient = (UISametimeClient)component;

        // Ensure that the dojo theme is loaded
        UIViewRootEx2 rootEx = (UIViewRootEx2)context.getViewRoot();
        rootEx.setDojoTheme(true);
        
        // Mark the ST client as enabled
        UISametimeClient.enableClient(rootEx, true);
        
        // Find the Sametime Endpoint
        String endpointName = stClient.getEndpoint();
        if(StringUtil.isEmpty(endpointName)) {
            endpointName = EndpointFactory.SERVER_SAMETIME;
        }
        Endpoint stServer = EndpointFactory.getEndpointUnchecked(endpointName);
        if(stServer==null) {
            //Platform.getInstance().log("Sametime server URL is undefined.");
            return;
        }

        // Get the url of the Lotus Sametime proxy server
        String linkurl = stServer.getUrl();
        if(StringUtil.isEmpty(linkurl)) {
            //Platform.getInstance().log("Sametime server URL is undefined.");
            return;
        }    
        if(linkurl.endsWith("/")) {
            linkurl = linkurl.substring(0,linkurl.length()-1);
        }

        // Write the ST proxy
        writeProxyConfig(context, rootEx, stClient, stServer, linkurl);

        // Write the client script file references
        writeClientScriptFile(context, rootEx, stClient, stServer, linkurl);

        // Write the login
        writeLogin(context, rootEx, stClient, stServer);
    }
    
    protected void writeProxyConfig(FacesContext context, UIViewRootEx2 rootEx, UISametimeClient stClient, Endpoint stServer, String linkurl) throws IOException {
        // Generate the proxy configuration
        try {
            // Create the proxy object and the corresponding script
            JsonObject proxyConfig = createProxyConfig(context, stClient, stServer, linkurl);
            StringBuilder b = new StringBuilder(256);
            // Sametime FIXME: djConfig!
            b.append("var djConfig = { parseOnLoad: true, isDebug: false };\n" );
            b.append("var stproxyConfig=");
            JsonGenerator.toJson(JsonJavaFactory.instance,b,proxyConfig,true);
            b.append(";\n");
            String onInitProxy = stClient.getInitProxyScript();
            if(StringUtil.isNotEmpty(onInitProxy)) {
                b.append(onInitProxy);
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
    
    protected JsonObject createProxyConfig(FacesContext context, UISametimeClient stClient, Endpoint stServer, String linkurl) throws IOException {
        // Get the proxy object in memory
        JsonObject proxyConfig = new JsonJavaObject();
        proxyConfig.putJsonProperty("server", stServer.getUrl());
        boolean autoTunnel = stClient.isAutoTunnelURI();
        if(autoTunnel) {
            //http://xxxx/xsp/.ibmxspres/.extlib/sbt/sametime/tunnel.html
            // Note that the URL must be absolute
            String htmlResource = FacesContext.getCurrentInstance().getExternalContext().encodeResourceURL(SBTResources.SAMETIME_TUNNEL_HTML);
            String tunnelURI = FacesUtil.makeUrlAbsolute(FacesContext.getCurrentInstance(), htmlResource);
            proxyConfig.putJsonProperty("tunnelURI", tunnelURI);
        }
        
        boolean connectClient = stClient.isConnectClient();
        if(connectClient) {
            proxyConfig.putJsonProperty("isConnectClient", connectClient);
        }
        return proxyConfig;
    }

    
    protected void writeClientScriptFile(FacesContext context, UIViewRootEx2 rootEx, UISametimeClient stClient, Endpoint stServer, String linkurl) throws IOException {
        // Generate a client script resource
        String script = stClient.getClientScriptFile();
        if(StringUtil.isNotEmpty(script)) {
            boolean baseComp = false;
            if(script.equals(UISametimeClient.SCRIPT_BASECOMP)) {
                baseComp = true;
            } else if(script.equals(UISametimeClient.SCRIPT_LIVENAME)) {
                baseComp = true;
            } else if(script.equals(UISametimeClient.SCRIPT_WIDGETS)) {
                baseComp = true;
            }

            if(baseComp) {
                StringBuilder baseCompUrl = new StringBuilder(128);
                baseCompUrl.append(linkurl);
                baseCompUrl.append("/stbaseapi/baseComps.js");
                baseCompUrl.append("?noHub=");
                baseCompUrl.append(stClient.isNoHub());
                baseCompUrl.append("&lang=");
                String lang = stClient.getLang();
                if(StringUtil.isEmpty(lang)) {
                    lang = XSPContext.getXSPContext(context).getLocaleString();
                }
                baseCompUrl.append(lang);
                // The bascomp URL
                ScriptResource baseCompJS = new ScriptResource(baseCompUrl.toString(),true);
                rootEx.addEncodeResource(context,baseCompJS);
                // And the corresponding CSS
                StyleSheetResource baseCss = new StyleSheetResource(linkurl + "/stwebclient/dojo.blue/sametime/themes/WebClientAllNoTundra.css");
                rootEx.addEncodeResource(context,baseCss);
            }
            
            int preload = 0;
            if(script.equals(UISametimeClient.SCRIPT_LIVENAME)) {
                preload = 1;
                ScriptResource livenameJS= new ScriptResource(linkurl + "/stwebclient/livenameLight.js",true);
                rootEx.addEncodeResource(context,livenameJS);
            } else if(script.equals(UISametimeClient.SCRIPT_WIDGETS)) {
                preload = 2;
                baseComp = true;
                ScriptResource widgetsJS= new ScriptResource(linkurl + "/stwebclient/widgetsLight.js",true);
                rootEx.addEncodeResource(context,widgetsJS);
            }
            
            // Add some dojo resources to optimize the use of the JS aggregator
            if(preload>0) {
                rootEx.addEncodeResource(context,ExtLibResources.dojoItemFileWriteStore);
                rootEx.addEncodeResource(context,ExtLibResources.dojoDndMoveable);
                rootEx.addEncodeResource(context,ExtLibResources.dijitMenu);
                if(preload>=2) {
                    rootEx.addEncodeResource(context,ExtLibResources.dojoString);
                    rootEx.addEncodeResource(context,ExtLibResources.dojoCookie);
                    rootEx.addEncodeResource(context,ExtLibResources.dojoIoScript);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitToolbar);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitEditor);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitTree);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitLayoutContentPane);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitLayoutBorderContainer);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitFormButton);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitFormComboBox);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitFormCheckBox);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitFormTextBox);
                    rootEx.addEncodeResource(context,ExtLibResources.dijitTree);
                }
            }
        }
    }
    
    protected void writeLogin(FacesContext context, UIViewRootEx2 rootEx, UISametimeClient stClient, Endpoint stServer) throws IOException {
        boolean autoLogin = stClient.isAutoLogin(); 
        if(autoLogin) {
            try {
                if(stServer instanceof BasicEndpoint) {
                    writeAutoLoginBasic(context, rootEx, stClient, (BasicEndpoint)stServer);
                } else if(stServer instanceof SSOEndpoint) {
                    writeAutoLoginToken(context, rootEx, stClient, (SSOEndpoint)stServer);
                }
            } catch(ClientServicesException ex) {
                throw new FacesExceptionEx(ex);
            }
        }
    }
    protected void writeAutoLoginBasic(FacesContext context, UIViewRootEx2 rootEx, UISametimeClient stClient, BasicEndpoint stServer) throws IOException, ClientServicesException {
        // check if stAutoLogin is true
        boolean autoLogin = stClient.isAutoLogin(); 
        if(autoLogin) {
            if(stServer.isAuthenticated()) {
                String user = stServer.getUserIdentity();
                String pwd = stServer.getPassword();
                String status = stClient.getLoginStatus();
                if(StringUtil.isEmpty(status)) {
                    status = "I'm available";
                }
                StringBuilder b = new StringBuilder(256);
                b.append("dojo.addOnLoad(function(){\n");
                b.append("if(stproxy){stproxy.login.loginByPassword('");
                JavaScriptUtil.appendJavaScriptString(b, user);
                b.append("','");
                JavaScriptUtil.appendJavaScriptString(b, pwd);
                b.append("',stproxy.status.AVAILABLE,'");
                JavaScriptUtil.appendJavaScriptString(b, status);
                b.append("',null,null)}\n");
                b.append("});\n");
                //b.append("dojo.addOnUnload(function(){if(stproxy){stproxy.login.logout(stproxy.session.USERID)}});\n");
                
                rootEx.addScript(b.toString());
            }
        }
    }
    protected void writeAutoLoginToken(FacesContext context, UIViewRootEx2 rootEx, UISametimeClient stClient, SSOEndpoint stServer) throws IOException {
        throw new NotImplementedException();
    }

  
//    
// EXPERIMENTAL CODE - DO NOT REMOVE FOR NOW!
//    
//    protected void addDynamicScript(StringBuilder b, String url) {
////      b.append("document.write(\"<script src='xxx'></script>\");\n");
//      b.append("document.write(\"\\x3Cscript type='text\\/javascript' src='");
//      JavaScriptUtil.appendJavaScriptString(b, url);
//      b.append("'>\\x3C/script>\");\n");
//  }
//
//  protected void addDynamicScript_(StringBuilder b, String url) {
//      b.append("var tx=dojo._getText('");
//      JavaScriptUtil.appendJavaScriptString(b, url);
//      b.append("');\n");
//      b.append("var sc=document.createElement('script');\n");
//      b.append("sc.setAttribute('type','text/javascript');\n");
//      b.append("sc.text=tx;");
//      JavaScriptUtil.appendJavaScriptString(b, url);
//      b.append("');\n");
//      b.append("dojo.doc.getElementsByTagName('head')[0].appendChild(sc);\n");
//      
////      b.append("var sc=document.createElement('script');\n");
////      b.append("sc.setAttribute('type','text/javascript');\n");
////      b.append("sc.setAttribute('src', '");
////      JavaScriptUtil.appendJavaScriptString(b, url);
////      b.append("');\n");
////      b.append("dojo.doc.getElementsByTagName('head')[0].appendChild(sc);\n");
//  }
    
}
