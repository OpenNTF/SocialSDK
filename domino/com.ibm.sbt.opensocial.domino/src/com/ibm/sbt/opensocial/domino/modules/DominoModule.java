package com.ibm.sbt.opensocial.domino.modules;

import org.apache.shindig.auth.SecurityTokenCodec;
import org.apache.shindig.config.ContainerConfig;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.ibm.sbt.opensocial.domino.config.ExtensionPointContainerConfig;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManagerProvider;
import com.ibm.sbt.opensocial.domino.internal.DominoHttpFetcher;
import com.ibm.sbt.opensocial.domino.security.DominoSecurityTokenCodec;

/**
 * Guice module the injects default bindings for Domino.
 *
 */
public class DominoModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ContainerConfig.class).to(ExtensionPointContainerConfig.class);
		bind(SecurityTokenCodec.class).to(DominoSecurityTokenCodec.class);
		bind(ContainerExtPointManager.class).toProvider(ContainerExtPointManagerProvider.class);
		bind(IExtensionRegistry.class).toProvider(ExtensionRegistryProvider.class);
		bind(HttpFetcher.class).to(DominoHttpFetcher.class);
	}
	
	/**
	 * Simple provider class to inject instances of {@link org.eclipse.core.runtime.IExtensionRegistry}.
	 *
	 */
	@Singleton
	public static class ExtensionRegistryProvider implements Provider<IExtensionRegistry> {
		@Override
		public IExtensionRegistry get() {
			return Platform.getExtensionRegistry();
		}
	}
}


