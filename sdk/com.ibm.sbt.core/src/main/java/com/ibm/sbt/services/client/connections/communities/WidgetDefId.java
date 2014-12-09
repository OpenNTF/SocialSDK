package com.ibm.sbt.services.client.connections.communities;

/**
 * @author Christian Gosch, inovex GmbH
 * <br><strong>valid values:</strong> StatusUpdates, Forum, Bookmarks, Files; Blog, IdeationBlog, Activities, Wiki, Calendar, RelatedCommunities, SubcommunityNav
 */
public enum WidgetDefId {
	
	// default widgets
	StatusUpdates("StatusUpdates"),
	Forum("Forum"),
	Bookmarks("Bookmarks"),
	Files("Files"),
	// optional widgets
	Blog("Blog"),
	IdeationBlog("IdeationBlog"),
	Activities("Activities"),
	Wiki("Wiki"),
	Calendar("Calendar"),
	// derived widgets
	RelatedCommunities("RelatedCommunities"),
	SubcommunityNav("SubcommunityNav");
	
	private final String name;
	
	private WidgetDefId(String myName) {
		this.name = myName;
	}
	
	public String toString() {
		return this.name;
	}

}
