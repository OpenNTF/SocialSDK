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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.WeightManager;
import com.ibm.sbt.provisioning.sample.app.logging.CustomLogger;
import com.ibm.sbt.provisioning.sample.app.model.Rest;
import com.ibm.sbt.provisioning.sample.app.model.Weight;
import com.ibm.sbt.provisioning.sample.app.model.WeightSet;
import com.ibm.sbt.provisioning.sample.app.model.Weights;
import com.ibm.sbt.provisioning.sample.app.util.Util;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.ChangePassword;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.SetOneTimePassword;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.customer.RegisterCustomer;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.ActivateSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.GetSubscriber;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscription.CreateSubscription;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscription.GetSubscription;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * This class represents a task that triggers the provisioning of the subscribers in input to the application in the 
 * context of separated threads .
 * <p>
 * This class contains the main method executed when launching the application.
 * The main method performs the necessary inizializations needed for provisioning the subscribers in input.
 * The <code>run()</code> method of this class is periodically executed thanks to the singleton
 * {@link com.ibm.sbt.provisioning.sample.app.WeightManager}, in order to 
 * permit to the partially provisioned subscriber to complete their provisioning process .
 * Each parsed subscriber is represented by an instance of the class {@link SubscriberTask}, that IS-A <code>Runnable</code> too,
 *  and the subscriber's provisioning logic is encapsulated in the {@link SubscriberTask#run()} method .
 * 
 * */
public class BSSProvisioning implements Runnable {

	/**
	 * pattern used for the subscribers' state transitions timestamps
	 */
	public static final String DATE_FORMAT = "dd MM yyyy HH:mm:ss SSS" ;
	private static final Logger logger = Logger.getLogger(BSSProvisioning.class.getName());
	/**
	 * <code>SimpleDateFormat</code> object used for formatting 
	 * the subscribers' state transitions' points in time
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	/**
	 * <code>BasicEndpoint</code> object representing the
	 * BSS Host url
	 */
	protected static BasicEndpoint basicEndpoint ;
	/**
	 * path to the weights json input file
	 */
	private static String weightsFile ;
    /**
    * path to the weights data object
    */
	protected static Weights weights;
	/**
	 * boolean set to true when the default weights.json file ( that comes packaged in the jar) must be used
	 */
	private static boolean weightsFileAsInput = false ;
	/**
	 * a queue containing all the {@link SubscriberTask} instances representing to the
	 * subscribers in input to be provisioned
	 */
	protected static ConcurrentLinkedQueue<SubscriberTask> subscriberTasks ;
	/**
	 * the Executor used by the application to provision the subscribers in input
	 * each in their own thread , executing the <code>run()</code> method of the corresponding
	 * {@link SubscriberTask} instance
	 */
	protected static ExecutorService threadpool ;
	/**
	 * number of subscribers in input
	 */
	protected static AtomicInteger subscribersQuantity;
	/**
	 * number of currently provisioned subscribers
	 */
	protected static AtomicInteger subscribersProvisioned = new AtomicInteger(0);
	/**
	 * <code>Map</code> associating each subscribers with a String array, containing a timestamp for each 
	 * state transition of the corresponding subscriber
	 */
	protected static Map<String,String[]> stateTransitionReport ;
	/**
	 * <code>Map</code> associating each subscribers with a int array, containing a counter for each 
	 * of the call made to provision the corresponding subscriber
	 */
	protected static Map<String,int[]> subscriberWeightReport ;
	
	/**
	 * Entry point of the application 
	 * <p>
	 * Initially the customer registration and the subscription creation will be triggered 
	 * by mean of calls to the BSS api and a list of subscriber to provision will be parsed
	 * from an input file. For each subscriber, an instance of the {@link SubscriberTask} class, that
     * implements <code>Runnable</code> too, will be created and put in the <code>static ConcurrentLinkedQueue</code> {@link #subscriberTasks}. 
     * Then the <code>run()</code> method will be invoked in the context of the main
	 * thread and the previously parsed subscribers will start to be provisioned in the 
	 * context of separated threads. 
	 *
	 * @param  args     Application input parameters<br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[0] : BSS host url(required)<br>
	 * 				    &nbsp;&nbsp;&nbsp;&nbsp;args[1] : BSS user(required)<br> 
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[2] : BSS password<br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[3] : partNumber/subscriptionIdentifier<br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[4] : path to the customer.json file(required)<br> 
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[5] : path to the subscribers.json file(required)<br> 
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;args[6] : path to the weights.json file(optional)<br> 
	 *   				&nbsp;&nbsp;&nbsp;&nbsp;args[7] : number of threads in the provisioning threadpool(optional default to 10)<br>
	 */
	public static void main( String[] args ){
		boolean logFileCreated = false ;
		try {
			logFileCreated = CustomLogger.setup();
		} catch (IOException ioe) {
			logger.severe(ioe.getMessage());
		}
		if(!logFileCreated){
			logger.severe("No log file will be available !!!");
		}
		
		stateTransitionReport = new ConcurrentHashMap<String,String[]>();
		subscriberWeightReport = new ConcurrentHashMap<String,int[]>();
		
		if (args.length < 6 || args.length > 8) {
			// Note: The email must be unique for each run of the application, it is used as the administrator email address
			logger.severe("Usage: java com.ibm.sbt.provisioning.sample.app.BSSProvisioning\n "
					+ "args[0] = BSS host url(required)\n "
					+ "args[1] = BSS user(required)\n "
					+ "args[2] = BSS password(required)\n "
					+ "args[3] = partNumber/subscriptionIdentifier(required)\n"
					+ "args[4] = customer.json's path(required)\n "
					+ "args[5] = path to the subscribers.json file(required)\n "
					+ "args[6] = path to the weights.json file(optional)\n "
					+ "args[7] = provisioning threadpool size(optional default to 10)\n"
					+ "The application accepts as input 4,5,7 or 8 input arguments\n");
		}else{
			String url = args[0];
			String user = args[1];
			String password = args[2];
			String partNumber = args[3];
			String customerFile = args[4]; 
			String subscribersFile = args[5];
			
			weightsFile = "/weights.json"; 
			int threadpoolSize = 10 ;
			
			switch( args.length ){
				case 6 :
					break ;
				case 7 :
					boolean isNotAnInt = false ;
					try{
						threadpoolSize = Integer.parseInt(args[6]);
					}catch( NumberFormatException nfe ){
						logger
							.severe("NumberFormatException thrown while parsing arg[6], assuming it represents the path to the weights.json file ");
						isNotAnInt = true ;
					}
					if(isNotAnInt){
						weightsFile = args[6]; 
						weightsFileAsInput = true ;
					}
					break ;
				case 8 :
					weightsFile = args[6]; 
					weightsFileAsInput = true ;
					try{
						threadpoolSize = Integer.parseInt(args[7]);
					}catch( NumberFormatException nfe ){
						logger.severe("NumberFormatException thrown while parsing arg[7], using default threadpoolsize (10) ");
						isNotAnInt = true ;
					}
					break ;
			}
			for(String arg :args){
				System.out.println(arg);
			}
			
			List<JsonJavaObject> subscribers = null ;
			subscribers = Util.parseSubscribers( subscribersFile );
			if( subscribers != null && subscribers.size() > 0){
				
				threadpool = Executors.newFixedThreadPool(threadpoolSize);
				
        		subscribersQuantity = new AtomicInteger(subscribers.size());
				basicEndpoint = new BasicEndpoint();
				basicEndpoint.setUrl(url);
				basicEndpoint.setForceTrustSSLCertificate(true);
				basicEndpoint.setUser(user);
				basicEndpoint.setPassword(password); 
				
				String customerJson =  null ;
				try{
					customerJson = Util.readFully(customerFile);
				} catch (IOException ioe) {
					logger.severe(ioe.getMessage());
				}
				if( customerJson != null ){
					String orgAdminEmail = getOrgAdminEmail( customerFile ) ;
					customerJson = StringUtil.replace(customerJson, "<email>", orgAdminEmail);
					// CUSTOMER REGISTRATION
					String customerId = registerCustomer( customerJson ) ;
					if( customerId != null ){
						logger.finest("customerId = "+customerId);
						// ORG.ADMINISTRATOR RETRIEVAL
						JsonEntity orgAdminEntity = retrieveOrgAdmin( orgAdminEmail ) ;
						if( orgAdminEntity != null ){
							String orgAdminId = String.valueOf(orgAdminEntity.getAsLong("Id"));
							if( orgAdminId != null ){
								logger.info( "org admin subscriber "+orgAdminEmail+" added with id "+orgAdminId );
								// ORG.ADMINISTRATOR ACTIVATION
								Boolean subscriberActive = activateOrgAdmin( orgAdminId ) ;
								if( subscriberActive!= null && subscriberActive ){
									// ORG.ADMINISTRATOR ONE TIME PASSWORD SETTING 
									Boolean oneTimePasswordSet = orgAdminOneTimePasswordSetting( orgAdminEmail , "onet1me!" ) ;
									if( oneTimePasswordSet != null && oneTimePasswordSet ){
										// ORG.ADMINISTRATOR PASSWORD CHANGING
										Boolean passwordChanged = orgAdminChangePassword(orgAdminEmail, "onet1me!", "passw0rd") ;
										if( passwordChanged != null && passwordChanged){
											// SUBSCRIPTION CREATION
											String subscriptionId = 
												createSubscription( customerId , 3, partNumber/*"D0NWLLL"*/, subscribersQuantity.get()) ;
											if( subscriptionId != null ){
												// sleeping 10 sec waiting for the subscription to be created and activated
												try {
													Thread.sleep(1000*10);
												} catch (InterruptedException e) {
													logger.severe(e.getMessage());
												}
												// SUBSCRIPTION RETRIEVAL
												JsonEntity subscription = retrieveSubscription( subscriptionId ) ;
												if( subscription != null ){
													String currentState = subscription.getAsString("Subscription/SubscriptionState");
													boolean subscriptionActive = currentState.equalsIgnoreCase("ACTIVE");
													int i = 0 ;
													// WAITING FOR SUBSCRIPTION TO BECOME ACTIVE
													// looping ( max 20 times with 15 sec period ) till the state of the subscription is active
													// sometimes it takes more than 5 min to be activated
													while( !subscriptionActive && i < 20 ){
														try {
															Thread.sleep(1000*15);
														} catch (InterruptedException e) {
															logger.severe(e.getMessage());
														}
														subscription = retrieveSubscription( subscriptionId ) ;
														if(subscription != null){
															currentState = subscription.getAsString("Subscription/SubscriptionState");
															subscriptionActive = currentState.equalsIgnoreCase("ACTIVE");
														}else{
															logger.severe("the subscription has not been retrieved !!!");
														}
														i++ ;
													}
													if(subscriptionActive){
														// SUBSCRIPTION ACTIVE -->
														subscriberTasks = new ConcurrentLinkedQueue<SubscriberTask>();
														for( JsonJavaObject subscriber : subscribers ){
															String suscriberEmail = 
																subscriber
																	.getAsObject("Subscriber")
																	.getAsObject("Person")
																	.getAsString("EmailAddress");
															SubscriberTask subscriberTask = 
																	new SubscriberTask(subscriber, customerId, subscriptionId,
																			SubscriberTask.State.SUBSCRIBER_NON_EXISTENT );	
															subscriberTasks.add(subscriberTask);
															stateTransitionReport.put(suscriberEmail, new String[6]);
															subscriberWeightReport.put(suscriberEmail, new int[3]);
														}
														new BSSProvisioning().run();
													}else{
														logger.severe("the subscription has not been activated after 5 min waiting !!!");
														WeightManager.getInstance().shutdown();
													}
												}else{
													logger.severe("the subscription has not been retrieved !!!");
													WeightManager.getInstance().shutdown();
												}
											}else{
												logger.severe("the subscription has not been created !!!");
												WeightManager.getInstance().shutdown();
											}
										}else{
											logger.severe("org admin subscriber password has not been changed !!!");
											WeightManager.getInstance().shutdown();
										}
									}else{
										logger.severe("org admin subscriber one time password has not been set !!!");
										WeightManager.getInstance().shutdown();
									}
								}else{
									logger.severe("the org admin subscriber is not active !!!");
									WeightManager.getInstance().shutdown();
								}
							}else{
								logger.severe("org admin id is null !!!");
								WeightManager.getInstance().shutdown();
							}
						}else{
							logger.severe("could not retrive org admin subscriber !!!");
							WeightManager.getInstance().shutdown();
						}
					}else{
						logger.severe("customer has not been registered !!!");
						WeightManager.getInstance().shutdown();
					}
				}else{
					logger.severe("No customer to be created !!!");
					WeightManager.getInstance().shutdown();
				}
			}else{
				logger.severe("No subscribers to provison !!!");
				WeightManager.getInstance().shutdown();
			}
		}
	}
	
	/**
	 * Business logic of the task 
	 * <p>
	 * This method will simply poll instances of the {@link SubscriberTask} class from the 
	 * <code>static ConcurrentLinkedQueue</code> {@link #subscriberTasks}
	 * and submit them for execution to the <code>static ExecutorService</code> named {@link #threadpool}. 
	 * This <code>Executor</code> represents a thread pool that reuses 
	 * a fixed number of threads operating off a shared unbounded internal queue. If additional tasks are submitted when all threads are active, 
	 * they will wait in the queue until a thread is available. Every time the <code>submit()</code> method is invoked on
	 * the <code>static</code> {@link #threadpool} object, the <code>run()</code> method of the corresponding {@link SubscriberTask}
     * instance, will be executed in the context of a separated thread of execution.
	 */
	@Override
	public void run(){
		try{
			logger.info("Provisioning of users... " + BSSProvisioning.getSubscribersProvisioned().get() + "/" + BSSProvisioning.getSubscribersQuantity().get());
			// first execution in the context of the main thread, all the others in their own thread
			if(!Thread.currentThread().getName().equals("main")){
				Thread.currentThread().setName("Provisioning");
			}

			if(BSSProvisioning.getSubscribersQuantity().get() > BSSProvisioning.getSubscribersProvisioned().get()){
				// stop submitting when the threshold has been reached and the queue has been emptied
				while( !WeightManager.getInstance().isThresholdReached() && subscriberTasks != null && !subscriberTasks.isEmpty() ){
					SubscriberTask subscriberTask = subscriberTasks.poll();
					if( subscriberTask != null ){
						logger.info("Flow : "+subscriberTask.getSubscriberTaskKey()+" and status " + subscriberTask.getStatus().name()+" polled from the queue...submitting it...");
						if( subscriberTask.getStatus() == SubscriberTask.State.SUBSCRIBER_NON_EXISTENT ){
							String[] subscriberReport = stateTransitionReport.get(subscriberTask.getSubscriberEmail());
							subscriberReport[0] = sdf.format(new Date());
						}
						threadpool.submit(subscriberTask);
					}
					//sleeping 250 millisec between one submit and the other with the goal of 
					//avoiding to overwhelm the server with requests
					Thread.sleep(250);
				}
			}
		}catch(Exception e){
			logger.severe("Catched Exception in the BSSProvisioning run() method : "+e.getMessage() );
		}
	}
	
	/**
	 * This method will trigger the customer registration by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.customer.RegisterCustomer} class
	 * <p>
	 * @param  customerJson   json formatted string representing the customer to be created<br>
	 * @return the BSS customer identifier
	 */
	private static String registerCustomer( String customerJson ){
		String customerId = null ;
		logger.info("Customer registration ...");
		WeightedBSSCall<String> registerCustomer = new RegisterCustomer(customerJson);
		try{
			customerId = registerCustomer.call();
		} catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return customerId ;
	}
	
	/**
	 * This method will trigger the organization administrator retrieval by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.GetSubscriber} class
	 * <p>
	 * @param  email   email used as user credential of the organization administrator<br>
	 * @return a <code>JsonEntity</code> representing the retrieved organization administrator
	 */
	private static JsonEntity retrieveOrgAdmin( String email ){
		JsonEntity orgAdminEntity = null ;
		logger.info("Org administrator retrieval...");
		WeightedBSSCall<JsonEntity> getSubscriber = new GetSubscriber( email, null );
		try {
			orgAdminEntity = getSubscriber.call() ;
		} catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return orgAdminEntity ;
	}
	
	/**
	 * This method will trigger the organization administrator activation by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber.ActivateSubscriber} class
	 * <p>
	 * @param  orgAdminId   BSS identifier of the organization administrator<br>
	 * @return <code>true</code> if activated, <code>false</code> otherwise
	 */
	private static Boolean activateOrgAdmin( String orgAdminId ){
		Boolean subscriberActive = false ;
		logger.info("Org administrator activation...");
		WeightedBSSCall<Boolean> activateSubscriber = new ActivateSubscriber(orgAdminId);
		try{
			subscriberActive = activateSubscriber.call();
		} catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return subscriberActive ;
	}
	
	/**
	 * This method will trigger the organization administrator one time password setting by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.SetOneTimePassword} class
	 * <p>
	 * @param  email   email used as user credential of the organization administrator<br>
	 * @param  oneTimePassword   string representing the one time password<br>
	 * @return <code>true</code> if the one time password is set, <code>false</code> otherwise
	 */
	private static Boolean orgAdminOneTimePasswordSetting( String email , String oneTimePassword ){
		Boolean oneTimePasswordSet = false ;
		logger.info("Org administrator one time password setting...");
		WeightedBSSCall<Boolean> setOneTimePassword = new SetOneTimePassword(email , oneTimePassword );
		try{
			oneTimePasswordSet = setOneTimePassword.call();
		}catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return oneTimePasswordSet ;
	}
	
	/**
	 * This method will trigger the organization administrator password changing by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.authentication.ChangePassword} class
	 * <p>
	 * @param  email   email used as user credential of the organization administrator<br>
	 * @param  oneTimePassword   string representing the one time password<br>
	 * @param  newPassword   string representing the new password<br>
	 * @return <code>true</code> if the password is changed, <code>false</code> otherwise
	 */
	private static Boolean orgAdminChangePassword( String email , String oneTimePassword , String newPassword ){
		Boolean passwordChanged = false ;
		logger.info("Org administrator changing password ...");
		WeightedBSSCall<Boolean> changePassword = new ChangePassword(email, oneTimePassword, newPassword);
		try{
			passwordChanged = changePassword.call();
		}catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return passwordChanged ;
	}
	
	/**
	 * This method will trigger the subscription creation by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscription.CreateSubscription} class
	 * <p>
	 * @param  customerId   the BSS customer identifier<br>
	 * @param  duration   an int representing the subscription duration in years<br>
	 * @param  partNumber   string representing the subscription identifier<br>
	 * @param  quantity   an int representing the number of seat of the subscription<br>
	 * @return the BSS subscription identifier
	 */
	private static String createSubscription( String customerId , int duration , String partNumber, int quantity ){
		String subscriptionId = null ;
		logger.info("Subscription creation ...");
		WeightedBSSCall<String> createSubscription = new CreateSubscription(customerId, duration, partNumber, quantity);
		try {
			subscriptionId = createSubscription.call();
		}catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return subscriptionId ;
	}
	
	/**
	 * This method will trigger the subscription retrieval by mean of invocation of the 
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall#call()} method 
	 * on an instance of the {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscription.GetSubscription} class
	 * <p>
	 * @param  subscriptionId   the BSS subscription identifier<br>
	 * @return a <code>JsonEntity</code> representing the retrieved subscription
	 */
	private static JsonEntity retrieveSubscription( String subscriptionId ){
		JsonEntity subscription = null ;
		logger.info("Subscription retrieval ...");
		WeightedBSSCall<JsonEntity> getSubscription = new GetSubscription(subscriptionId);
		try {
			subscription = getSubscription.call();
		}catch (Exception e) {
    		logger.severe(e.getClass()+" : " + e.getMessage());
		}
		return subscription ;
	}
	
	/**
	 * CSV report generation 
	 * <p>
	 * This method uses the {@link #stateTransitionReport} <code>Map</code> for the generation of a CSV report 
	 * tracing the lifecycle of each subscriber
	 */
	public static void generateStateTransitionReport(){
		try{
			File csvReport = new File("stateTransitionReport.csv");
			BufferedWriter bwSub = new BufferedWriter(new FileWriter(csvReport));
		      bwSub
		        .write("Email,SUBSCRIBER_NON_EXISTENT,SUBSCRIBER_ADDED,SUBSCRIBER_ACTIVE,SUBSCRIBER_ONE_TIME_PWD_SET,SUBSCRIBER_ENTITLED,SEAT_ASSIGNED,TOTAL TIME(sec)\n");
		      for(String emailSubscriptionId:stateTransitionReport.keySet()){
				long seatAssigned = 0L ;
				long subscriberNonExistent = 0L ;
        		String[] timestamps = stateTransitionReport.get(emailSubscriptionId);
        		bwSub.write(emailSubscriptionId + ",");
                int index = 0 ;
                for(String timestamp : timestamps){
                	if( index == 0 ){
                		try {
                			subscriberNonExistent = (sdf.parse(timestamp)).getTime();
						} catch (ParseException e) {
							logger.severe("Unparsable SUBSCRIBER_NON_EXISTENT timestamp :\n" + e.getMessage() );
						}
                	}else if(index == 5){
                		try {
							seatAssigned = (sdf.parse(timestamp)).getTime();
						} catch (ParseException e) {
							logger.severe("Unparsable SEAT_ASSIGNED timestamp :\n" + e.getMessage() );
						}
                    }
                	if( timestamp == null){
                		bwSub.write("null,");
                	}else{
                		bwSub.write(timestamp+",");
                	}
                	index++;
                }
                if(subscriberNonExistent != 0L && seatAssigned != 0L ){
                	long totalProvisioningTime = (seatAssigned - subscriberNonExistent)/1000 ;
                	bwSub.write(totalProvisioningTime+"\n");
                }else{
                	bwSub.write("null\n");
                }
			}
			bwSub.flush();
            bwSub.close();
		}catch( IOException ioe ){
			ioe.printStackTrace();
	    }
	}
	
	/**
	 * CSV report generation 
	 * <p>
	 * This method uses {@link #subscriberWeightReport} <code>Map</code>  for the generation of a CSV report 
	 * tracing the weight consumed by each subscriber
	 */
	public static void generateSubscriberWeightReport(){
		try{
			File csvReport = new File("subscriberWeightReport.csv");
			BufferedWriter bwSub = new BufferedWriter(new FileWriter(csvReport));
			bwSub.write("Email,/resource/subscriber:POST,/service/authentication,/resource/subscriber:GET,TOTAL_WEIGHT\n");
      for(String emailSubscriptionId:subscriberWeightReport.keySet()){
				int totalWeight = 0 ;
        int[] callNumbers = subscriberWeightReport.get(emailSubscriptionId);
        bwSub.write(emailSubscriptionId + ",");
                int index = 0 ;
                for(int callNumber : callNumbers){
                	switch( index ){
        			case 0 :
              totalWeight =
                  totalWeight
                      + (callNumber * WeightManager.getInstance().getWeightValuePerBSSCall("/resource/subscriber",
                          Rest.POST));
        				bwSub.write(callNumber+",");
        				break;
        			case 1 :
              totalWeight =
                  totalWeight
                      + (callNumber * WeightManager.getInstance().getWeightValuePerBSSCall("/service/authentication",
                          Rest.ALL));
        				bwSub.write(callNumber+",");
        				break;
        			case 2 :
              totalWeight =
                  totalWeight
                      + (callNumber * WeightManager.getInstance().getWeightValuePerBSSCall("/resource/subscriber",
                          Rest.GET));
        				bwSub.write(callNumber+","+totalWeight+"\n");
        				break;
        			}
                	index++;
                }
			}
			bwSub.flush();
            bwSub.close();
		}catch( IOException ioe ){
			ioe.printStackTrace();
	    }
	}
	
	/**
	 * CSV report generation 
	 * <p>
	 * This method uses a map having as keys the BSS calls keys as specified by the
	 * {@link com.ibm.sbt.provisioning.sample.app.weightedBSSCall.BSSCall} <code>interface</code>
	 * and as values the number of times that endpoint has been accessed using 
	 * that HTTP method, for the generation of a CSV report keeping track of the BSS API usage
	 */
	public static void generateCallsCounterReport(){
		try{
			File csvReport = new File("callsCounterReport.csv");
			BufferedWriter bwSub = new BufferedWriter(new FileWriter(csvReport));
			String header = "" ;
			String row = "" ;
		      WeightSet defaultSet = weights.getDefaultSet();
		      for(String url:defaultSet.getUrls()){
		        HashMap<Rest, Weight> methodMap = defaultSet.getAllFromUrl(url);
		        for(Rest method:methodMap.keySet()){
		          Weight weight = methodMap.get(method);
		          header = header + url + ":" + method + ",";
		          row = row + weight.getCounter() + ",";
		        }
			}
			header = ( header.substring( 0, header.length() -1 ) ) + "\n" ;
			row = ( row.substring( 0, row.length() -1 ) ) + "\n" ;
			logger.info(header);
			logger.info(row);
			bwSub.write(header);
			bwSub.write(row);
			bwSub.flush();
            bwSub.close();
		}catch( IOException ioe ){
			ioe.printStackTrace();
	    }
	}

	/**
	 * {@link #basicEndpoint} getter method
	 */
	public static BasicEndpoint getBasicEndpoint() {
		return basicEndpoint;
	}

	/**
	 * {@link #weightsFile} getter method
	 */
	public static String getWeightsFile() {
		return weightsFile;
	}

  /**
   * {@link #weights} getter method
   */
  public static Weights getWeights(){
    return weights;
  }

	/**
	 * {@link #weightsFileAsInput} getter method
	 */
	public static boolean isWeightsFileAsInput() {
		return weightsFileAsInput;
	}

	/**
	 * {@link #subscriberTasks} getter method
	 */
	public static ConcurrentLinkedQueue<SubscriberTask> getSubscribersTasks() {
		return subscriberTasks;
	}

	/**
	 * {@link #threadpool} getter method
	 */
	public static ExecutorService getThreadPool() {
		return threadpool;
	}

	/**
	 * {@link #subscribersQuantity} getter method
	 */
  public static AtomicInteger getSubscribersQuantity(){
		return subscribersQuantity;
	}

	/**
	 * {@link #subscribersProvisioned} incrementation method
	 */
	public static synchronized void incrementSubscribersProvisioned(){
    subscribersProvisioned.incrementAndGet();
	}

	/**
	 * {@link #subscribersProvisioned} getter method
	 */
  public static synchronized AtomicInteger getSubscribersProvisioned(){
		return subscribersProvisioned ;
	}

	/**
	 * {@link #stateTransitionReport} getter method
	 */
	public static Map<String, String[]> getStateTransitionReport() {
		return stateTransitionReport;
	}

	/**
	 * {@link #subscriberWeightReport} getter method
	 */
	public static Map<String, int[]> getSubscriberWeightReport() {
		return subscriberWeightReport;
	}
	
	/**
	 * This method will parse the json input file representing the customer in order to return the email address of the
	 * organization administrator
	 * 
	 * @return a String representing the organization administrator email
	 */
	private static String getOrgAdminEmail( String customerFilePath ){
		String orgAdminEmail = null ;
		String customerJson = null ;
		JsonDataHandler handler = null ;
		try{
      		customerJson = Util.readFully(customerFilePath);
			handler = new JsonDataHandler(customerJson);
		}catch( IOException ioe ){
			logger.severe("Exception thrown during customer.json file parsing : "+ ioe.getMessage());
		}catch (JsonException e) {
			logger.severe("Exception thrown during customer.json file parsing : "+ e.getMessage());
		}
		if( customerJson != null && handler != null ) {
			orgAdminEmail = handler.getAsString("Customer/Organization/Contact/EmailAddress");
			logger.finest("Org admin email :"+orgAdminEmail);
		}
		return orgAdminEmail ; 
	}
}
