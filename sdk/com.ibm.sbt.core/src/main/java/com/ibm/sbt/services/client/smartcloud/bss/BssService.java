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
package com.ibm.sbt.services.client.smartcloud.bss;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.JsonFeedHandler;
import com.ibm.sbt.services.client.base.JsonService;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Base service class for Business Support System services
 * 
 * @author mwallace
 */
public class BssService extends JsonService {

	public static String PROPERTY_LIST = "List";
	public static String PROPERTY_CUSTOMER = "Customer";
	public static String PROPERTY_SUBSCRIBER = "Subscriber";
	public static String PROPERTY_SUBSCRIPTION = "Subscription";
	public static String PROPERTY_ID = "Id";
	
	public static String RESELLER_ORDER = "RESELLER-ORDER";
	
	public static String API_RESOURCE_CUSTOMER = "/api/bss/resource/customer";
	public static String API_RESOURCE_SUBSCRIBER = "/api/bss/resource/subscriber";
	public static String API_RESOURCE_SUBSCRIPTION = "/api/bss/resource/subscription";
	public static String API_AUTHORIZATION_GETROLELIST = "/api/bss/service/authorization/getRoleList";
	public static String API_AUTHORIZATION_ASSIGNROLE = "/api/bss/service/authorization/assignRole";
	public static String API_AUTHORIZATION_UNASSIGNROLE = "/api/bss/service/authorization/unassignRole";
	public static String API_AUTHENTICATION_CHANGEPASSWORD = "/api/bss/service/authentication/changePassword";
	public static String API_AUTHENTICATION_RESETPASSWORD = "/api/bss/service/authentication/resetPassword";
	public static String API_AUTHENTICATION_SETONETIMEPASSWORD = "/api/bss/service/authentication/setOneTimePassword";
	
	public enum DurationUnits {
		YEARS, MONTHS, DAYS
	};
	
	public enum BillingFrequency {
		MRC, QRC, ARC
	};
	
	public enum Role {
		CustomerAdministrator, User, VSR
	};
	
	public enum PaymentMethodType {
		CREDIT_CARD, PURCHASE_ORDER, INVOICE, NONE
	};
	
	public enum AddressType {
		BILLING, MAILING, MULTIPURPOSE
	};
	
	public enum CustomerIdType {
		CAAS_CUSTOMER_ID,
		COREMETRIC_CUSTOMER_ID,
		GLOBALCROSSING_ID,
		GLOBALIVE_ID,
		IBM_CUSTOMER_NUMBER,
		IBM_CUSTOMER_NUMBER_PREV,
		IBM_SITE_NUMBER,
		SALESFORCE_ACCOUNT_ID,
		SALESFORCE_CONTACT_ID,
		SALESFORCE_ID,
		SALESFORCE_LEAD_ID,
		SALESFORCE_OPPORTUNITY_ID,
		SIEBEL_ID,
		STERLING_CUSTOMER_ID,
		TMS_CUSTOMER_ID,
		UNICA_CUSTOMER_ID,
		UNYTE_CUSTOMER_ID
	};
	
	protected static Map<String, String> JsonHeader = new HashMap<String, String>();
	static {
		JsonHeader.put("Content-Type", "application/json");
	}

	protected static Map<String, String> SuspendCustomerHeader = new HashMap<String, String>();
	static {
		SuspendCustomerHeader.put("x-operation", "suspendCustomer");
	}

	protected static Map<String, String> UnsuspendCustomerHeader = new HashMap<String, String>();
	static {
		UnsuspendCustomerHeader.put("x-operation", "unsuspendCustomer");
	}

	protected static Map<String, String> ActivateSubscriberHeader = new HashMap<String, String>();
	static {
		ActivateSubscriberHeader.put("x-operation", "activateSubscriber");
	}

	protected static Map<String, String> SuspendSubscriberHeader = new HashMap<String, String>();
	static {
		SuspendSubscriberHeader.put("x-operation", "suspendSubscriber");
	}

	protected static Map<String, String> UnsuspendSubscriberHeader = new HashMap<String, String>();
	static {
		UnsuspendSubscriberHeader.put("x-operation", "unsuspendSubscriber");
	}

	protected static Map<String, String> SuspendSubscriptionHeader = new HashMap<String, String>();
	static {
		SuspendSubscriptionHeader.put("x-operation", "suspendSubscription");
	}

	protected static Map<String, String> UnsuspendSubscriptionHeader = new HashMap<String, String>();
	static {
		UnsuspendSubscriptionHeader.put("x-operation", "unsuspendSubscription");
	}

	protected static Map<String, String> EntitleSubscriberHeader = new HashMap<String, String>();
	static {
		EntitleSubscriberHeader.put("x-operation", "entitleSubscriber");
	}

	protected static Map<String, String> RevokeSubscriberHeader = new HashMap<String, String>();
	static {
		RevokeSubscriberHeader.put("x-operation", "revokeSubscriber");
	}

	static final String	sourceClass	= BssService.class.getName();
	static final Logger	logger = Logger.getLogger(sourceClass);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public BssService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public BssService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public BssService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public BssService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public BssService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.client.base.BaseService#getDataFormat()
     */
    @Override
    public Handler getDataFormat() {
    	if (this.dataFormat == null) {
    		this.dataFormat = new HandlerJson(); 
    	}
    	return super.getDataFormat();
    }

    /**
     * Return id for the specified JSON object.
     * 
     * @param jsonObject
     * @return
     */
    public String getId(JsonJavaObject jsonObject, String propertyName) {
    	if (jsonObject == null) {
    		return null;
    	}
    	Object value = jsonObject.get(PROPERTY_ID);
    	String subscriberId = (value == null) ? null : String.valueOf(((Number)value).longValue());
    	if (subscriberId == null) {
    		jsonObject = (JsonJavaObject)jsonObject.getAsObject(propertyName);
    		if (jsonObject != null) {
    			value = jsonObject.get(PROPERTY_ID);
    	    	subscriberId = (value == null) ? null : String.valueOf(((Number)value).longValue());
    		}
    	}
    	return subscriberId;
    }
        
    //
    // Internals
    //
	
	protected IFeedHandler<JsonEntity> getJsonFeedHandler() {
		return new JsonFeedHandler<JsonEntity>(this, PROPERTY_LIST);
	}
    
}
