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
define(["../../../declare", "../../../lang", "../../../dom", "../../../connections/controls/_ConnectionsWidget", "../../../connections/controls/astream/_SbtAsConfigUtil", "../../../connections/controls/sharebox/_InputForm"], function(declare, lang, dom, _ConnectionsWidget, _SbtAsConfigUtil, _InputForm){
    /**
     * Wrapper for the connections ActivityStream Dijit.
     * 
     * @class sbt.controls.astream._ActivityStream
     */
    var _ActivityStream = declare([_ConnectionsWidget],
    {    
        /**
         * The ConfigUtil will be held here.
         * 
         * @property configUtil 
         * @type Object
         * @default null
         */
        configUtil: null,

        /**
         * The InputForm will be held here.
         * 
         * @property inputForm 
         * @type Object
         * @default null
         */
        inputForm: null,

        /**
         * This wraps the given node in a div with class="lotusContent lotusBoard" since the ActivityStream css rules expect such a layout.
         * Used to wrap the ActivityStream's dom node.
         * 
         * @method wrapDomNode
         * 
         * @param {String} domNodeId The id of the dom node to wrap.
         */
        wrapDomNode: function(domNodeId){
            var asNode = document.getElementById(domNodeId);
            var parent = asNode.parentNode;
            var wrapperDiv = document.createElement('div');
            wrapperDiv.className= "lotusContent lotusBoard";
            
            parent.appendChild(wrapperDiv);
            wrapperDiv.appendChild(asNode);
        },
        
        /**
         * The constructor. This will set up the connections ActivityStream dijit with a config object, and create the ShareBox and SideNav.
         * Takes EITHER a feedUrl and an optional extensions object, OR an ActivityStream config object. 
         * If a feedUrl is specified any config object supplied will be ignored.
         * 
         * @method constructor
         * 
         * @param {Object} args
         *     @params {String} args.activityStreamNode The id of the html element to attach the ActivityStream to.
         *     @params {String} args.shareBoxNode The id of the html element to attach the share box to.
         *     @params {String} args.sideNavNode The id of the html element to attach the views side nav to.
         *     @params {String} args.feedUrl The url of the feed to populate the ActivityStream with.
         *     @params {Object} [args.extensions] A simple list of extensions to load.
         *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
         *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
         *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
         *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
         *         
         *     @params {Object} args.config An ActivityStream config object. Only specify this without a feedUrl argument.
         * @param {String} activityStreamNode: The node to attach the ActivityStream to. This should have its div created in the defaultTemplate.
         * @param {String} shareBoxNode: The node to attach the ShareBox to. This should have its div created in the defaultTemplate.
         * @param {String} sideNavNode: The node to attach the views SideNav to. This should have its div created in the defaultTemplate.
         */
        constructor: function(args){
            this.mixinXhrHandler();
            var configUtil = new _SbtAsConfigUtil(this.xhrHandler);
            
            args.xhrHandler = this.xhrHandler;
            if(args.activityStreamNode){
                this.wrapDomNode(args.activityStreamNode);
            }
            var self = this;
            
            configUtil.buildSbtConfig(args).then(function(cfg){
                if(args.sideNavNode && Object.keys(cfg.views).length > 1){
                    if(com.ibm.social.as.nav){
                        new com.ibm.social.as.nav.ASSideNav({
                            configObject : cfg
                        }, dom.byId(args.sideNavNode));
                    }else{
                        new com.ibm.social.as.gadget.viewnav.ASGadgetViewSideNav({
                            configObject : cfg
                        }, dom.byId(args.sideNavNode));
                    }
                }
                if(args.activityStreamNode){
                    window.activityStreamConfig = cfg;
                    new com.ibm.social.as.ActivityStream({
                        configObject: cfg,
                        domNode: args.activityStreamNode,
                        isGadget: false,
                        selectedState: true
                    });
                }
            });
            
        },
        
        /**
         * Overwrite the ActivityStream's XhrHandler with our own.
         * 
         * @method mixinXhrHandler
         */
        mixinXhrHandler: function(){
            if(com.ibm.social.as.util.xhr.XhrHandler.init !== undefined){
                com.ibm.social.as.util.xhr.XhrHandler.init(this.xhrHandler);
            }
            else{
                lang.mixin(com.ibm.social.as.util.AbstractHelper.prototype, this.xhrHandler);
            }
        },
        
        /**
         * Function to update the ActivityStream with the latest data.
         * 
         * @method update
         */
        update: function(){
            dojo.publish("com/ibm/social/as/event/updatestate", [true]);
        }
        
    });
    return _ActivityStream;
});
