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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.jscript.IValue;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.GenericService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.util.JsonNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.accessor.JsonArrayBlockAccessor;

/**
 * Generic Json Rest Data Source.
 * @author Philippe Riand
 */
public class GenericRestJsonDataSource extends GenericRestDataSource {

    public static class JsonAccessor extends JsonArrayBlockAccessor {
        
        private static final int FT_FIRST0  = 0;
        private static final int FT_FIRST1  = 1;
        private static final int FT_PAGE0   = 2;
        private static final int FT_PAGE1   = 3;

        private static final long serialVersionUID = 1L;
        
        private String splitPath;
        private String pFirst;
        private int pFirstType;
        private String pCount;
        
        public JsonAccessor() {
        }
        public JsonAccessor(GenericRestJsonDataSource ds) {
            super(ds);
            this.splitPath = ds.getSplitPath();
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
            ///2.0/answers?page=1&pagesize=3&order=desc&sort=activity&site=stackoverflow   
            map.put(pFirst,Integer.toString(page)); 
            map.put(pCount,Integer.toString(blockSize)); 
            return map;
        }
        
        @Override
        protected Block loadBlock(int index, int blockSize) {
            try {
                ClientService svc = createService(findEndpointBean(),getServiceUrl());
                Map<String,String> parameters = getParameters(index, blockSize);
                //String text = (String)svc.get(parameters, ClientService.FORMAT_TEXT);
                //IValue collection = (IValue)JsonParser.fromJson(JSJson.factory,text);
                Object[] data = null; 
                HandlerJson json= new HandlerJson();
                JsonJavaObject result = (JsonJavaObject)svc.get(getServiceUrl(),parameters,json);
                if(result!=null) {
                    if(StringUtil.isNotEmpty(splitPath)) {
                        JsonNavigator nav = new JsonNavigator(result);
                        List<Object> nodes = nav.nodes(splitPath);
                        if(nodes!=null) {
                            data=nodes.toArray();
                        }
                    } 
                }
                if(data!=null) {
                    return new ArrayBlock(index,data);
                } else {
                    return new EmptyBlock(index);
                }
            } catch(Exception ex) {
                throw new FacesExceptionEx(ex,"Error while reading the JSON stream");
            }
        }
        
        protected ClientService createService(Endpoint endpoint, String serviceUrl) {
            GenericService svc = new GenericService(endpoint);
            return svc;
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            super.writeExternal(out);
            out.writeObject(splitPath);
            out.writeObject(pFirst);
            out.writeInt(pFirstType);
            out.writeObject(pCount);
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            super.readExternal(in);
            this.splitPath = (String)in.readObject();
            this.pFirst = (String)in.readObject();
            this.pFirstType = in.readInt();
            this.pCount = (String)in.readObject();
        }
    }

    public GenericRestJsonDataSource() {
    }
    
    @Override
    protected RestDataBlockAccessor createAccessor() {
        return new JsonAccessor(this);
    }
    
//    @Override
//    public Object saveState(FacesContext context) {
//        if (isTransient()) {
//            return null;
//        }
//        Object[] state = new Object[1];
//        state[0] = super.saveState(context);
//        return state;
//    }
//    @Override
//    public void restoreState(FacesContext context, Object state) {
//        Object[] values = (Object[])state;
//        super.restoreState(context, values[0]);
//    }
}
