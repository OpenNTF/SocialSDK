package com.ibm.sbt.services.client.connections.communities;

import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This class implements additional services to manage widgets inside a Connections Community.
 * This code should become part of Community, like management services for Bookmarks, Subcommunities etc.
 * The Community Widget itself is modeled by Widget, WidgetXPath etc.
 * 
 * @author Christian Gosch, inovex GmbH, based on code by Carlos Manias
 *
 */
public class WidgetCommunity extends Community {

	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 *  
	 * @param communityService
	 * @param communityUuid
	 */
	public WidgetCommunity(WidgetCommunityService communityService, String communityUuid) {
		setService(communityService);
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}
	
	public WidgetCommunity(){}
	
	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 * 
	 * @param communityUuid
	 */
	public WidgetCommunity(String communityUuid) {
		setAsString(CommunityXPath.communityUuid, communityUuid);
	}
	
	/**
	 * Constructor (THIS IS AN EXTENDED COMMUNITY!)
	 * @param svc
	 * @param handler
	 */
	public WidgetCommunity(WidgetCommunityService svc, XmlDataHandler handler) {
		super(svc,handler);
	}
	
    /**
     * 
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public WidgetCommunity(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	// Overridden persistence methods 
	
	/**
	 * This method loads the "widget enabled" community 
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	
	public WidgetCommunity load() throws ClientServicesException
    {
		return getService().getCommunity(getCommunityUuid());
    }
	
	/**
	 * This method updates the "widget enabled" community on the server
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public WidgetCommunity save() throws ClientServicesException{
		if(StringUtil.isEmpty(getCommunityUuid())){
			String id = getService().createCommunity(this);
			return getService().getCommunity(id);
		}else{
			getService().updateCommunity(this);
			return getService().getCommunity(getCommunityUuid());
		}
	}
	
	@Override
	public WidgetCommunityService getService(){
		return (WidgetCommunityService)super.getService();
	}
	
	// Additional service methods for managing Widgets inside a Community
	
	/**  Create widget of given type at this community
	 * @param widgetDefId (required)
	 * @return Widget created
	 */
	public Widget createCommunityWidget(WidgetDefId widgetDefId) throws ClientServicesException {
		return getService().createCommunityWidget(this, widgetDefId);
	}
	
	/**
	 * This method gets the widgets of a community
	 * 
	 * @return list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets() throws ClientServicesException {
	   	return getService().getCommunityWidgets(getCommunityUuid());
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param widgetDefId
	 * @return list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(WidgetDefId widgetDefId) throws ClientServicesException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), widgetDefId);
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of widgets of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(Map<String, String> parameters) throws ClientServicesException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), parameters );
	}

	/**
	 * This method gets the widgets of a community
	 * 
	 * @param widgetDefId
	 * @param parameters
     * 				 Various parameters that can be passed to get a feed of widgets of a community. 
     * 				 The parameters must be exactly as they are supported by IBM Connections like ps, sortBy etc.
   	 * @return list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(WidgetDefId widgetDefId, Map<String, String> parameters) throws ClientServicesException {
	   	return getService().getCommunityWidgets(getCommunityUuid(), widgetDefId, parameters );
	}

}
