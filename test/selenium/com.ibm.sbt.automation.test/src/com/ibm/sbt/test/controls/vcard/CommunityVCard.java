package com.ibm.sbt.test.controls.vcard;

import static org.junit.Assert.*;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseVCardTest;

/**
 * @author sberrybyrne
 * @date 20 Mar 2013
 */
public class CommunityVCard extends BaseVCardTest{
	
	@Test
	public void testCommunityVCard() {
		assertTrue("Expected to find the Community vCard on the page", checkCommunityVCard("Social_Communities_Controls_CommunityVCard"));
	}
	
}
