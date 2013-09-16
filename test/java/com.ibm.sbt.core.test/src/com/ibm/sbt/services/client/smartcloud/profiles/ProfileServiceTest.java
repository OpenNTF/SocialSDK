package com.ibm.sbt.services.client.smartcloud.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * Tests for the java SmartCloud Profile API by calling SmartCloud server using configuration in managed-beans
 * 
 * @author Vimal Dhupar
 */
public class ProfileServiceTest extends BaseUnitTest {
	public final static String	TEST_USERID	= "20547574";
	public final static String	TEST_GUID	= "964198";
	public final static String	TEST_EMAIL = "FrankAdams@try.lotuslive.com";
	public final static String	TEST_PASSWORD = "Password61";
	
	
	@Test
	public final void testGetProfile() throws SBTServiceException {

		ProfileService profileService = new ProfileService("smartcloud");
		authenticateEndpoint(profileService.getEndpoint(), TEST_EMAIL, TEST_PASSWORD);
		Profile profile = profileService.getProfile(TEST_USERID);
		assertNotNull(profile);
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("Sales Executive IBM Collaboration Software", profile.getAbout());
		assertEquals("Mountain View", profile.getAddress());
		assertEquals("SDK Renovations", profile.getDepartment());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("frankadams@try.lotuslive.com", profile.getEmail());
		assertEquals("456-098-3784", profile.getTelephoneNumber());
		assertNotNull(profile.getProfileUrl());
		assertNotNull(profile.getThumbnailUrl());
		assertEquals("Sales Executive", profile.getJobTitle());
	}
	
	@Test
	public final void testGetContactByGUID() throws SBTServiceException {
		ProfileService profileService = new ProfileService("smartcloud");
		authenticateEndpoint(profileService.getEndpoint(), TEST_EMAIL, TEST_PASSWORD);
		Profile profile = profileService.getContact(TEST_GUID);
		assertNotNull(profile);
		assertNotNull(profile.getDisplayName());
		assertNotNull(profile.getAbout());
		assertNotNull(profile.getDepartment());
	}
	
	@Test
	public final void testGetMyContacts() throws SBTServiceException {
		ProfileService profileService = new ProfileService("smartcloud");
		authenticateEndpoint(profileService.getEndpoint(), TEST_EMAIL, TEST_PASSWORD);
		ProfileList profiles = profileService.getMyContacts();
		assertNotNull(profiles);
		for(Profile profileItr : profiles) { 
			assertNotNull(profileItr.getDisplayName()); 
			assertNotNull(profileItr.getId());
		}
	}
	
	@Test
	public final void testGetMyConnections() throws SBTServiceException {
		ProfileService profileService = new ProfileService("smartcloud");
		authenticateEndpoint(profileService.getEndpoint(), TEST_EMAIL, TEST_PASSWORD);
		ProfileList profiles = profileService.getMyConnections();
		assertNotNull(profiles);
		for(Profile profileItr : profiles) { 
			assertNotNull(profileItr.getDisplayName()); 
			assertNotNull(profileItr.getId());
		}
	}
}
