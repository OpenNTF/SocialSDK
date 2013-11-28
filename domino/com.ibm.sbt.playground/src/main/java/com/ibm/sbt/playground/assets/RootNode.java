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
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.playground.dojo.JsonTreeRenderer;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of a root node.
 */
public class RootNode extends CategoryNode {

	public RootNode() {
		super(null,"");
	}

	public Asset loadAsset(VFSFile root, String unid) throws IOException {
		AssetNode n = findAsset(this, unid);
		if(n!=null) {
			return n.load(root);
		}
		return null;
	}
	
	private AssetNode findAsset(Node parent, String unid) {
		if(parent instanceof AssetNode) {
			AssetNode sn = (AssetNode)parent;
			if(StringUtil.equals(sn.getUnid(), unid)) {
				return sn;
			}
		} else if(parent instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)parent;
			List<Node> children = cn.getChildren();
			for(int i=0; i<children.size(); i++) {
				AssetNode s = findAsset(children.get(i), unid);
				if(s!=null) {
					return s;
				}
			}
		}
		return null;
	}

	public List<Node> getAllChildrenFlat() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		List<Node> children = getChildren();
		for(int i=0; i<children.size(); i++) {
			addNode(nodes,children.get(i));
		}
		return nodes;
	}
	private void addNode(List<Node> nodes, Node node) {
		nodes.add(node);
		if(node instanceof CategoryNode) {
			CategoryNode cn = (CategoryNode)node;
			List<Node> children = cn.getChildren();
			for(int i=0; i<children.size(); i++) {
				addNode(nodes,children.get(i));
			}
		}
	}
	
	public String getAsJson() {
		try {
			JsonTreeRenderer r = new JsonTreeRenderer();
			return r.generateAsStringHier(this, true);
		} catch (IOException e) {
			return "{}";
		}
	}
}
