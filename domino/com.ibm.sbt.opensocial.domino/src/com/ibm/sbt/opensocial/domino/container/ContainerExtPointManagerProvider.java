package com.ibm.sbt.opensocial.domino.container;

import java.util.logging.Logger;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Provides the {@link com.ibm.sbt.opensocial.domino.container.ContatinerExtPointManager}.
 *
 */
@Singleton
public class ContainerExtPointManagerProvider implements Provider<ContainerExtPointManager> {

	private ContainerExtPointManager manager;
	
	/**
	 * Creates a new container extension point manager provider.
	 */
	@Inject
	public ContainerExtPointManagerProvider(IExtensionRegistry registry, Logger log) {
		manager = new ContainerExtPointManager(registry, log);
	}
	
	@Override
	public ContainerExtPointManager get() {
		return manager;
	}
}
