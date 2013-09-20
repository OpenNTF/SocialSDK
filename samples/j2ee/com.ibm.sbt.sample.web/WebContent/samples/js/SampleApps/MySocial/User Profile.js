require(["sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/CommunityService",
         "sbt/connections/controls/profiles/ProfilePanel",
         "sbt/connections/controls/profiles/ProfileTagsGrid",
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/controls/search/SearchGrid",
         "sbt/connections/controls/astream/ActivityStreamWrapper"],
function(dom, stringUtil, CommunityService, ProfilePanel, ProfileTagsGrid, CommunityGrid, SearchGrid, ActivityStreamWrapper) {
	// Profile Panel
	var profilePanel = new ProfilePanel({
        userid : '%{name=sample.userId1}'
    }, document.createElement('div'));
    dom.byId("profilePanelDiv").appendChild(profilePanel.domNode);

    // Profile Tags Grid
    var profileTagsGrid = new ProfileTagsGrid({
        type : "list",
        targetKey : "%{name=sample.userId1}"
    });
    profileTagsGrid.profileTagAction = {
        getTooltip : function(item) {
        	return string.substitute("Display people tagged with ${term}", { title : item.getValue("term") });
        },
        execute : function(item,opts,event) {
            var term = item.getValue("term");
            
            var searchGrid = new SearchGrid({
                type: "public",
                query : { component : "profiles", tag : term }
           });
           dom.byId("taggedProfilesDiv").appendChild(searchGrid.domNode);
           searchGrid.update();
        }
    };
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
