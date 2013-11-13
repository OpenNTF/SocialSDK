package com.ibm.sbt.services.client.connections.profiles;

/**
 * Profiles ProfileServiceException
 * 
 * @author Swati Singh
 */
import com.ibm.sbt.services.client.SBTServiceException;

public class ProfileServiceException extends SBTServiceException{
	
	private static final long serialVersionUID = 1L;
	
	protected ProfileServiceException(Throwable nextException) {
		super(nextException);
	}
	
	public ProfileServiceException(Throwable nextException, String message) {
		super(nextException, message);
	}
	
	public ProfileServiceException(Throwable nextException, String message, Object...params) {
		super(nextException, message, params);
	}

}