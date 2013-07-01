require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "library",
	         pinFile: true,
	         hideSorter:true,
	         hidePager:true
	    });
        
        grid.renderer.template = dom.byId("fileRow").innerHTML;
        
        grid.renderer.renderTable =  function(grid, el, items, data){
        	var div = this._create("div",{style:"width:300px; height:200px;"}, el );
        	var ul = this._create("ul", {style:"list-style:none outside none;margin: 0;"},div);
            return ul;	
        };
        
	    dom.byId("gridDiv").appendChild(grid.domNode);
	    grid.update();
});