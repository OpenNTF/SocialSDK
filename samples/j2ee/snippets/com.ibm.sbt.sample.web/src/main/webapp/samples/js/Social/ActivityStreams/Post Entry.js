require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
    	var activityStreamService = new ActivityStreamService();
    	var postData = { //creating post data
    			actor:{
    				id: "@me"
    			},
    			object: {
    				id: "objectid",
    				displayName: " a test post to activity stream "+new Date().getTime()
    			},
    			verb: ASConstants.Verb.POST
    		};
    	var promise = activityStreamService.postEntry(
    		postData,
    		ASConstants.ASUser.ME,
    		ASConstants.ASGroup.ALL,
    		ASConstants.ASApplication.ALL
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