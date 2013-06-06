package com.ibm.sbt.services.client.connections.profiles;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.connections.profiles.exception.ProfileServiceException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;

/**
 * ProfileAdminService can be used to perform Admin operations related to Connection Profiles. 
 * 
 * @Represents Connections ProfileAdminService
 * @author Swati Singh
 * <pre>
 * Sample Usage
 * {@code
 *  ProfileAdminService _service = new ProfileAdminService();
 *  Profile profile = _service.getProfile("testUser61@renovations.com", false);
 *  profile.set("uid", "testUser");
 *  boolean success = service.createProfile(profile);
 * }
 * </pre>
 */
public class ProfileAdminService extends ProfileService {

	/**
	 * Constructor Creates ProfileAdminService Object with default endpoint and default cache size
	 */
	public ProfileAdminService() {
		this(DEFAULT_ENDPOINT, DEFAULT_CACHE_SIZE);
    }
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 *            Creates ProfileAdminService with specified endpoint and a default CacheSize
	 */
	public ProfileAdminService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileAdminService with specified endpoint and CacheSize
	 */
	public ProfileAdminService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * Wrapper method to delete a User's profile
	 * <p>
	 * User should be logged in as a administrator to call this method
	 * 
	 * @param userId
	 * 			unique identifier of user whose profile is to be deleted, it can either be a email or userid
	 * @return boolean
	 * 				returns true if profile is deleted succesfully
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
		boolean result = executeDelete(resolveProfileUrl(ProfileEntity.ADMIN.getProfileEntityType(), ProfileType.DELETEPROFILE.getProfileType()),
				parameters);

		removeProfileDataFromCache(profile.getReqId());

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "deleteProfile", result);
		}
		return result;
	}

	/**
	 * Wrapper method to create a User's profile
	 * <p>
	 * User should be logged in as a administrator to call this method
	 * 
	 * @param Profile
	 * @return boolean
	 * 				value is true if profile is create successfully
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
		boolean returnVal = executePost(resolveProfileUrl(ProfileEntity.ADMIN.getProfileEntityType(),ProfileType.ADDPROFILE.getProfileType()),
				parameters, headers, createPayload, ClientService.FORMAT_NULL);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "create");
		}
		return returnVal;
	}

}
