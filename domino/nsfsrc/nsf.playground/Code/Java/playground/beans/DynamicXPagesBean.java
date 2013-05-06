package playground.beans;

import com.ibm.xsp.extlib.interpreter.DynamicXPageBean;

public class DynamicXPagesBean extends DynamicXPageBean {

	public DynamicXPagesBean() {
		
	}
	
	public boolean isDynamicPage(String className) {
		return className.startsWith("xsp.playground.");
	}	
}
