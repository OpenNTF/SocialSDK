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

/**
 * @author Niklas Heidloff
 */

public class UILogin extends UIComponentBase {

	public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.facebook.login"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.facebook.login"; //$NON-NLS-1$	
	
	private java.lang.String perms;
	private java.lang.String maxRows;
	private java.lang.String showFaces;
	private java.lang.String width;
	private java.lang.String appId;
	
	public UILogin() {
		super();
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public java.lang.String getAppId() {		
		if (null != this.appId) {
			return this.appId;
		}
		
		ValueBinding _vb = getValueBinding("appId"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}
	
	public void setAppId(java.lang.String appId) {
		this.appId = appId;
	}	
	
	public java.lang.String getPerms() {
		if (null != this.perms) {
			return this.perms;
		}
		
		ValueBinding _vb = getValueBinding("perms"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}

	public void setPerms(java.lang.String perms) {
		this.perms = perms;
	}	

	public java.lang.String getMaxRows() {
		if (null != this.maxRows) {
			return this.maxRows;
		}
		
		ValueBinding _vb = getValueBinding("maxRows"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}

	public void setMaxRows(java.lang.String maxRows) {
		this.maxRows = maxRows;
	}	

	public java.lang.String getShowFaces() {
		if (null != this.showFaces) {
			return this.showFaces;
		}
		
		ValueBinding _vb = getValueBinding("showFaces"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}

	public void setShowFaces(java.lang.String showFaces) {
		this.showFaces = showFaces;
	}	
	
	public java.lang.String getWidth() {
		if (null != this.width) {
			return this.width;
		}
		
		ValueBinding _vb = getValueBinding("width"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}	
	}

	public void setWidth(java.lang.String width) {
		this.width = width;
	}
	
	@Override
	public void restoreState(FacesContext _context, Object _state) {
		Object _values[] = (Object[]) _state;
		super.restoreState(_context, _values[0]);
        
		this.perms = (String)_values[1];
		this.maxRows = (String)_values[2];
		this.showFaces = (String)_values[3];
		this.width = (String)_values[4];
		this.appId = (String)_values[5];
	}

	@Override
	public Object saveState(FacesContext _context) {
		Object _values[] = new Object[6];
		_values[0] = super.saveState(_context);
		_values[1] = perms;
		_values[2] = maxRows;
		_values[3] = showFaces;
		_values[4] = width;
		_values[5] = appId;
		return _values;
	}		
}
