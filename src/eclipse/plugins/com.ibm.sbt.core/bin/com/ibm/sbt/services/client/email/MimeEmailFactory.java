package com.ibm.sbt.services.client.email;

import java.util.List;

import com.ibm.commons.util.io.json.JsonObject;

/**
 * Creates instances of MimeEmail objects.
 *
 */
public interface MimeEmailFactory {
    
    /**
     * Creates a MimeEmail from a JSON object.  The JSON should follow the below
     * model.
     * <code>
     *   {
     *     "to" : ["***REMOVED***@renovations.com", "tamado@renovations.com"],
     *     "cc" : ["pclemmons@renovations.com"],
     *     "bcc" : [],
     *     "subject" : "This is a test email"
     *     "mimeParts" : 
           [
     *       {
     *         "mimeType" : "text/plain",
     *         "content" : "This is plain text"
     *         "headers" : 
     *         {
     *           "header1":"value1", 
     *           "header2":"value2"
     *         }
     *       },
     *       {
     *         "mimeType" : "text/html",
     *         "content" : "<b>This is html</b>"
     *       },
     *       {
     *         "mimeType" : "application/embed+json",
     *         "content" : {
     *           "gadget" : "http://renovations.com/gadget.xml",
     *           "context" : {
     *             "foo" : "bar"
     *           }
     *         }
     *       }
     *     ]
     *   }
     *
     * </code>
     * @param json The JSON object to create the email from.
     * @return A MimeEmail object.
     * @throws MimeEmailException Thrown if the MIME email cannot be created.
     */
    public MimeEmail createMimeEmail(JsonObject json) throws MimeEmailException;
    
    /**
     * Creates a MimeEmail object.
     * @param to The to field of the MIME email.
     * @param cc The cc field of the MIME email.
     * @param bcc The bcc field of the MIME email.
     * @param subject The subject of the MIME email.
     * @param mimeParts The MIME parts of the MIME email.
     * @return A MimeEmail object.
     * @throws MimeEmailException Thrown if the MIME email cannot be created.
     */
    public MimeEmail createMimeEmail(List<String> to, List<String> cc, List<String> bcc,
            String subject, List<MimePart> mimeParts) throws MimeEmailException;

}
