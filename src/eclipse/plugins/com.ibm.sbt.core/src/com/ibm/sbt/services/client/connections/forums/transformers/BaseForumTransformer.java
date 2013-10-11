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

package com.ibm.sbt.services.client.connections.forums.transformers;

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.ForumsXPath;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.services.client.connections.forums.model.FlagType;


/**
 * ForumTransformer provides helper methods for construction of Forums XML payload.
 * <p>
 * @author Swati Singh
 * @author Manish Kataria
 */

public class BaseForumTransformer extends AbstractBaseTransformer {
	private BaseForumEntity entity;
	private String	sourcepath = "/com/ibm/sbt/services/client/connections/forums/templates/";
	//to know if the request sent is create or update call , so that payload for Forum Reply entry can be correctly formed
	private String serviceOp;
	/*
	 * Tranformer needs instance of forum/topic
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */

	public BaseForumTransformer(BaseForumEntity entity, String method) {
		this.entity = entity;
		this.serviceOp = method;
	}

	public BaseForumTransformer(BaseForumEntity entity) {
		this(entity,"");
	}

	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {

		String xml = getTemplateContent(sourcepath+"BaseEntryTmpl.xml");
		String categoryXml = getTemplateContent(sourcepath+"CategoryTmpl.xml");

		String tagsXml = "";
		String contentXml = "";
		String titleXml = "";
		String flagXml = "";
		String answered = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"answered\"></category>";
		String answer = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"answer\"></category>";
		String pinned = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"pinned\"></category>";
		String locked = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"locked\"></category>";
		String question = "<category scheme=\"http://www.ibm.com/xmlns/prod/sn/flags\" term=\"question\"></category>";

		boolean removeFlag = false;	
		if( entity instanceof ForumTopic){
			if(((ForumTopic)entity).isPinned() == true){
				flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),ForumsXPath.flag.getName(),FlagType.PIN.getFlagType());
			}
			if(((ForumTopic)entity).isLocked() == true){
				flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),ForumsXPath.flag.getName(),FlagType.LOCK.getFlagType());
			}
			if(((ForumTopic)entity).isQuestion() == true){
				flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),ForumsXPath.flag.getName(),FlagType.QUESTION.getFlagType());
			}
			if(((ForumTopic)entity).isAnswered() == true){
				flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),ForumsXPath.flag.getName(),FlagType.ANSWERED.getFlagType());
			}
		}
		if( entity instanceof ForumReply){
			if(((ForumReply)entity).isAnswer() == true){
				flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),ForumsXPath.flag.getName(),FlagType.ANSWER.getFlagType());
			}
		}

		for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){

			String currentElement = xmlEntry.getKey();
			String currentValue = "";
			if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains("tag")){
				tagsXml += getXMLRep(getStream(sourcepath+"CategoryTagTmpl.xml"),"tag",XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("content")){
				contentXml = getXMLRep(getStream(sourcepath+"ContentTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase("title")){
				titleXml = getXMLRep(getStream(sourcepath+"TitleTemplate.xml"),currentElement,XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.equalsIgnoreCase(FlagType.PIN.getFlagType())){
				if( StringUtil.equalsIgnoreCase(currentValue,"true")){
					flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),"flag",currentElement);
				}
				else{
					flagXml = StringUtil.replace(flagXml, pinned, "");
				}
			}
			if(currentElement.equalsIgnoreCase(FlagType.LOCK.getFlagType())){
				if( StringUtil.equalsIgnoreCase(currentValue,"true")){
					flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),"flag",currentElement);
				}
				else{
					flagXml = StringUtil.replace(flagXml, locked, "");
				}
			}
			if(currentElement.equalsIgnoreCase(FlagType.QUESTION.getFlagType())){
				if( StringUtil.equalsIgnoreCase(currentValue,"true")){
					flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),"flag",currentElement);
				}
				else{
					flagXml = StringUtil.replace(flagXml, question, "");
				}
			}
			if(currentElement.equalsIgnoreCase(FlagType.ANSWERED.getFlagType())){
				if( StringUtil.equalsIgnoreCase(currentValue,"true")){
					flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),"flag",currentElement);
				}
				else{
					flagXml = StringUtil.replace(flagXml, answered, "");
				}
			}
			if(currentElement.equalsIgnoreCase(FlagType.ANSWER.getFlagType())){
				if( StringUtil.equalsIgnoreCase(currentValue,"true")){
					flagXml += getXMLRep(getStream(sourcepath+"FlagTmpl.xml"),"flag",currentElement);
				}
				else{
					flagXml = StringUtil.replace(flagXml, answer, "");
				}
			}
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		if(StringUtil.isNotEmpty(tagsXml)){
			xml = getXMLRep(xml, "getTags",tagsXml);
		}
		if(StringUtil.isNotEmpty(flagXml)){
			if(removeFlag == true){
				xml = getXMLRep(xml, "getFlags","");
			}else{
				xml = getXMLRep(xml, "getFlags",flagXml);
			}
		}

		if(entity instanceof Forum){
			categoryXml = getXMLRep(categoryXml, "forum-type", "forum-forum");
		}else if(entity instanceof ForumTopic){
			categoryXml = getXMLRep(categoryXml, "forum-type", "forum-topic");
		}else if(entity instanceof ForumReply){
			categoryXml = getXMLRep(categoryXml, "forum-type", "forum-reply");
			String referenceXml = getTemplateContent(sourcepath+"ReferenceTmpl.xml");
			referenceXml = getXMLRep(referenceXml, "topicId", ((ForumReply)entity).getTopicUuid());
			if(StringUtil.isNotEmpty(serviceOp)){
				if(StringUtil.equalsIgnoreCase(serviceOp, ForumService.CREATE_OP)){// checking if the request is a create call
					referenceXml = getXMLRep(referenceXml, "topicId", ((ForumReply)entity).getTopicUuid());
					xml = getXMLRep(xml, "getReference", referenceXml);
				}
			}
		}
		xml = getXMLRep(xml, "getCategory", categoryXml);
		xml = removeExtraPlaceholders(xml);
		return xml;
	}
}