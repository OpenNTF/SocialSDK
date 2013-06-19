require([ "sbt/dom", "sbt/connections/controls/communities/CommunityGrid" ],
	function(dom, CommunityGrid) {
		try {
	        var communityGrid = new CommunityGrid({
	            type: "my"
	        });
	        communityGrid.renderer.template = dom.byId("MyCommunitiesTmpl").innerHTML;
	        communityGrid.renderer.pagerTemplate = dom.byId("pagerTemplate").innerHTML;
	        communityGrid.renderer.sortTemplate = dom.byId("sortingTemplate").innerHTML;
	        communityGrid.renderer.sortAnchor = dom.byId("sortAnchorTemplate").innerHTML;
	        communityGrid.renderer.tableClass = "table table-striped";
			
		    // create custom action
		    communityGrid.communityAction = {
		        getTooltip : function(item) {
		        	return string.substitute("Display details for ${title}", 
		        			{ title : item.getValue("title") });
		        },

		        execute : function(item,opts,event) {
		            var str =
		                "communityUuid: " + item.getValue("communityUuid") + "\n" +
		                "title: " + item.getValue("title") + "\n" +
		                "communityUrl: " + item.getValue("communityUrl");
		            alert(str);
		        }
		    };

		    dom.byId("myCommunitiesDiv").appendChild(communityGrid.domNode);
	        communityGrid.update();		
		} catch (err) {
			alert(err);
		}
	}
);
