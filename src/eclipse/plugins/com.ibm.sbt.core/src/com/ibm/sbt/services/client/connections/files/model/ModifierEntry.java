package com.ibm.sbt.services.client.connections.files.model;

import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;


public class ModifierEntry extends PersonEntry {

	public ModifierEntry(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public void setUserUuid(String userUuId) {
		setAsString(FileEntryXPath.UserUuidModifier, userUuId);
	}
	
	public void setName(String name) {
		setAsString(FileEntryXPath.NameModifier, name);
	}

	public void setEmail(String email) {
		setAsString(FileEntryXPath.EmailModifier, email);
	}

	public void setUserState(String userState) {
		setAsString(FileEntryXPath.UserStateModifier, userState);
	}
	
	public String getEmail() {
		return dataHandler.getAsString(FileEntryXPath.EmailModifier);
	}

	public String getName() {
		return dataHandler.getAsString(FileEntryXPath.NameModifier);
	}

	public String getUserState() {
		return dataHandler.getAsString(FileEntryXPath.UserStateModifier);
	}

	public String getUserUuid() {
		return dataHandler.getAsString(FileEntryXPath.UserUuidModifier);
	}
}
