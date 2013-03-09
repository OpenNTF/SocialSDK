require(["sbt/Endpoint", "sbt/connections/CommunityService", "sbt/dom"], function(Endpoint, CommunityService, dom) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl: "/connections/opensocial/basic/rest/people/@me/",
        handleAs: "json",
        loginUi: "dialog",
        load: function(response) {
            handleLoggedIn(response.entry, CommunityService, dom);
        },
        error: function(error) {
            handleError(dom, error);
        }
    });
    displayMessage(dom, "Please wait... Loading your profile entry");
});

function loadCommunity(communityService, dom) {
    communityService.getMyCommunities({
        parameters: { ps: 1 },
        load: function(communities) {
            handleCommunitiesLoaded(communities, dom);
        },
        error: function(error) {
            handleError(dom, error);
        }       
    });
}

function createCommunity(communityService, title, content, dom) {
    var entry = dom.byId("entry").getUserData("entry");
    
    var community = communityService.getCommunity({ loadIt : false }); 
    community.setTitle(title);
    community.setContent(content);
    community.setAuthor(entry);
    community.setContributor(entry);
    communityService.createCommunity(community, {               
        load : function(community) { 
            handleCommunityCreated(community, dom);
        },
        error : function(error) {
            handleError(dom, error);
        }
    });
    
    displayMessage(dom, "Please wait... Creating community: " + community.getTitle());
}

function deleteCommunity(community, dom) {
    community.remove({               
        load : function() { 
            handleCommunityDeleted(community, dom);
        },
        error : function(error) {
            handleError(dom, error);
        }
    });
    
    displayMessage(dom, "Please wait... Deleting community: " + community.getCommunityUuid());
}

function updateCommunity(community, title, content, tags, dom) {
    community.setTitle(title);
    community.setContent(content);
    community.setTags(tags);
    community.update({               
        load : function(community) {
            handleCommunityUpdated(community, dom);
        },
        error : function(error) {
            handleError(dom, error);
        }
    });
    displayMessage(dom, "Please wait... Updating community: " + community.getCommunityUuid());
}

function handleLoggedIn(entry, CommunityService, dom) {
    dom.byId("entry").setUserData("entry", entry, function() {});
    
    var communityService = new CommunityService();
    loadCommunity(communityService, dom);

    addOnClickHandlers(communityService, dom);
    
    displayMessage(dom, "Please wait... Loading your latest community");
}

function handleCommunitiesLoaded(communities, dom) {
    if (!communities || communities.length == 0) {
        dom.byId("communityId").value = "";
        dom.byId("communityTitle").value = "";
        dom.byId("communityContent").value = "";
        dom.byId("communityTags").value = "";
        displayMessage(dom, "You are not a member of any communities."); 
        return;
    }
    
    dom.byId("communityTitle").value = communities[0].getTitle();
    dom.byId("communityTags").value = communities[0].getTags().join();
    dom.byId("communityId").value = communities[0].getCommunityUuid();
    dom.byId("communityId").setUserData("community", communities[0], function() {});

    var content = communities[0].getContent();
    if (!content || content.length == 0) {
        content = communities[0].getSummary();
    }
    dom.byId("communityContent").value = content;
   
    displayMessage(dom, "Successfully loaded community: " + communities[0].getCommunityUuid());
}

function handleCommunityCreated(community, dom) {
    var id = dom.byId("communityId");
    id.value = community.getCommunityUuid();
    id.setUserData("community", community, function() {});
    
    displayMessage(dom, "Successfully created community: " + community.getCommunityUuid());
}

function handleCommunityDeleted(community, dom) {
    dom.byId("communityId").value = "";
    dom.byId("communityId").setUserData("community", null, function() {});
    dom.byId("communityTitle").value = "";
    dom.byId("communityContent").value = "";
    dom.byId("communityTags").value = "";

    displayMessage(dom, "Successfully deleted community: " + community.getCommunityUuid());
}

function handleCommunityUpdated(community, dom) {
    displayMessage(dom, "Successfully updated community: " + community.getCommunityUuid());
}

function addOnClickHandlers(communityService, dom) {
    dom.byId("loadBtn").onclick = function(evt) {
        loadCommunity(communityService, dom);
    };
    dom.byId("createBtn").onclick = function(evt) {
        dom.byId("communityId").value = "";
        var title = dom.byId("communityTitle");
        var content = dom.byId("communityContent");
        createCommunity(communityService, title.value, content.value, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        var id = dom.byId("communityId");
        var community = id.getUserData("community");
        if (community) {
            deleteCommunity(community, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        var id = dom.byId("communityId");
        var community = id.getUserData("community");
        if (community) {
            var title = dom.byId("communityTitle");
            var content = dom.byId("communityContent");
            var tags = dom.byId("communityTags");
            updateCommunity(community, title.value, content.value, tags.value, dom);
        }
    };
}

function displayMessage(dom, msg) {
    dom.setText("success", msg); 
    
    dom.byId("success").style.display = "";
    dom.byId("error").style.display = "none";
}

function handleError(dom, error) {
    dom.setText("error", "Error: " + error.message);
    
    dom.byId("success").style.display = "none";
    dom.byId("error").style.display = "";
}

function clearError(dom) {
    dom.setText("error", "");
    
    dom.byId("error").style.display = "none";
}
