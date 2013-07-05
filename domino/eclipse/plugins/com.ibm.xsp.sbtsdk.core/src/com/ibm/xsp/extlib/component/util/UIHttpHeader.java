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

package com.ibm.xsp.extlib.component.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.complex.Parameter;
import com.ibm.xsp.util.StateHolderUtil;

/**
 * This component is used to set HTTP headers to the response.
 * 
 * @author Philippe Riand
 */
public class UIHttpHeader extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.util.HttpHeader"; //$NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.util.HttpHeader"; // $NON-NLS-1$
	public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.util.HttpHeader"; //$NON-NLS-1$	
	
    private List<Parameter> _attributes;
	
	public UIHttpHeader() {
	    // No renderer to be set, as this component doesn't render markup
        setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
    public void addHeader(Parameter header){
        if (_attributes == null) {
            _attributes = new ArrayList<Parameter>();
        }
        _attributes.add(header);
    }

    public List<Parameter> getHeaders() {
        return _attributes;
    }
	
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if(isRendered()) {
            if(_attributes!=null) {
                HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
                for(int i=0; i<_attributes.size(); i++) {
                    Parameter p = _attributes.get(i);
                    String name = p.getName();
                    if(StringUtil.isNotEmpty(name)) {
                        String value = p.getValue();
                        response.addHeader(name, value);
                    }
                }
            }
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = super.saveState(context);
        state[1] = StateHolderUtil.saveList(context,_attributes);
        return state;
    }
    
    @Override
    public void restoreState(FacesContext context, Object value) {
        Object[] values = (Object[])value;
        super.restoreState(context, values[0]);
        _attributes = StateHolderUtil.restoreList(context, null, values[1]);
    }
}
