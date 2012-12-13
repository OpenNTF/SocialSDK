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
package com.ibm.sbt.playground.snippets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;

public class Importer extends AbstractImportExport {

	public static final String[] HTMLJS_EXTENSIONS = new String[]{"html","js","css"};
	public static final String[] JSP_EXTENSIONS = new String[]{"jsp"};
	public static final String[] XPAGES_EXTENSIONS = new String[]{"xsp"};

	private VFSFile rootDirectory;
	private NodeFactory factory;
	private String[] extensions;
	
	public Importer(VFSFile rootDirectory, NodeFactory factory, String[] extensions) {
		this.rootDirectory = rootDirectory;
		this.factory = factory;
		this.extensions = extensions;
	}

	
	public RootNode readSnippets() throws IOException {
		return readSnippets(new RootNode());
	}

	public RootNode readSnippets(RootNode root) throws IOException {
		browseDirectory(rootDirectory,root);
		return root;
	}
	
	private void browseDirectory(VFSFile file, CategoryNode node) throws IOException {
		Set<String> snippets = new HashSet<String>();
		VFSFile[] children = file.getChildren();
		for(VFSFile s: children) {
			if(s.isFolder()) {
				CategoryNode cn = factory.createCategoryNode(node, s.getName(), s.getPath());
				node.getChildren().add(cn);
				browseDirectory(s,cn);
			} else if(s.isFile()) {
				String ext = s.getExtension();
				if(ext!=null) {
					String fileName = s.getNameWithoutExtension();
					if(!snippets.contains(fileName)) {
						if(isExtension(ext)) {
							String path = s.getPathWithoutExtension();
							SnippetNode sn = factory.createSnippetNode(node,fileName,path);
							node.getChildren().add(sn);
							snippets.add(fileName);
						}
					}
				}
			}
		}
		// Sort the samples alphabetically
		(new QuickSort.JavaList(node.getChildren()) {
			@Override
			public int compare(Object o1, Object o2) {
				AbstractNode n1 = (AbstractNode)o1;
				AbstractNode n2 = (AbstractNode)o2;
				if(n1.isCategory()&&n2.isSnippet()) return 1;
				if(n2.isCategory()&&n1.isSnippet()) return -1;
				return StringUtil.compareToIgnoreCase(n1.getName(), n2.getName());
			}
			
		}).sort();
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
	
//	public static void main(String[] args) {
//		String path = "c:\\ibmdev\\workspace\\SDK\\java\\sample\\eclipse\\plugins\\com.ibm.sbt.sample.web\\WebContent\\js";
//		Importer imp = new Importer(createVFSFile(new File(path)), new DefaultNodeFactory());
//	}
}
