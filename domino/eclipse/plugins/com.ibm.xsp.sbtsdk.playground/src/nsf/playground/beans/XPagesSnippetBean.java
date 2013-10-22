package nsf.playground.beans;

import nsf.playground.playground.PreviewHandler.EnvParameterProvider;

import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.xpages.XPagesSnippetAssetNode;

/**
 * Classes that encapsulates the business logic for an XPages snippet.
 * 
 * @author priand
 */
public abstract class XPagesSnippetBean extends AssetBean {

	public static final String FORM = "XPagesSnippet";
	
	protected String getAssetForm() {
		return FORM;
	}
	
	protected AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId) {
		return new XPagesSnippetAssetNode(parent,name,category,notesUnid,assetId);
	}
	
	public String processParameters(SBTEnvironment env, String xpages) throws Exception {
		EnvParameterProvider prov = new EnvParameterProvider(env);
		return ParameterProcessor.process(xpages, prov);
	}
}
