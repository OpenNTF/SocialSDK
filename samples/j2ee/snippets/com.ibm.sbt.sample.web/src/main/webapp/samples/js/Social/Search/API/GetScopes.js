require([ "sbt/dom", "sbt/json", "sbt/connections/SearchService" ], function(dom,json,SearchService) {
        var searchService = new SearchService();
        var promise = searchService.getScopes();
        promise.then(
            function(scopes) {
                dom.setText("json", json.jsonBeanStringify(scopes));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
