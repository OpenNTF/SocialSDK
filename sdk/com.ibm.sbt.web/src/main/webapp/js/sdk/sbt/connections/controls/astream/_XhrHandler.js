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
define(["../../../declare", "../../../config", "../../../stringUtil", "../../../lang", "../../../util"], function(declare, config, stringUtil, lang, util){
    /*
     * @class _XhrHandler
     */
    var _XhrHandler = declare(null,
    {
        /*
         * The endpoint to use when making xhr requests.
         * 
         * @property endpoint 
         * @type Object
         * @default null
         */
        endpoint: null,
        
        
        authString: "basic/",
        
        /*
         * If it is public, make sure the AS urls go through the "anonymous" (i.e. requires no auth) urls.
         * 
         * @property isPublic 
         * @type Boolean
         * @default false
         */
        isPublic: false,
        
        /*
         * Service mappings, e.g. if /files path is customised to /myfiles
         * 
         * @property contextRootMap 
         * @type Object
         */
        contextRootMap: {
            connections: "connections"
        },
        
        /*
         * The endpoint auth mapped to their AS url equivalents. This will be used to ensure each request uses the right url.
         * 
         * @property _authStrings 
         * @type Object
         */
        _authStrings : {
            "sso" : "",
            "basic" : "basic/",
            "oauth" : "oauth/",
            "public" : "anonymous/"
        },
        
        /*
         * This method modifies the activitystream request urls so that they
         * 1. Go through our proxy, and have the correct service mapping.
         * 2. Have the correct auth signature
         * 3. Are using the right application protocol
         * 
         * Most urls will be using sso by default.
         * 
         * @method modifyUrl
         * 
         * @param {Object} args
         * 
         * @param {String} args.url The url which we will xhr to. If this is not present this method does nothing.
         */
        modifyUrl: function(args){
            if(!args.url){
                return;
            }
            if (args.url.indexOf("/") === 0){
                args.serviceUrl = args.url;
                args.serviceUrl = this.correctUrlAuth(args.serviceUrl);
                delete args.url;
            } else if (args.url.indexOf("proxy") === -1){
                if(args.url.indexOf("https") !== -1){
                    args.url = this.endpoint.proxy.proxyUrl + "/" + this.endpoint.proxyPath + "/" + args.url.replace(/https:\/\/.[^\/]+\//, "");
                }
                else if(args.url.indexOf("http") !== -1){
                    args.url = this.endpoint.proxy.proxyUrl + "/" + this.endpoint.proxyPath + "/" + args.url.replace(/http:\/\/.[^\/]+\//, "");
                }
                args.url = this.correctUrlAuth(args.url);
            }
        },
        
        /*
         * @param {String} url An ActivityStream url. 
         * 
         * @return {String} A url with the correct authentication pattern for our current endpoint.
         */
        correctUrlAuth: function (url){
            // replace the piece from 'opensocial/' to 'rest/' with the correct auth.
            var authType = this.isPublic ? "anonymous/" : this._authStrings[this.endpoint.authType];
            var correctASAuthSig = "opensocial/" + authType + "rest/";
            var opensocialIndex = url.indexOf("opensocial/");
            var restEndIndex = url.indexOf("rest/") + 5;
            
            if(opensocialIndex !== -1 && restEndIndex !== -1 && url.indexOf(correctASAuthSig) === -1){
                url = url.slice(0, opensocialIndex) + correctASAuthSig + url.slice(restEndIndex);
            }
            
            return url;
        },
        
        getEndpoint: function(){
            return this.endpoint;
        },
        
        /*
         * The constructor.
         * 
         * @method constructor
         * @param {String} endpointName The name of the endpoint to use.
         */
        constructor: function(endpointName){
            this.endpoint = config.findEndpoint(endpointName);
        },
        
        /*
         * Make an XHR HEAD request.
         * @method xhrGet
         * @param args The xhr args.
         */
        xhrGet: function(args){
            this.modifyUrl(args);
            return this.xhr("GET", args);
        },
        
        /*
         * Make an XHR POST request.
         * @param args xhr args
         */
        xhrPost: function(args){
            this.modifyUrl(args);
            return this.xhr("POST", args);
        },
        
        /*
         * Make an XHR PUT request.
         * @param args xhr args
         */
        xhrPut: function(args){
            this.modifyUrl(args);
            return this.xhr("PUT", args);
        },
        
        /*
         * Make an XHR DELETE request.
         * @param args xhr args
         */
        xhrDelete: function(args){
            this.modifyUrl(args);
            return this.xhr("DELETE", args);
        },
        
        /*
         * Make an XHR GET request.
         * @param args xhr args
         */
        xhrHead: function(args){
            this.modifyUrl(args);
            return this.xhr("HEAD", args);
        },
        
        /*
         * An xhrHandler needs to have an xhr function to use the xhr handler init in connections >= 4.5
         */
        xhr: function(method, args){
            this.modifyUrl(args);
            return this.endpoint.xhr(method, args);
        }
    });
    
    return _XhrHandler;
});
