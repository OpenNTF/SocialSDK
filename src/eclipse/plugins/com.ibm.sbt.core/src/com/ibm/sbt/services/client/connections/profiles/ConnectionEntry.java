package com.ibm.sbt.services.client.connections.profiles;


import java.util.logging.Logger;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

public class ConnectionEntry extends BaseEntity{

	static final String		sourceClass	= ConnectionEntry.class.getName();
	static final Logger		logger		= Logger.getLogger(sourceClass);

	public ConnectionEntry(BaseService svc, DataHandler<?> handler) {
		super(svc, handler);
	}
	
	/**
	 * @return the title
	 */
	public String getConnectionId() {
		String selfLink = getSelfLink();
		String connectionId = selfLink.substring(selfLink.lastIndexOf("connectionId="),selfLink.lastIndexOf('&'));// check why the selfLink is not returned
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		setAsString(ConnectionEntryXPath.id, connectionId);
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return getAsString(ConnectionEntryXPath.title);
	}

	public void setTitle(String title) {
		setAsString(ConnectionEntryXPath.title, title);
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return getAsString(ConnectionEntryXPath.content);
	}

	/**
	 * @return the Contributor Name
	 */
	public String getContributorName() {
		return getAsString(ConnectionEntryXPath.contributorName);
	}

	/**
	 * @return the Contributor UserId
	 */
	public String getContributorUserId() {
		return getAsString(ConnectionEntryXPath.contributorUserId);
	}

	/**
	 * @return the Contributor Email
	 */
	public String getContributorEmail() {
		return getAsString(ConnectionEntryXPath.contributorEmail);
	}

	/**
	 * @return the Author Name
	 */
	public String getAuthorName() {
		return getAsString(ConnectionEntryXPath.authorName);
	}

	/**
	 * @return the Author UserId
	 */
	public String getAuthorUserId() {
		return getAsString(ConnectionEntryXPath.authorUserId);
	}

	/**
	 * @return the Author Email
	 */
	public String getAuthorEmail() {
		return getAsString(ConnectionEntryXPath.authorEmail);
	}
	
	public void setContent(String content) {
		setAsString(ConnectionEntryXPath.content, content);
	}

	public String getUpdated() {
		return getAsString(ConnectionEntryXPath.updated);
	}

	public String getSelfLink(){
		return getAsString(ConnectionEntryXPath.selfLinkFromEntry);
	}

	public String getEditLink(){
		return getAsString(ConnectionEntryXPath.editLinkFromEntry);
	}
	
	

}
