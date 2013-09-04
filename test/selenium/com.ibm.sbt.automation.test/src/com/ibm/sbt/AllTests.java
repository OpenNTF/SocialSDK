package com.ibm.sbt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.ibm.sbt.test.AcmeTestSuite;
import com.ibm.sbt.test.js.SmartCloudTestSuite;

/**
 * @author Lorenzo Boccaccia 
 * @date May 31, 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ CheckinTestSuite.class, SbtTestSuite.class, SbtxTestSuite.class, AcmeTestSuite.class, SmartCloudTestSuite.class })
public class AllTests {

}
