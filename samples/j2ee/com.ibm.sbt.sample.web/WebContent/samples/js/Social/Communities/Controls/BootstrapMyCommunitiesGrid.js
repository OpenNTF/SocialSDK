require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid"], 
         
function(dom, CommunityGrid) {
	var domNode = dom.byId("communityRow");
    var CustomCommunityRow = domNode.text || domNode.textContent;
	
    var grid = new CommunityGrid({
        type: "my"
    });

    grid.renderer.template = CustomCommunityRow;
     
    dom.byId("gridDiv").appendChild(grid.domNode);
              
    grid.update();
	
	
});


