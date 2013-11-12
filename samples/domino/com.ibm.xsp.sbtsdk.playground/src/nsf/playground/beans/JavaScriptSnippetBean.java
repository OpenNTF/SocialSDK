package nsf.playground.beans;

import lotus.domino.NotesException;
import lotus.domino.ViewEntry;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetAssetNode;
import com.ibm.xsp.sbtsdk.servlets.JavaScriptLibraries;
import com.ibm.xsp.sbtsdk.servlets.JavaScriptLibraries.JSLibrary;

/**
 * Classes that encapsulates the business logic for a JS snippet.
 * 
 * @author priand
 */
public abstract class JavaScriptSnippetBean extends AssetBean {

	public static final String FORM = "JavaScriptSnippet";
	
	public JavaScriptSnippetBean() {
	}
	
	public String getDefaultJavaScriptLibrary() {
		return JavaScriptLibraries.LIBRARIES[0].getLabel();
	}
	
	public String[] getJavaScriptLibraries() {
		String[] libs = new String[JavaScriptLibraries.LIBRARIES.length];
		for(int i=0; i<libs.length; i++) {
			libs[i] = JavaScriptLibraries.LIBRARIES[i].getLabel();
		}
		return libs;
	}
	
	protected String getAssetForm() {
		return FORM;
	}
	
	protected RootNode readSnippetsNodes() throws NotesException {
		this.assetLoaderJavaScriptLibrary = JavaScriptLibraries.getCurrentJavaScriptLibrary();
		try {
			return super.readSnippetsNodes();
		} finally {
			this.assetLoaderJavaScriptLibrary = null;
		}
	}
	// Just for readSnippetsNodes
	private transient JSLibrary assetLoaderJavaScriptLibrary;
	
	protected boolean acceptAsset(ViewEntry e, String filterRuntimes, String filterLibraries) {
		if(!super.acceptAsset(e, filterRuntimes, filterLibraries)) {
			return false;
		}

		// Check for the JavaScript libraries
		// The current library should match the properties
		if(assetLoaderJavaScriptLibrary!=null && StringUtil.isNotEmpty(filterLibraries)) {
			boolean ok = false;
			
			String[] jsl = StringUtil.splitString(filterLibraries, ',', true);
			for(int i=0; i<jsl.length; i++) {
				String js = jsl[i];
				if(assetLoaderJavaScriptLibrary.getTag().startsWith(js)) {
					ok = true;
					break;
				}
			}
			
			if(!ok) {
				return false;
			}
		}
		
		// Ok, we want it!
		return true;
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JSSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
}
