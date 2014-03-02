/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
 
dojo.provide("sbt.connections.controls.profiles.ProfileTagsGrid");

/**
 * @module sbt.connections.controls.profiles.ProfileTagsGrid
 */
define([ "../../../declare", 
         "../../../lang",
		 "../../../config",
		 "../../../controls/grid/Grid", 
		 "./ProfileTagsGridRenderer", 
		 "./ProfileTagAction", 
		 "../../../store/parameter",
		 "../../../connections/ProfileConstants"], 
        function(declare, lang, sbt, Grid, ProfileTagsGridRenderer, ProfileTagAction, parameter, consts) {

    /**
     * @class ProfileTagsGrid
     * @namespace sbt.connections.controls.profiles
     * @module sbt.connections.controls.profiles.ProfileTagsGrid
     */
    var ProfileTagsGrid = declare(Grid, {
    	
    	/**
    	 * @param lite Specifies how much tag information you want to retrieve. The options are lite or full.
    	 */
    	format : "full",
    	
    	/**
    	 * @param options, This is a list of all
    	 * the different types of profile tags grids available.
    	 * Depending on which one is selected specific arguments will be given to
    	 * the atom store and grid renderer.
    	 */
        options : {
            "list" : {
                storeArgs : {
                    url : consts.AtomTagsDo,
                    feedXPath : consts.ProfileTagsXPath,
                    attributes : consts.ProfileTagsXPath,
                    namespaces : consts.Namespaces
                },
                rendererArgs : {
                    type : "list"
                }
            }
        },
        
        /**
         * A profile tag action, defines default behaviour for when 
         * items in the grid are clicked on or hovered on,
         * it is possible to override these actions
         */
        profileTagAction: new ProfileTagAction(),
        
        /**
         * This is the default grid that will be created if no 
         * arguments are given.
         */
        defaultOption: "list",
        
        /**Constructor function
         * @method constructor
         * */
        constructor: function(args) {
        },
        
        contextRootMap: {
            profiles: "profiles"
        },
        
        /**
         * Override buildUrl to add format, target and source
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = { format : this.format };
            
            if (this.query) {
            	params = lang.mixin(params, this.query);
            }
            if (this.targetEmail) {
            	params = lang.mixin(params, { targetEmail : this.targetEmail });
            } 
            if (this.targetKey) {
            	params = lang.mixin(params, { targetKey : this.targetKey });
            } 
            if (this.sourceEmail) {
            	params = lang.mixin(params, { sourceEmail : this.sourceEmail });
            } 
            if (this.sourceKey) {
            	params = lang.mixin(params, { sourceKey : this.sourceKey });
            } 

            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },
        
        /**
         * The post create function is called, after the grid has been created.
         * The function will call the super classes post create
         * then load the semantic tag service. The semantic tag service
         * is Javascript for creating business card functionality.
         * @method postCreate
         */
        postCreate: function() {        	
        	this.inherited(arguments);
        },
        
        /**
         * Creates a renderer for the grid.The renderer is responsible for 
         * loading the grid's HTML content.
         * @method createDefaultRenderer
         * @param args sets the template the renderer will use, by checking args.type, but for
         * profile grids this will always be "profile"
         * @returns an instance of a  profile gird renderer.
         */
        createDefaultRenderer : function(args) {
            return new ProfileTagsGridRenderer(args);
        },
        
        /**
         * In the grid HTML an element can have an event attached 
         * using dojo-attach-event="onClick: handleClick".
         * This method is the handler for the onclick event.
         * @method handleClick
         * @param el the element that fired the event
         * @param data all of the items from the current row of the grid. 
         * @param ev the event 
         */
        handleClick: function(el, data, ev) {
            if (this.profileTagAction) {
            	console.log(data);
                this._stopEvent(ev);
                
                this.profileTagAction.execute(data, { grid : this.grid }, ev);
            }
        }
        
        // Internals
        
    });

    return ProfileTagsGrid;
});