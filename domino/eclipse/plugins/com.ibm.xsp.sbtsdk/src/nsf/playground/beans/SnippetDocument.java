package nsf.playground.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.snippets.AbstractNode;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.RootNode;
import com.ibm.sbt.playground.snippets.SnippetNode;
import com.ibm.sbt.playground.snippets.dojo.JsonTreeRenderer;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * Classes that encapsulate the business logic for Snippet document.
 * 
 * @author priand
 */
public class SnippetDocument {

	public boolean querySave(DominoDocument doc) throws NotesException {
		// Create the encoded ID for the snippet
		String category = doc.getItemValueString("Category");
		String name = doc.getItemValueString("Name");
		doc.replaceItemValue("Id",AbstractNode.encodeSnippet(category,name));
		
		return true;
	}
	
	/**
	 * Get the list of snippets as a JSON file for a Dojo tree. 
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
		View v = db.getView("AllSnippetsFlat");
		try {
			RootNode root = new RootNode();
			String snippetSearch = (String)ExtLibUtil.getViewScope().get("snippetSearch");
			if(StringUtil.isNotEmpty(snippetSearch)) {
				v.FTSearch(snippetSearch);
				ViewEntryCollection col = v.getAllEntries();
				for(ViewEntry e=col.getFirstEntry(); e!=null; e=col.getNextEntry()) {
					Vector<?> values = e.getColumnValues();
					String unid = e.getUniversalID();
					String cat = (String)values.get(0);
					String name = (String)values.get(1);
					String jspUrl = (String)values.get(2);
					CategoryNode c = findCategory(root, cat);
					SnippetNode snippet = new SnippetNode(c,name,cat,unid,jspUrl);
					c.getChildren().add(snippet);
				}
			} else { 
				v.setAutoUpdate(false);
				ViewNavigator nav = v.createViewNav();
				nav.setBufferMaxEntries(500);
				for(ViewEntry e=nav.getFirst(); e!=null; e=nav.getNext()) {
					Vector<?> values = e.getColumnValues();
					String unid = e.getUniversalID();
					String cat = (String)values.get(0);
					String name = (String)values.get(1);
					String jspUrl = (String)values.get(2);
					CategoryNode c = findCategory(root, cat);
					SnippetNode snippet = new SnippetNode(c,name,cat,unid,findUniqueUrl(c,unid,jspUrl));
					c.getChildren().add(snippet);
				}
			}
			return root;
		} finally {
			v.recycle();
		}
	}
	private String findUniqueUrl(CategoryNode cat, String unid, String jspUrl) {
		List<AbstractNode> children = cat.getChildren();
		int childrenCount = children.size();
		for(int i=0; i<childrenCount; i++) {
			if(StringUtil.equals(children.get(i).getJspUrl(),jspUrl)) {
				return unid;
			}
		}
		return jspUrl;
	}
	private CategoryNode findCategory(RootNode node, String cat) {
		String[] cats = StringUtil.splitString(cat, '/');
		return findCategory(node, cats, 0);
	}
	private CategoryNode findCategory(CategoryNode parent, String[] cats, int level) {
		List<AbstractNode> children = parent.getChildren();
		CategoryNode found = null;
		for(int i=0; i<children.size(); i++) {
			AbstractNode c = children.get(i);
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
	 * Read the snippet categories
	 */
	public String[] getAllCategories() throws NotesException {
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView("AllSnippets");
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
