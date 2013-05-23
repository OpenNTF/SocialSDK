package doc;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.FacesUtil;


public class OptionsBean {

	private String playgroundUrl;
	
	public OptionsBean() {
	}

	public String getPlaygroundUrl() {
		return playgroundUrl;
	}
	public void setPlaygroundUrl(String playgroundUrl) {
		this.playgroundUrl = playgroundUrl;
	}
	
	public String getPlaygroundUrl(String snippet) {
		StringBuilder b = new StringBuilder();
		String baseUrl = playgroundUrl;
		if(StringUtil.isEmpty(baseUrl)) {
			// Get the current server and use a default database name
			baseUrl = FacesUtil.makeUrlAbsolute(FacesContext.getCurrentInstance(), "/SBTPlayground.nsf");
		}
		b.append(baseUrl);
		b.append("/Playground.xsp");
		if(StringUtil.isNotEmpty(snippet)) {
			b.append("#snippet=");
			b.append(encodeUnid(snippet));
		}
		String url = b.toString(); 
		return url;
	}
	
	public String getAPIExplorerUrl(String api) {
		StringBuilder b = new StringBuilder();
		String baseUrl = playgroundUrl;
		if(StringUtil.isEmpty(baseUrl)) {
			// Get the current server and use a default database name
			baseUrl = FacesUtil.makeUrlAbsolute(FacesContext.getCurrentInstance(), "/SBTPlayground.nsf");
		}
		b.append(baseUrl);
		b.append("/Explorer.xsp");
		if(StringUtil.isNotEmpty(api)) {
			b.append("#api=");
			b.append(encodeUnid(api));
		}
		String url = b.toString(); 
		// TEMP FIX FOR DEMO
		// Make sure it is HTTP else the iframe might fail
		if(url.startsWith("https:")) {
			url = "http:"+url.substring("https:".length());
		}
		return url;
	}
	
	public String getAPIExplorerMiniUrl(String products, String api, String title, int idx) {
		StringBuilder b = new StringBuilder();
		String baseUrl = playgroundUrl;
		if(StringUtil.isEmpty(baseUrl)) {
			// Get the current server and use a default database name
			baseUrl = FacesUtil.makeUrlAbsolute(FacesContext.getCurrentInstance(), "/SBTPlayground.nsf");
		}
		b.append(baseUrl);
		b.append("/ExplorerMini.xsp");
		b.append("?rand=");
		b.append("123456789");
		b.append("#api=");
		if(StringUtil.isNotEmpty(products)) {
			if(StringUtil.indexOfIgnoreCase(products,"domino")>=0) {
				api = PathUtil.concat("Domino",api,'/');
			} else if(StringUtil.indexOfIgnoreCase(products,"connections")>=0) {
				api = PathUtil.concat("Connections",api,'/');
			} else if(StringUtil.indexOfIgnoreCase(products,"smartcloud")>=0) {
				api = PathUtil.concat("SmartCloud",api,'/');
			}
		}
		b.append(encodeUnid(api));
		if(StringUtil.isNotEmpty(title)) {
			String unid=encodeUnid(title);
			if(idx>1) {
				unid += "_"+idx;
			}
			b.append("&unid=");
			b.append(unid);
		}
		String url = b.toString(); 
		// TEMP FIX FOR DEMO
		// Make sure it is HTTP else the iframe might fail
		if(url.startsWith("https:")) {
			url = "http:"+url.substring("https:".length());
		}
		System.out.println("URL="+url);
		return url;
	}
	
	// Borrowed from the playground plug-in to avoid the dependency
	public static String encodeUnid(String s) {
		if(s.startsWith("/")) {
			s = s.substring(1);
		}
		StringBuilder b = new StringBuilder();
		for(int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if(Character.isLetterOrDigit(c)) {
				b.append(c);
			} else if(c=='_'||c=='-'||c=='.'||c=='@'||c=='$'||c=='('||c==')') {
				b.append(c);
			} else {
				b.append('_');
			}
		}
		return b.toString();
	}
}
