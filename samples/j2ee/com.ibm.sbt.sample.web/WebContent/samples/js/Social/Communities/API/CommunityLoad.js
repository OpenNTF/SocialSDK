require(["sbt/dom", "sbt/json", "sbt/connections/CommunityService"], function(dom,json,CommunityService) {
	var communityService = new CommunityService();
	var community = communityService.newCommunity(); 
	community.setCommunityUuid("%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}");
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