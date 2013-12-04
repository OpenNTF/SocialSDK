require([ "sbt/dom", "sbt/json", "sbt/connections/BookmarkService" ], 
	function(dom, json, BookmarkService) {
	try {
		var bookmarkService = new BookmarkService();
		var promise = bookmarkService.getServiceConfigEntries();
		promise.then(function() {
			//Information whether email is hidden or not can be accessed using promise.summary.isEmailExposed like below:
			dom.setText("json", json.jsonBeanStringify({isEmailExposed : promise.summary.isEmailExposed}));
		}, function(error) {
			dom.setText("json", json.jsonBeanStringify(error));
		});
	} catch (error) {
		dom.setText("json", json.jsonBeanStringify(error));
	}
});