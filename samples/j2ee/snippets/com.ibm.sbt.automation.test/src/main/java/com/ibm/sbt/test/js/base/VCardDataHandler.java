/*
 * � Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at,
 * 
 * http,//www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.js.base;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class VCardDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_VCardDataHandler";
    
    public VCardDataHandler() {
        setAuthType(AuthType.NONE);
    }
    
    @Test
    public void testVCardDataHandler() {
        addSnippetParam("sample.comunityId", "foo");
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals("tag:profiles.ibm.com,2006:entrye0b62b52-6a67-4489-b03b-4eb1f62c73e7\n\t\t\n\t\tFrank Adams\n\t\t2013-04-05T17:15:36.427Z\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\tFrank Adams\n\t\t\t0EE5A7FA-3434-9A59-4825-7A7000278DAA\n\t\t\tFrankAdams@renovations.com\n\t\t\tactive\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\t\n\t\tProfile information for Frank Adams\n\t\t\n\t\t\tBEGIN:VCARD\n\t\t\tVERSION:2.1\n\t\t\tPHOTO;VALUE=URL:https://qs.renovations.com:444/profiles/photo.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427\n\t\t\tN:Adams;Frank\n\t\t\tFN:Frank Adams\n\t\t\tHONORIFIC_PREFIX:\n\t\t\tNICKNAME:\n\t\t\tX_PREFERRED_LAST_NAME:\n\t\t\tX_NATIVE_FIRST_NAME:\n\t\t\tX_NATIVE_LAST_NAME:\n\t\t\tX_ALTERNATE_LAST_NAME:\n\t\t\tURL:https://qs.renovations.com:444/profiles/atom/profile.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7\n\t\t\tSOUND;VALUE=URL:https://qs.renovations.com:444/profiles/audio.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427\n\t\t\tEMAIL;INTERNET:FrankAdams@renovations.com\n\t\t\tEMAIL;X_GROUPWARE_MAIL:fadams@gmail.com\n\t\t\tX_BLOG_URL;VALUE=URL:\n\t\t\tTZ:Etc/GMT+12\n\t\t\tX_PREFERRED_LANGUAGE:\n\t\t\tORG:\n\t\t\tX_ORGANIZATION_CODE:\n\t\t\tROLE:\n\t\t\tX_EMPTYPE:\n\t\t\tTITLE:Chief Operating Officer\n\t\t\tX_BUILDING:Building1\n\t\t\tX_FLOOR:Floor1\n\t\t\tX_OFFICE_NUMBER:Office1\n\t\t\tTEL;WORK:55555555\n\t\t\tTEL;CELL: +353 86 81551111\n\t\t\tTEL;FAX:\n\t\t\tTEL;X_IP:\n\t\t\tTEL;PAGER:\n\t\t\tX_PAGER_ID:\n\t\t\tX_PAGER_TYPE:\n\t\t\tX_PAGER_PROVIDER:\n\t\t\tCATEGORIES:acme-airlines-it-staff,it,it-staff,itstaff,my-hero\n\t\t\tX_EXPERIENCE:\n\t\t\tX_DESCRIPTION:User Experience Character - Directory2\n\t\t\tX_MANAGER_UID:lsuarez\n\t\t\tX_IS_MANAGER:N\n\t\t\tX_PROFILE_KEY:e0b62b52-6a67-4489-b03b-4eb1f62c73e7\n\t\t\tUID:0EE5A7FA-3434-9A59-4825-7A7000278DAA\n\t\t\tX_PROFILE_UID:FAdams\n\t\t\tX_LCONN_USERID:0EE5A7FA-3434-9A59-4825-7A7000278DAA\n\t\t\tX_EMPLOYEE_NUMBER:\n\t\t\tX_DEPARTMENT_NUMBER:\n\t\t\tX_DEPARTMENT_TITLE:\n\t\t\tX_SHIFT:\n\t\t\tREV:2013-04-05T17:15:36.427Z\n\t\t\tX_PROFILE_TYPE:default\n\t\t\tEND:VCARD", json.getString("/a:feed/a:entry"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("a:contributor/snx:userid"));
        Assert.assertEquals("BEGIN:VCARD\n\t\t\tVERSION:2.1\n\t\t\tPHOTO;VALUE=URL:https://qs.renovations.com:444/profiles/photo.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427\n\t\t\tN:Adams;Frank\n\t\t\tFN:Frank Adams\n\t\t\tHONORIFIC_PREFIX:\n\t\t\tNICKNAME:\n\t\t\tX_PREFERRED_LAST_NAME:\n\t\t\tX_NATIVE_FIRST_NAME:\n\t\t\tX_NATIVE_LAST_NAME:\n\t\t\tX_ALTERNATE_LAST_NAME:\n\t\t\tURL:https://qs.renovations.com:444/profiles/atom/profile.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7\n\t\t\tSOUND;VALUE=URL:https://qs.renovations.com:444/profiles/audio.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427\n\t\t\tEMAIL;INTERNET:FrankAdams@renovations.com\n\t\t\tEMAIL;X_GROUPWARE_MAIL:fadams@gmail.com\n\t\t\tX_BLOG_URL;VALUE=URL:\n\t\t\tTZ:Etc/GMT+12\n\t\t\tX_PREFERRED_LANGUAGE:\n\t\t\tORG:\n\t\t\tX_ORGANIZATION_CODE:\n\t\t\tROLE:\n\t\t\tX_EMPTYPE:\n\t\t\tTITLE:Chief Operating Officer\n\t\t\tX_BUILDING:Building1\n\t\t\tX_FLOOR:Floor1\n\t\t\tX_OFFICE_NUMBER:Office1\n\t\t\tTEL;WORK:55555555\n\t\t\tTEL;CELL: +353 86 81551111\n\t\t\tTEL;FAX:\n\t\t\tTEL;X_IP:\n\t\t\tTEL;PAGER:\n\t\t\tX_PAGER_ID:\n\t\t\tX_PAGER_TYPE:\n\t\t\tX_PAGER_PROVIDER:\n\t\t\tCATEGORIES:acme-airlines-it-staff,it,it-staff,itstaff,my-hero\n\t\t\tX_EXPERIENCE:\n\t\t\tX_DESCRIPTION:User Experience Character - Directory2\n\t\t\tX_MANAGER_UID:lsuarez\n\t\t\tX_IS_MANAGER:N\n\t\t\tX_PROFILE_KEY:e0b62b52-6a67-4489-b03b-4eb1f62c73e7\n\t\t\tUID:0EE5A7FA-3434-9A59-4825-7A7000278DAA\n\t\t\tX_PROFILE_UID:FAdams\n\t\t\tX_LCONN_USERID:0EE5A7FA-3434-9A59-4825-7A7000278DAA\n\t\t\tX_EMPLOYEE_NUMBER:\n\t\t\tX_DEPARTMENT_NUMBER:\n\t\t\tX_DEPARTMENT_TITLE:\n\t\t\tX_SHIFT:\n\t\t\tREV:2013-04-05T17:15:36.427Z\n\t\t\tX_PROFILE_TYPE:default\n\t\t\tEND:VCARD", json.getString("a:content"));
        Assert.assertEquals("tag:profiles.ibm.com,2006:entrye0b62b52-6a67-4489-b03b-4eb1f62c73e7", json.getString("a:id"));
        Assert.assertEquals("Frank Adams", json.getString("a:contributor/a:name"));
        Assert.assertEquals("FrankAdams@renovations.com", json.getString("a:contributor/a:email"));
        Assert.assertEquals("Frank Adams", json.getString("a:title"));
        Assert.assertEquals("2013-04-05T17:15:36.427Z", json.getString("a:updated"));
        Assert.assertEquals("https://qs.renovations.com:444/profiles/photo.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427", json.getString("a:link[@rel='http://www.ibm.com/xmlns/prod/sn/image']/@href"));
        Assert.assertEquals("Chief Operating Officer", json.getString("TITLE"));
        Assert.assertEquals("55555555", json.getString("TEL;WORK"));
        Assert.assertEquals("Building1", json.getString("X_BUILDING"));
        Assert.assertEquals("Floor1", json.getString("X_FLOOR"));
        Assert.assertEquals("Office1", json.getString("X_OFFICE_NUMBER"));
        Assert.assertEquals("Profile information for Frank Adams", json.getString("a:summary[@type='text']"));
        Assert.assertEquals("fadams@gmail.com", json.getString("EMAIL;X_GROUPWARE_MAIL"));
    }

}
