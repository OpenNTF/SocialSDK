package com.ibm.sbt.services.client.activitystreams;
/**
 * Activity streams ASApplication class, allows user to choose Application type
 * @author Manish Kataria
 */
public enum ASApplication {
	// Possibile values : @communities,@tags,@people,@status
	
	COMMUNITIES("@communities"),
	TAGS("@tags"),
	PEOPLE("@people"),
	STATUS("@status"),
	ALL("@all"),
	NOAPP("NOAPP");
	
	String applicationType;
	ASApplication(String applicationType){
		this.applicationType = applicationType;
	}
	/**
	 * Wrapper method to return application type
	 * <p>
	 */
	public String getApplicationType(){return applicationType;}
}
