package com.ibm.sbt.services.client.connections.forums.serializers;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.CATEGORY;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.CONTENT;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HTML;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.REF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.SCHEME;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TERM;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.TYPE;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.ANSWER;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.ANSWERED;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.FORUM;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.FORUM_REPLY;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.FORUM_TOPIC;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.LOCKED;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.PINNED;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.QUESTION;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.THR_IN_REPLY_TO;
import static com.ibm.sbt.services.client.connections.forums.utils.ForumConstants.XMLNS_THR;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.ConnectionsConstants.Namespace;
import com.ibm.sbt.services.client.base.serializers.AtomEntitySerializer;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;

public class ForumSerializer extends AtomEntitySerializer<BaseForumEntity> {

	public ForumSerializer(BaseForumEntity entity) {
		super(entity);
	}

	public String generateCreate() {
		return generateUpdate();
	}

	public String generateUpdate() {
		Node entry = entry();

		appendChildren(entry, title(), content(), typeCategory());
		appendChildren(entry, tags());

		if (isForumTopic()) {
			appendChildren(entry, pinned(), locked(), question(), answered());
		}

		if (isForumReply()) {
			appendChildren(entry, answer(), inReplyTo());
		}

		return serializeToString();
	}

	protected Element answer() {
		if (!isForumReply()) {
			return null;
		}
		ForumReply reply = (ForumReply) entity;
		return reply.isAnswer() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, ANSWER)) : null;
	}
	protected Element answered() {
		if (!isForumTopic()) {
			return null;
		}
		ForumTopic topic = (ForumTopic) entity;
		return topic.isAnswered() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, ANSWERED)) : null;
	}

	@Override
	protected Element content() {
		Element element = element(CONTENT, attribute(TYPE, HTML));
		addText(element, entity.getContent());
		return element;
	}

	protected Element inReplyTo() {
		if (!isForumReply()) {
			return null;
		}
		ForumReply reply = (ForumReply) entity;
		return element(
				THR_IN_REPLY_TO,
				attribute(XMLNS_THR, Namespace.THR.getUrl()),
				attribute(REF, "urn:lsid:ibm.com:forum:" + reply.getTopicUuid()));
	}

	protected Element locked() {
		if (!isForumTopic()) {
			return null;
		}
		ForumTopic topic = (ForumTopic) entity;
		return topic.isLocked() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, LOCKED)) : null;
	}

	protected Element pinned() {
		if (!isForumTopic()) {
			return null;
		}
		ForumTopic topic = (ForumTopic) entity;
		return topic.isPinned() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, PINNED)) : null;
	}

	protected Element question() {
		if (!isForumTopic()) {
			return null;
		}
		ForumTopic topic = (ForumTopic) entity;
		return topic.isQuestion() ? element(CATEGORY,
				attribute(SCHEME, Namespace.FLAGS.getUrl()),
				attribute(TERM, QUESTION)) : null;
	}

	protected Element typeCategory() {
		Element element = element(CATEGORY,
				attribute(SCHEME, Namespace.TYPE.getUrl()),
				attribute(TERM, getTypeCategoryTerm()));

		return element;
	}

	private String getTypeCategoryTerm() {
		if (isForum()) {
			return FORUM;
		} else if (isForumTopic()) {
			return FORUM_TOPIC;
		} else if (isForumReply()) {
			return FORUM_REPLY;
		}

		return null;
	}

	private boolean isForum() {
		if (entity instanceof Forum) {
			return true;
		}

		return false;
	}

	private boolean isForumReply() {
		if (entity instanceof ForumReply) {
			return true;
		}

		return false;

	}

	private boolean isForumTopic() {
		if (entity instanceof ForumTopic) {
			return true;
		}

		return false;
	}
}
