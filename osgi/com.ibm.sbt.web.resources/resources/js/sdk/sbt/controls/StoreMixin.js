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

dojo.provide("sbt.controls.StoreMixin");

/**
 * 
 */
define([ "../declare", "../lang", "../stringUtil", "../util"], 
    function(declare, lang, stringUtil, util) {

    /**
     * @class StoreMixin
     * @namespace sbt.controls
     * @module sbt.controls.StoreMixin
     */
    var StoreMixin = declare(null, {

        /*
         * Arguments for the associated data store
         */
        _storeArgs: null,

        /**
         * Constructor method for ControlStore behaviour.
         * Creates a default store and renderer, if none have been already created
         * @method constructor
         * @param args
         */
        constructor: function(args) {
            if (!this.store) {
                if (args && args.storeArgs) {
                    this._storeArgs = lang.mixin({}, args.storeArgs);
                    this._storeArgs.endpoint = this.endpoint;
                    this.store = this.createDefaultStore(args.storeArgs);
                } else if (this.options) {
                    this._storeArgs = lang.mixin({}, this.options[this.defaultOption].storeArgs);
                    this._storeArgs.endpoint = this.endpoint;
                    if (args && args.type && this.options.hasOwnProperty(args.type)) {
                        lang.mixin(this._storeArgs, this.options[args.type].storeArgs); 
                    }   
                }
                this.store = this.createDefaultStore(this._storeArgs);
            }
        },
        
        /**
         * Create the store to be used with this Grid.
         * 
         * @method - createDefaultStore
         * @param args - the arguments to pass to the atom store, such as URL and attributes
         * @returns - an atom store instance
         */
        createDefaultStore: function(args) {
            var store = this._createDefaultStore(args);
            var url = store.getUrl();
            if (url) {
                url = this.buildUrl(url, args, store.getEndpoint());
            }
            store.setUrl(url);

            return store;
        },
        
        /**
         * Allow Grid to build the complete URL before it is passed to the store.
         * 
         * @method buildUrl
         * @param url base url
         * @param args arguments that will be passed to the store
         * @param endpoint The endpoint, needed to verify if custom service mappings are present.
         * @returns Built url
         */
        buildUrl: function(url, args, endpoint) {
            var params = {};
            if (this.query) {
                params = lang.mixin(params, this.query);
            }
            return this.constructUrl(url, params, this.getUrlParams(), endpoint);
        },
        
        /**
         * Return the auth type to be  used
         * @returns {String}
         */
        getAuthType: function() {
        	return "";
        },
        
        /**
         * Return the url parameters to be used
         * @returns {Object}
         */
        getUrlParams: function() {
        	return { authType : this.getAuthType() };
        },
                
        /**
         * Construct a url using the specified parameters 
         * @method constructUrl
         * @param url
         * @param params
         * @param urlParams
         * @param endpoint An endpoint which may contain custom service mappings.
         * @returns
         */
        constructUrl : function(url,params,urlParams, endpoint) {
            if (!url) {
                throw new Error("Grid.constructUrl: Invalid argument, url is undefined or null.");
            }
            
            if(endpoint){
                lang.mixin(this.contextRootMap, endpoint.serviceMappings);
                
                if(this.contextRootMap){
                    url = stringUtil.transform(url, this.contextRootMap, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    }, this);
                }
            }
            
            if (urlParams) {
                url = stringUtil.replace(url, urlParams);
                
                if (url.indexOf("//") != -1) {
                	// handle empty values
                	url = url.replace(this._regExp, "/");
                }
            }
            if (params) {
                for (param in params) {
                    if (params[param]) {
                        if (url.indexOf("?") == -1) {
                            url += "?";
                        } else if (url.indexOf("&") != (url.length - 1)) {
                            url += "&";
                        }
                        url += param + "=" + encodeURIComponent(params[param]);
                    }
                }
            }
            return url;
        }
        
    });
    
    return StoreMixin;
});
