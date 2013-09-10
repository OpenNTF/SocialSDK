var results = [];

require(["sbt/dom", "sbt/json", "sbt/connections/ProfileService"], 
    function(dom,json,ProfileService) {
    var profileService = new ProfileService();    
    var profileId = "%{name=sample.userId1|helpSnippetId=Social_Profiles_Get_Profile}";
    profileService.getProfile(profileId).then(    
        function(profile) {
        	results.push({"Cache count after first get operation" : profileService._cache.count});
        	dom.setText("json", json.jsonBeanStringify(results));
        	profile.setJobTitle("%{name=sample.updateProfileJobTitle}");	
        	profileService.updateProfile(profile).then(
        		function (profile){
        			results.push({"Cache count after first update operation" : profileService._cache.count});
                	dom.setText("json", json.jsonBeanStringify(results));
                	profileService.getProfile(profileId).then(
                			function(profile) {
                	        	results.push({"Cache count after second get operation" : profileService._cache.count});
                	        	dom.setText("json", json.jsonBeanStringify(results));
                			}, function(error){
                        		dom.setText("json", json.jsonBeanStringify(error));
                        	}
                		);	
        		}, function(error){
            		dom.setText("json", json.jsonBeanStringify(error));
            	}
        	);
        },
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
    
    
});
