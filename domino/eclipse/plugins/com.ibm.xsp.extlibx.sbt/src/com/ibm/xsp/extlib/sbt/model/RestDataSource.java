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

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.extlib.model.DataAccessorBlockSource;
import com.ibm.xsp.util.StateHolderUtil;

/**
 * Data source used to access REST service.
 * @author Philippe Riand
 */
public abstract class RestDataSource extends DataAccessorBlockSource {
    
    // Service access
    private String endpoint;
    private String serviceUrl;
    private List<UrlParameter> urlParameters;

    public RestDataSource() {
    }
    
    public String getDefaultEndpoint() {
        return null;
    }
    
    @Override
    protected abstract RestDataBlockAccessor createAccessor();
    
    
    @Override
    protected String composeUniqueId() {
        // Override to identify the datasource data
        StringBuilder b = new StringBuilder();
        b.append(super.composeUniqueId());
        b.append('|');
        b.append(getEndpoint());
        b.append('|');
        b.append(getServiceUrl());
        return b.toString();
    }
    
    public String getEndpoint() {
        if (null != endpoint) {
            return endpoint;
        }
        ValueBinding valueBinding = getValueBinding("endpoint");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getServiceUrl() {
        if (null != serviceUrl) {
            return serviceUrl;
        }
        ValueBinding valueBinding = getValueBinding("serviceUrl");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public List<UrlParameter> getUrlParameters() {
        return this.urlParameters;
    }
    
    public void addUrlParameter(UrlParameter attribute) {
        if(urlParameters==null) {
            urlParameters = new ArrayList<UrlParameter>();
        }
        urlParameters.add(attribute);
    }

    public void setUrlParameters(List<UrlParameter> parameters) {
        this.urlParameters = parameters;
    }
    
    @Override
    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }
        Object[] state = new Object[4];
        state[0] = super.saveState(context);
        state[1] = endpoint;
        state[2] = serviceUrl;
        state[3] = StateHolderUtil.saveList(context, urlParameters);
        return state;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[])state;
        super.restoreState(context, values[0]);
        this.endpoint = (String)values[1];
        this.serviceUrl = (String)values[2];
        this.urlParameters = StateHolderUtil.restoreList(context, getComponent(), values[3]);
    }
}
