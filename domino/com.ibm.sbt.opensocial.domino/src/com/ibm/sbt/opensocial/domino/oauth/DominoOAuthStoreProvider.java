package com.ibm.sbt.opensocial.domino.oauth;

import java.util.List;

import com.google.caja.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.sbt.services.endpoints.OAuthEndpoint;
import com.ibm.xsp.model.domino.DominoUtils;

/**
 * Provides the OAuth 1.0a store to Shindig.
 *
 */
@Singleton
public class DominoOAuthStoreProvider implements Provider<DominoOAuthStore> {

	private DominoOAuthStore oauthStore;

	@Inject
	public DominoOAuthStoreProvider(ContainerExtPointManager manager) {
		oauthStore = new DominoOAuthStore(manager);
	}

	public DominoOAuthStore get() {
		return oauthStore;
	}

	private OAuthEndpoint createSmartCloudEndpoint() {
		OAuthEndpoint endpoint = new OAuthEndpoint();
		endpoint.setServiceName("SmartCloud");
		endpoint.setConsumerKey(DominoUtils.getEnvironmentString("playground-os-sc-key"));
		endpoint.setConsumerSecret(DominoUtils.getEnvironmentString("playground-os-sc-secret"));
		endpoint.setSignatureMethod("PLAINTEXT");
		return endpoint;
	}

	private List<OAuthEndpoint> getEndpoints() {
		List<OAuthEndpoint> endpoints = Lists.newArrayList();
		endpoints.add(createSmartCloudEndpoint());
		return endpoints;
	}

}
