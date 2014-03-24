package com.ibm.sbt.services.client.connections.wikis;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum WikiUrlParts {
	wikiLabel, wikiPage;

	public NamedUrlPart get(String value){
		return new NamedUrlPart(name(), value);
	}
}
