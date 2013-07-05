/*
 * © Copyright IBM Corp. 2010
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
package com.ibm.xsp.extlib.sbt.connections.contenttype;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ViewHandlerEx;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.component.connections.UIConnectionsClient;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.extlib.sbt.resources.SBTResources;
import com.ibm.xsp.renderkit.ContentTypeRenderer;
import com.ibm.xsp.renderkit.dojo.DojoUtil;

/**
 * @author Ishfak
 * @author Philippe riand
 */
public class VCard implements ContentTypeRenderer {
    
    // Using the dojo widget ensures that the semtag is processed once the dijit
    // in instanciated after a partial refresh
    private static final boolean USE_DOJO   = true;

    public static final String CONTENT_TYPE_PROFILE_VCARD           = "xs:lc.vcard"; // $NON-NLS-1$
    public static final String CONTENT_TYPE_PROFILE_VCARD_INLINE    = "xs:lc.vcardi"; // $NON-NLS-1$
    
    public static final String CONTENT_TYPE_PROFILE_VCARD_INC_LABEL           = CONTENT_TYPE_PROFILE_VCARD +"|Profiles VCard"; // $NON-NLS-1$
    public static final String CONTENT_TYPE_PROFILE_VCARD_INLINE_INC_LABEL    = CONTENT_TYPE_PROFILE_VCARD_INLINE + "|Profiles VCard Inline"; // $NON-NLS-1$

    public static final String[] CONTENT_TYPES = new String[]{
        CONTENT_TYPE_PROFILE_VCARD_INC_LABEL, 
        CONTENT_TYPE_PROFILE_VCARD_INLINE_INC_LABEL,
    };

    public static final String PROFILES_SEMANTICTAGSERVLET = "/profiles/ibm_semanticTagServlet/javascript/semanticTagService.js"; // $NON-NLS-1$
    
    public VCard () {
    }
    
    public String[] getContentTypes() {
        return CONTENT_TYPES;
    }

    public boolean render(FacesContext context, UIComponent component, ResponseWriter writer, String contentType, String value) throws IOException {
        if(contentType.equals(CONTENT_TYPE_PROFILE_VCARD)) {
            UIViewRootEx rootEx = (UIViewRootEx)context.getViewRoot();
            if(UIConnectionsClient.isClientEnabled(rootEx)) {
                renderProfilesCard(context, writer, rootEx, component, value);
            } else {
                renderConnectionsText(context, writer, rootEx, component, value);
            }
            return true;
        } else if (contentType.equals(CONTENT_TYPE_PROFILE_VCARD_INLINE)) {
            UIViewRootEx rootEx = (UIViewRootEx)context.getViewRoot();
            if(UIConnectionsClient.isClientEnabled(rootEx)) {
                renderProfilesCardInline(context, writer, rootEx, component, value);
            } else {
                renderConnectionsText(context, writer, rootEx, component, value);
            }
            return true;
        }
        return false;
    }
    
    private void renderProfilesCard(FacesContext context, ResponseWriter writer, UIViewRootEx rootEx, UIComponent component, String value) throws IOException {
        String vals[] = StringUtil.splitString(value,'|');
        
        String userId = vals[0];
        String userName = vals.length>=2 ? vals[1] : null;
        if(StringUtil.isEmpty(userName)) {
            userName = userId;
        }
        
        if(USE_DOJO) {
            rootEx.setDojoParseOnLoad(true);
            rootEx.setDojoTheme(true);
            writer.startElement("span", component); // $NON-NLS-1$
            Map<String,String> attr = new HashMap<String, String>();
            attr.put("id", component.getClientId(context)+"_cn");
            attr.put("userId", userId);
            attr.put("userName", userName);
            DojoUtil.addDojoHtmlAttributes(context, "extlib.dijit.ProfilesVCard", null, attr);
            writer.endElement("span");  // $NON-NLS-1$
            ExtLibResources.addEncodeResource(rootEx, SBTResources.dojoProfilesVCard);
        } else {
            writer.startElement("span", component); // $NON-NLS-1$
            writer.writeAttribute("class", "vcard", null); // $NON-NLS-1$ $NON-NLS-2$
            
            writer.startElement("a", component);
            writer.writeAttribute("class", "fn url", null); // $NON-NLS-1$ $NON-NLS-2$
            
            writer.writeAttribute("style", "font-size:90%;", null); // $NON-NLS-1$ $NON-NLS-2$
            if(vals.length>=3)  {
                ViewHandlerEx viewHandler = (ViewHandlerEx)context.getApplication().getViewHandler();
                String href = viewHandler.getResourceURL(context,vals[2]);
                writer.writeURIAttribute("href", context.getExternalContext().encodeResourceURL(href), "href"); // $NON-NLS-1$
            } else {
                writer.writeURIAttribute("href", "javascript:void(0);", "href"); // $NON-NLS-1$
            }
            writer.writeText(userName, null);
            writer.endElement("a");
            
            writer.startElement("span", component); // $NON-NLS-1$
            if(userId.indexOf('@')>=0) {
                writer.writeAttribute("class", "email", null); // $NON-NLS-1$ $NON-NLS-2$
            } else {
                writer.writeAttribute("class", "x-lconn-userid", null); // $NON-NLS-1$ $NON-NLS-2$
            }
            writer.writeAttribute("style", "display:none", null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeText(userId, null);
            writer.endElement("span"); // $NON-NLS-1$
            
            writer.endElement("span");  // $NON-NLS-1$
        }
    }
    
    private void renderProfilesCardInline (FacesContext context, ResponseWriter writer, UIViewRootEx rootEx, UIComponent component, String value) throws IOException {
        String vals[] = StringUtil.splitString(value,'|');
        
        String userId = vals[0];
        String userName = vals.length>=2 ? vals[1] : null;
        if(StringUtil.isEmpty(userName)) {
            userName = userId;
        }
        
        if(USE_DOJO) {
            writer.startElement("span", component); // $NON-NLS-1$
            Map<String,String> attr = new HashMap<String, String>();
            attr.put("userId", userId);
            attr.put("userName", userName);
            DojoUtil.addDojoHtmlAttributes(context, "extlib.dijit.ProfilesVCardInline", null, attr);
            writer.endElement("span");  // $NON-NLS-1$
            ExtLibResources.addEncodeResource(rootEx, SBTResources.dojoProfilesVCardInline);
        } else {
            writer.startElement("span", component); // $NON-NLS-1$
            writer.writeAttribute("class", "vcard X-person-display-inline", null); // $NON-NLS-1$ $NON-NLS-2$
            
            writer.startElement("span", component); // $NON-NLS-1$
            writer.writeAttribute("class", "fn", null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeAttribute("style", "display:none;", null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeText(userName, null);
            writer.endElement("span"); // $NON-NLS-1$
            
            writer.startElement("span", component); // $NON-NLS-1$
            if(userId.indexOf('@')>=0) {
                writer.writeAttribute("class", "email", null); // $NON-NLS-1$ $NON-NLS-2$
            } else {
                writer.writeAttribute("class", "x-lconn-userid", null); // $NON-NLS-1$ $NON-NLS-2$
            }
            writer.writeAttribute("style", "display:none;", null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeText(userId, null);
            writer.endElement("span"); // $NON-NLS-1$
            
            writer.endElement("span");  // $NON-NLS-1$
        }
    }
    
    
    // If the sametime client is *not* enabled
    private void renderConnectionsText(FacesContext context, ResponseWriter writer, UIViewRootEx rootEx, UIComponent component, String value) throws IOException {
        if(value.indexOf('|')>=0) {
            String vals[] = StringUtil.splitString(value,'|');
            if(StringUtil.isNotEmpty(vals[1])) {
                value = vals[1];
            } else {
                value = vals[0];
            }
        }
        writer.writeText(value, null);
    }
    
}
