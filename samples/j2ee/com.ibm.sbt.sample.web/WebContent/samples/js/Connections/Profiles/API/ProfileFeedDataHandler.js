require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/ProfileConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts,ProfileFeed) {
        var domNode = dom.byId("ProfileFeed");
        var ProfileFeed = domNode.text || domNode.textContent;

        var profileFeedHandler = new XmlDataHandler({
            data : ProfileFeed,
            namespaces : consts.Namespaces,
            xpath : consts.ProfileFeedXPath
        });
    
        try {
            var results = [];
            var entitiesArray = profileFeedHandler.getEntitiesDataArray(ProfileFeed);
            for (var i=0; i<entitiesArray.length; i++) {
                var profileEntryHandler = new XmlDataHandler({
                    data : entitiesArray[i],
                    namespaces : consts.Namespaces,
                    xpath : consts.ProfileXPath
                });
                results.push(getResults(profileEntryHandler));
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
        "id" : dataHandler.getAsString("id"),
        "userid" : dataHandler.getAsString("userid"),
        "name" : dataHandler.getAsString("name"),
        "email" : dataHandler.getAsString("email"),
        "title" : dataHandler.getAsString("title"),
        "statusUpdate" : dataHandler.getAsString("title[@type='text']"),
        "statusLastUpdate" : dataHandler.getAsString("updated"),
        "altEmail" : dataHandler.getAsString("altEmail"),
        "photoUrl" : dataHandler.getAsString("photoUrl"),
        "photoUrl" : dataHandler.getAsString("photoUrl"),
        "jobTitle" : dataHandler.getAsString("jobTitle"),
        "organizationUnit" : dataHandler.getAsString("organizationUnit"),
        "fnUrl" : dataHandler.getAsString("fnUrl"),
        "telephoneNumber" : dataHandler.getAsString("telephoneNumber"),
        "building" : dataHandler.getAsString("building"),
        "floor" : dataHandler.getAsString("floor"),
        "officeNumber" : dataHandler.getAsString("officeNumber") || "<empty>",
        "streetAddress" : dataHandler.getAsString("streetAddress"),
        "extendedAddress" : dataHandler.getAsString("extendedAddress"),
        "locality" : dataHandler.getAsString("locality"),
        "postalCode" : dataHandler.getAsString("postalCode"),
        "region" : dataHandler.getAsString("region"),
        "countryName" : dataHandler.getAsString("countryName"),
        "soundUrl" : dataHandler.getAsString("soundUrl"),
        "summary" : dataHandler.getAsString("summary"),
        "groupwareMail" : dataHandler.getAsString("groupwareMail")
    };
}