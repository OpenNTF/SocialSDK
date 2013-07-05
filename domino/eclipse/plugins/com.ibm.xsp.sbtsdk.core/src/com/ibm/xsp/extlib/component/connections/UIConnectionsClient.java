/*
 * © Copyright IBM Corp. 2010, 2011
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

package com.ibm.xsp.extlib.component.connections;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.stylekit.ThemeControl;


/**
 * Connections client.
 * <p>
 * This component generates what is needed to access the Connections services.
 * </p>
 */
public class UIConnectionsClient extends UIComponentBase implements ThemeControl {

    // Test if the client is enabled
    private static final String ATTR_CONNECTIONS_ENABLED       = "extlib.connections.enabled"; 
    
    public static boolean isClientEnabled(UIViewRootEx rootEx) {
        return rootEx.getAttributes().get(ATTR_CONNECTIONS_ENABLED)!=null;
    }
    public static void enableClient(UIViewRootEx rootEx, boolean enabled) {
        if(enabled) {
            rootEx.getAttributes().put(ATTR_CONNECTIONS_ENABLED,Boolean.TRUE);
        } else {
            rootEx.getAttributes().remove(ATTR_CONNECTIONS_ENABLED);
        }
    }
    
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.connections.ConnectionsClient"; //$NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.connections.ConnectionsClient"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.connections.Connections"; //$NON-NLS-1$

    private String endpoint;
    
    // Profiles
    private Boolean profilesBusinessCard;
    
    // Communities
    private Boolean communitiesBusinessCard;
    private String  initSvcConfigScript;

    private Boolean loadCSS;
    private Boolean loadDojo;
    
    // !Not published is xsp-config on purpose!
    private Boolean debug;
        
    public UIConnectionsClient() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getStyleKitFamily() {
        return "Connections.Client"; // $NON-NLS-1$
    }
    
    public String getEndpoint() {
        if (null != this.endpoint) {
            return this.endpoint;
        }
        ValueBinding _vb = getValueBinding("endpoint"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public boolean isProfilesBusinessCard() {
        if (null != this.profilesBusinessCard) {
            return this.profilesBusinessCard;
        }
        ValueBinding _vb = getValueBinding("profilesBusinessCard"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setProfilesBusinessCard(boolean profilesBusinessCard) {
        this.profilesBusinessCard = profilesBusinessCard;
    }
    
    public boolean isCommunitiesBusinessCard() {
        if (null != this.communitiesBusinessCard) {
            return this.communitiesBusinessCard;
        }
        ValueBinding _vb = getValueBinding("communitiesBusinessCard"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setCommunitiesBusinessCard(boolean communitiesBusinessCard) {
        this.communitiesBusinessCard = communitiesBusinessCard;
    }
    
    public String getInitSvcConfigScript() {
        if (null != this.initSvcConfigScript) {
            return this.initSvcConfigScript;
        }
        ValueBinding _vb = getValueBinding("initSvcConfigScript"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setInitSvcConfigScript(String initSvcConfigScript) {
        this.initSvcConfigScript = initSvcConfigScript;
    }

    public boolean isLoadCSS() {
        if (null != this.loadCSS) {
            return this.loadCSS;
        }
        ValueBinding _vb = getValueBinding("loadCSS"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setLoadCSS(boolean loadCSS) {
        this.loadCSS = loadCSS;
    }

    public boolean isLoadDojo() {
        if (null != this.loadDojo) {
            return this.loadDojo;
        }
        ValueBinding _vb = getValueBinding("loadDojo"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setLoadDojo(boolean loadDojo) {
        this.loadDojo = loadDojo;
    }

    public boolean isDebug() {
        if (null != this.debug) {
            return this.debug;
        }
        ValueBinding _vb = getValueBinding("debug"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    @Override
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.endpoint = (String)_values[1];
        this.profilesBusinessCard = (Boolean)_values[2];
        this.communitiesBusinessCard = (Boolean)_values[3];
        this.initSvcConfigScript = (String)_values[4];
        this.loadCSS = (Boolean)_values[5];
        this.loadDojo = (Boolean)_values[6];
        this.debug = (Boolean)_values[7];
    }

    @Override
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[8];
        _values[0] = super.saveState(_context);
        _values[1] = endpoint;
        _values[2] = profilesBusinessCard;
        _values[3] = communitiesBusinessCard;
        _values[4] = initSvcConfigScript;
        _values[5] = loadCSS;
        _values[6] = loadDojo;
        _values[7] = debug;
       return _values;
    }
}
