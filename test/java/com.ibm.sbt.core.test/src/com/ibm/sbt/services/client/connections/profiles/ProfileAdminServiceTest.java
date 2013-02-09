package com.ibm.sbt.services.client.connections.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.util.SBTException;

/**
 * Tests for the java connections Profile API by calling Connections server using configuration in
 * managed-beans
 * 
 * @author Vineet Kanwal
 */
public class ProfileAdminServiceTest extends BaseUnitTest {

	@Ignore
	@Test
	public final void testCreateAndDeleteProfile() {
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = profileAdminService.getProfile("testUser@renovations.com", false);
		profile.set("guid", "testUserD9A04-F2E1-1222-4825-7A700026E92C");
		profile.set("email", "testUser@renovations.com");
		profile.set("uid", "testUser");
		profile.set("distinguishedName", "CN=testUser def,o=renovations");
		profile.set("displayName", "testUser");
		profile.set("givenNames", "testUser");
		profile.set("surname", "testUser");
		profile.set("userState", "active");

		boolean result = profileAdminService.createProfile(profile);
		assertTrue(result);
		profile = profileAdminService.getProfile("testUser@renovations.com", true);
		assertNotNull(profile);
		assertEquals("testUser", profile.getDisplayName());

		result = profileAdminService.deleteProfile(profile);
		assertTrue(result);
		profile = profileAdminService.getProfile("testUser@renovations.com", true);
		assertNull(profile.getDisplayName());
	}

	@Ignore
	@Test
	public final void testCreateProfileForInvalidCredentials() {
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		Profile profile = profileAdminService.getProfile("testUser@renovations.com", false);
		profile.set("guid", "testUserD9A04-F2E1-1222-4825-7A700026E92C");
		profile.set("email", "testUser@renovations.com");
		profile.set("uid", "testUser");
		profile.set("distinguishedName", "CN=testUser def,o=renovations");
		profile.set("displayName", "testUser");
		profile.set("givenNames", "testUser");
		profile.set("surname", "testUser");
		profile.set("userState", "active");

		boolean result = profileAdminService.createProfile(profile);
		assertFalse(result);
	}

	@Ignore
	@Test(expected = SBTException.class)
	public final void testCreateProfileForError() {
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = profileAdminService.getProfile("testUser@renovations.com", false);
		boolean result = profileAdminService.createProfile(profile);
		assertFalse(result);
	}

	@Ignore
	@Test
	public final void testDeleteProfileForNonExistingProfile() {
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = profileAdminService.getProfile("testUser@renovations.com", false);
		boolean result = profileAdminService.deleteProfile(profile);
		assertFalse(result);
	}

}
