package nsf.playground.beans;

import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.opensocial.GadgetSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for an OpenSocial gadget snippet.
 * 
 * @author priand
 */
public abstract class GadgetSnippetBean extends AssetBean {

	public static final String FORM = "GadgetSnippet";
	
	protected String getAssetForm() {
		return FORM;
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new GadgetSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
	
	public String getContainerUrl() {
		PlaygroundEnvironment e = DataAccessBean.get().getCurrentEnvironment(null);
		String url = "/.ibmxspres/domino/osplayground/gadgets/js/container:embedded-experiences:open-views:actions:selection.js?c=1&amp;debug=1&amp;container="+e.getId();
		return url;
	}
}
