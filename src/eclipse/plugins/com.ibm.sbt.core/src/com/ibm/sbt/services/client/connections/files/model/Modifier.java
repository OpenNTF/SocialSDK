package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;


public class Modifier extends Person {

	public Modifier(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public void setUserUuid(String userUuId) {
		setAsString(FileEntryXPath.ModifierUserUuid, userUuId);
	}
	
	public void setName(String name) {
		setAsString(FileEntryXPath.ModifierName, name);
	}

	public void setEmail(String email) {
		setAsString(FileEntryXPath.ModifierEmail, email);
	}

	public void setUserState(String userState) {
		setAsString(FileEntryXPath.ModifierUserState, userState);
	}
	
	public String getEmail() {
		return dataHandler.getAsString(FileEntryXPath.ModifierEmail);
	}

	public String getName() {
		return dataHandler.getAsString(FileEntryXPath.ModifierName);
	}

	public String getUserState() {
		return dataHandler.getAsString(FileEntryXPath.ModifierUserState);
	}

	public String getUserUuid() {
		return dataHandler.getAsString(FileEntryXPath.ModifierUserUuid);
	}
}
