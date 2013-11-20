require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var targetProfileId = "%{name=sample.email2}";
	var sourceProfileId = "%{name=sample.email1}";
	var profileService = new ProfileService();
	var promise = profileService.createTags(['testTag1', 'testTag2'], targetProfileId , sourceProfileId);
	promise.then(
		function(){
			profileService.getTags(targetProfileId).then(    
	            function(tags) {
	                dom.setText("json", json.jsonBeanStringify(tags));
	            },
	            function(error) {            	
	                dom.setText("json", json.jsonBeanStringify(error));
	            }
	        );
		},function(error){
			dom.setText("json", json.jsonBeanStringify(error));
		}
	);
});