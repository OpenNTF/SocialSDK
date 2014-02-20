package com.ibm.sbt.services.client.connections.forums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class LikeUnlikePostTest extends BaseForumServiceTest {

	@Before
	public void initForumTopic() {
		topic = createForumTopic();
	}
	
    @Test
    public void testLikeunlikePostTest() {
        try {
            forumService.createRecommendation(topic.getTopicUuid());
            ForumTopic recommendedPost = forumService.getForumTopic(topic.getTopicUuid());
            assertEquals(recommendedPost.getRecommendationCount(), "1");
            forumService.deleteRecommendation(topic.getTopicUuid());
            ForumTopic unrecommendedPost = forumService.getForumTopic(topic.getTopicUuid());
            assertEquals(unrecommendedPost.getRecommendationCount(), "0");
        } catch (Exception e) {
            fail("Error calling forumService.recommendPost() caused by: "+e.getMessage());
        }
    }

    @After
    public void deleteForumTopicDataOnExit() {
        try {
        	forumService.removeForumTopic(topic.getTopicUuid());
        	forumService.removeForum(topic.getForumUuid());
        } catch (ForumServiceException e) {
                fail("Error calling forumService.removeForumTopic() caused by: "+e.getMessage());
        }
    }
}

