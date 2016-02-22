/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.automation.core.test;

/**
 * @author Francis 
 * @since 16 Jul 2013
 */
public class BaseViewTest extends BaseApiTest {
    /**
     * Default constructor
     */
    public BaseViewTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Override
    protected boolean isEnvironmentValid() {
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
}
