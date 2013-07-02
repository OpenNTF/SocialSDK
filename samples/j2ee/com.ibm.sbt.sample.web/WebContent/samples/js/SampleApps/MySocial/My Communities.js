require([ "sbt/dom", "sbt/connections/controls/communities/CommunityGrid" ],
	function(dom, CommunityGrid) {
	
	        var communityGrid = new CommunityGrid({
	            type: "my",
	            hidePager: true,
	            hideSorter:true
	        });
	        communityGrid.renderer.template = dom.byId("MyCommunitiesTmpl").innerHTML;
			
	        communityGrid.renderer.renderTable =  function(grid, el, items, data){
	        	var div = this._create("div",{"class":"span3"}, el );
	        	
	        	var headingDiv = this._create("div", {"class":"examples"},div);
	        	var h3 = this._create("h3", {
	        		"class":"h3",
	        		innerHTML: '<a href="">My Communities</a>'
	        	}, headingDiv);
	        	
	        	var ul = this._create("ul", {"class":"files list"},div);
	            return ul;	
	        };
	        
		    dom.byId("myCommunitiesDiv").appendChild(communityGrid.domNode);
	        communityGrid.update();		

	}
);
