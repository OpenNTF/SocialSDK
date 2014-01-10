package nsf.playground.beans;

import java.net.URLEncoder;

import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.sbt.opensocial.domino.container.ContainerExtPointException;
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
	//Loads the default common container feature plug embedded experiences, open views, actions, and selection.
	//To load another feature just put a colon then the feature name.  For example if I want to load feature foo I would put
	// /.ibmxspres/domino/sbtos/gadgets/js/container:embedded-experiences:open-views:actions:selection:foo.js
	public static final String CONTAINER_JS = "/.ibmxspres/domino/sbtos/gadgets/js/container:embedded-experiences:open-views:actions:selection.js?c=1&debug=1&container=";
	
	protected String getAssetForm() {
		return FORM;
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new GadgetSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}

	/**
	 * Gets the URL to load the container JavaScript for the current container/environment.
	 * @return The URL to load the container JavaScript for the current container/environment.
	 * @throws Exception Thrown if there is an error getting the container URL.
	 */
	public String getContainerUrl() throws Exception {
		PlaygroundEnvironment e = PlaygroundEnvironment.getCurrentEnvironment();
		//Even though getId will already be encoded, we encode it again because when Shindig decodes it we want to
		//make sure it matches the id of the container (which is URL encoded when registered with Shindig)
		String url = CONTAINER_JS + URLEncoder.encode(e.getId(), "UTF-8");
		return url;
	}
	
	/**
	 * Gets the current container ID.
	 * @return The current container ID.
	 * @throws ContainerExtPointException Thrown when there is an error getting the container ID.
	 */
	public String getContainerId() throws ContainerExtPointException {
		String containerId = PlaygroundEnvironment.getCurrentEnvironment().getId();
		return containerId;
	}
}
