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
package com.ibm.sbt.provisioning.sample.app.services;

import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
/**
 * This class represents a singleton that encapsulate an instance of
 * the class <code>com.ibm.sbt.services.client.smartcloud.bss.GRTESubscriberManagementService</code> 
 * */


//TODO Temp class Subscriber can be used when rest of code moved to SocialSDK
public class GRTESubscriber {

	private static GRTESubscriber instance = null;
    private GRTESubscriberManagementService service;
    
    private GRTESubscriber() {
    	this.service = new GRTESubscriberManagementService(BSSProvisioning.getBasicEndpoint());
    }
 
    public static synchronized GRTESubscriber getInstance() {
        if (instance == null) 
        	instance = new GRTESubscriber();
        return instance;
    }
	
    public GRTESubscriberManagementService getService() {
		return service;
	}
}