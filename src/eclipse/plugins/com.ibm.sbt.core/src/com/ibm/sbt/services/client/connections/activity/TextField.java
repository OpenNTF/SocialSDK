package com.ibm.sbt.services.client.connections.activity;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class TextField extends Field{
	
	public TextField() {
		super("text");
	}
	
	public TextField(BaseService svc, DataHandler<?> handler) {
		super("text", svc, handler);
	}
	
	public TextField(String summary) {
		super("text");
		setTextSummary(summary);
	}
	
	public void setTextSummary(String summary) {
		setAsString(ActivityXPath.summary, summary);
	}
	
	public String getTextSummary() {
		return getAsString(ActivityXPath.summary);
	}
}
