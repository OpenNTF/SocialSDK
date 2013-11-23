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

/**
 * 
 */

define([ "../../../declare", "../../../Endpoint", "dojo/_base/config", "sbt/config"], function(declare, Endpoint, dojoConfig, config) {

    /**
     * The class which handles loading of the semantic tag service for connections, this is needed for displaying vcards.
     * 
     * @class sbt.controls.vcard.connections.SemanticTagService
     */
    var SemanticTagService = declare(null, {
    });

    /**
     * Whether or not to enable bidirectional language support.
     * 
     * @property SemTagSvcConfig
     * @type Object
     */
    var SemTagSvcConfig = { 
        isBidiRTL: true
    };

    /**
     * Do not support loading semantic tag service from multiple different locations.
     * 
     * @method SemanticTagService.loadSemanticTagService
     * 
     * @param {Object} [args] Can contain an error function.
     *     @param {Function} [args.error]
     *     
     */
    SemanticTagService.loadSemanticTagService = function(args) {
        if (SemTagSvcConfig.error) {
            if (args.error) {
                args.error(SemTagSvcConfig.error);
            }
            return;
        }

        // load the semantic tag service (only once!!!)
        if (SemTagSvcConfig.loading || SemTagSvcConfig.loaded) {
            return;
        }
        SemTagSvcConfig.loading = true;
        
        var inclDojo = false;
        if (args && args.inclDojo) {
            inclDojo = args.inclDojo;
        }
        
        var endpoint = config.findEndpoint("connections");
        if (args && args.endpoint) {
            endpoint = args.endpoint;
        }
        var proxy = endpoint.proxy.proxyUrl + "/" + endpoint.proxyPath;
        if (!SemTagSvcConfig.baseUrl) {
            SemTagSvcConfig.baseUrl = endpoint.baseUrl; 
            SemTagSvcConfig.proxyURL = proxy;
        }

        var serviceUrl = "/profiles/ibm_semanticTagServlet/javascript/semanticTagService.js?inclDojo=" + inclDojo;
        
        var locale = "en";
        if (dojoConfig) {
        	dojoConfig.proxy = proxy;
        	locale = dojoConfig.locale;
        }
        endpoint.xhrGet({
            serviceUrl : serviceUrl,
            handleAs : "text",
            headers: {
                "Accept-Language": locale + ",en;q=0.8" // set request locale to dojo locale, fall back to english if unavailable.
            },
            load : function(response) {
                SemTagSvcConfig.loading = false;
                try {
                    var re = new RegExp(endpoint.baseUrl, "g");
                    var _response = response.replace(re, proxy);
                    eval(_response);
                    SemTagSvcConfig.loaded = true;
                } catch (error) {
                    SemTagSvcConfig.error = error;
                    if (args && args.error) {
                        args.error(error);
                    }
                }
            },
            error : function(error) {
                SemTagSvcConfig.loading = false;
                SemTagSvcConfig.error = error;
                if (args && args.error) {
                    args.error(error);
                }
            }
        });
        
    };
    
    return SemanticTagService;
});
