package com.ibm.sbt.services.client.activitystreams;

import com.ibm.sbt.services.client.SBTServiceException;

public class ActivityStreamServiceException extends SBTServiceException {

	protected ActivityStreamServiceException(Throwable nextException, String message) {
		super(nextException, message);
		// TODO Auto-generated constructor stub
	}

	protected ActivityStreamServiceException(Throwable nextException) {
		super(nextException);
		// TODO Auto-generated constructor stub
	}
}
