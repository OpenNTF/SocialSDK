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
package com.ibm.sbt.services.client.connections.activity;

import com.ibm.commons.util.StringUtil;

/**
 * Class for construction of service URL for Activity Service <br>
 * @author Vimal Dhupar
 */

public class ActivityServiceUrlBuilder {

	public static String BASEURL = "/activities";
	public static String AUTHTYPE = "/service";
    public static String SERVICEURL = BASEURL + AUTHTYPE + "/atom2/{activityAction}";
    
    private ActivityServiceUrlBuilder(String baseUrl) {
    }

    public static String populateURL(String activityAction) {
        String ret = SERVICEURL;
        if (StringUtil.isEmpty(activityAction)) {
        	// if no action is specified then, by default we are fetching all activities.
            activityAction = ActivityAction.ACTIVITIES.getActivityAction();
        } 
        
        ret = ret.replace("{activityAction}", activityAction);
        return ret;
    }
}
