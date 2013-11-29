/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.wikis.serializers;

import java.util.Collection;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.wikis.Wiki;

/**
 * @author Mario Duarte
 *
 */
public class WikiSerializer extends AtomEntitySerializer<Wiki> {
	
	public WikiSerializer(Wiki wiki) throws Exception {
		super(wiki);
	}
	
	public void generateCreate() {
		Node entry = entry();
		
		appendChilds(entry,
				title(),
				label(),
				wikiCategory(),
				sharedWith(),
				permissions(),
				summary()
		);
	}
	
	public void generateUpdate() {
		Node entry = genericAtomEntry();
		
		appendChilds(entry,
				label(),
				wikiCategory(),
				communityId(),
				sharedWith(),
				permissions()
		);
	}
	
	private Element wikiCategory() {
		return categoryType("wiki");
	}
	
	private Element label() {
		return textElement(Namespaces.TD, "label", entity.getLabel());
	}
	
	private Element communityId() {
		return textElement(Namespaces.SNX, "communityUuid", entity.getCommunityUuid());
	}
	
	private Element sharedWith() {
		return null; //FIXME
	}
	
	private Element permissions() {
		return textElement(Namespaces.TD, "permissions", serializePermissions());
	}
	
	@Override
	protected Element content() {
		return textElement("content", entity.getContent(), 
				attribute("type", "application/atom+xml"));
	}
	
	private String serializePermissions() {
		Set<String> permissions = entity.getPermissions();
		if(permissions == null) return null;
		else {
			return StringUtil.concatStrings(toArray(permissions), ',',  true);
		}
	}
	
	private String[] toArray(Collection<String> col) {
		if(col == null) return null;
		else return col.toArray(new String[0]);
	}
}
