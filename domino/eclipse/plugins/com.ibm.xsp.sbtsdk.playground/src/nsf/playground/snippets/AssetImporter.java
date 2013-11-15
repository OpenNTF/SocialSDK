package nsf.playground.snippets;

import java.io.File;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.faces.application.FacesMessage;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;
import nsf.playground.jobs.AsyncAction;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.Node;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.vfs.FileVFS;
import com.ibm.sbt.playground.vfs.GitVFS;
import com.ibm.sbt.playground.vfs.VFS;
import com.ibm.sbt.playground.vfs.VFSFile;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.context.FacesContextEx;

/**
 * Base class for importing assets.
 * 
 * @author priand
 *
 */
public abstract class AssetImporter {

	public static AssetImporter createImporter(String type, Database db) {
		if(StringUtil.equals(type, JavaScriptSnippetImporter.TYPE)) {
			return new JavaScriptSnippetImporter(db);
		} else if(StringUtil.equals(type, JavaSnippetImporter.TYPE)) {
			return new JavaSnippetImporter(db);
		} else if(StringUtil.equals(type, XPagesSnippetImporter.TYPE)) {
			return new XPagesSnippetImporter(db);
		} else if(StringUtil.equals(type, GadgetSnippetImporter.TYPE)) {
			return new GadgetSnippetImporter(db);
		} else if(StringUtil.equals(type, APIImporter.TYPE)) {
			return new APIImporter(db);
		}
		return null;
	}
	
	private Database db;
	
	public AssetImporter(Database db) {
		this.db = db;
	}
	
	public Database getDatabase() {
		return db;
	}
	
	
	//
	// Methods to be implemented by the concrete classes
	//
	
	// "html", "api"...
	protected abstract String getAssetType(); 
	protected abstract String getAssetForm(); 

	protected abstract NodeFactory getNodeFactory(); 
	
	protected abstract void saveAsset(ImportSource source, VFSFile root, AssetNode node, Asset asset) throws Exception;
	
	
	////////////////////////////////////////////////////////////////////////////////
	//
	// Import sources management
	//
	////////////////////////////////////////////////////////////////////////////////
	
	protected ImportSource getSource(String type, String srcName) throws Exception {
		return loadSource(type,srcName);
	}
	
	protected ImportSource loadSource(String type, String srcName) throws Exception {
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

	protected ImportSource loadSource(Document document) throws Exception {
		//String type = document.getItemValueString("Type");
		String name = document.getItemValueString("Name");
		String source = document.getItemValueString("Source");
		String location = document.getItemValueString("Location");
		String userName = document.getItemValueString("User");
		String password = document.getItemValueString("Password");
		ImportSource src = new ImportSource(name,source,location,userName,password);
		return src;
	}
	protected int importAssets(ImportSource source, VFSFile root, Node node, final AsyncAction action) throws Exception {
		int count=0;
		if(node.isCategory()) {
			CategoryNode cn=(CategoryNode) node;
			// Browse recursively...
			List<Node> children=cn.getChildren();
			for(Node n : children) {
				count+=importAssets(source, root, n, action);
			}
		} else if(node.isAsset()) {
			if(action!=null) {
				action.updateTask(StringUtil.format("Importing Asset: {0}", node.getPath()));
			}
			Asset asset = loadAsset(source, root, (AssetNode) node);
			if(asset!=null && shouldImport(asset)) {
				saveAsset(source, root, (AssetNode)node, asset);
				count++;
			}
		}
		return count;
	}

	protected Asset loadAsset(ImportSource source, VFSFile root, AssetNode node) throws Exception {
		return node.load(root);
	}
	
	protected boolean shouldImport(Asset asset) {
		String pub = asset.getProperty("publish");
		if(StringUtil.equals(pub, "false")) {
			return false;
		}
		return true;
	}

	
	////////////////////////////////////////////////////////////////////////////////
	//
	// Import action
	//
	////////////////////////////////////////////////////////////////////////////////

	public void importAssets(String sourceName, AsyncAction action) throws Exception {
		try {
			ImportSource source = getSource(getAssetType(),sourceName);
			if(action!=null) {
				action.updateTask("Deleting assets");
			}
			deleteAssets(source,action);
			if(action!=null) {
				if(action.isCancelled()) {
					return;
				}
				action.updateCompletion(100,50);
				action.updateTask("Importing assets");
			}
			
			int count = importAssets(source,action);
			String msg = StringUtil.format("{0} assets successfully imported from source {1}", count, sourceName);

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
			String msg = StringUtil.format("Error while importing assets from source \"{0}\"", sourceName);

			FacesContextEx ctx = FacesContextEx.getCurrentInstance();
			if(ctx!=null) {
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
				FacesContextEx.getCurrentInstance().addMessage(null, m);
			}

			Platform.getInstance().log(ex);

			if(action!=null) {
				action.updateException(ex,msg);
			}
		}	
	}
	protected int importAssets(ImportSource source, final AsyncAction action) throws Exception {
		VFS vfs=createImportVFS(source);
		VFSFile rootDir=vfs.getRoot();

		AssetBrowser a=new AssetBrowser(rootDir, getNodeFactory()) {
			// Import all the nodes to the DB, regardless of the constraints (endpoints, jslibs...)
			protected boolean includeNode(Properties properties){
				return true;
			}
		};

		AssetBrowser.Callback cb=null;
		if(action!=null) {
			cb=new AssetBrowser.Callback() {
				public void update(String statusString) {
					action.updateTask(statusString);
				}

				public boolean isCancelled() {
					return action.isCancelled();
				}
			};
		}
		RootNode root=a.readAssets(cb);
		if(action!=null&&action.isCancelled()) {
			return 0;
		}
		return importAssets(source, rootDir, root, action);
	}
	
	protected VFS createImportVFS(ImportSource source) {
		if(StringUtil.equals(source.getSource(), "file")) {
			String location=source.getLocation().trim();
			File baseDir=new File(location);
			if(!baseDir.exists()) {
				throw new FacesExceptionEx(null, "Import directory {0} does not exist", location);
			}
			FileVFS vfs=new FileVFS(baseDir);
			return vfs;
		} else if(StringUtil.equals(source.getSource(), "github")) {
			String location=source.getLocation().trim();
			if(StringUtil.isEmpty(location)) {
				throw new FacesExceptionEx(null, "GitHub: Location is empty", location);
			}
			GitVFS vfs=new GitVFS(location, source.getUserName(), source.getPassword());
			return vfs;
		}
		throw new FacesExceptionEx(null, "Import action is not available for a source type {0}", source.getSource());
	}

	
	////////////////////////////////////////////////////////////////////////////////
	//
	// Delete action
	//
	////////////////////////////////////////////////////////////////////////////////
	public void deleteAssets(String sourceName) throws Exception {
		try {
			ImportSource source = getSource(getAssetType(),sourceName);
			deleteAssets(source,null);
			
			String msg = StringUtil.format("Assets successfully deleted from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
			String msg = StringUtil.format("Error while deleting assets from source {0}", sourceName);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			FacesContextEx.getCurrentInstance().addMessage(null, m);
		}	
	}

	protected void deleteAssets(ImportSource source, AsyncAction action) throws Exception {
		View v = db.getView("AllSnippetsByImportSource");
		try {
			Vector key = new Vector();
			key.add(getAssetForm());
			key.add(source.getName());
			v.getAllEntriesByKey(key).removeAll(true);
		} finally {
			v.recycle();
		}
	}
	
	
	
	//
	// Utilities to be used by subclasses
	//
	protected void setItemValue(Document doc, String name, Object v) throws Exception {
		if(v==null) {
			return;
		}
		Object dominoValue=toDominoType(v);
		doc.replaceItemValue(name, dominoValue);
	}
	protected void setItemValueRichText(Document doc, String name, String v) throws Exception {
		setItemValue(doc, name, v);
//		if(v==null) {
//			return;
//		}
//        RichTextItem rti = doc.createRichTextItem(name);
//        rti.appendText(v);
	}
	protected void setItemValues(Document doc, String name, String value) throws Exception {
		if(StringUtil.isNotEmpty(value)) {
			String[] t = StringUtil.splitString(value, ',');
			setItemValue(doc, name, t);
		}
	}
	protected Object toDominoType(Object v) throws Exception {
		if(v.getClass().isArray()) {
			int length=Array.getLength(v);
			if(length>0) {
				Vector<Object> vec=new Vector<Object>(length);
				for(int i=0; i<length; i++) {
					vec.add(toDominoType(Array.get(v, i)));
				}
				return vec;
			}
		}
		return v;
	}
	
}
