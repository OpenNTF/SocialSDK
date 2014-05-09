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
package com.ibm.sbt.services.client.connections.files.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lorenzo Boccaccia 
 * @date Jun 14, 2013
 */
public class FileCommentParameterBuilder {

    
    /**
     * Used to validate the local cache of the feed and entry documents retrieved previously. If the feed or entry has not been modified since the specified date, HTTP response code 304 (Not Modified) is returned.
     */
    Boolean ifModifiedSince; 
    /**
     * Contains an ETag response header sent by the server in a previous request to the same URL. If the ETag is still valid for the specified resource, HTTP response code 304 (Not Modified) is returned.
     */
    String ifNoneMatch;

    
    /**
     * Specifies whether or not the permissions for each user should be displayed for each entry in the returned Atom document. This parameter takes a Boolean value of either true or false. By default, the permission information is not returned.
     */
    Boolean acls;    
    
    public enum IdentifierType {
        UUID,LABEL;
    }

    /**
     * Indicates how the document is identified in the {document-id} variable segment of the web address. 
     * By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a File Atom entry. 
     * Specify "label" if the URL instead contains the value from the <td:label> element of a File Atom entry.
     */
    IdentifierType identifier;
    
    
    public Map<String, String> buildParameters() {
        Map<String, String> ret = new HashMap<String, String>();
        if (acls!=null) {
            ret.put("acls", acls.toString());
        }
        if (identifier!=null) {
            ret.put("identifier", identifier.toString().toLowerCase());
        }        
        return ret;
        
    }
    
    
    public Map<String, String> buildHeaders() {
        Map<String, String> ret = new HashMap<String, String>();
        if (ifModifiedSince!=null) {
            ret.put("If-Modified-Since", ifModifiedSince.toString());
        }        
        if (ifNoneMatch!=null) {
            ret.put("If-None-Match", ifNoneMatch);
        }        

        
        
        return ret;
        
    }
    
    
}
