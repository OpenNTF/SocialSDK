package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;


public class Author extends Person {
	
	public Author(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public void setUserUuid(String userUuId) {
		setAsString(FileEntryXPath.UserUuid, userUuId);
	}
	
	public void setName(String name) {
		setAsString(FileEntryXPath.UserName, name);
	}

	public void setEmail(String email) {
		setAsString(FileEntryXPath.Email, email);
	}

	public void setUserState(String userState) {
		setAsString(FileEntryXPath.UserState, userState);
	}
	
	public String getEmail() {
		return getAsString(FileEntryXPath.Email);
	}

	public String getName() {
		return getAsString(FileEntryXPath.UserName);
	}

	public String getUserState() {
		return getAsString(FileEntryXPath.UserState);
	}

	public String getUserUuid() {
		return getAsString(FileEntryXPath.UserUuid);
	}
}
