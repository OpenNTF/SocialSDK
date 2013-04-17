package nsf.playground.beans;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for a JS snippet.
 * 
 * @author priand
 */
public class SnippetBean extends AssetBean {

	protected String getFlatView() {
		return "AllSnippetsFlat";
	}

	protected String getAllView() {
		return "AllSnippets";
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JSSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
}
