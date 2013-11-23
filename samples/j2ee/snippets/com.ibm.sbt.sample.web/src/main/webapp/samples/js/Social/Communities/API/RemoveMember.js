require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    
	try {
        var communityService = new CommunityService();
        var communityUuid = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var id = "%{name=sample.id2|helpSnippetId=Social_Profiles_Get_Profile}";
        var promise = communityService.removeMember(communityUuid, id);
        promise.then(
            function(entityId) {
                var result =  { entityId : entityId, response : promise.response };                
                dom.setText("json", json.jsonBeanStringify(result));
            },
            function(error) {            	
                dom.setText("json", json.jsonBeanStringify(error));
            }       
        );
    } catch (error) {    	
        dom.setText("json", json.jsonBeanStringify(error));
    }  
    
});
