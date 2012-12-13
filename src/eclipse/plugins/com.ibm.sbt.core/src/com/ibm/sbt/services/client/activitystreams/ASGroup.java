package com.ibm.sbt.services.client.activitystreams;
/**
 * Activity streams ASGroup class, allows user to choose Group type
 * @author Manish Kataria
 */
public enum ASGroup {
	
	//// Possible values : @all,@following,@friends,@self
	
	ALL("@all"),
	FOLLOWING("@following"),
	FRIENDS("@friends"),
	SELF("@self"),
	INVOLVED("@involved"),
	SAVED("@saved"),
	ACTION("@actions"),
	RESPONSES("@responses");
	String groupType;
	private ASGroup(String groupType) {
		this.groupType = groupType;
	}
	
	public String getGroupType(){return groupType;}

}
