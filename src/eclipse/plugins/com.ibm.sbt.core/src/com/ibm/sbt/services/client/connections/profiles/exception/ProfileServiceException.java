package com.ibm.sbt.services.client.connections.profiles.exception;

/**
 * Files ProfileServiceException
 * @author Swati Singh
 */
import com.ibm.commons.util.AbstractException;

public class ProfileServiceException extends AbstractException{
	private static final long serialVersionUID = 1L;

	public ProfileServiceException(Throwable nextException) {
        super(nextException);
    }
    
    public ProfileServiceException(Throwable nextException, String msg, Object...params) {
        super(nextException, msg, params);
    }

}
