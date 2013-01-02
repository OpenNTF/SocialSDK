package com.ibm.xsp.sbtsdk.services.mail;

import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.email.AbstractMimeEmail;
import com.ibm.sbt.services.client.email.MimeEmail;
import com.ibm.sbt.services.client.email.MimeEmailException;
import com.ibm.sbt.services.client.email.MimePart;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * A default implementation of MimeEmail.
 *
 */
public class DominoMimeEmail extends AbstractMimeEmail implements MimeEmail {

	public static final String EMBED_XML = "application/embed+xml;charset=UTF-8";
    public static final String EMBED_JSON = "application/embed+json;charset=UTF-8";
    public static final String TEXT_HTML = "text/html;charset=UTF-8";
    public static final String TEXT_PLAIN = "text/plain;charset=UTF-8";
    
    /**
     * Constructs a MIME email.
     */
    public DominoMimeEmail() {
        super();
    }
    
    /**
     * Constructs a MIME email.
     * @param to A list of email addresses for the to field.
     * @param cc A list of email addresses for the cc field.
     * @param bcc A list of email addresses for the bcc field.
     * @param subject The subject of the email.
     * @param mimeParts A list of MIME parts to add to the email.
     */
    public DominoMimeEmail(List<String> to, List<String> cc, List<String> bcc, 
            String subject, List<MimePart> mimeParts) {
        super(to, cc, bcc, subject, mimeParts);
    }
    
    /**
     * Constructs a MIME email from a JSON object.  The JSON object should follow the below
     * model.
     * 
     * <code>
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
     * @param json  The JSON to create the MIME email from.
     * @throws MimeEmailException Thrown when there was an error creating the new MIME email.
     */
    public DominoMimeEmail(JsonObject json) throws MimeEmailException {
        super(json);
    }
    
    /* 
     * @see com.ibm.sbt.services.client.email.MimeEmail#send()
     */
    @Override
    public void send() throws MimeEmailException {
        if(getTo().isEmpty() && getCC().isEmpty() && getBCC().isEmpty()) {
            throw new MimeEmailException("The email's to, cc, and bcc fields are empty.");
        }
        try {
        	// Should use the log.nsf first!
            //Database db = DominoUtils.openDatabaseByName("log.nsf");
        	Database db = ExtLibUtil.getCurrentDatabase();
        	if(db==null) {
        		// Should use the log application
        		throw new MimeEmailException("Unbale to find an NSF to send the email message");
        	}
			lotus.domino.Session session = db.getParent();
			Document doc = db.createDocument();

			doc.appendItemValue("Form", "Memo"); //$NON-NLS-1$

			doc.appendItemValue("Subject", getSubject()); //$NON-NLS-1$
			doc.appendItemValue("SendTo", getCommaSeparatedTo()); //$NON-NLS-1$
			doc.appendItemValue("CopyTo", getCommaSeparatedCC()); //$NON-NLS-1$
			doc.appendItemValue("BlindCopyTo", getCommaSeparatedTo()); //$NON-NLS-1$ 

			boolean convertMime = session.isConvertMime();
			session.setConvertMIME(false);
			try { 
	
				MIMEEntity topRelated = doc.createMIMEEntity("Body"); //$NON-NLS-1$
	
				// Set up the multipart header
				MIMEHeader header = topRelated.createHeader("Content-Type"); //$NON-NLS-1$
	
				// EE needs to use multipart/alternative or the email clients will
				// turn the content into an attachment.
				header.setHeaderVal("multipart/related");
	
				MIMEEntity topAlternative = topRelated.createChildEntity();
	
				// Set up the multipart header
				header = topAlternative.createHeader("Content-Type");
	
				// EE needs to use multipart/alternative or the email clients will
				// turn the content into an attachment.
				header.setHeaderVal("multipart/alternative");
	
				try {
					for(MimePart part : getMimeParts()) {
						MIMEEntity mime = topAlternative.createChildEntity();
						try {
							lotus.domino.Stream stream = session.createStream();
							try {
								String mimeType = part.getMimeType();
								String content = part.getContent();
								stream.writeText(content);
								mime.setContentFromText(stream, mimeType, 0);
								if(StringUtil.equals(mimeType, "text/plain")) {
									mime.encodeContent(MIMEEntity.ENC_QUOTED_PRINTABLE);
								} else {
					            	mime.encodeContent(MIMEEntity.ENC_BASE64);
								}
							} finally {
								stream.close();
								stream.recycle();
							}
						} finally {
							mime.recycle();
						}
		            }
	
					
	
	// USING JAVA MAIL				
	//	            MimeMultipart mmp = new MimeMultipart("alternative");
	//	            MimeBodyPart bodyPart;
	//	            for(MimePart part : getMimeParts()) {
	//	                bodyPart = new MimeBodyPart();
	//	                bodyPart.setContent(part.getContent(), part.getMimeType());
	//	                Map<String, String> headers = part.getHeaders();
	//	                Set<String> keys = headers.keySet();
	//	                for(String key : keys) {
	//	                    bodyPart.addHeader(key, headers.get(key));
	//	                }
	//	                mmp.addBodyPart(bodyPart);
	//	            }
	//	            msg.setContent(mmp);
					
					// format of email message in regards to various parts
					// multipart/related
					// multipart/alternative
					// plain text
					// embedded experience (json or xml)
					// html
	
	// USING NOTES API				
	//				Iterator<MimeBodyPartWrapper> keyItr = mimePartList.iterator();
	//
	//				MimeBodyPart mimePart;
	//				MimeBodyPartWrapper mimePartWrapper;
	//				MIMEEntity mime;
	//				lotus.domino.Stream stream;
	//				while (keyItr.hasNext()) {
	//					mimePartWrapper = keyItr.next();
	//					mimePart = mimePartWrapper.getMimeBodyPart();
	//					// mimePart.setHeader("Content-transfer-encoding",
	//					// encodingType);
	//					mime = topAlternative.createChildEntity();
	//					// header = mime.createHeader("Content-transfer-encoding");
	//					// header.setHeaderVal(encodingType);
	//					stream = session.createStream();
	//					stream.writeText((String) mimePart.getContent());
	//
	//					// mime.setContentFromText(stream,
	//					// mimePart.getContentType(), 0);
	//					// String cType="text/plain";
	//					if (TEXT_PLAIN.equalsIgnoreCase(mimePartWrapper.getContentType())) {
	////						System.out.println("here plain text");
	//						mime.setContentFromText(stream, TEXT_PLAIN, 0);
	//						mime.encodeContent(MIMEEntity.ENC_QUOTED_PRINTABLE);
	//					} else if (EMBED_JSON.equalsIgnoreCase(mimePartWrapper.getContentType())) {
	////						System.out.println("here ee json");
	//						mime.setContentFromText(stream, EMBED_JSON, 0);
	////						System.out.println("ee json encoding: "
	////								+ mime.getEncoding()
	////								+ ", MIMEEntity.ENC_BASE64: "
	////								+ MIMEEntity.ENC_BASE64);
	//						mime.encodeContent(MIMEEntity.ENC_BASE64);
	//					} else if (EMBED_XML.equalsIgnoreCase(mimePartWrapper.getContentType())) {
	//						mime.setContentFromText(stream, EMBED_XML, 0);
	//						mime.encodeContent(MIMEEntity.ENC_BASE64);
	//					} else if (TEXT_HTML.equalsIgnoreCase(mimePartWrapper.getContentType())) {
	////						System.out.println("here html");
	//						mime.setContentFromText(stream, TEXT_HTML, 0);
	//						mime.encodeContent(MIMEEntity.ENC_QUOTED_PRINTABLE);
	//					}
	//
	//					stream.close();
	//					stream.recycle();
	//					mime.recycle();
	//				}
				} catch (Exception e) {
					// TODO: Add Logging!
					e.printStackTrace();
				}
	
				// Must happen after setting all of the mime content
				String from = null; //getFrom();
				if (StringUtil.isNotEmpty(from)) {
					doc.appendItemValue("Principal", from);
					doc.appendItemValue("iNetFrom", from);
					doc.appendItemValue("$iNetPrincipal", from);
				}
	
				doc.send(false);
				doc.recycle();
	
				// Set mime conversion back to what it was originally
			} finally {
				session.setConvertMIME(convertMime);
			}

		} catch (lotus.domino.NotesException e) {
			throw new MimeEmailException(e);
		}
    }
}
