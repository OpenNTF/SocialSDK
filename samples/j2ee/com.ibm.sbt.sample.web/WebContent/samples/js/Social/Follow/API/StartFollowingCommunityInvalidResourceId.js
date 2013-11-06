require(["sbt/connections/FollowService", "sbt/dom", "sbt/json"], 
    function(FollowService, dom, json) {
        var followService = new FollowService();
        followService.startFollowing("invalidResourceId").then(
    		function(resource) {
                dom.setText("json", json.jsonBeanStringify(resource));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
		);
					
	}

);
