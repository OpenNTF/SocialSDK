package nsf.playground.environments;

import java.io.IOException;

import javax.faces.context.FacesContext;

import nsf.playground.beans.DataAccess;


import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironmentFactory;
import com.ibm.xsp.util.ManagedBeanUtil;

public class EnvironmentFactory extends SBTEnvironmentFactory {

	public EnvironmentFactory() {
	}
	
	public SBTEnvironment getEnvironment(String name) {
		try {
			DataAccess acc = (DataAccess) ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), DataAccess.BEAN_NAME);
			return acc.getEnvironment(name);
		} catch(IOException ex) {}
		return null;
	}
}
