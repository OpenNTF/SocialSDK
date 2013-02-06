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
package demo;

import java.io.IOException;

import javax.servlet.ServletContext;

import com.ibm.sbt.playground.assets.AssetBrowser;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.assets.RootNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode;
import com.ibm.sbt.playground.assets.javasnippets.JavaSnippetNodeFactory;
import com.ibm.sbt.playground.vfs.ServletVFS;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a Java code snippet factory.
 */
public class SnippetFactoryForJava extends JavaSnippetNodeFactory {

	private static RootNode root = null;
	public static RootNode getSnippets(ServletContext context) {
		if(root==null) {
			try {
				root = readAssets(context);
			} catch(IOException ex) {
				root = new RootNode();
			}
		}
		return root;
	}
	public static VFSFile getRootFile(ServletContext context) {
		ServletVFS vfs = new ServletVFS(context, "/java/");
		return vfs.getRoot();
	}

	private static RootNode readAssets(ServletContext context) throws IOException {
		VFSFile file = getRootFile(context);
		AssetBrowser imp = new AssetBrowser(file,new SnippetFactoryForJava());
		return (RootNode)imp.readAssets(new RootNode(),null);
	}

	@Override
	public JavaSnippetAssetNode createAssetNode(CategoryNode parent, String name) {
		return new DemoJavaSnippetNode(parent, name);
	}
}
