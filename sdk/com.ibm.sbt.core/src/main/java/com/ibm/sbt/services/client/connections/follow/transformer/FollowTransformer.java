package com.ibm.sbt.services.client.connections.follow.transformer;

import java.util.Map;

import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.follow.FollowXPath;

public class FollowTransformer extends AbstractBaseTransformer {
	
	public final String feedNameSpace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:o=\"http://ns.opensocial.org/2008/opensocial\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:td=\"urn:ibm.com/td\" xmlns:relevance=\"http://a9.com/-/opensearch/extensions/relevance/1.0/\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:spelling=\"http://a9.com/-/opensearch/extensions/spelling/1.0/\" xmlns:ca=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0 namespace\">";
	public String source = "<category term=\"${source}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/source\"></category>";
	public String resourceType = "<category term=\"${resourceType}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/resource-type\"></category>";
	public String resourceId = "<category term=\"${resourceId}\" scheme=\"http://www.ibm.com/xmlns/prod/sn/resource-id\"></category>";
	public final String closeEntry = "</entry>";
	
	@Override
	public String transform(Map<String, Object> fieldmap) throws TransformerException {
		StringBuilder atomPayload = new StringBuilder();
		source = getXMLRep(source,"source",fieldmap.get(FollowXPath.Source.getName()).toString());
		resourceType = getXMLRep(resourceType,"resourceType",fieldmap.get(FollowXPath.ResourceType.getName()).toString());
		resourceId = getXMLRep(resourceId,"resourceId",fieldmap.get(FollowXPath.ResourceId.getName()).toString());
		
		atomPayload.append(feedNameSpace).append(source).append(resourceType).append(resourceId).append(closeEntry);
		return atomPayload.toString();
	}

}
