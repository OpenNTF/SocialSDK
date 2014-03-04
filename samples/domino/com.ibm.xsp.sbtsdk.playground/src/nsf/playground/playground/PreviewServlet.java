package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.servlet.FacesContextServlet;


public class PreviewServlet extends FacesContextServlet {

	private static final long serialVersionUID = 1L;

	private PreviewJavaScriptHandler javaScriptHandler;
	private PreviewJavaHandler javaHandler;
	private PreviewXPagesHandler xpagesHandler;
	private PreviewGadgetHandler gadgetHandler;
	
	public PreviewServlet() {
	}

	public PreviewHandler getJavaScriptSnippetHandler() {
		if(javaScriptHandler==null) {
			javaScriptHandler = new PreviewJavaScriptHandler();
		}
		return javaScriptHandler;
	}

	public PreviewHandler getJavaSnippetHandler() {
		if(javaHandler==null) {
			javaHandler = new PreviewJavaHandler();
		}
		return javaHandler;
	}

	public PreviewHandler getXPagesSnippetHandler() {
		if(xpagesHandler==null) {
			xpagesHandler = new PreviewXPagesHandler();
		}
		return xpagesHandler;
	}

	public PreviewHandler getGadgetSnippetHandler() {
		if(gadgetHandler==null) {
			gadgetHandler = new PreviewGadgetHandler();
		}
		return gadgetHandler;
	}

	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		// Make sure that the JSF servlet is created an initialized
		NotesContext.getCurrent().getModule().getXspEngineServlet();
		
		// Create a temporary FacesContext and make it available
        FacesContext context = initContext(servletRequest, servletResponse);
        try {
    		// Make sure that the app hasn't been discarded
    		// If so, then we return a SERVICE_UNAVAILABLE error
        	// Just for security
    		ApplicationEx app = ((FacesContextEx)context).getApplicationEx();
    		if(app.getController()==null) {
    			HttpServletResponse resp = (HttpServletResponse)servletResponse; 
    			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    			resp.setContentType("text/html");
    			PrintWriter w = resp.getWriter();
    			w.println("The server session has expired. Please reload the main page to start a new session");
    			w.flush();
    			return;
    		}

    		// Do whatever you need
            super.service(servletRequest, servletResponse);
        } finally {
            releaseContext(context);
        }
    }

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Dispatch to the right handler
		String pathInfo = req.getPathInfo();
		if(StringUtil.startsWithIgnoreCase(pathInfo, "/javascriptsnippet")) {
			getJavaScriptSnippetHandler().doService(req, resp);
		} else if(StringUtil.startsWithIgnoreCase(pathInfo, "/javasnippet")) {
			getJavaSnippetHandler().doService(req, resp);
		} else if(StringUtil.startsWithIgnoreCase(pathInfo, "/jsppage")) {
			getJavaSnippetHandler().doService(req, resp);
		} else if(StringUtil.startsWithIgnoreCase(pathInfo, "/xpagessnippet")) {
			getXPagesSnippetHandler().doService(req, resp);
		} else if(StringUtil.startsWithIgnoreCase(pathInfo, "/gadgetsnippet")) {
			getGadgetSnippetHandler().doService(req, resp);
		}
	}
}
