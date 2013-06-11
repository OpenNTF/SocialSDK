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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.xsp.extlib.model.DataBlockAccessor;


/**
 * Data accessor holding JSON objects.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public abstract class RestDataBlockAccessor extends DataBlockAccessor {
    
    private static final long serialVersionUID = 1L;
    
    private String endpoint;
    private String serviceUrl;
    //private String authorizationBean;
    //private String url;
    private Map<String,String> urlParameters;

    public RestDataBlockAccessor() {} // Serializable
    public RestDataBlockAccessor(RestDataSource ds) {
        super(ds,ds.getMaxBlockCount());
        this.endpoint = ds.getEndpoint();
        this.serviceUrl = ds.getServiceUrl();
        //this.url = ds.getUrl();
        this.urlParameters = new HashMap<String, String>();
        List<UrlParameter> params = ds.getUrlParameters();
        if(params!=null && !params.isEmpty()) {
            for(UrlParameter p: params) {
                String name = p.getName();
                String value = p.getValue();
                urlParameters.put(name,value);
            }
        }
    }
    
    public String getServiceUrl() {
        return serviceUrl;
    }

    public com.ibm.sbt.services.endpoints.Endpoint findEndpointBean() {
        String name = getEndpoint();
        String def = null;
        if(StringUtil.isEmpty(name)) {
            def = ((RestDataSource)getDataSource()).getDefaultEndpoint();
        }
        Endpoint ep =  EndpointFactory.getEndpoint(name, def);
        return ep;
    }
    
    public String findEndpointName(){
        String name = getEndpoint();
        if(StringUtil.isEmpty(name)){
            name = ((RestDataSource)getDataSource()).getDefaultEndpoint();
        }
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public Map<String, String> getUrlParameters() {
        return urlParameters;
    }
    public void setUrlParameters(Map<String, String> urlParameters) {
        this.urlParameters = urlParameters;
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(endpoint);
        out.writeObject(serviceUrl);
//        out.writeObject(authorizationBean);
//        out.writeObject(url);
        out.writeObject(urlParameters);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        endpoint = (String)in.readObject();
        serviceUrl = (String)in.readObject();
//        authorizationBean = (String)in.readObject();
//        url = (String)in.readObject();
        urlParameters = (Map<String,String>)in.readObject();
    }
}
