package com.ibm.sbt.services.client.activitystreams.model;

/**
 * Actor class for persisting Recommendation (Like/UnLike) information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Recommendation {
	
	private Actor Author;
	private String id;
	
	
	public Actor getAuthor() {
		return Author;
	}
	public void setAuthor(Actor author) {
		Author = author;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}
