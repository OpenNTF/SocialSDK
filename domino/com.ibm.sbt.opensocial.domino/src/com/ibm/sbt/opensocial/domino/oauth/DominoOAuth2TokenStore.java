package com.ibm.sbt.opensocial.domino.oauth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetContext;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.GadgetException.Code;
import org.apache.shindig.gadgets.GadgetSpecFactory;
import org.apache.shindig.gadgets.oauth2.OAuth2Accessor;
import org.apache.shindig.gadgets.oauth2.OAuth2Arguments;
import org.apache.shindig.gadgets.oauth2.OAuth2Error;
import org.apache.shindig.gadgets.oauth2.OAuth2GadgetContext;
import org.apache.shindig.gadgets.oauth2.OAuth2RequestException;
import org.apache.shindig.gadgets.oauth2.OAuth2Token;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Encrypter;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2TokenPersistence;
import org.apache.shindig.gadgets.spec.BaseOAuthService.EndPoint;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.OAuth2Service;
import org.apache.shindig.gadgets.spec.OAuth2Spec;

import com.google.common.base.Joiner;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointException;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;

/**
 * Stores OAuth2 information.
 *
 */
//TODO Right now every container is forced to let every gadget share OAuth tokens.  Some containers may not want that.  We should allow
//containers to specify that in their configuration and handle that here.
public class DominoOAuth2TokenStore {
	private static final String CLASS = DominoOAuth2TokenStore.class.getName();

	private Map<String, DominoOAuth2Accessor> accessorStore = Collections.synchronizedMap(new HashMap<String, DominoOAuth2Accessor>());
	private Map<String, OAuth2Token> accessTokenStore = Collections.synchronizedMap(new HashMap<String, OAuth2Token>());
	private Map<String, OAuth2Token> refreshTokenStore = Collections.synchronizedMap(new HashMap<String, OAuth2Token>());

	private static class OAuth2SpecInfo {
		private final String authorizationUrl;
		private final String scope;
		private final String tokenUrl;

		public OAuth2SpecInfo(final String authorizationUrl, final String tokenUrl, final String scope) {
			this.authorizationUrl = authorizationUrl;
			this.tokenUrl = tokenUrl;
			this.scope = scope;
		}

		public String getAuthorizationUrl() {
			return this.authorizationUrl;
		}

		public String getScope() {
			return this.scope;
		}

		public String getTokenUrl() {
			return this.tokenUrl;
		}
	}

	private ContainerExtPointManager manager;
	private Logger log;
	private GadgetSpecFactory specFactory;
	private OAuth2Encrypter encrypter;
	private Authority authority;
	private String contextRoot;
	private BlobCrypter stateCrypter;
	private String globalRedirectUri;
	
	/**
	 * Creates a new DominoOAuth2TokenStore.
	 * @param specFactory The spec factory containing the gadget specs.
	 * @param manager The container extension point manager.
	 * @param log The logger.
	 * @param encrypter The OAuth 2.0 encyrpter.
	 * @param authority The authority information.
	 * @param contextRoot The servers context root.
	 * @param stateCrypter The encrypter to use for state information.
	 * @param globalRedirectUri The OAuth 2 callback URI.
	 */
	public DominoOAuth2TokenStore(GadgetSpecFactory specFactory, ContainerExtPointManager manager,
			Logger log, OAuth2Encrypter encrypter,
			Authority authority, String contextRoot,
			BlobCrypter stateCrypter,
			String globalRedirectUri) {
		this.specFactory = specFactory;
		this.manager = manager;
		this.encrypter = encrypter;
		this.authority = authority;
		this.contextRoot = contextRoot;
		this.stateCrypter = stateCrypter;
		this.globalRedirectUri = globalRedirectUri;
		this.log = log;
	}

	/**
	 * Gets an OAuth 2 accessor.  This method will actually merge OAuth 2 information from the gadget spec
	 * if it is present and the OAuth 2 client allows it.
	 * @param securityToken The security token containing information to lookup the accessor by.
	 * @param arguments The OAuth 2 arguments.
	 * @param gadgetUri The gadget URI.
	 * @return An OAuth 2 accessor if one exists in the store.
	 */
	public DominoOAuth2Accessor getOAuth2Accessor(SecurityToken securityToken, OAuth2Arguments arguments, Uri gadgetUri) {
		final String method = "getOAuth2Accessor";
		log.entering(CLASS, method,
				new Object[] { securityToken, arguments, gadgetUri });
		DominoOAuth2Accessor ret = null;
		if ((gadgetUri == null) || (securityToken == null)) {
			ret = new BasicDominoOAuth2Accessor();
			ret.setErrorResponse(null, OAuth2Error.GET_OAUTH2_ACCESSOR_PROBLEM,
					"OAuth2Accessor missing a param --- gadgetUri = "
							+ gadgetUri + " , securityToken = " + securityToken, "");
		} else {
			final String serviceName = StringUtils.defaultString(arguments.getServiceName());
			try {
				ret = getOAuth2Accessor(gadgetUri, serviceName, securityToken, arguments);
				storeOAuth2Accessor(ret);
			} catch (Exception e) {
				log.logp(Level.WARNING, CLASS, method, "Error while getting OAuth 2 accessor.", e);
				ret = new BasicDominoOAuth2Accessor();
				ret.setErrorResponse(e, OAuth2Error.GET_OAUTH2_ACCESSOR_PROBLEM, "Error while getting OAuth 2 accessor", "");
			}
		}

		log.exiting(CLASS, method, ret);

		return ret;
	}
	
	/**
	 * Gets the scope the OAuth2Arguments.  If the OAuth2Arguments does not have scope information than we will try and get it
	 * from the gadget spec.  If the gadget spec does not have scope information than return the empty string.
	 * @param arguments OAuth 2 arguments.
	 * @param specInfo Gadget spec.
	 * @return The OAuth 2 scope.
	 */
	private String getScope(OAuth2Arguments arguments, OAuth2SpecInfo specInfo) {
		return StringUtils.isBlank(arguments.getScope()) ? StringUtils.isBlank(specInfo.getScope()) ? "" : specInfo.getScope() : arguments.getScope();
	}
	
	private DominoOAuth2Accessor getOAuth2Accessor(Uri gadgetUri, String serviceName, SecurityToken securityToken, OAuth2Arguments arguments) throws GadgetException, OAuth2RequestException {
		OAuth2SpecInfo specInfo = this.lookupSpecInfo(securityToken, arguments, gadgetUri);
		String scope = getScope(arguments, specInfo);
		String container = securityToken.getContainer();
		DominoOAuth2Accessor persistedAccessor = getOAuth2Accessor(gadgetUri.toString(), serviceName,
					securityToken.getViewerId(), scope, container);
		return addModuleOverrides(persistedAccessor, securityToken, arguments, gadgetUri, specInfo);
	}
	
	private DominoOAuth2Accessor addModuleOverrides(DominoOAuth2Accessor accessor, SecurityToken securityToken, OAuth2Arguments arguments, Uri gadgetUri, OAuth2SpecInfo specInfo) 
			throws OAuth2RequestException {
		DominoOAuth2Accessor mergedAccessor = new BasicDominoOAuth2Accessor(accessor);
		mergedAccessor.setContainer(securityToken.getContainer());
		if (accessor.isAllowModuleOverrides()) {
			final String specAuthorizationUrl = specInfo.getAuthorizationUrl();
			final String specTokenUrl = specInfo.getTokenUrl();

			if (!StringUtils.isBlank(specAuthorizationUrl)) {
				mergedAccessor.setAuthorizationUrl(specAuthorizationUrl);
			}
			if (!StringUtils.isBlank(specTokenUrl)) {
				mergedAccessor.setTokenUrl(specTokenUrl);
			}
		}
		return mergedAccessor;
	}

	private OAuth2SpecInfo lookupSpecInfo(final SecurityToken securityToken,
			final OAuth2Arguments arguments, final Uri gadgetUri) throws OAuth2RequestException {
		log.entering(CLASS, "lookupSpecInfo", new Object[] { securityToken, arguments, gadgetUri });

		final GadgetSpec spec = this.findSpec(securityToken, arguments, gadgetUri);
		final OAuth2Spec oauthSpec = spec.getModulePrefs().getOAuth2Spec();
		if (oauthSpec == null) {
			throw new OAuth2RequestException(OAuth2Error.LOOKUP_SPEC_PROBLEM,
					"Failed to retrieve OAuth URLs, spec for gadget " + securityToken.getAppUrl()
					+ " does not contain OAuth element.", null);
		}
		final OAuth2Service service = oauthSpec.getServices().get(arguments.getServiceName());
		if (service == null) {
			throw new OAuth2RequestException(OAuth2Error.LOOKUP_SPEC_PROBLEM,
					"Failed to retrieve OAuth URLs, spec for gadget does not contain OAuth service "
							+ arguments.getServiceName() + ".  Known services: "
							+ Joiner.on(',').join(oauthSpec.getServices().keySet()) + '.', null);
		}

		String authorizationUrl = null;
		final EndPoint authorizationUrlEndpoint = service.getAuthorizationUrl();
		if (authorizationUrlEndpoint != null) {
			authorizationUrl = authorizationUrlEndpoint.url.toString();
		}

		String tokenUrl = null;
		final EndPoint tokenUrlEndpoint = service.getTokenUrl();
		if (tokenUrlEndpoint != null) {
			tokenUrl = tokenUrlEndpoint.url.toString();
		}

		final OAuth2SpecInfo ret = new OAuth2SpecInfo(authorizationUrl, tokenUrl, service.getScope());

		log.exiting(CLASS, "lookupSpecInfo", ret);

		return ret;
	}

	private GadgetSpec findSpec(final SecurityToken securityToken, final OAuth2Arguments arguments,
			final Uri gadgetUri) throws OAuth2RequestException {
		final String method = "findSpec";
		log.entering(CLASS, method, new Object[] { arguments, gadgetUri });

		GadgetSpec ret;

		try {
			final GadgetContext context = new OAuth2GadgetContext(securityToken, arguments, gadgetUri);
			ret = this.specFactory.getGadgetSpec(context);
		} catch (final GadgetException e) {
			log.logp(Level.WARNING, CLASS, method, "Error finding GadgetContext " + gadgetUri.toString(), e);
			throw new OAuth2RequestException(OAuth2Error.GADGET_SPEC_PROBLEM, gadgetUri.toString(), e);
		}

		// this is cumbersome in the logs, just return whether or not it's null
		if (ret == null) {
			log.exiting(CLASS, method, null);
		} else {
			log.exiting(CLASS, method, "non-null spec omitted from logs");
		}

		return ret;
	}

	private DominoOAuth2Store getOAuth2Store(String container) throws GadgetException {
		final String method = "getOAuth2Store";
		ContainerExtPoint extPoint = manager.getExtPoint(container);
		if(extPoint != null) {
			try {
				return extPoint.getContainerOAuth2Store();
			} catch (ContainerExtPointException e) {
				log.logp(Level.WARNING, CLASS, method, "There was an error getting the OAuth2Store for container " + container, e);
				throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, 
						"There was an error getting the OAuth2Store for container " + container, e);
			}
		} else {
			log.logp(Level.WARNING, CLASS, method, "There was no ContainerExtPoint for container {0}.", new Object[] {container});
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, "No ContainerExtPoint for container " + container);
		}
	}

	/**
	 * Removes an OAuth 2 accessor from the store.
	 * @param accessor The accessor to remove.
	 * @throws GadgetException Thrown if there is an issue removing the accessor.
	 */
	public void removeOAuth2Accessor(DominoOAuth2Accessor accessor) throws GadgetException {
		synchronized (accessorStore) {
			accessorStore.remove(generateKey(accessor));	
		}
	}

	/**
	 * Stores an OAuth 2 accessor in the store.
	 * @param accessor The accessor to store.
	 * @throws GadgetException Thrown if there is an issue storing the accessor.
	 */
	public void storeOAuth2Accessor(DominoOAuth2Accessor accessor) throws GadgetException {
		synchronized (accessorStore) {
			accessorStore.put(generateKey(accessor), accessor);	
		}
	}

	/**
	 * Removes an OAuth 2 accessor from the store. 
	 * @param accessor The accessor to remove.
	 * @throws GadgetException Thrown if there is an issue removing the accessor.
	 */
	public void removeAccessToken(DominoOAuth2Accessor accessor) throws GadgetException {
		synchronized (accessTokenStore) {
			accessTokenStore.remove(generateKey(accessor));
		}
	}

	/**
	 * Removes an OAuth 2 refresh token from the store.
	 * @param accessor The accessor containing the refresh token to remove.
	 * @throws GadgetException Thrown is there is an issue removing the refresh token.
	 */
	public void removeRefreshToken(DominoOAuth2Accessor accessor) throws GadgetException {
		synchronized (refreshTokenStore) {
			refreshTokenStore.remove(generateKey(accessor));
		}
	}

	private String generateKey(DominoOAuth2Accessor accessor) throws GadgetException {
		return generateKey(accessor.getContainer(), accessor.getServiceName(), accessor.getScope(), accessor.getUser());
	}

	private String generateKey(DominoOAuth2CallbackState state) throws GadgetException {
		return generateKey(state.getContainer(), state.getServiceName(), state.getScope(), state.getUser());
	}

	private String generateKey(String container, OAuth2Token token) throws GadgetException {
		return generateKey(container, token.getServiceName(), token.getScope(), token.getUser());
	}

	private String generateKey(String container, String serviceName, String scope, String user) throws GadgetException {
		if(container == null || serviceName == null || user == null) {
			throw new GadgetException(GadgetException.Code.OAUTH_STORAGE_ERROR, "Invalid key parameters, container: " + container + " user: " + user + " service name: " + serviceName);
		}
		return container + ":" + serviceName + ":" + scope + ":" + user;
	}

	/**
	 * Creates a new OAuth 2 token that can be used as a refresh or access token.
	 * @return A new OAuth 2 token.
	 */
	public OAuth2Token createToken() {
		return new OAuth2TokenPersistence(this.encrypter);
	}

	/**
	 * Stores an OAuth 2 refresh token in the store.
	 * @param container The container the refresh token belongs to.
	 * @param token The token to store.
	 * @throws GadgetException Thrown if there is an issue storing the refresh token.
	 */
	public void storeRefreshToken(String container, OAuth2Token token) throws GadgetException {
		synchronized (refreshTokenStore) {
			refreshTokenStore.put(generateKey(container, token), token);
		}
	}

	/**
	 * Stores an OAuth 2 access token in the store.
	 * @param container The container the access token belongs to.
	 * @param token The token to store.
	 * @throws GadgetException Thrown if there is an issue storing the access token.
	 */
	public void storeAccessToken(String container, OAuth2Token token) throws GadgetException {
		synchronized (accessTokenStore) {
			accessTokenStore.put(generateKey(container, token), token);
		}
	}


	/**
	 * Gets an OAuth 2 accessor.  This method should only be called if you are sure the token is already in the store.
	 * It will not consider any OAuth2 information from within the gadget.
	 * @param state The OAuth 2 callback state information.
	 * @return An OAuth 2 accessor.
	 * @throws GadgetException Thrown if there is a problem retrieving the accessor.
	 */
	public DominoOAuth2Accessor getOAuth2Accessor(DominoOAuth2CallbackState state) throws GadgetException {
		DominoOAuth2Accessor ret = accessorStore.get(generateKey(state));
		if (ret == null || !ret.isValid()) {
			final DominoOAuth2Client client = getClient(state);
			if (client == null) {
				throw new GadgetException(Code.OAUTH_STORAGE_ERROR, 
						"Could not find OAuth2 client information where container = " + state.getContainer() + 
						", user = " + state.getUser() + ", serviceName = " + state.getServiceName() + ", and scope = " + state.getScope() + ".");
			} else {
				ret = createAccessor(state, client);
				this.storeOAuth2Accessor(ret);
			}
		}
		return ret;
	}
	
	private DominoOAuth2Client getClient(DominoOAuth2CallbackState state) throws GadgetException {
		return getClient(state.getUser(), state.getServiceName(), state.getContainer(), 
				state.getScope(), state.getGadgetUri());
	}
	
	private DominoOAuth2Client getClient(String user, String serviceName, String container, String scope, 
			String gadgetUri) throws GadgetException {
		final String method = "getClient";
		DominoOAuth2Store store = getOAuth2Store(container);
		if(store == null) {
			log.logp(Level.WARNING, CLASS, method, 
					"Could not find an OAuth2 store for the container {0}, returning an error accessor.", 
					new Object[]{container});
			return null;
		}
		final DominoOAuth2Client client = store.getClient(user, serviceName, container, scope, gadgetUri);

		if (client == null) {
			log.logp(Level.WARNING, CLASS, method, 
					"Could not find OAuth2 client information where container = {0}, user = {1}, serviceName = {2}, and scope = {3}.", 
					new Object[]{container, user, serviceName, scope});
			return null;
		}
		return client;
	}
	
	private DominoOAuth2Accessor createAccessor(DominoOAuth2CallbackState state, DominoOAuth2Client client) 
			throws GadgetException {
		return createAccessor(state.getGadgetUri(), state.getServiceName(), state.getUser(), state.getScope(), state.getContainer(),
				client);
	}
	
	private DominoOAuth2Accessor createAccessor(String gadgetUri, String serviceName, String user, String scope, String container, 
			DominoOAuth2Client client) throws GadgetException {
		final OAuth2Token accessToken = this.getAccessToken(gadgetUri, serviceName, user, scope, container);
		final OAuth2Token refreshToken = this.getRefreshToken(gadgetUri, serviceName, user, scope, container);
		String authType = client.getClientAuthenticationType() == null ? null : client.getClientAuthenticationType().toString();
		final BasicDominoOAuth2Accessor newAccessor = new BasicDominoOAuth2Accessor(gadgetUri, serviceName,
				user, scope, client.isAllowModuleOverride(), this.stateCrypter, this.globalRedirectUri,
				this.authority, this.contextRoot, container);
		newAccessor.setAccessToken(accessToken);
		newAccessor.setAuthorizationUrl(client.getAuthorizationUrl());
		newAccessor.setClientAuthenticationType(authType);
		newAccessor.setAuthorizationHeader(client.useAuthorizationHeader());
		newAccessor.setUrlParameter(client.useUrlParameter());
		newAccessor.setClientId(client.getClientId());
		newAccessor.setClientSecret(client.getClientSecret().getBytes());
		newAccessor.setGrantType(client.getGrantType().toString());
		newAccessor.setRefreshToken(refreshToken);
		newAccessor.setTokenUrl(client.getTokenUrl());
		//TODO do we want to support other types?
		newAccessor.setType(OAuth2Accessor.Type.CONFIDENTIAL);
		newAccessor.setAllowedDomains(new String[0]);
		return newAccessor;
	}
	
	/**
	 * Gets an OAuth 2 access token.
	 * @param gadgetUri The gadget URI.
	 * @param serviceName The OAuth 2 service name.
	 * @param user The user.
	 * @param scope The OAuth 2 scope for the service.
	 * @param container The container.
	 * @return An OAuth 2 access token.
	 * @throws GadgetException Thrown if there is a problem retrieving the access token.
	 */
	public OAuth2Token getAccessToken(String gadgetUri, String serviceName, String user, String scope, String container) throws GadgetException {
		return accessTokenStore.get(generateKey(container, serviceName, scope, user));
	}
	
	/**
	 * Gets an OAuth 2 refresh token.
	 * @param gadgetUri The gadget URI.
	 * @param serviceName The OAuth 2 service name.
	 * @param user The user.
	 * @param scope The OAuth 2 scope for the service.
	 * @param container The container.
	 * @return An OAuth 2 refresh token.
	 * @throws GadgetException Thrown if there is a problem retrieving the refresh token.
	 */
	public OAuth2Token getRefreshToken(String gadgetUri, String serviceName, String user, String scope, String container) throws GadgetException {
		return refreshTokenStore.get(generateKey(container, serviceName, scope, user));
	}

	private DominoOAuth2Accessor getOAuth2Accessor(String gadgetUri, String serviceName,
			String user, String scope, String container) throws GadgetException {
		final DominoOAuth2CallbackState state = new DominoOAuth2CallbackState(this.stateCrypter);
		state.setGadgetUri(gadgetUri);
		state.setServiceName(serviceName);
		state.setUser(user);
		state.setScope(scope);
		state.setContainer(container);
		return getOAuth2Accessor(state);
	}

}
