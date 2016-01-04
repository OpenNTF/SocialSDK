package com.ibm.sbt.provisioning.sample.app.services;

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;
import com.ibm.sbt.services.endpoints.Endpoint;

// TODO Move to SubscriberManagementService when code can be merged to SocialSDK


public class GRTESubscriberManagementService extends
		SubscriberManagementService {
	
	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public GRTESubscriberManagementService(Endpoint endpoint) {
		super(endpoint);
	}
	
	private static final long serialVersionUID = 2L;
	
	/**
     * Add a subscriber w/ suppressing emails either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param subscriber
     * @return {JsonJavaObject}
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject addSubscriberSuppressEmail(SubscriberJsonBuilder subscriber) throws BssException, IOException, JsonException {
    	return addSubscriberSuppressEmail(subscriber.toJson());
    }

    /**
     * Add a subscriber w/ suppressing emails either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param subscriberJson
     * @return {JsonJavaObject}
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject addSubscriberSuppressEmail(String subscriberJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, subscriberJson);
    	return addSubscriberSuppressEmail(jsonObject);
    }

    /**
     * Add a subscriber w/ suppressing emails either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param subscriberJson
     * @return JSON object containing 
     * @throws BssException
     * @throws IOException 
     */
    public JsonJavaObject addSubscriberSuppressEmail(JsonJavaObject subscriberJson) throws BssException {
		try {
			// TODO add url to  BSSUrls when moving to SocialSDK
			String serviceUrl = "/api/bss/resource/subscriber?suppressEmail=true";
			Response serverResponse = createData(serviceUrl, null, JsonHeader, subscriberJson, ClientService.FORMAT_JSON);
			return (serverResponse == null) ? null : (JsonJavaObject)serverResponse.getData();
		} catch (Exception e) {
			throw new BssException(e, "Error adding subscriber {0} caused by {1}", subscriberJson, e.getMessage());
		}
    }
}
