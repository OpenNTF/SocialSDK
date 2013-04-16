require(["sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/CommunityGridRenderer", 
         "sbt/config",
         "sbt/i18n",
         "sbt/dom",
         "sbt/store/parameter",
         "sbt/connections/CommunityConstants"],
        function(Grid, CommunityGridRenderer, sbt, i18n, dom, parameter) {
    
        var gridRenderer = new CommunityGridRenderer();
         
        var grid = new Grid({
            storeArgs : {
                url : sbt.connections.communitiesUrls.allCommunities,
                attributes : sbt.connections.communityConstants.xpath_community,
                paramSchema: parameter.communities.all
            },
            renderer : gridRenderer
        });
         
        dom.byId("gridDiv").appendChild(grid.domNode);
         
        grid.update();
    }
);