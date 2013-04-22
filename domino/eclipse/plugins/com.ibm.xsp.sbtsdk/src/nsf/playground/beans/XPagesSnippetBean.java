package nsf.playground.beans;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for an XPages snippet.
 * 
 * @author priand
 */
public abstract class XPagesSnippetBean extends AssetBean {

	protected String getFlatView() {
		return "AllXPagesSnippetsFlat";
	}

	protected String getAllView() {
		return "AllXPagesSnippets";
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JavaSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
}
