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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.GenericService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.accessor.XmlArrayBlockAccessor;
import com.ibm.xsp.util.StateHolderUtil;

/**
 * Data source that handles Data Accessor.
 * @author Philippe Riand
 */
public class GenericRestXmlDataSource extends GenericRestDataSource {

    public static class XmlAccessor extends XmlArrayBlockAccessor {

    	private static final int FT_FIRST0  = 0;
        private static final int FT_FIRST1  = 1;
        private static final int FT_PAGE0   = 2;
        private static final int FT_PAGE1   = 3;

        private static final long serialVersionUID = 1L;
        
        private String splitPath;
        private String totalCountPath;
        private String pFirst;
        private int pFirstType;
        private String pCount;

        public XmlAccessor() {
        }
        public XmlAccessor(GenericRestDataSource ds) {
            super(ds);
            this.splitPath = ds.getSplitPath();
            this.totalCountPath = ds.getTotalCountPath();
            this.pFirst = ds.getParamFirst();
            if(StringUtil.isEmpty(pFirst)) {
                // Default
                pFirst = "first";
            }
            String ft = ds.getParamFirstType();
            if(StringUtil.equals(ft, "first0")) {
                this.pFirstType = FT_FIRST0;
            } else if(StringUtil.equals(ft, "first1")) {
                this.pFirstType = FT_FIRST1;
            } else if(StringUtil.equals(ft, "page0")) {
                this.pFirstType = FT_PAGE0;
            } else if(StringUtil.equals(ft, "page1")) {
                this.pFirstType = FT_PAGE1;
            } else {
                // Default
                this.pFirstType = FT_FIRST0;
            }
            this.pCount = ds.getParamCount();
            if(StringUtil.isEmpty(pCount)) {
                // Default
            	pCount = "count";
            }
        }
        
        protected Map<String,String> getParameters(int index, int blockSize) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.putAll(getUrlParameters());
            int page = 0;
            switch(pFirstType) {
                case FT_FIRST0: {
                    page = index*blockSize;
                } break;
                case FT_FIRST1: {
                    page = (index*blockSize)+1;
                } break;
                case FT_PAGE0: {
                    page = index;
                } break;
                case FT_PAGE1: {
                    page = index+1;
                } break;
            }
            map.put(pFirst,Integer.toString(page)); 
            map.put(pCount,Integer.toString(blockSize)); 
            return map;
        }
        
        @Override
        public NamespaceContext getNamespaceContext() {
        	// TODO..
            return null; //ConnectionDataSource.XPATH_CONTEXT;
        }
        
        @Override
        public String getEntryXPath() {
            return splitPath;
        }
        
        @Override
        public String getTotalCountXPath() {
            return totalCountPath;
        }
        
        @Override
        protected Block loadBlock(int index, int blockSize) {
            try {
            	ClientService svc = createService(findEndpointBean(),getServiceUrl());
                Map<String,String> parameters = getParameters(index, blockSize);
                HandlerXml xml = new HandlerXml();
                Document doc = (Document)svc.get(getServiceUrl(),parameters, xml);
                return new XmlBlock(index,doc);
            } catch(Exception ex) {
                throw new FacesExceptionEx(ex,"Error while reading the XML service entries");
            }
        }
        
        protected ClientService createService(Endpoint endpoint, String serviceUrl) {
        	//TODO-Padraic changed constructor
            GenericService svc = new GenericService(endpoint);
            return svc;
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(splitPath);
            out.writeObject(totalCountPath);
            out.writeObject(pFirst);
            out.writeInt(pFirstType);
            out.writeObject(pCount);
            super.writeExternal(out);
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.splitPath = (String)in.readObject();
            this.totalCountPath = (String)in.readObject();
            this.pFirst = (String)in.readObject();
            this.pFirstType = in.readInt();
            this.pCount = (String)in.readObject();
            super.readExternal(in);
        }
    }

    private List<GenericRestXmlNamespaceUri> namespaceUris;

    public GenericRestXmlDataSource() {
    }

    public List<GenericRestXmlNamespaceUri> getNamespaceUris() {
        return this.namespaceUris;
    }

    public void setNamespaceUris(List<GenericRestXmlNamespaceUri> namespaceUris) {
        this.namespaceUris = namespaceUris;
    }

    public void addNamespaceUri(GenericRestXmlNamespaceUri namespaceUri) {
        if(namespaceUris==null) {
            this.namespaceUris = new ArrayList<GenericRestXmlNamespaceUri>();
        }
        this.namespaceUris.add(namespaceUri);
    }

    @Override
    protected RestDataBlockAccessor createAccessor() {
        return new XmlAccessor(this);
    }
    
    @Override
    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }
        Object[] state = new Object[2];
        state[0] = super.saveState(context);
        state[1] = StateHolderUtil.saveList(context, namespaceUris);
        return state;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[])state;
        super.restoreState(context, values[0]);
        this.namespaceUris = StateHolderUtil.restoreList(context, getComponent(), values[1]);        
    }
}
