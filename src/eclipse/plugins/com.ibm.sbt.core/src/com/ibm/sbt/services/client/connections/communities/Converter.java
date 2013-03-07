package com.ibm.sbt.services.client.connections.communities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * This file contains the converter functions, these functions are used by Community Service wrapper methods
 * and return the data in required type
 * 
 * @author Swati Singh
 */

public class Converter {
	static final String sourceClass = Converter.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);
    
    /**
	 * This method returns a array of members of a community. This is called by getMembers method of CommunityService 
	 * 
	 * @param cs
	 * @param Data
	 * @return Member[] 
	 * @throws XMLException
	 */
	static public Member[] returnMembers(CommunityService cs, Document data) throws XMLException
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnMembers");
		}
		NodeList memberEntries = data.getElementsByTagName("entry");
		Member[] members = new Member[memberEntries.getLength()];
		if(memberEntries != null && memberEntries.getLength() > 0) {
			for(int i = 0 ; i < memberEntries.getLength();i++) {
				Node entry = memberEntries.item(i);
				Document doc =  DOMUtil.createDocument();
				Node dup = doc.importNode(entry, true);
				doc.appendChild(dup);
				members[i] = new Member(cs, entry.getFirstChild().getTextContent());
				members[i].setData(doc);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnMembers", members);
		}
		return members;
	}
	
	 /**
	 * This method returns a array of communities. This is called by GetMyCommunities/GetPublicCommunities/GetSubCommunities methods of CommunityService 
	 * 
	 * @param cs
	 * @param Data
	 * @return Community[] 
	 * @throws XMLException
	 */
	static public Collection<Community> returnCommunities(CommunityService cs, Document data) throws XMLException
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnCommunities");
		}
		Collection<Community> communities = null;
		
		if(data != null){
			NodeList communityEntries = data.getElementsByTagName("entry");
			communities = new ArrayList<Community>();
			if(communityEntries != null && communityEntries.getLength() > 0) {
				for(int i = 0 ; i < communityEntries.getLength();i++) {
					Node entry = communityEntries.item(i);
					Document doc =  DOMUtil.createDocument();
					Node dup = doc.importNode(entry, true);
					doc.appendChild(dup);
					Community community = new Community(cs, entry.getFirstChild().getTextContent());//snx:communityUuid
					community.setData(doc);
					communities.add(community);
				}
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnCommunities");
		}
		return communities;
	}
	
	 /**
	 * This method returns a array of bookmarks. This is called by GetBookmarks methods of CommunityService 
	 * 
	 * @param cs
	 * @param Data
	 * @return Bookmark[] 
	 * @throws XMLException
	 */
	static public Bookmark[] returnBookmarks(CommunityService cs, Document data) throws XMLException
    {	
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnBookmarks");
		}
		NodeList bookmarkEntries = data.getElementsByTagName("entry");
		Bookmark[] bookmarks = new Bookmark[bookmarkEntries.getLength()];
		if(bookmarkEntries != null && bookmarkEntries.getLength() > 0) {
			for(int i = 0 ; i < bookmarkEntries.getLength();i++) {
				Node entry = bookmarkEntries.item(i);
				Document doc =  DOMUtil.createDocument();
				Node dup = doc.importNode(entry, true);
				doc.appendChild(dup);
				bookmarks[i] = new Bookmark(cs, entry.getFirstChild().getTextContent());
				bookmarks[i].setData(doc);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnBookmarks", bookmarks);
		}	
    	return bookmarks;
    }

	 /**
	 * This method returns a array of forum Topics. This is called by GetForumTopics methods of CommunityService 
	 * 
	 * @param cs
	 * @param Data
	 * @return ForumTopic[] 
	 * @throws XMLException
	 */
	static public ForumTopic[] returnForumTopics(CommunityService cs, Document data) throws XMLException
	{	
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnForumTopics");
		}
		NodeList forumTopicEntries = data.getElementsByTagName("entry");
		ForumTopic[] forumTopics = new ForumTopic[forumTopicEntries.getLength()];
		if(forumTopicEntries != null && forumTopicEntries.getLength() > 0) {
			for(int i = 0 ; i < forumTopicEntries.getLength();i++) {
				Node entry = forumTopicEntries.item(i);
				Document doc =  DOMUtil.createDocument();
				Node dup = doc.importNode(entry, true);
				doc.appendChild(dup);	  
				forumTopics[i] = new ForumTopic(cs, entry.getFirstChild().getTextContent());
				forumTopics[i].setData(doc);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnForumTopics", forumTopics);
		}
		return forumTopics;
	}

}
