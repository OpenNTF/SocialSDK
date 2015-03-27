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
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class MakeActivityPublicTest extends BaseActivityServiceTest {

	/*
	 * Used this test to validate private/public activities created via the UI
	 * @Test
	 */
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testReadActivities() throws ClientServicesException, XMLException {
		String privateUuid = TestEnvironment.getProperty("privateUuid");
		String publicUuid = TestEnvironment.getProperty("publicUuid");
		
		if (!StringUtil.isEmpty(privateUuid)) {
			Activity privateActivity = activityService.getActivity(privateUuid);
			EntityList<Member> members = privateActivity.getMembers();
			Assert.assertEquals(1, members.size());
		}
		
		if (!StringUtil.isEmpty(publicUuid)) {
			Activity publicActivity = activityService.getActivity(publicUuid);
			EntityList<Member> members = publicActivity.getMembers();
			Assert.assertEquals(2, members.size());
			for (Member nMember : members) {
				System.out.println(nMember.toXmlString());
			}
			String orgid = TestEnvironment.getProperty("customerid");
			orgid = StringUtil.isEmpty(orgid) ? "*" : orgid;
			Member member = getMember(publicActivity, orgid);
			Assert.assertNotNull(member);
		}
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testMakeActivityPublic() throws ClientServicesException, XMLException {
		Activity created = createActivity();
		
		// uncomment to stop the activity being deleted
		//this.activity = null;
		
		EntityList<Member> members = created.getMembers();
		Assert.assertEquals(1, members.size());
		
		String orgid = TestEnvironment.getProperty("customerid");
		orgid = StringUtil.isEmpty(orgid) ? "*" : orgid;
		created.addMember(Member.TYPE_ORGANIZATION, orgid, Member.ROLE_READER);
		members = created.getMembers();
		Assert.assertEquals(2, members.size());
		Member member = getMember(created, orgid);
		Assert.assertNotNull(member);
		Assert.assertEquals(orgid, member.getContributor().getUserid());
		Assert.assertEquals(Member.ROLE_READER, member.getRole());
	}

}
