
require([ "sbt/dom", "sbt/json", "sbt/connections/SearchService" ], function(dom,json,SearchService) {
        var searchService = new SearchService();
        var promise = searchService.getTagged("%{name=sample.searchTag}");
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
