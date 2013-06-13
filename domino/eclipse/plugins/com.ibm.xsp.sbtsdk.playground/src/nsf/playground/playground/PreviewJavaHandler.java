package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Document;
import lotus.domino.NotesException;
import nsf.playground.beans.DataAccessBean;
import nsf.playground.beans.JavaSnippetBean;
import nsf.playground.environments.PlaygroundEnvironment;
import nsf.playground.jsp.JspCompiler;
import nsf.playground.jsp.JspFragment;
import nsf.playground.jsp.JspSampleWriter;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.IExceptionEx;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.util.HtmlUtil;
import com.ibm.xsp.util.ManagedBeanUtil;


public class PreviewJavaHandler extends PreviewHandler {

	private static final String LAST_REQUEST = "javasnippet.lastrequest"; 

	static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		String sOptions;
		String id;
        public RequestParams(String sOptions, String id) {
        	this.sOptions = sOptions;
        	this.id = id;
        }
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if(pathInfo.equals("/javasnippet")) {
			String sOptions = req.getParameter("fm_options");
			String id = req.getParameter("fm_id");
			RequestParams requestParams = new RequestParams(sOptions,id);
			req.getSession().setAttribute(LAST_REQUEST, requestParams);
			String url = UrlUtil.getRequestUrl(req);
			url = StringUtil.replace(url, "/javasnippet", "/jsppage");
			resp.sendRedirect(url);
		} else {
			execRequest(req, resp);
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execRequest(req, resp);
	}

	public void execRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestParams requestParams = (RequestParams)req.getSession().getAttribute(LAST_REQUEST);
		if(requestParams!=null) {
	        execRequest(req, resp, requestParams);
		} else {
			PrintWriter pw = resp.getWriter();
			pw.println("Social Business Tooolkit Playground - Java Snippet Preview Servlet");
			pw.flush();
		}
	}
	
	protected void execRequest(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setStatus(HttpServletResponse.SC_OK);

		String sOptions = requestParams.sOptions;
		JsonJavaObject options = new JsonJavaObject();
		try {
			options = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, sOptions);
		} catch(Exception ex) {}
		boolean debug = options.getBoolean("debug");

		String unid = requestParams.id;

		DataAccessBean dataAccess = DataAccessBean.get();
		
		String envName = options.getString("env");
		PlaygroundEnvironment env = dataAccess.getEnvironment(envName);
		env.prepareEndpoints();
		
		SBTEnvironment.push(Context.get(), env);

		EnvParameterProvider prov = new EnvParameterProvider(env);

		// Load the jsp document
		String jsp = null;
		try {
			Document doc = DominoUtils.getDocumentById(DominoUtils.getCurrentDatabase(), unid);
			jsp = doc.getItemValueString("Jsp");
			jsp = ParameterProcessor.process(jsp, prov);
		} catch(NotesException ex) {
			throw new ServletException(ex);
		}
		
		PrintWriter pw = resp.getWriter();
		
		pw.println("<!DOCTYPE html>");
		pw.println("<html lang=\"en\">");
		
		pw.println("<head>");
		pw.println("  <title>Social Business Playground - Java Snippet</title>");
		pw.println("</head>");

		pw.println("<body>");
		
//		pw.println("<pre>");
//		pw.println(jsp);
//		pw.println("</pre>");
		
		String jspClassName = "xspjsp.jsp_"+unid;
		//String jspClassName = "jsp_"+unid;
		
		String sourceCode = null;
		try {
			JavaSnippetBean bean = (JavaSnippetBean)ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), "javaSnippetBean");
			Class<JspFragment> jspClass = bean.getCompiledClass(jspClassName);
			if(jspClass==null) {
				JspCompiler compiler = new JspCompiler();
				sourceCode = compiler.compileJsp(jsp, jspClassName);
				if(false) {
					pw.println("<pre>");
					pw.println(HtmlUtil.toHTMLContentString(sourceCode, false));
					pw.println("</pre>");
				}
				jspClass = bean.compileSnippet(jspClassName, sourceCode);
			}
			if(jspClass!=null) {
				JspFragment f = jspClass.newInstance();
				f.exec(new JspSampleWriter(pw),req,resp);
			}
		} catch(Throwable e) {
			pw.println("Execution error");
			pw.println("<pre>");
			
			StringWriter sw = new StringWriter();
			PrintWriter psw = new PrintWriter(sw);
			if(e instanceof IExceptionEx) {
				((IExceptionEx)e).printExtraInformation(psw);
				psw.println("");
			}
			if(sourceCode!=null) {
				psw.println("<pre>");
				psw.println(HtmlUtil.toHTMLContentString(sourceCode, false));
				psw.println("</pre>");
			}
			e.printStackTrace(psw);
			psw.flush();
			pw.println(HtmlUtil.toHTMLContentString(sw.toString(), false));
			pw.println("</pre>");
		}
		
		pw.println("</body>");
		pw.println("</html>");
		
		pw.flush();
		pw.close();
	}
}
