package nsf.playground.beans;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Vector;

import javax.faces.application.FacesMessage;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.snippets.AbstractNode;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.Importer;
import com.ibm.sbt.playground.snippets.RootNode;
import com.ibm.sbt.playground.snippets.Snippet;
import com.ibm.sbt.playground.snippets.SnippetNode;
import com.ibm.sbt.playground.snippets.AbstractImportExport.VFSFile;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.extlib.beans.UserBean;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class ImportSources implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
//	private List<String> sources;
		
	public ImportSources() {
	}

	
	//
	// Import sources management
	//
	
	public Source getSource(String type, String srcName) throws Exception {
		return loadSource(type,srcName);
	}
	
	private Source loadSource(String type, String srcName) throws Exception {
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView("AllImportSourcesByType");
		try {
			Vector keys = new Vector();
			keys.add(type);
			keys.add(srcName);
			Document doc = v.getDocumentByKey(keys);
			try {
				return loadSource(doc);
			} finally {
				doc.recycle();
			}
		} finally {
			v.recycle();
		}
	}

	private Source loadSource(Document document) throws Exception {
		String type = document.getItemValueString("Type");
		if(StringUtil.equals(type, "html") || StringUtil.equals(type, "api")) {
			String name = document.getItemValueString("Name");
			String source = document.getItemValueString("Source");
			String location = document.getItemValueString("Location");
			Source src = new Source(type,name,source,location);
			return src;
		}
		throw new FacesExceptionEx(null,"Unknown import source type {0}",type);
	}
	
	//
	// Managing the import sources
	//
	public static class Source implements Serializable {
		private static final long serialVersionUID = 1L;

		private String type;
		private String name;
		private String source;
		private String location;

		public Source(String type, String name, String source, String location) {
			this.type = type;
			this.name = name;
			this.source = source;
			this.location = location;
		}
		public String getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public String getSource() {
			return source;
		}
		public String getLocation() {
			return location;
		}
		
		// HTML/JS Snippets
		public void createSnippet(VFSFile root, SnippetNode node) throws Exception {
			Snippet snippet = node.load(root);
			Database db = ExtLibUtil.getCurrentDatabase();
			Document doc = db.createDocument();
			try {
				setItemValue(doc,"Form", "CodeSnippet");
				setItemValue(doc,"Author", UserBean.get().getId()); // Should we make this private (reader field)?
				setItemValue(doc,"Id", node.getUnid());
				setItemValue(doc,"Category", node.getCategory());
				setItemValue(doc,"Name", node.getName());
				setItemValue(doc,"Description", snippet.getDescription());
				setItemValue(doc,"ImportSource", getName());
				setItemValue(doc,"Tags", snippet.getTags());
				setItemValue(doc,"Documentation", snippet.getDocumentation());
				setItemValue(doc,"Html", snippet.getHtml());
				setItemValue(doc,"Css", snippet.getCss());
				setItemValue(doc,"JavaScript", snippet.getJs());
				
				doc.save();
			} finally {
				doc.recycle();
			}
		}
		public void importSnippets(VFSFile root, AbstractNode node) throws Exception {
			if(node.isCategory()) {
				CategoryNode cn = (CategoryNode)node;
				// Browse recursively...
				List<AbstractNode> children = cn.getChildren();
				for(AbstractNode n: children) {
					importSnippets(root,n);
				}
			} else if(node.isSnippet()) {
				SnippetNode sn = (SnippetNode)node;
				createSnippet(root,sn);
			}
		}
		public void importSnippets() throws Exception {
			if(!StringUtil.equals(getType(), "html")) {
				throw new FacesExceptionEx(null,"Import snippet is not available for a type {0}",getType());
			}
			if(StringUtil.equals(getSource(), "file")) {
				File baseDir = new File(getLocation());
				if(!baseDir.exists()) {
					throw new FacesExceptionEx(null,"Import directory {0} does not exist",getLocation());
				}
				Importer.VFSFile rootDir = Importer.createVFSFile(baseDir);
				Importer.NodeFactory factory = new Importer.DefaultNodeFactory(); 
				Importer imp = new Importer(rootDir,factory,Importer.HTMLJS_EXTENSIONS);
				RootNode root = imp.readSnippets();
				importSnippets(rootDir,root);
				return;
			}
			throw new FacesExceptionEx(null,"Import snippet is not available for a source {0}",getSource());
		}

		
		// API Explorer

		
		// Utilities

		private void setItemValue(Document doc, String name, Object v) throws Exception {
			if(v==null) {
				return;
			}
			Object dominoValue = toDominoType(v);
			doc.replaceItemValue(name, dominoValue);
		}
		private Object toDominoType(Object v) throws Exception {
			if(v.getClass().isArray()) {
				int length = Array.getLength(v);
				if(length>0) {
					Vector vec = new Vector(length);
					for(int i=0; i<length; i++) {
						vec.add(toDominoType(Array.get(v, i)));
					}
					return vec;
				}
			}
			return v;
		}
	}
	
	public void importSnippets(String sourceName) throws Exception {
		try {
			Source source = getSource("html",sourceName);
			deleteSnippets(source);
			importSnippets(source);
			
			String msg = StringUtil.format("Snippets sucessfully imported from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
			String msg = StringUtil.format("Error while importing snippets from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		}	
	}
	
	public void deleteSnippets(String sourceName) throws Exception {
		try {
			Source source = getSource("html",sourceName);
			deleteSnippets(source);
			
			String msg = StringUtil.format("Snippets sucessfully deleted from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
			String msg = StringUtil.format("Error while deleting snippets from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		}	
	}

	private void importSnippets(Source source) throws Exception {
		source.importSnippets();
	}
	private void deleteSnippets(Source source) throws Exception {
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView("AllSnippetsByImportSource");
		try {
			v.getAllEntriesByKey(source.getName()).removeAll(true);
		} finally {
			v.recycle();
		}
	}
}
