require([ "sbt/dom", "sbt/config" ], function(dom, config) {
    config.Properties["loginUi"] = "dialog";
    
    var endpoint = config.findEndpoint("connections");
    var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
    
    endpoint.request("/communities/service/atom/community/bookmarks?communityUuid="+communityId).then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});