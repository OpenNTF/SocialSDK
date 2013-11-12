require(["sbt/dom", "sbt/stringUtil", "sbt/connections/controls/communities/CommunityGrid"], function(dom, stringUtil, CommunityGrid) {
    var grid = new CommunityGrid();

    // create custom action
    grid.communityAction = {
        getTooltip : function(item) {
        	return stringUtil.replace("Display details for {title}", { title : item.getValue("title") });
        },

        execute : function(item,opts,event) {
            var str =
                "communityUuid: " + item.getValue("communityUuid") + "\n" +
                "title: " + item.getValue("title") + "\n" +
                "summary: " + item.getValue("summary") + "\n" +
                "communityUrl: " + item.getValue("communityUrl");
            alert(str);
        }
    };

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
