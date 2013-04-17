package nsf.playground.beans;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for a Java snippet.
 * 
 * @author priand
 */
public class JavaSnippetBean extends AssetBean {

	protected String getFlatView() {
		return "AllJavaSnippetsFlat";
	}

	protected String getAllView() {
		return "AllJavaSnippets";
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JavaSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
}
