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

package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v5_0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SerializationUtil;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Tag;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Profile API by calling Connections server
 * using configuration in managed-beans
 * 
 * @author Swati Singh
 * @author Vineet Kanwal
 */
public class ProfileServiceTest extends BaseUnitTest {

	@Rule public ExpectedException thrown= ExpectedException.none();
	protected ProfileService profileService;

	@Before
	public void initProfileServiceTest() {
		if (profileService==null){
			profileService = new ProfileService();
		}
	}

	@Test
	public final void testGetProfile() throws Exception {

		ProfileAdminService profileAdminService = new ProfileAdminService();
		profileAdminService.getEndpoint().logout();
		String userId = "testUserD9A04-F2E1-1222-4825-7A700026E92C";
		String userEmail = "testUser@renovations.com";
		String userName = "testUser";

		Profile profile = new Profile(profileAdminService, "testUser");
		profile.setAsString("guid", userId);
		profile.setAsString("email", userEmail);
		profile.setAsString("uid", userName);
		profile.setAsString("distinguishedName",
				"CN=testUser def,o=renovations");
		profile.setAsString("displayName", userName);
		profile.setAsString("givenNames", userName);
		profile.setAsString("surname", userName);
		
		profile.setAsString("userState", "active");

		profileAdminService.createProfile(profile);

		profile = profileService.getProfile("testUser@renovations.com");
		assertNotNull(profile);
		assertEquals(userName, profile.getName());
		assertEquals(userEmail, profile.getEmail());
		assertEquals(userId, profile.getUserid());

		profileAdminService.deleteProfile(profile.getUserid());
	}

	@Test()
	public final void testGetProfileWithNullUserId() throws Exception {
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Required input parameter is missing : id");
		profileService.getProfile(null);
	}

	@Test
	public void testSearchProfilesWithParams() throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("email", TestEnvironment.getCurrentUserEmail());
		parameters.put("ps", "5");
		EntityList<Profile> profileEntries = profileService.searchProfiles(parameters);
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testSearchProfilesWithInvalidParams() throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("email", "abc@xyz.c");
		EntityList<Profile> profileEntries = profileService.searchProfiles(parameters);
		assertEquals(0, profileEntries.size());
	}

	@Test
	public void testGetColleagues() throws Exception {
		EntityList<Profile> profileEntries = profileService.getColleagues(TestEnvironment.getCurrentUserEmail());
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testGetColleaguesForInvalidUser() throws Exception {
		if (profileService.getApiVersion().isAtLeast(v5_0)){
			thrown.expect(ClientServicesException.class);
			thrown.expectMessage("400:Bad Request");
		}
		EntityList<Profile> profileEntries = profileService.getColleagues("abc@xyz.c");
		if (profileService.getApiVersion().lessThan(v5_0)){
			assertEquals(0, profileEntries.size());
		}
	}

	@Test
	public void testGetColleaguesConnectionEntries() throws Exception {
		EntityList<ColleagueConnection> connectionEntries = profileService
				.getColleagueConnections(TestEnvironment.getCurrentUserEmail());
		if (connectionEntries != null && !connectionEntries.isEmpty()) {
			for (ColleagueConnection Connection : connectionEntries) {
				assertNotNull(Connection.getTitle());
			}
		}
	}

	@Test
	public void testGetColleaguesConnectionEntriesForInvalidUser()
			throws Exception {
		if (profileService.getApiVersion().isAtLeast(v5_0)){
			thrown.expect(ClientServicesException.class);
			thrown.expectMessage("400:Bad Request");
		}
		EntityList<ColleagueConnection> connectionEntries = profileService
				.getColleagueConnections("abc@xyz.c");
		if (profileService.getApiVersion().lessThan(v5_0)){
			assertEquals(0, connectionEntries.size());
		}
	}

	@Test
	public void testGetCommonColleaguesProfiles() throws Exception {
		EntityList<Profile> profileEntries = profileService.getCommonColleagues(
				TestEnvironment.getCurrentUserEmail(),
				TestEnvironment.getSecondaryUserEmail());
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profileEntry : profileEntries) {
				assertNotNull(profileEntry.getJobTitle());
			}
		}
	}

	@Test
	public void testGetReportToChain() throws Exception {
		EntityList<Profile> profileEntries = profileService
				.getReportingChain(TestEnvironment.getCurrentUserEmail());
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testGetDirectReports() throws Exception {
		EntityList<Profile> profileEntries = profileService.getPeopleManaged(TestEnvironment.getCurrentUserEmail());
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testSendInviteAndCheckColleagues() throws Exception {
		String user1 = TestEnvironment.getCurrentUserEmail();
		String user2 = TestEnvironment.getSecondaryUserEmail();
		String connectionId = profileService.sendInvite(TestEnvironment.getSecondaryUserEmail());
		ColleagueConnection connection = profileService.checkColleague(user1, user2);
		assertNotNull(connection.getTitle());
		assertNotNull(connection.getConnectionId());
		assertEquals(user2, connection.getContributorEmail());

		//Fix the Person abstraction is no performing xpath lookups correctly
		//assertEquals(user2, connection.getContributor().getEmail());
		profileService.deleteInvite(connectionId);
	}

	@Test
	public final void testUpdateProfile() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		String phoneNumber = "9999999999";
		profile.setTelephoneNumber(phoneNumber);
		profileService.updateProfile(profile);
		profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		assertEquals(phoneNumber, profile.getTelephoneNumber());

		profileService.getEndpoint().logout();
	}
	
	@Test
	public final void testAddTags() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getSecondaryUserEmail());
		profile.setAsString("tags", "tag1, tag2, tag3");
		profileService.addTags(TestEnvironment.getCurrentUserEmail(), TestEnvironment.getSecondaryUserEmail(), profile);

		EntityList<Tag> tags = profileService.getTags(TestEnvironment.getSecondaryUserEmail());
		for(Tag tag : tags){
			assertNotNull(tag.getTerm());
		}

		profileService.getEndpoint().logout();
	}

	/**
	 * Updating Profile of a user with credentials of some other user
	 */
	@Test
	public final void testUpdateProfileWithInvalidCredentials() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getSecondaryUserEmail());
		profile.setTelephoneNumber("TEST_PHONE_NUMBER");
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("403:Forbidden");
		profileService.updateProfile(profile);
	}


	@Test
	public final void testUpdateProfileWithNullArgument() throws Exception {
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Invalid Input : Profile passed is null");
		profileService.updateProfile(null);
	}

	@Test
	public final void testUpdateProfilePhoto() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		File file = new File("src/test/java/com/ibm/sbt/config/image1.jpg");
		profileService.updateProfilePhoto(file, profile.getUserid());
		//profile = profileService.getProfile(properties.getProperty("email1"));
		//TODO: To improve the test, we could download the file after updating and do a checksum test to see if that is the updated file
		// also, do a checksum before updating to see that it is the other file, and update to the first or to the second file
	}

	@Test
	public final void testUpdateProfilePhotoNullExtension() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		File file = new File("config/image");
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Cannot open the file");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}

	@Test
	public final void testUpdateProfilePhotoForInvalidFilePath()
			throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		File file = new File("image1.jpg");
		profile.setPhotoLocation(file.getAbsolutePath());
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("Cannot open the file");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}
	
	@Test
	public final void testProfileSerialization() throws Exception {
		Profile profile = profileService.getProfile(TestEnvironment.getCurrentUserEmail());
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				Profile profileObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					profileObject = (Profile) ois.readObject();
				} catch (Exception e) {}
				assertEquals(TestEnvironment.getCurrentUserEmail(), profileObject.getEmail());
			}
		}.isSerializable(profile);
	}
		
	@Test
	public final void testProfileListSerialization() throws Exception {
		EntityList<Profile> profiles = profileService.getReportingChain(properties.getProperty("email1"));
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				EntityList<Profile> allprofiles = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allprofiles = (EntityList<Profile>) ois.readObject();
					for (Iterator<Profile> iterator = allprofiles.iterator(); iterator.hasNext();) {
						Profile localprofile = iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allprofiles.size()>0));
			}
		}.isSerializable(profiles);
	}
}
