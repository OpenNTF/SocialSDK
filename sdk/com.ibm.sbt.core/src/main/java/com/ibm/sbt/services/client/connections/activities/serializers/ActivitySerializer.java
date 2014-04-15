/*
 * © Copyright IBM Corp. 2013
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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.ACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ASSIGNEDTO;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMMUNITYUUID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMMUNITY_ACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMPLETED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.DATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.EMPTY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.EXTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FIELD;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FILE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HREF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.IN_REPLY_TO;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_COMMUNITYACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_EXTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LINK;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.NAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.PERSON;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.POSITION;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_DUEDATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SOURCE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEMPLATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEXT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.USERID;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
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
			Element element = element(FIELD, 
				attribute(SCHEME, Namespace.SNX.getUrl()),
				attribute(FID, field.getFid()),
				attribute(NAME, field.getName()),
				attribute(TYPE, field.getType()),
				attribute(POSITION, EMPTY+field.getPosition()));

			String type = field.getType();
			if (DATE.equals(type)) {
				DateField dateField = (DateField)field;
				addText(element, DateSerializer.toString(dateField.getDate()));
			} else if (FILE.equals(type)) {
				FileField fileField = (FileField)field;
			} else if (LINK.equals(type)) {
				LinkField linkField = (LinkField)field;
			} else if (PERSON.equals(type)) {
				PersonField personField = (PersonField)field;
			} else if (TEXT.equals(type)) {
				TextField textField = (TextField)field;
			}
		}
		
		return elements;
	}
	
	protected Element activityCategory() {
		return element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, ACTIVITY), 
				attribute(LABEL, ACTIVITY));
	}
	
	protected Element communityCategory() {
		return entity.isCommunityActivity() ? element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, COMMUNITY_ACTIVITY), 
				attribute(LABEL, LABEL_COMMUNITYACTIVITY)) : null;
	}
	
	protected Element communityUuid() {
		return entity.isCommunityActivity() ? 
				textElement(Namespace.SNX.getUrl(), COMMUNITYUUID, entity.getCommunityUuid()) : null;
	}
	
	protected Element linkContainer() {
		return entity.isCommunityActivity() ? element(LINK, 
				attribute(REL, Namespace.CONTAINER.getUrl()), 
				attribute(TYPE, APPLICATION_ATOM_XML), 
				attribute(HREF, EMPTY)) : null;
	}
	
	protected Element priorityCategory() {
		Priority priority = entity.getPriority();
		return (priority != null) ? element(CATEGORY, 
				attribute(SCHEME, Namespace.PRIORITY.getUrl()),
				attribute(TERM, EMPTY + priority.getTerm()),
				attribute(LABEL, priority.getLabel())) : null;
	}
	
	protected Element externalCategory() {
		return entity.isExternal() ? element(CATEGORY, 
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(LABEL, LABEL_EXTERNAL),
				attribute(TERM, EXTERNAL)) : null;
	}
	
	protected Element completedCategory() {
		return entity.isCompleted() ? element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, COMPLETED)) : null;
	}
	
	protected Element templateCategory() {
		return entity.isTemplate() ? element(CATEGORY, 
				attribute(SCHEME, Namespace.TYPE.getUrl()), 
				attribute(TERM, TEMPLATE)) : null;
	}
		
	protected Element duedate() {
		return textElement(SNX_DUEDATE, DateSerializer.toString(entity.getDuedate()));
	}
	
	protected Element inReplyTo() {
		String inReplyTo = entity.getInReplyTo();
		return StringUtil.isNotEmpty(inReplyTo) ? element(Namespace.THR.getUrl(), IN_REPLY_TO, 
				attribute(REF, inReplyTo), 
				attribute(HREF, inReplyTo), 
				attribute(TYPE, APPLICATION_ATOM_XML),
				attribute(SOURCE, entity.getActivityUuid())) : null;
	}
		
	protected Element assignedTo() {
		Person assignedTo = entity.getAssignedTo();
		return (assignedTo != null) ? element(Namespace.SNX.getUrl(), ASSIGNEDTO, 
				attribute(NAME, assignedTo.getName()), 
				attribute(USERID, assignedTo.getUserid())) : null;
	}

	@Override
	protected Element content() {
		return textElement(CONTENT, entity.getContent(), 
				attribute(TYPE, APPLICATION_ATOM_XML));
	}
	
}
