require(["sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants", "sbt/dom"], 
    function(ActivityStreamService, ASConstants, dom) {
		var userID = "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}";
    	var activityStreamService = new ActivityStreamService();
    	var postData = { //creating post data
    			content: "A test update @ "+new Date()
    		};
    	var promise = activityStreamService.postMicroblogEntry(
    			postData,
    	    	userID,
    			ASConstants.ASGroup.ALL
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