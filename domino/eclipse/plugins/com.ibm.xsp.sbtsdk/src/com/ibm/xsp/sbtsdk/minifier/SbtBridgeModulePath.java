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

package com.ibm.xsp.sbtsdk.minifier;

import com.ibm.xsp.resource.DojoModulePathResource;

/**
 * @author priand
 */
public class SbtBridgeModulePath extends DojoModulePathResource {

    public SbtBridgeModulePath() {
        super("sbt/_bridges", "/.ibmxspres/.extlib/sbt_bridges/dojo-amd"); // $NON-NLS-1$ $NON-NLS-2$
    }
}
