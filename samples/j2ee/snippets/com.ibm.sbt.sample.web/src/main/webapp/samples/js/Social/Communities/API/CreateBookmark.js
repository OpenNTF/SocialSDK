require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
        var communityService = new CommunityService();  
        
        var time = ((new Date()).getTime());
        var bookmarkJson = {
        	"title" : "IBM " + time,
        	"link" : "www.ibm.com#" + time
        };

        var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        
        var promise = communityService.createBookmark(communityId, bookmarkJson);
        promise.then(
        	function(bookmark) {
                dom.setText("json", json.jsonBeanStringify(bookmark));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);