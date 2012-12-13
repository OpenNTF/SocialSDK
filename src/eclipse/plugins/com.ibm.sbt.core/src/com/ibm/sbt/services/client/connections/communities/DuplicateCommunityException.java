package com.ibm.sbt.services.client.connections.communities;

public class DuplicateCommunityException extends CommunityServiceException {

	public DuplicateCommunityException(Throwable nextException, String message) {
		super(nextException, message);
	}

	private static final long	serialVersionUID	= 5540435513912659769L;

}
