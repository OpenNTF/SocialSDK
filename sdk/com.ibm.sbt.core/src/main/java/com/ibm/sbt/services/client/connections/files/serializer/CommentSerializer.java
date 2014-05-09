package com.ibm.sbt.services.client.connections.files.serializer;

import org.w3c.dom.Node;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.base.serializers.PersonSerializer;
import com.ibm.sbt.services.client.connections.files.Comment;
import com.ibm.sbt.services.client.connections.files.FileConstants;
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

/**
 * @author Lorenzo Boccaccia 
 * @date 8 May 2014
 */
public class CommentSerializer extends AtomEntitySerializer<Comment> {

    public CommentSerializer(Comment entity) {
        super(entity);
    }

    
    public Node getFileEntry() {
        Node n =genericAtomEntry();
        appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), ConnectionsConstants.UUID, entity.getCommentId()));

        appendChildren(n, element(ConnectionsConstants.Namespace.TD.getUrl(), ConnectionsConstants.CATEGORY, 
                attribute(ConnectionsConstants.SCHEME, ConnectionsConstants.Namespace.TAG.getUrl()),
                attribute(ConnectionsConstants.TERM, FileConstants.CATEGORY_COMMENT),
                attribute(ConnectionsConstants.LABEL, FileConstants.CATEGORY_COMMENT)
                )
                );
        
       
        appendChildren(n, textElement(ConnectionsConstants.CONTENT, entity.getContent()),
                attribute(ConnectionsConstants.TYPE,ConnectionsConstants.TEXT));
        
        
        if (entity.getModified()!=null)     
            appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.MODIFIED, DateSerializer.toString(entity.getModified())));
        if (entity.getCreated()!=null)     
            appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.CREATED, DateSerializer.toString(entity.getCreated())));
            
        if (entity.getModifier()!=null)   
            appendChildren(n,new PersonSerializer(entity.getModifier()).xmlNode(ConnectionsConstants.Namespace.TD.getUrl(),FileConstants.MODIFIER ));

        return n;
    }


    public String generateFileUpdatePayload() {
       getFileEntry();
       
        return serializeToString();
    }
}
