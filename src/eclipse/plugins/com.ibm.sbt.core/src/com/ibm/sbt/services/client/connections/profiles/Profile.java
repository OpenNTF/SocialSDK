/*
 * © Copyright IBM Corp. 2012
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

package com.ibm.sbt.services.client.connections.profiles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import java.lang.Object;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.XmlTextUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;

/**
 * @Represents Connections Profile
 *
 */
public class Profile implements Serializable{
   
    private static final long serialVersionUID = 2500126733627058535L;
    static final Map<String, String> xpathMap;
    private Map<String, String> fieldsMap = new HashMap<String, String>(); //this holds the values which dev sets while creating/ updating profile
    static final String sourceClass = Profile.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);

    private Document data; 
    private String reqId; // this can me userId or email. This is the field used to request for a profile
    private ProfileService profileService; // Service reference
    
    static {
	    xpathMap = new HashMap<String, String>();
	    String[][] pairs = {
	    		
	    		{"uid",					"/a:feed/a:entry/a:contributor/snx:userid"},
	    		{"name",				"/a:feed/a:entry/a:contributor/a:name"},
	    		{"email",				"/a:feed/a:entry/a:contributor/a:email"},
	    		{"photo",				"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:img[@class='photo']/@src"},
	    		{"title",				"/a:feed/a:entry/a:content/h:div/h:span/h:div[@class='title']"},
	    		{"organizationUnit",	"/a:feed/a:entry/a:content/h:div/h:span/h:div[@class='org']/h:span[@class='organization-unit']"},
	    		{"fnUrl",				"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:a[@class='fn url']/@href"},			
	    		{"telephoneNumber",		"/a:feed/a:entry/a:content/h:div/h:span/h:div[@class='tel']/h:span[@class='value']"},			
	    		{"bldgId",				"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:span[@class='x-building']"},			
	    		{"floor",				"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:span[@class='x-floor']"},
	    		{"streetAddress",		"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:div[@class='street-address']"},
	    		{"extendedAddress", 	"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:div[@class='extended-address x-streetAddress2']"},
	    		{"locality",			"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:span[@class='locality']"},
	    		{"postalCode",			"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:span[@class='postal-code']"},
	    		{"region",				"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:span[@class='region']"},
	    		{"countryName",			"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:div[@class='country-name']"},			
	    		{"soundUrl",			"/a:feed/a:entry/a:content/h:div/h:span/h:div/h:a[@class='sound url']/@href"},	
				{"summary",				"/a:feed/a:entry/a:summary"}
	    				
	    		
	    };
	    for (String[] pair : pairs) {
	        xpathMap.put(pair[0], pair[1]);
	    }
	}
    
    static com.ibm.commons.xml.NamespaceContext nameSpaceCtx = new NamespaceContext() {

		@Override
		public String getNamespaceURI(String prefix) {
			String uri;
			if(prefix.equals("h"))
				uri = "http://www.w3.org/1999/xhtml";
			else if(prefix.equals("a"))
				uri = "http://www.w3.org/2005/Atom";
			else if(prefix.equals("snx"))
				uri = "http://www.ibm.com/xmlns/prod/sn";
			else 
				uri = null;
			return uri;
		}

		// Dummy implementation - not used!
		@Override
		public Iterator<String> getPrefixes(String val) {
			return null;
		}

		// Dummy implemenation - not used!
		@Override
		public String getPrefix(String uri) {
			return null;
		}

		@Override
		public Iterator<String> getPrefixes() {
			return null;
		}
	};
    

    public Profile(ProfileService profileService, String reqId) {
    	this.profileService = profileService;
    	this.reqId = reqId;
    }

    public Object getData() {
    	return data;
    }

    public void setData(Document data) {
    	this.data = data;
    }

    public Map<String, String> getFieldsMap() {
    	return fieldsMap;
    }

    public void load()
    {
    	profileService.load(this);
    }
    
    public void update()
    {
    	profileService.updateProfile(this);
    }
    
    public String getId() {
    	String id = fieldsMap.get("uid");
    	if(id!=null)
    		return id;
    	else
    		return get("uid");
    }

	/**
     * @return the name
     */
    public String getDisplayName() {
    	String name = fieldsMap.get("name");
    	if(name!=null)
    		return name;
    	else
    		return get("name");
    }

   
    /**
     * @return the email
     */
    public String getEmail() {
    	String email = fieldsMap.get("email");
    	if(email!= null)
    		return email;
    	else
    		return get("email");
    }
    /**
     * @return the Title
     */
      
    public String getTitle() {
    	String title = fieldsMap.get("title");
    	if(title != null)
    		return title;
    	else
    		return get("title");
    }
    /**
     * @return the telepone number
     */
    
    public String getPhoneNumber() {
    	String telephoneNumber = fieldsMap.get("telephoneNumber");
    	if(telephoneNumber != null)
    		return telephoneNumber;
    	else
    		return get("telephoneNumber");
    }
    /**
     * @return the department
     */
    
    public String getDepartment() {
    	String organizationUnit = fieldsMap.get("organizationUnit");
    	if(organizationUnit != null)
    		return organizationUnit;
    	else
    		return get("organizationUnit");
	}
    
    
    /**
     * @return the thumbNailUrl
     */
    public String getThumbnailUrl() {
    	if(fieldsMap.get("photo")!=null)
    		return fieldsMap.get("photo");
    	else
    		return get("photo");
    }
    
    /**
     * @return the  profile Url
     */
    public String getProfileUrl() {
    
    	if(fieldsMap.get("fnUrl")!=null)
    		return fieldsMap.get("fnUrl");
    	else
    		return get("fnUrl");
    
    }
    
    /**
     * @return the pronunciation url
     */
    
    public String getPronunciationUrl() {
    
    	if(fieldsMap.get("soundUrl")!=null)
    		return fieldsMap.get("soundUrl");
    	else
    		return get("soundUrl");
    
    }
    
    /**
     * @return the about me
     */
    public String  getAbout() {
    	if(fieldsMap.get("summary")!=null)
    		return fieldsMap.get("summary");
    	else
    		return get("summary");
    }
    
    /**
     * @return the address
     */
   
    public Object getAddress() {
    	 HashMap<String, String> AddressMap = new HashMap<String, String>();
//    	 AddressMap.put("country",get("countryName"));
//    	 AddressMap.put("locality",get("locality"));
//    	 AddressMap.put("postalCode",get("postalCode"));
//    	 AddressMap.put("region",get("region"));
//     	 AddressMap.put("streetAddress",StringUtil.format(",", get("streetAddress"), get("extendedAddress")));
    	 AddressMap.put("building",get("bldgId"));
    	 AddressMap.put("floor",get("floor"));
    	 return AddressMap;
    }
  
     /**
     * @return the id
     */
    public String getUniqueId() {
    	return get("uid");
    }

    public String getReqId() {
    	return reqId;
    }
   
    public void setReqId(String reqId) {
    	this.reqId = reqId;
    }
    
    public void setAddress(Map<String, String> addressMap) {
    	
    	Iterator<Map.Entry<String,String>> address = addressMap.entrySet().iterator();
		while(address.hasNext()){
			Map.Entry<String,String> addressPairs = (Map.Entry<String,String>)address.next();
			
			
			if(addressPairs.getKey().equalsIgnoreCase("country")) {
				fieldsMap.put("countryName",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("locality")) {
				fieldsMap.put("locality",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("postalCode")) {
				fieldsMap.put("postalCode",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("region")) {
				fieldsMap.put("region",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("streetAddress")) {
				fieldsMap.put("streetAddress",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("building")) {
				fieldsMap.put("bldgId",addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("floor")) {
				fieldsMap.put("floor",addressPairs.getValue());
			}
			
		}
   }
    
    public void setPhoneNumber(String telephoneNumber){
    	fieldsMap.put("telephoneNumber", telephoneNumber);
    }
    public void setTitle(String title){
    	
    	fieldsMap.put("jobResp", title);
    }
    
    public void setPhotoLocation(String imgLoc){
    	fieldsMap.put("imageLocation", imgLoc);
	}

        
    public void set(String fieldName,String value) {
    	fieldsMap.put(fieldName, value);
    }
    
    /**
     * @return value for specified field. Field names follow IBM Connections naming convention
     */
    public String get(String fieldName)
    {
    	String xpQuery = getXPathQuery(fieldName);
    	return getFieldUsingXPath(xpQuery);
    }
    
    /**
     * @return xpath query for specified field. Field names follow IBM Connections naming convention
     */
    public String getXPathQuery(String fieldName)
    {	
    	return xpathMap.get(fieldName);
    }

    /**
     * @return Execute xpath query on Profile XML
     */
    public String getFieldUsingXPath(String xpathQuery)
    {
    	try {
    		return DOMUtil.value(this.data, xpathQuery, nameSpaceCtx);
    	} catch (XMLException e) {
    		logger.log(Level.SEVERE, "Error executing xpath query" , e);
    		return null;
    	} 
    }

    public void clearFieldsMap(){
    	fieldsMap.clear();
    }
    
    public String getKeyIdentifier(String fieldMapkey, String requestType){

    	String mappedKey ="";
    	if(requestType.equalsIgnoreCase("create")){
	    	if((ProfileContentMap.createFieldsIdentifierMap).get(fieldMapkey)!=null)
	    		mappedKey = (ProfileContentMap.createFieldsIdentifierMap).get(fieldMapkey);
    	}
    	else{
    		if((ProfileContentMap.updateFieldsIdentifierMap).get(fieldMapkey)!=null){
    			mappedKey = (ProfileContentMap.updateFieldsIdentifierMap).get(fieldMapkey);
    		}
    	}
    	return mappedKey;

    }
    
    public Object constructCreateRequestBody(){

    	String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"application/xml\"><person xmlns=\"http://ns.opensocial.org/2008/opensocial\"><com.ibm.snx_profiles.attrib>";

    	Iterator<Map.Entry<String,String>> entries = fieldsMap.entrySet().iterator();
    	String mappedKey = "";
    	while(entries.hasNext()){

    		Map.Entry<String,String> fieldMapPairs = (Map.Entry<String,String>)entries.next();
    		mappedKey = getKeyIdentifier(fieldMapPairs.getKey(), "create");
    		if(mappedKey!=null){
    			body += "<entry><key>" + mappedKey + "</key><value><type>text</type><data>"+ XmlTextUtil.escapeXMLChars(fieldMapPairs.getValue()) + "</data></value></entry>";
    		}
    		entries.remove();

    	}
    	body += "</com.ibm.snx_profiles.attrib></person></content></entry>";
    	return body;

    }
    
    public Object constructUpdateRequestBody(){

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns=\"http://www.w3.org/2005/Atom\"><category term=\"profile\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category><content type=\"text\">\nBEGIN:VCARD\nVERSION:2.1\n";

		Iterator<Map.Entry<String,String>> entries = fieldsMap.entrySet().iterator();
		String mappedKey = "";
		while(entries.hasNext()){
			// todo- make changes to update address
			Map.Entry<String,String> fieldMapPairs = (Map.Entry<String,String>)entries.next();
			mappedKey = getKeyIdentifier(fieldMapPairs.getKey(), "update");
		
			if(mappedKey!=null){
				body += mappedKey + ":"+ XmlTextUtil.escapeXMLChars(fieldMapPairs.getValue())+ "\n";
			}
			entries.remove();

		}
		body += "END:VCARD\n</content></entry>";
		
		return body;

	}
    
    @Override
	public String toString()
    {
    	try {
			return DOMUtil.getXMLString(data);
		} catch (XMLException e) {
			logger.log(Level.SEVERE, "Error converting xml to String" , e);
			return "";
		}
    }
    
 
}