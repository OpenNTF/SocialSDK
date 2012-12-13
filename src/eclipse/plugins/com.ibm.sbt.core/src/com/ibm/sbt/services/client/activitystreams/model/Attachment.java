package com.ibm.sbt.services.client.activitystreams.model;

/**
 * Actor class for persisting Attachment information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Attachment {
	
	private boolean isImage;
	private String summary;
	private Actor author;
	private String id;
	private String published;
	private String url;
	private String displayName;
	private Image image;
	

	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isImage() {
		return isImage;
	}
	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Actor getAuthor() {
		return author;
	}
	public void setAuthor(Actor author) {
		this.author = author;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
