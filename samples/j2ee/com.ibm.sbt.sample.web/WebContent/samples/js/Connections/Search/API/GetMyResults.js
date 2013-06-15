require([ "sbt/dom", "sbt/json", "sbt/connections/SearchService" ], function(dom,json,SearchService) {
        var searchService = new SearchService();
        var promise = searchService.getMyResults("%{sample.searchQuery}");
        promise.then(
            function(results) {
                dom.setText("json", json.jsonBeanStringify(results));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
