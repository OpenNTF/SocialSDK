package com.ibm.sbt.services.client.connections.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;

/**
 * This file contains the converter functions, these functions are used by Profile Service wrapper methods
 * and return the data in required type
 * 
 * @author Swati Singh
 */

public class Converter {
	
	static final String sourceClass = Converter.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);
  
	/**
	 * This method returns a array of profiles. 
	 * 
	 * @param Data
	 * @param ProfileService
	 * @return Profile[] 
	 */
    static public Profile[] convertToProfiles(ProfileService ps, Document data)
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnProfiles");
		}
		
		Profile[] profile = null;
		
		if(data != null){
			NodeList profileEntries = data.getElementsByTagName("entry");
			profile = new Profile[profileEntries.getLength()];
			if(profileEntries != null && profileEntries.getLength() > 0) {
				for(int i = 0 ; i < profileEntries.getLength();i++) {
					Node entry = profileEntries.item(i);
					Document doc;
					try {
						doc = DOMUtil.createDocument();
						Node dup = doc.importNode(entry, true);
						Element root = doc.createElement("feed");
						root.appendChild(dup);
						doc.appendChild(root);
						profile[i] = new Profile(ps, DOMUtil.value(entry, "contributor/snx:userid"));
						profile[i].setData(doc);
					} catch (XMLException e) {
						if (logger.isLoggable(Level.SEVERE)) {
							logger.log(Level.SEVERE, "Error encountered while converting data into contacts profiles", e);
						}
					}
				}
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnProfiles");
		}
		return profile;
	}
	
    static public Collection<ConnectionEntry> returnConnectionEntries(Document data, String type)
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnColleagueConnections");
		}
		Collection<ConnectionEntry> connections = null;
		
		if(data != null){
			NodeList connectionEntries = data.getElementsByTagName("entry");
			connections = new ArrayList<ConnectionEntry>();
			if(connectionEntries != null && connectionEntries.getLength() > 0) {
				for(int i = 0 ; i < connectionEntries.getLength();i++) {
					Node entry = connectionEntries.item(i);
					Document doc;
					try {
						doc = DOMUtil.createDocument();
						Node dup = doc.importNode(entry, true);
						Element root = doc.createElement("feed");
						root.appendChild(dup);
						doc.appendChild(root);
						ConnectionEntry connectionEntry = ConnectionEntry.createConnectionEntryWithData(doc, type);
						connections.add(connectionEntry);
					} catch (XMLException e) {
						if (logger.isLoggable(Level.SEVERE)) {
							logger.log(Level.SEVERE, Messages.ProfileError_2 + "returnColleagueConnections()", e);
						}
					}
				
				}
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnColleagueConnections", connections);
		}
		return connections;
	}
	
    static public Collection<Profile> returnProfileEntries(ProfileService ps, Document data)
	{
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "returnProfileEntries");
		}
		
		Collection<Profile> profiles = null;
		
		if(data != null){
			NodeList profileEntries = data.getElementsByTagName("entry");
			profiles = new ArrayList<Profile>();
			if(profileEntries != null && profileEntries.getLength() > 0) {
				for(int i = 0 ; i < profileEntries.getLength();i++) {
					Node entry = profileEntries.item(i);
					Document doc;
					try {
						doc = DOMUtil.createDocument();
						Node dup = doc.importNode(entry, true);
						Element root = doc.createElement("feed");
						root.appendChild(dup);
						doc.appendChild(root);
						Profile profile = new Profile(ps, DOMUtil.value(entry, "contributor/snx:userid"));
						profile.setData(doc);
						profiles.add(profile);
					} catch (XMLException e) {
						if (logger.isLoggable(Level.SEVERE)) {
							logger.log(Level.SEVERE, Messages.ProfileError_2 + "returnProfileEntries()", e);
						}
					}
				}
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
    		logger.exiting(sourceClass, "returnProfileEntries");
		}
		return profiles;
	}
	
	

}
