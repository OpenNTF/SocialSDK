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

package com.ibm.xsp.extlib.component.facebook;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.component.UIViewRootEx;

/**
 * @author Niklas Heidloff
 */

public class UIFacebookClient extends UIComponentBase {

	public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.facebook.sdk"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.facebook.sdk"; //$NON-NLS-1$	

	private java.lang.String endpoint;
	
	public UIFacebookClient() {
		super();
		setRendererType(RENDERER_TYPE);
	}
	
	// Test if the client is enabled
    public static final String ATTR_ENABLED = "extlib.facebook.enabled"; 
    public static boolean isClientEnabled(UIViewRootEx rootEx) {
        return rootEx.getAttributes().get(ATTR_ENABLED)!=null;
    }
    public static void enableClient(UIViewRootEx rootEx, boolean enabled) {
        if(enabled) {
            rootEx.getAttributes().put(ATTR_ENABLED,Boolean.TRUE);
        } else {
            rootEx.getAttributes().remove(ATTR_ENABLED);
        }
    }

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public java.lang.String getEndpoint() {		
		if (null != this.endpoint) {
			return this.endpoint;
		}
		
		ValueBinding _vb = getValueBinding("endpoint"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}
	
	public void setEndpoint(java.lang.String endpoint) {
		this.endpoint = endpoint;
	}	

	@Override
	public void restoreState(FacesContext _context, Object _state) {
		Object _values[] = (Object[]) _state;
		super.restoreState(_context, _values[0]);
        
		this.endpoint = (String)_values[1];
	}

	@Override
	public Object saveState(FacesContext _context) {
		Object _values[] = new Object[2];
		_values[0] = super.saveState(_context);
		_values[1] = endpoint;
		return _values;
	}
}
