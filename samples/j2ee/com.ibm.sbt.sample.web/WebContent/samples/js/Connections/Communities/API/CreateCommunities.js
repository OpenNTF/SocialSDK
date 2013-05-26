require(["sbt/dom", "sbt/json", "sbt/connections/CommunityConstants", "sbt/connections/CommunityService"], 
    function(dom,json,consts,CommunityService) {

    var now = new Date();
    var title = "CreateCommunity Test " + now.getTime();
    var content = "Create community test content: " + now.getTime();

    var communityService = new CommunityService();

    try { 
        var results = [];
        
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
                results.push(community);
                dom.setText("json", json.jsonBeanStringify(results));
            },
            function(error) {
                results.push(error);
                dom.setText("json", json.jsonBeanStringify(results));
            }
        );
    
        // Community instance
        var community = communityService.newCommunity();
        community.setCommunityType(consts.Restricted);
        community.setTitle(title + " - 2");
        community.setContent(content + " - 2");
        community.setTags("tag1,tag2,tag3");
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
    
        // Load it
        community.setTitle(title + " - 3");
        community.setContent(content + " - 3");
        community.setTags("tag1,tag2,tag3");
        promise = communityService.createCommunity(community);
        promise.then(
            function(community) {
                results.push(community);
                dom.setText("json", json.jsonBeanStringify(results));
                community.load().then(
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
        
        // Invalid title
        community.setTitle(title + " - 4");
        community.setContent(content + " - 4");
        community.setTags("tag1,tag2,tag3");
        community.setCommunityType(consts.Restricted);
        promise = communityService.createCommunity(community);
        promise.then(
            function(community) {
                results.push(community);
                dom.setText("json", json.jsonBeanStringify(results));
                
                promise = communityService.createCommunity(community);
                promise.then(
                    function(community) {
                        results.push(community);
                        dom.setText("json", json.jsonBeanStringify(results));
                    },
                    function(error) {
                        results.push({ error : error, type : "invalidTitle" });
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