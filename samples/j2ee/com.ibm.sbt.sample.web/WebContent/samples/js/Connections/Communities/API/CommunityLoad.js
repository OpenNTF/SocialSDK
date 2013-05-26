require(["sbt/dom", "sbt/json", "sbt/connections/CommunityService"], function(dom,json,CommunityService) {
	var communityService = new CommunityService();
	var community = communityService.newCommunity(); 
	community.setCommunityUuid("%{sample.communityId}");
    var results = [];
    var promise = community.load();
    promise.then(    
        function(community){
            results.push(community);
            dom.setText("json", json.jsonBeanStringify(results));
        },
        function(error){
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
});