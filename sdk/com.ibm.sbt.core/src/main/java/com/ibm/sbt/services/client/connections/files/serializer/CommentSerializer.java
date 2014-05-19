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
import static com.ibm.sbt.services.client.connections.files.FileConstants.CATEGORY_COMMENT;
import static com.ibm.sbt.services.client.connections.files.FileConstants.CREATED;
import static com.ibm.sbt.services.client.connections.files.FileConstants.MODIFIED;
import static com.ibm.sbt.services.client.connections.files.FileConstants.MODIFIER;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.base.serializers.PersonSerializer;
import com.ibm.sbt.services.client.connections.files.Comment;

/**
 * @author Lorenzo Boccaccia 
 * @date 8 May 2014
 */
public class CommentSerializer extends AtomEntitySerializer<Comment> {

    public CommentSerializer(Comment entity) {
        super(entity);
    }

    
    public Node getCommentEntry() {
        Node n =genericAtomEntry();
        if (entity.getCommentId()!=null)
            appendChildren(n, textElement(Namespace.TD.getUrl(), UUID, entity.getCommentId()));

        appendChildren(n, element(CATEGORY, 
                attribute(SCHEME, Namespace.TAG.getUrl()),
                attribute(TERM, CATEGORY_COMMENT),
                attribute(LABEL, CATEGORY_COMMENT)
                )
                );
       
        if (entity.getModified()!=null)     
            appendChildren(n, textElement(Namespace.TD.getUrl(), MODIFIED, DateSerializer.toString(entity.getModified())));
        if (entity.getCreated()!=null)     
            appendChildren(n, textElement(Namespace.TD.getUrl(), CREATED, DateSerializer.toString(entity.getCreated())));
            
        if (entity.getModifier()!=null)   
            appendChildren(n,new PersonSerializer(entity.getModifier()).xmlNode(MODIFIER, Namespace.TD.getUrl() ));

        return n;
    }

    public String generateCommentUpdatePayload() {
       getCommentEntry();
        return serializeToString();
    }
}
