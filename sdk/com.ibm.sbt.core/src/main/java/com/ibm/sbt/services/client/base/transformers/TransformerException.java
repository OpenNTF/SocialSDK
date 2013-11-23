package com.ibm.sbt.services.client.base.transformers;

import com.ibm.sbt.services.client.SBTServiceException;

/**
 * This class represents Community Transformer Exception   
 * 
 * @author Manish Kataria
 */


public class TransformerException extends SBTServiceException {

	private static final long serialVersionUID = 8490009617440249954L;

	protected TransformerException(Throwable nextException) {
		super(nextException);
	}

	public TransformerException(Throwable nextException, String message) {
		super(nextException, message);
	}
	public TransformerException(Throwable nextException, String message, Object ...objects) {
		super(nextException, message,objects);
	}
}