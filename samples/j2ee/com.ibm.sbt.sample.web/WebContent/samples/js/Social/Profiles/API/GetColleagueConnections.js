require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/json" ], function(ProfileService,dom,json) {
    try {
        var profileService = new ProfileService();
        var promise = profileService.getColleagueConnections("%{name=sample.id1}");
        promise.then(
            function(connectionEntries) {
            	var result = [];
	        	 for(var i=0; i<connectionEntries.length; i++){
	                 var profile = connectionEntries[i];	           
	                 result.push({"Name" : profile.getTitle()}); 
	             }  
            	dom.setText("json", json.jsonBeanStringify(result));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch(error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }
});
