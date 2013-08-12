require(["sbt/connections/ProfileAdminService","sbt/dom", "sbt/json"], function(ProfileAdminService,dom, json) {
	var id = "%{sample.createProfileId}";
	var profileAdminService = new ProfileAdminService();	
	var promise = profileAdminService.deleteProfile(id);
	promise.then(function(id){
			var result = toJson(id);
			dom.setText("json",json.jsonBeanStringify(result));
		},function(error){
			dom.setText("json", json.jsonBeanStringify(error));
		}
	);
});	

function toJson(id) {
    return {
        "id" : id        
    };
}