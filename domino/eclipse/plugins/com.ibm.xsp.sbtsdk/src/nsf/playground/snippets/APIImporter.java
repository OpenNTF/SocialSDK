package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;

import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.apis.APIDescription;
import com.ibm.sbt.playground.assets.apis.APINodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing API Descriptions.
 * 
 * @author priand
 *
 */
public class APIImporter extends AssetImporter {

	public static final String TYPE = "api";
	
	public APIImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetView() {
		return "AllAPIsByImportSource";
	}

	protected NodeFactory getNodeFactory() {
		return new APINodeFactory();
	}
	
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		APIDescription snippet = (APIDescription)node.load(root);
		Document doc = getDatabase().createDocument();
		try {
			setItemValue(doc,"Form", "APIDescription");
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", node.getUnid());
			setItemValue(doc,"Category", node.getCategory());
			setItemValue(doc,"Name", node.getName());
			setItemValue(doc,"Description", snippet.getDescription());
			setItemValue(doc,"ImportSource", source.getName());
			setItemValue(doc,"Tags", snippet.getTags());
			setItemValue(doc,"Endpoint", snippet.getEndpoint());
			setItemValue(doc,"BaseDocUrl", snippet.getBaseDocUrl());
			setItemValue(doc,"Documentation", snippet.getDocumentation());
			setItemValueRichText(doc,"Json", snippet.getJson());
			
			doc.save();
		} finally {
			doc.recycle();
		}
	}
}
