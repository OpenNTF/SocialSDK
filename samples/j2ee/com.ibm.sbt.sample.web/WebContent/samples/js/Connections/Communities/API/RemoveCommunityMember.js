require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], function(CommunityService, dom, json) {
    
	try {
        var communityService = new CommunityService();
        var communityUuid = "%{sample.communityId}";
        var id = "%{sample.id2}";
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
