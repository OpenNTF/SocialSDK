package com.ibm.sbt.services.client.email;

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.io.json.JsonObject;

/**
 * An abstract implementation of MIMEEmail.  Implementors should extend this
 * class when supporting MIME emails.
 *
 */
public abstract class AbstractMimeEmail implements MimeEmail {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private List<MimePart> mimeParts;
    private String subject;

    /**
     * Constructs a MIME email.
     */
    public AbstractMimeEmail() {
        setTo(new ArrayList<String>());
        setCC(new ArrayList<String>());
        setBCC(new ArrayList<String>());
        setSubject("");
        setMimeParts(new ArrayList<MimePart>());
    }
    
    /**
     * Constructs a MIME email.
     * @param to A list of email addresses for the to field.
     * @param cc A list of email addresses for the cc field.
     * @param bcc A list of email addresses for the bcc field.
     * @param subject The subject of the email.
     * @param mimeParts A list of MIME parts to add to the email.
     */
    public AbstractMimeEmail(List<String> to, List<String> cc, List<String> bcc, 
            String subject, List<MimePart> mimeParts) {
        setSubject(subject);
        setTo(to);
        setCC(cc);
        setBCC(bcc);
        setMimeParts(mimeParts);
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
    public AbstractMimeEmail(JsonObject json) throws MimeEmailException {
        
        Object subjectObj = json.getJsonProperty(SUBJECT);
        if(subjectObj instanceof String) {
            setSubject((String)subjectObj);
        } else {
            setSubject("");
        }
        
        Object toObj = json.getJsonProperty(TO);
        if(toObj instanceof List) {
            setTo((List<String>)toObj);
        } else {
            setTo(new ArrayList<String>());
        }
        
        Object ccObj = json.getJsonProperty("cc");
        if(ccObj instanceof List) {
            setCC((List<String>)ccObj);
        } else {
            setCC(new ArrayList<String>());
        }
        
        Object bccObj = json.getJsonProperty(BCC);
        if(bccObj instanceof List) {
            setBCC((List<String>)bccObj);
        } else {
            setBCC(new ArrayList<String>());
        }
        
        Object mimePartsObj = json.getJsonProperty(MIME_PARTS);
        if(mimePartsObj instanceof List) {
            setMimeParts(createMimeParts((List<JsonObject>)mimePartsObj));
        } else {
            setMimeParts(new ArrayList<MimePart>());
        }
    }

    /**
     * Creates a list of MimePart objects from a List JsonObjects.
     * @param mimeParts  The List of JsonObjects to construct the MimePart objects from.
     * @return A list of MimePart objects.
     * @throws MimeEmailException Thrown when there is an error creating the MimePart objects.
     */
    protected List<MimePart> createMimeParts(List<JsonObject> mimeParts) throws MimeEmailException {
        List<MimePart> parts = new ArrayList<MimePart>(mimeParts.size());
        for(JsonObject json : mimeParts) {
            parts.add(new MimePart(json));
        }
        return parts;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) { 
        this.subject = (subject == null) ? "" : subject;
    }

    @Override
    public List<String> getTo() {
        return to;
    }

    @Override
    public String getCommaSeparatedTo() {
        return createCommaSeparatedList(getTo());
    }

    @Override
    public void setTo(List<String> to) {
        this.to = (to == null) ? new ArrayList<String>() : to;
    }

    @Override
    public void addToAddress(String email) {
        if(email == null) return;
        this.to.add(email);
    }

    @Override
    public void removeToAddress(String email) {
        this.to.remove(email);
    }

    @Override
    public List<String> getCC() {
        return cc;
    }

    @Override
    public String getCommaSeparatedCC() {
        return createCommaSeparatedList(getCC());
    }

    @Override
    public void setCC(List<String> cc) {
        this.cc = (cc == null) ? new ArrayList<String>() : cc;
    }

    @Override
    public void addCCAddress(String email) {
        if(email == null) return;
        this.cc.add(email);
    }

    @Override
    public void removeCCAddress(String email) {
        this.cc.remove(email);
    }

    @Override
    public List<String> getBCC() {
        return bcc;
    }

    @Override
    public String getCommaSeparatedBCC() {
        return createCommaSeparatedList(getBCC());
    }

    @Override
    public void setBCC(List<String> bcc) {
        this.bcc = (bcc == null) ? new ArrayList<String>() : bcc;
    }

    @Override
    public void addBCCAddress(String email) {
        if(email == null) return;
        this.bcc.add(email);
    }

    @Override
    public void removeBCCAddress(String email) {
        this.bcc.remove(email);
    }

    @Override
    public List<MimePart> getMimeParts() {
        return mimeParts;
    }

    @Override
    public void setMimeParts(List<MimePart> mimeParts) {
        this.mimeParts = (mimeParts == null) ? new ArrayList<MimePart>() : mimeParts;
    }

    @Override
    public void addMimePart(MimePart mimePart) {
        if(mimePart == null) return;
        this.mimeParts.add(mimePart);
    }

    @Override
    public void removeMimePart(MimePart mimePart) {
        this.mimeParts.remove(mimePart);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = true;
        if(o instanceof DefaultMimeEmail) {
            MimeEmail test = (MimeEmail)o;
            result &= test.getBCC().equals(this.getBCC());
            result &= test.getCC().equals(this.getCC());
            result &= test.getCommaSeparatedBCC().equals(this.getCommaSeparatedBCC());
            result &= test.getCommaSeparatedCC().equals(this.getCommaSeparatedCC());
            result &= test.getCommaSeparatedTo().equals(this.getCommaSeparatedTo());
            result &= test.getMimeParts().equals(this.getMimeParts());
            result &= (test.getSubject() == null) ? test.getSubject() == this.getSubject() : 
                test.getSubject().equals(this.getSubject());
            result &= test.getTo().equals(this.getTo());
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Creates a comma separated list.
     * @param items The list of items to create the list from.
     * @return A comma separated list.
     */
    private String createCommaSeparatedList(List<String> items) {
        StringBuilder list = new StringBuilder();
        for(int i = 0; i < items.size(); i++) {
            list.append(items.get(i));
            if(i != items.size() -1) {
                list.append(",");
            }
        }
        return list.toString();
    }
    
    @Override
    public abstract void send() throws MimeEmailException;

}