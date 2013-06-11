require([ "sbt/base/JsonDataHandler", "sbt/lang", "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileConstants"], 
    function(JsonDataHandler,lang,dom,json,consts) {

	var entry = {"entry":{"photos":[{"value":"PROFILES","type":"Source"},{"value":"20547574__1354512034.jpg","type":"Photo"}],"telephone":45609,"aboutMe":"Sales Executive IBM Collaboration Software","profileUrl":"https://apps.na.collabserv.com/contacts/profiles/view/20547574","mobilephone":33001,"orgs":[{"value":"PROFILES","type":"Source"},{"value":"IBM Test - SDK Renovations","type":"Org"}],"country":"US","website":"https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=0f357879-ccee-4927-98c1-7bb88d5dc81f","id":"na.collabserv.com:user:20547574","orgId":20542369,"addresses":[{"value":"PROFILES","type":"Source"},{"address":"","title":"Address"},{"address":"","title":"Address"},{"address":"","title":"Address"},{"address":"Mountain View","title":"Primary Address"}],"photo":"20547574__1354512034.jpg","emailAddress":"frankadams@try.lotuslive.com","websites":[{"value":"PROFILES","type":"Source"},{"title":"Website","website":""},{"title":"Website","website":""},{"title":"Website","website":""},{"website":"https://www.ibm.com/developerworks/mydeveloperworks/groups/service/html/communityview?communityUuid=0f357879-ccee-4927-98c1-7bb88d5dc81f"}],"fullName":"Frank Adams","objectId":20547574,"jobtitle":"Sales Executive","ims":[{"value":"PROFILES","type":"Source"}],"emails":[{"value":"PROFILES","type":"Source"},{"title":"Primary Email","email":"frankadams@try.lotuslive.com"}],"org":{"name":"IBM Test - SDK Renovations"},"displayName":"Frank Adams","address":"Mountain View","phoneNumbers":[{"value":"PROFILES","type":"Source"},{"title":"Work ","phone":45609},{"title":"Residence 1","phone":45608},{"title":"Residence 2","phone":45787},{"title":"Primary Telephone","phone":45609},{"title":"MobilePhone","phone":330011}]}};
	
	var profileEntryHandler = new JsonDataHandler({
            data : entry,
            jsonpath : consts.ProfileJPath
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
    	// Tests for generic methods for single entity
        "entityId" : dataHandler.getEntityId(),
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
        "organisationIdN" : dataHandler.getAsNumber("orgId"),
        
        // Test for getAsArray
        "departmentA" : dataHandler.getAsArray("department"),
        "countryA" : dataHandler.getAsArray("country"),
        "aboutA" : dataHandler.getAsArray("about")
    };
}