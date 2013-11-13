package com.ibm.sbt.services.client.connections.profiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.client.ClientService;

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
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
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
	 * @param id
	 * 			unique identifier of user whose profile is to be deleted, it can either be a email or userid
	 * @throws ProfileServiceException 
	 */
	public void deleteProfile(String id) throws ProfileServiceException
	{	
		
		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		try{
			Map<String, String> parameters = new HashMap<String, String>();
			setIdParameter(parameters,id );
			String deleteUrl = resolveProfileUrl(ProfileAPI.ADMIN.getProfileEntityType(), ProfileType.DELETEPROFILE.getProfileType());
			super.deleteData(deleteUrl, parameters, getUniqueIdentifier(id));
		}
		catch(ClientServicesException e){
			throw new ProfileServiceException(e, Messages.DeleteProfileException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.DeleteProfileException, id);
		}

	}

	/**
	 * Wrapper method to create a User's profile
	 * <p>
	 * User should be logged in as a administrator to call this method
	 * 
	 * @param Profile
	 * @throws ProfileServiceException 
	 */
	public void createProfile(Profile profile) throws ProfileServiceException 
	{
		
		if (profile == null) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_3);
		}		
		try {
			Map<String, String> parameters = new HashMap<String,String>();
			setIdParameter(parameters, profile.getUserid());
			Object createPayload = constructCreateRequestBody(profile);
			
			String createUrl = resolveProfileUrl(ProfileAPI.ADMIN.getProfileEntityType(),ProfileType.ADDPROFILE.getProfileType());
			super.createData(createUrl, parameters, createPayload, ClientService.FORMAT_CONNECTIONS_OUTPUT);
			
		}catch(ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.CreateProfileException, profile.getUserid());
		}catch (TransformerException e) {
			throw new ProfileServiceException(e, Messages.CreateProfilePayloadException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.CreateProfileException, profile.getUserid());
		}

	}

}
