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

package com.ibm.xsp.extlib.renderkit.html_extended.connections;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.dojo.FacesDojoComponent;
import com.ibm.xsp.extlib.component.connections.UIConnectionsClient;
import com.ibm.xsp.extlib.renderkit.dojo.DojoWidgetRenderer;
import com.ibm.xsp.extlib.resources.ExtLibResources;
import com.ibm.xsp.resource.DojoModuleResource;

/**
 * @author Philippe Riand
 */

public class ConnectionsWidgetRenderer extends DojoWidgetRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if(UIConnectionsClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
            super.encodeBegin(context, component);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if(UIConnectionsClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
            super.encodeEnd(context, component);
        }
    }

    @Override
    public boolean getRendersChildren() {
        if(UIConnectionsClient.isClientEnabled((UIViewRootEx)FacesContext.getCurrentInstance().getViewRoot())) {
            return super.getRendersChildren();
        }
        return true;
    }
    
    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if(UIConnectionsClient.isClientEnabled((UIViewRootEx)context.getViewRoot())) {
            super.encodeChildren(context, component);
        }
    }

    @Override
    protected String getTagName() {
        return "span";
    }

    @Override
    protected String getDefaultDojoType(FacesContext context, FacesDojoComponent component) {
        return null;
    }

    @Override
    protected DojoModuleResource getDefaultDojoModule(FacesContext context, FacesDojoComponent component) {
        return null;
    }

    @Override
    protected void writeDojoModule(FacesContext context, UIViewRootEx viewEx, FacesDojoComponent dojoComponent, String dojoType) {
        DojoModuleResource module = new DojoModuleResource(dojoType);
        ExtLibResources.addEncodeResource(viewEx, module);
    }
}
