require(["sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/CommunityGridRenderer", 
         "sbt/config",
         "sbt/i18n",
         "sbt/dom", 
         "sbt/connections/CommunityConstants"],
        function(Grid, CommunityGridRenderer, sbt, i18n, dom) {
    
        var gridRenderer = new CommunityGridRenderer();
         
        var grid = new Grid({
            storeArgs : {
                url : sbt.connections.communitiesUrls.allCommunities,
                attributes : sbt.connections.communityConstants.xpath_community
            },
            renderer : gridRenderer
        });
         
        dom.byId("gridDiv").appendChild(grid.domNode);
         
        grid.update();
    }
);