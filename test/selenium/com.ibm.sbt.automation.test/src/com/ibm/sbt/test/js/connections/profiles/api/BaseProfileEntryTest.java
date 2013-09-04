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
package com.ibm.sbt.test.js.connections.profiles.api;

import org.junit.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class BaseProfileEntryTest extends BaseApiTest {
    
    public void validate(JavaScriptPreviewPage previewPage) {
        JsonJavaObject json = previewPage.getJson();
        validate(json);
    }
        
    public void validate(JsonJavaObject json) {
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("getEntityId"));
        Assert.assertEquals("tag:profiles.ibm.com,2006:entrye0b62b52-6a67-4489-b03b-4eb1f62c73e7", json.getString("id"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("userid"));
        Assert.assertEquals("Frank Adams", json.getString("name"));
        Assert.assertEquals("FrankAdams@renovations.com", json.getString("email"));
        Assert.assertEquals("Frank Adams", json.getString("title"));
        Assert.assertEquals("fadams@gmail.com", json.getString("altEmail"));
        Assert.assertEquals("https://qs.renovations.com:444/profiles/photo.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427", json.getString("photoUrl"));
        Assert.assertEquals("Chief Operating Officer", json.getString("jobTitle"));
        Assert.assertEquals("<empty>", json.getString("organizationUnit"));
        Assert.assertEquals("https://qs.renovations.com:444/profiles/atom/profile.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7", json.getString("fnUrl"));
        Assert.assertEquals("55555555", json.getString("telephoneNumber"));
        Assert.assertEquals("Building1", json.getString("building"));
        Assert.assertEquals("Floor1", json.getString("floor"));
        Assert.assertEquals("<empty>", json.getString("streetAddress"));
        Assert.assertEquals("<empty>", json.getString("extendedAddress"));
        Assert.assertEquals("<empty>", json.getString("locality"));
        Assert.assertEquals("<empty>", json.getString("postalCode"));
        Assert.assertEquals("<empty>", json.getString("region"));
        Assert.assertEquals("<empty>", json.getString("countryName"));
        Assert.assertEquals("https://qs.renovations.com:444/profiles/audio.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427", json.getString("soundUrl"));
        Assert.assertEquals("Profile information for Frank Adams", json.getString("summary"));
        Assert.assertEquals("fadams@gmail.com", json.getString("groupwareMail"));
    }

}
