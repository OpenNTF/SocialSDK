package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.CommonConstants.AT;

import com.ibm.sbt.services.client.base.NamedUrlPart;

/**
 * 
 * @author Carlos Manias
 *
 */
public enum ProfileParams {
	sourceId("sourceEmail", "sourceKey"), targetId("targetEmail", "targetKey"), userId("email", "userid"); 

	private final String keyParam;
	private final String emailParam;
	
	private ProfileParams(String emailParam, String keyParam){
		this.emailParam = emailParam;
		this.keyParam = keyParam;
	}
	
	public NamedUrlPart get(String id){
		String paramName = isEmail(id)?emailParam:keyParam;
		return new NamedUrlPart(name(), paramName+"="+id);
	}

	public String getParamName(String id){
		return isEmail(id)?emailParam:keyParam;
	}
	
	private static boolean isEmail(String id) {
		return (id == null) ? false : id.contains(AT);
	}
}
