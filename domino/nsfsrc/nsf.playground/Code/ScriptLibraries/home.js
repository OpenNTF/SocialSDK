dojo.addOnLoad(function(){
    /*accessibility*/
   	/*set proper navigation roles for two sets of tabs*/   
  	dojo.forEach(dojo.query(".dijitTabListWrapper"),function(node){
  		if (node.parentNode==dojo.byId("dijit_layout_TabContainer_0_tablist")){
  			dojo.attr(node,"role","navigation");
    		dojo.attr(node,"aria-label","primary");	
  		}else if (node.parentNode==dojo.byId("dijit_layout_TabContainer_1_tablist")){
        	dojo.attr(node,"role","navigation");
    		dojo.attr(node,"aria-label","best practices");
    	}else{
    		//nothing
    		console.log("uh oh:" + node)
    	}
	});
  	/*assign level 2 headings to the accordion section titles*/
    dojo.forEach(dojo.query(".dijitAccordionTitle"),function(node){
        dojo.attr(node,"role","heading");
        dojo.attr(node,"aria-level",2);
    });	
});//end dojo.addOnLoad
