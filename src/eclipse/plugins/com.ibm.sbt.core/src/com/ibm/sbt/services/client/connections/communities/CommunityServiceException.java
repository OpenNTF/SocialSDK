package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.SBTServiceException;

/**
 * This class represents Community Service Exception   
 * 
 * @author Swati Singh
 */

public class CommunityServiceException extends SBTServiceException {

	private static final long	serialVersionUID	= -3217791404553288961L;

	protected CommunityServiceException(Throwable nextException) {
		super(nextException);
	}

	public CommunityServiceException(Throwable nextException, String message) {
		super(nextException, message);
	}
}