/*
 *  Copyright IBM Corp. 2012
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
package com.ibm.sbt.playground.assets.opensocial;

import java.io.IOException;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.playground.assets.Asset;
import com.ibm.sbt.playground.assets.AssetNode;
import com.ibm.sbt.playground.assets.CategoryNode;
import com.ibm.sbt.playground.vfs.VFSFile;


/**
 * Definition of an OpenSocial gadget snippet.
 */
public class GadgetSnippetAssetNode extends AssetNode {

	public GadgetSnippetAssetNode(CategoryNode parent, String name) {
		super(parent,name);
	}

	public GadgetSnippetAssetNode(CategoryNode parent, String name, String category, String unid, String jspUrl) {
		super(parent,name,category,unid,jspUrl);
	}

	@Override
	public GadgetSnippet load(VFSFile root) throws IOException {
		return (GadgetSnippet)super.load(root);
	}

	@Override
	public Asset createAsset(VFSFile root) throws IOException {
		VFSFile parent = getParentFile(root);

		// Look for a spec.json
		String spec = loadFile(parent,"spec.json");
		if(StringUtil.isNotEmpty(spec)) {
			GadgetSnippet s = (GadgetSnippet)new GadgetSnippet();
			try {
				JsonJavaObject o=(JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, spec);
				String t = o.getString("title");
				s.setTitle(t);
				//boolean isDefault = o.getBoolean("isDefault");
				String ag = getFirstString(o,"gadget");
				if(ag!=null) {
					s.setGadgetXml(loadFile(parent, ag));
				}
				String ah = getFirstString(o,"htmlFiles");
				if(ah!=null) {
					s.setHtml(loadFile(parent, ah));
				}
				String ac = getFirstString(o,"cssFiles");
				if(ac!=null) {
					s.setCss(loadFile(parent, ac));
				}
				String aj = getFirstString(o,"jsFiles");
				if(aj!=null) {
					s.setJs(loadFile(parent,aj));
				}
				String ap = getFirstString(o,"eeDataModel");
				if(ap!=null) {
					s.setJson(loadFile(parent,ap));
				}
				return s;
			} catch (JsonException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	private String getFirstString(JsonJavaObject o, String prop) {
		Object p = o.get(prop);
		if(p instanceof List<?>) {
			List<?> l = (List<?>)p;
			if(l.size()>0) {
				return (String)l.get(0);
			}
		}
		if(p instanceof String) {
			return (String)p;
		}
		return null;
	}
	
//	public Asset createAsset(VFSFile root) throws IOException {
//		VFSFile parent = getParentFile(root);
//		String xml = loadResource(parent,"xml");
//		String docHtml = loadResource(parent,"doc.html");
//		GadgetSnippet s = (GadgetSnippet)new GadgetSnippet();
//		s.setXml(xml);
//		s.setDocHtml(docHtml);
//		return s;
//	}
}
