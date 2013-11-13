package com.ibm.sbt.test.controls.vcard;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseVCardTest;

/**
 * @author sberrybyrne
 * @date 20 Mar 2013
 */
public class ProfileVCardEmail extends BaseVCardTest{
	
	@Test
	public void testProfileVCardEmail() {
		assertTrue("Expected to trigger the vcard's appearance by hovering and clicking", checkProfileVCard("Social_Profiles_Controls_ProfileVCardEmail"));
	}
	
}
