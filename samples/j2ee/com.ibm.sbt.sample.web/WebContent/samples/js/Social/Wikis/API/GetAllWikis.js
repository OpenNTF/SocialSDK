require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        var wikiService = new WikiService();
        var promise = wikiService.getAllWikis({
                asc : true,
                page : 1,
                ps : 2
            });
        promise.then(
            function(wikis) {
                dom.setText("json", json.jsonBeanStringify(wikis));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
