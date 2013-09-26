package com.ibm.sbt.services.client.connections.activity;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

public class PersonField extends Field{
	
	public PersonField() {
		super("person");
	}
	
	public PersonField(BaseService svc, DataHandler<?> handler) {
		super("person", svc, handler);
	}
	
	public PersonField(String userid, String email) {
		super("person");
		setPersonUserid(userid);
		setPersonEmail(email);
	}
	
	public PersonField(String userid, String email, String name) {
		super("person");
		setPersonUserid(userid);
		setPersonEmail(email);
		setPersonName(name);
	}

	public void setPersonUserid(String userid) {
		setAsString(ActivityXPath.fieldPersonUserId, userid);
	}
	
	public void setPersonEmail(String email) {
		setAsString(ActivityXPath.fieldPersonEmail, email);
	}
	
	public void setPersonName(String userid) {
		setAsString(ActivityXPath.fieldPersonName, userid);
	}
	
	public String getPersonUserid() {
		return getAsString(ActivityXPath.fieldPersonUserId);
	}
	
	public String getPersonEmail() {
		return getAsString(ActivityXPath.fieldPersonEmail);
	}
	
	public String getPersonName() {
		return getAsString(ActivityXPath.fieldPersonName);
	}
}
