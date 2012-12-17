package com.ibm.sbt.services.client.connections.communities;

import java.util.HashMap;
/**
 * This file represents all the parameters that are supported by Connections Community Service
 * to get the list of Communities or community related data  
 * 
 * @author Swati Singh
 */
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
   /**
	 * getEmail
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * sets email 
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
		paramsMap.put("email", email);
	}
	/**
	 * getTag
	 * 
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * sets tag 
	 * 
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
		paramsMap.put("tag", tag);
	}
	/**
	 * getSortOrder
	 * 
	 * @return sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	/**
	 * sets sortOrder 
	 * 
	 * @param sortOrder
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		paramsMap.put("sortOrder", sortOrder);
	}
	/**
	 * getSortBy
	 * 
	 * @return sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}
	/**
	 * sets sortBy 
	 * 
	 * @param sortBy
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
		paramsMap.put("sortBy", sortBy);
	}
	/**
	 * getSearch
	 * 
	 * @return search
	 */
	public String getSearch() {
		return search;
	}
	/**
	 * sets search 
	 * 
	 * @param search
	 */
	public void setSearch(String search) {
		this.search = search;
		paramsMap.put("search", search);
	}
	/**
	 * getUserId
	 * 
	 * @return userid
	 */
	public String getUserId() {
		return userid;
	}
	/**
	 * sets userid
	 * 
	 * @param userid
	 */
	public void setUserId(String userid) {
		this.userid = userid;
		paramsMap.put("userid", userid);
	}
	/**
	 * getCommunityUuid
	 * 
	 * @return communityUuid
	 */
	public String getCommunityUuid() {
		return communityUuid;
	}
	/**
	 * sets communityUuid
	 * 
	 * @param communityUuid
	 */
	public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
		paramsMap.put("communityUuid", communityUuid);
	}
	
}
