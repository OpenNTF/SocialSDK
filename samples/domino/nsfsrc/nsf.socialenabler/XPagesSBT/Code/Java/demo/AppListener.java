package demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.shindig.config.ContainerConfigException;

import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener;

public class AppListener implements ApplicationListener {
	
	private ContainerExtPoint extPoint = new SocialEnablerContainerExtPoint();

	public void applicationCreated(ApplicationEx appEx) {
		List<ContainerExtPoint> extPoints = new ArrayList<ContainerExtPoint>();
		extPoints.add(extPoint);
		try {
			ContainerExtPointManager.registerContainers(extPoints);
		} catch (ContainerConfigException e) {
			e.printStackTrace();
		}
	}

	public void applicationDestroyed(ApplicationEx appEx) {
		List<ContainerExtPoint> extPoints = new ArrayList<ContainerExtPoint>();
		extPoints.add(extPoint);
		try {
			ContainerExtPointManager.unregisterContainers(extPoints);
		} catch (ContainerConfigException e) {
			e.printStackTrace();
		}
	}
}
