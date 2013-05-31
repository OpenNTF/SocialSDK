package nsf.playground.beans;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
	
	private String applicationTitle;
	private String applicationLogo;
	
	public OptionsBean() {
		this.javaScriptSnippetsEnabled = true;
		this.javaSnippetsEnabled = getEnvironmentBoolean("JavaSnippets");
		this.xpagesSnippetsEnabled = getEnvironmentBoolean("XPagesSnippets");
		this.gadgetSnippetsEnabled = getEnvironmentBoolean("GadgetSnippets");
		this.explorerEnabled = getEnvironmentBoolean("APIExplorer");
		this.apacheLicense = getEnvironmentBoolean("ApacheLicense");
		this.environments = getEnvironmentString("Environments");

		this.applicationTitle = getEnvironmentString("AppTitle","IBM Social Business Toolkit");
		this.applicationLogo = getEnvironmentString("AppLogo");
	}
	
	protected String getEnvironmentString(String propName) {
		return getEnvironmentString(propName, null);
	}
	protected String getEnvironmentString(String propName, String defaultValue) {
		String name = "Playground_"+propName;
		String value = DominoUtils.getEnvironmentString(name);
		if(StringUtil.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}
	protected int getEnvironmentInt(String propName) {
		return getEnvironmentInt(propName, 0);
	}
	protected int getEnvironmentInt(String propName, int defaultValue) {
		String value = getEnvironmentString(propName, null);
		if(StringUtil.isNotEmpty(value)) {
			try {
				return Integer.parseInt(value.trim());
			} catch(NumberFormatException ex) {}
		}
		return defaultValue;
	}
	protected boolean getEnvironmentBoolean(String propName) {
		return getEnvironmentBoolean(propName, false);
	}
	protected boolean getEnvironmentBoolean(String propName, boolean defaultValue) {
		String value = getEnvironmentString(propName, null);
		if(StringUtil.isNotEmpty(value)) {
			try {
				return Integer.parseInt(value.trim())!=0;
			} catch(NumberFormatException ex) {}
		}
		return defaultValue;
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
	
}
