package com.ibm.sbt.services.client.connections.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.profiles.ProfileAdminService;
import com.ibm.sbt.services.client.connections.profiles.Profile;

/**
 * Tests for the java connections Profile API by calling Connections server using configuration in
 * managed-beans
 * 
 * @author Swati Singh
 * @author Vineet Kanwal
 */
public class ProfileAdminServiceTest extends BaseUnitTest {

	@Test
	public final void testCreateAndDeleteProfile() throws Exception{
//		ProfilesDataGenerator pdc = new ProfilesDataGenerator()
//		pdc.createProfile();
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		Profile profile = profileAdminService.getProfile("testUser@renovations.com");
		
		profile.setAsString("guid", "testUser"+System.currentTimeMillis());
		profile.setAsString("email", "testUser"+System.currentTimeMillis()+"@renovations.com");
		profile.setAsString("uid", "testUser"+System.currentTimeMillis());
		profile.setAsString("distinguishedName", "CN=testUser def,o=renovations");
		profile.setAsString("displayName", "testUser"+System.currentTimeMillis());
		profile.setAsString("givenNames", "testUser");
		profile.setAsString("surname", "testUser");
		profile.setAsString("userState", "active");
		 
		profileAdminService.createProfile(profile);
		profile = profileAdminService.getProfile("testUser@renovations.com");
		assertNotNull(profile.getName());

	}


	@Ignore
	@Test
	public final void testDeleteProfile() throws Exception {
		ProfileAdminService profileAdminService = new ProfileAdminService();
		authenticateEndpoint(profileAdminService.getEndpoint(), properties.getProperty("adminUser"),
				properties.getProperty("passwordAdmin"));
		ProfileService profileService = new ProfileService();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", "testUser");
		ProfileList profileEntries = profileAdminService.searchProfiles(parameters);
		Profile profile = profileEntries.iterator().next();
		profileAdminService.deleteProfile(profile.getUserid());
		profile = profileAdminService.getProfile("testUser@renovations.com");
		assertNull(profile.getName());
	}

}
