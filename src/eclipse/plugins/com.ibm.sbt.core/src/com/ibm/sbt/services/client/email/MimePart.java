package com.ibm.sbt.services.client.email;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.commons.util.io.json.JsonObject;

/**
 * Represents a single MIME part of a MIME email.
 *
 */
public class MimePart {
    
    /**
     * MIME type property for the JSON representation.
     */
    public static final String MIME_TYPE = "mimeType";
    
    /**
     * Content property for the JSON representation.
     */
    public static final String CONTENT = "content";
    
    /**
     * Headers property for the JSON representation.
     */
    public static final String HEADERS = "headers";

    public String mimeType;
    public String content;
    public Map<String, String> headers;
    
    /**
     * Constructs a MIME part.
     * @param mimeType The MIME type as specified by <a href="http://www.iana.org/assignments/media-types/index.html">IANA</a>.
     * @param content The content of the MIME part.
     * @param headers The MIME part headers.
     * @throws MimeEmailException Thrown when the MIME part cannot be created.
     */
    public MimePart(String mimeType, String content, Map<String, String> headers) 
            throws MimeEmailException {
        setMimeType(mimeType);
        setContent(content);
        setHeaders(headers);
    }
    
    /**
     * Constructs a MIME part from a JSON object.  The JSON should follow the format of the 
     * below example.
     * <code>
     * {
     *   "mimeType" : "text/plain",
     *   "content" : "This is plain text"
     *   "headers" : {
     *     "header1" : "value1",
     *     "header2" : "value2"
     *   }
     * }
     * </code>
     * The MIME type SHOULD be registered with <a href="http://www.iana.org/assignments/media-types/index.html">IANA</a>.
     * @param json The JSON to create the MIME part from.
     * @throws MimeEmailException Thrown when the MIME part cannot be created.
     */
    public MimePart(JsonObject json) throws MimeEmailException {
        Object mimeTypeObj = json.getJsonProperty(MIME_TYPE);
        if(mimeTypeObj instanceof String) {
            setMimeType((String)mimeTypeObj);
        } else {
            throw new MimeEmailException("The MIME type must be a String.");
        }
        
        Object contentObj = json.getJsonProperty(CONTENT);
        if(contentObj instanceof String) {
            setContent((String)contentObj);
        } else if(contentObj instanceof JsonObject) {
            setContent(((JsonObject)contentObj).toString());
        } else {
            setContent("");
        }
        
        Object headersObj = json.getJsonProperty(HEADERS);
        if(headersObj instanceof JsonObject) {
            setHeaders(createHeaders((JsonObject)headersObj));
        } if(headersObj instanceof Map) {
            setHeaders((Map<String, String>)headersObj);
        } else {
            setHeaders(new HashMap<String, String>());
        }
    }
    
    /**
     * Creates a set of headers to be used for the MIME part from a List of
     * JsonObjects.
     * @param json JsonObject to create the headers from.
     * @return A set of headers to be used for the MIME part.
     */
    private Map<String, String> createHeaders(JsonObject json) {
        Map<String, String> headers = new HashMap<String, String>();
        Iterator<String> properties = json.getJsonProperties();
        while(properties.hasNext()) {
            String key = properties.next();
            Object valueObj = json.getJsonProperty(key);
            headers.put(key, valueObj.toString());

        }
        return headers;
    }

    /**
     * Gets the MIME type to use for the MIME part.
     * @return the MIME type.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the MIME type to use for the MIME part.
     * @param mimeType The MIME type to set.
     * @throws MimeEmailException Thrown when the MIME type is <code>null<code> or the empty String.
     */
    public void setMimeType(String mimeType) throws MimeEmailException {
        if(mimeType == null || mimeType.length() == 0) {
            throw new MimeEmailException("The MIME type must be a non-empty value.");
        }
        this.mimeType = mimeType;
    }

    /**
     * Gets the content of the MIME part.
     * @return The content of the MIME part.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the MIME part.
     * @param content The content to set for the MIME part.
     */
    public void setContent(String content) {
        this.content = (content == null) ? "" : content;
    }

    /**
     * Gets the headers for the MIME part.
     * @return The headers for the MIME part.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the headers of the MIME part.
     * @param headers The headers to set for the MIME part.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = (headers == null) ? new HashMap<String, String>() : headers;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        boolean result = true;
        if(o instanceof MimePart) {
            MimePart test = (MimePart)o;
            result &= (test.getContent() == null) ? test.getContent() == this.getContent() : 
                test.getContent().equals(this.getContent());
            result &= test.getHeaders().equals(this.getHeaders());
            result &= test.getMimeType().equals(this.getMimeType());
        } else {
            result = false;
        }
        return result;
    }
}
