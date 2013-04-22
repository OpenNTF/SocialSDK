package nsf.playground.beans;

import nsf.playground.jsp.JspFragment;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for a Java snippet.
 * 
 * @author priand
 */
public abstract class JavaSnippetBean extends AssetBean {

	protected String getFlatView() {
		return "AllJavaSnippetsFlat";
	}

	protected String getAllView() {
		return "AllJavaSnippets";
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JavaSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
	
	//
	// This is delegated to the NSF as we don't have access to the Bazaar from here
	//
	public Class<JspFragment> getCompiledClass(String jspClassName) throws Exception {
		return null;
	}
	public Class<JspFragment> compileSnippet(String jspClassName, String source) throws Exception {
		return null;
	}
}
