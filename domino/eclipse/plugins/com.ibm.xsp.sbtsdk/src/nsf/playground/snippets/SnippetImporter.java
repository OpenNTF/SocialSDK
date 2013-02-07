package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.apis.APINodeFactory;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing JS Snippets.
 * 
 * @author priand
 *
 */
public class SnippetImporter extends AssetImporter {
	
	public static final String TYPE = "html";
	
	public SnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetView() {
		return "AllSnippetsByImportSource";
	}

	protected NodeFactory getNodeFactory() {
		return new JSSnippetNodeFactory();
	}

	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		JSSnippet snippet = (JSSnippet)node.load(root);
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", "CodeSnippet");
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"Description", snippet.getDescription());
			setItemValue(doc,"ImportSource", source.getName());
			setItemValue(doc,"Tags", snippet.getTags());
			setItemValue(doc,"Documentation", snippet.getDocumentation());
			setItemValue(doc,"Html", snippet.getHtml());
			setItemValue(doc,"Css", snippet.getCss());
			setItemValue(doc,"JavaScript", snippet.getJs());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
