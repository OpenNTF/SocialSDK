package com.ibm.sbt.services.client.connections.activities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.test.lib.TestEnvironment;

public class ActivityDeleteMembers extends BaseActivityServiceTest {

	Activity activity;
	
	@Before
	public void AddMembersToActivity() throws ClientServicesException, XMLException {
		String memberId = TestEnvironment.getSecondaryUserUuid();
		String memberId2 = TestEnvironment.getThirdUserUuid();
		Member member = new Member();
		Member member2 = new Member();
		member.setId(memberId);
		member2.setId(memberId2);
		Member[] members = {member,member2};
		activity = createActivity();
		activityService.addMembers(activity, Arrays.asList(members));
	}
	
	@Test
	public void testDeleteMembers() throws ClientServicesException, XMLException {
		String memberId = TestEnvironment.getSecondaryUserUuid();
		String memberId2 = TestEnvironment.getThirdUserUuid();
		Member member = new Member();
		Member member2 = new Member();
		member.setId(memberId);
		member2.setId(memberId2);
		Member[] members = {member,member2};
		
		List<Member> membersBefore = activityService.getMembers(activity.getActivityUuid());
		activityService.removeMembers(activity.getActivityUuid(), Arrays.asList(members), null);
		
		List<Member> membersAfter = activityService.getMembers(activity.getActivityUuid());
		assertTrue(membersAfter.size() == membersBefore.size()-2);
	}

}
