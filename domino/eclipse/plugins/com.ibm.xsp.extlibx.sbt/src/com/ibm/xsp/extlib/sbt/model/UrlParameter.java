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

package com.ibm.xsp.extlib.sbt.model;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

/**
 * Url Parameter.
 * @author Philippe Riand
 */
public class UrlParameter extends ValueBindingObjectImpl implements Serializable {

    private static final long serialVersionUID = -1L;
    
    private String _name;
    private String _value;

    public UrlParameter() {
    }

    public UrlParameter(String name, String value) {
        this._name = name;
        this._value = value;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        if (_name != null) {
            return _name;
        }
        
        ValueBinding vb = getValueBinding("name"); //$NON-NLS-1$
        if (vb != null) {
            return (String)vb.getValue(getFacesContext());
        }

        return null;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        if (_value != null){
            return _value;
        }
        ValueBinding vb = getValueBinding("value"); //$NON-NLS-1$
        if (vb != null){
            return (String)vb.getValue(FacesContext.getCurrentInstance());
        }
        return null;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        _value = value;
    }
    
    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[3];
        state[0] = super.saveState(context);
        state[1] = _name;
        state[2] = _value;
        return state;
    }
    
    @Override
    public void restoreState(FacesContext context, Object value) {
        Object[] state = (Object[])value;
        super.restoreState(context, state[0]);
        _name = (String)state[1];
        _value = (String)state[2];
    }
}
