define(["../../../../declare", "../../../../config", 
        "../../../../widget/_TemplatedWidget", "./ConfigOverlay", 
        "../../../../controls/TypeAhead", "../../../../text!./templates/ConfigButton.html"], 
        function(declare, config, _TemplatedWidget, ConfigOverlay, TypeAhead, template){
    var configExtension = declare([_TemplatedWidget], {
        imgSrc: null,
        altText: "",
        templateString: template,
        refreshEventName: "refreshStreamEvent",
        configOverlay: null,
        currentConfig: "",
        
        postMixInProperties: function(){
            this.imgSrc = config.Properties.sbtUrl + "/sbt/connections/controls/astream/extensions/images/redcog.png";
            dojo.subscribe(this.refreshEventName, dojo.hitch(this, "closeConfig"));
        },

        /**
         * Open the overlay for configuring the filters.
         * 
         * @method openConfig
         */
        openConfig: function(){
            if(!this.configOverlay){
            	this.configOverlay = new ConfigOverlay({
            		eventName: this.refreshEventName
            	});
            	var asNode = dojo.byId("activityStreamNode");
                var asHeight = dojo.style(asNode, "height") + "px";
                dojo.style(this.configOverlay.domNode, {
                    "background-color": "white",
                    "width": "100%",
                    "height": asHeight,
                    "display": "inline"
                  });
                dojo.place(this.configOverlay.domNode, asNode, "first");
            }else{
                dojo.style(this.configOverlay.domNode, {
                    "display": "inline"
                });
            }
        	
        },
        
        /**
         * Close the overlay for configuring the filters.
         * 
         * @method closeConfig
         */
        closeConfig: function(args){
        	this.currentUrl = args.url;
        	
        	dojo.style(this.configOverlay.domNode, {
        	    "display": "none"
        	});
        	dojo.publish(com.ibm.social.as.constants.events.UPDATESTATE, [{}]);
        }
    });
    
    return configExtension;
});
