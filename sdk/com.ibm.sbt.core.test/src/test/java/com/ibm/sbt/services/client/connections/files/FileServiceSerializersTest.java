package com.ibm.sbt.services.client.connections.files;

import static org.junit.Assert.*;
import org.junit.Test;
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
public class FileServiceSerializersTest {

    @Test
    public void testFlagSerializer() {
        String payload = new FlagSerializer("identifier","reason",FileConstants.FlagType.COMMENT).flagPayload();
        
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">\r\n   <in-ref-to xmlns=\"http://www.ibm.com/xmlns/prod/sn\" ref=\"identifier\" ref-item-type=\"comment\" rel=\"http://www.ibm.com/xmlns/prod/sn/report-item\"/>\r\n   <content type=\"text\">reason</content>\r\n</entry>",
                payload);
    
    }

}
