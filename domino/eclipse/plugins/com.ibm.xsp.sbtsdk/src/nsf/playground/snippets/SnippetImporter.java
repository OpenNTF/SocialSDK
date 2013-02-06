package nsf.playground.snippets;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Vector;

import javax.faces.application.FacesMessage;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;
import nsf.playground.jobs.AsyncAction;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetAssetNode;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.FileVFS;
import com.ibm.sbt.playground.vfs.GitVFS;
import com.ibm.sbt.playground.vfs.VFS;
import com.ibm.sbt.playground.vfs.VFSFile;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;

public class SnippetImporter {
	
	private Database db;
	
	public SnippetImporter(Database db) {
		this.db = db;
	}

	
	//
	// Import sources management
	//
	
	public Source getSource(String type, String srcName) throws Exception {
		return loadSource(type,srcName);
	}
	
	private Source loadSource(String type, String srcName) throws Exception {
		View v = db.getView("AllImportSourcesByType");
		try {
			Vector<Object> keys = new Vector<Object>();
			keys.add(type);
			keys.add(srcName);
			Document doc = v.getDocumentByKey(keys);
			try {
				if(doc!=null) {
					return loadSource(doc);
				}
				return null;
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
			String userName = document.getItemValueString("User");
			String password = document.getItemValueString("Password");
			Source src = new Source(type,name,source,location,userName,password);
			return src;
		}
		throw new FacesExceptionEx(null,"Unknown import source type {0}",type);
	}
	
	
	//
	// Managing the import sources
	//
	private class Source implements Serializable {
		private static final long serialVersionUID = 1L;

		private String type;
		private String name;
		private String source;
		private String location;
		private String userName;
		private String password;
		
		public Source(String type, String name, String source, String location, String userName, String password) {
			this.type = type;
			this.name = name;
			this.source = source;
			this.location = location;
			this.userName = userName;
			this.password = password;
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
		public String getUserName() {
			return userName;
		}
		public String getPassword() {
			return password;
		}
		
		// HTML/JS Snippets
		public void createSnippet(VFSFile root, JSSnippetAssetNode node) throws Exception {
			JSSnippet snippet = node.load(root);
			Document doc = db.createDocument();
			try {
				setItemValue(doc,"Form", "CodeSnippet");
				setItemValue(doc,"Author", doc.getParentDatabase().getParent().getUserName()); // Should we make this private (reader field)?
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
		public int importSnippets(VFSFile root, Node node, final AsyncAction action) throws Exception {
			int count = 0;
			if(node.isCategory()) {
				CategoryNode cn = (CategoryNode)node;
				// Browse recursively...
				List<Node> children = cn.getChildren();
				for(Node n: children) {
					count += importSnippets(root,n,action);
				}
			} else if(node.isAsset()) {
				if(action!=null) {
					action.updateTask(StringUtil.format("Importing Snippet: {0}", node.getPath()));
				}
				JSSnippetAssetNode sn = (JSSnippetAssetNode)node;
				createSnippet(root,sn);
				count++;
			}
			return count;
		}
		public int importSnippets(final AsyncAction action) throws Exception {
			if(!StringUtil.equals(getType(), "html")) {
				throw new FacesExceptionEx(null,"Import action is not available for a type {0}",getType());
			}
			VFS vfs = createImportVFS();
			VFSFile rootDir = vfs.getRoot();
			
			
			AssetBrowser a = new AssetBrowser(rootDir, new JSSnippetNodeFactory());
			
			AssetBrowser.Callback cb = null;
			if(action!=null) {
				cb = new AssetBrowser.Callback() {
					public void update(String statusString) {
						action.updateTask(statusString);
					}
					public boolean isCancelled() {
						return action.isCancelled();
					}
				};
			}
			RootNode root = a.readAssets(cb);
			if(action!=null && action.isCancelled()) {
				return 0;
			}
			return importSnippets(rootDir,root,action);
		}
		private VFS createImportVFS() {
			if(StringUtil.equals(getSource(), "file")) {
				File baseDir = new File(getLocation().trim());
				if(!baseDir.exists()) {
					throw new FacesExceptionEx(null,"Import directory {0} does not exist",getLocation());
				}
				FileVFS vfs = new FileVFS(baseDir); 
				return vfs;
			} else if(StringUtil.equals(getSource(), "github")) {
				String basePath = getLocation().trim();
				if(StringUtil.isEmpty(basePath)) {
					throw new FacesExceptionEx(null,"GitHub: Location is empty",getLocation());
				}
				GitVFS vfs = new GitVFS(location,getUserName(),getPassword()); 
				return vfs;
			}
			throw new FacesExceptionEx(null,"Import action is not available for a source {0}",getSource());
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
					Vector<Object> vec = new Vector<Object>(length);
					for(int i=0; i<length; i++) {
						vec.add(toDominoType(Array.get(v, i)));
					}
					return vec;
				}
			}
			return v;
		}
	}
	
	public void importSnippets(String sourceName, AsyncAction action) throws Exception {
		try {
			Source source = getSource("html",sourceName);
			if(action!=null) {
				action.updateTask("Deleting Snippets");
			}
			deleteSnippets(source,action);
			if(action!=null) {
				if(action.isCancelled()) {
					return;
				}
				action.updateCompletion(100,50);
				action.updateTask("Importing Snippets");
			}
			
			int count = importSnippets(source,action);
			String msg = StringUtil.format("{0} snippets successfully imported from source {1}", count, sourceName);

			if(action!=null) {
				action.updateCompletion(100,100);
				action.updateTask(msg);
			}

			FacesContextEx ctx = FacesContextEx.getCurrentInstance();
			if(ctx!=null) {
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
				FacesContextEx.getCurrentInstance().addMessage(null, m);
			}
		} catch(Exception ex) {
			FacesContextEx ctx = FacesContextEx.getCurrentInstance();
			if(ctx!=null) {
				Platform.getInstance().log(ex);
				String msg = StringUtil.format("Error while importing snippets from source {0}", sourceName);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
				FacesContextEx.getCurrentInstance().addMessage(null, m);
			}
		}	
	}
	
	public void deleteSnippets(String sourceName) throws Exception {
		try {
			Source source = getSource("html",sourceName);
			deleteSnippets(source,null);
			
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

	private int importSnippets(Source source, AsyncAction action) throws Exception {
		return source.importSnippets(action);
	}
	private void deleteSnippets(Source source, AsyncAction action) throws Exception {
		View v = db.getView("AllSnippetsByImportSource");
		try {
			v.getAllEntriesByKey(source.getName()).removeAll(true);
		} finally {
			v.recycle();
		}
	}
}
