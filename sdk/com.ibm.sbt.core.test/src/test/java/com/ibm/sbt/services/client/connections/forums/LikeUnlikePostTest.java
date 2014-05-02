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

package com.ibm.sbt.services.client.connections.forums;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class LikeUnlikePostTest extends BaseForumServiceTest {

	@Before
	public void initForumTopic() throws Exception {
		topic = createForumTopic();
	}
	
    @Test
    public void testLikeunlikePostTest() throws Exception {
        forumService.createRecommendation(topic.getTopicUuid());
        ForumTopic recommendedPost = forumService.getForumTopic(topic.getTopicUuid());
        assertEquals(recommendedPost.getRecommendationCount(), "1");
        forumService.deleteRecommendation(topic.getTopicUuid());
        ForumTopic unrecommendedPost = forumService.getForumTopic(topic.getTopicUuid());
        assertEquals(unrecommendedPost.getRecommendationCount(), "0");
    }

    @After
    public void deleteForumTopicDataOnExit() throws Exception {
    	forumService.removeForumTopic(topic.getTopicUuid());
    	forumService.removeForum(topic.getForumUuid());
    }
}

