package com.ibm.sbt.services.client.email;

/**
 * Exception thrown when there is an error dealing with MIME emails.
 *
 */
public class MimeEmailException extends Exception {

    private static final long serialVersionUID = -2821319712477114852L;
    
    /**
     * Constructs a MimeEmailExpception.
     * @param message The error message.
     */
    public MimeEmailException(String message) {
        super(message);
    }
    
    /**
     * Constructs a MimeEmailException.
     * @param message The error message.
     * @param cause The cause of the exception.
     */
    public MimeEmailException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a MimeEmailException.
     * @param cause The cause of the exception.
     */
    public MimeEmailException(Throwable cause) {
        super(cause);
    }

}
