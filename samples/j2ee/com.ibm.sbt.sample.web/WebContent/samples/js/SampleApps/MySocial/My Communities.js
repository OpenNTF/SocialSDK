require([ "sbt/dom", "sbt/connections/controls/communities/CommunityGrid" ],
	function(dom, CommunityGrid) {
	
	        var communityGrid = new CommunityGrid({
	            type: "my",
	            hidePager: true,
	            hideSorter:true
	        });
	        communityGrid.renderer.template = dom.byId("MyCommunitiesTmpl").innerHTML;
			
	        communityGrid.renderer.renderTable =  function(grid, el, items, data){
	        	var div = this._create("div",{style:"width:300px; height:200px;"}, el );
	        	var ul = this._create("ul", {style:"list-style:none outside none;margin: 0;"},div);
	            return ul;	
	        };
	        
		    dom.byId("myCommunitiesDiv").appendChild(communityGrid.domNode);
	        communityGrid.update();		

	}
);
