package com.ibm.sbt.services.client.connections.profiles;

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
import com.ibm.sbt.services.client.SerializationUtil;

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

		Profile profile = new Profile(profileAdminService, "testUser");
		profile.setAsString("guid", "testUserD9A04-F2E1-1222-4825-7A700026E92C");
		profile.setAsString("email", "testUser@renovations.com");
		profile.setAsString("uid", "testUser");
		profile.setAsString("distinguishedName",
				"CN=testUser def,o=renovations");
		profile.setAsString("displayName", "testUser");
		profile.setAsString("givenNames", "testUser");
		profile.setAsString("surname", "testUser");
		profile.setAsString("userState", "active");

		profileAdminService.createProfile(profile);

		profile = profileService.getProfile("testUser@renovations.com");
		assertNotNull(profile);
		assertEquals("testUser", profile.getName());
		assertEquals("testUser@renovations.com", profile.getEmail());
		assertNotNull(profile.getUserid());
		assertNotNull(profile.getProfileUrl());
		assertNotNull(profile.getPronunciationUrl());
		assertNotNull(profile.getThumbnailUrl());

		profileAdminService.deleteProfile(profile.getUserid());
	}

	@Test(expected = ProfileServiceException.class)
	public final void testGetProfileWithNullUserId() throws Exception {
		profileService.getProfile(null);
	}

	@Test
	public void testSearchProfilesWithParams() throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("email", properties.getProperty("email1"));
		parameters.put("ps", "5");
		ProfileList profileEntries = profileService.searchProfiles(parameters);
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
		ProfileList profileEntries = profileService.searchProfiles(parameters);
		assertEquals(0, profileEntries.size());
	}

	@Test
	public void testGetColleagues() throws Exception {
		ProfileList profileEntries = profileService.getColleagues(properties
				.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testGetColleaguesForInvalidUser() throws Exception {
		ProfileList profileEntries = profileService.getColleagues("abc@xyz.c");
		assertEquals(0, profileEntries.size());
	}

	@Test
	public void testGetColleaguesConnectionEntries() throws Exception {
		ColleagueConnectionList connectionEntries = profileService
				.getColleagueConnections(properties.getProperty("user1"));
		if (connectionEntries != null && !connectionEntries.isEmpty()) {
			for (ColleagueConnection Connection : connectionEntries) {
				assertNotNull(Connection.getTitle());
			}
		}
	}

	@Test
	public void testGetColleaguesConnectionEntriesForInvalidUser()
			throws Exception {
		ColleagueConnectionList connectionEntries = profileService
				.getColleagueConnections("abc@xyz.c");
		assertEquals(0, connectionEntries.size());
	}

	@Test
	public void testCheckColleague() throws Exception {
		String connectionId = profileService.sendInvite(properties.getProperty("email2"));
		ColleagueConnection connection = profileService.checkColleague(
				properties.getProperty("email1"),
				properties.getProperty("email2"));
		assertNotNull(connection.getTitle());
		assertNotNull(connection.getConnectionId());
		//assertEquals(connection.getConnectionId(), connectionId);
		//profileService.deleteInvite(connectionId);
		profileService.deleteInvite(connection.getConnectionId());
		//Interestingly, the connectionId retrieved when the invite is sent and the one on the ColleagueConnection are different
		//but both work to delete the invite
		//Also, if there is an invite it is like an actual connection, so we must probably check the "status" of the connection
	}

	@Test
	public void testGetCommonColleaguesProfiles() throws Exception {
		ProfileList profileEntries = profileService.getCommonColleagues(
				properties.getProperty("email1"),
				properties.getProperty("email2"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profileEntry : profileEntries) {
				assertNotNull(profileEntry.getJobTitle());
			}
		}
	}

	@Test
	public void testGetReportToChain() throws Exception {
		ProfileList profileEntries = profileService
				.getReportingChain(properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testGetDirectReports() throws Exception {
		ProfileList profileEntries = profileService.getPeopleManaged(properties
				.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}

	@Test
	public void testSendInvite() throws Exception {
		String connectionId = profileService.sendInvite(properties.getProperty("email2"));
		profileService.deleteInvite(connectionId);
	}

	@Test
	public final void testUpdateProfile() throws Exception {
		Profile profile = profileService.getProfile(properties
				.getProperty("email1"));
		profile.setTelephoneNumber("9999999999");
		profileService.updateProfile(profile);
		profile = profileService.getProfile(properties.getProperty("email1"));
		assertEquals("9999999999", profile.getTelephoneNumber());

		profileService.getEndpoint().logout();
	}

	/**
	 * Updating Profile of a user with credentials of some other user
	 */
	@Test(expected = ProfileServiceException.class)
	public final void testUpdateProfileWithInvalidCredentials()
			throws Exception {
		Profile profile = profileService.getProfile(properties
				.getProperty("email2"));
		profile.setTelephoneNumber("TEST_PHONE_NUMBER");
		profileService.updateProfile(profile);
	}

	@Test(expected = ProfileServiceException.class)
	public final void testUpdateProfileWithNullArgument() throws Exception {
		profileService.updateProfile(null);
	}

	@Test
	public final void testUpdateProfilePhoto() throws Exception {
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("src/test/java/com/ibm/sbt/config/image1.jpg");
		profileService.updateProfilePhoto(file, profile.getUserid());
		//profile = profileService.getProfile(properties.getProperty("email1"));
		//TODO: To improve the test, we could download the file after updating and do a checksum test to see if that is the updated file
		// also, do a checksum before updating to see that it is the other file, and update to the first or to the second file
	}

	@Test
	public final void testUpdateProfilePhotoNullExtension() throws Exception {
		Profile profile = profileService.getProfile(properties
				.getProperty("email1"));
		File file = new File("config/image");
		thrown.expect(ProfileServiceException.class);
		thrown.expectMessage("Cannot open the file");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}

	@Test
	public final void testUpdateProfilePhotoForInvalidFilePath()
			throws Exception {
		Profile profile = profileService.getProfile(properties
				.getProperty("email1"));
		File file = new File("image1.jpg");
		profile.setPhotoLocation(file.getAbsolutePath());
		thrown.expect(ProfileServiceException.class);
		thrown.expectMessage("Cannot open the file");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}
	
	@Test
	public final void testProfileSerialization() throws Exception {
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				Profile profileObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					profileObject = (Profile) ois.readObject();
				} catch (Exception e) {}
				assertEquals(profileObject.getEmail(), properties.getProperty("email1"));
			}
		}.isSerializable(profile);
	}
		
	@Test
	public final void testProfileListSerialization() throws Exception {
		ProfileList profiles = profileService.getReportingChain(properties.getProperty("email1"));
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				ProfileList allprofiles = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allprofiles = (ProfileList) ois.readObject();
					for (Iterator<Profile> iterator = allprofiles.iterator(); iterator.hasNext();) {
						Profile localprofile = iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allprofiles.size()>0));
			}
		}.isSerializable(profiles);
	}
}
