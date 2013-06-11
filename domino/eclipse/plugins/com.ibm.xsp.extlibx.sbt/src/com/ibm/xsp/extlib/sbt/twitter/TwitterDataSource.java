/*
 * © Copyright IBM Corp. 2011
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
package com.ibm.xsp.extlib.sbt.twitter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.IValue;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.sbt.util.JsonNavigator;
import com.ibm.sbt.util.XmlNavigator;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor;
import com.ibm.xsp.extlib.sbt.model.RestDataSource;
import com.ibm.xsp.extlib.sbt.model.RestObjectDataSource;
import com.ibm.xsp.extlib.sbt.services.client.TwitterService;

/**
 * @author doconnor
 * @author tony.mcguckin@ie.ibm.com
 *
 */
public class TwitterDataSource extends RestObjectDataSource {

    /**
         * @author doconnor
         *
         */
    public static class TwitterDataBlockAccessor extends RestDataBlockAccessor {
        private static final String VERSION = "1";
        private static final String FORMAT = "json";
        private static final String TWTR_MENTIONS = VERSION + "/statuses/mentions." + FORMAT;
        private static final String TWTR_RT_BY_ME = VERSION + "/statuses/retweeted_by_me." + FORMAT;
        private static final String TWTR_RT_TO_ME = VERSION + "/statuses/retweeted_to_me." + FORMAT;
        private static final String TWTR_RT_OF_ME = VERSION + "/statuses/retweets_of_me." + FORMAT;
        private static final String TWTR_TIME_LINE = VERSION + "/statuses/home_timeline." + FORMAT;
        private static final String TWTR_PUBLIC_TIME_LINE = VERSION + "/statuses/public_timeline." + FORMAT;
        private String feedType;
        private String hashTag;
        private String searchEndpoint;

        /**
         * 
         */
        public TwitterDataBlockAccessor() {
        }

        /**
         * @param ds
         */
        public TwitterDataBlockAccessor(RestDataSource ds) {
            super(ds);
            this.hashTag = ((TwitterDataSource)ds).getHashTag();
            this.feedType = ((TwitterDataSource)ds).getFeedType();
            this.searchEndpoint = ((TwitterDataSource)ds).getSearchEndpoint();
        }
        
        protected String getUrl(){
            String url = TWTR_TIME_LINE;
            if(StringUtil.equals("mentions", this.feedType)){
                url = TWTR_MENTIONS;
            }
            else if(StringUtil.equals("publicTimeLine", this.feedType)){
                url = TWTR_PUBLIC_TIME_LINE;
            }
            else if(StringUtil.equals("rtByMe", this.feedType)){
                url = TWTR_RT_BY_ME;
            }
            else if(StringUtil.equals("rtOfMe", this.feedType)){
                url = TWTR_RT_OF_ME;
            }
            else if(StringUtil.equals("rtToMe", this.feedType)){
                url = TWTR_RT_TO_ME;
            }
            return url;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.ibm.xsp.extlib.model.DataBlockAccessor#loadBlock(int, int)
         */
        @Override
        protected Block loadBlock(int index, int blockSize) {
            String statusUrl = getUrl();
            try {
                Endpoint provider = findEndpointBean();
                Map<String, String> params = getParameters(index, blockSize);
                List<TwitterEntry> entries = new ArrayList<TwitterEntry>();
                if(StringUtil.isEmpty(hashTag)){
                    ClientService svc = createClientService(provider, getServiceUrl());
                   
                    HandlerJson json  = new HandlerJson();
                    ArrayList collection = (ArrayList)svc.get(statusUrl, json);
                    
                    if(collection != null){
                        int vc = collection.size();
                        for(int i = 0; i < vc; i++) {
                            Object o = collection.get(i);
                            if(o != null){
                                JsonNavigator nav = new JsonNavigator(o);
                                TwitterEntry entry = new TwitterEntry();
                                //entry.setTweetContent(nav.stringValue("text"));
                                entry.setTitle(nav.stringValue("text"));
                                entry.setAuthor(nav.get("user").stringValue("name"));
                                entries.add(entry);
                            }
                        }
                    }
                }
                else{
                    //TODO change this to JSON to be consistent
                    //http://search.twitter.com/search.json?q=%40twitterapi
                    ClientService svc = createClientService(provider, "search.atom");
                    if(hashTag.indexOf('#') != 0){
                        hashTag = "#" + hashTag;
                    }
                    params.put("q", hashTag);
                    
                    //TODO - Padraic
                    HandlerXml xml= new HandlerXml();
                    Object doc = svc.get(null,params, xml);
                    XmlNavigator navigator = new XmlNavigator((Document)doc);
                    DataNavigator dn = navigator.get("feed/entry");
                    
                    Date date = null;
                    DateFormat dateFormat = DateFormat.getDateTimeInstance();
                    
                    for(int i = 0; i < dn.getCount(); i++){
                        TwitterEntry entry = new TwitterEntry();
                        DataNavigator entryNav = dn.get(i);
                        
                        String title = entryNav.stringValue("title");
                        entry.setTitle(title);
                        
                        String content = entryNav.stringValue("content");
                        entry.setContent(content);
                        
                        String author = entryNav.stringValue("author/name");
                        entry.setAuthor(author);
                        
                        String authorLink = entryNav.stringValue("author/uri");
                        entry.setAuthorLink(authorLink);
                        
                        String publishedDate = entryNav.stringValue("published");
                        entry.setPublishedDate(publishedDate);
                        
                        String updatedDate = entryNav.stringValue("updated");
                        entry.setUpdatedDate(updatedDate);
                        
                        // 2 links available:
                        // we determine the difference based on the rel attribute...
                        //DataNavigator linkNav = entryNav.get("link[@rel='image']/@href");
                        DataNavigator linkNav = entryNav.get("link"); //$NON-NLS-1$
                        String image = null;
                        String tweetLink = null;
                        if(null != linkNav){
                            linkNav = linkNav.selectEq("@rel", "image"); //$NON-NLS-1$ //$NON-NLS-2$
                            if(null != linkNav){
                                image = linkNav.stringValue("@href"); //$NON-NLS-1$
                            }
                            linkNav = entryNav.get("link");
                            linkNav = linkNav.selectEq("@rel", "alternate"); //$NON-NLS-1$ //$NON-NLS-2$
                            if(null != linkNav){
                                tweetLink = linkNav.stringValue("@href"); //$NON-NLS-1$
                            }
                        }
                        entry.setImage(image);
                        entry.setTweetLink(tweetLink);
                        
                        entries.add(entry);
                    }
                }
                TwitterEntry[] data = entries.toArray(new TwitterEntry[0]);
                return new ArrayBlock(index, data);
            } catch (Exception ex) {
                throw new FacesExceptionEx(ex, "Error calling twitter service with url {0}", statusUrl);
            }
        }
        
        public Endpoint findSearchEndpointBean() {
            String name = searchEndpoint;
            String def = null;
            if(StringUtil.isEmpty(name)) {
                def = ((TwitterDataSource)getDataSource()).getSearchEndpoint();
            }
            Endpoint ep =  EndpointFactory.getEndpoint(name, def);
            return ep;
        }
        
        protected TwitterService createClientService(Endpoint ep, String url){
            return new TwitterService(ep);
        }
        
        protected Map<String, String>getParameters(int index, int blockSize){
            // twitter api expects 1 based indexing
            ++index;
            
            Map<String, String> params = new HashMap<String, String>();
            
            params.put("page", String.valueOf(index));
            params.put("count", String.valueOf(blockSize));
            
            return params;
        }

        /* (non-Javadoc)
         * @see com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor#writeExternal(java.io.ObjectOutput)
         */
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            super.writeExternal(out);
            out.writeObject(feedType);
            out.writeObject(hashTag);
            out.writeObject(searchEndpoint);
        }

        /* (non-Javadoc)
         * @see com.ibm.xsp.extlib.sbt.model.RestDataBlockAccessor#readExternal(java.io.ObjectInput)
         */
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            super.readExternal(in);
            feedType = (String)in.readObject();
            hashTag = (String)in.readObject();
            searchEndpoint = (String)in.readObject();
        }
    }
    
    private String hashTag;
    private String feedType;
    private String searchEndpoint;

    /**
     * 
     */
    public TwitterDataSource() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.xsp.extlib.sbt.model.RestObjectDataSource#createAccessor()
     */
    @Override
    protected RestDataBlockAccessor createAccessor() {
        return new TwitterDataBlockAccessor(this);
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.model.RestDataSource#getDefaultEndpoint()
     */
    @Override
    public String getDefaultEndpoint() {
        return "twitter";
    }
    
    public String getHashTag() {
        if (null != hashTag) {
            return hashTag;
        }
        ValueBinding valueBinding = getValueBinding("hashTag");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    
    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
    
    public String getFeedType() {
        if (null != feedType) {
            return feedType;
        }
        ValueBinding valueBinding = getValueBinding("feedType");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    
    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }
    
    @Override
    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }
        Object[] state = new Object[4];
        state[0] = super.saveState(context);
        state[1] = hashTag;
        state[2] = feedType;
        state[3] = searchEndpoint;
        return state;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[])state;
        super.restoreState(context, values[0]);
        this.hashTag = (String)values[1];
        this.feedType = (String)values[2];
        this.searchEndpoint = (String)values[3];
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.sbt.model.RestDataSource#getServiceUrl()
     */
    @Override
    public String getServiceUrl() {
        String url = super.getServiceUrl();
        if(StringUtil.isEmpty(url)){
            url = "http://api.twitter.com";
        }
        return url;
    }

    /**
     * @return the searchEndpoint
     */
    public String getSearchEndpoint() {
        return searchEndpoint;
    }

    /**
     * @param searchEndpoint the searchEndpoint to set
     */
    public void setSearchEndpoint(String searchEndpoint) {
        this.searchEndpoint = searchEndpoint;
    }
}
