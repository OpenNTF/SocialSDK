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
package com.ibm.sbt.sample.connections.test;

import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.commons.runtime.util.ParameterProcessor;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.BaseUnitTest;


/**
 * Tests the AdminPublishActivityStream class from the sbt.sample.app project.
 * @author Francis
 *
 */
public class AdminPublishActivityStreamTest extends BaseUnitTest {
    @Ignore
    @Test
    public void testPost() throws Exception {
    	/*
        AdminPublishActivityStream apas = new AdminPublishActivityStream("connections", false);
        authenticateEndpoint(apas.getEndpoint(), "admin", "passw0rd");
        
        InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream("com/ibm/sbt/sample/app/data.properties");
        Properties props = new Properties();
        props.load(propertiesStream);
        
        InputStream templateStream = this.getClass().getClassLoader().getResourceAsStream("com/ibm/sbt/sample/app/template.json");
        Scanner s = new Scanner(templateStream).useDelimiter("\\A");
        String template = s.hasNext() ? s.next() : "";
        
        String streamEntry = apas.postToStream(mergeData(template, props));
        
        assertNotNull("Expected to retrieve a non-null response after post to server", streamEntry);
        */
    }

    /*
     * Merges template with data
     */
    private JsonJavaObject mergeData(String template, Properties properties) throws JsonException, IOException{
        String filledTemplate = ParameterProcessor.process(template, properties);
        JsonJavaObject templateObj = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, filledTemplate);
        
        return templateObj;
    }
}
