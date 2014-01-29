package com.ibm.javascript.units;

import java.io.File;

public class BaseRhinoTest extends AbstractRhinoTest {

	protected String getPrefixFolder() {
		File project = new File(System.getProperty("user.dir"),"..");
		File sbt = new File(project, "com.ibm.sbt.web");
		sbt = new File(sbt, "src");
		sbt = new File(sbt, "main");
		sbt = new File(sbt, "webapp");
		sbt = new File(sbt, "js");
		sbt = new File(sbt, "sdk");
		return sbt.getAbsolutePath();
	}

	protected String getTestFolder() {
		String currentDir = System.getProperty("user.dir");
		return currentDir;
	}
}
