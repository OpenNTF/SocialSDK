require(["sbt/dom", "sbt/json", "sbt/connections/CommunityConstants", "sbt/connections/CommunityService"], 
    function(dom,json,consts,CommunityService) {

    var now = new Date();
    var title = "CreateCommunity Test " + now.getTime();
    var content = "Create community test content: " + now.getTime();

    var communityService = new CommunityService();

    try { 
        // Community instance
        var community = communityService.newCommunity();
        community.setCommunityType(consts.Restricted);
        community.setTitle(title + " - 2");
        community.setContent(content + " - 2");
        community.setTags("tag1,tag2,tag3");
        promise = communityService.createCommunity(community);
        promise.then(
            function(community) {
                dom.setText("json", json.jsonBeanStringify(community));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    } catch (err) {
        dom.setText("json", err);
    }
    
});