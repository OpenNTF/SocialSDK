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
package com.ibm.sbt.services.client.connections.activities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsFeedXpath;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.common.Person;

/**
 * @author mwallace
 *
 */
public class ActivityTest extends BaseActivityServiceTest {

	@Test
	public void testActivity() throws XMLException, IOException {
		Node node = readXml("activity1.xml");
		XPathExpression xpath = (XPathExpression)AtomXPath.singleEntry.getPath();
		Activity activity = new Activity(activityService, node, nameSpaceCtx, xpath);
		
		System.out.println(activity.toXmlString());
		
		Assert.assertEquals("5f89a416-68e5-4666-af8e-72150bf50e22", activity.getActivityUuid());
		Assert.assertEquals("com.ibm.sbt.services.client.connections.activities.ActivityServiceCrudrTest#1615749198 Community - 1396026691896", activity.getTitle());
		Assert.assertEquals(true, activity.isCommunityActivity());
		Assert.assertEquals("commuid-5f89a416-68e5-4666-af8e-72150bf50e22", activity.getCommunityUuid());
		Assert.assertEquals(1, activity.getPriority());
		Assert.assertEquals("https://apps.na.collabservtest.lotus.com/activities/service/atom2/acl?activityUuid=5f89a416-68e5-4666-af8e-72150bf50e22", activity.getMemberListUrl());
		Assert.assertArrayEquals(new String[] { "https://apps.na.collabservtest.lotus.com/activities/service/atom2/categories?activityUuid=5f89a416-68e5-4666-af8e-72150bf50e22" }, activity.getActivityNodeUrls());
		Assert.assertEquals(true, activity.isCompleted());
		Assert.assertEquals(true, activity.isExternal());
		Assert.assertEquals(true, activity.isTemplate());
	}

	@Test
	public void testActivityEntry() throws XMLException, IOException {
		Node node = readXml("activity1.xml");
		XPathExpression xpath = (XPathExpression)AtomXPath.singleEntry.getPath();
		Activity activity = new Activity(activityService, node, nameSpaceCtx, xpath);
	
		String entryData = activity.createEntryData();
		Assert.assertNotNull(entryData);
	}
		
	@Test
	public void testActivityFeed() throws XMLException, IOException {
		Node doc = readXml("activities.xml");
		
		XmlDataHandler dataHandler = new XmlDataHandler(doc, nameSpaceCtx);
		List<Node> nodeEntries = dataHandler.getEntries(ConnectionsFeedXpath.Entry);
		List<Activity> activities = new ArrayList<Activity>();
		for (Node node : nodeEntries) {
			XPathExpression xpath = (node instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
			activities.add(new Activity(activityService, node, nameSpaceCtx, xpath));
		}

		for (Activity activity : activities) {
			Assert.assertNotNull(activity.getActivityUuid());
			Assert.assertNotNull(activity.getThemeId());
			Assert.assertNotNull(activity.getTitle());
			Assert.assertNotNull(activity.getPublished());
			Assert.assertNotNull(activity.getUpdated());
			Assert.assertNotNull(activity.getPermissions());
			Assert.assertNotNull(activity.getPosition());
			Assert.assertNotNull(activity.getCollectionTitle());
			Assert.assertNotNull(activity.getActivityNodeUrls());
			Assert.assertNotNull(activity.getMemberListUrl());
			Person author = activity.getAuthor();
			Assert.assertNotNull(author.getName());
			Assert.assertNotNull(author.getUserid());
			Assert.assertNotNull(author.getUserState());
			Person contributor = activity.getContributor();
			Assert.assertNotNull(contributor.getName());
			Assert.assertNotNull(contributor.getUserid());
			Assert.assertNotNull(contributor.getUserState());
		}
	}

}
