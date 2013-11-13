require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/CommunityConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts) {
        var domNode = dom.byId("CommunityMembersFeed");
        var CommunityMembersFeed = domNode.text || domNode.textContent;

        var membersFeedHandler = new XmlDataHandler({
            data : CommunityMembersFeed,
            namespaces : consts.Namespaces,
            xpath : consts.CommunityFeedXPath
        });
    
        try {
            var results = [];
            var entitiesArray = membersFeedHandler.getEntitiesDataArray(CommunityMembersFeed);
            for (var i=0; i<entitiesArray.length; i++) {
                var memberEntryHandler = new XmlDataHandler({
                    data : entitiesArray[i],
                    namespaces : consts.Namespaces,
                    xpath : consts.MemberXPath
                });
                results.push(getResults(memberEntryHandler));
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
        "userid" : dataHandler.getAsString("contributorUserid"),
        "email" : dataHandler.getAsString("contributorEmail"),
        "name" : dataHandler.getAsString("contributorName"),
        "role" : dataHandler.getAsString("role")
    };
}