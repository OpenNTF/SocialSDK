var currentCommunity = null;

require(["sbt/Endpoint", "sbt/connections/CommunityService", "sbt/dom"], function(Endpoint, CommunityService, dom) {
    var endpoint = Endpoint.find("connections");
    var url = "/connections/opensocial/basic/rest/people/@me/";
    if (endpoint.proxyPath == "smartcloud") {
        url = "/manage/oauth/getUserIdentity";
    }
    endpoint.request(url, {handleAs: "json"}).then(
        function(response) {
            handleLoggedIn(response.entry, CommunityService, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
    dom.setText("success", "Please wait... Loading your profile entry");
});

function loadCommunity(communityService, dom) {
    currentCommunity = null;
    var communityId = dom.byId("communityId").value;
    if (communityId) {
        communityService.getCommunity(communityId).then(
            function(community) {
                handleCommunityLoaded(community, dom);
            },
            function(error) {
                handleError(dom, error);
            } 
        );
    } else {
        communityService.getMyCommunities({ ps: 1 }).then(
            function(communities) {
                var community = (!communities || communities.length == 0) ? null : communities[0];
                handleCommunityLoaded(community, dom);
            },
            function(error) {
                handleError(dom, error);
            }       
        );
    }
}

function createCommunity(communityService, title, type, content, tags, dom) {
    currentCommunity = null;
    var community = communityService.newCommunity(); 
    community.setTitle(title);
    community.setCommunityType(type);
    community.setContent(content);
    community.setTags(tags);
    communityService.createCommunity(community).then(  
        function(community) { 
            community.load().then(
                function(community) { 
                    handleCommunityCreated(community, dom);
                },
                function(error) {
                    handleError(dom, error);
                }
            );
        },
        function(error) {
            handleError(dom, error);
        }
    );
    
    displayMessage(dom, "Please wait... Creating community: " + community.getTitle());
}

function deleteCommunity(community, dom) {
    community.remove().then(               
        function() { 
            handleCommunityDeleted(community, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
    
    displayMessage(dom, "Please wait... Deleting community: " + community.getCommunityUuid());
}

function updateCommunity(community, title, type, content, tags, dom) {
    community.setTitle(title);
    community.setCommunityType(type);
    community.setContent(content);
    community.setTags(tags);
    community.update().then(               
        function(communityId) {
            handleCommunityUpdated(community, dom);
        },
        function(error) {
            handleError(dom, error);
        }
    );
    displayMessage(dom, "Please wait... Updating community: " + community.getCommunityUuid());
}

function handleLoggedIn(entry, CommunityService, dom) {
    var communityService = new CommunityService();
    loadCommunity(communityService, dom);

    addOnClickHandlers(communityService, dom);
    
    displayMessage(dom, "Please wait... Loading your latest community");
}

function handleCommunityLoaded(community, dom) {
    if (!community) {
        dom.byId("communityId").value = "";
        dom.byId("communityType").value = "private";
        dom.byId("communityTitle").value = "";
        dom.byId("communityContent").value = "";
        dom.byId("communityTags").value = "";
        displayMessage(dom, "Unable to load community."); 
        return;
    }
    
    dom.byId("communityId").value = community.getCommunityUuid();
    dom.byId("communityType").value = community.getCommunityType();
    dom.byId("communityTitle").value = community.getTitle();
    dom.byId("communityTags").value = community.getTags().join();
    
    currentCommunity = community;

    var content = community.getContent();
    if (!content || content.length == 0) {
        content = community.getSummary();
    }
    dom.byId("communityContent").value = content;
   
    displayMessage(dom, "Successfully loaded community: " + community.getCommunityUuid());
}

function handleCommunityCreated(community, dom) {
    handleCommunityLoaded(community, dom);
    
    displayMessage(dom, "Successfully created community: " + community.getCommunityUuid());
}

function handleCommunityDeleted(community, dom) {
    dom.byId("communityId").value = "";
    dom.byId("communityTitle").value = "";
    dom.byId("communityContent").value = "";
    dom.byId("communityTags").value = "";
    
    currentCommunity = null;

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
        var type = dom.byId("communityType");        
        var content = dom.byId("communityContent");
        var tags = dom.byId("communityTags");
        createCommunity(communityService, title.value, type.value, content.value, tags.value, dom);
    };
    dom.byId("deleteBtn").onclick = function(evt) {
        var id = dom.byId("communityId");
        if (currentCommunity) {
            deleteCommunity(currentCommunity, dom);
        }
    };
    dom.byId("updateBtn").onclick = function(evt) {
        var id = dom.byId("communityId");
        if (currentCommunity) {
            var title = dom.byId("communityTitle");
            var type = dom.byId("communityType");
            var content = dom.byId("communityContent");
            var tags = dom.byId("communityTags");
            updateCommunity(currentCommunity, title.value, type.value, content.value, tags.value, dom);
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
