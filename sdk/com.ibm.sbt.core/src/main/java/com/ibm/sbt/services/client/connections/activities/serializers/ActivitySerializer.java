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

package com.ibm.sbt.services.client.connections.activities.serializers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespaces;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.activities.Activity;
import com.ibm.sbt.services.client.connections.activities.DateField;
import com.ibm.sbt.services.client.connections.activities.Field;
import com.ibm.sbt.services.client.connections.activities.FileField;
import com.ibm.sbt.services.client.connections.activities.LinkField;
import com.ibm.sbt.services.client.connections.activities.PersonField;
import com.ibm.sbt.services.client.connections.activities.Priority;
import com.ibm.sbt.services.client.connections.activities.TextField;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * @author mwallace
 *
 */
public class ActivitySerializer extends AtomEntitySerializer<Activity> {
	
	public ActivitySerializer(Activity activity) {
		super(activity);
	}
	
	public String generateCreate() {
		return generateUpdate();
	}
	
	public String generateUpdate() {
		Node entry = genericAtomEntry();
		
		appendChildren(entry, tags());

		appendChildren(entry,
				activityCategory(),
				duedate(),
				communityUuid(),
				communityCategory(),
				priorityCategory(),
				externalCategory(),
				completedCategory(),
				templateCategory(),
				linkContainer(),
				summary(),
				inReplyTo(),
				assignedTo()
		);

		appendChildren(entry, fields());
		
		return serializeToString();
	}
	
	protected List<Element> fields() {
		Field[] fields = entity.getFields();
		if (fields == null) {
			return null;
		}
		ArrayList<Element> elements = new ArrayList<Element>();
		for (Field field : fields) {
			Element element = element("field", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn"),
				attribute("fid", field.getFid()),
				attribute("name", field.getName()),
				attribute("type", field.getType()),
				attribute("position", ""+field.getPosition()));
			
			String type = field.getType();
			if ("date".equals(type)) {
				DateField dateField = (DateField)field;
				addText(element, DateSerializer.toString(dateField.getDate()));
			} else if ("file".equals(type)) {
				FileField fileField = (FileField)field;
			} else if ("link".equals(type)) {
				LinkField linkField = (LinkField)field;
			} else if ("person".equals(type)) {
				PersonField personField = (PersonField)field;
			} else if ("text".equals(type)) {
				TextField textField = (TextField)field;
			}
		}
		
		return elements;
	}
	
	protected Element activityCategory() {
		return element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "activity"), 
				attribute("label", "Activity"));
	}
	
	protected Element communityCategory() {
		return entity.isCommunityActivity() ? element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "community_activity"), 
				attribute("label", "Community Activity")) : null;
	}
	
	protected Element communityUuid() {
		return entity.isCommunityActivity() ? 
				textElement(Namespaces.SNX, "communityUuid", entity.getCommunityUuid()) : null;
	}
	
	protected Element linkContainer() {
		return entity.isCommunityActivity() ? element("link", 
				attribute("rel", "http://www.ibm.com/xmlns/prod/sn/container"), 
				attribute("type", "application/atom+xml"), 
				attribute("href", "")) : null;
	}
	
	protected Element priorityCategory() {
		Priority priority = entity.getPriority();
		return (priority != null) ? element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/priority"),
				attribute("term", "" + priority.getTerm()),
				attribute("label", priority.getLabel())) : null;
	}
	
	protected Element externalCategory() {
		return entity.isExternal() ? element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/flags"),
				attribute("label", "External"),
				attribute("term", "external")) : null;
	}
	
	protected Element completedCategory() {
		return entity.isCompleted() ? element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "completed")) : null;
	}
	
	protected Element templateCategory() {
		return entity.isTemplate() ? element("category", 
				attribute("scheme", "http://www.ibm.com/xmlns/prod/sn/type"), 
				attribute("term", "template")) : null;
	}
		
	protected Element duedate() {
		return textElement("snx:duedate", DateSerializer.toString(entity.getDuedate()));
	}
	
	protected Element inReplyTo() {
		String inReplyTo = entity.getInReplyTo();
		return StringUtil.isNotEmpty(inReplyTo) ? element(Namespaces.THR, "in-reply-to", 
				attribute("ref", inReplyTo), 
				attribute("href", inReplyTo), 
				attribute("type", "application/atom+xml"),
				attribute("source", entity.getActivityUuid())) : null;
	}
		
	protected Element assignedTo() {
		Person assignedTo = entity.getAssignedTo();
		return (assignedTo != null) ? element(Namespaces.SNX, "assignedto", 
				attribute("name", assignedTo.getName()), 
				attribute("userid", assignedTo.getUserid())) : null;
	}
		
	@Override
	protected Element content() {
		return textElement("content", entity.getContent(), 
				attribute("type", "application/atom+xml"));
	}
	
}
