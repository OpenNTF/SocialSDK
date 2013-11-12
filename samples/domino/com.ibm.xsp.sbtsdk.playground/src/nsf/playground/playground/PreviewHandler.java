package nsf.playground.playground;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.PathUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.xsp.context.DojoLibrary;
import com.ibm.xsp.context.DojoLibraryFactory;

/**
 * Base handler class for preview activities.
 * 
 * The dispatching is actually done by the PreviewServlet
 * 
 * @author priand
 */
public abstract class PreviewHandler {
	
	public static class EnvParameterProvider implements com.ibm.commons.runtime.util.ParameterProcessor.ParameterProvider {
		SBTEnvironment env;
		public EnvParameterProvider(SBTEnvironment env) {
			this.env = env;
		}
		public String getParameter(String name) {
			if(env!=null) {
				String value = env.getPropertyValueByName(name);
				if(value!=null) {
					return value;
				}
			}
			return null;
		}
	}

	public void doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getMethod().equalsIgnoreCase("get")) {
			doGet(req, resp);
		} else if(req.getMethod().equalsIgnoreCase("post")) {
			doPost(req, resp);
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	
	//
	// Helpers
	//
	protected String getDefautLibraryPath(String serverUrl) {
		DojoLibrary lib = DojoLibraryFactory.getDefaultLibrary();
        String s = '-'+lib.getVersionTag();
		return serverUrl+"/xsp/.ibmxspres/dojoroot"+s+"/";
	}
	
	protected String composeServerUrl(HttpServletRequest req) {		
//		System.out.println("scheme:"+req.getScheme());
//		System.out.println("server:"+req.getServerName());
//		System.out.println("serverPath:"+req.getServletPath());
//		System.out.println("contextPath:"+req.getContextPath());
//		System.out.println("pathInfo:"+req.getPathInfo());
//		System.out.println("BaseURL:"+b.toString());
		
		StringBuilder b = new StringBuilder();
		String scheme = req.getScheme();
		String server = req.getServerName();
		int port = req.getServerPort();
		
		b.append(scheme);
		b.append("://");
		b.append(server);
		if( !(((port==80)&&scheme.equals("http")) || ((port==443)&&scheme.equals("https"))) ) {
			b.append(':');
			b.append(Integer.toString(port));
		}
		
		return b.toString();
	}
	
	protected String composeDatabaseUrl(HttpServletRequest req, String serverUrl) {
		String contextPath = req.getContextPath();
		return serverUrl+contextPath;
	}
	
	protected String composeToolkitUrl(String databaseUrl) {
		return PathUtil.concat(databaseUrl,RuntimeConstants.get().getConstant(RuntimeConstants.LIBRARY_BASEURL),'/');
		//return PathUtil.concat(databaseUrl,"xsp"+PlaygroundToolkitServletFactory.LIBRARY_PATHINFO,'/');
	}
}
