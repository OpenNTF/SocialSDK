package com.ibm.sbt.opensocial.domino.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.uri.Uri.UriException;
import org.apache.shindig.config.ContainerConfigException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;

import com.google.caja.util.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.ibm.sbt.opensocial.domino.internal.OpenSocialPlugin;

/**
 * Class that manages container extension points.  If an application wishes to add a container to the 
 * OpenSocial implementation they first need to implement {@link com.ibm.sbt.opensocial.domino.container.ContainerExtPoint}.
 * Applications can contribute information about their containers via two mechanisms.
 * 
 * <ol>
 * 	<li>Via the OSGi extension point com.ibm.sbt.opensocial.domino.container.</li>
 *  <li>Via {@link #registerContainer(ContainerExtPoint)}.  When using {@link #registerContainer(ContainerExtPoint)}
 *  make sure you also call {@link #unregisterContainer(ContainerExtPoint)} when the app is terminated.</li>
 * </ol>
 *
 */
@Singleton
public class ContainerExtPointManager {
	private static final String EXT_PT_ID = "com.ibm.sbt.opensocial.domino.container";
	private static final String CLASS_ATR = "class";
	private static final String CLASS = ContainerExtPointManager.class.getName();
	private static Map<String, ContainerExtPoint> containers = Maps.newConcurrentMap();
	
	private static List<ContainerExtPointListener> listeners = 
			Collections.synchronizedList(new ArrayList<ContainerExtPointListener>());
	private IExtensionRegistry registry;
	private Logger log;
	
	private IRegistryChangeListener registryChangeListener = new IRegistryChangeListener() {
		@Override
		public void registryChanged(IRegistryChangeEvent event) {
			IExtensionDelta[] deltas = event.getExtensionDeltas(OpenSocialPlugin.ID, EXT_PT_ID);
			for(int i = 0; i < deltas.length; i++) {
				IExtensionDelta delta = deltas[i];
				IExtension ext = delta.getExtension();
				modifyContainers(ext.getConfigurationElements(), delta.getKind() == IExtensionDelta.ADDED);
			}
		}
	};
	
	/**
	 * Creates a new container extension point manager.
	 */
	public ContainerExtPointManager(IExtensionRegistry registry, Logger logger) {
		this.registry = registry;
		this.log = logger;
		loadOSGiExtPoints();
	}


	private void loadOSGiExtPoints() {
		registry.addRegistryChangeListener(registryChangeListener, OpenSocialPlugin.ID);
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXT_PT_ID);
		modifyContainers(elements, true);
	}


	private void modifyContainers(IConfigurationElement[] elements, boolean add) {
		final String method = "modifyContainers";
		List<ContainerExtPoint> containers = Lists.newArrayList();
		for(int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			try {
				Object containerObject = element.createExecutableExtension(CLASS_ATR);
				if(containerObject instanceof ContainerExtPoint) {
					containers.add((ContainerExtPoint)containerObject);
				}
			} catch (CoreException e) {
				log.logp(Level.WARNING, CLASS, method, 
						"Error adding container extension point from OSGi extension point.", e);
			}
		}
		if(add) {
			try {
				registerContainers(containers);
			} catch (ContainerConfigException e) {
				log.logp(Level.WARNING, CLASS, method, "Error registering containers from extension point.", e);
			}
		} else {
			try {
				unregisterContainers(containers);
			} catch (ContainerConfigException e) {
				log.logp(Level.WARNING, CLASS, method, "Error un-registering containers from extension point.", e);
			}
		}
	}
	
	/**
	 * Gets a unmodifiable copy of all the container extension points.
	 * @return All the container extension points.  The key to the map is the container name.
	 */
	public Map<String, ContainerExtPoint> getExtPoints() {
		return Collections.unmodifiableMap(containers);
	}
	
	/**
	 * Gets a container extension point.
	 * @param container The container name.
	 * @return A container extension point.
	 */
	public ContainerExtPoint getExtPoint(String container) {
		return containers.get(container);
	}
	
	/**
	 * Registers containers with the OpenSocial implementation.
	 * @param containers The container to register.
	 * @throws ContainerConfigException Thrown if there is a container extension point that is not valid.
	 */
	public static void registerContainers(Collection<ContainerExtPoint> containers) throws ContainerConfigException {
		for(ContainerExtPoint extPoint : containers) {
			try {
				validateContainerId(extPoint.getId());
				ContainerExtPointManager.containers.put(extPoint.getId(), extPoint);
			} catch (ContainerExtPointException e) {
				throw new ContainerConfigException(e);
			}
		}
		synchronized(listeners) {
			for(ContainerExtPointListener listener : listeners) {
				listener.added(containers);
			}
		}
	}
	
	/**
	 * Validates that the container IDs. Container IDs MUST
	 * <ul>
	 * 	<li>be URL encoded</li>
	 * <li>NOT contain a colon</li>
	 * </ul>
	 * This is due to limitations in Shindig.  We enforce this here to make sure consumers
	 * do not harm themselves and then be left scratching their heads when things go wrong.
	 * Container IDs are part of the security token in Shindig and there are places in Shindig where
	 * the assumption is made that the security token is safe to place in a URL.  So if the container ID
	 * has a space in it for example we will get errors because Shindig does not try to URL encoded the 
	 * security token.  Container IDs cannot container colons because Shindig uses colons as separators
	 * for the different parts of the security token.  If a container ID contains a colon then the code
	 * in Shindig that parses the security token will be confused by the extra colons.
	 * @param id The container ID to validate.
	 * @throws ContainerConfigException  Thrown when the container ID is not valid.
	 */
	private static void validateContainerId(String id) throws ContainerConfigException {
		try{
			//This is an easy way to make sure the id is URL encoded, if parse throws an error 
			//than there is a problem.
			Uri.parse("http://foo/" + id);
		} catch(UriException e) {
			throw new ContainerConfigException("Container IDs must be URL encoded. ID: " + id, e);
		}
		if(id.contains(":")) {
			throw new ContainerConfigException("Container IDs cannot contain colons. ID: " + id);
		}
	}


	/**
	 * Unregisters containers with the OpenSocial implementation.
	 * @param containers The container to unregister.
	 * @throws ContainerConfigException Thrown if there is an error un-registering the containers.
	 */
	public static void unregisterContainers(Collection<ContainerExtPoint> containers) throws ContainerConfigException {
		for(ContainerExtPoint extPoint : containers) {
			try {
				ContainerExtPointManager.containers.remove(extPoint.getId());
			} catch (ContainerExtPointException e) {
				throw new ContainerConfigException(e);
			}
		}
		synchronized(listeners) {
			for(ContainerExtPointListener listener : listeners) {
				listener.removed(containers);
			}
		}
	}
	
	/**
	 * Adds an extension point listener to be notified when new extension points are added and removed.
	 * @param listener The listener to be notified.
	 */
	public void addExtensionPointListener(ContainerExtPointListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes an extension point listener.
	 * @param listener The listener to be removed.
	 */
	public void removeExtensionPointListener(ContainerExtPointListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Listener to be notified when new container extension points are added or removed.
	 *
	 */
	public interface ContainerExtPointListener {
		
		/**
		 * Called when new container extension points are added.
		 * @param extPoints The new extension points that were added. 
		 */
		public void added(Collection<ContainerExtPoint> extPoints);
		
		/**
		 * Called when container extension points are removed.
		 * @param extPoints The extension points that were removed.
		 */
		public void removed(Collection<ContainerExtPoint> extPoints);
	}
}
