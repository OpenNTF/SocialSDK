package com.ibm.sbt.services.client.connections.forums;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;

import org.junit.Test;

import com.ibm.sbt.services.client.SerializationUtil;

/**
 * @author Swati Singh
 *
 */
public class ForumSerializationTests extends BaseForumServiceTest {
	
	
	@Test
	public final void testForumSerialization() throws Exception {
		Forum forumGot = forumService.getForum(createForum().getForumUuid());
		final String forumUuid = forumGot.getForumUuid();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				Forum forumObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					forumObject = (Forum) ois.readObject();
					assertEquals(forumObject.getForumUuid(), forumUuid);
				} catch (Exception e) {}
				
			}
		}.isSerializable(forumGot);
	}
	
	@Test
	public final void testForumListSerialization() throws Exception {
		ForumList forums = forumService.getAllForums();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				ForumList allforums = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allforums = (ForumList) ois.readObject();
					for (Iterator iterator = allforums.iterator(); iterator.hasNext();) {
						Forum localForum = (Forum) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allforums.size()>0));
			}
		}.isSerializable(forums);
	}
	
	@Test
	public final void testForumTopicSerialization() throws Exception {
		ForumTopic topicGot = forumService.getForumTopic(createForumTopic().getTopicUuid());
		final String topicUuid = topicGot.getTopicUuid();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				ForumTopic topicObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					topicObject = (ForumTopic) ois.readObject();
					assertEquals(topicObject.getTopicUuid(), topicUuid);
				} catch (Exception e) {}
				
			}
		}.isSerializable(topicGot);
	}
	
		
	@Test
	public final void testTopicListSerialization() throws Exception {
		TopicList topics = forumService.getPublicForumTopics();
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				TopicList allTopics = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allTopics = (TopicList) ois.readObject();
					for (Iterator iterator = allTopics.iterator(); iterator.hasNext();) {
						ForumTopic localTopic = (ForumTopic) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allTopics.size()>0));
			}
		}.isSerializable(topics);
	}
	
	@Test
	public final void testForumReplySerialization() throws Exception {
		ForumReply reply = forumService.getForumReply(createForumReply().getReplyUuid());
		final String replyUuid = reply.getReplyUuid();
		new SerializationUtil() {
			@Override
			public void validateSerializable() {
				ForumReply replyObject = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					replyObject = (ForumReply) ois.readObject();
					assertEquals(replyObject.getReplyUuid(), replyUuid);
				} catch (Exception e) {}
				
			}
		}.isSerializable(reply);
	}
	
		
	@Test
	public final void testReplyListSerialization() throws Exception {
		ForumReply reply = createForumReply();
		ReplyList replies = forumService.getForumTopicReplies(reply.getTopicUuid());
		new SerializationUtil() {
			
			@Override
			public void validateSerializable() { 
				ReplyList allreplies = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
					allreplies = (ReplyList) ois.readObject();
					for (Iterator iterator = allreplies.iterator(); iterator.hasNext();) {
						ForumReply localreply = (ForumReply) iterator.next();
					}
				} catch (Exception e) {}
				assertEquals(true, (allreplies.size()>0));
			}
		}.isSerializable(replies);
	}
	
}