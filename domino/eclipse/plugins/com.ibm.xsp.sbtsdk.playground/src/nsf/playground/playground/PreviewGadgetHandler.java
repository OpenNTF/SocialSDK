package nsf.playground.playground;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.SystemCache;
import com.ibm.commons.util.io.ByteStreamCache;
import com.ibm.commons.util.io.ReaderInputStream;


public class PreviewGadgetHandler extends PreviewHandler {

	static class RequestParams implements Serializable {
		private static final long serialVersionUID = 1L;
		String sOptions;
		String gadget;
		String html;
		String js;
		String css;
		String json;
		String properties;
        public RequestParams(String sOptions, String gadget, String html, String js, String css, String json, String properties) {
        	this.sOptions = sOptions;
        	this.gadget = gadget;
        	this.html = html;
        	this.js = js;
        	this.css = css;
        	this.json = json;
        	this.properties = properties;
        }
	}

	private static SystemCache requestParamsMap = new SystemCache("gadgets", 50);

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sOptions = req.getParameter("fm_options");
		String gadgetId = req.getParameter("fm_gadgetid");
		String gadget = req.getParameter("fm_gadget");
		String html = req.getParameter("fm_html");
		String js = req.getParameter("fm_js");
        String css = req.getParameter("fm_css");
        String json = req.getParameter("fm_json");
        String properties = req.getParameter("fm_properties");
        RequestParams requestParams = new RequestParams(sOptions,gadget,html,js,css,json,properties);
        requestParamsMap.put(gadgetId, requestParams);
        resp.setStatus(200);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String[] parts = StringUtil.splitString(pathInfo.substring(1), '/', false);
		if(parts.length==3) {
			RequestParams requestParams = (RequestParams)requestParamsMap.get(parts[1]);
			if(requestParams!=null) {
				String fileName = parts[2];
				if(StringUtil.endsWithIgnoreCase(fileName,".xml")) {
					emit(resp,requestParams.gadget,"text/xml;charset=utf-8");
					return;
				}
				if(StringUtil.endsWithIgnoreCase(fileName,".html")) {
					emit(resp,requestParams.html,"text/html;charset=utf-8");
					return;
				}
				if(StringUtil.endsWithIgnoreCase(fileName,".css")) {
					emit(resp,requestParams.css,"text/css;charset=utf-8");
					return;
				}
				if(StringUtil.endsWithIgnoreCase(fileName,".js")) {
					emit(resp,requestParams.js,"application/javascript;charset=utf-8");
					return;
				}
				if(StringUtil.endsWithIgnoreCase(fileName,".json")) {
					emit(resp,requestParams.json,"application/json;charset=utf-8");
					return;
				}
			} else {
		        resp.setStatus(404);
				PrintWriter pw = resp.getWriter();
				pw.println("In memory gadget cache has expired");
				pw.flush();
			}
		}
		
		PrintWriter pw = resp.getWriter();
		pw.println("Social Business Toolkit Playground - OpenSocial Gadget Snippet Preview Servlet");
		pw.flush();
		
		// Return the different parts of the gadget
	}
	
	private void emit(HttpServletResponse resp, String text, String contentType) throws IOException {
		resp.setStatus(200);
		resp.setContentType(contentType);
		ByteStreamCache bs = new ByteStreamCache();
		InputStream is = new ReaderInputStream(new StringReader(text),"utf-8");
		bs.copyFrom(is);
		resp.setContentLength((int)bs.getLength());
		OutputStream os = resp.getOutputStream();
		bs.copyTo(os);
		os.flush();
	}
}
