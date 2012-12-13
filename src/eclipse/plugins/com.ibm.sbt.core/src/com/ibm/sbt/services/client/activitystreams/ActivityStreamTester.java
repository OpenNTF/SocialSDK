package com.ibm.sbt.services.client.activitystreams;

import com.ibm.sbt.services.client.SBTServiceException;

/**
 * ActivityStreamTester Sample to test Activity Stream.
 * 
 * @author Manish Kataria
 */
public class ActivityStreamTester {

	public static void main(String[] args) throws SBTServiceException {
		ActivityStreamService _service = new ActivityStreamService(null);
		_service.getActivityStream();

	}

}
