package com.ibm.sbt.services.client.connections.communities;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.services.client.connections.communities.util.Messages;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * This class implements additional services to manage widgets inside a Connections Community.
 * This code should become part of CommunityService, like management services for Bookmarks, Subcommunities etc.
 * The Community Widget itself is modeled by Widget, WidgetXPath etc.
 * 
 * @author Christian Gosch, inovex GmbH, based on code my Carlos Manias
 */
@SuppressWarnings("serial")
public class WidgetCommunityService extends CommunityService {
	
	private static final String COMMUNITY_UNIQUE_IDENTIFIER = "communityUuid";
	
	public static final String COMMUNITYTYPE_PUBLIC = "public";
	public static final String COMMUNITYTYPE_RESTRICTED = "publicInviteOnly";
	public static final String COMMUNITYTYPE_PRIVATE = "private";
	
	public static final String	WidgetsException					= "Problem occurred while fetching Widgets of Community with id : {0}";
	public static final String	WidgetDefIdException				= "Missing Widget type definition";
	
	public static final String WIDGET_DEF_ID = "widgetDefId";

	/**
	 * Constructor Creates WidgetCommunityService Object with default endpoint and default CacheSize
	 */
	public WidgetCommunityService() {
		super(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates WidgetCommunityService Object with a specified endpoint and default CacheSize
	 * 
	 * @param endpoint
	 */
	public WidgetCommunityService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates WidgetCommunityService Object with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 */
	public WidgetCommunityService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	/**
	 * Constructor - Creates WidgetCommunityService Object with a specified endpoint and default CacheSize
	 * 
	 * @param endpoint
	 */
	public WidgetCommunityService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates WidgetCommunityService Object with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 */
	public WidgetCommunityService(Endpoint endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}

	/**
	 * Wrapper method to get a "Widget enabled" Community
	 * <p>
	 * fetches community content from server and populates the data member of {@link Community} with the fetched content 
	 * @see CommunityService#getCommunity(String)
	 *
	 * @param communityUuid
	 *			   id of community
	 * @return A Community
	 * @throws ClientServicesException
	 */
	@Override
	public WidgetCommunity getCommunity(String communityUuid) throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
        String url = CommunityUrls.COMMUNITY_INSTANCE.format(this, CommunityUrls.getCommunityUuid(communityUuid));
		WidgetCommunity widgetCommunity;
		try {
			widgetCommunity = (WidgetCommunity)getEntity(url, parameters, getWidgetCommunityFeedHandler());
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, Messages.CommunityException, communityUuid);
		} catch (Exception e) {
			throw new ClientServicesException(e, Messages.CommunityException, communityUuid);
		}
		
		return widgetCommunity;
	}
	
	/**
	 * Create sub community for given parent community.
	 * <br><b>ATTN:</>Sub communities are not allowed to have sub communities themselves!
	 * @param parentCommunity
	 * @param title
	 * @param tags
	 * @param content
	 * @param type
	 * @return {WidgetCommunity}
	 */
	public WidgetCommunity createSubCommunity(Community parentCommunity, String title, Collection<String> tags, String content, String type) throws ClientServicesException {
		
		WidgetCommunity subCommunity = null;
		
		// set up new community object
		Community community = prepareCommunityEntity(title, tags, content, type, null, null);
		
		// create sub community
		String uuid = super.createSubCommunity(community, parentCommunity);
		
		// load community object immediately after creation to reflect automatic extensions
		subCommunity = this.getCommunity(uuid);
		
		return subCommunity;
	}
	
	private Community prepareCommunityEntity(String title, Collection<String> tags, String content, String type, String uuid, Community parentCommunity) {
		// set up new community object
		Community community = new Community();
		if (null != uuid) {
			// for update only: "id" is required!
			community.setCommunityUuid(uuid);
			community.setAsString(CommunityXPath.communityUuid, uuid);
			community.setAsString(CommunityXPath.id, uuid);
		}
		if (null != parentCommunity && null != parentCommunity.getCommunityUuid()) {
			community.setParentCommunityUrl(parentCommunity.getSelfUrl());
		}
		community.setTitle(title); // required
		// prepare tags
		HashSet<String> interim = new HashSet<String>();
		for (Iterator<String> iterator = tags.iterator(); iterator.hasNext();) {
			String tag = (String) iterator.next();
			interim.add(tag.toLowerCase());
		}
		tags = interim;
		community.setTags(new ArrayList<String>(tags));
		community.setContent(content);
		// set type: 
			// - public (view:all, edit:all, membership:self/invite) 
			// - private (view:members, edit:members, membership:invite) 
			// - publicInviteOnly (view:all, edit:?, membership:invite)
		if (COMMUNITYTYPE_PUBLIC.equals(type) || COMMUNITYTYPE_PRIVATE.equals(type) || COMMUNITYTYPE_RESTRICTED.equals(type)) {
			community.setCommunityType(type);
		} else {
			community.setCommunityType(COMMUNITYTYPE_PUBLIC);
		}
		
		return community;
	}
	
	/**  Create widget of given type at given community
	 * @param community (required)
	 * @param widgetDefId (required)
	 * @return Widget created
	 */
	public Widget createCommunityWidget(Community community, WidgetDefId widgetDefId) throws ClientServicesException {
		if (null == community) {
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		if (null == widgetDefId) {
			throw new ClientServicesException(null, WidgetDefIdException);
		}
		
		Widget communityWidget = null;
		
		Widget widget = new Widget();
		widget.setWidgetDefId(widgetDefId.toString());
		widget.setWidgetLocation("col2"); // required unless hidden is not true
		widget.setWidgetHidden(Boolean.FALSE); // optional, default: false
		
		Object widgetPayload = widget.constructCreateRequestBody();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(COMMUNITY_UNIQUE_IDENTIFIER, community.getCommunityUuid()); // CommunityService.COMMUNITY_UNIQUE_IDENTIFIER

		Map<String, String> requestheaders = new HashMap<String, String>();
		requestheaders.put("Content-Type", "application/atom+xml");

		StringBuilder comBaseUrl = new StringBuilder(WidgetCommunityUrls.COMMUNITY_WIDGETS_FEED.format(this)); // "{communities}/service/atom/{authType}/community/widgets"

		// Add required parameters
		if (null != params && params.size() > 0) {
			comBaseUrl.append('?');
			boolean setSeparator = false;
			for (Map.Entry<String, String> param : params.entrySet()) {
				String key = param.getKey();
				if (StringUtil.isEmpty(key)) continue;
				String value = EntityUtil.encodeURLParam(param.getValue());
				if (StringUtil.isEmpty(value)) continue;
				if (setSeparator) {
					comBaseUrl.append('&');
				} else {
					setSeparator = true;
				}
				comBaseUrl.append(key).append('=').append(value);
			}
		}

		String widgetPostUrl = comBaseUrl.toString();
		
		// execute request
		Response result = createData(widgetPostUrl, null, requestheaders, widgetPayload);
		
		// read response data
		IFeedHandler<Widget> feedHandler = getWidgetFeedHandler();
		widget = feedHandler.createEntity(result);
		
		EntityList<Widget> widgetList = getCommunityWidgets(community.getCommunityUuid(), widgetDefId);
		if (null != widgetList && widgetList.size() > 0) {
			communityWidget = widgetList.get(0);
			// TODO Check if an EntityList<Widget> entry is complete (or if fields are missing). EntityList<Community> does NOT contain all entry data!
		}
		
		return communityWidget;
	}

	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @return A list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(String communityUuid) throws ClientServicesException {
		return getCommunityWidgets(communityUuid,	null, null);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param widgetDefId
	 * @return A list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(String communityUuid, WidgetDefId widgetDefId) throws ClientServicesException {
		return getCommunityWidgets(communityUuid,	widgetDefId, null);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param parameters
	 * @return A list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(String communityUuid, Map<String, String> parameters) throws ClientServicesException {
		return getCommunityWidgets(communityUuid,	null, parameters);
	}
	
	/**
	 * Wrapper method to get Widgets of a community
	 * 
	 * @param communityUuid 
	 * 				 community Id of which Widgets are to be fetched
	 * @param widgetDefId
	 * @param parameters
	 * @return A list of widgets
	 * @throws ClientServicesException
	 */
	public EntityList<Widget> getCommunityWidgets(String communityUuid, WidgetDefId widgetDefId, Map<String, String> parameters) throws ClientServicesException {
		
		if (StringUtil.isEmpty(communityUuid)){
			throw new ClientServicesException(null, Messages.NullCommunityIdException);
		}
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		parameters.put(COMMUNITY_UNIQUE_IDENTIFIER, communityUuid);
		if (null != widgetDefId) {
			parameters.put(WIDGET_DEF_ID, widgetDefId.toString());
		}
	
        String requestUrl = WidgetCommunityUrls.COMMUNITY_WIDGETS_FEED.format(this);
		
        EntityList<Widget> widgets = null;
		try {
			widgets = (EntityList<Widget>) getEntities(requestUrl, parameters, getWidgetFeedHandler());
		} catch (ClientServicesException e) {
			throw new ClientServicesException(e, WidgetsException, communityUuid);
		}
		
		return widgets;
	}

	public EntityList<Invite> getInvitesList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return super.getInviteEntityList(requestUrl, parameters);
	}
	
	/***************************************************************
	 * FeedHandlers for each entity type
	 ****************************************************************/

	/**
	 * Factory method to instantiate a FeedHandler for WidgetCommunities
	 * @return IFeedHandler&lt;WidgetCommunity&gt;
	 */
	protected IFeedHandler<WidgetCommunity> getWidgetCommunityFeedHandler() {
		return new AtomFeedHandler<WidgetCommunity>(this, false) {
			@Override
			protected WidgetCommunity entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new WidgetCommunity(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * Factory method to instantiate a FeedHandler for Widgets
	 * @return IFeedHandler&lt;Widget&gt;
	 */
	protected IFeedHandler<Widget> getWidgetFeedHandler() {
		return new AtomFeedHandler<Widget>(this, false) {
			@Override
			protected Widget entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Widget(service, node, nameSpaceCtx, xpath);
			}
		};
	}

}
