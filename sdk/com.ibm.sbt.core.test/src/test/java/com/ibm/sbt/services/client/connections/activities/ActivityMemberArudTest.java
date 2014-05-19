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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Member;

/**
 * @author mwallace
 *
 */
public class ActivityMemberArudTest extends BaseActivityServiceTest {

	@Test
	public void testAddActivityMember() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		Member member = activity.addMember(Member.TYPE_PERSON, getMemberId(), Member.ROLE_OWNER);
		//System.out.println(member.toXmlString());
		System.out.println(member.getId());
		
		EntityList<Member> members = activity.getMembers();
		boolean found = false;
		for (Member nextMember : members) {
			System.out.println(nextMember.getId());
			//System.out.println(nextMember.getTitle());
			//System.out.println(nextMember.getRole());
			//System.out.println(nextMember.toXmlString());
			if (nextMember.getId().equals(member.getId())) {
				found = true;
				break;
			}
		}
		Assert.assertEquals(2, members.size());
		Assert.assertTrue(found);
	}
	
	@Test
	public void testRetrieveActivityMember() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		Member member = activity.addMember(Member.TYPE_PERSON, getMemberId(), Member.ROLE_OWNER);
		//System.out.println(member.toXmlString());
		System.out.println(member.getId());
		String id = member.getId();
		
		member = activity.getMember(member.getId());
		Assert.assertNotNull(member);
		Assert.assertEquals(Member.ROLE_OWNER, member.getRole());
		Assert.assertEquals(id, member.getId());
	}
	
	@Test
	public void testUpdateActivityMember() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		Member member = activity.addMember(Member.TYPE_PERSON, getMemberId(), Member.ROLE_MEMBER);
		//System.out.println(member.toXmlString());
		System.out.println(member.getId());
		String id = member.getId();
		
		member.setRole(Member.ROLE_OWNER);
		activity.updateMember(member);
		
		member = activity.getMember(member.getId());
		Assert.assertNotNull(member);
		Assert.assertEquals(Member.ROLE_OWNER, member.getRole());
		Assert.assertEquals(id, member.getId());
	}
	
	@Test
	public void testDeleteActivityMember() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		Member member = activity.addMember(Member.TYPE_PERSON, getMemberId(), Member.ROLE_MEMBER);
		//System.out.println(member.toXmlString());
		//String id = member.getId();
		
		EntityList<Member> members = activity.getMembers();
		Assert.assertEquals(2, members.size());
		
		activity.deleteMember(member);
		
		members = activity.getMembers();
		Assert.assertEquals(1, members.size());
	}
	
}
