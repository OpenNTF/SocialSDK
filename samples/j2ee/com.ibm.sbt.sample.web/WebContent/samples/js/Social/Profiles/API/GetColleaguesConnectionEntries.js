require([ "sbt/connections/ProfileService", "sbt/dom", "sbt/json" ], function(ProfileService,dom,json) {
    var results = [];   
	try {
        var profileService = new ProfileService();
        var promise = profileService.getColleaguesConnectionEntry("%{name=sample.id1}");
        promise.then(
            function(connectionEntries) {
            	var result = [];
	        	 for(var i=0; i<connectionEntries.length; i++){
	                 var profile = connectionEntries[i];	           
	                 result.push({"Name" : profile.getTitle()}); 
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
