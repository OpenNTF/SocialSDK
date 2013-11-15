package nsf.playground.beans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;
import nsf.playground.environments.PlaygroundEnvironment;

import com.ibm.commons.runtime.util.URLEncoding;
import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.std.ArrayObject;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSDefaultObject;
import com.ibm.jscript.types.FBSString;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.dojo.JsonTreeRenderer;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 * Base class for beans managing assets.
 * 
 * @author priand
 */
public abstract class AssetBean {

	public String encodeUrl(String s) throws UnsupportedEncodingException, IOException {
		return URLEncoding.encodeURIString(s, "utf-8", 0, true);
	}
	
	protected abstract String getAssetForm(); 
	
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
	protected RootNode readSnippetsNodes() throws NotesException {
		this.assetLoaderEnvironment = PlaygroundEnvironment.getCurrentEnvironment();
		try {
			//PlaygroundEnvironment env = DataAccessBean.get().findCurrentEnvironment();
			Database db = ExtLibUtil.getCurrentDatabase();
			View v = db.getView("AllSnippetsFlat");
			try {
				RootNode root = new RootNode();
				String apisSearch = (String)ExtLibUtil.getViewScope().get("assetSearch");
				if(StringUtil.isNotEmpty(apisSearch)) {
					v.FTSearch(apisSearch);
					//ViewEntryCollection col = v.getAllEntriesByKey(getAssetForm());
					ViewEntryCollection col = v.getAllEntries();
					for(ViewEntry e=col.getFirstEntry(); e!=null; e=col.getNextEntry()) {
						Vector<?> values = e.getColumnValues();
						String notesUnid = e.getUniversalID();
						// 2 type
						String type = (String)values.get(0);
						if(!StringUtil.equals(type, getAssetForm())) {
							// Ignore if it is not of the right type
							continue;
						}
						String cat = (String)values.get(1);
						String name = (String)values.get(2);
						String jspUrl = (String)values.get(3);
						// 4 ImportSource
						// 5 CreateDate
						// 6 Description
						String filterRuntimes = (String)values.get(7);
						String filterLibraries = (String)values.get(8);
						if(acceptAsset(e, filterRuntimes, filterLibraries)) {
							CategoryNode c = findCategory(root, cat);
							AssetNode node = createAssetNode(notesUnid,c,name,cat,jspUrl);
							node.setTooltip((String)values.get(6));
							c.getChildren().add(node);
						}
					}
				} else { 
					v.setAutoUpdate(false);
					ViewNavigator nav = v.createViewNavFromCategory(getAssetForm());
					nav.setBufferMaxEntries(500);
					for(ViewEntry e=nav.getFirst(); e!=null; e=nav.getNext()) {
						Vector<?> values = e.getColumnValues();
						String notesUnid = e.getUniversalID();
						// 2 type
						String cat = (String)values.get(1);
						String name = (String)values.get(2);
						String assetId = (String)values.get(3);
						// 4 ImportSource
						// 5 CreateDate
						// 6 Description
						String filterRuntimes = (String)values.get(7);
						String filterLibraries = (String)values.get(8);
						if(acceptAsset(e, filterRuntimes, filterLibraries)) {
							CategoryNode c = findCategory(root, cat);
							AssetNode node = createAssetNode(notesUnid,c,name,cat,findUniqueUrl(c,notesUnid,assetId));
							node.setTooltip((String)values.get(6));
							c.getChildren().add(node);
						}
					}
				}
				return root;
			} finally {
				v.recycle();
			}
		} finally {
			this.assetLoaderEnvironment = null;
		}
	}
	// Just for readSnippetsNodes
	private transient PlaygroundEnvironment assetLoaderEnvironment;
	protected boolean acceptAsset(ViewEntry e, String filterRuntimes, String filterLibraries) {
		// Check for the runtimes
		// If at least one runtime is available, then we do accept the snippet
		if(assetLoaderEnvironment!=null && StringUtil.isNotEmpty(filterRuntimes)) {
			boolean ok = false;
			String[] eps = StringUtil.splitString(filterRuntimes, ',', true);
			for(int i=0; i<eps.length; i++) {
				String ep = eps[i];
				if(runtimeExists(ep)) {
					ok = true;
				}
			}
			if(!ok) {
				return false;
			}
		}
		
		return true;
	}
	protected boolean runtimeExists(String name) {
		String[] runtimes = assetLoaderEnvironment.getRuntimesArray();
		if(runtimes!=null && runtimes.length>0) {
			for(int i=0; i<runtimes.length; i++) {
				if(StringUtil.equalsIgnoreCase(runtimes[i], name)) {
					return true;
				}
			}
			return false;
		}
		return true;
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
		if(StringUtil.isEmpty(cat)) {
			return node;
		}
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
		View v = db.getView("AllSnippets");
		ViewNavigator nav = v.createViewNavFromCategory(getAssetForm());
		try {
			nav.setMaxLevel(1);
			//nav.setCacheSize(128);
			List<String> categories = new ArrayList<String>();
			for(ViewEntry ve=nav.getFirst(); ve!=null; ve=nav.getNext(ve)) {
				categories.add((String)ve.getColumnValues().get(1));
			}
			return categories.toArray(new String[categories.size()]);
		} finally {
			nav.recycle();
		}
	}
	
    private static Pattern paramsPattern = Pattern.compile("%\\{(.*?)\\}");
    
	public FBSDefaultObject extractParams(String[] ss) throws InterpretException {
		PlaygroundEnvironment env = PlaygroundEnvironment.getCurrentEnvironment();
		
		ArrayObject params = new ArrayObject(JavaScriptUtil.getJSContext());
		if(ss!=null) {
			for(int i=0; i<ss.length; i++) {
			    Matcher paramsMatcher = paramsPattern.matcher(ss[i]);
			    while(paramsMatcher.find()){
			    	String s = paramsMatcher.group(1);
			    	String[] all = StringUtil.splitString(s, '|', true);
					FBSDefaultObject o = new ObjectObject(JavaScriptUtil.getJSContext());
			    	for(int j=0; j<all.length; j++) {
			    		String[] p = StringUtil.splitString(all[j], '=', true);
			    		if(p.length==1) {
			    			o.put("name", FBSString.get(p[0]));
			    		} else if(p.length>1) {
			    			o.put(p[0],FBSString.get(p[1]));
			    		}
			    	}
			    	FBSString propName = (FBSString)o.get("name");
			    	if(propName!=null) {
			    		String v = env.getPropertyValueByName(propName.stringValue());
			    		if(StringUtil.isNotEmpty(v)) {
			    			o.put("value", FBSString.get(v));
			    		}
		    		}
			    	params.addArrayValue(o);
			    }
			}
		}
		return params;
	}
}
