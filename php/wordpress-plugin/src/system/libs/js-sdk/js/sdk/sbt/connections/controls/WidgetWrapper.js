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
define(["../../declare", "../../config", "../../lang", "../../widget/_TemplatedWidget", "../../stringUtil", "../../json", 
        "../../text!./templates/WidgetFrame.html", "../../i18n!sbt/connections/controls/nls/WidgetWrapper" ], 
        function(declare, config, lang, _TemplatedWidget, stringUtil, JSON, frameTemplate, nls) {

    /**
     * Base class for wrapped widgets. 
     * This class manages population of an iframe with a template which should be provided by subclasses.
     * 
     * @class sbt.controls.WidgetWrapper
     */
    var WidgetWrapper = declare([ _TemplatedWidget ], {
        /**
         * The templateString from dijit's _TemplatedMixin. Set it to be our iframe Template.
         * 
         * @property templateString 
         * @type String
         * @default frameTemplate
         */
        templateString: frameTemplate,
        
        /**
         * The url of our default frame content. In this case, a simple loading page.
         * 
         * @property frameContent
         * @type String
         * @default "/sbt/connections/controls/templates/LoadingPage.html"
         */
        frameContent: "/sbt/connections/controls/templates/LoadingPage.html",
        
        /**
         * The attach point for the iframe. This is specified in the frameTemplate.
         * 
         * @property iframeNode
         * @type Object
         * @default null
         */
        iframeNode: null,
        
        /**
         * The endpoint we will authenticate before it is used by the underlying widget.
         * @property endpoint
         * @type Object
         * @default null
         */
        _endpoint: null,
        
        /**
         * Constructor function. 
         * 
         * @method constructor
         * @param {Object} args
         *     @param {String} [args.endpoint] If this is provided it will look for an endpoint with this name which will be used throughout the widget.
         *     Otherwise it will default to the "connections" endpoint.
         */
        constructor: function(args){
            var endpointName = args.endpoint || "connections";
            var ep = config.findEndpoint(endpointName);
            this._endpoint = ep;
        },

        /**
         * After the widget is created but BEFORE it is added to the dom.
         * @method postCreate
         */
        postCreate: function() {
        	this.inherited(arguments);
            
        	if (this.frameContent) {
        	    this.iframeNode.src = config.Properties.sbtUrl + this.frameContent;
        	}
        },
        
        /**
         * Get an object with the transformations to be performed on the template. Subclasses should override this to provide values for any substitution variables in their templates.
         * 
         * @method getTransformObject
         * @returns {Object}
         */
        getTransformObject: function(){
            return {};
        },
        
        /**
         * After the widget has been created AND added to the dom. 
         * 
         * When instantiating programmatically, this method should ALWAYS be called explicitly after creation.
         * 
         * @method startup
         */
        startup: function() {
            this.inherited(arguments);
            if (this.frameContent) {
                var iframe = (this.iframeNode.contentWindow || this.iframeNode.contentDocument);
                if (iframe.document){
                    iframe = iframe.document;
                }
                if(this.defaultTemplate){
                    
                    this.defaultTemplate = stringUtil.transform(this.defaultTemplate, this.getTransformObject(), function(value, key){
                        if(!value){
                            return "${" + key + "}";
                        }
                        else{
                            return value;
                        }
                    });
                    this.defaultTemplate = stringUtil.transform(this.defaultTemplate,  this._endpoint.serviceMappings, function(value, key){
                        if(!value){
                            return key;
                        }
                        else{
                            return value;
                        }
                    });
                }
                else{
                    this.defaultTemplate = nls.unsupportedBrowser;
                }
                
                iframe.open();
                iframe.write(this.defaultTemplate);
                iframe.close();
            }
        }
                        
    });
    
    return WidgetWrapper;
});