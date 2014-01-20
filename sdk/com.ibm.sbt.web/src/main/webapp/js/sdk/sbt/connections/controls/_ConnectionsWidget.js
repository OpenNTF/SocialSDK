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
define(["../../declare", "../../config", "../../connections/controls/astream/_XhrHandler", "dijit/_Widget"], function(declare, config, _XhrHandler){
    
    /*
     * Base class for common functionality of all connections widgets.
     * 
     * @class sbt.controls._ConnectionsWidget
     */
    var _ConnectionsWidget = declare([dijit._Widget],
    {
        /*
         * The url of the resources/web folder, which contains the projects we need.
         * 
         * @property baseUrl
         * @type String
         */
        baseUrl: config.Properties.serviceUrl + "/proxy/connections/${connections}/resources/web/",
        
        /*
         * The _XhrHandler to use in this connections widget.
         * 
         * @property xhrHandler
         * @type {Object}
         */
        xhrHandler: null,
        
        /*
         * Setup with common Connections widget functionality.
         * @param {Object} args
         *     @param {Object} [args.xhrHandler] The _XhrHandler to be used throughout this widget.
         *     @param {String} [endpoint] The endpoint to be used when creating an _XhrHandler for this connections widget.
         */
        constructor: function(args){
            window.ibmConfig = window.ibmConfig || {};
            window.ibmConfig.versionStamp = window.ibmConfig.versionStamp || 1234567890;
            
            this.registerModulePaths();
            this.xhrHandler = args.xhrHandler || this.requireXhr(args.endpoint);
        },
        
        /*
         * Register the connections modules we will be using.
         * 
         * @method registerModulePaths
         */
        registerModulePaths: function(){
            dojo.registerModulePath("com", this.baseUrl + "com");
            dojo.registerModulePath("lconn", this.baseUrl + "lconn");
            dojo.registerModulePath("net", this.baseUrl + "net");
        },
        
        /*
         * Create the _XhrHandler from the endpoint name.
         * 
         * @method requireXhr
         * @param endpointName
         */
        requireXhr: function(endpointName){
            var _endpointName = endpointName || "connections";
            return new _XhrHandler(_endpointName);
        }
        
    });
    
    return _ConnectionsWidget;
});

