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

package com.ibm.sbt.services.client.connections.activities;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * @author mwallace
 *
 */
public enum ActivityXPath implements FieldEntry {
	activity("./snx:activity"),
	position("./snx:position"),
	depth("./snx:depth"),
	permissions("./snx:permissions"),
	icon("./snx:icon"),
	themeId("./snx:themeId"),
	duedate("./snx:duedate"),
	communityUuid("./snx:communityUuid"),
	goal("./a:subtitle"),
	type("./a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/type']/@term"),
	flags("./a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/flags']/@term"),
	priority("./a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/priority']/@term"),
	defaultView("./a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/default-view']/@term"),
	deleted("./a:category[@term='deleted']"),
	reply("./a:category[@term='reply']"),
	external("./a:category[@term='external']"),
	completed("./a:category[@term='completed']"),
	template("./a:category[@term='template']"),
	memberListHref("./a:link[@rel='http://www.ibm.com/xmlns/prod/sn/member-list']/@href"),
	activityNode("./snx:activityNode"),
	assignedTo("./snx:assignedto"),
	collectionTitle("./app:collection/a:title"),
	collectionCategoryHrefs("./app:collection/app:categories/@href"),
	entry_field("/a:entry/snx:field"),
	field("./snx:field"),
	field_fid("./@fid"),
	field_name("./@name"),
	field_type("./@type"),
	field_position("./@position"),
	field_hidden("./@hidden"),
	field_date("."),
	field_person("."),
	field_link("./a:link"),
	field_summary("./a:summary"),
	field_link_editmedia("./a:link[@rel='edit-media']"),
	field_link_enclosure("./a:link[@rel='enclosure']"),
	in_reply_to("./thr:in-reply-to"),
	in_reply_to_href("./thr:in-reply-to/@href"),
	assignedto("./snx:assignedto"),
	attr_label("./@label"),
	attr_term("./@term"),	
	attr_scheme("./@scheme"),	
	attr_ref("./@ref"),	
	attr_source("./@source"),	
	attr_href("./@href"),	
	attr_name("./@name"),	
	attr_userid("./@userid"),	
	;
	
	private final XPathExpression path;
	
	private ActivityXPath(String xpath) {
		XPathExpression xpathExpr = null;
		try {
			xpathExpr = DOMUtil.createXPath(xpath);
		} catch (XMLException e) {
			e.printStackTrace();
		}
		this.path = xpathExpr;
	}
	
	@Override
	public XPathExpression getPath() {
		return path;
	}
	
	@Override
	public String getName() {
		return this.name();
	}
}