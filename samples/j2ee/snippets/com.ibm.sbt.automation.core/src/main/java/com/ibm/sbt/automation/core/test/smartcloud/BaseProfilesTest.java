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
package com.ibm.sbt.automation.core.test.smartcloud;

import junit.framework.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.services.client.smartcloud.profiles.Profile;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileService;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileServiceException;

/**
 * @author mwallace
 * @author Vimal Dhupar
 *  
 * @date 19 Mar 2013
 */
public class BaseProfilesTest extends BaseApiTest {
    
    protected ProfileService profileService;

    public BaseProfilesTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    public Profile getProfile(String userId) {
        createContext();
        
        ProfileService profileService = getProfileService();
        Profile profile = null;
        try {
            profile = profileService.getProfile(userId);
        } catch (ProfileServiceException pse) {
            Assert.fail("Error get profile: " + pse.getMessage());
            pse.printStackTrace();
        } 
        return profile;
    }
    
    protected ProfileService getProfileService() {
        if (profileService == null) {
            profileService = new ProfileService("smartcloud");
        }
        return profileService;
    }

    protected void validate(Profile profile, JsonJavaObject json) {
        Assert.assertEquals(profile.getId(), json.getString("getUserid"));
        Assert.assertEquals(profile.getDisplayName(), json.getString("getName"));
        Assert.assertEquals(profile.getEmail(), json.getString("getEmail"));
        Assert.assertEquals(profile.getThumbnailUrl(), json.getString("getThumbnailUrl"));
        Assert.assertEquals(profile.getJobTitle(), json.getString("getJobTitle"));
        Assert.assertEquals(profile.getDepartment(), json.getString("getDepartment"));
        Assert.assertEquals(profile.getTelephoneNumber(), json.getString("getTelephoneNumber"));
    }
}
