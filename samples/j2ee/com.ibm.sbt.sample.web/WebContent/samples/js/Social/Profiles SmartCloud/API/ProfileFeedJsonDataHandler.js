require([ "sbt/base/JsonDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileConstants"], 
    function(JsonDataHandler,lang,dom,json,consts) {

	var feed = {"startIndex":0,"totalResults":2,"entry":[{"photos":[{"value":"PROFILES","type":"Source"},{"value":"20548027__1354513972.jpg","type":"Photo"}],"profileUrl":"https://apps.na.collabserv.com/contacts/contacts/view/966797","orgs":[{"value":"CONTACTS","type":"Source"},{"value":"IBM Test - SDK Renovations","type":"Org"}],"id":"na.collabserv.com:contact:966797","connectedToId":20548027,"addresses":[{"value":"CONTACTS","type":"Source"},{"address":"12 BSL, WK tower, NJ","title":"Primary Address"}],"emailAddress":"alangoodwin@try.lotuslive.com","websites":[{"value":"PROFILES","type":"Source"},{"title":"Website","website":""},{"title":"Website","website":""},{"title":"Website","website":""},{"website":"https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=0f357879-ccee-4927-98c1-7bb88d5dc81f"}],"objectId":966797,"type":"FRIEND","jobtitle":"Technical Sales Manager, WorldWide Team.","updated":"2013-03-26T14:27:58.000Z","ims":[{"value":"PROFILES","type":"Source"}],"emails":[{"value":"CONTACTS","type":"Source"},{"title":"Primary Email","email":"alangoodwin@try.lotuslive.com"}],"org":{"name":"IBM Test - SDK Renovations"},"displayName":"Alan Goodwin","address":"12 BSL, WK tower, NJ","companyId":206004,"phoneNumbers":[{"value":"CONTACTS","type":"Source"},{"title":"Primary Telephone","phone":"440-998-1234"},{"title":"MobilePhone","phone":"+3388721928"}]},{"photos":[{"value":"PROFILES","type":"Source"}],"profileUrl":"https://apps.na.collabserv.com/contacts/contacts/view/966800","orgs":[{"value":"CONTACTS","type":"Source"},{"value":"IBM Test - SDK Renovations","type":"Org"}],"id":"na.collabserv.com:contact:966800","connectedToId":20180530,"addresses":[{"value":"PROFILES","type":"Source"}],"emailAddress":"phil@riand.com","websites":[{"value":"PROFILES","type":"Source"}],"objectId":966800,"type":"FRIEND","jobtitle":"","updated":"2013-03-26T14:28:04.000Z","ims":[{"value":"PROFILES","type":"Source"}],"emails":[{"value":"CONTACTS","type":"Source"},{"title":"Primary Email","email":"phil@riand.com"}],"org":{"name":"IBM Test - SDK Renovations"},"displayName":"Philippe Riand","companyId":206004,"phoneNumbers":[{"value":"PROFILES","type":"Source"}]}],"itemsPerPage":2}
	
	var profileFeedHandler = new JsonDataHandler({
            data : feed,
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
        "entityId" : dataHandler.getEntityId(),
        
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
        "organisationIdN" : dataHandler.getAsNumber("orgId"),
        
        // Test for getAsArray
        "departmentA" : dataHandler.getAsArray("department"),
        "countryA" : dataHandler.getAsArray("country"),
        "aboutA" : dataHandler.getAsArray("about")
    };
}