require(["sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/controls/communities/CommunityGrid", 
         "sbt/connections/controls/bootstrap/CommunityRendererMixin",
         "sbt/lang"], 
         
function(dom, stringUtil, CommunityGrid, CommunityRendererMixin, lang) {
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
    
    lang.mixin(grid.renderer, CommunityRendererMixin);

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
