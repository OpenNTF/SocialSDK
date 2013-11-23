require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        
	var wikiLabel = "%{name=WikiService.wikiLabel|helpSnippetId=Social_Wikis_Get_My_Wikis}";
	var label = "%{name=WikiService.wikiPageLabel}";
	var title = "%{name=WikiService.wikiPageTitle}" || label;
	var summary = "%{name=WikiService.wikiPageSummary}" || "Wiki summary for: "+label;
	var tags = "%{name=WikiService.wikiPageTags}" || "wikipage";
	
	var wikiService = new WikiService();
	var wikiPage = wikiService.newWikiPage();
	wikiPage.setWikiLabel(wikiLabel);
	wikiPage.setTitle(title);
	wikiPage.setSummary(summary);
	wikiPage.setTags(tags);
	wikiPage.setLabel(label);
    var promise = wikiService.createWikiPage(wikiPage);
    promise.then(
        function(wikiPage) {
            dom.setText("json", json.jsonBeanStringify(wikiPage));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
