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

/**
 * Tests for the java connections Profile API by calling Connections server using configuration in
 * managed-beans
 * 
 * @author Vineet Kanwal
 */
public class ProfileServiceTest extends BaseUnitTest {

	@Ignore
	@Test
	public final void testGetProfileForEmail() {

		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		assertNotNull(profile);
		assertNotNull(profile.getData());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("Profile information for Frank Adams", profile.getAbout());
		assertEquals("{building=WTFT5, floor=3rd}", profile.getAddress().toString());
		assertEquals(null, profile.getDepartment());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals(properties.getProperty("email1"), profile.getEmail());
		assertEquals(properties.getProperty("userId1"), profile.getId());
		assertEquals("123445678", profile.getPhoneNumber());
		assertNotNull(profile.getProfileUrl());
		assertNotNull(profile.getPronunciationUrl());
		assertNotNull(profile.getThumbnailUrl());
		assertEquals("Dr", profile.getTitle());
		assertEquals(properties.getProperty("userId1"), profile.getUniqueId());
	}

	@Ignore
	@Test
	public final void testGetProfileForUserId() {

		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile(properties.getProperty("userId1"));
		assertNotNull(profile);
		assertNotNull(profile.getData());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("Profile information for Frank Adams", profile.getAbout());
		assertEquals("{building=WTFT5, floor=3rd}", profile.getAddress().toString());
		assertEquals(null, profile.getDepartment());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals(properties.getProperty("email1"), profile.getEmail());
		assertEquals(properties.getProperty("userId1"), profile.getId());
		assertEquals("123445678", profile.getPhoneNumber());
		assertNotNull(profile.getProfileUrl());
		assertNotNull(profile.getPronunciationUrl());
		assertNotNull(profile.getThumbnailUrl());
		assertEquals("Dr", profile.getTitle());
		assertEquals(properties.getProperty("userId1"), profile.getUniqueId());
	}

	@Ignore
	@Test
	public final void testGetProfiles() {

		ProfileService profileService = new ProfileService();
		Profile[] profiles = profileService.getProfiles(new String[] { properties.getProperty("email1"),
				properties.getProperty("email2") });
		assertNotNull(profiles);
		assertEquals(2, profiles.length);
		assertNotNull(profiles[0]);
		assertNotNull(profiles[0].getData());
		assertEquals("Frank Adams", profiles[0].getDisplayName());
		assertNotNull(profiles[1]);
		assertNotNull(profiles[1].getData());
		assertEquals("Bill Jordan", profiles[1].getDisplayName());
	}

	@Ignore
	@Test
	public final void testGetProfileWithLoadFalse() {

		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile(properties.getProperty("email1"), false);
		assertNotNull(profile);
		assertNull(profile.getData());
	}

	@Ignore
	@Test
	public final void testGetProfileForInvalidEmail() {

		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile("Test@Ignore @Test.com");
		assertNotNull(profile);
		assertNull(profile.getDisplayName());
	}

	@Ignore
	@Test
	public final void testGetProfileForInvalidUserId() {

		ProfileService profileService = new ProfileService();
		Profile profile = profileService.getProfile("ewuirewi983298329832");
		assertNotNull(profile);
		assertNull(profile.getDisplayName());
	}

	@Ignore
	@Test
	public final void testUpdateProfile() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		Map<String, String> addressMap = new HashMap<String, String>();
		addressMap.put("building", "TEST_BUILDING");
		addressMap.put("floor", "TEST_FLOOR");
		profile.setAddress(addressMap);
		profile.setPhoneNumber("TEST_PHONE_NUMBER");
		boolean result = profileService.updateProfile(profile);
		assertEquals(true, result);
		profile = profileService.getProfile(properties.getProperty("email1"));
		assertEquals("{building=TEST_BUILDING, floor=TEST_FLOOR}", profile.getAddress().toString());
		assertEquals("TEST_PHONE_NUMBER", profile.getPhoneNumber());

		// CHANGING IT BACK TO ORIGINAL
		addressMap = new HashMap<String, String>();
		addressMap.put("building", "WTFT5");
		addressMap.put("floor", "3rd");
		profile.setAddress(addressMap);
		profile.setPhoneNumber("123445678");
		result = profileService.updateProfile(profile);
		assertEquals(true, result);
	}

	/**
	 * Updating Profile of a user with credentials of some other user
	 */
	@Ignore
	@Test
	public final void testUpdateProfileWithInvalidCredentials() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user2"),
				properties.getProperty("passwordUser2"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		Map<String, String> addressMap = new HashMap<String, String>();
		addressMap.put("building", "TEST_BUILDING");
		addressMap.put("floor", "TEST_FLOOR");
		profile.setAddress(addressMap);
		profile.setPhoneNumber("TEST_PHONE_NUMBER");
		boolean result = profileService.updateProfile(profile);
		assertFalse(result);
	}

	@Ignore
	@Test
	public final void testUpdateProfileForEmptyFields() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		boolean result = profileService.updateProfile(profile);
		assertEquals(true, result);
	}

	@Ignore
	@Test
	public final void testUpdateProfileForNull() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		boolean result = profileService.updateProfile(null);
		assertEquals(false, result);
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhoto() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("config/image.jpg");
		profile.setPhotoLocation(file.getAbsolutePath());
		boolean result = profileService.updateProfilePhoto(profile);
		assertEquals(true, result);
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhotoNullExtension() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("config/image");
		profile.setPhotoLocation(file.getAbsolutePath());
		boolean result = profileService.updateProfilePhoto(profile);
		assertEquals(false, result);
	}

	@Ignore
	@Test
	public final void testUpdateProfilePhotoForInvalidFilePath() {

		ProfileService profileService = new ProfileService();
		authenticateEndpoint(profileService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileService.getProfile(properties.getProperty("email1"));
		File file = new File("image1.jpg");
		profile.setPhotoLocation(file.getAbsolutePath());
		boolean result = profileService.updateProfilePhoto(profile);
		assertEquals(false, result);
	}
}
