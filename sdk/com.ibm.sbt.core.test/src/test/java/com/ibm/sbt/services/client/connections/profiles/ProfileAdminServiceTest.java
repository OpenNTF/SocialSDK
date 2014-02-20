package com.ibm.sbt.services.client.connections.profiles;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;

/**
 * Tests for the java connections Profile API by calling Connections server using configuration in
 * managed-beans
 * 
 * @author Swati Singh
 * @author Vineet Kanwal
 */
public class ProfileAdminServiceTest extends BaseUnitTest {

	protected ProfileAdminService profileAdminService;

	@Before
	public void initProfileServiceTest() {
		if (profileAdminService==null){
			profileAdminService = new ProfileAdminService();
		}
	}
	
	@Test
	public final void testCreateAndDeleteProfile() throws Exception{
		Profile profile = profileAdminService.getProfile("testUser@renovations.com");
		
		profile.setAsString("guid", "testUser"+System.currentTimeMillis());
		String email = "testUser"+System.currentTimeMillis()+"@renovations.com";
		profile.setAsString("email", email);
		profile.setAsString("uid", "testUser"+System.currentTimeMillis());
		profile.setAsString("distinguishedName", "CN=testUser def,o=renovations");
		profile.setAsString("displayName", "testUser"+System.currentTimeMillis());
		profile.setAsString("givenNames", "testUser");
		profile.setAsString("surname", "testUser");
		profile.setAsString("userState", "active");
		 
		profileAdminService.createProfile(profile);
		profile = profileAdminService.getProfile(email);
		assertNotNull(profile.getName());
		profileAdminService.deleteProfile(email);
		profile = profileAdminService.getProfile(email);
		assertNull(profile.getName());
	}

}
