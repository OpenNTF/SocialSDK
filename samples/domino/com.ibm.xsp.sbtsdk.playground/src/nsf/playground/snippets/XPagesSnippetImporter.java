package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.XPagesSnippetBean;

import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.xpages.XPagesSnippet;
import com.ibm.sbt.playground.assets.xpages.XPagesSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing XPages Snippets.
 * 
 * @author priand
 *
 */
public class XPagesSnippetImporter extends AssetImporter {
	
	public static final String TYPE = "xpages";
	public static final String FORM = XPagesSnippetBean.FORM;
	
	public XPagesSnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetForm() {
		return FORM;
	}

	protected NodeFactory getNodeFactory() {
		return new XPagesSnippetNodeFactory();
	}

	@Override
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception {
		XPagesSnippet snippet = (XPagesSnippet)asset;
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
			setItemValueRichText(doc,"XPages", snippet.getXsp());
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
