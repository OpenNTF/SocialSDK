package com.ibm.sbt.js;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.ibm.javascript.units.BaseRhinoTest;


public class URLBuilderTest  extends BaseRhinoTest {

	@Test
	public void testBuilder() throws Exception {
		
		String script;
		script = IOUtils.toString(getClass().getResourceAsStream(
				"TestBuilder.js"));
	
		executeScript(script, "test");
		
	}
}
