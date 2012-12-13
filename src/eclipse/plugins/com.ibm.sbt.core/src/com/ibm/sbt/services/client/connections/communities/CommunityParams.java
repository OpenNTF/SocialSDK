package com.ibm.sbt.services.client.connections.communities;

import java.util.HashMap;

public class CommunityParams {
	

	//constructor 
	public CommunityParams() {
		paramsMap = new HashMap<String, String>(); 
	}
	
	//paramsMap will contain a list of parameters to be added to the url
	private HashMap<String, String> paramsMap;
	private String email;
	private String tag;
	private String sortOrder;
	private String sortBy;
	private String search; 
	private String userid;
	private String communityUuid;
	
   public HashMap<String, String> getParameterMap() {
    	return paramsMap;
    }
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		paramsMap.put("email", email);
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
		paramsMap.put("tag", tag);
	}
	
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		paramsMap.put("sortOrder", sortOrder);
	}
	
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
		paramsMap.put("sortBy", sortBy);
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
		paramsMap.put("search", search);
	}
	public String getUserId() {
		return userid;
	}
	public void setUserId(String userid) {
		this.userid = userid;
		paramsMap.put("userid", userid);
	}
	public String getCommunityUuid() {
		return communityUuid;
	}
	public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
		paramsMap.put("communityUuid", communityUuid);
	}
	
}
