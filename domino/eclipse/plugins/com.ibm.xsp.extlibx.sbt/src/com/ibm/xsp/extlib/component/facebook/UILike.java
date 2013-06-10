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

public class UILike extends UIComponentBase {

	public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.facebook.like"; //$NON-NLS-1$
	public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.facebook.like"; //$NON-NLS-1$	

	private java.lang.String href;
	private java.lang.String send;
	private java.lang.String showFaces;
	private java.lang.String width;
	
	public UILike() {
		super();
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public java.lang.String getHref() {
		if (null != this.href) {
			return this.href;
		}
		
		ValueBinding _vb = getValueBinding("href"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}		
	}

	public void setHref(java.lang.String href) {
		this.href = href;
	}	

	public java.lang.String getSend() {
		if (null != this.send) {
			return this.send;
		}
		
		ValueBinding _vb = getValueBinding("send"); //$NON-NLS-1$
		if (_vb != null) {
			return (java.lang.String) _vb.getValue(getFacesContext());
		} else {
			return null;
		}	
	}

	public void setSend(java.lang.String send) {
		this.send = send;
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
        
		this.href = (String)_values[1];
		this.send = (String)_values[2];
		this.showFaces = (String)_values[3];
		this.width = (String)_values[4];
	}

	@Override
	public Object saveState(FacesContext _context) {
		Object _values[] = new Object[5];
		_values[0] = super.saveState(_context);
		_values[1] = href;
		_values[2] = send;
		_values[3] = showFaces;
		_values[4] = width;
		return _values;
	}	
}
