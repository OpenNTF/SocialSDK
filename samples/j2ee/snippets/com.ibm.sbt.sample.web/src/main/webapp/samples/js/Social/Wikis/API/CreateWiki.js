require([ "sbt/dom", "sbt/json", "sbt/connections/WikiService" ], function(dom,json,WikiService) {
        
	var label = "%{name=WikiService.wikiLabel}";
	var title = "%{name=WikiService.wikiTitle}" || label;
	var summary = "%{name=WikiService.wikiSummary}" || "Wiki summary for: "+label;
	var tags = "%{name=WikiService.wikiTags}" || "wiki";
	
	var wikiService = new WikiService();
	var wiki = wikiService.newWiki(); 
    wiki.setTitle(title);
    wiki.setSummary(summary);
    wiki.setTags(tags);
    wiki.setLabel(label);
    var promise = wikiService.createWiki(wiki);
    promise.then(
        function(wiki) {
            dom.setText("json", json.jsonBeanStringify(wiki));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
});
