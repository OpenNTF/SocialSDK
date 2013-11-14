require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        
	var wikiService = new WikiService();
	var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wiki_Pages}";
	var pageLabel = "%{name=WikiService.pageLabel|helpSnippetId=Social_Wikis_Get_My_Wiki_Pages}";
    var promise = wikiService.getWikiPage(wikiLabel, pageLabel, { includeTags:true, acls:true });
    promise.then(
        function(wikiPage) {
            dom.setText("json", json.jsonBeanStringify(wiki));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
