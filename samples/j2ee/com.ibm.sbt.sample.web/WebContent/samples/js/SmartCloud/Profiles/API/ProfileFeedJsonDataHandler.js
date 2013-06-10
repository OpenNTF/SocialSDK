require([ "sbt/base/JsonDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileConstants", "sbt/smartcloud/MockFeed" ], 
    function(JsonDataHandler,lang,dom,json,consts,mockFeed) {

	var profileFeedHandler = new JsonDataHandler({
            data : mockFeed.feed,
            jsonpath : consts.ProfileJPath
        });

        try {
        	 var results = [];
            var entries = profileFeedHandler.getEntitiesDataArray();
            for(var i=0;i<entries.length;i++) {
            	var profileEntryHandler = new JsonDataHandler({
                     data : entries[i],
                     jsonpath : consts.ProfileJPath
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
    	// Tests for generic methods for single entity
        "getEntityId" : dataHandler.getEntityId(),
//        "entityData" : dataHandler.getEntityData(),
        
        // Tests for getAsString
        "title" : dataHandler.getAsString("title"),
        "profileUrl" : dataHandler.getAsString("profileUrl"),
        "name" : dataHandler.getAsString("displayName"),
        
        // Tests for getAsBoolean
        "thumbnailUrlB" : dataHandler.getAsBoolean("thumbnailUrl"),
        "emailB" : dataHandler.getAsBoolean("emailAddress"),
        "addressB" : dataHandler.getAsBoolean("address"),
        
        // Test for getAsNumber
        "phoneNumber" : dataHandler.getAsNumber("phoneNumbers"),
        
        // Test for getAsArray
        "departmentA" : dataHandler.getAsArray("department"),
        "countryA" : dataHandler.getAsArray("country"),
        "organisationIdA" : dataHandler.getAsArray("orgId"),
        "aboutA" : dataHandler.getAsArray("about")
    };
}