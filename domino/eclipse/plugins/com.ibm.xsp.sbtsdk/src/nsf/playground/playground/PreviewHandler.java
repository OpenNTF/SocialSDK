package nsf.playground.playground;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.jslibrary.SBTEnvironment;

/**
 * Base handler class for preview activities.
 * 
 * The dispatching is actually done by the PreviewServlet
 * 
 * @author priand
 */
public abstract class PreviewHandler {

	protected static class EnvParameterProvider implements com.ibm.commons.runtime.util.ParameterProcessor.ParameterProvider {
		SBTEnvironment env;
		EnvParameterProvider(SBTEnvironment env) {
			this.env = env;
		}
		public String getParameter(String name) {
			if(env!=null) {
				return env.getPropertyValueByName(name);
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
}
