package com.ibm.sbt.services.client.connections.communities;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.WidgetXPath;
import com.ibm.sbt.services.client.connections.communities.serializers.WidgetSerializer;

/**
 * This File represents Community Widget
 *
 * @author Christian Gosch, inovex GmbH
 *
 */
public class Widget extends AtomEntity {

	public Widget(WidgetCommunityService communityService, String id) {
		setService(communityService);
		setAsString(WidgetXPath.id, id);
	}
	
	public Widget() {}
	
	public Widget(String id) {
		setAsString(WidgetXPath.id, id);
	}
	
	public Widget(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression)
	{
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	public Widget(WidgetCommunityService svc, XmlDataHandler handler)
	{
		super(svc,handler);
	}
	
	/**
	 * getId
	 * 
	 * @return id (ATOM id URL)
	 */	
	public String getId() {
		return getAsString(WidgetXPath.id);
	}
	
	/**
	 * getTitle
	 * 
	 * @return title (Widget title)
	 */	
	public String getTitle() {
		return getAsString(WidgetXPath.title);
	}

	/**
	 * Method sets the Widget title (Widget title)
	 */	
	public void setTitle(String title) {
		setAsString(WidgetXPath.title, title);
	}

	/**
	 * getWidgetAtomUrl
	 * 
	 * @return widgetAtomUrl (URL to get this Widgets ATOM entry.)
	 */	
	public String getWidgetAtomUrl() {
		return getAsString(WidgetXPath.widgetAtomUrl);
	}

	/**
	 * getWidgetsEditUrl
	 * 
	 * @return widgetsEditUrl (URL to use to edit Widget.)
	 */	
	public String getWidgetsEditUrl() {
		return getAsString(WidgetXPath.widgetsEditUrl);
	}

	/**
	 * getWidgetUrl
	 * 
	 * @return widgetUrl (URL that can be used in a Web browser to display the Widget.)
	 */	
	public String getWidgetUrl() {
		return getAsString(WidgetXPath.widgetBrowserUrl);
	}

	/**
	 * getWidgetDefId
	 * 
	 * <br><strong>valid values:</strong> StatusUpdates, Forum, Bookmarks, Files; Blog, IdeationBlog, Activities, Wiki, Calendar, RelatedCommunities, SubcommunityNav
	 * 
	 * @return widgetDefId (Indicates the type of widget. Must match the widgetDefId of an enabled widget; Will be required for POSTs, Ignored for PUTs.)
	 */	
	public String getWidgetDefId() {
		return getAsString(WidgetXPath.widgetDefId);
	}

	/**
	 * setWidgetDefId
	 * 
	 * <br><strong>valid values:</strong> StatusUpdates, Forum, Bookmarks, Files; Blog, IdeationBlog, Activities, Wiki, Calendar, RelatedCommunities, SubcommunityNav
	 * 
	 * @param widgetDefId (Indicates the type of widget. Must match the widgetDefId of an enabled widget; Will be required for POSTs, Ignored for PUTs.)
	 */	
	public void setWidgetDefId(String widgetDefId) {
		setAsString(WidgetXPath.widgetDefId, widgetDefId);
	}

	/**
	 * getWidgetCategory
	 * 
	 * @return widgetCategory (Category from Widget Configuration file.)
	 */	
	public String getWidgetCategory() {
		return getAsString(WidgetXPath.widgetCategory);
	}

	/**
	 * getWidgetInstanceId
	 * 
	 * @return widgetInstanceId (Widget Instance ID, required for PUT, ignored for POST, DELETE.)
	 */	
	public String getWidgetInstanceId() {
		return getAsString(WidgetXPath.widgetInstanceId);
	}

	/**
	 * setWidgetInstanceId
	 * 
	 * @param widgetInstanceId (Widget Instance ID, required for PUT, ignored for POST, DELETE.)
	 */	
	public void setWidgetInstanceId(String widgetInstanceId) {
		setAsString(WidgetXPath.widgetInstanceId, widgetInstanceId);
	}

	/**
	 * getWidgetHidden
	 * 
	 * @return widgetHidden (Hidden/Visible state, optional default false.)
	 */	
	public boolean getWidgetHidden() {
		return getAsBoolean(WidgetXPath.widgetHidden);
	}

	/**
	 * setWidgetHidden
	 * 
	 * @param widgetHidden (Hidden/Visible state, optional default false.)
	 */	
	public void setWidgetHidden(boolean widgetHidden) {
		setAsBoolean(WidgetXPath.widgetHidden, widgetHidden);
	}

	/**
	 * getWidgetLocation
	 * 
	 * <br><strong>valid values</strong>: col2 or col3 (or col2statusposts for Status Updates widget only)
	 * 
	 * @return widgetLocation (Column location, required unless snx:hidden is true in which case it's ignored.)
	 */	
	public String getWidgetLocation() {
		return getAsString(WidgetXPath.widgetLocation);
	}

	/**
	 * setWidgetLocation
	 * 
	 * <br><strong>valid values</strong>: col2 or col3 (or col2statusposts for Status Updates widget only)
	 * 
	 * @param widgetLocation (Column location, required unless snx:hidden is true in which case it's ignored.)
	 */	
	public void setWidgetLocation(String widgetLocation) {
		setAsString(WidgetXPath.widgetLocation, widgetLocation);
	}

	/**
	 * getPreviousWidgetInstanceId
	 * 
	 * <br><strong>valid values</strong>: ID or empty if first item in column
	 * 
	 * @return previousWidgetInstanceId (Position given by previous widget instance id, optional. ignored if snx:hidden is true.)
	 */	
	public String getPreviousWidgetInstanceId() {
		return getAsString(WidgetXPath.previousWidgetInstanceId);
	}

	/**
	 * setPreviousWidgetInstanceId
	 * 
	 * <br><strong>valid values</strong>: ID or empty if first item in column
	 * 
	 * @param previousWidgetInstanceId (Position given by previous widget instance id, optional. ignored if snx:hidden is true.)
	 */	
	public void setPreviousWidgetInstanceId(String previousWidgetInstanceId) {
		setAsString(WidgetXPath.previousWidgetInstanceId, previousWidgetInstanceId);
	}
	
	// XML persistence methods
	
	/**
	 * This method is used by communityService wrapper methods to construct request body for Add/Update operations
	 * @return Object
	 */
	public Object constructCreateRequestBody() throws ClientServicesException {
		return createWidgetRequestPayload();
	}
	
	private String createWidgetRequestPayload() throws ClientServicesException {
		WidgetSerializer serializer = new WidgetSerializer(this);
		String xml = serializer.createPayload();
		return xml;		
	}
	
	
	// TODO: Add methods to handle Widget properties. XML looks like sequence of: <snx:widgetProperty key="...">...</snx:widgetProperty> and forms a map.

}
