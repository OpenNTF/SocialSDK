require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
    	var activityStreamService = new ActivityStreamService();
    	var postData = { //creating post data
    			actor:{
    				id:"@me"
    			},
    			object:{
    				id:"objectid",
    				displayName:"IBM software universe event"
    			},
    			verb:"invite"
    		};
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