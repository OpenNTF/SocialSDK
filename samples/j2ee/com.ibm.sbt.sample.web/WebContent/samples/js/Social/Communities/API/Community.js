require([ "sbt/base/XmlDataHandler", "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(XmlDataHandler,dom,json,CommunityService) {
        try {
            var results = [];
            
            var communityService = new CommunityService();
            var community = communityService.newCommunity("%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}");
            results.push(community);
            dom.setText("json", json.jsonBeanStringify(results));
            
            var promise = community.load();
            promise.then(
                function(community) {
                    results.push(community);
                    dom.setText("json", json.jsonBeanStringify(results));
                }, function(error) {
                    results.push(error);
                    dom.setText("json", json.jsonBeanStringify(results));
                }
            );
        } catch (err) {
            dom.setText("json", json.jsonBeanStringify(err));
        }
    }
);
