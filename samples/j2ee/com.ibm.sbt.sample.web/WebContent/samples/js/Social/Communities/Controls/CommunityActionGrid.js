require(["sbt/dom", "sbt/connections/controls/communities/CommunityGrid", "dojo/string"], function(dom, CommunityGrid,string) {
    var grid = new CommunityGrid();

    // create custom action
    grid.communityAction = {
        getTooltip : function(item) {
        	return string.substitute("Display details for ${title}", { title : item.getValue("title") });
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
