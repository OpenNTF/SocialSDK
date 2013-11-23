require([ "sbt/dom", "sbt/json" ], function(dom,json) {
    var results = {
        name : "DemoSnippet",
        communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}"
    };
        
    dom.setText("json", json.jsonBeanStringify(results));
});
