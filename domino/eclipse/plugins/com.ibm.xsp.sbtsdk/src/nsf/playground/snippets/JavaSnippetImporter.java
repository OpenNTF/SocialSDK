package nsf.playground.snippets;

import nsf.playground.beans.JavaSnippetBean;
import lotus.domino.Database;
import lotus.domino.Document;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippet;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing Java Snippets.
 * 
 * @author priand
 *
 */
public class JavaSnippetImporter extends AssetImporter {
	
	public static final String TYPE = "java";
	public static final String FORM = JavaSnippetBean.FORM;
	
	public JavaSnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetForm() {
		return FORM;
	}

	protected NodeFactory getNodeFactory() {
		return new JavaSnippetNodeFactory();
	}

	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		JavaSnippet snippet = (JavaSnippet)node.load(root);
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValue(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Jsp", snippet.getJsp());
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
