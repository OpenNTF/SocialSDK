package com.ibm.sbt.services.client.connections.communities.model;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.datahandlers.FieldEntry;

/**
 * 
 * @author Christian Gosch, inovex GmbH, based on code by Carlos Manias
 *
 */
public enum WidgetXPath implements FieldEntry {
	// http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Community_widgets_feed_ic45&content=pdcontent
	entry("/a:entry"),
	id("./a:id"), // <id>...</id>: ATOM id (URL) (GET)
	title("./a:title"), // <title type="text">...</title>: [String] user visible title (POST, PUT)
	category("./a:category[@term='widget'][@scheme='http://www.ibm.com/xmlns/prod/sn/type']"), // <category term="widget" scheme="http://www.ibm.com/xmlns/prod/sn/type"> </category>: [empty] Identifies the entry as a widget Atom entry. (Required on POST/PUT)
	widgetAtomUrl("./a:link[@rel='self']/@href"), // <link href="..." rel="self"> </link>: URL to get this Widgets ATOM entry (GET)
	widgetsEditUrl("./a:link[@rel='edit']/@href"), // <link href="..." rel="edit"> </link>: URL to use to edit Widget (no instanceId contained!?) (GET)
	widgetBrowserUrl("./a:link[@rel='alternate']/@href"), // <link href="..." rel="alternate"> </link>: URL that can be used in a Web browser to display the Widget. (GET)
	widgetDefId("./snx:widgetDefId"), // <snx:widgetDefId>...</snx:widgetDefId>: [Name] Indicates the type of widget. Must match the widgetDefId of an enabled (available) widget. (GET, required on POST)
	widgetCategory("./snx:widgetCategory"), // <snx:widgetCategory>...</snx:widgetCategory>: [String] Category from Widget Configuration file (GET)
	widgetInstanceId("./snx:widgetInstanceId"), // <snx:widgetInstanceId>...</snx:widgetInstanceId>: [UUID] Widget Instance ID (GET, DELETE, required for PUT)
	widgetHidden("./snx:hidden"), // <snx:hidden>...</snx:hidden>: [boolean] Hidden/Visible state, POST/PUT: optional, default: false (visible)
	widgetLocation("./snx:location"), // <snx:location>col2|col3</snx:location>: [Enum] Column location; POST/PUT:  required unless snx:hidden is true in which case it's ignored.
	previousWidgetInstanceId("./snx:previousWidgetInstanceId"), // <snx:previousWidgetInstanceId>...</snx:previousWidgetInstanceId>: [UUID] Position given by previous widget instance id; POST/PUT: optional. ignored if snx:hidden is true.
	widgetProperty("./snx:widgetProperty"); // <snx:widgetProperty key="...">...</snx:widgetProperty>: [key/value] Provides the widget instance data.  There may be multiple instances of this element. (GET)
	
	private final XPathExpression path;
	
	private WidgetXPath(String xpath) {
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
