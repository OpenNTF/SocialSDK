package com.ibm.sbt.services.client.email;

import java.util.List;

import com.ibm.commons.util.io.json.JsonObject;

/**
 * A default implementation of MimeEmailFactory.  This uses DefaultMimeEmail as the underlying implementation for MimeEmail.
 *
 */
public class DefaultMimeEmailFactory implements MimeEmailFactory {
    
    private static MimeEmailFactory instance;
    
    private DefaultMimeEmailFactory() {};

    @Override
    public MimeEmail createMimeEmail(JsonObject json) throws MimeEmailException {
        return new DefaultMimeEmail(json);
    }

    @Override
    public MimeEmail createMimeEmail(List<String> to, List<String> cc,
            List<String> bcc, String subject, List<MimePart> mimeParts) throws MimeEmailException {
        return new DefaultMimeEmail(to, cc, bcc, subject, mimeParts);
    }
    
    /**
     * Gets an instance of DefaultMimeEmailFactory.
     * @return An instance of DefaultMimeEmailFactory.
     */
    public static MimeEmailFactory getInstance() {
        if(instance == null) {
            instance = new DefaultMimeEmailFactory();
        }
        return instance;
    }

}
