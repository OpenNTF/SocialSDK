require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
        var communityService = new CommunityService();  
        var community = communityService.getCommunity({     
            loadIt : false
        }); 
        var now = new Date();
        community.setTitle("Test Community " + now.getTime());
        community.setContent("Test community created: " + now);
        communityService.createCommunity(community, {               
            load: function(community){
                dom.setText("json", json.jsonBeanStringify(community));
            },
            error: function(error){
                dom.setText("json", json.jsonBeanStringify(error));
            }
        });
    }
);