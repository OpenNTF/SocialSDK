require(["sbt/dom", "sbt/json", "sbt/connections/CommunityConstants", "sbt/connections/CommunityService"], 
    function(dom,json,consts,CommunityService) {

    var now = new Date();
    var title = "CreateCommunity Test " + now.getTime();
    var content = "Create community test content: " + now.getTime();

    var communityService = new CommunityService();

    try { 
        // Community JSON
        var communityJson = {
            type : consts.Restricted,
            title : title + " - 1",
            content : content + " - 1",
            tags : [ "tag1", "tag2", "tag3" ]
        };
        var promise = communityService.createCommunity(communityJson);
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