require([ "sbt/dom", "sbt/json", "sbt/connections/SearchService" ], function(dom,json,SearchService) {
    	var requestArgs = {
        	scope : "communities", constraint : { type: "field", id: "title", exactMatch: false }
        };
        var searchService = new SearchService();
        var promise = searchService.getMyResults("%{name=sample.searchQuery}", requestArgs);
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
