require([ "sbt/dom", "sbt/json" ], function(dom,json) {
    
    var results = {
        name : "DemoSnippet",
        communityUuid : "%{name=sample.communityId}"
    };
        
    dom.setText("json", json.jsonBeanStringify(results));
});
