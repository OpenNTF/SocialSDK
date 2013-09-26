package com.ibm.sbt.services.client.connections.activity;

import java.util.Date;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class DateField extends Field{

	public DateField() {
		super("date");
	}
	
	public DateField(BaseService svc, DataHandler<?> handler) {
		super("date", svc,handler);
	}
	
	public DateField(Date date) {
		super("date");
		setDate(date);
	}

	public void setDate(Date date) {
		setAsDate(ActivityXPath.field.getName(), date);
	}
	
	public Date getDate() {
		return getAsDate(ActivityXPath.field);
	}
}
