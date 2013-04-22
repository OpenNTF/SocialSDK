package nsf.playground.beans;

import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Bean used to drive the options used by the application.
 * 
 * This bean should be store in the application scope
 * 
 * @author priand
 */
public abstract class OptionsBean {

	private boolean javaScriptSnippetsEnabled;
	private boolean javaSnippetsEnabled;
	private boolean xPagesSnippetsEnabled;
	private boolean explorerEnabled;
	private boolean apacheLicense;
	
	public OptionsBean() {
		this.javaScriptSnippetsEnabled = true;
		this.javaSnippetsEnabled = DominoUtils.getEnvironmentInt("Playground_JavaSnippets")!=0;
		this.xPagesSnippetsEnabled = DominoUtils.getEnvironmentInt("Playground_XPagesSnippets")!=0;
		this.explorerEnabled = DominoUtils.getEnvironmentInt("Playground_APIExplorer")!=0;
		this.apacheLicense = DominoUtils.getEnvironmentInt("Playground_ApacheLicense")!=0;
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

	public boolean isXPagesSnippetsEnabled() {
		return xPagesSnippetsEnabled;
	}

	public void setXPagesSnippetsEnabled(boolean xPagesSnippetsEnabled) {
		this.xPagesSnippetsEnabled=xPagesSnippetsEnabled;
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
}
