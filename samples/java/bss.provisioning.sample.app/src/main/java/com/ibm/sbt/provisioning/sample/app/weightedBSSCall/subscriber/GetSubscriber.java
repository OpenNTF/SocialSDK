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

import java.util.logging.Logger;

import com.ibm.sbt.provisioning.sample.app.services.Subscriber;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

public class GetSubscriber extends WeightedBSSCall<JsonEntity>{
	
	private static final Logger logger = Logger.getLogger(GetSubscriber.class.getName());
	private String email;
	private String id;
	
	public  GetSubscriber( String email, String id ){
		this.email = email ;
		this.id =id ;
	}
	/**
	 * Method used for triggering the proper HTTP call for retrieving a subscriber,
	 *  towards the BSS endpoint designed for managing subscribers
	 */
	@Override
	protected JsonEntity doCall() throws Exception {
		JsonEntity subscriber = null ;
		EntityList<JsonEntity> subscriberEntityList = null ;
		logger.fine("triggering call : "+ this.getKey() );
		if( this.email != null && !this.email.equals("") ){
			subscriberEntityList = Subscriber.getInstance().getService().getSubscribersByEmail(this.email);
			int[] subscriberWeightReport = BSSProvisioning.getSubscriberWeightReport().get(this.email);
			if( subscriberWeightReport!= null ){
				subscriberWeightReport[2]++ ;
			}
		}else if( this.id != null && !this.id.equals("") ){
			subscriberEntityList = Subscriber.getInstance().getService().getSubscribersById(this.id);
		}
		if( subscriberEntityList != null ){
			subscriber = subscriberEntityList.get(0);
		}
    	return subscriber;
	}

	@Override
	public String getKey() {
		return BSSEndpoints.RES_SUBSCRIBER.getEndpointString()+":GET";
	}
}
