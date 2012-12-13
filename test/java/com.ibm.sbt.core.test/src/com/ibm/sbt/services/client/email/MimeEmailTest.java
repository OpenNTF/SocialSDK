package com.ibm.sbt.services.client.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;

public class MimeEmailTest {
    List<String> to = new ArrayList<String>();
    List<String> cc = new ArrayList<String>();
    List<String> bcc = new ArrayList<String>();
    private JsonObject jsonTextPart;
    private JsonObject jsonHtmlPart;
    private JsonObject jsonEEPart;
    private JsonObject jsonEEDataModel;
    private JsonObject jsonHtmlEmail;
    private JsonObject jsonTextEmail;
    private JsonObject jsonEEEmail;
    private JsonObject jsonNoMimeParts;
    private MimeEmail eeEmail;
    private MimeEmail textEmail;
    private MimeEmail htmlEmail;
    private MimeEmail noMimePartsEmail;
    private MimeEmail email;
    private List<MimePart> emailParts = new ArrayList<MimePart>();
    

    @Before
    public void setUp() throws Exception {
        to.add("sdaryn@renovations.com");
        to.add("tamado@renovations.com");
        
        cc.add("pclemmons@renovations.com");
        
        bcc.add("example@renovations.com");
        
        jsonEEDataModel = new JsonJavaObject();
        jsonEEDataModel.putJsonProperty("gadget", "http://renovations.com/gadget.xml");
        jsonEEDataModel.putJsonProperty("context", "This is some context");
        
        jsonTextPart = new JsonJavaObject();
        jsonTextPart.putJsonProperty(MimePart.MIME_TYPE, "text/plain");
        jsonTextPart.putJsonProperty(MimePart.CONTENT, "This is some text");
        
        jsonHtmlPart = new JsonJavaObject();
        jsonHtmlPart.putJsonProperty(MimePart.MIME_TYPE, "text/html");
        jsonHtmlPart.putJsonProperty(MimePart.CONTENT, "<b>This is some html</b>");
        
        jsonEEPart = new JsonJavaObject();
        jsonEEPart.putJsonProperty(MimePart.MIME_TYPE, "application/embed+json");
        jsonEEPart.putJsonProperty(MimePart.CONTENT, jsonEEDataModel);
        
        List<JsonObject> textMimeParts = new ArrayList<JsonObject>();
        textMimeParts.add(jsonTextPart);
        jsonTextEmail = new JsonJavaObject();
        jsonTextEmail.putJsonProperty(MimeEmail.TO, to);
        jsonTextEmail.putJsonProperty(MimeEmail.CC, cc);
        jsonTextEmail.putJsonProperty(MimeEmail.BCC, bcc);
        jsonTextEmail.putJsonProperty(MimeEmail.SUBJECT, "Look At This");
        jsonTextEmail.putJsonProperty(MimeEmail.MIME_PARTS, textMimeParts);
        textEmail = new DefaultMimeEmail(jsonTextEmail);
        
        
        List<JsonObject> htmlMimeParts = new ArrayList<JsonObject>();
        htmlMimeParts.add(jsonHtmlPart);
        jsonHtmlEmail = new JsonJavaObject();
        jsonHtmlEmail.putJsonProperty(MimeEmail.TO, to);
        jsonHtmlEmail.putJsonProperty(MimeEmail.CC, cc);
        jsonHtmlEmail.putJsonProperty(MimeEmail.BCC, bcc);
        jsonHtmlEmail.putJsonProperty(MimeEmail.MIME_PARTS, htmlMimeParts);
        htmlEmail = new DefaultMimeEmail(jsonHtmlEmail);
        
        
        List<JsonObject> eeMimeParts = new ArrayList<JsonObject>();
        eeMimeParts.add(jsonTextPart);
        eeMimeParts.add(jsonHtmlPart);
        eeMimeParts.add(jsonEEPart);
        jsonEEEmail = new JsonJavaObject();
        jsonEEEmail.putJsonProperty(MimeEmail.TO, to);
        jsonEEEmail.putJsonProperty(MimeEmail.SUBJECT, "EE Email");
        jsonEEEmail.putJsonProperty(MimeEmail.MIME_PARTS, eeMimeParts);
        eeEmail = new DefaultMimeEmail(jsonEEEmail);
        
        jsonNoMimeParts = new JsonJavaObject();
        jsonNoMimeParts.putJsonProperty(MimeEmail.TO, to);
        jsonNoMimeParts.putJsonProperty(MimeEmail.CC, cc);
        jsonNoMimeParts.putJsonProperty(MimeEmail.BCC, bcc);
        noMimePartsEmail = new DefaultMimeEmail(jsonNoMimeParts);
        
        for(JsonObject json : eeMimeParts){
            emailParts.add(new MimePart(json));
        }
        email = new DefaultMimeEmail(to, cc, bcc, "Look At This", emailParts);
    }

    @After
    public void tearDown() throws Exception {
        to = new ArrayList<String>();
        cc = new ArrayList<String>();
        bcc = new ArrayList<String>();
        jsonTextPart = null;
        jsonHtmlPart = null;
        jsonEEPart = null;
        jsonEEDataModel = null;
        jsonHtmlEmail = null;
        jsonTextEmail = null;
        jsonEEEmail =  null;
        jsonNoMimeParts = null;
        eeEmail = null;
        textEmail = null;
        htmlEmail = null;
        noMimePartsEmail = null;
        email = null;
        emailParts = new ArrayList<MimePart>();
    }

    @Test(expected = MimeEmailException.class)
    public void testMimeEmailNull() throws Exception {
        new DefaultMimeEmail(null, null, null, "Subject", new ArrayList<MimePart>()).send();
    }
    
    @Test(expected = MimeEmailException.class)
    public void testMimeEmailEmpty() throws Exception {
        new DefaultMimeEmail(new ArrayList<String>(), new ArrayList<String>(), 
                new ArrayList<String>(), "Subject", new ArrayList<MimePart>()).send();
    }
    
    @Test(expected = MimeEmailException.class)
    public void testMimeEmailJson() throws Exception {
        List<JsonObject> eeMimeParts = new ArrayList<JsonObject>();
        eeMimeParts.add(jsonTextPart);
        eeMimeParts.add(jsonHtmlPart);
        eeMimeParts.add(jsonEEPart);
        JsonObject json = new JsonJavaObject();
        json.putJsonProperty(MimeEmail.MIME_PARTS, eeMimeParts);
        new DefaultMimeEmail(json).send();
    }

    @Test
    public void testGetSubject() {
        assertEquals("EE Email", eeEmail.getSubject());
        assertEquals("Look At This", textEmail.getSubject());
        assertEquals("", htmlEmail.getSubject());
        assertEquals("", noMimePartsEmail.getSubject());
        assertEquals("Look At This", email.getSubject());
    }

    @Test
    public void testSetSubject() {
        eeEmail.setSubject("new subject");
        assertEquals("new subject", eeEmail.getSubject());
        
        eeEmail.setSubject(null);
        assertEquals("", eeEmail.getSubject());
    }

    @Test
    public void testGetTo() {
        assertEquals(to, eeEmail.getTo());
        assertEquals(to, textEmail.getTo());
        assertEquals(to, htmlEmail.getTo());
        assertEquals(to, noMimePartsEmail.getTo());
        assertEquals(to, email.getTo());
    }

    @Test
    public void testGetCommaSeparatedTo() {
        assertEquals("sdaryn@renovations.com,tamado@renovations.com", eeEmail.getCommaSeparatedTo());
        assertEquals("sdaryn@renovations.com,tamado@renovations.com", textEmail.getCommaSeparatedTo());
        assertEquals("sdaryn@renovations.com,tamado@renovations.com", htmlEmail.getCommaSeparatedTo());
        assertEquals("sdaryn@renovations.com,tamado@renovations.com", noMimePartsEmail.getCommaSeparatedTo());
        assertEquals("sdaryn@renovations.com,tamado@renovations.com", email.getCommaSeparatedTo());
    }

    @Test
    public void testSetTo() {
        email.setTo(bcc);
        assertEquals(bcc, email.getTo());
        
        email.setTo(null);
        assertEquals(new ArrayList<String>(), email.getTo());
    }
    
    @Test
    public void testAddToAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addToAddress(testEmail);
        assertEquals(test, email.getTo());
        
        email.addToAddress(null);
        assertEquals(test, email.getTo());
    }
    
    @Test
    public void testRemoveToAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addToAddress(testEmail);
        assertEquals(test, email.getTo());
        
        email.removeToAddress(null);
        assertEquals(test, email.getTo());
        
        email.removeToAddress(testEmail);
        assertEquals(new ArrayList<String>(), email.getTo());
    }

    @Test
    public void testGetCC() {
        assertEquals(new ArrayList<String>(), eeEmail.getCC());
        assertEquals(cc, textEmail.getCC());
        assertEquals(cc, htmlEmail.getCC());
        assertEquals(cc, noMimePartsEmail.getCC());
        assertEquals(cc, email.getCC());
    }

    @Test
    public void testGetCommaSeparatedCC() {
        assertEquals("", eeEmail.getCommaSeparatedCC());
        assertEquals("pclemmons@renovations.com", textEmail.getCommaSeparatedCC());
        assertEquals("pclemmons@renovations.com", htmlEmail.getCommaSeparatedCC());
        assertEquals("pclemmons@renovations.com", noMimePartsEmail.getCommaSeparatedCC());
        assertEquals("pclemmons@renovations.com", email.getCommaSeparatedCC());
    }

    @Test
    public void testSetCC() {
        email.setCC(to);
        assertEquals(to, email.getCC());
        
        email.setCC(null);
        assertEquals(new ArrayList<String>(), email.getCC());
    }
    
    @Test
    public void testAddCCAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addCCAddress(testEmail);
        assertEquals(test, email.getCC());
        
        email.addCCAddress(null);
        assertEquals(test, email.getCC());
    }
    
    @Test
    public void testRemoveCCAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addCCAddress(testEmail);
        assertEquals(test, email.getCC());
        
        email.removeCCAddress(null);
        assertEquals(test, email.getCC());
        
        email.removeCCAddress(testEmail);
        assertEquals(new ArrayList<String>(), email.getCC());
    }

    @Test
    public void testGetBCC() {
        assertEquals(new ArrayList<String>(), eeEmail.getBCC());
        assertEquals(bcc, textEmail.getBCC());
        assertEquals(bcc, htmlEmail.getBCC());
        assertEquals(bcc, noMimePartsEmail.getBCC());
        assertEquals(bcc, email.getBCC());
    }

    @Test
    public void testGetCommaSeparatedBCC() {
        assertEquals("", eeEmail.getCommaSeparatedBCC());
        assertEquals("example@renovations.com", textEmail.getCommaSeparatedBCC());
        assertEquals("example@renovations.com", htmlEmail.getCommaSeparatedBCC());
        assertEquals("example@renovations.com", noMimePartsEmail.getCommaSeparatedBCC());
        assertEquals("example@renovations.com", email.getCommaSeparatedBCC());
    }

    @Test
    public void testSetBCC() {
        email.setBCC(to);
        assertEquals(to, email.getBCC());
        
        email.setBCC(null);
        assertEquals(new ArrayList<String>(), email.getBCC());
    }
    
    @Test
    public void testAddBCCAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addBCCAddress(testEmail);
        assertEquals(test, email.getBCC());
        
        email.addBCCAddress(null);
        assertEquals(test, email.getBCC());
    }
    
    @Test
    public void testRemoveBCCAddress() {
        String testEmail = "example@renovations.com";
        List<String> test = new ArrayList<String>();
        test.add(testEmail);
        MimeEmail email = new DefaultMimeEmail();
        email.addBCCAddress(testEmail);
        assertEquals(test, email.getBCC());
        
        email.removeBCCAddress(null);
        assertEquals(test, email.getBCC());
        
        email.removeBCCAddress(testEmail);
        assertEquals(new ArrayList<String>(), email.getBCC());
    }

    @Test
    public void testGetMimeParts() throws Exception {
        List<MimePart> textParts = new ArrayList<MimePart>();
        textParts.add(new MimePart(jsonTextPart));
        
        List<MimePart> htmlParts = new ArrayList<MimePart>();
        htmlParts.add(new MimePart(jsonHtmlPart));
        
        assertEquals(emailParts, eeEmail.getMimeParts());
        assertEquals(textParts, textEmail.getMimeParts());
        assertEquals(htmlParts, htmlEmail.getMimeParts());
        assertEquals(new ArrayList<MimePart>(), noMimePartsEmail.getMimeParts());
        assertEquals(emailParts, email.getMimeParts());
    }

    @Test
    public void testSetMimeParts() throws Exception {
        email.setMimeParts(null);
        assertEquals(new ArrayList<String>(), email.getMimeParts());
        
        List<MimePart> parts = new ArrayList<MimePart>();
        parts.add(new MimePart(jsonTextPart));
        email.setMimeParts(parts);
    }
    
    @Test
    public void testAddMimePart() throws MimeEmailException {
        MimePart part = new MimePart("some/text", "some content", new HashMap<String, String>());
        List<MimePart> parts = new ArrayList<MimePart>();
        parts.add(part);
        MimeEmail email = new DefaultMimeEmail();
        email.addMimePart(part);
        assertEquals(parts, email.getMimeParts());
        
        email.addMimePart(null);
        assertEquals(parts, email.getMimeParts());
    }
    
    @Test
    public void testRemoveMimePart() throws MimeEmailException {
        MimePart part = new MimePart("some/text", "some content", new HashMap<String, String>());
        List<MimePart> parts = new ArrayList<MimePart>();
        parts.add(part);
        MimeEmail email = new DefaultMimeEmail();
        email.addMimePart(part);
        assertEquals(parts, email.getMimeParts());
        
        email.removeMimePart(null);
        assertEquals(parts, email.getMimeParts());
        
        email.removeMimePart(part);
        assertEquals(new ArrayList<MimePart>(), email.getMimeParts());
    }
    
    @Test
    public void testEqualsObject() throws Exception {
        MimeEmail test = new DefaultMimeEmail(to, cc, bcc, null, new ArrayList<MimePart>());
        assertFalse(test.equals(eeEmail));
        assertTrue(test.equals(noMimePartsEmail));
        assertFalse(test.equals(email));
        assertFalse(test.equals(null));
        assertFalse(test.equals(new Object()));
    }

}
