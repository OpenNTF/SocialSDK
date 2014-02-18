require(["sbt/dom", 
         "sbt/stringUtil", 
         "sbt/connections/controls/communities/CommunityGrid", 
         "sbt/lang"], 
         
function(dom, stringUtil, CommunityGrid, lang) {
    var grid = new CommunityGrid({
    	theme:"bootstrap",
    	hidePager:true,
    	hideSorter:true,
    	hideFooter:true
    	});

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
    
    grid.renderer.tableClass = "table";

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});
