require(["sbt/connections/CommunityService","sbt/dom"], function(CommunityService,dom) {
    var communityService = new CommunityService();

    dom.byId("loadBtn").onclick = function(evt) {
        loadCommunity(communityService, dom);
    };
    dom.byId("createBtn").onclick = function(evt) {
        createCommunity(communityService, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        deleteCommunity(communityService, dom);
    };
    dom.byId("updateBtn").onclick = function(evt) {
        updateCommunity(communityService, dom);
    };
});

function loadCommunity(communityService, dom) {
    communityService.getMyCommunities({
        parameters: { ps: 1 },
        load: function(communities) {
            if (!communities || communities.length == 0) {
                dom.setText("content", "You are not a member of any communities."); 
                return;
            }
            
            dom.byId("communityTitle").value = communities[0].getTitle();
            dom.byId("communityContent").value = communities[0].getContent();
            dom.byId("communityTags").value = communities[0].getTags().join();
            dom.byId("communityId").value = communities[0].getCommunityUuid();
            dom.byId("communityId").setUserData("community", communities[0], function() {});
            
            clearError(dom);
        },
        error: function(error) {
            displayError(dom, error);
        }       
    });
}

function createCommunity(communityService, dom) {
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
            
            clearError(dom);
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
                
                clearError(dom);
            },
            error : function(error) {
                displayError(dom, error);
            }
        });
    }
}

function updateCommunity(communityService, dom) {
    var id = dom.byId("communityId");
    var community = id.getUserData("community");
    if (community) {
        var title = dom.byId("communityTitle");
        var content = dom.byId("communityContent");
        community.setTitle(title.value);
        community.setContent(content.value);
        community.update({               
            load : function(community) {
                clearError(dom);
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

function clearError(dom) {
    dom.setText("content", "");
}
