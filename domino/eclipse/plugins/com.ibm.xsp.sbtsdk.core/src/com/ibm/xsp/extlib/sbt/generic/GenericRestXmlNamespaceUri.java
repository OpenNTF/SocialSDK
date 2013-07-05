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

package com.ibm.xsp.extlib.sbt.generic;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

/**
 * 
 */
public class GenericRestXmlNamespaceUri extends ValueBindingObjectImpl {
	
	private String _prefix;
	private String _uri;
    
    public GenericRestXmlNamespaceUri() {
    }
    
    public GenericRestXmlNamespaceUri(String prefix, String uri) {
    	_prefix = prefix;
    	_uri = uri;
    }
	
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
        if (null != _prefix) {
            return _prefix;
        }
        ValueBinding valueBinding = getValueBinding("prefix");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
	}
	
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		_prefix = prefix;
	}
	
	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
        if (null != _uri) {
            return _uri;
        }
        ValueBinding valueBinding = getValueBinding("uri");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
	}
	
	/**
	 * @param uri The uri to set.
	 */
	public void setUri(String uri) {
		_uri = uri;
	}

	@Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[3];
        state[0] = super.saveState(context);
        state[1] = _prefix;
        state[2] = _uri;
        return state;
    }
    
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        _prefix = (String) values[1];
        _uri = (String) values[2];
    }
}
