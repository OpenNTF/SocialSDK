package com.ibm.javascript.units;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.test.lib.TestEnvironment;

public abstract class AbstractRhinoTest {
	protected Context cx;
	protected ScriptableObject scope;

	private Application application;
	private com.ibm.commons.runtime.Context context;
	protected Properties properties = new Properties();
	protected static String jsTestName = null;
	
	Logger log = Logger.getLogger(BaseRhinoTest.class.getName());

	@Before
	public void setup() throws Exception {
		
		// launchSnippet("TestCommunity.js");
		long i = System.currentTimeMillis();
		cx = ContextFactory.getGlobal().enterContext();
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(Context.VERSION_1_6);
		scope = cx.initStandardObjects();
		// Assumes we have env.rhino.js as a resource on the classpath.

		Assert.assertNotNull(getClass().getResourceAsStream(
				"/com/ibm/javascript/units/env.rhino.js"));
		String envjs = IOUtils.toString(getClass().getResourceAsStream(
				"/com/ibm/javascript/units/env.rhino.js"));
		
		String printFunction = "function print(message) {Packages.java.util.logging.Logger.getAnonymousLogger().info(message); }; ";
		cx.evaluateString(scope, printFunction, "print", 1, null);

		String xpath = IOUtils.toString(getClass().getResourceAsStream(
				"/com/ibm/javascript/units/wgxpath.install.js"));

		cx.evaluateString(scope, xpath, "wgxpath.install.js", 1, null);

		cx.evaluateString(scope, envjs, "env.rhino.js", 1, null);

		// This will load the home page DOM.
		String options = "Envjs.scriptTypes['text/javascript'] = true;";
		cx.evaluateString(scope, options, "opt", 1, null);

		String load = "window.location = 'file:///"
				+ getTestFolder().replace("\\", "/")
				+ "/src/main/resources/com/ibm/javascript/units/base.html' ";

		cx.evaluateString(scope, load, "loadPage", 1, null);

		executeScript(" document.evaluate = undefined; wgxpath.install();", "wgxpath install");

		executeScript(" document.domain = 'sbtintegration.swg.usma.ibm.com' ",
				"crd");
		executeScript(
				"var prefix = 'file:///" + getPrefixFolder().replace("\\", "/")
						+ "';", "setPrefix");
		executeScript(
				"var overload = 'file:///" + getTestFolder().replace("\\", "/")
						+ "';", "setOverload");

		setUpJavaSide();
	
		loadJunitBridges();
		createMockConfig();
		log.info("TEST ENVIRONMENT INITIALISED IN: " + (System.currentTimeMillis() - i));
		// inject a dojo file:///pathtodojo
		// inject the library another file with serialized the servlet
		// servlet url should point to local js file

	}

	abstract protected String getPrefixFolder();

	abstract protected String getTestFolder();
	
	private void createMockConfig() {
		try {
			executeScript(
					this.getClass().getResourceAsStream("/com/ibm/javascript/units/MockLibrary.js"),
					"loadMockedLibrary");
		} catch (IOException e) {
			throw new UnsupportedOperationException(e);
		}

	}

	private void loadJunitBridges() {

		String scriptlet = "var assert = Packages.org.junit.Assert;  "
				+ "var fail = function (message) {  assert.fail(message);  };";

		executeScript(scriptlet, "loadJUnit");

	}

	protected Object executeScript(String script, String mnemonic) {
		long t = System.currentTimeMillis();
		try {
			return cx.evaluateString(scope, script, mnemonic, 1, null);
		} catch (JavaScriptException e) {
			Object obj = e.getValue();
			ScriptableObject so = (ScriptableObject) obj;

			throw e;
		} finally {
			log.info("EXECUTED SCRIPT '" + mnemonic +"' IN " + (System.currentTimeMillis()-t));
		}
	}

	protected Object executeScript(File f) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(f);
		String mnemonic = f.getAbsolutePath();
		return executeScript(fileInputStream, mnemonic);
	}

	private Object executeScript(InputStream fileInputStream, String mnemonic)
			throws IOException {
		String mockLib = IOUtils.toString(fileInputStream);
		if (mockLib.trim().length()==0) {
			log.warning("empty script executed: " +mnemonic);
		}
		return executeScript(mockLib, mnemonic);
	}

	public final void setUpJavaSide() {
		RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = com.ibm.commons.runtime.Context.init(application, null, null);
		TestEnvironment.setRequiresAuthentication(true);
	}

	@After
	public final void tearDownJavaSide() {
		com.ibm.commons.runtime.Context.destroy(context);
		Application.destroy(application);
	}

	public String dumpDom() {
		String dump = "document.body.innerHTML;";
		Object ret = executeScript(dump, "tst");
		
		log.info("data: " + ret.toString());
		return ret.toString();
	}
	
	public String dumpText() {
		String dump = "document.body.textContent;";
		Object ret = executeScript(dump, "dumpText");
		
		log.fine("data: " + ret.toString());
		return ret.toString();
	}

	public String dumpText(String id) {
		String dump = "document.getElementById('" + id + "').textContent;";
		Object ret = executeScript(dump, "dumpTextByID");
		log.fine(ret.toString());
		return ret.toString();
	}

	protected void waitForAllPromisesToReturn() throws TimeoutException {
	}

	public static String getJsTestName() {
		return jsTestName;
	}

}
