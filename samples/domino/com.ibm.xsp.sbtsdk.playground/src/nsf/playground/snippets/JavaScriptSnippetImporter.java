package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.JavaScriptSnippetBean;

import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing JS Snippets.
 * 
 * @author priand
 *
 */
public class JavaScriptSnippetImporter extends AssetImporter {
	
	public static final String TYPE = "html";
	public static final String FORM = JavaScriptSnippetBean.FORM;
	
	public JavaScriptSnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetForm() {
		return FORM;
	}

	protected NodeFactory getNodeFactory() {
		return new JSSnippetNodeFactory();
	}

	@Override
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception {
		JSSnippet snippet = (JSSnippet)asset;
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getEffectiveUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"FilterRuntimes", snippet.getProperty("runtimes"));
			setItemValue(doc,"FilterLibraries", snippet.getProperty("jslibs"));
			setItemValues(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Html", snippet.getHtml());
			setItemValueRichText(doc,"Css", snippet.getCss());
			setItemValueRichText(doc,"JavaScript", snippet.getJs());
			snippet.getProperties().remove("endpoints");
			snippet.getProperties().remove("jslibs");
			snippet.getProperties().remove("description");
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
