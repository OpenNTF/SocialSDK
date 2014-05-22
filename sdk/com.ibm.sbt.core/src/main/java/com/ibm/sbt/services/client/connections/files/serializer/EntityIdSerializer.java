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

package com.ibm.sbt.services.client.connections.files.serializer;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.ENTRY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.FEED;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.LABEL;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.connections.files.FileConstants.ITEM_ID;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.XmlSerializer;

/**
 * @author Lorenzo Boccaccia
 * @date 8 May 2014
 */
public class EntityIdSerializer extends XmlSerializer {

    String category;
    private Collection<String> ids;

    public EntityIdSerializer(Collection<String> fileIds, String category) {
        this.ids = fileIds;
        this.category = category;
    }


    public EntityIdSerializer(Collection<String> fileIds) {
        this(fileIds,null);
    }

    private void generateFileIdListPayload() {
        Node entry = entry();
        for (Node node : entryList()) {
        appendChildren(entry,node);
        }
    }

    public String fileIdListPayload() {
        generateFileIdListPayload();
        return serializeToString();
    }

    private Node entry() {
        Element element = element(Namespace.ATOM.getUrl(), FEED);
        element.setAttributeNS(Namespace.XMLNS.getUrl(), Namespace.TD.getNSPrefix(), Namespace.TD.getUrl());
        Node root = rootNode(element);
        return root;
    }

    private List<Node> entryList() {
        List<Node> ret = new LinkedList<Node>();
        for (String id : ids) {
            Element entry = element(ENTRY);
            Element element = textElement(Namespace.TD.getUrl(), ITEM_ID, id);
            appendChildren(entry, element);
            if (category!=null) {
                Element cat = element(ConnectionsConstants.CATEGORY, attribute(LABEL, category),
                       attribute(TERM, category), attribute(SCHEME, Namespace.TAG.getUrl()) );
                appendChildren(entry, cat);
            }
            ret.add(entry);
        }
        return ret;
    }
}
