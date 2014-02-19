require(["sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/connections/CommunityService",
         "sbt/lang"], 
         
function(dom, stringUtil, CommunityGrid, CommunityService, lang) {
    var grid = new CommunityGrid({ 
    		type: "my",
    		theme:"bootstrap",
    		hidePager:true,
	    	hideSorter:true,
	    	hideFooter:true
    	});
    
    
    var communityService = new CommunityService();
    
    // create custom action
    grid.communityAction = {
        getTooltip : function(item) {
        	return stringUtil.replace("Delete {title}", { title : item.getValue("title") });
        },

        execute : function(item,opts,event) {
           var communityUuid = item.getValue("communityUuid");
           communityUuid = communityService.getUrlParameter(communityUuid, "communityUuid");
           var promise = communityService.deleteCommunity(communityUuid);
           promise.then(
               function(communityUuid) {
            	   grid.refresh();
               }, function(error) {
                   alert(error);
               }
           );
        }
    };

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
