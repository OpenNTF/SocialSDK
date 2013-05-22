package com.ibm.sbt.services.client.connections.communities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

public class Member {

	static final Map<String, String> xpath_community_Members;
	private HashMap<String, String> fieldsMap = new HashMap<String, String>(); //this holds the values which dev sets while creating/ updating profile

	private CommunityService communityService; // Service reference

	static {
		xpath_community_Members = new HashMap<String, String>();
		String[][] pairs = {
				{"entry",				"/a:feed/a:entry"},
				{"id",					"/a:entry/a:contributor/snx:userid"},
				{"name",				"/a:entry/a:contributor/a:name"},
				{"email",				"/a:entry/a:contributor/a:email"},
				{"role",				"/a:entry/snx:role"}
		};
		for (String[] pair : pairs) {
			xpath_community_Members.put(pair[0], pair[1]);
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
			// TODO Auto-generated method stub
			return null;
		}
	};



	private Document data;
	private String id; // this can be userId or email. 
	private String name;
	private String email;

	public Member(CommunityService communityService, String id) {
		this.communityService = communityService;
		this.id = id;
	}

	public Member(CommunityService communityService, String id, String name, String email) {
		this.communityService = communityService;
		this.id = id;
		this.name = name;
		this.email = email;
	}
	public Object getData() {
		return data;
	}

	public void setData(Document data) {
		this.data = data;
	}

	public static com.ibm.commons.xml.NamespaceContext getNameSpaceCtx() {
		return nameSpaceCtx;
	}

	public void setNameSpaceCtx(com.ibm.commons.xml.NamespaceContext nameSpaceCtx) {
		this.nameSpaceCtx = nameSpaceCtx;
	}

	public HashMap<String, String> getFieldsMap() {
		return fieldsMap;
	}

	public String getId() {
		String id = fieldsMap.get("id");
    	if(id!= null)
    		return id;
    	else{
    		if(this.id == null){    		
    			this.id =  get("id");
    		}
    		return this.id;
    	}
	}

	public String getName() {
		String name = fieldsMap.get("name");
    	if(name!= null)
    		return name;
    	else{
    		if(this.name == null){    		
    			this.name =  get("name");
    		}
    		return this.name;
    	}
	}

	public String getEmail() {
		String email = fieldsMap.get("email");
    	if(email!= null)
    		return email;
    	else{
    		if(this.email == null){    		
    			this.email =  get("email");
    		}
    		return this.email;
    	}
	}
	public void setName(String name) {
		fieldsMap.put("name",name);
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getRole() {
		String role = fieldsMap.get("role");
		return role;
	}
	public void setRole(String role){
		fieldsMap.put("role",role);
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
		return xpath_community_Members.get(fieldName);
	}

	/**
	 * @return Execute xpath query on Profile XML
	 */
	public String getFieldUsingXPath(String xpathQuery)
	{
		try {
			return DOMUtil.value(this.data, xpathQuery, nameSpaceCtx);
		} catch (XMLException e) {
			return null;
		}	
	}

	protected String createMemberEntry() throws ClientServicesException {

		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">";
		body += "<contributor>";
		if(isEmail(getId())){
			body += "<email>" + getId() + "</email>";
		}
		else{
			body += "<snx:userid xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" + getId() + "</snx:userid>";
		}

		body += "</contributor>";
		if(getRole() != null){
			body += "<snx:role xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" component=\"http://www.ibm.com/xmlns/prod/sn/communities\">" + getRole() + "</snx:role>";
		}
		body += "</entry>";
		
		return body;
	}

	@Override
	public String toString()
	{
		try {
			return DOMUtil.getXMLString(data);
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	private boolean isEmail(String userId)
	{
		if (StringUtil.isEmpty(userId))
			return false;
		return userId.contains("@");
	}
}
