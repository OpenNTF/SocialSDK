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

import com.ibm.sbt.playground.snippets.AbstractImportExport;
import com.ibm.sbt.playground.snippets.AbstractImportExport.NodeFactory;
import com.ibm.sbt.playground.snippets.CategoryNode;
import com.ibm.sbt.playground.snippets.Importer;
import com.ibm.sbt.playground.snippets.SnippetNode;
import com.ibm.sbt.playground.vfs.FileVFS;
import com.ibm.sbt.playground.vfs.ServletVFS;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a code snippet.
 */
public class SnippetFactory {

	//private static RootNode root = null;
	public static DemoRootNode getSnippets(ServletContext context) {
		DemoRootNode root = null;
		if(root==null) {
			try {
				root = readSnippets(context);
			} catch(IOException ex) {
				root = new DemoRootNode();
			}
		}
		return root;
	}
	public static VFSFile getRootFile(ServletContext context) {
		ServletVFS vfs = new ServletVFS(context, "/js/");
		return vfs.getRoot();
	}
	
	private static DemoRootNode readSnippets(ServletContext context) throws IOException {
		VFSFile file = getRootFile(context);
		NodeFactory factory = new AbstractImportExport.DefaultNodeFactory() {
			@Override
			public SnippetNode createSnippetNode(CategoryNode parent, String name) {
				return new DemoSnippetNode(parent, name);
			}
			@Override
			public CategoryNode createCategoryNode(CategoryNode parent, String name) {
				return new DemoSnippetCategory(parent, name);
			}
			
		};
		Importer imp = new Importer(file,factory,Importer.HTMLJS_EXTENSIONS);
		return (DemoRootNode)imp.readSnippets(new DemoRootNode());
	}
}
