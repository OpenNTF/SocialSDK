require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        
	var wikiService = new WikiService();
	var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wikis}";
    var promise = wikiService.getWiki(wikiLabel, { includeTags:true, acls:true });
    promise.then(
        function(wiki) {
            dom.setText("json", json.jsonBeanStringify(wiki));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
