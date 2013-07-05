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

package com.ibm.xsp.extlib.component.sametime;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.stylekit.ThemeControl;


/**
 * Sametime client.
 * <p>
 * This component generates what is needed to access the Sametime services.
 * </p>
 */
public class UISametimeClient extends UIComponentBase implements ThemeControl {

    public static final String SCRIPT_BASECOMP  = "basecomp";
    public static final String SCRIPT_LIVENAME  = "livename";
    public static final String SCRIPT_WIDGETS   = "widgets";
    
    
    // Test if the client is enabled
    public static final String ATTR_ENABLED = "extlib.sametime.enabled"; 
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
    
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.sametime.SametimeClient"; //$NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.sametime.SametimeClient"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.Sametime"; //$NON-NLS-1$

    // XPages specific properties
    private String endpoint;
    private Boolean autoLogin;
    private String loginStatus;
    private Boolean autoTunnelURI;
    private String clientScriptFile;
    private String initProxyScript;
    
    // Some sametime initialization properties
    private Boolean noHub;
    private String lang;
    private Boolean connectClient;
        
    public UISametimeClient() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getStyleKitFamily() {
        return "Sametime.Client"; // $NON-NLS-1$
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
    
    public boolean isAutoLogin() {
        if (null != this.autoLogin) {
            return this.autoLogin;
        }
        ValueBinding _vb = getValueBinding("autoLogin"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }
    
    public String getLoginStatus() {
        if (null != this.loginStatus) {
            return this.loginStatus;
        }
        ValueBinding _vb = getValueBinding("loginStatus"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
    
    public boolean isAutoTunnelURI() {
        if (null != this.autoTunnelURI) {
            return this.autoTunnelURI;
        }
        ValueBinding _vb = getValueBinding("autoTunnelURI"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setAutoTunnelURI(boolean autoTunnelURI) {
        this.autoTunnelURI = autoTunnelURI;
    }
    
    public String getClientScriptFile() {
        if (null != this.clientScriptFile) {
            return this.clientScriptFile;
        }
        ValueBinding _vb = getValueBinding("clientScriptFile"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setClientScriptFile(String clientScriptFile) {
        this.clientScriptFile = clientScriptFile;
    }
    
    public String getInitProxyScript() {
        if (null != this.initProxyScript) {
            return this.initProxyScript;
        }
        ValueBinding _vb = getValueBinding("initProxyScript"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setInitProxyScript(String initProxyScript) {
        this.initProxyScript = initProxyScript;
    }
    
    public boolean isNoHub() {
        if (null != this.noHub) {
            return this.noHub;
        }
        ValueBinding _vb = getValueBinding("noHub"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setNoHub(boolean noHub) {
        this.noHub = noHub;
    }
    
    public String getLang() {
        if (null != this.lang) {
            return this.lang;
        }
        ValueBinding _vb = getValueBinding("lang"); //$NON-NLS-1$
        if (_vb != null) {
            return (String) _vb.getValue(FacesContext.getCurrentInstance());
        } 
        return null;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public boolean isConnectClient() {
        if (null != this.connectClient) {
            return this.connectClient;
        }
        ValueBinding _vb = getValueBinding("connectClient"); //$NON-NLS-1$
        if (_vb != null) {
            Boolean val = (Boolean) _vb.getValue(FacesContext.getCurrentInstance());
            if(val!=null) {
                return val.booleanValue();
            }
        } 
        return false;
    }
    public void setConnectClient(boolean connectClient) {
        this.connectClient = connectClient;
    }
    
    @Override
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.endpoint = (String)_values[1];
        this.autoLogin = (Boolean)_values[2];
        this.loginStatus = (String)_values[3];
        this.autoTunnelURI = (Boolean)_values[4];
        this.clientScriptFile = (String)_values[5];
        this.initProxyScript = (String)_values[6];
        this.noHub = (Boolean)_values[7];
        this.lang = (String)_values[8];
        this.connectClient = (Boolean)_values[9];
    }

    @Override
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[10];
        _values[0] = super.saveState(_context);
        _values[1] = endpoint;
        _values[2] = autoLogin;
        _values[3] = loginStatus;
        _values[4] = autoTunnelURI;
        _values[5] = clientScriptFile;
        _values[6] = initProxyScript;
        _values[7] = noHub;
        _values[8] = lang;
        _values[9] = connectClient;
       return _values;
    }
}
