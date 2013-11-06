package com.ibm.sbt.services.client.connections.files.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Lorenzo Boccaccia 
 * @date May 28, 2013
 */
public class FileCreationParameters {
    
    /**
     * Specifies whether you want to get a notification when someone adds or updates a comment on a file. Options are on or off. The default value is on. 
     */

    public NotificationFlag commentNotification;
    
    
        
    /**
     * Date to use as the creation date of the file. This value can be set by the user, and defaults to the current system time on the server.
     * Sent as the time in the number of milliseconds since January 1, 1970, 00:00:00 GMT time.
     */
    public Date created;
    
    /**
     * Specifies whether you want to show the file path to the file. if true, 
     * adds an entry extension <td:path> element that specifies the file path to the object.
     */
    public Boolean includePath;
        
    /**
     * Specifies whether the person updating the file wants to get a notification when someone subsequently updates the file. 
     * Options are on or off. The default value is off.
     */
    public NotificationFlag mediaNotification;
    public enum NotificationFlag {
        ON, OFF
    }
    /**
     * Date to use as the last modified date of the file. This value can be set by the user, and defaults to the current system time on the server.
     * Sent as the time in the number of milliseconds since January 1, 1970, 00:00:00 GMT time.
     */
    public Date modified;

    /**
     *  Indicates if users that are shared with can share this document. The default value is false.
     */
    public Boolean propagate;
        
    /**
     * Defines the level of permission that the people listed in the sharedWith parameter have to the file. Only applicable if the sharedWith parameter is passed. 
     * Permission level options are Edit or View. 
     * The default value is View.
     */
    public Permission sharePermission;
    public enum Permission {
        EDIT, VIEW
    }
    /**
     *  Text. Explanation of the share.
     */
    public String shareSummary;
        
     /**
      * User ID of the user to share the content with. This parameter can be applied multiple times.
      */
    public Collection<String> shareWith = new LinkedList<String>();
    
    /**
     * String. Keyword that helps to classify the file. This parameter can be applied multiple times if multiple tags are passed. 
     */
    public Collection<String> tags = new LinkedList<String>();
        
    /**
     * Specifies who can see the file. Options are private or public. A public file is visible to all users and can be shared by all users. 
     * The default value is private.
     */
    public Visibility visibility;
    public enum Visibility {
        PUBLIC, PRIVATE
    }
    
    
    public Map<String, String> buildParameters() {
        Map<String, String> ret = new HashMap<String, String>();
        if (commentNotification!=null) {
            ret.put("commentNotification", commentNotification.toString().toLowerCase());
        }
        if (mediaNotification!=null) {
            ret.put("mediaNotification", mediaNotification.toString().toLowerCase());
        }
        if (created!=null) {
            ret.put("created", Long.toString(created.getTime()));
        }        
        if (includePath!=null) {
            ret.put("includePath", includePath.toString());
        }       
        if (modified!=null) {
            ret.put("modified", Long.toString(modified.getTime()));
        }      
        
        if (propagate!=null) {
            ret.put("propagate", propagate.toString());
        }  
        if (sharePermission!=null) {
            ret.put("sharePermission", sharePermission.toString().toLowerCase());
        }
        if (shareSummary!=null) {
            ret.put("shareSummary", shareSummary.toString());
        }
        if (shareWith!=null && shareWith.size()>0) {
            if ( shareWith.size()>1) 
                throw new UnsupportedOperationException("multivalue shareWith args not yet supported");
            ret.put("shareWith", shareWith.iterator().next());
        }
        if (tags!=null && tags.size()>0) {
            if ( tags.size()>1) 
            throw new UnsupportedOperationException("multivalue tags args not yet supported");
            ret.put("tag", tags.iterator().next());
        }
        if (visibility!=null) {
            ret.put("visibility", visibility.toString().toLowerCase());
        }        
        return ret;
        
    }
    
}
