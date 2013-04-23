package nsf.playground.beans;

import javax.faces.context.FacesContext;

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
	private boolean explorerEnabled;
	private boolean apacheLicense;
	private String environments;
	
	public OptionsBean() {
		this.javaScriptSnippetsEnabled = true;
		this.javaSnippetsEnabled = DominoUtils.getEnvironmentInt("Playground_JavaSnippets")!=0;
		this.xpagesSnippetsEnabled = DominoUtils.getEnvironmentInt("Playground_XPagesSnippets")!=0;
		this.explorerEnabled = DominoUtils.getEnvironmentInt("Playground_APIExplorer")!=0;
		this.apacheLicense = DominoUtils.getEnvironmentInt("Playground_ApacheLicense")!=0;
		this.environments = DominoUtils.getEnvironmentString("Playground_Environments");
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
}
