require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid", "sbt/connections/CommunityService"], function(dom, CommunityGrid, CommunityService) {
    var grid = new CommunityGrid({
         type: "my",
         hideSorter: true
    });
             
    var domNode = dom.byId("communityRow");
    var CustomCommunityRow = domNode.text || domNode.textContent;
    grid.renderer.template = CustomCommunityRow;

    dom.byId("gridDiv").appendChild(grid.domNode);
    
    grid.update();
    
	var communityService = new CommunityService();

    dom.byId("deleteBtn").onclick = function(evt) {
        var communities = grid.getSelected();
        
        var str = "";
        for (i in communities) {
        	str += (str.length == 0) ? "    " : "\n    ";
            str += communities[i].data.getValue("title");
        }
        if (str.length == 0) {
        	alert("Select the communities you want to delete.");
        } else {
        	var ret = confirm("Are you sure you want to deleted the following:\n" +
        			str + "\n It is not possible to restore deleted communities!");
        	if (ret) {
        		deleteCommunities(grid, communityService, dom, communities);
        	}
        }
        
    };
});

function deleteCommunities(grid, communityService, dom, communities) {
	var community = communities.pop();
	if (!community) {
		grid.refresh();
		return;
	}
	
    var uid = community.data.getValue("uid");
    var communityUuid = extractCommunityUuid(communityService, uid);
    var title = community.data.getValue("title");
    
    var statusDiv = dom.byId("statusDiv");
    statusDiv.innerHTML = "Deleting community: "+title;
    
    communityService.deleteCommunity(communityUuid).then(
        function(communityUuid) {
        	statusDiv.innerHTML = "Sucessfully deleted community: "+title;
        	deleteCommunities(grid, communityService, dom, communities);
        }, function(error) {
            alert("Unable to delete '"+ title + "' aborting operation.");
        }
    );
}

function extractCommunityUuid(service, uid) {
    if (uid && uid.indexOf("http") == 0) {
        return service.getUrlParameter(uid, "communityUuid");
    } else {
        return uid;
    }
};




