require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/json" ], function(ProfileService,dom,json) {
    var results = [];   
	try {
        var profileService = new ProfileService();
        var query = { userid : "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}" };
        var promise = profileService.search(query);
        promise.then(
            function(profiles) {
            	var result = [];
	        	 for(var i=0; i<profiles.length; i++){
	                 var profile = profiles[i];	           
	                 result.push({"Name" : profile.getName()}); 
	             }  
	        	 results.push(result);
            	dom.setText("json", json.jsonBeanStringify(results));
            },
            function(error) {
            	results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        );
    } catch(error) {
    	results.push(error);
        dom.setText("json", json.jsonBeanStringify(results));
    }
});
