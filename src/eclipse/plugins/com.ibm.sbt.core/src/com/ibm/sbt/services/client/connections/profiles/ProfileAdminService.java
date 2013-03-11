package com.ibm.sbt.services.client.connections.profiles;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.profiles.exception.ProfileServiceException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;


public class ProfileAdminService extends ProfileService {
	/**
	 * Default Constructor - 0 argument constructor
	 *  
	 * Calls the Constructor of BaseService Class.
	 */
	
	public ProfileAdminService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
    }
	/** 
	 * Constructor - 1 argument constructor
	 * 
	 * @param endpoint
	 * Creates ProfileService with specified endpoint and a default CacheSize
	 */
	
	public ProfileAdminService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}
	
	/** 
	 * Constructor - 2 argument constructor
	 * 
	 * @param endpoint
	 * @param cacheSize
	 * 
	 * Creates ProfileService with specified values of endpoint and CacheSize
	 */
	
	public ProfileAdminService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * deleteProfile - to delete profile
	 * @param userId
	 * @return
	 * 
	 * This method is used to delete a user's profile.
	 * argument should be a unique id of the user whose profile needs to be deleted
	 * the argument could either be a email or userid
	 * User should be logged in as a administrator to call this method
	 * @throws ProfileServiceException 
	 */
	public boolean deleteProfile(Profile profile) throws ProfileServiceException
	{	
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "deleteProfile", profile);
		}
		if (profile == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}
		
		Map<String, String> parameters = new HashMap<String, String>();

		if (isEmail(profile.getReqId())) {
			parameters.put("email", profile.getReqId());
		} else {
			parameters.put("userid", profile.getReqId());
		}
		boolean result = executeDelete(ProfilesAPI.DELETEPROFILE.getUrl(), parameters);

		removeProfileDataFromCache(profile.getReqId());

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteProfile", result);
		}
		return result;
	}

	/**
	 * create - to create a User's profile
	 * @param userId
	 * @return
	 * 
	 * This method is used to create a user's profile.
	 * User should be logged in as a administrator to call this method
	 * @throws ProfileServiceException 
	 */
	public boolean createProfile(Profile profile) throws ProfileServiceException 
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "create", profile);
		}
		if (profile == null) {
			throw new IllegalArgumentException(Messages.InvalidArgument_3);
		}		
		Map<String, String> parameters = new HashMap<String,String>();
		Map<String, String> headers = new HashMap<String,String>();
		headers.put(Headers.ContentType,Headers.ATOM);

		if(isEmail(profile.getReqId())){
			parameters.put(ProfileRequestParams.EMAIL,profile.getReqId());
		}
		else{
			parameters.put(ProfileRequestParams.USERID,profile.getReqId()); 
		}
		Object createPayload = profile.constructCreateRequestBody();
		boolean returnVal = executePost(ProfilesAPI.ADDPROFILE.getUrl(), parameters, headers, createPayload, ClientService.FORMAT_NULL);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "create");
		}
		return returnVal;
	}

}
