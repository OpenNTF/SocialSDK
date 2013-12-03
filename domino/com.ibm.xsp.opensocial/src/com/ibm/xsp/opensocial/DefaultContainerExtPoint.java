package com.ibm.xsp.opensocial;

import com.ibm.sbt.opensocial.domino.config.DefaultContainerConfig;
import com.ibm.sbt.opensocial.domino.config.OpenSocialContainerConfig;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;

public abstract class DefaultContainerExtPoint implements ContainerExtPoint {
	private OpenSocialContainerConfig containerConfig;
	
	public DefaultContainerExtPoint() {
		this.containerConfig = new DefaultContainerConfig();
	}

	@Override
	public OpenSocialContainerConfig getContainerConfig() {
		return this.containerConfig;
	}

}
