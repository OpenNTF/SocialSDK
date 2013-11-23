package nsf.playground.snippets;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.JavaSnippetBean;

import com.ibm.sbt.playground.assets.Asset;
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

	@Override
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception {
		JavaSnippet snippet = (JavaSnippet)asset;
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getEffectiveUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValue(doc,"FilterRuntimes", snippet.getProperty("runtimes"));
			setItemValues(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Jsp", snippet.getJsp());
			snippet.getProperties().remove("endpoints");
			snippet.getProperties().remove("description");
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
