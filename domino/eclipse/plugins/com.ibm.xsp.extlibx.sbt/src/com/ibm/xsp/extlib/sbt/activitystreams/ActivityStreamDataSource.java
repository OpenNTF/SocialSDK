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

package com.ibm.xsp.extlib.sbt.activitystreams;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.util.XMIConverter;
import com.ibm.jscript.IValue;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.XmlNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.connections.ConnectionDataSource;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataSource;
import com.ibm.xsp.extlib.sbt.model.accessor.AtomXmlBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.accessor.JsonArrayBlockAccessor;

/**
 * Data source that handles Data Accessor.
 * @author Philippe Riand
 */
public class ActivityStreamDataSource extends RestDataSource {
    
    public static class ASJsonAccessor extends JsonArrayBlockAccessor {

        private static final long serialVersionUID = 1L;
        
        // Coming from the data source
        private String userId;
        private String groupId;
        private String appId;
        private String activityId;
        
        private String updatedSince;
        private String sortOrder;
        private String filterBy;
        private String filterOp;
        private String filterValue;
        
        public ASJsonAccessor() {
        }
        public ASJsonAccessor(ActivityStreamDataSource ds) {
            super(ds);
            this.userId = ds.getUserId(); 
            this.groupId = ds.getGroupId(); 
            this.appId = ds.getAppId(); 
            this.activityId = ds.getActivityId();
            Object us = ds.getUpdatedSince();
            if(us!=null) {
                if(us instanceof Number) {
                    this.updatedSince = XMIConverter.composeDate(((Number)us).longValue());
                } else if(us instanceof Date) {
                    this.updatedSince = XMIConverter.composeDate(((Date)us).getTime());
                } else {
                    this.updatedSince = us.toString();
                }
            }
            this.sortOrder = ds.getSortOrder(); 
            this.filterBy = ds.getFilterBy(); 
            this.filterOp = ds.getFilterOp(); 
            this.filterValue = ds.getFilterValue(); 
        }
        
        protected Map<String,String> getParameters(int index, int blockSize) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.putAll(getUrlParameters());
            map.put("startIndex",Integer.toString(index+1)); 
            map.put("count",Integer.toString(blockSize)); 
            if(userId!=null) map.put("userId",userId); 
            if(groupId!=null) map.put("groupId",groupId); 
            if(appId!=null) map.put("appId",appId); 
            if(activityId!=null) map.put("activityId",activityId); 
            if(updatedSince!=null) map.put("updatedSince",updatedSince); 
            if(sortOrder!=null) map.put("sortOrder",sortOrder); 
            if(filterBy!=null) map.put("filterBy",filterBy); 
            if(filterOp!=null) map.put("filterOp",filterOp); 
            if(filterValue!=null) map.put("filterValue",filterValue); 
            map.put("format","json");
            return map;
        }
        
        @Override
        protected Block loadBlock(int index, int blockSize) {
            try {
            	
                ActivityStreamService svc = createService(findEndpointBean(),getServiceUrl());
                Map<String,String> parameters = getParameters(index, blockSize);

                //TODO Padraic Is this correct?
                DataNavigator.Json nav = new DataNavigator.Json(svc.getAllUpdatesStream(parameters)); // this.data has the response feed.
    			DataNavigator entry = nav.get("list");
                
                Object[] data = new Object[entry.getCount()];
                for(int i=0; i<data.length; i++) {
                    data[i] = entry.get(i);
                }
                return new ArrayBlock(index,data);
            } catch(Exception ex) {
                throw new FacesExceptionEx(ex,"Error while reading the Activity Streams");
            }
        }
        
        protected ActivityStreamService createService(Endpoint endpoint, String serviceUrl) {
        	//TODO Padraic service URL ?
        	ActivityStreamService svc = new ActivityStreamService();
        	//ActivityStreamService svc = new ActivityStreamService(enpoint,serviceUrl);
        	
            return svc;
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            super.writeExternal(out);
            out.writeObject(userId);
            out.writeObject(groupId);
            out.writeObject(appId);
            out.writeObject(activityId);
            out.writeObject(updatedSince);
            out.writeObject(sortOrder);
            out.writeObject(filterBy);
            out.writeObject(filterOp);
            out.writeObject(filterValue);
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            super.readExternal(in);
            userId = (String)in.readObject();
            groupId = (String)in.readObject();
            appId = (String)in.readObject();
            activityId = (String)in.readObject();
            updatedSince = (String)in.readObject();
            sortOrder = (String)in.readObject();
            filterBy = (String)in.readObject();
            filterOp = (String)in.readObject();
            filterValue = (String)in.readObject();
        }
    }
    public static class ASXmlAccessor extends AtomXmlBlockAccessor {

        private static final long serialVersionUID = 1L;
        
        // Coming from the data source
        private String userId;
        private String groupId;
        private String appId;
        private String activityId;
        
        private String updatedSince;
        private String sortOrder;
        private String filterBy;
        private String filterOp;
        private String filterValue;
        
        public ASXmlAccessor() {
        }
        public ASXmlAccessor(ActivityStreamDataSource ds) {
            super(ds);
            this.userId = ds.getUserId(); 
            this.groupId = ds.getGroupId(); 
            this.appId = ds.getAppId(); 
            this.activityId = ds.getActivityId();
            Object us = ds.getUpdatedSince();
            if(us!=null) {
                if(us instanceof Number) {
                    this.updatedSince = XMIConverter.composeDate(((Number)us).longValue());
                } else if(us instanceof Date) {
                    this.updatedSince = XMIConverter.composeDate(((Date)us).getTime());
                } else {
                    this.updatedSince = us.toString();
                }
            }
            this.sortOrder = ds.getSortOrder(); 
            this.filterBy = ds.getFilterBy(); 
            this.filterOp = ds.getFilterOp(); 
            this.filterValue = ds.getFilterValue(); 
        }
        
        protected Map<String,String> getParameters(int index, int blockSize) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.putAll(getUrlParameters());
            map.put("startIndex",Integer.toString(index+1)); 
            map.put("count",Integer.toString(blockSize)); 
            if(userId!=null) map.put("userId",userId); 
            if(groupId!=null) map.put("groupId",groupId); 
            if(appId!=null) map.put("appId",appId); 
            if(activityId!=null) map.put("activityId",activityId); 
            if(updatedSince!=null) map.put("updatedSince",updatedSince); 
            if(sortOrder!=null) map.put("sortOrder",sortOrder); 
            if(filterBy!=null) map.put("filterBy",filterBy); 
            if(filterOp!=null) map.put("filterOp",filterOp); 
            if(filterValue!=null) map.put("filterValue",filterValue); 
            map.put("format","atom");
            return map;
        }
        
        
        @Override
        public NamespaceContext getNamespaceContext() {
            return ConnectionDataSource.XPATH_CONTEXT;
        }
                
        @Override
        protected Block loadBlock(int index, int blockSize) {
            try {
            	
                ActivityStreamService svc = createService(findEndpointBean(),getServiceUrl());
                Map<String,String> parameters = getParameters(index, blockSize);
                

                //TODO ActivityStream DataSource as XML? 
//                XmlNavigator.Xml nav = new XmlNavigator.Xml(svc.get); // this.data has the response feed.
//    			DataNavigator entry = nav.get("list");
//                
//                
//               Document doc = (Document)svc.get(parameters, ClientService.FORMAT_XML);
                
//                return new XmlBlock(index,doc);
                return null;
            } catch(Exception ex) {
                throw new FacesExceptionEx(ex,"Error while reading the Activity Streams");
            }
        }
        
        protected ActivityStreamService createService(Endpoint endpoint, String serviceUrl) {
        	//TODO Padraic
            ActivityStreamService svc = new ActivityStreamService();
            return svc;
        }
        
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            super.writeExternal(out);
            out.writeObject(userId);
            out.writeObject(groupId);
            out.writeObject(appId);
            out.writeObject(activityId);
            out.writeObject(updatedSince);
            out.writeObject(sortOrder);
            out.writeObject(filterBy);
            out.writeObject(filterOp);
            out.writeObject(filterValue);
        }
        
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            super.readExternal(in);
            userId = (String)in.readObject();
            groupId = (String)in.readObject();
            appId = (String)in.readObject();
            activityId = (String)in.readObject();
            updatedSince = (String)in.readObject();
            sortOrder = (String)in.readObject();
            filterBy = (String)in.readObject();
            filterOp = (String)in.readObject();
            filterValue = (String)in.readObject();
        }
    }

    // Keywords
    private String userId;
    private String groupId;
    private String appId;
    private String activityId;

    private Object updatedSince;
    private String format;
    private String sortOrder;
    private String filterBy;
    private String filterOp;
    private String filterValue;

    public ActivityStreamDataSource() {
    }
    
    @Override
    protected RestDataBlockAccessor createAccessor() {
        String format = getFormat();
        if(StringUtil.isEmpty(format) || StringUtil.equals(format, "json")) {
            return new ASJsonAccessor(this);
        }
        if(StringUtil.equals(format, "atom")) {
            return new ASXmlAccessor(this);
        }
        throw new FacesExceptionEx(null,"Unknown ActivityStreams format {0}",format);
    }

    public String getUserId() {
        if (null != userId) {
            return userId;
        }
        ValueBinding valueBinding = getValueBinding("userId");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        if (null != groupId) {
            return groupId;
        }
        ValueBinding valueBinding = getValueBinding("groupId");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAppId() {
        if (null != appId) {
            return appId;
        }
        ValueBinding valueBinding = getValueBinding("appId");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getActivityId() {
        if (null != activityId) {
            return activityId;
        }
        ValueBinding valueBinding = getValueBinding("activityId");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Object getUpdatedSince() {
        if (null != updatedSince) {
            return updatedSince;
        }
        ValueBinding valueBinding = getValueBinding("updatedSince");
        if (valueBinding != null) {
            Object value = valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setUpdatedSince(Object updatedSince) {
        this.updatedSince = updatedSince;
    }

    public String getFormat() {
        if (null != format) {
            return format;
        }
        ValueBinding valueBinding = getValueBinding("format");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    public String getSortOrder() {
        if (null != sortOrder) {
            return sortOrder;
        }
        ValueBinding valueBinding = getValueBinding("sortOrder");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getFilterBy() {
        if (null != filterBy) {
            return filterBy;
        }
        ValueBinding valueBinding = getValueBinding("filterBy");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getFilterOp() {
        if (null != filterOp) {
            return filterOp;
        }
        ValueBinding valueBinding = getValueBinding("filterOp");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setFilterOp(String filterOp) {
        this.filterOp = filterOp;
    }

    public String getFilterValue() {
        if (null != filterValue) {
            return filterValue;
        }
        ValueBinding valueBinding = getValueBinding("filterValue");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
    
    @Override
    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }
        Object[] state = new Object[11];
        state[0] = super.saveState(context);
        state[1] = userId;
        state[2] = groupId;
        state[3] = appId;
        state[4] = activityId;
        state[5] = updatedSince;
        state[6] = format;
        state[7] = sortOrder;
        state[8] = filterBy;
        state[9] = filterOp;
        state[10] = filterValue;
        return state;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[])state;
        super.restoreState(context, values[0]);
        userId = (String)values[1];
        groupId = (String)values[2];
        appId = (String)values[3];
        activityId = (String)values[4];
        updatedSince = values[5];
        format = (String)values[6];
        sortOrder = (String)values[7];
        filterBy = (String)values[8];
        filterOp = (String)values[9];
        filterValue = (String)values[10];
    }
}
