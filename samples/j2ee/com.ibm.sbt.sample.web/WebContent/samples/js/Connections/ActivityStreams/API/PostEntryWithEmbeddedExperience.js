require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var postData = {};
	var actor = postData.actor = {};
	actor.id = ASConstants.ASUser.ME;
	var object = postData.object = {};
	object.id = "objectid";
	object.displayName = "Entry with Embeded Experience";
	postData.verb = ASConstants.Verb.POST;
	var openSocial = postData.openSocial = {};
	var embed = openSocial.embed = {};
	embed.gadget = "https://icsqs.ibm.com:444/connections/resources/web/com.ibm.social.ee/ConnectionsEE.xml";
	var context = embed.context = {};
	context.id = "12345";
    var promise = acticityStreamService.postEntry(
    	postData,
		ASConstants.ASUser.PUBLIC,
		ASConstants.ASGroup.ALL,
		ASConstants.ASApplication.ALL
    );
    promise.then(
        function(newEntry) {
            dom.setText("json", json.jsonBeanStringify(newEntry));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
    
});
