require(["sbt/connections/ProfileService","sbt/dom", "sbt/json"], function(ProfileService,dom, json) {
	var targetProfileId = "%{name=sample.email2}";
	var sourceProfileId = "%{name=sample.email1}";
	var profileService = new ProfileService();
	//method call updateTags below will remove any existing tags which were added by same user and will add the new tags.
	//To add more tags to existing, one can first retrieve existing tags which were added by source user and send them along with new tags.
	//existing tags which were added by same user can be retrieved using profileService.getTags(targetProfileId,{sourceEmail:sourceProfileId})
	var promise = profileService.updateTags(['testTag1', 'testTag2'], targetProfileId , sourceProfileId);
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