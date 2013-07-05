/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.xsp.extlib.sbt.resources;

import com.ibm.xsp.resource.DojoModuleResource;


/**
 * Shared Social Business Toolkit resources.
 * @author Philippe Riand
 */
public class SBTResources {
	
    //
    // Sametime
    //
    public static final String SAMETIME_TUNNEL_HTML = "/.ibmxspres/.extlib/sbt/sametime/tunnel.html";
    public static final String FACEBOOK_CHANNEL_HTML = "/.ibmxspres/.extlib/sbt/facebook/channel.html";

    public static final DojoModuleResource dojoSametimeLogin = new DojoModuleResource("extlib.dojo.SametimeLogin"); // $NON-NLS-1$

    public static final DojoModuleResource dojoProfilesVCard = new DojoModuleResource("extlib.dijit.ProfilesVCard"); // $NON-NLS-1$
    public static final DojoModuleResource dojoProfilesVCardInline = new DojoModuleResource("extlib.dijit.ProfilesVCardInline"); // $NON-NLS-1$
    
    //public static final DojoModuleResource sametimeLiveName = new DojoModuleResource("sametime.LiveName"); // $NON-NLS-1$
    
}