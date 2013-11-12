require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "myFiles"
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