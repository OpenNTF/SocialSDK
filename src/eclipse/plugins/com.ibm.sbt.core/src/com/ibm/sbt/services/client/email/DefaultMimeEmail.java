package com.ibm.sbt.services.client.email;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.smartcloud.files.FileService;

/**
 * A default implementation of MimeEmail.
 */
public class DefaultMimeEmail extends AbstractMimeEmail implements MimeEmail {

    private static final String sourceClass = DefaultMimeEmail.class.getName();
    private static final Logger logger      = Logger.getLogger(sourceClass);

    /**
     * Constructs a MIME email.
     */
    public DefaultMimeEmail() {
        super();
    }

    /**
     * Constructs a MIME email.
     * 
     * @param to A list of email addresses for the to field.
     * @param cc A list of email addresses for the cc field.
     * @param bcc A list of email addresses for the bcc field.
     * @param subject The subject of the email.
     * @param mimeParts A list of MIME parts to add to the email.
     */
    public DefaultMimeEmail(List<String> to, List<String> cc, List<String> bcc,
            String subject, List<MimePart> mimeParts) {
        super(to, cc, bcc, subject, mimeParts);
    }

    /**
     * Constructs a MIME email from a JSON object. The JSON object should follow the below model. <code>
     * 
     *   {
     *     "to" : ["fadams@renovations.com", "tamado@renovations.com"],
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
     * 
     * @param json The JSON to create the MIME email from.
     * @throws MimeEmailException Thrown when there was an error creating the new MIME email.
     */
    public DefaultMimeEmail(JsonObject json) throws MimeEmailException {
        super(json);
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.sbt.services.client.email.MimeEmail#send()
     */
    @Override
    public void send() throws MimeEmailException {
        if (getTo().isEmpty() && getCC().isEmpty() && getBCC().isEmpty()) {
            throw new MimeEmailException(
                    "The email's to, cc, and bcc fields are empty.");
        }

        try {
            Session session = null;
            try {
                // Lookup a session at the default name, if no session is found
                // lookup a session factory and try to create a new one

                InitialContext initCtx = new InitialContext();
                Object obj = initCtx.lookup(EnvironmentConfig.INSTANCE
                        .getSbtSessionResource());
                if ((obj instanceof Session)) {
                    session = (Session) obj;
                }
            } catch (NameNotFoundException ex) {
                logger.info(StringUtil.format(
                        "JavaMail Session not bound in context at {0}, creating one from MailSessionFactory",
                        EnvironmentConfig.INSTANCE.getSbtSessionResource()));
            }
            if (session == null) {
                Context appCtx = Context.get();
                Object obj = appCtx.getBean(EnvironmentConfig.INSTANCE
                        .getSbtSessionFactoryResource());
                if (!(obj instanceof MailSessionFactory)) {
                    throw new MimeEmailException(
                            "Could not get valid Session object.");
                }
                session = ((MailSessionFactory) obj).getInstance();
            }

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(getCommaSeparatedTo(), true));
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(getCommaSeparatedCC(), true));
            msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(getCommaSeparatedBCC(), true));
            msg.setSubject(getSubject());
            msg.setSentDate(new Date());

            MimeMultipart mmp = new MimeMultipart("alternative");
            MimeBodyPart bodyPart;
            for (MimePart part : getMimeParts()) {
                bodyPart = new MimeBodyPart();
                bodyPart.setContent(part.getContent(), part.getMimeType());
                Map<String, String> headers = part.getHeaders();
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    bodyPart.addHeader(key, headers.get(key));
                }
                mmp.addBodyPart(bodyPart);
            }
            msg.setContent(mmp);

            // Send The Message
            Transport.send(msg);
        } catch (Exception e) {
            throw new MimeEmailException(e);
        }
    }
}
