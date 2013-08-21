require(["sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/CommunityService",
         "sbt/connections/controls/profiles/ProfilePanel",
         "sbt/connections/controls/profiles/ProfileTagsGrid",
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/astream/ActivityStreamWrapper"],
function(dom, stringUtil, CommunityService, ProfilePanel, ProfileTagsGrid, CommunityGrid, ActivityStreamWrapper) {
	// Profile Panel
	var profilePanel = new ProfilePanel({
        email : '%{sample.email1}'
    }, document.createElement('div'));
    dom.byId("profilePanelDiv").appendChild(profilePanel.domNode);

    // Profile Tags Grid
    var profileTagsGrid = new ProfileTagsGrid({
        type : "list",
        targetEmail : "%{sample.email1}"
    });
    dom.byId("profileTagsGridDiv").appendChild(profileTagsGrid.domNode);
    profileTagsGrid.update();
	
    // Community Grid
    var communityService = new CommunityService();
    var communityGrid = new CommunityGrid({
        type: "public",
        query: { tag: "course" }
    });
    communityGrid.communityAction = {
        getTooltip : function(item) {
        	return stringUtil.replace("Display recent updates for {title}", { title : item.getValue("title") });
        },

        execute : function(item,opts,event) {
            var communityUuid = item.getValue("communityUuid");
            communityUuid = communityService.getUrlParameter(communityUuid, "communityUuid");
            
            // remove existing
            var recentUpdatesDiv = dom.byId("recentUpdatesDiv");
            while (recentUpdatesDiv.childNodes[0]) {
                dom.destroy(recentUpdatesDiv.childNodes[0]);
            }

            // add a new wrapper
            var feedUrl = "/basic/rest/activitystreams/urn:lsid:lconn.ibm.com:communities.community:{communityUuid}/@all/@status?rollup=true";
            feedUrl = stringUtil.replace(feedUrl, { communityUuid : communityUuid });
            var activityStreamWrapper = new ActivityStreamWrapper({
                feedUrl : feedUrl,
                activityStreamNode : "activityStream"
            });
            recentUpdatesDiv.appendChild(activityStreamWrapper.domNode);
            activityStreamWrapper.startup();
        }
    };
    dom.byId("communityGridDiv").appendChild(communityGrid.domNode);
    communityGrid.update();
});
