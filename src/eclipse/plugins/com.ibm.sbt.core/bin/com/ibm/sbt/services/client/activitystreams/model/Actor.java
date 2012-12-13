package com.ibm.sbt.services.client.activitystreams.model;

/**
 * Actor class for persisting Author information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Actor {
	private String name;
	private String uid;
	private String type;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
