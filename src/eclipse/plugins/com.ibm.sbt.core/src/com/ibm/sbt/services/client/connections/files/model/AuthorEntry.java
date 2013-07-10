package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;


public class AuthorEntry extends PersonEntry {
	
	public AuthorEntry(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public void setUserUuid(String userUuId) {
		setAsString(FileEntryXPath.UserUuidFromEntry, userUuId);
	}
	
	public void setName(String name) {
		setAsString(FileEntryXPath.NameOfUserFromEntry, name);
	}

	public void setEmail(String email) {
		setAsString(FileEntryXPath.EmailFromEntry, email);
	}

	public void setUserState(String userState) {
		setAsString(FileEntryXPath.UserStateFromEntry, userState);
	}
	
	public String getEmail() {
		return getAsString(FileEntryXPath.EmailFromEntry);
	}

	public String getName() {
		return getAsString(FileEntryXPath.NameOfUserFromEntry);
	}

	public String getUserState() {
		return getAsString(FileEntryXPath.UserStateFromEntry);
	}

	public String getUserUuid() {
		return getAsString(FileEntryXPath.UserUuidFromEntry);
	}
}
