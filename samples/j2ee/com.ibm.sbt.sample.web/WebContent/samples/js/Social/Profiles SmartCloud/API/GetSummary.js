require([ "sbt/dom", "sbt/json", "sbt/smartcloud/ProfileService" ], 
	function(dom, json, ProfileService) {
	try {
		var profileService = new ProfileService();
		var promise = profileService.getMyConnections();
		promise.then(function() {
			dom.setText("json", json.jsonBeanStringify(promise.summary));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	} catch (error) {
		dom.setText("json", json.jsonBeanStringify(error));
	}
});