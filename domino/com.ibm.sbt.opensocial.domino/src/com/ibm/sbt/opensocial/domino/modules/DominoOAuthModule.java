package com.ibm.sbt.opensocial.domino.modules;

import org.apache.shindig.gadgets.oauth.OAuthModule;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.gadgets.oauth2.OAuth2Module;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Cache;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Persister;
import org.apache.shindig.gadgets.oauth2.persistence.sample.OAuth2PersistenceModule;

import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Cache;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Persister;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuthStoreProvider;

/**
 * Provides implementations for OAuth 1.0 and 2.0 stores in the Playground.
 * Shindig will retrieve and store keys and secrets through these modules.
 *
 */
public class DominoOAuthModule extends AbstractModule {

	@Override
	protected void configure() {
		// OAuth1.0a
		install(Modules.override(new OAuthModule()).with(new OAuthModuleOverride()));

		// OAuth2
		install(new OAuth2Module());
		install(Modules.override(new OAuth2PersistenceModule()).with(new OAuth2PersistenceModuleOverride()));
	}

	private static class OAuthModuleOverride extends AbstractModule {
		@Override
		protected void configure() {
			bind(OAuthStore.class).toProvider(DominoOAuthStoreProvider.class);
		}
	}


	private static class OAuth2PersistenceModuleOverride extends AbstractModule {
		@Override
		protected void configure() {
			bind(OAuth2Cache.class).to(DominoOAuth2Cache.class);
			bind(OAuth2Persister.class).to(DominoOAuth2Persister.class);
		}
	}
}
