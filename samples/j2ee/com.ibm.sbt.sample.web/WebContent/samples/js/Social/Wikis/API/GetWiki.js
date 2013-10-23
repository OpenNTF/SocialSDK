require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        
	var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wikis}";
	
	var wikiService = new WikiService();
    var promise = wikiService.getWiki(wikiLabel);
    promise.then(
        function(wiki) {
            dom.setText("json", json.jsonBeanStringify(wiki));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
