require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/CommunityConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts) {
        var domNode = dom.byId("CommunityEntryPublic");
        var CommunityEntryPublic = domNode.text || domNode.textContent;

        var communityEntryHandler = new XmlDataHandler({
            data : CommunityEntryPublic,
            namespaces : consts.Namespaces,
            xpath : consts.CommunityXPath
        });

        try {
            var results = getResults(communityEntryHandler);
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);

function getResults(dataHandler) {
    return {
        "getEntityId" : dataHandler.getEntityId(),
        "communityUuid" : dataHandler.getAsString("communityUuid"),
        "title" : dataHandler.getAsString("title"),
        "summary" : dataHandler.getAsString("summary"),
        "alternateUrl" : dataHandler.getAsString("alternateUrl"),
        "selfUrl" : dataHandler.getAsString("selfUrl"),
        "logoUrl" : dataHandler.getAsString("logoUrl"),
        "tags" : dataHandler.getAsArray("tags"),
        "content" : dataHandler.getAsString("content"),
        "memberCount" : dataHandler.getAsNumber("memberCount"),
        "communityType" : dataHandler.getAsString("communityType"),
        "published" : dataHandler.getAsDate("published"),
        "updated" : dataHandler.getAsDate("updated"),
        "authorUid" : dataHandler.getAsString("authorUserid"),
        "authorName" : dataHandler.getAsString("authorName"),
        "authorEmail" : dataHandler.getAsString("authorEmail"),
        "contributorUserid" : dataHandler.getAsString("contributorUserid"),
        "contributorName" : dataHandler.getAsString("contributorName"),
        "contributorEmail" : dataHandler.getAsString("contributorEmail")
    };
}