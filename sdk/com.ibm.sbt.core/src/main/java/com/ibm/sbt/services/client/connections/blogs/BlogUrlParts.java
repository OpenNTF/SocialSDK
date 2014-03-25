package com.ibm.sbt.services.client.connections.blogs;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum BlogUrlParts {
	blogHandle, entryAnchor;
	
	public NamedUrlPart get(String value){
		return new NamedUrlPart(name(), value);
	}
}
