package com.ibm.sbt.services.client.connections.files;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.files.serializer.EntityIdSerializer;
import com.ibm.sbt.services.client.connections.files.serializer.FileSerializer;
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
import com.ibm.sbt.services.client.connections.files.serializer.FlagSerializer;

/**
 * @author Lorenzo Boccaccia 
 * @date 7 May 2014
 */
public class FileServiceSerializersTest extends BaseFileServiceTest {

    @Test
    public void testFlagSerializer() {
        String payload = new FlagSerializer("identifier","reason",FileConstants.ItemType.COMMENT).flagPayload();
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">\r\n   <in-ref-to xmlns=\"http://www.ibm.com/xmlns/prod/sn\" ref=\"identifier\" ref-item-type=\"comment\" rel=\"http://www.ibm.com/xmlns/prod/sn/report-item\"/>\r\n   <content type=\"text\">reason</content>\r\n</entry>",
                payload);
    
    }
    @Test
    public void testFileIdFeedSerializer() {
        List<String> l = new LinkedList<String>();
        l.add("file1");
        l.add("file2");
        String payload = new EntityIdSerializer(l).fileIdListPayload();
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:td=\"urn:ibm.com/td\">\r\n   <entry>\r\n      <itemId xmlns=\"urn:ibm.com/td\">file1</itemId>\r\n   </entry>\r\n   <entry>\r\n      <itemId xmlns=\"urn:ibm.com/td\">file2</itemId>\r\n   </entry>\r\n</feed>",
                payload);
    
    }
    
    @Test
    public void testFileIdFeedSerializerWithCategory() {
        List<String> l = new LinkedList<String>();
        l.add("file1");
        l.add("file2");
        String payload = new EntityIdSerializer(l, "community").fileIdListPayload();
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:td=\"urn:ibm.com/td\">\r\n   <entry>\r\n      <itemId xmlns=\"urn:ibm.com/td\">file1</itemId>\r\n      <category label=\"community\" scheme=\"tag:ibm.com,2006:td/type\" term=\"community\"/>\r\n   </entry>\r\n   <entry>\r\n      <itemId xmlns=\"urn:ibm.com/td\">file2</itemId>\r\n      <category label=\"community\" scheme=\"tag:ibm.com,2006:td/type\" term=\"community\"/>\r\n   </entry>\r\n</feed>",
                payload);
    
    }
    
    @Test
    public void testFileSerializer() throws ClientServicesException {
        File f = new FileService().getPinnedFiles().get(0).load();
        String p = new FileSerializer(f).generateFileUpdatePayload();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\">\r\n   <title type=\"text\">LabelUpdateFileNew1395998842907</title>\r\n   <id>urn:lsid:ibm.com:td:8f6b6cca-b62f-43ba-b4cd-25d2c692072f</id>\r\n   <summary type=\"text\">SummaryNew1395998842907</summary>\r\n   <uuid xmlns=\"urn:ibm.com/td\">8f6b6cca-b62f-43ba-b4cd-25d2c692072f</uuid>\r\n   <label xmlns=\"urn:ibm.com/td\">LabelUpdateFileNew1395998842907</label>\r\n   <category xmlns=\"urn:ibm.com/td\" label=\"document\" scheme=\"tag:ibm.com,2006:td/type\" term=\"document\"/>\r\n   <category term=\"tag_word\">document</category>\r\n   <visibility xmlns=\"urn:ibm.com/td\">public</visibility>\r\n   <modified xmlns=\"urn:ibm.com/td\">2014-03-28T06:03:46.000Z</modified>\r\n</entry>",p);
    }
    
    @Test
    public void testFolderSerializer() throws ClientServicesException {
        File f = new FileService().getPublicFolders(null).get(0).load();
        String p = new FileSerializer(f).generateFileUpdatePayload();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\">\r\n   <title type=\"text\">Bill folder</title>\r\n   <id>urn:lsid:ibm.com:td:4560338f-3cb0-456a-9fac-68c2da5c8e8e</id>\r\n   <uuid xmlns=\"urn:ibm.com/td\">4560338f-3cb0-456a-9fac-68c2da5c8e8e</uuid>\r\n   <label xmlns=\"urn:ibm.com/td\">Bill folder</label>\r\n   <category xmlns=\"urn:ibm.com/td\" label=\"collection\" scheme=\"tag:ibm.com,2006:td/type\" term=\"collection\"/>\r\n   <category term=\"tag_word\">collection</category>\r\n   <visibility xmlns=\"urn:ibm.com/td\">public</visibility>\r\n   <modified xmlns=\"urn:ibm.com/td\">2014-05-08T16:22:08.000Z</modified>\r\n</entry>",p);
    }
}
