package doc;

import java.io.StringReader;
import java.util.List;

import javax.faces.application.FacesMessage;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TextUtil;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.xsp.component.UIInputEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

import doc.JsonTree.TreeModel;

public class DocAPI {

	public static class DocTreeModel implements TreeModel {
		public boolean isLeaf(ViewEntry ve) throws NotesException {
			String form = (String)ve.getColumnValues().get(1);
			return StringUtil.endsWithIgnoreCase(form, "API");
		}
	}
	
	public String getEntriesAsJson() throws Exception {
		JsonTree tree = new JsonTree();
		return tree.generateAsStringHier(new DocTreeModel(), "AllDocumentation", true);
	}

	public String getProducts() throws Exception {
		return "Connections,SmartCloud,Domino,WCM";
	}
	
	public void moveUp(String noteID) throws Exception {
		swap(noteID,true);
	}
	public void moveDown(String noteID) throws Exception {
		swap(noteID,false);
	}
	private boolean swap(String noteID, boolean previous) throws Exception {
		// Is there a faster way?
		View view = ExtLibUtil.getCurrentDatabase().getView("AllDocumentation");
		//view.setAutoUpdate(false);
		ViewNavigator vn = view.createViewNav();		
		try {
			for(ViewEntry ve=vn.getFirst(); ve!=null; ve=vn.getNext(ve)) {
				if(ve.getNoteID().equals(noteID)) {
					int docIndent = ve.getIndentLevel();
					Document doc = ve.getDocument();
					ve = previous ? vn.getPrev(ve) : vn.getNext(ve);
					if(ve!=null) {
						Document other = ve.getDocument();
						if(ve.getIndentLevel()==docIndent) {
							Object ts = other.getItemValue("OrderTS");
							other.replaceItemValue("OrderTS",doc.getItemValue("OrderTS"));
							doc.replaceItemValue("OrderTS",ts);
							doc.save();
							other.save();
							view.refresh();
							return true;
						}
					}
					return false;
				}
			}
		} finally {
			vn.recycle();
		}
		return false;
	}
	
	public void remove(DominoDocument doc, String fieldName, int idx) throws Exception {
		String json = doc.getItemValueString(fieldName);
		if(!StringUtil.isSpace(json)) {
			List<Object> array = (List<Object>)JsonParser.fromJson(JsonJavaFactory.instance,json);
			if(idx<array.size()) {
				array.remove(idx);
				doc.replaceItemValue(fieldName, JsonGenerator.toJson(JsonJavaFactory.instance,array));
			}
		}
	}

	public boolean isEmpty(DominoDocument doc, String fieldName) throws Exception {
		if(doc!=null) {
			Item item = doc.getDocument().getFirstItem(fieldName);
			if(item!=null) {
				String content;
				MIMEEntity e = item.getMIMEEntity();
				if(e!=null) {
					content = e.getContentAsText();
				} else { 
					content = item.getText();
				}
				if(!StringUtil.isSpace(content)) {
					// Should handle empty json objects/arrays as well
					content = content.trim();
					return content.equals("[]") || content.equals("{}");
				}
			}
		}
		return true;
	}
	
	public String generateValueList(String list) {
		if(StringUtil.isEmpty(list)) {
			return "";
		}
		StringBuilder b = new StringBuilder();
		b.append("<ul>");
		String[] values = StringUtil.splitString(list, '\n');
		for(int i=0; i<values.length; i++) {
			String s = values[i];
			if(StringUtil.isNotEmpty(s)) {
				String name = s;
				String desc = null;
				int pos = s.indexOf('|');
				if(pos>=0) {
					name = s.substring(0,pos);
					desc = s.substring(pos+1);
				}
				b.append("<li>");
				b.append("<b>");
				b.append(TextUtil.toXMLString(name));
				b.append("</b>");
				if(StringUtil.isNotEmpty(desc)) {
					b.append(": ");
					b.append(TextUtil.toXMLString(desc));
				}
				b.append("</li>");
			}
		}
		b.append("</ul>");
		return b.toString();
	}
	
	public void prettify(UIInputEx c) {
		String value = c.getValueAsString();
		value = prettify(value,false);
		if(value!=null) {
			c.setValue(value);
		} else {
			FacesContextEx ctx = FacesContextEx.getCurrentInstance(); 
			String msg = "Invalid file content";
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			ctx.addMessage(c != null ? c.getClientId(ctx) : null, m);
		}
	}
	public void compact(UIInputEx c) {
		String value = c.getValueAsString();
		value = prettify(value,true);
		if(value!=null) {
			c.setValue(value);
		} else {
			FacesContextEx ctx = FacesContextEx.getCurrentInstance(); 
			String msg = "Invalid file content";
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			ctx.addMessage(c != null ? c.getClientId(ctx) : null, m);
		}
	}
	public String prettify(String source,boolean compact) {
		try {
			source = source.trim();
			if(source.startsWith("[") || source.startsWith("{")) {
				return prettifyJSON(source,compact);
			} else if(source.startsWith("<")) {
				return prettifyXML(source,compact);
			}
			return source;
		} catch(Exception e) {
			return null;
		}
	}
	private String prettifyJSON(String source,boolean compact) throws Exception{
		Object o = JsonParser.fromJson(JsonJavaFactory.instance,new StringReader(source));
		return JsonGenerator.toJson(JsonJavaFactory.instance, o, compact);
	}
	private String prettifyXML(String source,boolean compact) throws Exception{
		org.w3c.dom.Document d = DOMUtil.createDocument(source);
		return DOMUtil.getXMLString(d,compact);
	}
}
