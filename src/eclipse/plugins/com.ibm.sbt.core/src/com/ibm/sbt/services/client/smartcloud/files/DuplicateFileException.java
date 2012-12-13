package com.ibm.sbt.services.client.smartcloud.files;

/**
 * Thrown when the file to be uploaded already exist
 * 
 * @author Lorenzo Boccaccia
 * @date Dec 11, 2012
 */
public class DuplicateFileException extends FileServiceException {

	public DuplicateFileException(Throwable nextException, String message) {
		super(nextException, message);

	}

	public DuplicateFileException(Throwable nextException) {
		super(nextException);

	}

	private static final long	serialVersionUID	= -3135604686100955547L;

}
