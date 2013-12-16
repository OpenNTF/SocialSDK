require(["sbt/declare", "sbt/dom", "sbt/connections/controls/communities/CommunityGrid",
         "sbt/controls/view/BaseView", "sbt/controls/view/Action"], 
    function(declare, dom, CommunityGrid, BaseView, Action) {
	    var baseView = new BaseView({ 
	    	title : "Communities", 
	    	iconClass : "lotusIcon iconsComponentsBlue24 iconsComponentsBlue24-CommunitiesBlue24",
	    	searchArgs : { selectedApplication : "communities" }
	    });
	    
	    addActions(baseView, declare, Action);
	    setContent(baseView, CommunityGrid);
	    
	    dom.byId("baseViewDiv").appendChild(baseView.domNode);
	}
);

function addActions(baseView, declare, Action) {
	var StartCommunity = declare("StartCommunity", [Action], {
		name: "Start a Community",
		execute: function(file, opt) {
			alert(this.name);
		}
	});
	
	baseView.addAction(new StartCommunity());
}

function setContent(baseView, CommunityGrid) {
	var communityGrid = new CommunityGrid();
    
    baseView.setContent(communityGrid);
}

