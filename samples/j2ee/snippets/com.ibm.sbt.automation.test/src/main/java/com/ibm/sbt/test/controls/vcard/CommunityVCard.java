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
package com.ibm.sbt.test.controls.vcard;

import static org.junit.Assert.*;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseVCardTest;

/**
 * @author sberrybyrne
 * @since 20 Mar 2013
 */
public class CommunityVCard extends BaseVCardTest{
	
	@Test
	public void testCommunityVCard() {
		assertTrue("Expected to find the Community vCard on the page", checkCommunityVCard("Social_Communities_Controls_Community_VCard"));
	}
	
}
