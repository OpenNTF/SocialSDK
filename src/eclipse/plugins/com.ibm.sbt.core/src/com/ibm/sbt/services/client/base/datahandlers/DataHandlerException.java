package com.ibm.sbt.services.client.base.datahandlers;

import com.ibm.commons.util.AbstractException;

public class DataHandlerException extends AbstractException {
	
	private static final long	serialVersionUID	= 1502495703152195504L;

	protected DataHandlerException(Throwable nextException) {
		super(nextException);
	}

	public DataHandlerException(Throwable nextException, String message) {
		super(nextException, message);
	}
	
	public DataHandlerException(Throwable nextException, String message, Object...params) {
		super(nextException, message, params);
	}
}
