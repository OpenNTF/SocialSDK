package playground.beans;

import java.util.Map;

import nsf.playground.jsp.JspFragment;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.javacompiler.JavaSourceClassLoader;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class JavaSnippetBean extends nsf.playground.beans.JavaSnippetBean {

	// Define this flag to support parameter processing
	// In this case, it defines a class loader per request, which might be resource consuming!
	public static boolean DYNAMIC_PAGES = false;
	
	public static boolean DEBUG = false;

	@SuppressWarnings("unchecked")
	@Override
	public Class<JspFragment> getCompiledClass(String jspClassName) throws Exception {
		JavaSourceClassLoader loader = getSourceClassLoader();
		if(loader.isCompiledFile(jspClassName)) {
			return (Class<JspFragment>)loader.loadClass(jspClassName);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized Class<JspFragment> compileSnippet(String jspClassName, String source) throws Exception {
		JavaSourceClassLoader loader = getSourceClassLoader();
		Class<JspFragment> f = getCompiledClass(jspClassName);
		if(f==null) {
			f = (Class<JspFragment>)loader.addClass(jspClassName, source);
		}
		return f;
	}

	// We don't want to share the class loader as the snippets can be modified, and
	// the parameters generate different classes
	private static final boolean SHARED_CLASS_LOADER = !DYNAMIC_PAGES;

	private JavaSourceClassLoader getSourceClassLoader() {
		Map<String,Object> scope = ExtLibUtil.getApplicationScope();
		JavaSourceClassLoader loader = SHARED_CLASS_LOADER ? (JavaSourceClassLoader)scope.get("playground.java.loader") : null;
		if(loader==null) {
			synchronized(this) {
				loader = (JavaSourceClassLoader)scope.get("playground.java.loader");
				if(loader==null) {
					String[] bundles = new String[] {
						"com.ibm.commons",
						"com.ibm.commons.xml",
						"com.ibm.commons.runtime",
						"com.ibm.sbt.core",
						"com.ibm.sbt.libs",
						"com.ibm.xsp.sbtsdk",
						"com.ibm.xsp.sbtsdk.playground",
						"com.ibm.pvc.servlet",
						"com.ibm.pvc.servlet.jsp"
					};
					loader = new JavaSourceClassLoader(FacesContextEx.getCurrentInstance().getContextClassLoader(),null,bundles);
					if(SHARED_CLASS_LOADER && !DEBUG) {
						scope.put("playground.java.loader",loader);
					}
				}
			}
		}
		return loader;
	}
}
