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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.provisioning.sample.app.WeightManager;
import com.ibm.sbt.provisioning.sample.app.model.SubscriptionEntitlement;
import com.ibm.sbt.provisioning.sample.app.model.SubscriptionEntitlement.NotesType;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.ChangePassword;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.SetOneTimePassword;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.ActivateSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.AddSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.AddSubscriberSuppressEmail;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.EntitleSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.GetSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.UpdateNotesSubscriber;
import com.ibm.sbt.services.client.ClientServicesException;
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
		SUBSCRIBER_PASSWORD_CHANGED, 
		SUBSCRIBER_UPDATED, 
		SUBSCRIBER_ENTITLED,
		SUBSCRIBE_FAILED,
		SEAT_ASSIGNED
	}
	
	private JsonJavaObject subscriber ;
	private String subscriberEmail ;
	private String customerId ;
  	private List<SubscriptionEntitlement> entitlements;
	private State status ;
	private String subscriberId ;
  	private String oneTimePassword;
  	private String newPassword;
  	private int attemptCount = 0;
  	private int attempts;
  	private boolean attemptsFlag = false;
  	private boolean suppressEmail = false;

	/**
	 * Each subscriberTask has a key made up by the subscriberEmail, column, subscriptionId
	 * */
	private String taskKey ;

	public SubscriberTask(JsonJavaObject subscriber, String customerId, List<SubscriptionEntitlement> entitlements, 
			State initialStatus, String newPassword){
		this.subscriber = subscriber;
		this.customerId = customerId;
		this.entitlements = entitlements;
		this.status = initialStatus;
		this.oneTimePassword = "onet1me!";
		this.newPassword = newPassword;
		this.subscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
		this.taskKey = this.subscriberEmail;
	}
	
	public SubscriberTask(JsonJavaObject subscriber, String customerId, List<SubscriptionEntitlement> entitlements, 
			State initialStatus, String newPassword, int attempts){
		this.subscriber = subscriber;
		this.customerId = customerId;
		this.entitlements = entitlements;
		this.status = initialStatus;
		this.oneTimePassword = "onet1me!";
		this.newPassword = newPassword;
		this.subscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
		this.taskKey = this.subscriberEmail;
		this.attempts = attempts;
		this.attemptsFlag = true;
	}

	public SubscriberTask(JsonJavaObject subscriber, String customerId, String subscriptionId, State initialStatus) {
		this.subscriber = subscriber ;
		this.customerId = customerId ;
		this.status = initialStatus ;
    	this.oneTimePassword = "onet1me!";
		this.subscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
    	this.taskKey = this.subscriberEmail;
	}
	
	public SubscriberTask(JsonJavaObject subscriber, String customerId, String subscriptionId, State initialStatus, int attempts) {
		this.subscriber = subscriber ;
		this.customerId = customerId ;
		this.status = initialStatus ;
    	this.oneTimePassword = "onet1me!";
		this.subscriberEmail = this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
    	this.taskKey = this.subscriberEmail;
    	this.attempts = attempts;
    	this.attemptsFlag = true;
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
			if (this.status == State.SUBSCRIBER_NON_EXISTENT) {
				if(suppressEmail){
					this.subscriberId = addSubscriberSuppressEmail();
				}else{
					this.subscriberId = addSubscriber();
				}
				if( this.subscriberId != null ) {
					this.status = State.SUBSCRIBER_ADDED ;
				}else{
					success = false ; 
				}
			} else if (this.status == State.SUBSCRIBER_ADDED) {
				if(updateSubscriber()){
					this.status = State.SUBSCRIBER_UPDATED;
				}else{
					success = false;
				}
			} else if (this.status == State.SUBSCRIBER_UPDATED) {
		    	  if( activateSubscriber() ){
		    		  this.status = State.SUBSCRIBER_ACTIVE ;
		    	  }else{
		    		  success = false ;
		    	  }
			} else if ( this.status == State.SUBSCRIBER_ACTIVE ) {
		    	  if (setOneTimePassword()){
		    		  this.status = State.SUBSCRIBER_ONE_TIME_PWD_SET ;
		    	  }else{
		    		  success = false ;
		    	  }
			} else if(this.newPassword == null){
				// If the new password is null, we don't change it, the user will change it on first login
        		// If the new password is not null, we change it
		        this.status = State.SUBSCRIBER_PASSWORD_CHANGED;
			} else if( this.status == State.SUBSCRIBER_ONE_TIME_PWD_SET ){
		        if(changePassword()){
		          this.status = State.SUBSCRIBER_PASSWORD_CHANGED;
		        }else{
		          success = false;
		        }
		    } else if(this.status == State.SUBSCRIBER_PASSWORD_CHANGED){
		    	  if( entitleSubscriber() ){
		    		  this.status = State.SUBSCRIBER_ENTITLED ;
		    	  }else{
		    		  success = false ;
		    	  }
		    } else if( this.status == State.SUBSCRIBER_ENTITLED ){
		    	  if( waitForSeatAssignment() ){
		    		  this.status = State.SEAT_ASSIGNED ;
		    	  }else{
		    		  success = false ;
		    	  }
		    } else if( this.status == State.SEAT_ASSIGNED){
		    	  success = false ;
		    	  attemptsFlag = false;
		    } else if( this.status == State.SUBSCRIBE_FAILED){
		    	  success = false ;
		    	  attemptsFlag = false;
		    	  logger.info("Checking for task completion...");
		    	  logger.info("Completed: "+checkForFinish());  
		    }
		}
		if(attemptsFlag){
			if(success==false){
				attemptCount++;
				logger.info("Attempts = " + attemptCount+"/"+attempts);
				logger.info("Waiting 30 seconds before retrying task...");
				try {
					Thread.sleep(1000*30);
				} catch (InterruptedException e) {
					logger.severe(e.getMessage());
				}
			}
			if(attemptCount>=attempts&&this.status != State.SEAT_ASSIGNED){
				this.status = State.SUBSCRIBE_FAILED;
				BSSProvisioning.failedSubscriptions.add(this);
				BSSProvisioning.incrementSubscribersFailed();
				logger.info("Checking for task completion...");
				logger.info("Completed: "+checkForFinish());  
			}
		}
		logger.info("Task execution exiting with status: "+ this.status.name() );
		if( this.status == State.SEAT_ASSIGNED ){
			logger.info("Task exited." );
		}else{
			BSSProvisioning.getSubscribersTasks().add(this);
			logger.info("Re-queuing it..." );
		}
	}

	@SuppressWarnings("unchecked")
	private String identifySubscriberState(){
		WeightedBSSCall<JsonEntity> getSubscriber = new GetSubscriber(subscriberEmail, null);
		JsonEntity subscriberEntity = null;
		try{
			subscriberEntity = getSubscriber.call();
			String subscriberState = subscriberEntity.getAsString("SubscriberState");
			if(subscriberState.equalsIgnoreCase("PENDING")){
				this.status = State.SUBSCRIBER_ADDED;
			}else if(subscriberState.equalsIgnoreCase("ACTIVE")){
				ArrayList<Object> seatSetList = (ArrayList<Object>) subscriberEntity.getAsObject("SeatSet");
				if(seatSetList.size() > 0){
					for(Object seatObject:seatSetList){
						JsonJavaObject seatSet = ((JsonJavaObject) seatObject);
						String seatState = seatSet.getAsString("SeatState");
						if(seatState.equalsIgnoreCase("ENTITLE_PENDING")){
							this.status = State.SUBSCRIBER_ENTITLED;
						}else if(seatState.equalsIgnoreCase("ASSIGNED")){
							this.status = State.SEAT_ASSIGNED;
						}
					}
				}
			}
			this.subscriberId = subscriberEntity.getAsLong("Id").toString();
		}catch(Exception e){
			logger.severe(e.getClass() + " : " + e.getMessage());
		}
		return this.subscriberId;
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
	    		String responseBody = ((ClientServicesException) e.getCause()).getResponseBody();
	    		if(emailAlreadyExists(responseBody)){
	    			subscriberId = identifySubscriberState();
	    		}
			}
		}
		return subscriberId ;
	}
	
	/**
	 * This method will trigger the adding of a subscriber w/ suppress email to an organization by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>AddSubscriber</code> class
	 * <p>
	 * @return the BSS subscriber identifier
	 */
	private String addSubscriberSuppressEmail(){
		String subscriberId = null ;
		if( this.subscriber != null ){
			((JsonJavaObject)this.subscriber.get("Subscriber")).putString("CustomerId", customerId);
			WeightedBSSCall<String> addSubscriberSuppressEmail = new AddSubscriberSuppressEmail(this.subscriber);
			logger.info("Adding subscriber with suppressed email...");
			try{
				subscriberId = addSubscriberSuppressEmail.call();
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
	    		String responseBody = ((ClientServicesException) e.getCause()).getResponseBody();
	    		if(emailAlreadyExists(responseBody)){
	    			subscriberId = identifySubscriberState();
	    		}
			}
		}
		return subscriberId ;
	}
	
	private boolean emailAlreadyExists(String errorResponse){
		try{
			JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(JsonJavaFactory.instanceEx2, errorResponse);
			JsonJavaObject bssResponse = json.getAsObject("BSSResponse");
			String responseMessage = bssResponse.getAsString("ResponseMessage");
			return responseMessage.equalsIgnoreCase("The email address already exists");
		}catch(JsonException e){
			logger.severe("Error parsing JSON");
		}
		return false;
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
			WeightedBSSCall<Boolean> setOneTimePassword = new SetOneTimePassword(this.subscriberEmail , this.oneTimePassword);
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
	 * This method will trigger the subscriber password changing by mean of invocation of the
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method on an
	 * instance of the
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.ChangePassword} class
	 * <p>
	 * 
	 * @param email email used as user credential of the subscriber<br>
	 * @param oneTimePassword string representing the one time password<br>
	 * @param newPassword string representing the new password<br>
	 * @return <code>true</code> if the password is changed, <code>false</code> otherwise
	 */
	private boolean changePassword(){
		Boolean passwordChanged = false;
		logger.info("Subscriber changing password ...");
		WeightedBSSCall<Boolean> changePassword = new ChangePassword(this.subscriberEmail, this.oneTimePassword, this.newPassword);
		try {
			passwordChanged = changePassword.call();
		} catch(Exception e){
			logger.severe(e.getClass() + " : " + e.getMessage());
		}
		return passwordChanged;
	}

   /**
   * This method will trigger the adding of a subscriber to an organization by mean of invocation of
   * the <code>call()</code> method of the <code>abstract
   *  class WeightedBSSCall</code> on an instance of the <code>AddSubscriber</code> class
   * <p>
   * 
   * @return the BSS subscriber identifier
   */
	private boolean updateSubscriber(){
		boolean updated = false;
		if(this.subscriber != null){
			List<NotesType> addedMailAttributes = new ArrayList<NotesType>();
			for(SubscriptionEntitlement entitlement:entitlements){
				NotesType entitlementNotesType = entitlement.getNotesType();
				if(!addedMailAttributes.contains(entitlementNotesType)){
					addedMailAttributes.add(entitlementNotesType);
					WeightedBSSCall<Boolean> updateSubscriber = new UpdateNotesSubscriber(this.subscriber, entitlementNotesType);
					logger.info("Updating subscriber...");
					try{
						updated = updateSubscriber.call();
					}catch(Exception e){
						logger.severe(e.getClass() + " : " + e.getMessage());
					}
				}
			}
		}
		return updated;
	}

	private String getSubscriberState(){
		WeightedBSSCall<JsonEntity> getSubscriber = new GetSubscriber(subscriberEmail, null);
		JsonEntity subscriberEntity = null;
		try{
			subscriberEntity = getSubscriber.call();
		}catch(Exception e){
			logger.severe(e.getMessage());
		}
		return subscriberEntity.getAsString("SubscriberState");
	}
  
	 /**
	  * This method will trigger the entitlement of the subscriber to the subscription by mean of invocation of the <code>call()</code> method of the <code>abstract
	  * class WeightedBSSCall</code> on an instance of the <code>EntitleSubscriber</code> class
	  * <p>
	  * @return <code>true</code> if the subscriber is entitled, <code>false</code> otherwise
	  */
	private boolean entitleSubscriber() {
		Boolean subscriberEntitled = false;
		logger.info("Entitling subscriber...");
		String subscriberState = getSubscriberState();
		if (!"ACTIVE".equals(subscriberState)) {
			return subscriberEntitled;
		}
		for (SubscriptionEntitlement entitlement : entitlements) {
			WeightedBSSCall<Boolean> entitleSubscriber = new EntitleSubscriber(
					this.subscriberId, entitlement.getSubscriptionId(),
					this.subscriberEmail);
			try {
				subscriberEntitled = entitleSubscriber.call();
				if (subscriberEntitled == null) {
					subscriberEntitled = false;
				}
				if (!subscriberEntitled) {
					// stop when any entitle subscriber fails
					logger.severe("Unable to entitle subscriber: "
							+ subscriberState + " to subscription: "
							+ entitlement.getPartNumber());
					break;
				}
			} catch (Exception e) {
				logger.severe(e.getClass() + " : " + e.getMessage());
			}
		}
		return subscriberEntitled;
	}
	
	/**
	 * This method will trigger the retrieval of the subscriber by mean of invocation of the <code>call()</code> method of the <code>abstract
	 *  class WeightedBSSCall</code> on an instance of the <code>GetSubscriber</code> class
	 * <p>.
	 * @return <code>true</code> if a seat from the subscription has been assigned to the subscriber, <code>false</code> otherwise
	 */
	private boolean waitForSeatAssignment(){
    	boolean seatsAssigned = false;
		if( this.subscriberId != null ){
			// SUBSCRIBER RETRIEVAL
			String suscriberEmail = 
				this.subscriber.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
			logger.info("Subscriber retrieval by email : " + suscriberEmail );

			WeightedBSSCall<JsonEntity> getSubscriber = new GetSubscriber( suscriberEmail , null );
			JsonEntity subscriberEntity = null ;
			try {
				subscriberEntity = getSubscriber.call();
			} catch (Exception e) {
	    		logger.severe(e.getClass()+" : " + e.getMessage());
			}
			if( subscriberEntity != null ){
				logger.info("Retrieved suscriber object");
				logger.info(subscriberEntity.getJsonObject().toString());
				List<Object> seatSet = subscriberEntity.getJsonObject().getAsList("SeatSet");
		        int numEntitlements = entitlements.size();
		        int entitlementsAssigned = 0;
		        for(SubscriptionEntitlement entitlement:entitlements){
		        	JsonJavaObject seatJson = findSeat(seatSet, entitlement.getSubscriptionId());
		        	if (seatJson != null) {
		        		String seatState = seatJson.getAsString("SeatState");
		        		if(!seatState.equalsIgnoreCase("ASSIGNED")){
		        			logger.info("seat not assigned !!!");
		        		} else if(seatState.equalsIgnoreCase("ASSIGNED")){
		        			entitlementsAssigned++;
		        			BSSProvisioning.getStateTransitionReport().get(this.subscriberEmail)[5] = new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
		        			logger.info("seat assigned !!!");
		        			if(numEntitlements == entitlementsAssigned){
		        				BSSProvisioning.incrementSubscribersProvisioned();
		        				seatsAssigned = true;
		        			}
		        			if(BSSProvisioning.getSubscribersQuantity().get() == (BSSProvisioning.getSubscribersProvisioned().get() + BSSProvisioning.getSubscribersFailed().get())){
		        				if(BSSProvisioning.getSubscribersFailed().get()>0){
		        					logger.warning("Provisioned Subscribers: "+ BSSProvisioning.getSubscribersProvisioned().get() +"\nFailed Subscribers: "+ BSSProvisioning.getSubscribersFailed().get());
		        				}else{		        				
		        					logger.finest("ALL SUBSCRIBERS PROVISIONED !!!");
		        				}
		        				BSSProvisioning.generateStateTransitionReport();
		        				BSSProvisioning.generateSubscriberWeightReport();
		        				BSSProvisioning.generateCallsCounterReport();
		        				WeightManager.getInstance().shutdown();
		        			}
		        		}
		        	}
		        }
			}else{
				logger.severe("the subscriber has not been retrieved !!!");
			}
		}
    	return seatsAssigned;
	}
	
	/**
	 * This method checks for completion of task
	 * @return <code>true</code> if complete, <code>false</code> otherwise
	 */
	private boolean checkForFinish(){
		boolean completed = false;
		if(BSSProvisioning.getSubscribersQuantity().get() == (BSSProvisioning.getSubscribersProvisioned().get() + BSSProvisioning.getSubscribersFailed().get())){
			completed = true;
			if(BSSProvisioning.getSubscribersFailed().get()>0){
				logger.warning("Provisioned Subscribers: "+ BSSProvisioning.getSubscribersProvisioned().get() +"\nFailed Subscribers: "+ BSSProvisioning.getSubscribersFailed().get());
			}else{		        				
				logger.finest("ALL SUBSCRIBERS PROVISIONED !!!");
			}
			BSSProvisioning.generateStateTransitionReport();
			BSSProvisioning.generateSubscriberWeightReport();
			BSSProvisioning.generateCallsCounterReport();
			WeightManager.getInstance().shutdown();
		}
		return completed;
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
	
	public JsonJavaObject getSubscriber() {
		return subscriber;
	}
	
	public String getSubscriberEmail() {
		return subscriberEmail;
	}
	
	public String getCustomerId() {
		return customerId;
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
	
	public void setSuppressEmail(boolean suppress){
		suppressEmail = suppress;
	}
}
