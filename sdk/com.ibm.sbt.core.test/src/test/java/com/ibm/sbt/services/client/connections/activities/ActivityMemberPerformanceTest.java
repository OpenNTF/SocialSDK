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
package com.ibm.sbt.services.client.connections.activities;

import java.text.MessageFormat;

import org.junit.Test;
import org.w3c.dom.Document;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Member;

/**
 * @author mwallace
 *
 */
public class ActivityMemberPerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testAddActivityMember() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		// e.g. ajones{0}@janet.iris.com 
		String emailPattern = System.getProperty("EmailPattern");
		
		for (int i=2; i<500; i++) {
			try {
				long startAdd = System.currentTimeMillis();
				String email = MessageFormat.format(emailPattern, i); 
				Member member = activity.addMember(Member.TYPE_PERSON, email, Member.ROLE_OWNER);
				long durationAdd = System.currentTimeMillis() - startAdd;
				
				long startRead = System.currentTimeMillis();
				EntityList<Member> members = activity.getMembers();
				XmlDataHandler dataHandler = new XmlDataHandler((Document)members.getData(), ConnectionsConstants.nameSpaceCtx);
				long durationRead = System.currentTimeMillis() - startRead;
				
				System.out.println(email+","+durationAdd+","+durationRead);
			} catch (Exception ignore) {
			}
		}
	}
	
}
