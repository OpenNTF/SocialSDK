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
define(["../../../declare", "../../../config", "../../../url", "com.ibm.social.as.gadget.ActivityStreamConfigUtil"], function(declare, config, Url){
    /*
     * @class sbt.controls.astream._SbtAsConfigUtil A helper module for building ActivityStream config objects.
     */
    var _SbtAsConfigUtil = declare([com.ibm.social.as.gadget.ActivityStreamConfigUtil],
    {
        /*
         * The constructor. 
         * 
         * @method constructor
         * @param {Object} xhrHandler The xhrHandler to use when making requests for user info.
         */
        constructor: function(xhrHandler){
            this.xhrHandler = xhrHandler;
        },
        
        /**
         * Retrieves user info from connections. 
         * @method getUserInfo
         * @returns {lconn.core.util.LCDeferred} A promise. Resolves with a user info object containing a user id, an os id and a displayName.
         */
        getUserInfo: function() {
            var promise = new lconn.core.util.LCDeferred();
            var microbloggingUrl = lconn.core.config.services.microblogging.secureUrl;
            var relativeUrl = "";
            var fullProxy = this.xhrHandler.endpoint.proxy.proxyUrl + "/" + this.xhrHandler.endpoint.proxyPath;
            var index = microbloggingUrl.indexOf(fullProxy);
            if(index !== -1){
                relativeUrl = microbloggingUrl.slice(index + fullProxy.length);
            }
            else{
                relativeUrl = new Url(microbloggingUrl).getPath();
            }
            var serviceUrl = relativeUrl + "/" + this.xhrHandler.endpoint.authType + "/rest/people/@me/@self";
            
            this.xhrHandler.xhrGet({
                serviceUrl: serviceUrl, //TODO Change these when opensocial oauth and/or endpoints become available
                handleAs: "json",
                load: function(resp) {
                    var user = resp && resp.entry;
                    if (user && user.id && user.displayName) {
                        
                        var connId = user.id;
                        if (connId && connId.indexOf('urn:lsid:lconn.ibm.com:') != -1) {
                            var endPrefix = connId.lastIndexOf(':');
                            if (endPrefix != -1) {
                                connId = connId.substr(endPrefix+1);
                            }
                        }

                        var ui = {
                                id: connId,
                                osId: user.id,
                                displayName: user.displayName
                        };
                        promise.resolve(ui);
                    }
                },
                error: function() {
                    promise.reject();
                }
            });
            
            return promise;
        },
        
        /*
         * Require only the extensions that we need.
         * 
         * @method requireExtensions
         * @param {Array} extensionsArray Array containing a list of the modules to require.
         */
        requireExtensions: function(extensionsArray){
            var i;
            for(i = 0; i < extensionsArray.length; i++){
                var ext = extensionsArray[i];
                dojo.require(ext);
            }
        },
        
        getBoardIdFromUrl: function(url){
            if(url.indexOf("urn:lsid:")===-1) // is it a community?
                return "@me";
            var stream = "/activitystreams/";
            var index = url.indexOf(stream)+stream.length;
            var idString = url.slice(index);
            index = 0;
            var result = idString.substring(index, idString.indexOf("/"));
            
            return result;
        },
        
        getBoardIdFromAppId: function(appId){
            if(appId.indexOf("urn:lsid:" === -1)){//is it a community?
                return "@me";
            }
            return appId;
        },
        
        /*
         * Build the config from a full config object. Adds missing information if
         * needed, such as user info.
         * 
         * @method buildSbtConfigFull
         * @param {Object} cfg An ActivityStream config object. If this does not contain user info then user info will be added based on the currently authenticated user.
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfigFull: function(cfg){
            if(cfg.eeManager)
                delete cfg.eeManager;
            var cfgPromise = new lconn.core.util.LCDeferred();
            if (!cfg.userInfo || !cfg.userInfo.id || !cfg.userInfo.displayName) {
                cfg.boardId = this.getBoardIdFromAppId(cfg.defaultUrlTemplateValues.appId);
                this.getUserInfo().then(
                    function(ui) {
                        if (ui && ui.id && ui.displayName)
                            cfg.userInfo = ui;
                        cfgPromise.resolve(cfg);
                    },
                    function(error) {
                        cfgPromise.reject(error);
                    });
            } else {
                cfgPromise.resolve(cfg);
            }
            if(cfg.extensions)
                this.requireExtensions(cfg.extensions);
            return cfgPromise;
        },
        
        /*
         * Builds a config object from a feedUrl and an optional extensions object.
         * The extensions object contains simple true or false values as a shortcut for four common extensions, 
         * e.g. 
         * extensions: {
         *   commenting: true,
         *   saving: true,
         *   refreshButton: true,
         *   deleteButton: true
         * }
         * These will be required if needed.
         * @method buildSbtConfigFromFeed
         * @param {Object} args This should contain a feedUrl, and an optional extensions object.
         *     @param {String} args.feedUrl The url of the ActivityStream feed.
         *     @params {Object} [args.extensions] A simple list of extensions to load.
         *         @params {Boolean} [args.extensions.commenting] If true load the commenting extension.
         *         @params {Boolean} [args.extensions.saving] If true load the saving extension.
         *         @params {Boolean} [args.extensions.refreshButton] If true load the refresh button extension.
         *         @params {Boolean} [args.extensions.DeleteButton] If true load the delete button extension.
         *     
         * 
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfigFromFeed: function(args){
            var cfgPromise = new lconn.core.util.LCDeferred();
            
            var cfg = {
                defaultUrlTemplate : args.feedUrl,
                defaultUrlTemplateValues : {},
                views : {
                    main : {}
                },
                eeManager : "com.ibm.social.as.ee.EEManager",
                extensions : [
                    "com.ibm.social.as.lconn.extension.GadgetPreloaderExtension"
                ],
                boardId: this.getBoardIdFromUrl(args.feedUrl)
            };
            
            if(args.extensions){
                if(args.extensions.commenting)
                    cfg.extensions.push("com.ibm.social.as.extension.CommentExtension");
                if(args.extensions.saving)
                    cfg.extensions.push("lconn.homepage.as.extension.SavedActionExtension");
                if(args.extensions.refreshButton)
                    cfg.extensions.push("com.ibm.social.as.gadget.refresh.RefreshButtonExtension");
                if(args.extensions.deleteButton)
                    cfg.extensions.push("com.ibm.social.as.lconn.extension.MicroblogDeletionExtension");
            }
            if(cfg.eeManager)
                delete cfg.eeManager;
            
            if(args.feedUrl.indexOf("anonymous") === -1){
                this.getUserInfo().then(
                    function(ui) {
                        if (ui && ui.id && ui.displayName)
                            cfg.userInfo = ui;
                        cfgPromise.resolve(cfg);
                    },
                    function(error) {
                        cfgPromise.reject(error);
                });
            } else{
                this.requireExtensions(cfg.extensions);
                cfgPromise.resolve(cfg);
            }
            this.requireExtensions(cfg.extensions);
            return cfgPromise;        
        },
        
        /*
         * If a feed is present build from feed, otherwise build from full config
         * object, augmenting if needed.
         * 
         * @method buildSbtConfig
         * @param args If this contains a feedUrl then builds a config from it. Otherwise it should have an args.config object.
         * @returns {lconn.core.util.LCDeferred} A promise which, when resolved, will contain a completed ActivityStream config object.
         */
        buildSbtConfig: function(args){
            if(args.feedUrl)
                return this.buildSbtConfigFromFeed(args);
            else
                return this.buildSbtConfigFull(args.config);
        }
    });
    
    return _SbtAsConfigUtil;
});
