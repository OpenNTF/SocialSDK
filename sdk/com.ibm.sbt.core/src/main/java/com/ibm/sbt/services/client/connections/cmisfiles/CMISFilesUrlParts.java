package com.ibm.sbt.services.client.connections.cmisfiles;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum CMISFilesUrlParts {
	repositoryId;
	
	public NamedUrlPart get(String value){
		return new NamedUrlPart(name(), value);
	}
}
