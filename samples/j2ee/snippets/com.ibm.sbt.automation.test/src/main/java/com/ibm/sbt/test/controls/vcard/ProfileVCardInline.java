package com.ibm.sbt.test.controls.vcard;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseVCardTest;

/**
 * @author sberrybyrne
 * @date 20 Mar 2013
 */
public class ProfileVCardInline extends BaseVCardTest{

	@Test
	public void testProfileVCardInline() {
		assertTrue("Expected to find the Inline Profile vCard on the page", checkProfileVCardInline("Social_Profiles_Controls_Profile_VCard_Inline"));
	}
	
}
