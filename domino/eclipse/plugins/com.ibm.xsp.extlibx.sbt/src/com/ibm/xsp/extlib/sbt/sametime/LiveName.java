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
package com.ibm.xsp.extlib.sbt.sametime;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.component.sametime.UISametimeClient;
import com.ibm.xsp.renderkit.ContentTypeRenderer;

/**
 * @author Ishfak
 *
 */
public class LiveName implements ContentTypeRenderer {

    public static final String CONTENT_TYPE = "xs:st.livename";
    public static final String CONTENT_TYPE_INC_LABEL = CONTENT_TYPE + "|IBM Sametime Live Names";
    public static final String[] CONTENT_TYPES = new String[]{CONTENT_TYPE_INC_LABEL};

    public LiveName() {
    }
    
    public String[] getContentTypes() {
        return CONTENT_TYPES;
    }

    public boolean render(FacesContext context, UIComponent component, ResponseWriter writer, String contentType, String value) throws IOException {
        if(contentType.equals(CONTENT_TYPE)) {
            if(UISametimeClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
                renderSTLiveName(context,writer, component, value);
            } else {
                renderSTText(context, writer, component, value);
            }
            return true;
        }
        return false;
    }

    // If the sametime client is enabled
    private void renderSTLiveName(FacesContext context, ResponseWriter writer, UIComponent component, String value) throws IOException {
        String userId = value;
        String displayName = null;
        if(value.indexOf('|')>=0) {
            String vals[] = StringUtil.splitString(value,'|');
            userId = vals[0];
            displayName = StringUtil.isEmpty(vals[1]) ? vals[0] : vals[1];
        }
        
        
        ((UIViewRootEx)context.getViewRoot()).setDojoParseOnLoad(true);
        
        writer.startElement("span", component);
        writer.writeAttribute("userId", userId, null);
        if(StringUtil.isNotEmpty(displayName)) {
            writer.writeAttribute("displayName", displayName, null);
        }
        writer.writeAttribute("dojoType", "sametime.LiveName", null);
        writer.endElement("span");
    }
    
    // If the sametime client is *not* enabled
    private void renderSTText(FacesContext context, ResponseWriter writer, UIComponent component, String value) throws IOException {
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