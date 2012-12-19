package com.ibm.sbt.services.client.smartcloud.profiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * Tests for the java SmartCloud Profile API by calling SmartCloud server using configuration in faces-config
 * 
 * @author Vimal Dhupar
 */
public class ProfileServiceTest extends BaseUnitTest {

	@Test
	public final void testGetProfile() throws SBTServiceException {
		
		ProfileService profileService = new ProfileService("smartcloud");
		authenticateEndpoint(profileService.getEndpoint(), "FrankAdams@try.lotuslive.com", "***REMOVED***");
		Profile profile = profileService.getProfile();
		assertNotNull(profile);
		assertNotNull(profile.getData());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("Sales Executive IBM Collaboration Software", profile.getAboutMe());
		assertEquals("Mountain View", profile.getAddress());
		assertEquals("SDK Renovations", profile.getDepartment());
		assertEquals("Frank Adams", profile.getDisplayName());
		assertEquals("frankadams@try.lotuslive.com", profile.getEmail());
		assertEquals("456-098-3784", profile.getPhoneNumber());
		assertNotNull(profile.getProfileUrl());
		assertNotNull(profile.getThumbnailUrl());
		assertEquals("Sales Executive", profile.getTitle());
		assertEquals("na.collabserv.com:user:20547574", profile.getUniqueId());
	}
}
