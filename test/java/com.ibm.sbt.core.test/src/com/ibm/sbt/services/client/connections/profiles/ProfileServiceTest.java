package com.ibm.sbt.services.client.connections.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;

/**
 * Tests for the java connections Profile API by calling Connections server using configuration in
 * managed-beans
 * 
 * @author Swati Singh
 * @author Vineet Kanwal
 */
public class ProfileServiceTest extends BaseUnitTest {
	
	@Ignore
	@Test
	public final void tempMethodPrePopulatedData() throws Exception {
		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile("mockUser@renovations.com");
		assertNotNull(profile);
		assertEquals("mockUser", profile.getName());
	}


		
	@Test
	public final void testGetProfile() throws Exception {

		ProfileAdminService profileAdminService = new ProfileAdminService();
		profileAdminService.getEndpoint().logout();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = new Profile(profileAdminService, "testUser");
		profile.setAsString("guid", "testUserD9A04-F2E1-1222-4825-7A700026E92C");
		profile.setAsString("email", "testUser@renovations.com");
		profile.setAsString("uid", "testUser");
		profile.setAsString("distinguishedName", "CN=testUser def,o=renovations");
		profile.setAsString("displayName", "testUser");
		profile.setAsString("givenNames", "testUser");
		profile.setAsString("surname", "testUser");
		profile.setAsString("userState", "active");

		profileAdminService.createProfile(profile);
		
		ProfileService svc = new ProfileService();
		profile = svc.getProfile("testUser@renovations.com");
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

		ProfileService profileService = new ProfileService();
		profileService.getProfile(null);
	
	}
	
	@Test
	public void testSearchProfilesWithParams() throws Exception {
		ProfileService profileService = new ProfileService();
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
		ProfileService profileService = new ProfileService();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("email", "abc@xyz.c");
		ProfileList profileEntries = profileService.searchProfiles(parameters);
		assertEquals(0, profileEntries.size());
		
	}

	@Test
	public void testGetColleagues() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getColleagues( properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}
	
	@Test
	public void testGetColleaguesForInvalidUser() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getColleagues("abc@xyz.c");
		assertEquals(0, profileEntries.size());
		
	}
	
	@Test
	public void testGetColleaguesConnectionEntries() throws Exception {
		ProfileService profileService = new ProfileService();
		ColleagueConnectionList connectionEntries = profileService.getColleagueConnections( properties.getProperty("user1"));
		if (connectionEntries != null && !connectionEntries.isEmpty()) {
			for (ColleagueConnection Connection : connectionEntries) {
				assertNotNull(Connection.getTitle());
			}
		}
	}
	
	@Test
	public void testGetColleaguesConnectionEntriesForInvalidUser() throws Exception {
		ProfileService profileService = new ProfileService();
		ColleagueConnectionList connectionEntries = profileService.getColleagueConnections("abc@xyz.c");
		assertEquals(0, connectionEntries.size());
	}
	
	@Ignore
	@Test
	public void testCheckColleague() throws Exception {
		ProfileService profileService = new ProfileService();
		ColleagueConnection Connection = profileService.checkColleague(properties.getProperty("email1"), properties.getProperty("email3"));
		assertNotNull(Connection.getTitle());
		assertNotNull(Connection.getConnectionId());
	}


	@Test
	public void testGetCommonColleaguesProfiles() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getCommonColleagues( properties.getProperty("email1"),properties.getProperty("email2") );
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profileEntry : profileEntries) {
				assertNotNull(profileEntry.getJobTitle());
			}
		}
	}

	@Test
	public void testGetReportToChain() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getReportingChain( properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}
	
	@Test
	public void testGetDirectReports() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getPeopleManaged( properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getName());
			}
		}
	}
	
	@Ignore
	@Test
	public void testSendInvite() throws Exception {
		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		
		profileService.sendInvite(properties.getProperty("email4"));
		
	}
		
	@Test
	public final void testUpdateProfile() throws Exception{

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
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
	public final void testUpdateProfileWithInvalidCredentials() throws Exception{

		ProfileService profileService = new ProfileService();
		//authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user2"),
			//	properties.getProperty("passwordUser2"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		profile.setTelephoneNumber("TEST_PHONE_NUMBER");
		profileService.updateProfile(profile);
	}

	@Test(expected = ProfileServiceException.class)
	public final void testUpdateProfileWithNullArgument() throws Exception{

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		profileService.updateProfile(null);
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhoto() throws Exception{

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("config/image.jpg");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhotoNullExtension() throws Exception {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("config/image");
		profileService.updateProfilePhoto(file, profile.getUserid());
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhotoForInvalidFilePath() throws Exception{

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("image1.jpg");
		profile.setPhotoLocation(file.getAbsolutePath());
		profileService.updateProfilePhoto(file, profile.getUserid());
	}
	

}
