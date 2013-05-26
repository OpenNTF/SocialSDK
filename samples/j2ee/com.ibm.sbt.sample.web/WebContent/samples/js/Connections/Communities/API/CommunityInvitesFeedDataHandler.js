require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/CommunityConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts) {
        var domNode = dom.byId("CommunityInvitesFeed");
        var CommunityInvitesFeed = domNode.text || domNode.textContent;

        var invitesFeedHandler = new XmlDataHandler({
            data : CommunityInvitesFeed,
            namespaces : consts.Namespaces,
            xpath : consts.CommunityFeedXPath
        });
    
        try {
            var results = [];
            var entitiesArray = invitesFeedHandler.getEntitiesDataArray(CommunityInvitesFeed);
            for (var i=0; i<entitiesArray.length; i++) {
                var inviteEntryHandler = new XmlDataHandler({
                    data : entitiesArray[i],
                    namespaces : consts.Namespaces,
                    xpath : consts.InviteXPath
                });
                results.push(getResults(inviteEntryHandler));
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
        "title" : dataHandler.getAsString("title"),
        "content" : dataHandler.getAsString("content"),
        "updated" : dataHandler.getAsDate("updated"),
        "authorUserid" : dataHandler.getAsString("authorUserid"),
        "authorName" : dataHandler.getAsString("authorName"),
        "contributorUserid" : dataHandler.getAsString("contributorUserid"),
        "contributorName" : dataHandler.getAsString("contributorName")
    };
}