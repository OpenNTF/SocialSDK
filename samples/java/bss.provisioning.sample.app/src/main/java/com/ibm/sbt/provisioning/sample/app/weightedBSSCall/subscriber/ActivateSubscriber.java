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
import com.ibm.sbt.provisioning.sample.app.model.Rest;
import com.ibm.sbt.provisioning.sample.app.services.Subscriber;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;

public class ActivateSubscriber extends WeightedBSSCall<Boolean>{
	
	private static final Logger logger = Logger.getLogger(ActivateSubscriber.class.getName());
	private String id;
	private String email ;
	
	public  ActivateSubscriber( String id  ){
		this(id, "") ;
	}
	
	public  ActivateSubscriber( String id , String email ){
		this.id = id ;
		this.email = email ;
	}
	/**
	 * Method used for triggering the proper HTTP call for activating an organization subscriber,
	 *  towards the BSS endpoint designed for managing subscribers
	 */
	@Override
	protected Boolean doCall() throws Exception {
		boolean subscriberActive = false ;
    logger.fine("triggering call : " + this.getUrl() + " " + getMethod());
		Subscriber.getInstance().getService().activateSubscriber(id);
		subscriberActive = true ;
		if(this.email != null){
			BSSProvisioning.getStateTransitionReport().get(this.email)[2] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
			BSSProvisioning.getSubscriberWeightReport().get(email)[0]++ ;
		}
		logger.info("subscriber with id "+this.id+" is now active !!!");
		return subscriberActive ;
	}

	@Override
  public String getUrl(){
    return BSSEndpoints.RES_SUBSCRIBER.getEndpointString();
	}

  @Override
  public Rest getMethod(){
    return Rest.POST;
  }
}
