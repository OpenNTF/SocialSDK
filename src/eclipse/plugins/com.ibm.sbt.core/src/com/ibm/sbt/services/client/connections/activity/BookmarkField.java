package com.ibm.sbt.services.client.connections.activity;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class BookmarkField extends Field{
	
	public BookmarkField() {
		super("link");
	}
	
	public BookmarkField(BaseService svc, DataHandler<?> handler) {
		super("link", svc, handler);
	}
	
	public BookmarkField(String bookmarkUrl, String bookmarkTitle) {
		super("link");
		setFieldLinkUrl(bookmarkUrl);
		setFieldLinkTitle(bookmarkTitle);
	}
	
	public String getFieldLinkUrl() {
		return getAsString(ActivityXPath.fieldLinkUrl);
	}
	
	public String getFieldLinkTitle() {
		return getAsString(ActivityXPath.fieldLinkTitle);
	}
	
	public void setFieldLinkUrl(String linkUrl) {
		setAsString(ActivityXPath.fieldLinkUrl, linkUrl);
	}
	
	public void setFieldLinkTitle(String linkTitle) {
		setAsString(ActivityXPath.fieldLinkTitle, linkTitle);
	}
	
}
