package com.ibm.sbt.opensocial.domino.config;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shindig.common.Nullable;
import org.apache.shindig.config.ContainerConfig;
import org.apache.shindig.config.ContainerConfigException;
import org.apache.shindig.config.JsonContainerConfig;
import org.apache.shindig.expressions.Expressions;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager.ContainerExtPointListener;

/**
 * Contributes containers from extension points.
 */
@Singleton
public class ExtensionPointContainerConfig extends JsonContainerConfig {
	private static final String CLASS = ExtensionPointContainerConfig.class.getName();
	
	private Logger log;
	
	private ContainerExtPointListener listener = new ContainerExtPointListener() {
		@Override
		public void added(Collection<ContainerExtPoint> extPoints) {
			modifyContainerConfigs(extPoints, true);
		}

		@Override
		public void removed(Collection<ContainerExtPoint> extPoints) {
			modifyContainerConfigs(extPoints, false);
		}
	};

	/**
	 * Creates a new extension point container config.
	 * @param containers The default set of containers.
	 * @param host The host.
	 * @param port The port.
	 * @param contextRoot The context root of the web app.
	 * @param expressions Expressions to be evaluated.
	 * @param extPointManager The container extension point manager.
	 * @param log The logger.
	 * @throws ContainerConfigException Thrown if a configuration is invalid.
	 */
	@Inject
	public ExtensionPointContainerConfig(@Named("shindig.containers.default") String containers,
            @Nullable @Named("shindig.host") String host,
            @Nullable @Named("shindig.port") String port,
            @Nullable @Named("shindig.contextroot") String contextRoot,
            Expressions expressions, ContainerExtPointManager extPointManager,
            Logger log)
			throws ContainerConfigException {
		super(containers, host, port, contextRoot, expressions);
		this.log = log;
		modifyContainerConfigs(extPointManager.getExtPoints().values(), true);
		extPointManager.addExtensionPointListener(listener);
	}

	/**
	 * Modifies the containers.
	 * @param containers The collections of containers to either add or remove.
	 * @param add True to add the collection of containers, false to remove them.
	 */
	private void modifyContainerConfigs(Collection<ContainerExtPoint> containers, boolean add) {
		final String method = "modifyContainerConfigs";
		if(!containers.isEmpty()) {
			Transaction transaction = newTransaction();
			for(ContainerExtPoint extPoint : containers) {
				if(add) {
					Map<String, Object> containerProps = extPoint.getContainerConfig().getProperties();
					if(!containerProps.containsKey(ContainerConfig.CONTAINER_KEY)) {
						containerProps.put(ContainerConfig.CONTAINER_KEY, 
								new ImmutableList.Builder<String>().add(extPoint.getId()).build());
					}
					transaction.addContainer(containerProps);
				} else {
					transaction.removeContainer(extPoint.getId());
				}
			}
			try {
				transaction.commit();
			} catch (ContainerConfigException e) {
				log.logp(Level.WARNING, CLASS, method, "Error while committing container modifications.", e);
			}
		}
	}
}