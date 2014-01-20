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
define(["../../../declare", "../../../lang", "../../../url", "../../../config", "../../../util", "../../../connections/controls/WidgetWrapper", "../../../text!../../../connections/controls/astream/templates/ActivityStreamContent.html"], function(declare, lang, Url, config, util, WidgetWrapper, defaultTemplate) {

    /**
     * The wrapper for the ActivityStream.
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
     * 
     * @class sbt.controls.astream.ActivityStreamWrapper
     */
    var ActivityStreamWrapper = declare([ WidgetWrapper ], {
        /**
         * The url of the css you want to use as the ActivityStream css. This is if on your connections server the css is in a non-default location.
         * @property cssUrl
         * @type String
         */
        cssUrl : "",
        
        /**
         * If the side navigation dijit on the Connections server is using a custom js class.
         * 
         * @property sideNavClassName
         * @type String
         */
        sideNavClassName : "",
        
        /**
         * Set the html template which will go inside the iframe.
         * 
         * @property defaultTemplate
         * @type String
         */
        defaultTemplate: defaultTemplate,
        
        /**
         * Overriding the method in WidgetWrapper for providing the substitutions for variables in the template.
         * 
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            var proxyUrl = this._endpoint.proxy.proxyUrl + "/" + this._endpoint.proxyPath;
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = {};
            if(libQuery){
                libQueryObj = util.splitQuery(libQuery, "&");
            }
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.4.3"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var connectionsSideNav = "~com.ibm.social.as.gadget.viewnav.ASGadgetViewSideNav.js";
            var cssUrl = connectionsUrl + "/${connections}/resources/web/com.ibm.social.as/css/activityStream.css";
            if(this.cssUrl){
                cssUrl = connectionsUrl + this.cssUrl;
            }
            if(this.sideNavClassName){
                if(this.sideNavClassName.lastIndexOf(".js") != this.sideNavClassName.length - 3){
                    connectionsSideNav = "~" + this.sideNavClassName + ".js";
                }else{
                    connectionsSideNav = "~" + this.sideNavClassName;
                }
            }
            
            var sbtProps = lang.mixin({}, config.Properties);
            lang.mixin(sbtProps, {
                libraryUrl: libUrl.getUrl(),
                loginUi: "popup"
            });
            var templateReplacements = {
                args: JSON.stringify(this.args),
                proxyUrl: proxyUrl,
                connectionsUrl: connectionsUrl,
                libraryUrl: libUrl.getUrl(),
                sbtProps: JSON.stringify(sbtProps),
                connectionsASNav: connectionsSideNav,
                cssUrl: cssUrl
            };
            
            
            return templateReplacements;
        },
        
        /**
         * Store the args so that they can be substituted into the defaultTemplate.
         * 
         * @property args
         * @type Object
         * @default null
         */
        args: null,
        
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
         * 
         */
        constructor: function(args){
            this.args = args;
            if(args.cssUrl){
                this.cssUrl = args.cssUrl;
            }
            if(args.sideNavClassName){
                this.sideNavClassName = args.sideNavClassName;
            }
            
            if(lang.isIE() < 10){ // Don't load the stream for IE < 10
                this.defaultTemplate = "";
            }
        }
        
    });
    
    return ActivityStreamWrapper;
});