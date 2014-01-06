package com.ibm.sbt.opensocial.domino.modules;

import java.util.List;

import org.apache.shindig.gadgets.oauth2.handler.OAuth2HandlerModule;
import org.apache.shindig.gadgets.oauth2.handler.TokenEndpointResponseHandler;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import com.ibm.sbt.opensocial.domino.oauth.DominoTokenAuthorizationResponseHandler;

public class DominoOAuth2HandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(Modules.override(new OAuth2HandlerModule()).with(new OAuth2HandlerModuleOverride()));
	}
	
	private class OAuth2HandlerModuleOverride extends AbstractModule {

		@Override
		protected void configure() {
		}

		@SuppressWarnings("unused")
		@Provides
		@Singleton
		public List<TokenEndpointResponseHandler> provideTokenEndpointResponseHandlers(
				final DominoTokenAuthorizationResponseHandler tokenAuthorizationResponseHandler) {
			return ImmutableList.of((TokenEndpointResponseHandler) tokenAuthorizationResponseHandler);
		}

	}

}
