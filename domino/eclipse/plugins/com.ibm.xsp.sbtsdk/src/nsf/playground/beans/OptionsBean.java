package nsf.playground.beans;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.util.ManagedBeanUtil;


/**
 * Bean used to drive the options used by the application.
 * 
 * This bean should be store in the application scope
 * 
 * @author priand
 */
public abstract class OptionsBean {
	
	public static OptionsBean get() {
		return (OptionsBean)ManagedBeanUtil.getBean(FacesContext.getCurrentInstance(), "optionsBean");
	}

	private boolean javaScriptSnippetsEnabled;
	private boolean javaSnippetsEnabled;
	private boolean xpagesSnippetsEnabled;
	private boolean gadgetSnippetsEnabled;
	private boolean explorerEnabled;
	private boolean apacheLicense;
	private String environments;
	private String endpoints;
	
	private String banner;
	private String applicationTitle;
	private String applicationLogo;
	
	public OptionsBean() {
		loadOptions();
	}
	
	public boolean checkSSLWarning() {
		// Just display a warning once (the first time a session hits it)
		FacesContext facesContext = FacesContext.getCurrentInstance();
		boolean emitWarning = !((HttpServletRequest)facesContext.getExternalContext().getRequest()).getScheme().equals("https");
		if(emitWarning) {
			boolean checked = ExtLibUtil.getSessionScope(facesContext).containsKey("__sslchecked");
			if(!checked) {
				ExtLibUtil.getSessionScope(facesContext).put("__sslchecked",Boolean.TRUE);
				return true;
			}
		}
		return false;
	}

	public boolean isJavaScriptSnippetsEnabled() {
		return javaScriptSnippetsEnabled;
	}
	public void setJavaScriptSnippetsEnabled(boolean javaScriptSnippetsEnabled) {
		this.javaScriptSnippetsEnabled=javaScriptSnippetsEnabled;
	}

	public boolean isJavaSnippetsEnabled() {
		return javaSnippetsEnabled;
	}
	public void setJavaSnippetsEnabled(boolean javaSnippetsEnabled) {
		this.javaSnippetsEnabled=javaSnippetsEnabled;
	}

	public boolean isXpagesSnippetsEnabled() {
		return xpagesSnippetsEnabled;
	}
	public void setXpagesSnippetsEnabled(boolean xpagesSnippetsEnabled) {
		this.xpagesSnippetsEnabled=xpagesSnippetsEnabled;
	}

	public boolean isGadgetSnippetsEnabled() {
		return gadgetSnippetsEnabled;
	}
	public void setGadgetSnippetsEnabled(boolean gadgetSnippetsEnabled) {
		this.gadgetSnippetsEnabled=gadgetSnippetsEnabled;
	}

	public boolean isExplorerEnabled() {
		return explorerEnabled;
	}
	public void setExplorerEnabled(boolean explorerEnabled) {
		this.explorerEnabled=explorerEnabled;
	}

	public boolean isApacheLicense() {
		return apacheLicense;
	}
	public void setApacheLicense(boolean apacheLicense) {
		this.apacheLicense=apacheLicense;
	}

	public String getEnvironments() {
		return environments;
	}
	public void setEnvironments(String environments) {
		this.environments=environments;
	}

	public String getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(String endpoints) {
		this.endpoints=endpoints;
	}

	public String getApplicationTitle() {
		return applicationTitle;
	}
	public String getApplicationTitle(String subTitle) {
		if(StringUtil.isNotEmpty(subTitle)) {
			return getApplicationTitle() + " - " + subTitle;
		}
		return getApplicationTitle();
	}
	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle=applicationTitle;
	}

	public String getApplicationLogo() {
		return applicationLogo;
	}
	public void setApplicationLogo(String applicationLogo) {
		this.applicationLogo=applicationLogo;
	}

	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner=banner;
	}
	
	
	//
	// Load Options
	//
	public void loadOptions() {
		Document doc = loadOptionsDocument();
		try {
			this.javaScriptSnippetsEnabled = true;
			this.javaSnippetsEnabled = getEnvironmentBoolean(doc,"JavaSnippets");
			this.xpagesSnippetsEnabled = getEnvironmentBoolean(doc,"XPagesSnippets");
			this.gadgetSnippetsEnabled = getEnvironmentBoolean(doc,"GadgetSnippets");
			this.explorerEnabled = getEnvironmentBoolean(doc,"APIExplorer");
			this.apacheLicense = getEnvironmentBoolean(doc,"ApacheLicense");
			this.environments = getEnvironmentString(doc,"Environments");
			this.endpoints = getEnvironmentString(doc,"Endpoints","connections,connectionsOA2,smartcloud,smartcloudOA2,sametime,domino");
	
			this.banner = getEnvironmentString(doc,"Banner");
			this.applicationTitle = getEnvironmentString(doc,"AppTitle","IBM Social Business Toolkit");
			this.applicationLogo = getEnvironmentString(doc,"AppLogo");
		} finally {
			try {
				if(doc!=null) {
					doc.recycle();
				}
			} catch(NotesException ex) {}
		}
	}
	protected Document loadOptionsDocument() {
		try {
			Database db = ExtLibUtil.getCurrentDatabase();
			View v = db.getView("GlobalOptions");
			Document doc = v.getFirstDocument();
			return doc;
		} catch(NotesException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public String getNotesIniPrefix() {
		return "Playground_";
	}
	protected String getEnvironmentString(Document doc, String propName) {
		return getEnvironmentString(doc, propName, null);
	}
	protected int getEnvironmentInt(Document doc, String propName) {
		return getEnvironmentInt(doc, propName, 0);
	}
	protected int getEnvironmentInt(Document doc, String propName, int defaultValue) {
		String value = getEnvironmentString(doc, propName, null);
		if(StringUtil.isNotEmpty(value)) {
			try {
				return Integer.parseInt(value.trim());
			} catch(NumberFormatException ex) {}
		}
		return defaultValue;
	}
	protected boolean getEnvironmentBoolean(Document doc, String propName) {
		return getEnvironmentBoolean(doc, propName, false);
	}
	protected boolean getEnvironmentBoolean(Document doc, String propName, boolean defaultValue) {
		String value = getEnvironmentString(doc, propName, null);
		if(StringUtil.isNotEmpty(value)) {
			try {
				return Integer.parseInt(value.trim())!=0;
			} catch(NumberFormatException ex) {}
		}
		return defaultValue;
	}
	protected String getEnvironmentString(Document doc, String propName, String defaultValue) {
		// Read from the document
		String value = getDocumentField(doc, propName);
		if(StringUtil.isNotEmpty(value)) {
			return value;
		}
		// Get from notes.ini
		value = DominoUtils.getEnvironmentString(getNotesIniPrefix()+propName);
		if(StringUtil.isNotEmpty(value)) {
			return value;
		}
		// Else, return the default value
		return defaultValue;
	}
	protected String getDocumentField(Document doc, String propName) {
		if(doc!=null) {
			try {
				String fieldName = propName;
				return doc.getItemValueString(fieldName);
			} catch(NotesException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
}
