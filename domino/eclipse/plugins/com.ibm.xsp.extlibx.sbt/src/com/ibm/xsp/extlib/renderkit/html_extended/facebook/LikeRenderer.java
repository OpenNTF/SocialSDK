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

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.extlib.component.facebook.UIFacebookClient;
import com.ibm.xsp.extlib.component.facebook.UILike;

/**
 * @author Niklas Heidloff
 */

public class LikeRenderer extends FacebookPluginBaseRenderer {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
		if(UIFacebookClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
	        ResponseWriter writer = context.getResponseWriter();
	        
	        UILike dialog = (UILike)component;	        
	
	        writer.startElement("fb:like", component);
	        
	        writer.writeAttribute("id", component.getClientId(context), null);
	        	        
	        if (dialog.getShowFaces() == null) {
	        	writer.writeAttribute("show_faces", "false", null);
	        }
	        else {
	        	writer.writeAttribute("show_faces", dialog.getShowFaces(), null);
	        }
	
	        if (dialog.getWidth() == null) {
	        	writer.writeAttribute("width", "450", null);
	        }
	        else {
	        	writer.writeAttribute("width", dialog.getWidth(), null);
	        }
	
	        if (dialog.getSend() == null) {
	        	writer.writeAttribute("send", "false", null);
	        }
	        else {
	        	writer.writeAttribute("send", dialog.getSend(), null);
	        }
	
	        if (dialog.getHref() == null) {
	        	writer.writeAttribute("href", "", null); 
	        }
	        else {
	        	writer.writeAttribute("href", dialog.getHref(), null);
	        }
		}
    }

	@Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
		if(UIFacebookClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
			ResponseWriter writer = context.getResponseWriter();
			writer.endElement("fb:like");
		}
    }
}
