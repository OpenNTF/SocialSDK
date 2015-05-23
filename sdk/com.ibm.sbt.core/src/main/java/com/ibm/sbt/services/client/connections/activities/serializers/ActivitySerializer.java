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

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ASSIGNEDTO;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMMUNITYUUID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMMUNITY_ACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.COMPLETED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.EXTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HREF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.INTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.IN_REPLY_TO;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_COMMUNITYACTIVITY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_EXTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL_INTERNAL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LINK;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.NAME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SNX_DUEDATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SOURCE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TEMPLATE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.USERID;

import java.text.SimpleDateFormat;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.activities.Activity;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * @author mwallace
 * 
 */
public class ActivitySerializer extends AtomEntitySerializer<Activity> {

	public static SimpleDateFormat actvitiyDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	NodeSerializer nodeSerializer;

	public ActivitySerializer(Activity activity) {
		super(activity);
		nodeSerializer = new NodeSerializer(activity);
	}

	public String generateCreate() {
		return generateUpdate();
	}

	public String generateUpdate() {
		// Node entry = genericAtomEntry();
		Element element = element(Namespace.ATOM.getUrl(), ENTRY);
		element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.APP.getNSPrefix(), Namespace.APP.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(),Namespace.THR.getNSPrefix(), Namespace.THR.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(),Namespace.SNX.getNSPrefix(), Namespace.SNX.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(),Namespace.XHTML.getNSPrefix(), Namespace.XHTML.getUrl());
		element.setAttributeNS(Namespace.XMLNS.getUrl(),Namespace.OPENSEARCH.getNSPrefix(),Namespace.OPENSEARCH.getUrl());

		Node entry = rootNode(element);

		appendChildren(entry, activityCategory(), duedate(), communityUuid(),
				communityCategory(), priorityCategory(), completedCategory(),
				templateCategory(), flagsCategory(), externalCategory(),
				linkContainer(), subtitle(), inReplyTo(), assignedTo());

		// appendChildren(entry, tags());
		appendChildren(entry, nodeSerializer.fields());

		return nodeSerializer.payload(serializeToString());
	}

	protected Element activityCategory() {
		String type = entity.getType();
		return element(CATEGORY, attribute(SCHEME, Namespace.TYPE.getUrl()),
				attribute(TERM, StringUtil.isEmpty(type) ? ACTIVITY : type));
	}

	protected Element communityCategory() {
		return entity.isCommunityActivity() ? element(CATEGORY,
				attribute(SCHEME, Namespace.TYPE.getUrl()),
				attribute(TERM, COMMUNITY_ACTIVITY),
				attribute(LABEL, LABEL_COMMUNITYACTIVITY)) : null;
	}

	protected Element flagsCategory() {
		return entity.getFlags() != null ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, entity.getFlags()),
				attribute(LABEL, entity.getFlags())) : null;
	}

	protected Element communityUuid() {
		return entity.isCommunityActivity() ? textElement(
				Namespace.SNX.getUrl(), COMMUNITYUUID,
				entity.getCommunityUuid()) : null;
	}

	protected Element linkContainer() {
		return entity.isCommunityActivity() ? element(LINK,
				attribute(REL, Namespace.CONTAINER.getUrl()),
				attribute(TYPE, APPLICATION_ATOM_XML), attribute(HREF, EMPTY))
				: null;
	}

	protected Element priorityCategory() {
		long priority = entity.getPriority();
		return element(CATEGORY,
				attribute(SCHEME, Namespace.PRIORITY.getUrl()),
				attribute(TERM, "" + priority));
	}

	protected Element externalCategory() {
		return element(
				CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(LABEL, entity.isExternal() ? LABEL_EXTERNAL
						: LABEL_INTERNAL),
				attribute(TERM, entity.isExternal() ? EXTERNAL : INTERNAL));
	}

	protected Element completedCategory() {
		return entity.isCompleted() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, COMPLETED)) : null;
	}

	protected Element templateCategory() {
		return entity.isTemplate() ? element(CATEGORY,
				attribute(SCHEME, Namespace.TYPE.getUrl()),
				attribute(TERM, TEMPLATE)) : null;
	}

	protected Element duedate() {
		return textElement(SNX_DUEDATE, DateSerializer.toString(
				actvitiyDateFormat, entity.getDuedate()));
	}

	protected Element inReplyTo() {
		String inReplyTo = entity.getInReplyTo();
		return StringUtil.isNotEmpty(inReplyTo) ? element(
				Namespace.THR.getUrl(), IN_REPLY_TO, attribute(REF, inReplyTo),
				attribute(HREF, inReplyTo),
				attribute(TYPE, APPLICATION_ATOM_XML),
				attribute(SOURCE, entity.getActivityUuid())) : null;
	}

	protected Element assignedTo() {
		Person assignedTo = entity.getAssignedTo();
		return (assignedTo != null) ? element(Namespace.SNX.getUrl(),
				ASSIGNEDTO, attribute(NAME, assignedTo.getName()),
				attribute(USERID, assignedTo.getUserid())) : null;
	}

}
