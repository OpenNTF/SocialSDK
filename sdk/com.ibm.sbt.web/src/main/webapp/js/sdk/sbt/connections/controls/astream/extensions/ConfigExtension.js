/* Copyright IBM Corp. 2011, 2013  All Rights Reserved.              */
define(["../../../../declare", "./ConfigButton"], function(declare, ConfigButton){
    /**
     * This extension hijacks some methods used in the activitystream when it is loaded,
     * and restores them when it is unloaded. Most importantly, it takes over the method
     * the stream uses for getting the current url.
     * 
     * @class ConfigExtension
     */
    var configExtension = declare([com.ibm.social.as.extension.interfaces.IExtension],{
        oldGetCurrentUrl: null,
        configButton: null,
        
    	updateUrl : function(args){
    		this.currentState.urlTemplate = args[0];
    	},
    	
    	newUrlGenConstructor : function(options){
    		dojo.publish(com.ibm.social.as.constants.events.UPDATESTATE, [{}]);
    	},
    	
    	getCurrentUrl : function(){
			return this.configButton.currentUrl;
    	},
        
        onLoad: function(){
        	if(!this.configButton){
                this.configButton = new ConfigButton();
        	}
            this.oldGetCurrentUrl = com.ibm.social.as.controller.UrlGenerator.prototype.getCurrentUrl;
            dojo.extend(com.ibm.social.as.controller.UrlGenerator, {
                getCurrentUrl : dojo.hitch(this, "getCurrentUrl")
            });

            dojo.publish(com.ibm.social.as.constants.events.PLACEHOLDERADD,
                    [com.ibm.social.as.view.placeholder.location.headerRight, this.configButton.domNode]);
        },
        
        onUnload: function(){
            this.added = false;
            
            dojo.extend(com.ibm.social.as.controller.UrlGenerator, {
                getCurrentUrl : this.oldGetCurrentUrl
            });
            
            dojo.publish(com.ibm.social.as.constants.events.PLACEHOLDERREMOVE,
                    [com.ibm.social.as.view.placeholder.location.headerRight]);
        }
    });
    
    return configExtension;
});