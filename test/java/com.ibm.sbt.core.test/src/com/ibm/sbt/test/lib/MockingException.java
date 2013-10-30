package com.ibm.sbt.test.lib;


public class MockingException extends RuntimeException {

	private static final long serialVersionUID = -7507205783644122407L;
	
	public MockingException(Throwable cause, String message) {
		super(message, cause);
	}
	

}
