require(["sbt/controls/grid/Grid", 
         "sbt/controls/grid/connections/ConnectionsGridRenderer", 
         "sbt/config",
         "sbt/i18n",
         "sbt/dom", 
         "sbt/text!sbt/controls/grid/connections/templates/CommunityRow.html",
         "sbt/connections/CommunityConstants"],
        function(Grid, ConnectionsGridRenderer, sbt, i18n, dom, CommunityRow) {
    
        var gridRenderer = new ConnectionsGridRenderer({
            _nls: { 
                summary : "Public Communities",
                updatedBy : "Updated by "
             },
             template: CommunityRow,
             rowClass: function(grid, item, i, items) {
                 return (i === 0 ? "placeRow lotusFirst" : "placeRow");
             },
             numOfMembers: function(grid, item, i, items) {
                 return this.getValue(item, "memberCount");
             },
             updatedDate: function(grid, item, i, items) {
                 return i18n.getUpdatedLabel(this.getValue(item, "updated"));
             },
             displayTags: function(grid, item, i, items) {
                 return "display: none";
             },
             tagsLabel: function(grid, item, i, items) {
                 return "";
             },
             tagsAnchors: function(grid, item, i, items) {
                 return "";
             },
             getValue: function(item, name) {
                 if (item.hasOwnProperty(name)) {
                     return item[name];
                 } else {
                     return item.getValue(name);
                 }
             }
        });
         
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