require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/CommunityConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts) {
        var domNode = dom.byId("CommunityFeed");
        var CommunityFeed = domNode.text || domNode.textContent;

        var communityFeedHandler = new XmlDataHandler({
            data : CommunityFeed,
            namespaces : consts.Namespaces,
            xpath : consts.CommunityFeedXPath
        });
    
        try {
            var results = [];
            var entitiesArray = communityFeedHandler.getEntitiesDataArray();
            for (var i=0; i<entitiesArray.length; i++) {
                var communityEntryHandler = new XmlDataHandler({
                    data : entitiesArray[i],
                    namespaces : consts.Namespaces,
                    xpath : consts.CommunityXPath
                });
                results.push(getResults(communityEntryHandler));
            }
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