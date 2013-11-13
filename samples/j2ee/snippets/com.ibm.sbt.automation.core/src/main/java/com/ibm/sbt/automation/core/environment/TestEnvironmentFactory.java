package com.ibm.sbt.automation.core.environment;

/**
 * @author mkataria
 * @author mwallace
 * 
 * @date Jan 14, 2013
 */
public class TestEnvironmentFactory {
    
    static public String SBT_SAMPLE_WEB = "sbt.sample.web";
    static public String SBT_PLAYGROUND = "sbt.playground";
    static public String ACME_SAMPLE_APP = "acme.sample.app";
        
    static private TestEnvironment sampleEnv = new SbtWebEnvironment();
    static private TestEnvironment playgroundEnv = new PlaygroundEnvironment();
    static private TestEnvironment acmeAppEnv = new AcmeEnvironment();
    
    static private TestEnvironment defaultEnv = sampleEnv;

    /**
     * Return the TestEnvironment instance to use.
     * 
     * @return the TestEnvironment instance to use.
     */
	public static TestEnvironment getEnvironment() {
	    String envName = System.getProperty(TestEnvironment.PROP_ENVIRONMENT);
	    TestEnvironment environment = getEnvironmentByName(envName);
	    if (environment == null) {
	        environment = defaultEnv;
	    }
		return environment;
	}

    /**
     * @param envName
     */
    public static TestEnvironment getEnvironmentByName(String envName) {
        if (SBT_SAMPLE_WEB.equals(envName)) {
            return sampleEnv;
        } else if (SBT_PLAYGROUND.equals(envName)) {
            return playgroundEnv;
        } else if (ACME_SAMPLE_APP.equals(envName)) {
        	return acmeAppEnv;
        }
        return null;
    }
	
    /**
     * Set the default environment to use.
     * 
     * @param envName
     */
	public static void setDefaultEnvironment(String envName) {
	    TestEnvironment environment = getEnvironmentByName(envName);
        if (environment != null) {
            defaultEnv = environment;
        }
	}

}
