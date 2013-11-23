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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class BaseEntity extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_BaseEntity";
    
    static final ArrayList<String> fooBarBaz = new ArrayList<String>();
    {
        fooBarBaz.add("foo");
        fooBarBaz.add("bar");
        fooBarBaz.add("baz");
    }
    
    static final ArrayList<String> foo = new ArrayList<String>();
    {
        foo.add("foo");
    }
    
    static final ArrayList<Double> eight = new ArrayList<Double>();
    {
        eight.add(new Double(8));
    }
    
    static final ArrayList<Boolean> bool = new ArrayList<Boolean>();
    {
        bool.add(Boolean.TRUE);
    }
    
    static final ArrayList<String> links = new ArrayList<String>();
    {
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fblogs%2Froller-ui%2Fblog%2F0EE5A7FA-3434-9A59-4825-7A7000278DAA");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fforums%2Fhtml%2Fsearch%3Fuserid%3D0EE5A7FA-3434-9A59-4825-7A7000278DAA%26name%3DFrank+Adams");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fwikis%2Fhome%2Fsearch%3Fuid%3D0EE5A7FA-3434-9A59-4825-7A7000278DAA%26name%3DFrank+Adams");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Ffiles%2Fapp%2Fperson%2F0EE5A7FA-3434-9A59-4825-7A7000278DAA");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fcommunities%2Fservice%2Fhtml%2Fallcommunities%3Fuserid%3D0EE5A7FA-3434-9A59-4825-7A7000278DAA");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fprofiles%2Fhtml%2FsimpleSearch.do%3FsearchFor%3D0EE5A7FA-3434-9A59-4825-7A7000278DAA%26searchBy%3Duserid");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Fdogear%2Fhtml%3Fuserid%3D0EE5A7FA-3434-9A59-4825-7A7000278DAA");
        links.add("https%3A%2F%2Fqs.renovations.com%3A444%2Factivities%2Fservice%2Fhtml%2Fmainpage%23dashboard%252Cmyactivities%252Cuserid%253D0EE5A7FA-3434-9A59-4825-7A7000278DAA%252Cname%253DFrank+Adams");
        links.add("https://qs.renovations.com:444/profiles/atom/profileEntry.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&format=full");
        links.add("https://qs.renovations.com:444/profiles/atom/profileType.do?type=default");
        links.add("https://qs.renovations.com:444/profiles/html/profileView.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7");
        links.add("https://qs.renovations.com:444/profiles/photo.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427");
        links.add("https://qs.renovations.com:444/profiles/audio.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7&lastMod=1365182136427");
        links.add("https://qs.renovations.com:444/profiles/vcard/profile.do?key=e0b62b52-6a67-4489-b03b-4eb1f62c73e7");
    }
    
    static final Object[][] Results = new Object[][] {
            { "userid", "userid123" },
            { "userid", "foo,bar" },
            { "null", "Invalid argument for BaseService.setAsString undefined,userid123" },
            { "undefined", "Invalid argument for BaseService.setAsString undefined,userid123" },
            { "userid", "[object Object]" },
            { "userid", "0EE5A7FA-3434-9A59-4825-7A7000278DAA" },
            { "userid", "0EE5A7FA-3434-9A59-4825-7A7000278DAA" },
            { "number", new Double(8) },
            { "number", new Double(8) },
            { "number", new Double(12) },
            { "null", "Invalid argument for BaseService.setAsNumber undefined,0" },
            { "undefined", "Invalid argument for BaseService.setAsNumber undefined,0" },
            { "number", "Invalid argument for BaseService.setAsNumber number,foo" },
            { "number", "Invalid argument for BaseService.setAsNumber number,[object Object]" },
            { "number", "Invalid argument for BaseService.setAsNumber number,foo" },
            { "number", new Double(12) },
            { "updated", null}, //"2013-04-30T23:00:00.000Z" },
            { "updated", null}, //"2013-03-28T21:14:14.649Z" },
            { "updated", null}, //"2013-04-05T17:15:36.427Z" },
            { "null", null}, //"Invalid argument for BaseService.setAsDate undefined,Wed May 01 2013 00:00:00 GMT+0100 (GMT Daylight Time)" },
            { "undefined", "Invalid argument for BaseService.setAsDate undefined,Wed May 01 2013 00:00:00 GMT+0100 (GMT Daylight Time)" },
            { "updated", "Invalid argument for BaseService.setAsDate updated,foo" },
            { "updated", "Invalid argument for BaseService.setAsDate updated,[object Object]" },
            { "updated", "Invalid argument for BaseService.setAsDate updated,foo" },
            { "updated", null}, //"2013-04-05T17:15:36.427Z" },
            { "boolean", "true" },
            { "boolean", "false" },
            { "boolean", "true" },
            { "boolean", "true" },
            { "boolean", "false" },
            { "null", "Invalid argument for BaseService.setAsBoolean undefined,undefined" },
            { "undefined", "Invalid argument for BaseService.setAsBoolean undefined,undefined" },
            { "boolean", "true" },
            { "boolean", "true" },
            { "boolean", "true" },
            { "boolean", "false" },
            { "a:link/@href", fooBarBaz },
            { "a:link/@href", fooBarBaz },
            { "a:link/@href", foo },
            { "a:link/@href", eight },
            { "a:link/@href", bool },
            { "a:link/@href", links },
            { "null", "Invalid argument for BaseService.setAsArray undefined,foo,bar,baz" },
            { "undefined", "Invalid argument for BaseService.setAsArray undefined,foo,bar,baz" },
            { "a:link/@href", links }
    };
    
    @Test
    @Ignore
    public void testBaseEntity() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        for (int i=0; i<jsonList.size(); i++) {
            JsonJavaObject json = (JsonJavaObject)jsonList.get(i);
            Iterator<String> properties = json.getProperties();
            if (properties.hasNext() && Results[i][1] != null) {
                String property = properties.next();
                Object value = json.get(property);
                Assert.assertEquals(Results[i][0], property);
                Assert.assertEquals("Match failed ["+i+"] name:"+property+" type:"+value.getClass(), Results[i][1], value);
            }
        }
    }
    
}
