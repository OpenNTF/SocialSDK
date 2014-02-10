package com.ibm.sbt.test.controls;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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

	
	private BaseGridTestSetup setup ;
	 
	@Before
	public void setup(){
		System.out.println("setting up activty");
		setup = new BaseGridTestSetup();
		setup.createActivity();
	}
	
	@After
    public void cleanup() {
		System.out.println("Deleting activty");
		setup.deleteActivity();
    }
	
}
