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
	
	public OptionsBean() {
		this.playgroundEnabled = true;
		this.explorerEnabled = DominoUtils.getEnvironmentInt("APIExplorer")!=0;
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
}
