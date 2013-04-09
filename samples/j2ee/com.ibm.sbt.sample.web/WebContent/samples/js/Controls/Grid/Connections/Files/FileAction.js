require(["sbt/dom", "sbt/controls/grid/connections/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "library"
	    });
        
        grid.fileAction = {
        	
        	getTooltip : function(item) {
        	    return "Tooltip override function";
        	     
        	},

        	execute : function(item,opts,event) {
        		alert("override execute function");
        		
        	}
        		
        };
		         
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});