require([ "sbt/base/XmlDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml", "sbt/connections/ProfileConstants" ], 
    function(XmlDataHandler,lang,dom,json,xml,consts) {
        var domNode = dom.byId("ProfileEntry");
        var ProfileEntry = domNode.text || domNode.textContent;

        var profileEntryHandler = new XmlDataHandler({
            data : ProfileEntry,
            namespaces : consts.Namespaces,
            xpath : consts.ProfileXPath
        });

        try {
            var results = getResults(profileEntryHandler);
            dom.setText("json", json.jsonBeanStringify(results));
        } catch (error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    }
);

function getResults(dataHandler) {
    return {
        "getEntityId" : dataHandler.getEntityId(),
        "id" : dataHandler.getAsString("id") || "<empty>",
        "userid" : dataHandler.getAsString("userid") || "<empty>",
        "name" : dataHandler.getAsString("name") || "<empty>",
        "email" : dataHandler.getAsString("email") || "<empty>",
        "title" : dataHandler.getAsString("title") || "<empty>",
        "altEmail" : dataHandler.getAsString("altEmail") || "<empty>",
        "photoUrl" : dataHandler.getAsString("photoUrl") || "<empty>",
        "jobTitle" : dataHandler.getAsString("jobTitle") || "<empty>",
        "organizationUnit" : dataHandler.getAsString("organizationUnit") || "<empty>",
        "fnUrl" : dataHandler.getAsString("fnUrl") || "<empty>",
        "telephoneNumber" : dataHandler.getAsString("telephoneNumber") || "<empty>",
        "building" : dataHandler.getAsString("building") || "<empty>",
        "floor" : dataHandler.getAsString("floor") || "<empty>",
        "officeNumber" : dataHandler.getAsString("officeNumber") || "<empty>",
        "streetAddress" : dataHandler.getAsString("streetAddress") || "<empty>",
        "extendedAddress" : dataHandler.getAsString("extendedAddress") || "<empty>",
        "locality" : dataHandler.getAsString("locality") || "<empty>",
        "postalCode" : dataHandler.getAsString("postalCode") || "<empty>",
        "region" : dataHandler.getAsString("region") || "<empty>",
        "countryName" : dataHandler.getAsString("countryName") || "<empty>",
        "soundUrl" : dataHandler.getAsString("soundUrl") || "<empty>",
        "summary" : dataHandler.getAsString("summary") || "<empty>",
        "groupwareMail" : dataHandler.getAsString("groupwareMail") || "<empty>"
    };
}