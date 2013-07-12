package com.ibm.sbt.services.client.connections.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.profiles.ProfileAdminService;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceException;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileList;
import com.ibm.sbt.services.client.connections.profiles.ConnectionEntryList;
import com.ibm.sbt.services.client.connections.profiles.ConnectionEntry;;

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
		assertEquals("mockUser", profile.getDisplayName());
	}


		
	@Test
	public final void testGetProfile() throws Exception {

		ProfileAdminService profileAdminService = new ProfileAdminService();
		profileAdminService.getEndpoint().logout();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = profileAdminService.newProfile();
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
		assertEquals("testUser", profile.getDisplayName());
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
				assertNotNull(profile.getDisplayName());
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
				assertNotNull(profile.getDisplayName());
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
		ConnectionEntryList connectionEntries = profileService.getColleaguesConnectionEntries( properties.getProperty("user1"));
		if (connectionEntries != null && !connectionEntries.isEmpty()) {
			for (ConnectionEntry connectionEntry : connectionEntries) {
				assertNotNull(connectionEntry.getTitle());
			}
		}
	}
	
	@Test
	public void testGetColleaguesConnectionEntriesForInvalidUser() throws Exception {
		ProfileService profileService = new ProfileService();
		ConnectionEntryList connectionEntries = profileService.getColleaguesConnectionEntries("abc@xyz.c");
		assertEquals(0, connectionEntries.size());
	}
	
	@Ignore
	@Test
	public void testCheckColleague() throws Exception {
		ProfileService profileService = new ProfileService();
		ConnectionEntry connectionEntry = profileService.checkColleague(properties.getProperty("email1"), properties.getProperty("email3"));
		assertNotNull(connectionEntry.getTitle());
		assertNotNull(connectionEntry.getConnectionId());
	}


	@Test
	public void testGetCommonColleaguesProfiles() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getCommonColleaguesProfiles( properties.getProperty("email1"),properties.getProperty("email2") );
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profileEntry : profileEntries) {
				assertNotNull(profileEntry.getTitle());
			}
		}
	}

	@Test
	public void testGetConnectionsColleagueEntriesByStatus() throws Exception {
		ProfileService profileService = new ProfileService();
		ConnectionEntryList connectionEntries = profileService.getConnectionsColleagueEntriesByStatus( properties.getProperty("user1"),properties.getProperty("accepted") );
		if (connectionEntries != null && !connectionEntries.isEmpty()) {
			for (ConnectionEntry connectionEntry : connectionEntries) {
				assertNotNull(connectionEntry.getTitle());
			}
		}
	}
	
	@Test
	public void testGetConnectionsProfileEntriesByStatus() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getConnectionsProfileEntriesByStatus( properties.getProperty("user1"),properties.getProperty("accepted") );
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profileEntry : profileEntries) {
				assertNotNull(profileEntry.getTitle());
			}
		}
	}
	
	@Test
	public void testGetReportToChain() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getReportToChain( properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getDisplayName());
			}
		}
	}
	
	@Test
	public void testGetDirectReports() throws Exception {
		ProfileService profileService = new ProfileService();
		ProfileList profileEntries = profileService.getDirectReports( properties.getProperty("user1"));
		if (profileEntries != null && !profileEntries.isEmpty()) {
			for (Profile profile : profileEntries) {
				assertNotNull(profile.getDisplayName());
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
		profile.setPhoneNumber("9999999999");
		profileService.updateProfile(profile);
		profile = profileService.getProfile(properties.getProperty("email1"));
		assertEquals("9999999999", profile.getPhoneNumber());

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
		profile.setPhoneNumber("TEST_PHONE_NUMBER");
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
		profile.setPhotoLocation(file.getAbsolutePath());
		profileService.updateProfilePhoto(profile);
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhotoNullExtension() throws Exception {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("config/image");
		profile.setPhotoLocation(file.getAbsolutePath());
		profileService.updateProfilePhoto(profile);
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
		profileService.updateProfilePhoto(profile);
	}
	

}
