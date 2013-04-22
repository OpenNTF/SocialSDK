package nsf.playground.environments;

import java.io.IOException;

import javax.faces.context.FacesContext;

import nsf.playground.beans.DataAccessBean;

import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironmentFactory;
import com.ibm.xsp.util.ManagedBeanUtil;

public class EnvironmentFactory extends SBTEnvironmentFactory {

	public EnvironmentFactory() {
	}
	
	// a change
	public SBTEnvironment getEnvironment(String name) {
		try {
			DataAccessBean acc = (DataAccessBean) ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), DataAccessBean.BEAN_NAME);
			return acc.getEnvironment(name);
		} catch(IOException ex) {}
		return null;
	}
}
