require([ "sbt/dom", "sbt/json" ], function(dom,json) {
    
    var results = {
        name : "DemoSnippet",
        communityUuid : "%{sample.communityId}"
    };
        
    dom.setText("json", json.jsonBeanStringify(results));
});
