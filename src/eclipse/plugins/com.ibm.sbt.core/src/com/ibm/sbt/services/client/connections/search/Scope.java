package com.ibm.sbt.services.client.connections.search;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

public class Scope extends BaseEntity{
	
	/**
	 * Scope model object
	 * 
	 * @author Manish Kataria 
	 */
	
	/**
	 * Constructor
	 *  
	 * @param Scope
	 * @param ResultId
	 */
	public Scope(SearchService searchService, String id) {
		setService(searchService);
		setAsString(SearchXPath.uid, id);
	}
	
	public Scope(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getId(){
		String id = getAsString(SearchXPath.uid);
		return id;
	}
	
	public String getTitle(){
		String id = getAsString(SearchXPath.title);
		return id;
	}
	
	public String getSummary(){
		String id = getAsString(SearchXPath.summary);
		return id;
	}
	
	public String getUpdated(){
		String id = getAsString(SearchXPath.updated);
		return id;
	}
	
	public String getLink(){
		String id = getAsString(SearchXPath.searchLink);
		return id;
	}

}
