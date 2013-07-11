package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.JavaScriptSnippetBean;

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

	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		JSSnippet snippet = (JSSnippet)node.load(root);
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"FilterEndpoints", snippet.getProperty("endpoints"));
			setItemValue(doc,"FilterLibraries", snippet.getProperty("jslibs"));
			setItemValue(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Html", snippet.getHtml());
			setItemValueRichText(doc,"Css", snippet.getCss());
			setItemValueRichText(doc,"JavaScript", snippet.getJs());
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
