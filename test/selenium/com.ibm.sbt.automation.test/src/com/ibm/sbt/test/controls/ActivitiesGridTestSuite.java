package com.ibm.sbt.test.controls;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.test.BaseGridTestSetup;
import com.ibm.sbt.test.controls.grid.actvities.BootstrapMyActivities;
import com.ibm.sbt.test.controls.grid.actvities.MyActivities;

@RunWith(Suite.class)
@SuiteClasses({MyActivities.class,BootstrapMyActivities.class})
public class ActivitiesGridTestSuite {

	
	private static BaseGridTestSetup setup ;
	 
	@BeforeClass
	public static void setup(){
		System.out.println("setting up activty");
		setup = new BaseGridTestSetup();
		setup.createActivity();
	}
	
	@AfterClass
    public static void cleanup() {
		System.out.println("Deleting activty");
		setup.deleteActivity();
    }
	
}
