package playground.beans;

import com.ibm.xsp.extlib.interpreter.DynamicXPageBean;

public class DynamicXPagesBean extends DynamicXPageBean {

	// Define this flag to support parameter processing
	// In this case, it defines a class loader per request, which might be resource consuming!
	public static boolean DYNAMIC_PAGES = false;
	
	public DynamicXPagesBean() {
		super();
	}
	
	// Create a new class loader per request to support the parsing of parameters
	protected boolean isRequestBasedClassLoader() {
		return DYNAMIC_PAGES;
	}
	
	public boolean isDynamicPage(String className) {
		return className.startsWith("xsp.playground.");
	}	
}
