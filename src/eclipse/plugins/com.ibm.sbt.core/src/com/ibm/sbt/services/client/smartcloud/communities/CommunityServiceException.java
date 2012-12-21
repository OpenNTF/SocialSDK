package com.ibm.sbt.services.client.smartcloud.communities;

import com.ibm.commons.util.AbstractException;

public class CommunityServiceException extends AbstractException {

	private static final long	serialVersionUID	= 2253492365193721975L;

	public enum Reason {
		CLIENT_ERROR, SERVER_ERROR, DUPLICATE_COMMUNITY, INVALID_INPUT
	}

	public CommunityServiceException(Throwable nextException, String message, Reason code) {
		// this forces developers to always provide a message
		super(nextException, message);
	}

}
