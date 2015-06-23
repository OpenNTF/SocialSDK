/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.files.serializer;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.UUID;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY_TAG;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CREATED;
import static com.ibm.sbt.services.client.connections.files.FileConstants.MODIFIED;
import static com.ibm.sbt.services.client.connections.files.FileConstants.MODIFIER;
import static com.ibm.sbt.services.client.connections.files.FileConstants.NOTIFICATION;
import static com.ibm.sbt.services.client.connections.files.FileConstants.VISIBILITY;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.base.serializers.PersonSerializer;
import com.ibm.sbt.services.client.connections.files.File;

/**
 * @author Lorenzo Boccaccia
 * @date 8 May 2014
 */
public class FileSerializer extends AtomEntitySerializer<File> {

	public FileSerializer(File entity) {
		super(entity);
	}

	public Node getFileEntry() {
		Node n = genericAtomEntry();
		if (entity.getFileId() != null)
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), UUID, entity.getFileId()));
		if (entity.getLabel() != null)
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), LABEL, entity.getLabel()));
		appendChildren(
				n,
				element(Namespace.TD.getUrl(), CATEGORY,
						attribute(SCHEME, Namespace.TAG.getUrl()),
						attribute(TERM, entity.getCategory()),
						attribute(LABEL, entity.getCategory())));
		if (entity.getTags() != null)
			for (String tag : entity.getTags()) {
				appendChildren(
						n,
						textElement(CATEGORY, tag,
								attribute(TERM, CATEGORY_TAG)));

			}
		if (StringUtil.isNotEmpty(entity.getVisibility()))
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), VISIBILITY,
							entity.getVisibility()));

		if (StringUtil.isNotEmpty(entity.getNotification()))
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), NOTIFICATION,
							entity.getNotification()));

		if (entity.getModified() != null)
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), MODIFIED,
							DateSerializer.toString(entity.getModified())));
		if (entity.getCreated() != null)
			appendChildren(
					n,
					textElement(Namespace.TD.getUrl(), CREATED,
							DateSerializer.toString(entity.getCreated())));

		if (entity.getModifier() != null)
			appendChildren(n,
					new PersonSerializer(entity.getModifier()).xmlNode(
							MODIFIER, Namespace.TD.getUrl()));

		return n;
	}

	public String generateFileUpdatePayload() {
		getFileEntry();
		return serializeToString();
	}
}
