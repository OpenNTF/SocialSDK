package nsf.playground.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.dojo.JsonTreeRenderer;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * Base class for beans managing assets.
 * 
 * @author priand
 */
public abstract class AssetBean {

	public String encodeUrl(String s) throws UnsupportedEncodingException, IOException {
		return URLEncoding.encodeURIString(s, "utf-8", 0, true);
	}
	
	protected abstract String getFlatView();

	protected abstract String getAllView();
	
	protected abstract AssetNode createAssetNode(String notesUnid, CategoryNode parent, String name, String category, String assetId);

	public boolean querySave(DominoDocument doc) throws NotesException {
		// Create the encoded ID for the snippet
		String category = doc.getItemValueString("Category");
		String name = doc.getItemValueString("Name");
		doc.replaceItemValue("Id",Node.encodeSnippet(category,name));
		
		return true;
	}
	
	/**
	 * Get the list of API as a JSON file for a Dojo tree. 
	 * @throws NotesException 
	 * @throws IOException 
	 */
	public String getSnippetsAsJson() throws NotesException, IOException {
		RootNode root = readSnippetsNodes();
		JsonTreeRenderer r = new JsonTreeRenderer();
		return r.generateAsStringHier(root,true);
	}
	private RootNode readSnippetsNodes() throws NotesException {
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView(getFlatView());
		try {
			RootNode root = new RootNode();
			String apisSearch = (String)ExtLibUtil.getViewScope().get("assetSearch");
			if(StringUtil.isNotEmpty(apisSearch)) {
				v.FTSearch(apisSearch);
				ViewEntryCollection col = v.getAllEntries();
				for(ViewEntry e=col.getFirstEntry(); e!=null; e=col.getNextEntry()) {
					Vector<?> values = e.getColumnValues();
					String notesUnid = e.getUniversalID();
					String cat = (String)values.get(0);
					String name = (String)values.get(1);
					String jspUrl = (String)values.get(2);
					CategoryNode c = findCategory(root, cat);
					AssetNode node = createAssetNode(notesUnid,c,name,cat,jspUrl);
					node.setTooltip((String)values.get(5));
					c.getChildren().add(node);
				}
			} else { 
				v.setAutoUpdate(false);
				ViewNavigator nav = v.createViewNav();
				nav.setBufferMaxEntries(500);
				for(ViewEntry e=nav.getFirst(); e!=null; e=nav.getNext()) {
					Vector<?> values = e.getColumnValues();
					String notesUnid = e.getUniversalID();
					String cat = (String)values.get(0);
					String name = (String)values.get(1);
					String assetId = (String)values.get(2);
					CategoryNode c = findCategory(root, cat);
					AssetNode node = createAssetNode(notesUnid,c,name,cat,findUniqueUrl(c,notesUnid,assetId));
					node.setTooltip((String)values.get(5));
					c.getChildren().add(node);
				}
			}
			return root;
		} finally {
			v.recycle();
		}
	}
	private String findUniqueUrl(CategoryNode cat, String unid, String assetId) {
		List<Node> children = cat.getChildren();
		int childrenCount = children.size();
		for(int i=0; i<childrenCount; i++) {
			if(StringUtil.equals(children.get(i).getUnid(),assetId)) {
				return unid;
			}
		}
		return assetId;
	}
	private CategoryNode findCategory(RootNode node, String cat) {
		String[] cats = StringUtil.splitString(cat, '/');
		return findCategory(node, cats, 0);
	}
	private CategoryNode findCategory(CategoryNode parent, String[] cats, int level) {
		List<Node> children = parent.getChildren();
		CategoryNode found = null;
		for(int i=0; i<children.size(); i++) {
			Node c = children.get(i);
			if(c instanceof CategoryNode) {
				if(StringUtil.equals(cats[level], c.getName())) {
					found = (CategoryNode)c;
					break;
				}
			}
		}
		if(found==null) {
			found = new CategoryNode(parent,cats[level]);
			parent.getChildren().add(found);
		}
		if(level<cats.length-1) {
			return findCategory(found, cats, level+1);
		}
		return found;
	}
	
	/**
	 * Read the asset categories
	 */
	public String[] getAllCategories() throws NotesException {
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView(getAllView());
		ViewNavigator nav = v.createViewNav();
		try {
			nav.setMaxLevel(0);
			//nav.setCacheSize(128);
			List<String> categories = new ArrayList<String>();
			for(ViewEntry ve=nav.getFirst(); ve!=null; ve=nav.getNext(ve)) {
				categories.add((String)ve.getColumnValues().get(0));
			}
			return categories.toArray(new String[categories.size()]);
		} finally {
			nav.recycle();
		}
	}
}
