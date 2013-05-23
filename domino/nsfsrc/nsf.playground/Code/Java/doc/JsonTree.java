package doc;

import java.io.IOException;
import java.io.StringWriter;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class JsonTree {

	public static interface TreeModel {
		public boolean isLeaf(ViewEntry ve) throws NotesException;
	}

	public String generateAsStringHier(TreeModel tree, String viewName, boolean compact) throws NotesException, IOException {
		View view = ExtLibUtil.getCurrentDatabase().getView(viewName);
		return generateAsStringHier(tree, view, compact);
	}

	public String generateAsStringHier(TreeModel tree, View view, boolean compact) throws NotesException, IOException {
		ViewNavigator nav = view.createViewNav();
		try {
			return generateAsStringHier(tree, nav, compact);
		} finally {
			nav.recycle();
		}
	}
	
	public String generateAsStringHier(TreeModel tree, ViewNavigator nav, boolean compact) throws NotesException, IOException {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw, compact);
		generateHier(tree, nav, jw);
		jw.flush();
		return sw.toString();
	}

	public void generateHier(TreeModel tree, ViewNavigator nav, JsonWriter jw) throws NotesException, IOException {
		jw.startArray();
		jw.startArrayItem();
		jw.startObject();
			jw.startProperty("id");
				jw.outStringLiteral("_root");
			jw.endProperty();
			jw.startProperty("name");
				jw.outStringLiteral("_root");
			jw.endProperty();
			jw.startProperty("children");
				jw.startArray();
					generateNodeHier(tree, nav,jw);
				jw.endArray();
			jw.endProperty();
		jw.endObject();
		jw.endArrayItem();
		jw.endArray();
	}

	protected void generateNodeHier(TreeModel tree, ViewNavigator nav, JsonWriter jw) throws NotesException, IOException {
		jw.startArrayItem();
			generateNodeEntryHier(tree, nav, jw);
		jw.endArrayItem();
	}

	public void generateNodeEntryHier(TreeModel tree, ViewNavigator nav, JsonWriter jw) throws NotesException, IOException {
		ViewEntry ve = nav.getCurrent();
		if(ve!=null) {
			int veLevel = ve.getIndentLevel();
			while(ve!=null && veLevel==ve.getIndentLevel()) {
				// We generate the current entry
				jw.startArrayItem();
				jw.startObject();
					jw.startProperty("id");
						String id = ve.getNoteID();
						jw.outStringLiteral(id);
					jw.endProperty();
					jw.startProperty("type");
						String type = (String)ve.getColumnValues().get(1);
						jw.outStringLiteral(type);
					jw.endProperty();
					jw.startProperty("name");
						String title = (String)ve.getColumnValues().get(2);
						jw.outStringLiteral(title);
					jw.endProperty();
	
					ve = nav.getNext();
					if(ve!=null && ve.getIndentLevel()>veLevel) {
						jw.startProperty("children");
							jw.startArray();
								generateNodeHier(tree, nav, jw);
							jw.endArray();
						jw.endProperty();
						ve = nav.getCurrent();
					}
				jw.endObject();
				jw.endArrayItem();
			}
		}
	}
}
