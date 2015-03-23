package com.ibm.sbt.services.client.connections.activities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.test.lib.TestEnvironment;

public class ActivityAddMembers extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testAddMembers() throws ClientServicesException, XMLException {
		String memberId = TestEnvironment.getSecondaryUserUuid();
		String memberId2 = TestEnvironment.getCurrentUserUuid();
		Member member = new Member();
		Member member2 = new Member();
		member.setId(memberId);
		member2.setId(memberId2);
		Member[] members = { member, member2 };
		Activity activity = createActivity();
		activityService.addMembers(activity, Arrays.asList(members));
		List<Member> members2 = activityService.getMembers(activity.getActivityUuid());
		assertTrue(members2.size() == members.length);
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testBadUserId() throws ClientServicesException, XMLException {
		Member member = new Member();
		Member member2 = new Member();
		member.setId("123321");
		member2.setId("32123");
		Member[] members = {member,member2};
		Activity activity = createActivity();
		try{
			activityService.addMembers(activity, Arrays.asList(members));
		}
		catch(ClientServicesException e){
			assertTrue(404 == e.getResponseStatusCode());
		}	
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testBadActivityId() throws ClientServicesException, XMLException {
		String memberId = TestEnvironment.getSecondaryUserUuid();
		String memberId2 = TestEnvironment.getCurrentUserUuid();
		Member member = new Member();
		Member member2 = new Member();
		member.setId(memberId);
		member2.setId(memberId2);
		Member[] members = {member,member2};
		Activity activity = createActivity();
		try{
			activityService.addMembers("1234322", Arrays.asList(members));
		}
		catch(ClientServicesException e){
			//Activities return a 200 when the activity uuid is not found
			assertTrue(200 == e.getResponseStatusCode());
		}
		
	}
	
	
}
