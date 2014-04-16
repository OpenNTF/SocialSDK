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

package com.ibm.sbt.services.client.connections.common.serializers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.common.Member;

/**
 * @author mwallace
 *
 */
public class MemberSerializer extends AtomEntitySerializer<Member> {
	
	public MemberSerializer(Member member) {
		super(member);
	}
	
	public String generateCreate() {
		Node entry = entry();
		
		appendChildren(entry,
				contributor(),
				memberCategory(),
				role()
		);
		
		return serializeToString();
	}
	
	public String generateUpdate() {
		return generateCreate();
	}
	
	private Element memberCategory() {
		return element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "person"));
	}
	
	protected Element role() {
		return textElement(Namespaces.SNX, "role", entity.getRole());
	}	
		
}
