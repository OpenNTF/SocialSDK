package nsf.playground.beans;

import lotus.domino.NotesException;
import lotus.domino.ViewEntry;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetAssetNode;
import com.ibm.sbt.services.endpoints.EndpointFactory;
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
		this.environment = PlaygroundEnvironment.getCurrentEnvironment();
		this.javaScriptLibrary = JavaScriptLibraries.getCurrentJavaScriptLibrary();
		try {
			return super.readSnippetsNodes();
		} finally {
			this.environment = null;
			this.javaScriptLibrary = null;
		}
	}
	// Just for readSnippetsNodes
	private transient PlaygroundEnvironment environment;
	private transient JSLibrary javaScriptLibrary;
	
	protected boolean acceptAsset(ViewEntry e, String filterEndpoints, String filterLibraries) {
		// Check for the endpoints
		// If at least one endpoint is not available, then we do not accept the snippet
		if(environment!=null && StringUtil.isNotEmpty(filterEndpoints)) {
			String[] eps = StringUtil.splitString(filterEndpoints, ',', true);
			for(int i=0; i<eps.length; i++) {
				String ep = eps[i];
				if(!endpointExists(ep)) {
					return false;
				}
			}
		}
		
		// Check for the JavaScript libraries
		// The current library should match the properties
		if(javaScriptLibrary!=null && StringUtil.isNotEmpty(filterLibraries)) {
			boolean ok = false;
			
			String[] jsl = StringUtil.splitString(filterLibraries, ',', true);
			for(int i=0; i<jsl.length; i++) {
				String js = jsl[i];
				if(javaScriptLibrary.getTag().startsWith(js)) {
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
	protected boolean endpointExists(String name) {
		// Should we manage a cache here?
		String alias = environment.getPropertyValueByName(EndpointFactory.SERVERPROP_PREFIX+name);
		if(StringUtil.isNotEmpty(alias)) {
			name = alias;
		}
		return EndpointFactory.getEndpointUnchecked(name)!=null;
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new JSSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
}
