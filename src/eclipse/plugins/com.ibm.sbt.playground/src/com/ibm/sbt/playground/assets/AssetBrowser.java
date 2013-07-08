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
import java.util.Properties;
import java.util.Set;
import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.playground.vfs.VFSFile;

public class AssetBrowser {
	
	public static interface Callback {
		public boolean isCancelled();
		public void update(String fileName);
	}

	private VFSFile rootDirectory;
	private NodeFactory factory;
	private String[] extensions;
	private SBTEnvironment.Endpoint[] endpoints;
	private String jsLibId;
	
	public AssetBrowser(VFSFile rootDirectory, NodeFactory factory) {
		this.rootDirectory = rootDirectory;
		this.factory = factory;
		this.extensions = factory.getAssetExtensions();
	}
	
	public AssetBrowser(VFSFile rootDirectory, NodeFactory factory, SBTEnvironment.Endpoint[] endpoints, String jslibId) {
        this.rootDirectory = rootDirectory;
        this.factory = factory;
        this.extensions = factory.getAssetExtensions();
        this.endpoints = endpoints;
        this.jsLibId = jslibId;
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
				CategoryNode cn = factory.createCategoryNode(node, s.getName());
				if(includeNode(cn.readGlobalProperties(s.getVFS()))){
				    node.getChildren().add(cn);
				    browseDirectory(s,cn,cb);
				}
			} else if(s.isFile()) {
				String ext = getExtension(s.getName(), extensions);
				if(ext!=null) {
					String fileName = getNameWithoutExtension(s.getName(), ext);
					if(!snippets.contains(fileName)) {
						AssetNode sn = factory.createAssetNode(node,fileName);
						node.getChildren().add(sn);
						snippets.add(fileName);
					}
				}
			}
		}
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

	/*
	 * Decide whether a node should be included in the tree.
	 * @param properties
	 * @return True if this node is to be included, false if it should not be included in the tree.
	 */
    private boolean includeNode(String properties) {
        if(this.endpoints == null){
            return false;
        }
        ReaderInputStream is = new ReaderInputStream(new StringReader(properties));
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String sampleEndpoints = p.getProperty(CategoryNode.ENDPOINT_PROPERTY_KEY);
        String sampleJsLibs = p.getProperty(CategoryNode.JS_LIB_ID_PROPERTY_KEY);
        if(StringUtil.isEmpty(sampleEndpoints) && StringUtil.isEmpty(sampleJsLibs))
            return true; // no requirements specified, include it.
        else{
            if(StringUtil.isEmpty(sampleEndpoints))    
                return jsLibMatches(sampleJsLibs);
            else if(StringUtil.isEmpty(sampleJsLibs))
                return endpointMatches(sampleEndpoints);
            else
                return endpointMatches(sampleEndpoints) && jsLibMatches(sampleJsLibs);
        }
        
    }
    
    private boolean jsLibMatches(String sampleJsLibs){
        if(this.jsLibId == null)
            return true; // no jsLibId specified in request url, include all samples.
        String sampleJsLibArray[] = sampleJsLibs.split(",");
        for(String sampleJsLib : sampleJsLibArray){
            if(this.jsLibId.contains(sampleJsLib))
                return true;
        }
        return false;
    }
    
    private boolean endpointMatches(String sampleEndpoints){
        if(this.endpoints == null)
            return true; // no endpoints in context for some reason, include samples.
        String[] sampleEndpointsArray = sampleEndpoints.split(",");
        for(String sampleEndpoint : sampleEndpointsArray){
            for(SBTEnvironment.Endpoint envEndpoint : this.endpoints){
                if(StringUtil.equals(sampleEndpoint, envEndpoint.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isExtension(String ext) {
		if(extensions!=null) {
			for(int i=0; i<extensions.length; i++) {
				if(extensions[i].equals(ext)) {
					return true;
				}
			}
		}
		return false;
	}	
    protected String getExtension(String name, String[] exts) {
        if (exts != null) {
            for (int i=0; i<exts.length; i++) {
                if (name.endsWith(exts[i])) {
                    return exts[i];
                }
            }
        } else {
            int pos = name.lastIndexOf('.');
            if(pos>=0) {
                return name.substring(pos+1);
            }
        }
        return null;
    }
    protected String getNameWithoutExtension(String name, String ext) {
        return name.substring(0, name.length() - (ext.length() + 1));
    }
}
