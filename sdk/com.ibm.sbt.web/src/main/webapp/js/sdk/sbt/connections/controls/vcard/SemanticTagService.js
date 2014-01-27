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

define([ "../../../declare", "../../../Endpoint", "../../../config", "../../../log"], 
        function(declare, Endpoint, config, log) {

    
    /**
     * Configuration Object.
     *
     * isBidiRTL - Whether or not to enable bidirectional language support.
     *
     * @property SemTagSvcConfig
     * @type Object
     */
    var SemTagSvcConfig = {
        isBidiRTL: true
    };
    
    /**  
     * The class which handles loading of the semantic tag service for connections, this is needed for displaying vcards.
     *
     * @class sbt.controls.vcard.connections.SemanticTagService  
     */
    var SemanticTagService = declare(null, {
    });

    /**
     * Load the Semantic tag service to parse any cards on the page.
     * 
     * @param {Object} [options]
     *   @params {Function} [options.error]
     *     An optional error callback, to be fired 
     *     in the event of a problem loading the 
     *     SemanticTagService from Connections.
     *   @params {Boolean} [options.inclCss]
     *     If true include's the card's css,
     *     otherwise false.
     *   @params {String} [options.endpoint]
     *     The name of the endpoint to be 
     *     used when fetching the
     *     SemanticTagService.
     */
    SemanticTagService.loadSemanticTagService = function(options){
        // Only load the service once.
        if (SemTagSvcConfig.loading || SemTagSvcConfig.loaded) {
            return;
        }
        
        if (SemTagSvcConfig.error) {
            if (errBack) {
                errBack(SemTagSvcConfig.error);
            }
            return;
        }
        //Set up defaults.
        var inclDojo = false; 
        var inclCss = false;
        var isBidiRTL = false;
        var endpointName = "connections";
        var errBack = null;
        var missingDojoError = function missingDojoError(){
            log.error("SemanticTagService.loadSemanticTagService:" +
            " Dojo is not available, set arg inclDojo : true to load Dojo from Connections.");
            if(errBack){
                errBack(SemTagSvcConfig.error);
            }
            return;
        };
        
        if(options){
            errBack = options.error || errBack;
            isBidiRTL = options.isBidiRTL || isBidiRTL;
            endpointName = options.endpoint || endpointName;
            inclCss = options.inclCss || inclCss;
            inclDojo = options.inclDojo || inclDojo;
            //we need dojo from somewhere.
            if(!dojo){
                if(!options.inclDojo){
                    missingDojoError();
                }else{
                    inclDojo = true;
                }
            }
        }else{
            if(!dojo){
                missingDojoError();
            }
        }
        // We'll be loading the card now.
        SemTagSvcConfig.loading = true;
        var endpoint = config.findEndpoint(endpointName);
        var proxy = endpoint.getProxyUrl();
        
        window.SemTagSvcConfig = window.SemTagSvcConfig || {};
        window.SemTagSvcConfig.baseUrl = proxy;
        window.SemTagSvcConfig.proxyURL = proxy;
        
        var serviceUrl = "/profiles/ibm_semanticTagServlet/javascript/semanticTagService.js";
        
        var locale = "en";
        dojo.config.proxy = proxy;
        locale = dojo.config.locale;
        
        if(inclCss){
            window.SemTagSvcConfig.loadCssFiles = inclCss;
            var pathToResources = "connections";
            pathToResources = endpoint.serviceMappings["pathToResources"] || pathToResources;
            
            window.SemTagSvcConfig.resourcesSvc = endpoint.baseUrl + "/" + pathToResources + "/resources"; //resourcesSvc is used when loading css. 
            //Load straight from connections so that relative css urls work.
        }
        
        var requestArgs = {
            method : "GET",
            handleAs : "text",
            query : {
                'inclDojo': inclDojo
            },
            headers: {
                "Accept-Language": locale + ",en;q=0.8"
            }
        };
        
        endpoint.request(serviceUrl, requestArgs).then(
            function(semTagScript){
                SemTagSvcConfig.loading = false;
                try {
                    var re = new RegExp(endpoint.baseUrl, "g");
                    var _semTagScript = semTagScript.replace(re, proxy);
                    eval(_semTagScript);
                    SemTagSvcConfig.loaded = true;
                } catch (error) {
                    SemTagSvcConfig.error = error;
                    if (errBack) {
                        errBack(error);
                    }
                }
            },
            function(error){
                SemTagSvcConfig.loading = false;
                SemTagSvcConfig.error = error;
                if (errBack) {
                    errBack(error);
                }
            }
        );
    };
    
    return SemanticTagService;
});
