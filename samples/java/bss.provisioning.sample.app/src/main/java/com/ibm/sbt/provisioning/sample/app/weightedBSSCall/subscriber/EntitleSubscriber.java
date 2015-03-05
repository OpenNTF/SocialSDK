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

import com.ibm.sbt.provisioning.sample.app.services.Subscriber;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;

public class EntitleSubscriber extends WeightedBSSCall<Boolean>{

	private static final Logger logger = Logger.getLogger(EntitleSubscriber.class.getName());
	private String subscriberId ;
	private String subscriptionId ;
	private String email ;
	
	public EntitleSubscriber( String subscriberId, String subscriptionId, String email ){
		this.subscriberId = subscriberId ;
		this.subscriptionId = subscriptionId ;
		this.email = email ;
	}
	/**
	 * Method used for triggering the proper HTTP call for entitling a subscriber to a subscription ,
	 *  towards the BSS endpoint designed for managing subscribers
	 */
	@Override
	protected Boolean doCall() throws Exception {
		boolean subscriberEntitled = false ;
		try {
			logger.fine("triggering call : "+ this.getKey() );
			Subscriber.getInstance().getService().entitleSubscriber(subscriberId, subscriptionId, true);
			subscriberEntitled = true ;
			BSSProvisioning.getStateTransitionReport().get(this.email)[4] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
			BSSProvisioning.getSubscriberWeightReport().get(this.email)[0]++ ;
			logger.info("subscriber with id "+subscriberId+" entitled to subscription with id "+subscriptionId);
		} catch (BssException be) {
    		logger.severe(" BssException : " + be.getMessage());
    	}
		return subscriberEntitled ;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.RES_SUBSCRIBER.getEndpointString()+":POST";
	}

}
