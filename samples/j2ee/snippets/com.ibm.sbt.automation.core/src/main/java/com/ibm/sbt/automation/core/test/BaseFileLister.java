package com.ibm.sbt.automation.core.test;

import java.io.File;
import java.io.IOException;


import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.NodeFactory;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippet;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetNodeFactory;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippet;
import com.ibm.sbt.playground.assets.jssnippets.JSSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.FileVFS;
import com.ibm.sbt.playground.vfs.VFSFile;

public class BaseFileLister {

	public static String jsRootPath = System.getProperty("user.dir")+"/test/com/ibm/javascript/units/jsTests";
	static String javaRootPath = "/samples/java/";
	
	/**
	 * Get a JSSnippet by name, if it is not found in sbt.sample.web it will look in sbtx.sample.web.
	 * 
	 * @param snippetName
	 * @return JSSnippet corresponding to snippetName 
	 */
	public static JSSnippet getJsSnippet(String snippetName) {
		JSSnippet snippet = (JSSnippet) getAsset(snippetName, new JSSnippetNodeFactory(), jsRootPath);
        return snippet;
    }
	
	/**
	 * Get a RootNode object which allows JSSnippets to be searched and manipulated.
	 * 
	 * @param context
	 * @return RootNode of the local JSSnippets.
	 */
	public static RootNode getJsSnippets(){
		return getSnippets(new JSSnippetNodeFactory(), jsRootPath);
	}
	
	/**
	 * Get JS snippets in JSON format, including sbtx.sample.web's JS snippets.
	 * 
	 * @param context
	 * @param request
	 * @return JSSnippets in JSON format. 
	 */
	public static String getJsSnippetsAsJson() {
	    RootNode root = getJsSnippets();
	    String json = root.getAsJson();
	    return json;
	}

	/**
	 * Get a JavaSnippet by name.
	 * 
	 * @param context
	 * @param request
	 * @param snippetName
	 * @return JavaSnippet corresponding to snippetName 
	 */
	public static JavaSnippet getJavaSnippet(String snippetName) {
		JavaSnippet result = (JavaSnippet) getAsset(snippetName, new JavaSnippetNodeFactory(), javaRootPath);
        return result;
    }
	
	/**
	 * Get a RootNode object which allows JavaSnippets to be searched and manipulated.
	 * 
	 * @param context
	 * @return RootNode of the local JavaSnippets.
	 */
	public static RootNode getJavaSnippets(){
		return getSnippets(new JavaSnippetNodeFactory(), javaRootPath);
	}
	
	/**
	 * Returns the root VFSFile for JS Samples.
	 * 
	 * @param context
	 * @return
	 */
	public static VFSFile getJsRootFile(){
		return getRootFile(jsRootPath);
	}

	/**
	 * Returns an Asset based on its name, NodeFactory type and root path.
	 * 
	 * @param context
	 * @param request
	 * @param assetName
	 * @param nodeFactory
	 * @param rootPath
	 * @return
	 */
	private static Asset getAsset(String assetName, NodeFactory nodeFactory, String rootPath){
		try {
	        RootNode root = getSnippets(nodeFactory, rootPath);
	        return root.loadAsset(getRootFile(rootPath), assetName);
	    } catch (IOException ioe) {
	        return null;
	    }
	}

	/**
	 * Returns a VFSFile for the given path and context.
	 * 
	 * @param context
	 * @param relPath The folder path.
	 * @return VFSFile from the relPath and context.
	 */
	private static VFSFile getRootFile(String relPath) {
		FileVFS vfs = new FileVFS(new File(relPath));
		return vfs.getRoot();
	}

	/**
	 * Get a RootNode object corresponding to the NodeFactory type and ServletContext
	 * 
	 * @param context
	 * @param nodeFactory The NodeFactory of the type of snippet to be retrieved.
	 * @param path The path to the root of the snippets to be retrieved.
	 * @return RootNode of the Snippets.
	 */
	private static RootNode getSnippets( NodeFactory nodeFactory, String path){
		RootNode root;
		try {
			VFSFile file = getRootFile(path);
			root = readAssets(file, nodeFactory);
		} catch(IOException ex) {
			root = new RootNode();
		}
		return root;
	}

	/**
	 * A method which will, for a given root folder and NodeFactory, return the corresponding RootNode of Snippets.
	 * 
	 * @param context
	 * @param file The root folder of the snippets.
	 * @param nodeFactory The NodeFactory corresponding to the snippet type.
	 * @return RootNode of snippets, which allows search and manipulation of snippets.
	 * @throws IOException
	 */
	private static RootNode readAssets(VFSFile file, NodeFactory nodeFactory) throws IOException{
	    AssetBrowser imp = new AssetBrowser(file, nodeFactory);
	    
		return (RootNode)imp.readAssets(new RootNode(), null);
	}
}
