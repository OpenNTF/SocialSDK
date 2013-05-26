require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
    	var activityStreamService = new ActivityStreamService();
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
    	var promise = activityStreamService.postEntry(
    		postData,
			"@me",
			"@all",
			"@all"
    	);
    	promise.then(
            function(newEntry){
                dom.setText("newEntryId", newEntry.entry.id);
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);