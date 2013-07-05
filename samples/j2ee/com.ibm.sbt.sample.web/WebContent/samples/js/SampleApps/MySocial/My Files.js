require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "library",
	         pinFile: true,
	         hideSorter:true,
	         hidePager:true
	    });
        
        grid.renderer.template = dom.byId("fileRow").innerHTML;
        
        grid.renderer.renderTable =  function(grid, el, items, data){
        	var div = this._create("div",{"class":"span3"}, el );
        	
        	var headingDiv = this._create("div", {"class":"examples"},div);
        	var h3 = this._create("h3", {
        		"class":"h3",
        		innerHTML: '<a href="">My Files</a>'
        	}, headingDiv);
        	
        	var ul = this._create("ul", {style:"list-style:none outside none;margin: 0;"},div);
            return ul;	
        };
        
	    dom.byId("gridDiv").appendChild(grid.domNode);
	    grid.update();
});