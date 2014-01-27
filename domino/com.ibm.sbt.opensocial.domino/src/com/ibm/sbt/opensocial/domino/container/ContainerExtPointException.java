package com.ibm.sbt.opensocial.domino.container;

/**
 * Exception for when there are errors contributing containers.
 *
 */
public class ContainerExtPointException extends Exception {

	private static final long serialVersionUID = -843143541884674751L;
	
	/**
	 * Constructs a container extension point exception.
	 * @param msg A message describing the error.
	 */
	public ContainerExtPointException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a container extension point exception.
	 * @param msg A message describing the error.
	 * @param e The exception causing the error.
	 */
	public ContainerExtPointException(String msg, Exception e){
		super(msg, e);
	}
	
	/**
	 * Constructs a container extension point exception.
	 * @param e The exception causing the error.
	 */
	public ContainerExtPointException(Exception e) {
		super(e);
	}
}
