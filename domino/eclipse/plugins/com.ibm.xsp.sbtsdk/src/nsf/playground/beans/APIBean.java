package nsf.playground.beans;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.apis.APIAssetNode;

/**
 * Classes that encapsulates the business logic for an API Description.
 * 
 * @author priand
 */
public class APIBean extends AssetBean {

	protected String getFlatView() {
		return "AllAPIsFlat";
	}

	protected String getAllView() {
		return "AllSnippets";
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new APIAssetNode(parent,name,category,notesUnid,assetId);
	}
}
