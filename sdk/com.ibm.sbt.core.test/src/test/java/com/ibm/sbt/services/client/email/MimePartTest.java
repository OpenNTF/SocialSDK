package com.ibm.sbt.services.client.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;

public class MimePartTest {
    
    private Map<String, String> headers = new HashMap<String, String>();
    private MimePart partWithHeaders;
    private MimePart partWithOutHeaders;
    private MimePart partWithOutContent;
    private MimePart jsonPartWithHeaders;
    private MimePart jsonPartWithOutHeaders;
    private MimePart jsonPartWithOutContent;
    private MimePart jsonPartWithInvalidTypes;
    private MimePart jsonPartWithJsonContent;
    private JsonObject jsonWithHeaders;
    private JsonObject jsonWithOutHeaders;
    private JsonObject jsonWithOutContent;
    private JsonObject jsonWithInvalidTypes;
    private JsonObject jsonWithJsonContent;
    private JsonObject jsonContent;

    @Before
    public void setUp() throws Exception {
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        
        jsonContent = new JsonJavaObject();
        jsonContent.putJsonProperty("hello", "wold");
        
        jsonWithHeaders = new JsonJavaObject();
        jsonWithHeaders.putJsonProperty(MimePart.MIME_TYPE, "some/type");
        jsonWithHeaders.putJsonProperty(MimePart.CONTENT, "more content");
        jsonWithHeaders.putJsonProperty(MimePart.HEADERS, headers);
        jsonPartWithHeaders = new MimePart(jsonWithHeaders);
        
        jsonWithOutHeaders = new JsonJavaObject();
        jsonWithOutHeaders.putJsonProperty(MimePart.MIME_TYPE, "some/type");
        jsonWithOutHeaders.putJsonProperty(MimePart.CONTENT, "more content");
        jsonPartWithOutHeaders = new MimePart(jsonWithOutHeaders);
        
        jsonWithOutContent = new JsonJavaObject();
        jsonWithOutContent.putJsonProperty(MimePart.MIME_TYPE, "some/type");
        jsonPartWithOutContent = new MimePart(jsonWithOutContent);
        
        jsonWithInvalidTypes = new JsonJavaObject();
        jsonWithInvalidTypes.putJsonProperty(MimePart.MIME_TYPE, "some/type");
        jsonWithInvalidTypes.putJsonProperty(MimePart.CONTENT, 123);
        jsonWithInvalidTypes.putJsonProperty(MimePart.HEADERS, 123);
        jsonPartWithInvalidTypes = new MimePart(jsonWithInvalidTypes);
        
        jsonWithJsonContent = new JsonJavaObject();
        jsonWithJsonContent.putJsonProperty(MimePart.MIME_TYPE, "some/type");
        jsonWithJsonContent.putJsonProperty(MimePart.CONTENT, jsonContent);
        jsonPartWithJsonContent = new MimePart(jsonWithJsonContent);
        
        partWithHeaders = new MimePart("some/type", "more content", headers);
        partWithOutHeaders = new MimePart("some/type", "more content", null);
        partWithOutContent = new MimePart("some/type", null, null);
        
        
    }

    @After
    public void tearDown() throws Exception {
        partWithHeaders = null;
        partWithOutHeaders = null;
        partWithOutContent = null;
        jsonPartWithHeaders = null;
        jsonPartWithOutHeaders = null;
        jsonPartWithOutContent = null;
        jsonPartWithInvalidTypes = null;
        jsonPartWithJsonContent = null;
        jsonWithHeaders = null;
        jsonWithOutHeaders = null;
        jsonWithOutContent = null;
        jsonWithInvalidTypes = null;
        jsonWithJsonContent = null;
        jsonContent = null;
    }

    @Test (expected = MimeEmailException.class)
    public void testMimePartNullMimeType() throws Exception {
       new MimePart(null, "more content", headers);
    }
    
    @Test (expected = MimeEmailException.class)
    public void testMimePartEmptyMimeType() throws Exception {
       new MimePart("", "more content", headers);
    }

    @Test (expected = MimeEmailException.class)
    public void testMimePartJsonObject() throws Exception {
        new MimePart(new JsonJavaObject());
    }
    
    @Test (expected = MimeEmailException.class)
    public void testMimePartJsonNonString() throws Exception {
        JsonObject json = new JsonJavaObject();
        json.putJsonProperty(MimePart.MIME_TYPE, 123);
        new MimePart(json);
    }

    @Test
    public void testGetMimeType() {
        assertEquals("some/type", partWithHeaders.getMimeType());
        assertEquals("some/type", partWithOutHeaders.getMimeType());
        assertEquals("some/type", partWithOutContent.getMimeType());
        assertEquals("some/type", jsonPartWithHeaders.getMimeType());
        assertEquals("some/type", jsonPartWithOutHeaders.getMimeType());
        assertEquals("some/type", jsonPartWithOutContent.getMimeType());
        assertEquals("some/type", jsonPartWithInvalidTypes.getMimeType());
        assertEquals("some/type", jsonPartWithJsonContent.getMimeType());
    }

    @Test (expected = MimeEmailException.class)
    public void testSetMimeTypeNull() throws Exception {
        partWithHeaders.setMimeType(null);
    }
    
    @Test (expected = MimeEmailException.class)
    public void testSetMimeTypeEmpty() throws Exception {
        partWithHeaders.setMimeType("");
    }
    
    @Test
    public void testSetMimeType() throws Exception {
        partWithHeaders.setMimeType("another/type");
        assertEquals("another/type", partWithHeaders.getMimeType());
    }

    @Test
    public void testGetContent() {
        assertEquals("more content", partWithHeaders.getContent());
        assertEquals("more content", partWithOutHeaders.getContent());
        assertEquals("", partWithOutContent.getContent());
        assertEquals("more content", jsonPartWithHeaders.getContent());
        assertEquals("more content", jsonPartWithOutHeaders.getContent());
        assertEquals("", jsonPartWithOutContent.getContent());
        assertEquals("", jsonPartWithInvalidTypes.getContent());
        assertEquals(jsonContent.toString(), jsonPartWithJsonContent.getContent());
    }

    @Test
    public void testSetContent() {
        partWithOutContent.setContent("here is some content");
        assertEquals("here is some content", partWithOutContent.getContent());
        
        partWithHeaders.setContent("{\"hello\":\"world\"}");
        assertEquals("{\"hello\":\"world\"}", partWithHeaders.getContent());
    }

    @Test
    public void testGetHeaders() {
        assertEquals(headers, partWithHeaders.getHeaders());
        assertEquals(new HashMap<String, String>(), partWithOutHeaders.getHeaders());
        assertEquals(new HashMap<String, String>(), partWithOutContent.getHeaders());
        assertEquals(headers, jsonPartWithHeaders.getHeaders());
        assertEquals(new HashMap<String, String>(), jsonPartWithOutHeaders.getHeaders());
        assertEquals(new HashMap<String, String>(), jsonPartWithOutContent.getHeaders());
        assertEquals(new HashMap<String, String>(), jsonPartWithInvalidTypes.getHeaders());
        assertEquals(new HashMap<String, String>(), jsonPartWithJsonContent.getHeaders());
    }

    @Test
    public void testSetHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("myheader", "myValue");
        headers.put("myheader2", "myValue2");
        partWithOutContent.setHeaders(headers);
        assertEquals(headers, partWithOutContent.getHeaders());
    }

    @Test
    public void testEqualsObject() {
        assertTrue(partWithHeaders.equals(jsonPartWithHeaders));
        assertTrue(partWithOutHeaders.equals(jsonPartWithOutHeaders));
        assertTrue(partWithOutContent.equals(jsonPartWithOutContent));
        assertFalse(partWithHeaders.equals(jsonPartWithOutHeaders));
        assertFalse(partWithHeaders.equals(null));
    }
}
