package com.ibm.sbt.opensocial.domino.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			registerContainers(containers);
		} else {
			unregisterContainers(containers);
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
	 */
	public static void registerContainers(Collection<ContainerExtPoint> containers) {
		for(ContainerExtPoint extPoint : containers) {
			ContainerExtPointManager.containers.put(extPoint.getId(), extPoint);
		}
		synchronized(listeners) {
			for(ContainerExtPointListener listener : listeners) {
				listener.added(containers);
			}
		}
	}
	
	/**
	 * Unregisters containers with the OpenSocial implementation.
	 * @param containers The container to unregister.
	 */
	public static void unregisterContainers(Collection<ContainerExtPoint> containers) {
		for(ContainerExtPoint extPoint : containers) {
			ContainerExtPointManager.containers.remove(extPoint.getId());
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
