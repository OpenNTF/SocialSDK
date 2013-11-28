/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.playground.assets;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.sbt.playground.vfs.VFSFile;

public class AssetBrowser {
	
	public static interface Callback {
		public boolean isCancelled();
		public void update(String fileName);
	}

	private VFSFile rootDirectory;
	private NodeFactory factory;
	private String[] runtimes;
	private String jsLibId;
	
	public AssetBrowser(VFSFile rootDirectory, NodeFactory factory) {
		this.rootDirectory = rootDirectory;
		this.factory = factory;
	}
	
	public AssetBrowser(VFSFile rootDirectory, NodeFactory factory, String[] runtimes, String jsLibId) {
        this.rootDirectory = rootDirectory;
        this.factory = factory;
        this.runtimes = runtimes;
        this.jsLibId = jsLibId;
    }
	
	public RootNode readAssets() throws IOException {
		return readAssets(new RootNode(),null);
	}

	public RootNode readAssets(Callback cb) throws IOException {
		return readAssets(new RootNode(),cb);
	}

	public RootNode readAssets(RootNode root, Callback cb) throws IOException {
		browseDirectory(rootDirectory,root,cb);
		return root;
	}
	
	private void browseDirectory(VFSFile file, CategoryNode node, Callback cb) throws IOException {
		if(cb!=null) {
			if(cb.isCancelled()) {
				return;
			}
			cb.update(StringUtil.format("Reading Folder: {0}",file.getPath()));
		}
		Set<String> snippets = new HashSet<String>();
		VFSFile[] children = file.getChildren();
		for(VFSFile s: children) {
			if(s.isFolder()) {
				String categoryName = s.getName();
				CategoryNode cn = factory.createCategoryNode(node, categoryName);
				if(cn!=null) {
					if(includeNode(cn.readGlobalProperties(s.getVFS()))){
					    node.getChildren().add(cn);
					    browseDirectory(s,cn,cb);
					}
				}
			} else if(s.isFile()) {
				String snippetName = factory.getSnippetName(s);
				if(StringUtil.isNotEmpty(snippetName) && !snippets.contains(snippetName)) {
					AssetNode sn = factory.createAssetNode(node,snippetName);
					if(sn!=null) {
						Properties p = new Properties();
						sn.readProperties(s.getVFS(), sn, p);
						if(includeNode(p)){
						    sn.getParent().getChildren().add(sn);
						    snippets.add(snippetName);
						}
					}
				}
			}
		}
	    List<Node> nodeChildren = node.getChildren();
	    if(nodeChildren == null || nodeChildren.isEmpty()){
	        CategoryNode nodeParent = node.getParent();
	        if(nodeParent != null){
	            nodeParent.getChildren().remove(node);
	        }
	    } else {
			// Sort the samples alphabetically
			(new QuickSort.JavaList(node.getChildren()) {
				@Override
				public int compare(Object o1, Object o2) {
					Node n1 = (Node)o1;
					Node n2 = (Node)o2;
					if(n1.isCategory()&&n2.isAsset()) return 1;
					if(n2.isCategory()&&n1.isAsset()) return -1;
					return StringUtil.compareToIgnoreCase(n1.getName(), n2.getName());
				}
				
			}).sort();
	    }
	}
	
	protected boolean includeNode(Properties properties){
	    if(this.runtimes == null){
            return true;
        }
        String sampleRuntimes = properties.getProperty(CategoryNode.RUNTIME_PROPERTY_KEY);
        String sampleJsLibs = properties.getProperty(CategoryNode.JS_LIB_ID_PROPERTY_KEY);
        if(StringUtil.isEmpty(sampleRuntimes) && StringUtil.isEmpty(sampleJsLibs))
            return true; // no requirements specified, include it.
        else{
            if(StringUtil.isEmpty(sampleRuntimes))    
                return jsLibMatches(sampleJsLibs);
            else if(StringUtil.isEmpty(sampleJsLibs))
                return runtimeMatches(sampleRuntimes);
            else
                return runtimeMatches(sampleRuntimes) && jsLibMatches(sampleJsLibs);
        }
	}

	/*
	 * Decide whether a node should be included in the tree.
	 * @param properties
	 * @return True if this node is to be included, false if it should not be included in the tree.
	 */
	protected boolean includeNode(String properties) {
        ReaderInputStream is = new ReaderInputStream(new StringReader(properties));
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return includeNode(p);
    }
    
	protected boolean jsLibMatches(String sampleJsLibs){
        if(this.jsLibId == null)
            return true; // no jsLibId specified in request url, include all samples.
        String sampleJsLibArray[] = sampleJsLibs.split(",");
        for(String sampleJsLib : sampleJsLibArray){
            if(this.jsLibId.contains(sampleJsLib))
                return true;
        }
        return false;
    }
    /**
     * 
     * @param sampleRuntimes comma-delimited String of runtimes the sample supports.
     * @return true if any of the runtimes matches one supported in the current environment, false otherwise.
     */
	protected boolean runtimeMatches(String sampleRuntimes){
        if(this.runtimes == null)
            return true; // no runtimes in context for some reason, include samples.
        String[] sampleRuntimesArray = sampleRuntimes.split(",");
        for(String sampleRuntime : sampleRuntimesArray){
            if(runtimesArrayContains(sampleRuntime))
                return true;
        }
        return false;
    }
    
    private boolean runtimesArrayContains(String runtimeName){
        for(String envRuntime : this.runtimes){
            if(StringUtil.equals(envRuntime, runtimeName))
                return true;
        }
        return false;
    }
}
