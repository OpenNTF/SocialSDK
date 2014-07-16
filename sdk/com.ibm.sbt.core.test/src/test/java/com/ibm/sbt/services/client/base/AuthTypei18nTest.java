/* © Copyright IBM Corp. 2014 
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
package com.ibm.sbt.services.client.base;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * 
 * @author Carlos Manias
 *
 */
public class AuthTypei18nTest {

	@Test
	public void testGetAuthTypeTurkish() {
		Locale current = Locale.getDefault();
		Locale turkey =  new Locale("tr-TR");
		Locale.setDefault(turkey);
		String basic = AuthType.BASIC.get();
		assertEquals("basic", basic);
		Locale.setDefault(current);
	}

}