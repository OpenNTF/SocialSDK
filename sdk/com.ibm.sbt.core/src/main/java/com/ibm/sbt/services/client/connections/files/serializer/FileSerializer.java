package com.ibm.sbt.services.client.connections.files.serializer;

import javax.xml.bind.DatatypeConverter;
import org.apache.http.client.utils.DateUtils;
import org.w3c.dom.Node;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.base.serializers.BaseEntitySerializer.DateSerializer;
import com.ibm.sbt.services.client.connections.common.serializers.PersonSerializer;
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
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileConstants;

/**
 * @author Lorenzo Boccaccia 
 * @date 8 May 2014
 */
public class FileSerializer extends AtomEntitySerializer<File> {

    public FileSerializer(File entity) {
        super(entity);
    }

    
    public Node getFileEntry() {
        Node n =genericAtomEntry();
        appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), ConnectionsConstants.UUID, entity.getFileId()));
        if (entity.getLabel() != null)
            appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), ConnectionsConstants.LABEL, entity.getLabel()));
        appendChildren(n, element(ConnectionsConstants.Namespace.TD.getUrl(), ConnectionsConstants.CATEGORY, 
                attribute(ConnectionsConstants.SCHEME, ConnectionsConstants.Namespace.TAG.getUrl()),
                attribute(ConnectionsConstants.TERM, entity.getCategory()),
                attribute(ConnectionsConstants.LABEL, entity.getCategory())
                )
                );
        if (entity.getTags()!=null)
        for (String tag : entity.getTags()) {
            appendChildren(n, textElement(ConnectionsConstants.CATEGORY, tag,                
                    attribute(ConnectionsConstants.TERM, FileConstants.CATEGORY_TAG)
                    ));
            
        }
        if (StringUtil.isNotEmpty( entity.getVisibility()))      
        appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.VISIBILITY, entity.getVisibility()));
        
        if (StringUtil.isNotEmpty( entity.getNotification()))      
            appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.NOTIFICATION, entity.getNotification()));
            
        
        if (entity.getModified()!=null)     
            appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.MODIFIED, DateSerializer.toString(entity.getModified())));
            
        if (entity.getModifier()!=null) ;     
            //TODO fix after personserializer
            //appendChildren(n, textElement(ConnectionsConstants.Namespace.TD.getUrl(), FileConstants.MODIFIED, DateSerializer.toString(entity.getModified())));
      
        return n;
    }


    public String generateFileUpdatePayload() {
       getFileEntry();
       
        return serializeToString();
    }
}
