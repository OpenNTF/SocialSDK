require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
    var communityService = new CommunityService();  
    var community = communityService.getCommunity({     
        loadIt : false
    }); 
    var now = new Date();
    community.setTitle("Test Community" + now.getTime());
    community.setContent("Test community created: " + now);
    communityService.createCommunity(community, {               
        load : function(community){         
            dom.setText("content","Title of new community is " + community.getTitle());
        },
        error : function(error){
            dom.setText("content","Error received. Error Code = " +  error.code + ". Error Message = " + error.message);
        }
    });
});