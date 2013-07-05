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

package com.ibm.xsp.extlib.sbt.connections;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.NamespaceContextImpl;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.connections.ConnectionsService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataSource;
import com.ibm.xsp.extlib.sbt.model.accessor.AtomXmlBlockAccessor;
/**
 * Data source for atom based Connections data.
 * @author Philippe Riand
 */
public class ConnectionDataSource extends RestDataSource {

    public static NamespaceContext XPATH_CONTEXT;
    static {
        NamespaceContextImpl ns = new NamespaceContextImpl();
        ns.addNamespace("a","http://www.w3.org/2005/Atom");
        ns.addNamespace("opensearch","http://a9.com/-/spec/opensearch/1.1/");
        ns.addNamespace("x","http://www.w3.org/1999/xhtml");
        
        ns.addNamespace("app","http://www.w3.org/2007/app");     
        ns.addNamespace("snx","http://www.ibm.com/xmlns/prod/sn");

        ns.addNamespace("activity","http://activitystrea.ms/spec/1.0/");

        XPATH_CONTEXT = ns;
    }
    
    public static class Accessor extends AtomXmlBlockAccessor {

        private static final long serialVersionUID = 1L;
        
        public Accessor() {} // Serialization
        public Accessor(ConnectionDataSource ds) {
            super(ds);
        }
        
        @Override
        public NamespaceContext getNamespaceContext() {
            return XPATH_CONTEXT;
        }
        
        protected Map<String,String> getParameters(int index, int blockSize) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.putAll(getUrlParameters());
            map.put("page",Integer.toString(index+1)); 
            map.put("ps",Integer.toString(blockSize)); 
            return map;
        }
        
        @Override
        protected Block loadBlock(int index, int blockSize) {
            try {
                ConnectionsService svc = createService(findEndpointBean(),getServiceUrl());
                Map<String,String> parameters = getParameters(index, blockSize);
                
                HandlerXml handler = new HandlerXml();
                Document doc = (Document)svc.get(getServiceUrl(),parameters, handler);
                
                return new XmlBlock(index,doc);
            } catch(Exception ex) {
                throw new FacesExceptionEx(ex,"Error while reading the Connections entries");
            }
        }
        
        protected ConnectionsService createService(Endpoint endpoint, String serviceUrl) {
        	//TODO Padraic
//            ConnectionsService svc = new ConnectionsService(endpoint,serviceUrl);
        	ConnectionsService svc = new ConnectionsService(endpoint);
            return svc;
        }
    }
    
    
    public ConnectionDataSource() {
    }

    @Override
    public String getDefaultEndpoint() {
        return EndpointFactory.SERVER_CONNECTIONS;
    }
    
    @Override
    protected RestDataBlockAccessor createAccessor() {
        return new Accessor(this);
    }    
}
