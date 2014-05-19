/*
 * � Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class CreateHiddenFieldSearchData extends BaseActivityServiceTest {

	@Test
	public void testCreateActivitiesWithHiddenFields() throws ClientServicesException, XMLException {
		for (int i=0; i<2; i++) {
			Field[] fields = new Field[5];
			for (int j=0; j<fields.length; j++) {
				fields[j] = createHiddenField("Field"+j, "Content"+j);
			}
			
			Activity created = createActivity(createTitle(""+i), Activity.PRIORITY_MEDIUM, fields);
			
			Activity read = activityService.getActivity(created.getActivityUuid());
			System.out.println("Read: "+read.toXmlString());
			Assert.assertEquals(5, read.getFields().length);
		}
		activity = null;
	}

    protected String createTitle(String name) {
    	return "ActivityHiddenField_" + name + "-" + System.currentTimeMillis();
    }	
    
    protected TextField createHiddenField(String name, String summary) {
		TextField textField = new TextField();
		textField.setName(name);
		textField.setPosition(1000);
		textField.setSummary(summary);
		textField.setHidden(true);

		return textField;
    }
	
}
