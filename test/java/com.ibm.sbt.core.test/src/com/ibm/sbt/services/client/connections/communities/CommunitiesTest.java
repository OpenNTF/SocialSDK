package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import lib.TestEndpoint;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.util.extractor.field.XMLFieldExtractor;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityList;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.ClientService.Args;

/**
 * Tests for the java connections Communities API a test class provides its own tests extending the test
 * endpoint abstract class
 * 
 * @author Lorenzo Boccaccia
 * @date Nov 22, 2012
 */
public class CommunitiesTest extends TestEndpoint {

	protected static final String	TEST_COMMUNITY_DESCRIPTION	= "Test Community Description";
	protected static final String	NEW_COMMUNITY				= "New Community";

	@Test
	public void testLoad() throws Exception {

		// test should use code in the same way we expect implementors to use it,
		// i.e. no changes at all from the java samples.

		CommunityService svc = new CommunityService();

		CommunityList communities = svc.getPublicCommunities();
		// some sample assertion, a proper test would be much more detailed than this
		for (Community community : communities) {
			assertNotNull(community.getTitle());
		}

		assertEquals(communities.size(), 2);
	}

	@Ignore
	@Test
	public void testCreate() throws Exception {

		CommunityService svc = new CommunityService();
		Community comm = new Community(svc, "");

		// using static constants allow to make assertions about them within the test client
		comm.setTitle(NEW_COMMUNITY);
		comm.setContent(TEST_COMMUNITY_DESCRIPTION);

		String communityId = svc.createCommunity(comm);
		Community comm2 = svc.getCommunity(communityId);
		assertEquals("NEW_COMMUNITY", comm2.getTitle());
		assertEquals("TEST_COMMUNITY_DESCRIPTION", comm2.getContent());
	}

	@Test
	public void testCommunity() {
		// we can also add other tests within the same class; this was added after
		// a problem with createCommunity() to actually perform a specific test over
		// the problematic step narrowing the case
		CommunityService svc = new CommunityService();
		Community comm = new Community(svc, "");
		comm.setTitle("Test");
		assertEquals("Test", comm.getTitle());
	}

	/**
	 * this methd is called for every operation on the registered endpoint a test class is thus paired with
	 * its own test data
	 */
	@Override
	protected Response testRequest(String method, Args args, Object content) throws ClientServicesException {

		// used by testLoad()
		if (method.equals("get") && args.getServiceUrl().equals("communities/service/atom/communities/all")) {
			try {
				return new Response(DOMUtil.createDocument(this.getClass().getResourceAsStream("allCommunities.xml")));
			} catch (XMLException e) {
				throw new ClientServicesException(e);
			}
		}
		// used by testCreate()
		if (method.equals("post") && args.getServiceUrl().equals("communities/service/atom/communities/my")) {

			// TODO: we should thest the API usage against the API XSD
			try {
				Node creationData = (DOMUtil.createDocument((String) content));
				NamespaceContext ctx = new XMLFieldExtractor(new HashMap<String, XPathExpression>())
						.getNamespaceContext();
				assertEquals(DOMUtil.evaluateXPath(creationData, "/entry/content", ctx).getStringValue(),
						CommunitiesTest.TEST_COMMUNITY_DESCRIPTION);
				assertEquals(DOMUtil.evaluateXPath(creationData, "/entry/title", ctx).getStringValue(),
						CommunitiesTest.NEW_COMMUNITY);

			} catch (Throwable e) {
				e.printStackTrace();
				throw new ClientServicesException(e);
			}

			// TODO: use the standard response we would get
			return new Response("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		}

		// this is here just to trap and detect the requests that are to be implemented for the test to work
		throw new ClientServicesException(new IllegalArgumentException(
				"Testing service only, please implement " + method + " at " + args.getServiceUrl() + " for "
						+ args.getParameters() + " for " + content + " with " + args.getHeaders()));
	}

}
