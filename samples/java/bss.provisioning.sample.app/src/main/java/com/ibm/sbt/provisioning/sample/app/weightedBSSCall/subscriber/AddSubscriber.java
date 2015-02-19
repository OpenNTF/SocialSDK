/*
 * © Copyright IBM Corp. 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.services.Subscriber;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;

public class AddSubscriber extends WeightedBSSCall<String>{

	private static final Logger logger = Logger.getLogger(AddSubscriber.class.getName());
	private JsonJavaObject subscriberJson;
	
	public AddSubscriber( JsonJavaObject subscriberJson ){
		this.subscriberJson= subscriberJson ;
	}
	/**
	 * Method used for triggering the proper HTTP call for adding a subscriber to an organization ,
	 *  towards the BSS endpoint designed for managing subscribers
	 */
	@Override
	protected String doCall() throws Exception {
		String email = this.subscriberJson.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
		String subscriberId = null ;
		try {     
			logger.fine("triggering call : "+ this.getKey() );
			JsonJavaObject subscriber = Subscriber.getInstance().getService().addSubscriber(this.subscriberJson);
			subscriberId = String.valueOf(subscriber.getAsLong("Long"));
			BSSProvisioning.getStateTransitionReport().get(email)[1] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
			BSSProvisioning.getSubscriberWeightReport().get(email)[0]++ ;
			logger.info("subscriber "+email+" added !!! his id is "+subscriberId);
    	} catch (BssException be) {
    		logger.severe(" BssException : " + be.getMessage());
    	} 
    	return subscriberId;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.RES_SUBSCRIBER.getEndpointString()+":POST";
	}

}
