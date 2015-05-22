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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscription;

import java.util.logging.Logger;
import com.ibm.sbt.provisioning.sample.app.model.Rest;
import com.ibm.sbt.provisioning.sample.app.services.Subscription;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.base.JsonEntity;

public class GetSubscription extends WeightedBSSCall<JsonEntity>{
	
	private static final Logger logger = Logger.getLogger(GetSubscription.class.getName());
	private String subscriptionId;
	
	public  GetSubscription( String subscriptionId ){
		this.subscriptionId = subscriptionId ;
	}
	/**
	 * Method used for triggering the proper HTTP call for retrieving an organization's subscription,
	 *  towards the BSS endpoint designed for managing subscriptions
	 */
	@Override
	protected JsonEntity doCall() throws Exception {
		JsonEntity subscription = null ;    
    logger.fine("triggering call : " + this.getUrl() + " " + getMethod());
		subscription = Subscription.getInstance().getService().getSubscriptionById(this.subscriptionId);
		logger.info("retrieved subscription with id "+ this.subscriptionId );
    	return subscription;
	}

	@Override
  public String getUrl(){
    return BSSEndpoints.RES_SUBSCRIPTION.getEndpointString();
	}
  @Override
  public Rest getMethod(){
    return Rest.GET;
}
}
