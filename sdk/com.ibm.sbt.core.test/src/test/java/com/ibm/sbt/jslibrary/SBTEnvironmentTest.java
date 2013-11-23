/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.jslibrary;

import com.ibm.sbt.jslibrary.SBTEnvironment.Property;

import junit.framework.TestCase;

/**
 * @author mwallace
 *
 */
public class SBTEnvironmentTest extends TestCase {

	public void testSetProperties() {
		SBTEnvironment environment = new SBTEnvironment();
		environment.setProperties("sample.email1={%sample.email1}\nsample.email2={%sample.email2}");
		Property[] properties = environment.getPropertiesArray();
		assertEquals("Error setting environment properties", 2, properties.length);
	}
}
