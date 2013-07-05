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
import com.ibm.xsp.extlib.component.facebook.UIComments;
import com.ibm.xsp.extlib.component.facebook.UIFacebookClient;

/**
 * @author Niklas Heidloff
 */

public class CommentsRenderer extends FacebookPluginBaseRenderer {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
		if(UIFacebookClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
	        ResponseWriter writer = context.getResponseWriter();
	        
	        UIComments dialog = (UIComments)component;	        	       
	
	        writer.startElement("fb:comments", component);

            writer.writeAttribute("id", component.getClientId(context), null);
	        
	        if (dialog.getNumPosts() == null) {
	        	writer.writeAttribute("num_posts", "", null);
	        }
	        else {
	        	writer.writeAttribute("num_posts", dialog.getNumPosts(), null);
	        }
	
	        if (dialog.getWidth() == null) {
	        	writer.writeAttribute("width", "500", null);
	        }
	        else {
	        	writer.writeAttribute("width", dialog.getWidth(), null);
	        }
	
	        if (dialog.getColorscheme() == null) {
	        	writer.writeAttribute("num_posts", "false", null);
	        }
	        else {
	        	writer.writeAttribute("num_posts", dialog.getColorscheme(), null);
	        }
	
	        if (dialog.getHref() == null) {
	        	writer.writeAttribute("href", "", null);
	        }
	        else {
	        	writer.writeAttribute("href", dialog.getHref(), null);
	        }              
	        
	        writer.endElement("fb:comments");
		}
    }

	@Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {      
    }
}
