package nsf;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceException;

import sbt.ConnectionsService;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

public class XMLService {
	
	
	
	public static String getCommunityXMLString(){
		ConnectionsService svc = new sbt.ConnectionsService("connections");
		HandlerXml handlerXML= new HandlerXml();
		Document msg=null;
		String xmlText = null;
		try {
			msg = (Document)svc.get("/communities/service/atom/communities/all",handlerXML).getData();
			xmlText =DOMUtil.getXMLString(msg);  
			return xmlText;
				
		} catch (ClientServicesException e) {
			e.printStackTrace();
		} catch (XMLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getActivityXMLString(){
		ConnectionsService svc = new sbt.ConnectionsService("connections");
		HandlerXml handlerXML= new HandlerXml();
		Document msg=null;
		String xmlText = null;
		try {
			msg = (Document)svc.get("/activities/service/atom2/everything",handlerXML).getData();
			xmlText =DOMUtil.getXMLString(msg);  
			return xmlText;
				
		} catch (ClientServicesException e) {
			e.printStackTrace();
		} catch (XMLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getProfileXMLString(){
		ProfileService svc = new ProfileService("connections");
		Profile prof=null;
		String xmlText = null;
		try {
			prof = svc.getMyProfile();
			xmlText =DOMUtil.getXMLString((Node)prof.getDataHandler().getData());
		} catch (XMLException e) {
			e.printStackTrace();
		}
		catch (ProfileServiceException e) {
			e.printStackTrace();
		}
		return xmlText;
	}
}
