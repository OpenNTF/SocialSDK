package com.ibm.sbt.opensocial.domino.oauth;

import org.apache.shindig.common.servlet.Authority;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;

/**
 * Provides the OAuth 1.0a store to Shindig.
 *
 */
@Singleton
public class DominoOAuthStoreProvider implements Provider<InternalDominoOAuthStore> {

	private static final String OAUTH_SIGNING_KEY_FILE = "shindig.signing.key-file";
	private static final String OAUTH_SIGNING_KEY_NAME = "shindig.signing.key-name";
	private static final String OAUTH_CALLBACK_URL = "shindig.signing.global-callback-url";

	private InternalDominoOAuthStore oauthStore;

	@Inject
	public DominoOAuthStoreProvider(ContainerExtPointManager manager, @Named(OAUTH_SIGNING_KEY_FILE) String signingKeyFile,
			@Named(OAUTH_SIGNING_KEY_NAME) String signingKeyName,
			@Named(OAUTH_CALLBACK_URL) String defaultCallbackUrl,
			Authority authority) {
		oauthStore = new InternalDominoOAuthStore(manager, authority, defaultCallbackUrl);
	}

	public InternalDominoOAuthStore get() {
		return oauthStore;
	}
}
