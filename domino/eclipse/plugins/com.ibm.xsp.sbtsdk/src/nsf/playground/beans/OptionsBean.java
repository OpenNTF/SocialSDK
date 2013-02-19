package nsf.playground.beans;

import com.ibm.xsp.model.domino.DominoUtils;


/**
 * Bean used to drive the options used by the application.
 * 
 * This bean should be store in the application scope
 * 
 * @author priand
 */
public class OptionsBean {

	private boolean playgroundEnabled;
	private boolean explorerEnabled;
	private boolean apacheLicense;
	
	public OptionsBean() {
		this.playgroundEnabled = true;
		this.explorerEnabled = DominoUtils.getEnvironmentInt("Playground_APIExplorer")!=0;
		this.apacheLicense = DominoUtils.getEnvironmentInt("Playground_ApacheLicense")!=0;
	}

	public boolean isPlaygroundEnabled() {
		return playgroundEnabled;
	}

	public void setPlaygroundEnabled(boolean playgroundEnabled) {
		this.playgroundEnabled=playgroundEnabled;
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
