package com.ibm.xsp.sbtsdk.services.mail;

import java.util.List;

import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.email.MimeEmail;
import com.ibm.sbt.services.client.email.MimeEmailException;
import com.ibm.sbt.services.client.email.MimeEmailFactory;
import com.ibm.sbt.services.client.email.MimePart;

/**
 * A default implementation of MimeEmailFactory.  This uses DefaultMimeEmail as the underlying implementation for MimeEmail.
 *
 */
public class DominoMimeEmailFactory implements MimeEmailFactory {
    
    private static MimeEmailFactory instance;
    
    private DominoMimeEmailFactory() {};

    public MimeEmail createMimeEmail(JsonObject json) throws MimeEmailException {
        return new DominoMimeEmail(json);
    }

    public MimeEmail createMimeEmail(List<String> to, List<String> cc,
            List<String> bcc, String subject, List<MimePart> mimeParts) throws MimeEmailException {
        return new DominoMimeEmail(to, cc, bcc, subject, mimeParts);
    }
    
    /**
     * Gets an instance of DefaultMimeEmailFactory.
     * @return An instance of DefaultMimeEmailFactory.
     */
    public static MimeEmailFactory getInstance() {
        if(instance == null) {
            instance = new DominoMimeEmailFactory();
        }
        return instance;
    }
}
