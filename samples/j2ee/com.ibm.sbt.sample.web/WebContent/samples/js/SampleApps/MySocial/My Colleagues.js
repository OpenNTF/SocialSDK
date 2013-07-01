require(["sbt/dom", "sbt/lang", "sbt/connections/controls/profiles/ProfileGrid"], function(dom, lang, ProfileGrid) {
    var grid = new ProfileGrid({
        type : "colleagues",
        email : "%{sample.email1}",
        hideSorter: true,
        hidePager: true,
        pageSize: 6
    });
    grid.renderer.template = dom.byId("profileRow").innerHTML;
    grid.renderer.renderTable =  function(grid, el, items, data){
    	var div = this._create("div",{style:"width:300px; height:200px;"}, el );
    	var ul = this._create("ul", {style:"list-style:none outside none;margin: 0;"},div);
        return ul;	
    };
    
    grid.profileAction = {
    		
        	execute: function(item, opts, event){
        		window.location.assign(item.fnUrl);
        	}
        
        };
       
    dom.byId("gridDiv").appendChild(grid.domNode);
    grid.update();
});