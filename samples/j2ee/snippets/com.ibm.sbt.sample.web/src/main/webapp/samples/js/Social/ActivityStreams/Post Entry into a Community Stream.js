require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
		var communityID = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
    	var activityStreamService = new ActivityStreamService();
    	var postData = { //creating post data
    			actor:{
    				id:"@me"
    			},
    			object:{
    				id:"objectid",
    				displayName:"IBM software universe event to community"
    			},
    			verb:"invite"
    		};
    	var promise = activityStreamService.postEntry(
    		postData,
    		ASConstants.ASUser.COMMUNITY+communityID,
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