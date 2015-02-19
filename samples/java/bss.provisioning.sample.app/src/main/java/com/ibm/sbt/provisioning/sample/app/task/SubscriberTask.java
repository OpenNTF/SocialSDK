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
package com.ibm.sbt.provisioning.sample.app.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.WeightManager;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.SetOneTimePassword;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.ActivateSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.AddSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.EntitleSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.GetSubscriber;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * This class IS-A <code>Runnable</code>. It represents a task whose <code>run()</code> method could be potentially executed in its own 
 * separated thread of execution.
 * The <code>run()</code> method permits the state transition of the object by mean of calls to the BSS API .
 * Those calls are encapsulated each in their own method and triggered by mean of invocation of the <code>call()</code>
 * method of the <code>abstract class WeightebBSSCall</code> on instances of the classes belonging to the 
 * package <code>com.ibm.sbt.provisioning.sample.app.weightedBSSCall</code>.
 * */
public class SubscriberTask implements Runnable {

	private static final Logger logger = Logger.getLogger(SubscriberTask.class.getName());
	
	public enum State {
		SUBSCRIBER_NON_EXISTENT,
		SUBSCRIBER_ADDED,
		SUBSCRIBER_ACTIVE,
		SUBSCRIBER_ONE_TIME_PWD_SET,
		SUBSCRIBER_ENTITLED,
		SEAT_ASSIGNED
	}
	
	private JsonJavaObject subscriber ;
	private String subscriberEmail ;
	private String customerId ;
	private String subscriptionId ;
	private State status ;
	private String subscriberId ;
	/**
	 * Each subscriberTask has a key made up by the subscriberEmail, column, subscriptionId
	 * */
	private String taskKey ;
	
	public SubscriberTask( JsonJavaObject subscriber, String customerId, String subscriptionId , State initialStatus ) {
		this.subscriber = subscriber ;
		this.customerId = customerId ;
		this.subscriptionId = subscriptionId ;
		this.status = initialStatus ;
		this.subscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
		this.taskKey = this.subscriberEmail + ":" + subscriptionId ;
	}
	
	/**
	 * Business logic of the task 
	 * <p>
	 * It will guide the subscriber through its state transition, from <code>SUBSCRIBER_NON_EXISTENT</code>
	 *  till <code>SEAT_ASSIGNED</code>, by mean of call to the BSS API.
	 * The logic needed for triggering the HTTP calls to the BSS API is encapsulated in the <code>doCall()</code>
	 *  method implementation of the classes that belong to the package <code>com.ibm.sbt.provisioning.sample.app.weightedBSSCall</code>.
	 *  This logic is triggered by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on the instances of the classes belonging to that package
	 */
	@Override
	public void run() {
		Thread.currentThread().setName("SubscriberTask: "+ this.taskKey );
		boolean success = true ;
		while( success == true ){
			if( this.status == State.SUBSCRIBER_NON_EXISTENT ) {
				this.subscriberId = addSubscriber();
				if( this.subscriberId != null ) {
					this.status = State.SUBSCRIBER_ADDED ;
				}else{
					success = false ; 
				}
			}else if( this.status == State.SUBSCRIBER_ADDED ){
				if( activateSubscriber() ){
					this.status = State.SUBSCRIBER_ACTIVE ;
				}else{
					success = false ;
				}
			}else if( this.status == State.SUBSCRIBER_ACTIVE ){
				if( setOneTimePassword() ){
					this.status = State.SUBSCRIBER_ONE_TIME_PWD_SET ;
				}else{
					success = false ;
				}
			}else if( this.status == State.SUBSCRIBER_ONE_TIME_PWD_SET ){
				if( entitleSubscriber() ){
					this.status = State.SUBSCRIBER_ENTITLED ;
				}else{
					success = false ;
				}
			}else if( this.status == State.SUBSCRIBER_ENTITLED ){
				if( waitForSeatAssignment() ){
					this.status = State.SEAT_ASSIGNED ;
				}else{
					success = false ;
				}
			}else if( this.status == State.SEAT_ASSIGNED ){
				success = false ;
			}
		}
		logger.info("Task execution exiting with status:"+ this.status.name() );
		if( this.status != State.SEAT_ASSIGNED ){
			BSSProvisioning.getSubscribersTasks().add(this);
			logger.info("Re-queuing it..." );
		}
	}
	
	/**
	 * This method will trigger the adding of a subscriber to an organization by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>AddSubscriber</code> class
	 * <p>
	 * @return the BSS subscriber identifier
	 */
	private String addSubscriber(){
		String subscriberId = null ;
		if( this.subscriber != null ){
			((JsonJavaObject)this.subscriber.get("Subscriber")).putString("CustomerId", customerId);
			WeightedBSSCall<String> addSubscriber = new AddSubscriber(this.subscriber);
			logger.info("Adding subscriber...");
			try{
				subscriberId = addSubscriber.call();
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
			}
		}
		return subscriberId ;
	}
	
	/**
	 * This method will trigger the subscriber activation by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>ActivateSubscriber</code> class
	 * <p>
	 * @return <code>true</code> if activated, <code>false</code> otherwise
	 */
	private boolean activateSubscriber(){
		Boolean subscriberActive = false ;
		WeightedBSSCall<Boolean> activateSubscriber = new ActivateSubscriber(this.subscriberId,this.subscriberEmail);
		try{
			logger.info("Activating subscriber...");
			subscriberActive = activateSubscriber.call();
			if( subscriberActive == null ){
				subscriberActive = false ;
			}
		} catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return subscriberActive ;
	}
	
	/**
	 * This method will trigger the subscriber one time password setting by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>SetOneTimePassword</code> class
	 * <p>
	 * @return <code>true</code> if the one time password is set, <code>false</code> otherwise
	 */
	private boolean setOneTimePassword(){
		Boolean oneTimePasswordSet = false ;
		if( this.subscriber != null ){
			WeightedBSSCall<Boolean> setOneTimePassword = new SetOneTimePassword(this.subscriberEmail , "onet1me!");
			try{
				logger.info("Setting one time password ...");
				oneTimePasswordSet = setOneTimePassword.call();
				if( oneTimePasswordSet == null ){
					oneTimePasswordSet = false ;
				}
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
			}
		}
		return oneTimePasswordSet ;
	}
	
	/**
	 * This method will trigger the entitlement of the subscriber to the subscription by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>EntitleSubscriber</code> class
	 * <p>
	 * @return <code>true</code> if the subscriber is entitled, <code>false</code> otherwise
	 */
	private boolean entitleSubscriber(){
		Boolean suscriberEntitled = false ;
		if( this.subscriberId != null && this.subscriptionId != null ){
			logger.info("Entitling subscriber...");
			WeightedBSSCall<Boolean> entitleSubscriber = new EntitleSubscriber(this.subscriberId, this.subscriptionId, this.subscriberEmail);
			try{
				suscriberEntitled = entitleSubscriber.call();
				if( suscriberEntitled == null ){
					suscriberEntitled = false ;
				}
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
			}
		}
		return suscriberEntitled ;
	}
	
	/**
	 * This method will trigger the retrieval of the subscriber by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>GetSubscriber</code> class
	 * <p>.
	 * @return <code>true</code> if a seat from the subscription has been assigned to the subscriber, <code>false</code> otherwise
	 */
	private boolean waitForSeatAssignment(){
		boolean seatAssigned = false ;
		if( this.subscriberId != null ){
			// SUBSCRIBER RETRIEVAL
			String suscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
			logger.info("Subscriber retrieval by email : " + suscriberEmail );
			WeightedBSSCall<JsonEntity> getSubscriber = new GetSubscriber( suscriberEmail , null );
			JsonEntity subscriberEntity = null ;
			try {
				subscriberEntity = getSubscriber.call();
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
			}
			if( subscriberEntity != null ){
				List<Object> seatSet = subscriberEntity.getJsonObject().getAsList("SeatSet");
	    		JsonJavaObject seatJson = findSeat(seatSet, subscriptionId);
	    		if (seatJson != null) {
	        		String seatState = seatJson.getAsString("SeatState");
	        		if(!seatState.equalsIgnoreCase("ASSIGNED")){
	        			logger.info("seat not assigned !!!");
	        		}else if(seatState.equalsIgnoreCase("ASSIGNED")){
	        			BSSProvisioning.getStateTransitionReport().get(this.subscriberEmail)[5] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
	        			logger.info("seat assigned !!!");
	        			BSSProvisioning.incrementSubscribersProvisioned();
	        			if( BSSProvisioning.getSubscribersQuantity() == BSSProvisioning.getSubscribersProvisioned() ){
	        				logger.finest("ALL PROVISIONED !!!");
	        				BSSProvisioning.generateStateTransitionReport();
	        				BSSProvisioning.generateSubscriberWeightReport();
	        				BSSProvisioning.generateCallsCounterReport();
	        				WeightManager.getInstance().shutdown();
	        			}
	        			seatAssigned = true ;
	        		}
	        	}
			}else{
				logger.severe("the subscriber has not been retrieved !!!");
			}
		}
		return seatAssigned ;
	}
	
	/**
	 * This method will loop through a list of object representing the seats a subscriber owns, looking for the
	 * one associated with the subscription specified as second argument
	 * <p>.
	 * @param  seatSet  a list of object representing the seats a subscriber owns<br>
	 * @param  subscriptionId   the BSS subscription identifier<br>
	 * @return a <code>JsonJavaObject</code> representing the seat associated with the subscription specified as second argument
	 */
	private JsonJavaObject findSeat(List<Object> seatSet, String subscriptionId) {
    	for (Object seat : seatSet) {
    		String nextSubscriptionId = "" + (long)((JsonJavaObject)seat).getAsDouble("SubscriptionId");
    		if (subscriptionId.equals(nextSubscriptionId)) {
    			return (JsonJavaObject)seat;
    		}
    	}
    	return null;
    }
	
	// GETTER and SETTER
	public JsonJavaObject getSubscriber() {
		return subscriber;
	}
	
	public String getSubscriberEmail() {
		return subscriberEmail;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public String getSubscriptionId() {
		return subscriptionId;
	}
	
	public State getStatus() {
		return status;
	}
	
	public String getSubscriberId() {
		return subscriberId;
	}
	
	public String getSubscriberTaskKey() {
		return taskKey;
	}
}
