package com.ibm.sbt.services.client.activitystreams;
/**
 * Activity streams ASApplication class, allows user to choose Application type
 * @author Manish Kataria
 */
public enum ASApplication {
	// Possibile values : @communities,@tags,@people,@status,@notesforme,@notesfromme,@responses
	
	COMMUNITIES("@communities"),
	TAGS("@tags"),
	PEOPLE("@people"),
	STATUS("@status"),
	NOTESFORME("@notesforme"),
	NOTESFROMME("@notesfromme"),
	RESPONSES("@responses"),
	ALL("@all"),
	NOAPP("NOAPP");
	
	String applicationType;
	ASApplication(String applicationType){
		this.applicationType = applicationType;
	}
	
	public String getApplicationType(){return applicationType;}
}
