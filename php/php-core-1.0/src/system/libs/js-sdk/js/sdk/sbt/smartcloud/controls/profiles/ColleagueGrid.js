define(["../../../declare",
        "./ProfileGrid",
        "./ColleagueGridRenderer"], 
  function(declare,ProfileGrid,ColleagueGridRenderer){
	
	var ColleagueGrid = declare(ProfileGrid,{
		
		hideViewAll: false,
		
		createDefaultRenderer : function(args) {
            return new ColleagueGridRenderer(args);
        },
        
		handleViewAll: function(item, opts, event){
			this.hideViewAll = true;
			this.renderer.template = this.renderer.fullTemplate;
            this.renderer.render(this, this.domNode, opts.items, opts);
	
		}
	});
	
	 
	
	return ColleagueGrid;
});