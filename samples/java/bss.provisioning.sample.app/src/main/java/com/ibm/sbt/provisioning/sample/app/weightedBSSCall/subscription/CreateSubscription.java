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
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency;
import com.ibm.sbt.services.client.smartcloud.bss.OrderJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriptionManagementService;

public class CreateSubscription extends WeightedBSSCall<String> {

	private static final Logger logger = Logger.getLogger(CreateSubscription.class.getName());
	private String customerId ;
	private int duration ;
	private String partNumber ;
	private int quantity ;
	
	public CreateSubscription( String customerId, int duration, String partNumber, int quantity ){
		this.customerId = customerId ;
		this.duration = duration ;
		this.partNumber = partNumber ;
		this.quantity = quantity ;
	}

	/**
	 * Method used for triggering the proper HTTP call for creating an organization's subscription,
	 *  towards the BSS endpoint designed for managing subscriptions
	 */
	@Override
	protected String doCall() throws Exception {
		String subscriptionId = null ;
		OrderJsonBuilder order = new OrderJsonBuilder();
    order
      .setCustomerId(this.customerId)
			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
		     .setDurationLength(this.duration)
		     .setPartNumber(this.partNumber)
		     .setPartQuantity(this.quantity)
		     .setBillingFrequency(BillingFrequency.ARC);
    logger.fine("triggering call : " + this.getUrl() + " " + getMethod());
		EntityList<JsonEntity> subscriptionList = Subscription.getInstance().getService().createSubscription(order);
		if( subscriptionList != null && subscriptionList.get(0) != null ){
			subscriptionId = String.valueOf(subscriptionList.get(0).getAsLong("SubscriptionId"));
			logger.info("created subscription with id : "+ subscriptionId );
		}
		return subscriptionId;
	}

	@Override
  public String getUrl(){
    return BSSEndpoints.RES_SUBSCRIPTION.getEndpointString();
	}
  @Override
  public Rest getMethod(){
    return Rest.POST;
  }
}
