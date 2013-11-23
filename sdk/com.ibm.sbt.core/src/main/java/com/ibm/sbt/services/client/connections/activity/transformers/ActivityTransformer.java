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

package com.ibm.sbt.services.client.connections.activity.transformers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.util.XmlTextUtil;
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.BookmarkField;
import com.ibm.sbt.services.client.connections.activity.DateField;
import com.ibm.sbt.services.client.connections.activity.Field;
import com.ibm.sbt.services.client.connections.activity.FileField;
import com.ibm.sbt.services.client.connections.activity.PersonField;
import com.ibm.sbt.services.client.connections.activity.TextField;

/**
 * Activity Transformer provides helper methods for construction of Activity XML payload. 
 * <p>
 * @author Vimal Dhupar
 */

public class ActivityTransformer extends AbstractBaseTransformer {

	private String	SOURCEPATH = "/com/ibm/sbt/services/client/connections/activity/templates/";
	private ActivityService service;
	/*
	 * Tranformer needs instance of forum/topic
	 * so it can determine values which were not modified by user
	 * hence missing in fieldsmap but required in request payload
	 */
	
	public ActivityTransformer() {
	}
	
	public ActivityTransformer(ActivityService service) {
		this.service = service;
	}
	
	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		
		String xml = getTemplateContent(SOURCEPATH + "ActivityNodeTmpl.xml");
		
		String categoryXml = "";
		String tagsXml = "";
		String uuidXml = "";
		String linksXml = "";
		String flagsXml = "";
		String dueDateXml = "";
		String titleXml = "";
		String contentXml = "";
		String positionXml = "";
		String assignedToXml = "";
		String inReplyToXml = "";
		String fieldsXml = "";
				
	for(Map.Entry<String, Object> xmlEntry : fieldmap.entrySet()){
			
			String currentElement = xmlEntry.getKey(); 
			String currentValue = "";
			Object currentValueObject = null;
			
			if(currentElement.contains("field"))
				currentValueObject = xmlEntry.getValue();
			else if(xmlEntry.getValue() != null){
				currentValue = xmlEntry.getValue().toString();
			}
			if(currentElement.contains("Category")){
				categoryXml += getXMLRep(getStream(SOURCEPATH + "CategoryTmpl.xml"), "Category", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("tag")){
				tagsXml += getXMLRep(getStream(SOURCEPATH + "TagTmpl.xml"), "tag", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("Content") && !StringUtil.equals(currentElement, "ContentType")){
				contentXml += getXMLRep(getStream(SOURCEPATH + "ContentTmpl.xml"), "Content", XmlTextUtil.escapeXMLChars(currentValue));
				String type = "";
				if(fieldmap.containsKey("ContentType")) {
					type = fieldmap.get("ContentType").toString(); 
				}
				if(StringUtil.isEmpty(type)) {
					type = "text";
				}
				contentXml = getXMLRep(contentXml, "ContentType", XmlTextUtil.escapeXMLChars(type));
			}
			if(currentElement.contains("Title")){
				titleXml += getXMLRep(getStream(SOURCEPATH + "TitleTmpl.xml"), "Title", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("CommunityUuid")){
				uuidXml = getXMLRep(getStream(SOURCEPATH+ "UuidTmpl.xml"), "CommunityUuid", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("CommunityLink")){
				linksXml = getXMLRep(getStream(SOURCEPATH+ "LinkTmpl.xml"), "CommunityLink", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("flag")){
				flagsXml = getXMLRep(getStream(SOURCEPATH+ "FlagsTmpl.xml"), "Flags", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("DueDate")){
				dueDateXml = getXMLRep(getStream(SOURCEPATH+ "DueDateTmpl.xml"), "DueDate", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("position") || StringUtil.equalsIgnoreCase(currentElement, "position")){
				positionXml += getXMLRep(getStream(SOURCEPATH + "PositionTmpl.xml"), "position", XmlTextUtil.escapeXMLChars(currentValue));
			}
			if(currentElement.contains("assignedToId") || currentElement.contains("assignedToName")){
				assignedToXml = getTemplateContent(SOURCEPATH + "AssignedTmpl.xml");
				assignedToXml = getXMLRep(assignedToXml, "assignedToId", XmlTextUtil.escapeXMLChars(fieldmap.get("assignedToId").toString()));
				assignedToXml = getXMLRep(assignedToXml, "assignedToName", XmlTextUtil.escapeXMLChars(fieldmap.get("assignedToId").toString()));
			}
			if(currentElement.contains("inReplyToId") || currentElement.contains("inReplyToUrl")){
				inReplyToXml = getTemplateContent(SOURCEPATH + "InReplyToTmpl.xml");
				inReplyToXml = getXMLRep(inReplyToXml, "InReplyToId", XmlTextUtil.escapeXMLChars(fieldmap.get("inReplyToId").toString()));
				inReplyToXml = getXMLRep(inReplyToXml, "InReplyToUrl", XmlTextUtil.escapeXMLChars(fieldmap.get("inReplyToUrl").toString()));
				inReplyToXml = getXMLRep(inReplyToXml, "activityId", XmlTextUtil.escapeXMLChars(fieldmap.get("inReplyToActivityId").toString()));
			}
			if(currentElement.contains("field")){
				// in case of file, add same values for rel = edit and rel = enclosure. so fetch template twice.
				fieldsXml += getTemplateContent(SOURCEPATH + "FieldTmpl.xml");
				fieldsXml = getXMLRep(fieldsXml, "fieldname", XmlTextUtil.escapeXMLChars(((Field)currentValueObject).getName()));
				fieldsXml = getXMLRep(fieldsXml, "fieldtype", XmlTextUtil.escapeXMLChars(((Field)currentValueObject).getType()));
				if(StringUtil.isNotEmpty(((Field)currentValueObject).getFid())) {
					fieldsXml = getXMLRep(fieldsXml, "fid", XmlTextUtil.escapeXMLChars(((Field)currentValueObject).getFid()));
				}
				if(StringUtil.isNotEmpty(((Field)currentValueObject).getPosition())) {
					fieldsXml = getXMLRep(fieldsXml, "position", XmlTextUtil.escapeXMLChars(((Field)currentValueObject).getPosition()));
				}
				
				if (currentValueObject instanceof DateField) {
					String dateAsISO = convertDate(((DateField)currentValueObject).getDate());
					fieldsXml = getXMLRep(fieldsXml, "getDate", XmlTextUtil.escapeXMLChars(dateAsISO));
				} else if (currentValueObject instanceof FileField) {
					String fileTmpl = getXMLRep(getStream(SOURCEPATH + "FieldFileTmpl.xml"), "fileUrl", XmlTextUtil.escapeXMLChars(((FileField)currentValueObject).getFieldFileUrl()));
					fileTmpl += getXMLRep(fileTmpl, "fileType", XmlTextUtil.escapeXMLChars(((FileField)currentValueObject).getFieldFileType()));
					fileTmpl += getXMLRep(fileTmpl, "fileSize", XmlTextUtil.escapeXMLChars("\"" + ((FileField)currentValueObject).getFieldFileSize()) + "\"");
					fieldsXml = getXMLRep(fieldsXml, "getFile", fileTmpl);
				} else if (currentValueObject instanceof TextField) {
					String summaryXml = getXMLRep(getStream(SOURCEPATH + "SummaryTmpl.xml"), "summary", XmlTextUtil.escapeXMLChars(((TextField)currentValueObject).getTextSummary()));
					fieldsXml = getXMLRep(fieldsXml, "getText", summaryXml);
				} else if (currentValueObject instanceof BookmarkField) {
					String bookmarkTmpl = getXMLRep(getStream(SOURCEPATH + "FieldBookmarkTmpl.xml"), "linkUrl", XmlTextUtil.escapeXMLChars(((BookmarkField)currentValueObject).getFieldLinkUrl()));
					bookmarkTmpl = getXMLRep(bookmarkTmpl, "linkTitle", XmlTextUtil.escapeXMLChars(((BookmarkField)currentValueObject).getFieldLinkTitle()));
					fieldsXml = getXMLRep(fieldsXml, "getBookmarkLink", bookmarkTmpl);
				} else if (currentValueObject instanceof PersonField) {
					String nameXml = getXMLRep(getStream(SOURCEPATH + "NameTmpl.xml"), "personName", XmlTextUtil.escapeXMLChars(((PersonField)currentValueObject).getPersonName()));
					String emailXml = getXMLRep(getStream(SOURCEPATH + "EmailTmpl.xml"), "email", XmlTextUtil.escapeXMLChars(((PersonField)currentValueObject).getPersonEmail()));
					String useridXml = getXMLRep(getStream(SOURCEPATH + "UseridTmpl.xml"), "userid", XmlTextUtil.escapeXMLChars(((PersonField)currentValueObject).getPersonUserid()));
					fieldsXml = getXMLRep(fieldsXml, "getName", nameXml);
					fieldsXml = getXMLRep(fieldsXml, "getEmail", emailXml);
					fieldsXml = getXMLRep(fieldsXml, "getUserId", useridXml);
				}
				fieldsXml = removeExtraPlaceholders(fieldsXml);
			}
		}
		if(StringUtil.isNotEmpty(titleXml)){
			xml = getXMLRep(xml, "getTitle",titleXml);
		}
		if(StringUtil.isNotEmpty(uuidXml)){
			xml = getXMLRep(xml, "getUuid",uuidXml);
		}
		if(StringUtil.isNotEmpty(tagsXml)){
			xml = getXMLRep(xml, "getTags",tagsXml);
		}
		if(StringUtil.isNotEmpty(linksXml)){
			xml = getXMLRep(xml, "getLink",linksXml);
		}
		if(StringUtil.isNotEmpty(dueDateXml)){
			xml = getXMLRep(xml, "getDueDate",dueDateXml);
		}
		if(StringUtil.isNotEmpty(flagsXml)){
			xml = getXMLRep(xml, "getFlags",flagsXml);
		}
		if(StringUtil.isNotEmpty(contentXml)){
			xml = getXMLRep(xml, "getContent",contentXml);
		}
		if(StringUtil.isNotEmpty(positionXml)){
			xml = getXMLRep(xml, "getPosition",positionXml);
		}
		if(StringUtil.isNotEmpty(assignedToXml)){
			xml = getXMLRep(xml, "getAssignedTo",assignedToXml);
		}
		if(StringUtil.isNotEmpty(fieldsXml)){
			xml = getXMLRep(xml, "getFields",fieldsXml);
		}
		if(StringUtil.isNotEmpty(inReplyToXml)){
			xml = getXMLRep(xml, "getInReplyTo",inReplyToXml);
		}
		xml = getXMLRep(xml, "getCategory", categoryXml);
		xml = removeExtraPlaceholders(xml);
		return xml;
	}

	private String convertDate(Date date) {
		String dateReturned = "";
		if(date != null) {
			try {
				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				df.setTimeZone(tz);
				dateReturned = df.format(date);
			} catch (Exception e ) {
				dateReturned = date.toString();
			}
		}
		return dateReturned;
	}
}