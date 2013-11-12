require(["sbt/dom", "sbt/json", "sbt/connections/CommunityConstants", "sbt/connections/CommunityService"], 
    function(dom,json,consts,CommunityService) {

    var now = new Date();
    var title = "CreateCommunity Test " + now.getTime();
    var content = "Create community test content: " + now.getTime();

    var communityService = new CommunityService();

    try { 
        var results = [];
        
        // Invalid title
        var community = communityService.newCommunity();
        community.setTitle(title);
        community.setContent(content + " - 1");
        community.setTags("tag1,tag2,tag3");
        community.setCommunityType(consts.Public);
        promise = communityService.createCommunity(community);
        promise.then(
            function(community) {
                results.push(community);
                dom.setText("json", json.jsonBeanStringify(results));
                
                community = communityService.newCommunity();
                community.setTitle(title);
                community.setContent(content + " - 1");
                community.setTags("tag1,tag2,tag3");
                community.setCommunityType(consts.Public);
                promise = communityService.createCommunity(community);
                promise.then(
                    function(community) {
                        results.push(community);
                        dom.setText("json", json.jsonBeanStringify(results));
                    },
                    function(error) {
                        results.push(error);
                        dom.setText("json", json.jsonBeanStringify(results));
                    }
                );
            },
            function(error) {
                results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        );
    } catch (err) {
        dom.setText("json", err);
    }
    
});