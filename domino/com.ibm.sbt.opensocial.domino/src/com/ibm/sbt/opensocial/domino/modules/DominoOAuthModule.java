package com.ibm.sbt.opensocial.domino.modules;

import java.util.List;
import java.util.logging.Logger;

import org.apache.shindig.common.Nullable;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.gadgets.GadgetSpecFactory;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.oauth.OAuthModule;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.apache.shindig.gadgets.oauth2.OAuth2FetcherConfig;
import org.apache.shindig.gadgets.oauth2.OAuth2Module;
import org.apache.shindig.gadgets.oauth2.OAuth2Request;
import org.apache.shindig.gadgets.oauth2.OAuth2RequestParameterGenerator;
import org.apache.shindig.gadgets.oauth2.handler.AuthorizationEndpointResponseHandler;
import org.apache.shindig.gadgets.oauth2.handler.ClientAuthenticationHandler;
import org.apache.shindig.gadgets.oauth2.handler.GrantRequestHandler;
import org.apache.shindig.gadgets.oauth2.handler.ResourceRequestHandler;
import org.apache.shindig.gadgets.oauth2.handler.TokenEndpointResponseHandler;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Cache;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Encrypter;
import org.apache.shindig.gadgets.oauth2.persistence.OAuth2Persister;
import org.apache.shindig.gadgets.oauth2.persistence.sample.OAuth2PersistenceModule;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.util.Modules;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Cache;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Persister;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2Request;
import com.ibm.sbt.opensocial.domino.oauth.DominoOAuth2TokenStore;
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
		bind(DominoOAuth2TokenStore.class).toProvider(DominoOAuth2TokenStoreProvider.class);
		install(Modules.override(new OAuth2Module()).with(new OAuth2ModuleOverride()));
		install(Modules.override(new OAuth2PersistenceModule()).with(new OAuth2PersistenceModuleOverride()));
	}

	private static class OAuthModuleOverride extends AbstractModule {
		@Override
		protected void configure() {
			bind(OAuthStore.class).toProvider(DominoOAuthStoreProvider.class);
		}
	}
	
	private static class OAuth2ModuleOverride extends AbstractModule {
		@Override
		protected void configure() {
			this.bind(OAuth2Request.class).toProvider(DominoOAuth2RequestProvider.class);
		}
	}
	
	private static class OAuth2PersistenceModuleOverride extends AbstractModule {
		@Override
		protected void configure() {
			bind(OAuth2Cache.class).to(DominoOAuth2Cache.class);
			bind(OAuth2Persister.class).to(DominoOAuth2Persister.class);
		}
	}
	
	public static class DominoOAuth2RequestProvider implements Provider<OAuth2Request> {
		
		private final DominoOAuth2TokenStore store;
		private final HttpFetcher fetcher;
		private final List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers;
		private final List<ClientAuthenticationHandler> clientAuthenticationHandlers;
		private final List<GrantRequestHandler> grantRequestHandlers;
		private final List<ResourceRequestHandler> resourceRequestHandlers;
		private final List<TokenEndpointResponseHandler> tokenEndpointResponseHandlers;
		private final boolean sendTraceToClient;
		private final OAuth2RequestParameterGenerator requestParameterGenerator;
		private final Logger log;

		@Inject
		public DominoOAuth2RequestProvider(final DominoOAuth2TokenStore store, final HttpFetcher fetcher,
	            final List<AuthorizationEndpointResponseHandler> authorizationEndpointResponseHandlers,
	            final List<ClientAuthenticationHandler> clientAuthenticationHandlers,
	            final List<GrantRequestHandler> grantRequestHandlers,
	            final List<ResourceRequestHandler> resourceRequestHandlers,
	            final List<TokenEndpointResponseHandler> tokenEndpointResponseHandlers,
	            @Named(OAuth2Module.SEND_TRACE_TO_CLIENT)
	            final boolean sendTraceToClient,
	            final OAuth2RequestParameterGenerator requestParameterGenerator,
	            final Logger log){
			this.store = store;
			this.fetcher = fetcher;
			this.authorizationEndpointResponseHandlers = authorizationEndpointResponseHandlers;
			this.clientAuthenticationHandlers = clientAuthenticationHandlers;
			this.grantRequestHandlers = grantRequestHandlers;
			this.resourceRequestHandlers = resourceRequestHandlers;
			this.tokenEndpointResponseHandlers = tokenEndpointResponseHandlers;
			this.sendTraceToClient = sendTraceToClient;
			this.requestParameterGenerator = requestParameterGenerator;
			this.log = log;
		}
		
		@Override
		public OAuth2Request get() {
			return new DominoOAuth2Request(store, fetcher, authorizationEndpointResponseHandlers, 
					clientAuthenticationHandlers, grantRequestHandlers, resourceRequestHandlers, 
					tokenEndpointResponseHandlers, sendTraceToClient, requestParameterGenerator, 
					sendTraceToClient, log);
		}
		
	}
	
	@Singleton
	public static class DominoOAuth2TokenStoreProvider implements Provider<DominoOAuth2TokenStore> {
		
		private DominoOAuth2TokenStore store;
		
		@Inject
		public DominoOAuth2TokenStoreProvider(final @Named(OAuth2FetcherConfig.OAUTH2_STATE_CRYPTER) BlobCrypter stateCrypter,
				final @Named("shindig.oauth2.global-redirect-uri") String globalRedirectUri,
				final GadgetSpecFactory specFactory, final ContainerExtPointManager extPointManager, Logger log,
				final OAuth2Encrypter encrypter, Authority authority, 
				@Nullable @Named("shindig.contextroot") String contextRoot) {
			this.store = new DominoOAuth2TokenStore(specFactory, extPointManager, log, encrypter, authority, 
					contextRoot, stateCrypter, globalRedirectUri);
		}
		
		@Override
		public DominoOAuth2TokenStore get() {
			return store;
		}
		
	}
}
