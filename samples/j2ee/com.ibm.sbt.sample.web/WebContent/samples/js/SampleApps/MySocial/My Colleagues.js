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
    	var div = this._create("div",{"class":"span3"}, el );
    	
    	var headingDiv = this._create("div", {"class":"examples"},div);
    	var h3 = this._create("h3", {
    		"class":"h3",
    		innerHTML: '<a href="">My Network</a>'
    	}, headingDiv);
    	
    	var ul = this._create("ul", {"class":"network"},div);
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