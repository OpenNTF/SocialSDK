require([ "sbt/dom", "sbt/json", "sbt/connections/SearchService" ], function(dom,json,SearchService) {
        var searchService = new SearchService();
        var requestArgs = {
        	scope : "communities", constraint : { type: "field", id: "title", exactMatch: false }
        };
        var promise = searchService.getResults("%{name=sample.searchQuery}", requestArgs);
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
