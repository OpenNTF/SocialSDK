package nsf.playground.playground;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.util.UrlUtil;


public class PreviewXPagesHandler extends PreviewHandler {

	private static final String LAST_REQUEST = "xpagessnippet.lastrequest"; 

	public static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		private String sOptions;
		private String id;
        public RequestParams(String sOptions, String id) {
        	this.sOptions = sOptions;
        	this.id = id;
        }
        public String getOptions() {
        	return sOptions;
        }
        public String getId() {
        	return id;
        }
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sOptions = req.getParameter("fm_options");
		String id = req.getParameter("fm_id");
        RequestParams requestParams = new RequestParams(sOptions,id);
        req.getSession().setAttribute(LAST_REQUEST, requestParams);
        String baseUrl = UrlUtil.getContextUrl(req);
        resp.sendRedirect(baseUrl+"/_PreviewXPages.xsp");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		pw.println("Social Business Toolkit Playground - XPages Snippet Preview Servlet");
		pw.flush();
	}
}
