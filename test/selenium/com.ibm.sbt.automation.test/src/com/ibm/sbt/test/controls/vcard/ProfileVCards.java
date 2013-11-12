package com.ibm.sbt.test.controls.vcard;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseVCardTest;

/**
 * @author sberrybyrne
 * @date 20 Mar 2013
 */
public class ProfileVCards extends BaseVCardTest{

	@Test
	public void testProfileVCards() {
		assertTrue("Expected to find the card attachpoints and trigger the appearance of the vcards on the page", super.checkProfileVCards("Social_Profiles_Controls_Profile_VCards"));
	}
	
}
