package com.ibm.sbt.services.client.connections.files.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lorenzo Boccaccia 
 * @date Jun 14, 2013
 */
public class FileCommentsFeedParameterBuilder {

    /**
     * Specifies whether or not the permissions for each user should be displayed for each entry in the returned feed. This parameter takes a Boolean value of either true or false. By default, the permission information is not returned.    
     */
    Boolean acls;


    /**
     * Specifies the relative returned comments. If provided, page and sI are ignored.
     */
    String commentId;  
    
    

    public enum IdentifierType {
        UUID,LABEL;
    }
    
    /**
     * Indicates how the document is identified in the {document-id} variable segment of the web address. <br>
     * By default, look up is performed with the expectation that the URL contains the value from the <td:uuid> element of a file Atom entry. <br>
     * Specify label if the URL instead contains the value from the <td:label> element of a file Atom entry.
     */
    IdentifierType identifier;
    
    /**
     * Page number. Specifies the page to be returned. The default value is 1, which returns the first page.
     */
    Integer page;    
    
    /**
     * Page size. Specifies the number of entries to return per page. The default value is 10. The maximum value you can specify is 500.
     */
    Integer ps;  
    
    /**Start index. Specifies the start index (as a number) in the collection from which the results should be returned. */
    Integer sI;
    
    public enum SortByType {
        CREATED, MODIFIED,PUBLISHED,UPDATED;
    }
    /**
     * Specifies what to sort the returned entries by.<br/>
     * created - Sorts the entries by the date the item was created.<br/>
     * modified - Sorts the entries by the last modified date.<br/>
     * published - Sorts the entries by the date the item was published (usually related to atom:published element).<br/>
     * updated - Sorts the entries by the last time the item was updated.<br/>
     * The default value of this parameter is modified.
     */
    SortByType sortBy;
    
    public enum SortOrderType {
        ASC,DESC
    }
    /**
     * Specifies the order in which to sort the results. The options are:
     * asc - Sorts the results in ascending order.<br/>
     * desc - Sorts the results in descending order.<br/>
     * If a value is specified for the sortBy parameter, but none is specified for this parameter, <br/>
     * then this parameter defaults to asc. If neither is specified, this parameter defaults to desc.
     */
    SortOrderType sortOrder;
    
    
    public Map<String, String> buildParameters() {
        Map<String, String> ret = new HashMap<String, String>();
        if (acls!=null) {
            ret.put("acls", acls.toString());
        }
        if (commentId!=null) {
            ret.put("commentId", commentId);
        }
        if (identifier!=null) {
            ret.put("identifier", identifier.toString().toLowerCase());
        }        
        if (page!=null) {
            ret.put("page", page.toString());
        }        
        if (ps!=null) {
            ret.put("ps", ps.toString());
        }        
        if (sI!=null) {
            ret.put("sI", sI.toString());
        }        
        if (sortBy!=null) {
            ret.put("sortBy", sortBy.toString().toLowerCase());
        }        
        if (sortOrder!=null) {
            ret.put("sortOrder", sortOrder.toString().toLowerCase());
        }        

        
        
        return ret;
        
    }
    
    
    
}
