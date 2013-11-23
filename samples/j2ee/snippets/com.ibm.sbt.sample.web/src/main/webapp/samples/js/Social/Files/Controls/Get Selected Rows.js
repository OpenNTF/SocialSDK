require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "publicFiles"
	    });
		
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
	    
	    dom.byId("selectedBtn").onclick = function(evt) {
	        var files = grid.getSelected();
	        var str = "";
	        for (i in files) {
	            str += files[i].data.getValue("label") + " ";
	        }
	        alert((str.length == 0) ? "Nothing Selected" : str);
	    };
});