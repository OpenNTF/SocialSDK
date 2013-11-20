package nsf.playground.snippets;

import lotus.domino.Database;
import lotus.domino.Document;
import nsf.playground.beans.GadgetSnippetBean;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.opensocial.GadgetSnippet;
import com.ibm.sbt.playground.assets.opensocial.GadgetSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.VFSFile;

/**
 * Class for importing OpenSocial Gadget Snippets.
 * 
 * @author priand
 *
 */
public class GadgetSnippetImporter extends AssetImporter {
	
	public static final String TYPE = "gadget";
	public static final String FORM = GadgetSnippetBean.FORM;
	
	public GadgetSnippetImporter(Database db) {
		super(db);
	}
	
	protected String getAssetType() {
		return TYPE;
	}

	protected String getAssetForm() {
		return FORM;
	}

	protected NodeFactory getNodeFactory() {
		return new GadgetSnippetNodeFactory();
	}

	@Override
	protected void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception {
		GadgetSnippet snippet = (GadgetSnippet)asset;
		Document doc = getDatabase().createDocument();
		try {
			// The gadget are actually imported with their directory
			// We adapt the category & unid consequently
			String name = snippet.getTitle();
			if(StringUtil.isEmpty(name)) {
				name = createPrettyName(node.getParent().getName());
			}
			String cat = createPrettyName(node.getParent().getCategory());
			String unid = PathUtil.concat(cat,node.getUnid(),Node.SEPARATOR);
			unid = Node.encodeUnid(unid);
			
			setItemValue(doc,"Form", FORM);
			setItemValue(doc,"Author", doc.getParentDatabase().getParent().getEffectiveUserName()); // Should we make this private (reader field)?
			setItemValue(doc,"Id", unid);
			setItemValue(doc,"Category", cat);
			setItemValue(doc,"Name", name);
			setItemValue(doc,"FilterRuntimes", snippet.getProperty("runtimes"));
			setItemValue(doc,"Description", snippet.getProperty("description"));
			setItemValues(doc,"Tags", snippet.getProperty("tags"));
			setItemValue(doc,"ImportSource", source.getName());
			setItemValueRichText(doc,"Gadget", snippet.getGadgetXml());
			setItemValueRichText(doc,"Html", snippet.getHtml());
			setItemValueRichText(doc,"Css", snippet.getCss());
			setItemValueRichText(doc,"JavaScript", snippet.getJs());
			setItemValueRichText(doc,"Json", snippet.getJson());
			snippet.getProperties().remove("endpoints");
			snippet.getProperties().remove("description");
			setItemValueRichText(doc,"Properties", snippet.getPropertiesAsString());
			setItemValueRichText(doc,"Documentation", snippet.getDocHtml());

			doc.save();
		} finally {
			doc.recycle();
		}
	}
	
	protected String createPrettyName(String s) {
		StringBuilder sb = new StringBuilder();
		boolean cap = true;
		int len = s.length();
		for(int i=0; i<len; i++) {
			char c = s.charAt(i);
			if(c=='_' || c=='-' || c==' ') {
				c = ' '; cap = true;
			} else if(cap && Character.isLetter(c)) {
				c = Character.toUpperCase(c); cap = false;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
}
