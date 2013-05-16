/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.ConnectionsService;

/**
 * @author mwallace 
 * @date 14 May 2013
 */
class ConnectionsEndpointAdapter {
    
    private Endpoint endpoint;

    private static final String X_REQUESTED_WITH    = "x-requested-with"; //$NON-NLS-1$
    private static final String X_UPDATE_NONCE      = "x-update-nonce"; //$NON-NLS-1$

    static final String sourceClass = ConnectionsEndpointAdapter.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);

    /*
     * Constructor
     */
    ConnectionsEndpointAdapter(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
    
    /*
     * The method blocks the header x-requested-with only for calls to IBM Connections Activities Service.
     * The Activities Service does not return the correct feed when this header is present in the request.
     */
    boolean isHeaderAllowed(String headerName, String serviceUrl){
        if (headerName.equalsIgnoreCase(X_REQUESTED_WITH))
        {
            if(serviceUrl.indexOf("activities/service") != -1){
                return false;
            }
        }
        return true;
    }
    
    /*
     * This method will update the 'X-Update-Nonce' header if needed i.e. if
     * the header value is set to {X-Update-Nonce} then either a nonce will be
     * retrieved and cached in the user session for later use or the already
     * cached value will be reused.
     */
    void updateHeaders(DefaultHttpClient client, HttpRequestBase method) {
        try {
            List<Header> headers = findTokenHeaders(method);
            if (headers.isEmpty()) {
                return;
            }
            for (Header header : headers) {
                if (X_UPDATE_NONCE.equalsIgnoreCase(header.getName())) {
                    updateNonceHeader(method);
                }
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("Error updating headers for connections endpoint: {0}", endpoint.getUrl());
            logger.log(Level.SEVERE, msg, e);
        }
    }

    /*
     * Create an instance of ConnectionsService.
     */
    ClientService getClientService() throws ClientServicesException {
        return new ConnectionsService(endpoint);
    }
    
    /*
     * Called when an authentication error is returned for the associated endpoint,
     * this allow the cached 'X-Update-Nonce' to be cleared as it it no longer valid
     * after an authentication failure.
     */
    void handleAuthenticationError() {
        clearNonceHeader();
    }
    
    //
    // Internal Stuff
    //
    
    /*
     * Update the nonce header with the real value. 
     */
    private void updateNonceHeader(HttpRequestBase method) throws ClientServicesException, IOException {
        if (!endpoint.isAuthenticated()) {
            clearNonceHeader();
            return;
        }
        
        String nonce = retrieveNonceHeader();
        updateNonceHeader(method, nonce);
    }

    /*
     * Return the named header
     */
    private Header findHeader(HttpRequestBase method, String name) {
        Header[] headers = method.getAllHeaders();
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                if (name.equalsIgnoreCase(header.getName())) {
                    return header;
                }
            }
        }
        return null;
    }
    
    /*
     * Return the headers that need replacement
     */
    private List<Header> findTokenHeaders(HttpRequestBase method) {
        Header[] headers = method.getAllHeaders();
        List<Header> tokenHeaders = new ArrayList<Header>();
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                if (isTokenHeader(header)) {
                    tokenHeaders.add(header);
                }
            }
        }
        return tokenHeaders;
    }
    
    /*
     * Return true if this header needs replacement
     */
    private boolean isTokenHeader(Header header) {
        String value = header.getValue();
        return StringUtil.startsWithIgnoreCase(value, "{") && StringUtil.endsWithIgnoreCase(value, "}");
    }

    /*
     * Remove any cached 'X-Update-Nonce' header.
     */
    private void clearNonceHeader() {
        Context context = Context.getUnchecked();
        if (context != null) {
            context.getSessionMap().remove(X_UPDATE_NONCE);
        }
    }

    /*
     * Update existing nonce header or create a new one with specifed value.
     */
    private void updateNonceHeader(HttpRequestBase method, String nonce) {
        Header header = findHeader(method, X_UPDATE_NONCE);
        if (header != null) {
            method.removeHeader(header);
        } 
        method.addHeader(X_UPDATE_NONCE, nonce);
    }

    /*
     * Retrieve the current nonce value i.e. if the value exists in the cache then
     * user that value otherwise emit a request to retrieve it.
     */
    private String retrieveNonceHeader() throws ClientServicesException, IOException {
        Object nonce = null;
        Context context = Context.getUnchecked();
        if (context != null) {
            nonce = context.getSessionMap().get(X_UPDATE_NONCE);
        }
        if (nonce != null) {
            // return the cached value
            return nonce.toString();
        } else {
            InputStream response = (InputStream)endpoint.xhrGet("files/basic/api/nonce");
            nonce = StreamUtil.readString(response);
            
            // cache the value
            context.getSessionMap().put(X_UPDATE_NONCE, nonce);
            
            return nonce.toString();
        }
    }
}
