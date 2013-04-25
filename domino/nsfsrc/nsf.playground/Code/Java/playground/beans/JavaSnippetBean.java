package playground.beans;

import java.util.Map;

import nsf.playground.jsp.JspFragment;

import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.javacompiler.JavaSourceClassLoader;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class JavaSnippetBean extends nsf.playground.beans.JavaSnippetBean {
	
	public static boolean DEBUG = false;

	@Override
	public Class<JspFragment> getCompiledClass(String jspClassName) throws Exception {
		JavaSourceClassLoader loader = getSourceClassLoader();
		if(loader.isCompiledFile(jspClassName)) {
			return (Class<JspFragment>)loader.loadClass(jspClassName);
		}
		return null;
//		return null;
	}

	@Override
	public synchronized Class<JspFragment> compileSnippet(String jspClassName, String source) throws Exception {
		JavaSourceClassLoader loader = getSourceClassLoader();
		Class<JspFragment> f = getCompiledClass(jspClassName);
		if(f==null) {
			f = (Class<JspFragment>)loader.addClass(jspClassName, source);
		}
		return f;
//		return null;
	}

	private JavaSourceClassLoader getSourceClassLoader() {
		Map<String,Object> scope = ExtLibUtil.getApplicationScope();
		JavaSourceClassLoader loader = (JavaSourceClassLoader)scope.get("playground.java.loader");
		if(loader==null) {
			synchronized(this) {
				loader = (JavaSourceClassLoader)scope.get("playground.java.loader");
				if(loader==null) {
					String[] bundles = new String[] {
						"com.ibm.commons",
						"com.ibm.commons.xml",
						"com.ibm.commons.runtime",
						"com.ibm.sbt.core",
						"com.ibm.xsp.sbtsdk",
						"org.eclipse.equinox.http.servlet",
						"com.ibm.pvc.servlet.jsp"
					};
					loader = new JavaSourceClassLoader(FacesContextEx.getCurrentInstance().getContextClassLoader(),null,bundles);
					if(!DEBUG) {
						scope.put("playground.java.loader",loader);
					}
				}
			}
		}
		return loader;
	}
}
