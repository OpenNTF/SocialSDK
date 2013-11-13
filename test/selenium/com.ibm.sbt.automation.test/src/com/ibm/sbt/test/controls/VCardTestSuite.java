package com.ibm.sbt.test.controls;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.controls.vcard.CommunityVCard;
import com.ibm.sbt.test.controls.vcard.ProfileVCard;
import com.ibm.sbt.test.controls.vcard.ProfileVCardEmail;
import com.ibm.sbt.test.controls.vcard.ProfileVCardInline;
import com.ibm.sbt.test.controls.vcard.ProfileVCards;

/**
 * @author sberrybyrne
 * @date 20 Mar 2013
 * 
 * Need to further extend classes to test more than just a controls existence.
 */
@RunWith(Suite.class)
@SuiteClasses({ CommunityVCard.class, ProfileVCard.class, ProfileVCardEmail.class, ProfileVCardInline.class, ProfileVCards.class })
public class VCardTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
