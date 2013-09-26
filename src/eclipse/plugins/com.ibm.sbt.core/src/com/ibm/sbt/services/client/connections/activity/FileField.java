package com.ibm.sbt.services.client.connections.activity;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class FileField extends Field{
	
	public FileField() {
		super("file");
	}
	
	public FileField(BaseService svc, DataHandler<?> handler) {
		super("file",svc, handler);
	}
	
	public FileField(String fileUrl, String fileType, int fileSize) {
		super("file");
		setFieldFileSize(fileSize);
		setFieldFileType(fileType);
		setFieldFileUrl(fileUrl);		
	}
	
	public String getFieldFileUrl() {
		return getAsString(ActivityXPath.fieldFileUrl);
	}
	
	public int getFieldFileSize() {
		return getAsInt(ActivityXPath.fieldFileSize);
	}
	
	public String getFieldFileType() {
		return getAsString(ActivityXPath.fieldFileType);
	}
	
	public void setFieldFileUrl(String fileUrl) {
		setAsString(ActivityXPath.fieldFileUrl, fileUrl);
	}
	
	public void setFieldFileSize(int size) {
		setAsString(ActivityXPath.fieldFileSize, "\"" + size + "\"");
	}
	
	public void setFieldFileType(String fileType) {
		setAsString(ActivityXPath.fieldFileType, fileType);
	}
}
