package com.ibm.sbt.opensocial.domino.config;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shindig.auth.BlobCrypterSecurityTokenCodec;
import org.apache.shindig.common.Nullable;
import org.apache.shindig.common.crypto.Crypto;
import org.apache.shindig.common.util.CharsetUtil;
import org.apache.shindig.config.ContainerConfig;
import org.apache.shindig.config.ContainerConfigException;
import org.apache.shindig.config.JsonContainerConfig;
import org.apache.shindig.expressions.Expressions;

import com.google.caja.util.Maps;
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
	private static final String DOMINO_CONTAINER = "domino";
	
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
		addSecurityTokenKeyToDominoContainer();
		modifyContainerConfigs(extPointManager.getExtPoints().values(), true);
		extPointManager.addExtensionPointListener(listener);
	}

	/**
	 * Adds a security token key to the domino container.  Since the domino container
	 * is defined in a static file (domino-container.js) we cannot specify a key there
	 * so when this class is started we get the config for the domino container and add
	 * the key.
	 */
	private void addSecurityTokenKeyToDominoContainer() {
		final String method = "addSecurityTokenToDominoContainer";
		Map<String, Object> domino = this.getProperties(DOMINO_CONTAINER);
		if(domino != null) {
			//We need to create a new map because getProperties returns an unmodifiable map
			Map<String, Object> config = Maps.newHashMap(domino);
			config.put(BlobCrypterSecurityTokenCodec.SECURITY_TOKEN_KEY, 
					CharsetUtil.newUtf8String(Crypto.getRandomBytes(20)));
			try {
				newTransaction().addContainer(config).commit();
			} catch (ContainerConfigException e) {
				log.logp(Level.WARNING, CLASS, method, 
						"Error commiting new container config for domino container after adding security token key.", 
						e);
			}
		} else {
			log.logp(Level.WARNING, CLASS, method, "There was not container with the ID domino, could not add token key.");
		}
	}

	/**
	 * Modifies the containers.
	 * @param containers The collections of containers to either add or remove.
	 * @param add True to add the collection of containers, false to remove them.
	 */
	private void modifyContainerConfigs(Collection<ContainerExtPoint> containers, boolean add) {
		final String method = "modifyContainerConfigs";
		try {
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
				transaction.commit();
			}
		} catch (Exception e) {
			log.logp(Level.WARNING, CLASS, method, "Error while committing container modifications.", e);
		}
	}
}