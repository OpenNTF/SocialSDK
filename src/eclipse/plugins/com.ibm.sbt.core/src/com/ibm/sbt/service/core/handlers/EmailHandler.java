package com.ibm.sbt.service.core.handlers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.email.DefaultMimeEmailFactory;
import com.ibm.sbt.services.client.email.MimeEmail;
import com.ibm.sbt.services.client.email.MimeEmailException;
import com.ibm.sbt.services.client.email.MimeEmailFactory;

/**
 * Handles requests to the proxy to send an email.
 *
 */
public class EmailHandler extends AbstractServiceHandler {
    
    /**
     * Math to the proxy handler.
     */
    public static final String URL_PATH = "mailer";
    
    private static final String EXTENSION_ID = "com.ibm.sbt.core.mimeemailfactory";
    private static final String APPLICATION_JSON = "application/json";
    private static MimeEmailFactory emailFactory;

    /* (non-Javadoc)
     * @see com.ibm.sbt.proxy.core.handlers.AbstractProxyHandler#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(!request.getContentType().startsWith(APPLICATION_JSON)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Content-Type must be application/json.");
            return;
        }
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"));
        try {
            Object obj = JsonParser.fromJson(JsonJavaFactory.instanceEx, getPostBody(request));
            List<JsonObject> emails = new ArrayList<JsonObject>();
            if(obj instanceof List){
                emails = (List<JsonObject>)obj;
            } else if (obj instanceof JsonObject) {
                JsonObject postBody = (JsonObject)obj;
                emails.add(postBody);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                        "Mailer API expects either an array of JSON objects or a single JSON object");
                return;
            }
            JsonObject jsonResponse = send(emails);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON);
            writer.write(jsonResponse.toString());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        } finally {
            writer.flush();
        }
    }
    
    /**
     * Sends a emails.
     * @param emails The emails to send.
     * @return A JSON response of what emails were successfully sent and which ones had errors.
     */
    private JsonObject send(List<JsonObject> emails) {
        MimeEmailFactory emailFactory = getEmailFactory();
        List<JsonObject> successful = new ArrayList<JsonObject>();
        List<JsonObject> error = new ArrayList<JsonObject>();
        for(JsonObject json : emails) {
            MimeEmail email;
            try {
                email = emailFactory.createMimeEmail(json);
                if(email != null) {
                    email.send();
                    successful.add(json);
                } else {
                    error.add(createErrorResponse(json, "Email could not be created from JSON."));
                }
            } catch (MimeEmailException e) {
                error.add(createErrorResponse(json, e.getMessage()));
            }
        }
        JsonObject response = new JsonJavaObject();
        response.putJsonProperty("successful", successful);
        response.putJsonProperty("error", error);
        return response;
    }
    
    /**
     * Creates an error JSON object.
     * @param email The JSON representing the email.
     * @param errorMessage The error message describing what went wrong.
     * @return An error JSON object.
     */
    private JsonObject createErrorResponse(JsonObject email, String errorMessage) {
        JsonObject error = new JsonJavaObject();
        error.putJsonProperty("message", errorMessage);
        error.putJsonProperty("email", email);
        return error;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.proxy.core.handlers.AbstractProxyHandler#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.proxy.core.handlers.AbstractProxyHandler#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.proxy.core.handlers.AbstractProxyHandler#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doDelete(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * Gets the POST body.
     * @param request The POST request.
     * @return The POST body.
     * @throws IOException Thrown if the body cannot be retrieved.
     */
    private String getPostBody(HttpServletRequest request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        buf.close();
        String body = buf.toString();
        return body;
    }
    
    /**
     * Gets an instance of MimeEmailFactory to use to send emails.
     * @return An instance of MimeEmailFactory to use to send emails.
     */
    private MimeEmailFactory getEmailFactory() {
        if(emailFactory == null) {
            Application app = Application.getUnchecked();
            if (app != null) {
                List<Object> factories = app.findServices(EXTENSION_ID);
                for(Object o : factories) {
                    if(o instanceof MimeEmailFactory) {
                        emailFactory = (MimeEmailFactory)o;
                    }
                }
            }
            emailFactory = DefaultMimeEmailFactory.getInstance();
        }
        return emailFactory;
    }
}
