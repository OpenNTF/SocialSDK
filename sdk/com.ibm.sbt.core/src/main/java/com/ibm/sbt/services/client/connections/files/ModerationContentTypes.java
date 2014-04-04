package com.ibm.sbt.services.client.connections.files;

public enum ModerationContentTypes {
DOCUMENTS, COMMENT;


	public String get() {
		return name().toLowerCase();
	}
}
