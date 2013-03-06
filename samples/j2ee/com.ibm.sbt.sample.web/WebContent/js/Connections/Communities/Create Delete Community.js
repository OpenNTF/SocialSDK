require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
    var communityService = new CommunityService();

    dom.byId("addBtn").onclick = function(evt) {
        addCommunity(communityService, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        deleteCommunity(communityService, dom);
    };
});

function addCommunity(communityService, dom) {
    var title = dom.byId("communityTitle");
    var content = dom.byId("communityContent");
    
    var community = communityService.getCommunity({ loadIt : false }); 
    community.setTitle(title.value);
    community.setContent(content.value);
    communityService.createCommunity(community, {               
        load : function(community) {         
            var id = dom.byId("communityId");
            id.value = community.getCommunityUuid();
            id.setUserData("community", community, function() {});
        },
        error : function(error) {
            displayError(dom, error);
        }
    });
}

function deleteCommunity(communityService, dom) {
    var id = dom.byId("communityId");
    var community = id.getUserData("community");
    if (community) {
        communityService.deleteCommunity(community, {               
            load : function(community) {         
                id.value = "";
                id.setUserData("community", null, function() {});
            },
            error : function(error) {
                displayError(dom, error);
            }
        });
    }
}

function displayError(dom, error) {
    dom.setText("content", "Error: " + error.message);
}
