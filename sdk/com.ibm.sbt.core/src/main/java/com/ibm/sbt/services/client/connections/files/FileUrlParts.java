package com.ibm.sbt.services.client.connections.files;

import com.ibm.sbt.services.client.base.NamedUrlPart;

public enum FileUrlParts {
	accessType, action, contentType, fileId, userId, communityId, category, libraryFilter, libraryId, folderId, commentId, versionId, proxyPath, proxyName; 
	
	public NamedUrlPart get(String value){
		return new NamedUrlPart(name(), value);
	}

}
