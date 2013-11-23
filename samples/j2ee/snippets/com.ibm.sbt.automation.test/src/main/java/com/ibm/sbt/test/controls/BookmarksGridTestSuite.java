package com.ibm.sbt.test.controls;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.test.BaseGridTestSetup;
import com.ibm.sbt.test.controls.grid.actvities.BootstrapMyActivities;
import com.ibm.sbt.test.controls.grid.actvities.MyActivities;
import com.ibm.sbt.test.controls.grid.bookmarks.AllBookmarks;
import com.ibm.sbt.test.controls.grid.bookmarks.BootstrapAllBookmarks;
import com.ibm.sbt.test.controls.grid.bookmarks.BootstrapMyBookmarks;
import com.ibm.sbt.test.controls.grid.bookmarks.BootstrapPublicBookmarks;
import com.ibm.sbt.test.controls.grid.bookmarks.MyBookmarks;
import com.ibm.sbt.test.controls.grid.bookmarks.PublicBookmarks;


@RunWith(Suite.class)
@SuiteClasses({AllBookmarks.class,BootstrapAllBookmarks.class,BootstrapMyBookmarks.class,
	BootstrapPublicBookmarks.class,MyBookmarks.class,PublicBookmarks.class})
public class BookmarksGridTestSuite {

	
	private static BaseGridTestSetup setup ;
	 
	@BeforeClass
	public static void setup(){
	
		setup = new BaseGridTestSetup();
		setup.createBookmark();
		
	}
	
	@AfterClass
    public static void cleanup() {
		
    }
}
