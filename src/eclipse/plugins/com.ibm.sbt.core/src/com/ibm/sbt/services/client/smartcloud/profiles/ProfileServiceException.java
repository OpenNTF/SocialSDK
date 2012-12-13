package com.ibm.sbt.services.client.smartcloud.profiles;

import com.ibm.sbt.services.client.SBTServiceException;

public class ProfileServiceException extends SBTServiceException {

	protected ProfileServiceException(Throwable e, String message) {
		super(e, message);
		// TODO Auto-generated constructor stub
	}

	public ProfileServiceException(Throwable e) {
		super(e);
	}

	private static final long	serialVersionUID	= 8888859017749838864L;

}
