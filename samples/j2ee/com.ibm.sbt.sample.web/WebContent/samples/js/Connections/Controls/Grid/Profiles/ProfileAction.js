require(["sbt/dom", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "profile",
        userid : "%{sample.userId1}"
    });
    
    grid.profileAction = {
    		
    	getTooltip: function(){
    		return "This is the override function;"
    		
    	},
    	
    	execute: function(){
    		alert("This is the click override function");
    	}
    
    };
   
    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
  
});