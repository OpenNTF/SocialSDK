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

dojo.provide("sbt.connections.controls.wrappers.FileGridWrapper");

define(["../../../declare", "../../../config", "../../../util", "../../../lang", "../../../url", "../../../connections/controls/WidgetWrapper", "../../../text!../templates/FileGridWrapperContent.html"], function(declare, config, util, lang, Url, WidgetWrapper, defaultTemplate) {

    /**
     * The wrapper for the ActivityStream. 
     * This class just has to provide its own template and the args it receives back to to the WidgetWrapper, which will take care of everything else.
     * 
     * @class sbtx.controls.astream.ActivityStreamWrapper
     */
    var FileGridWrapper = declare([ WidgetWrapper ], {

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
            var connectionsUrl = this._endpoint.baseUrl;
            var libUrl = new Url(config.Properties.libraryUrl);
            var libQuery = libUrl.getQuery();
            var libQueryObj = util.splitQuery(libQuery, "&");
            
            lang.mixin(libQueryObj, {
                lib: "dojo",
                ver: "1.8.0"
            });
            libQuery = util.createQuery(libQueryObj, "&");
            libUrl.setQuery(libQuery);
            
            var sbtProps = lang.mixin({}, config.Properties);
            lang.mixin(sbtProps, {
                libraryUrl: libUrl.getUrl(),
                loginUi: "popup"
            });
            var templateReplacements = {
                args: JSON.stringify(this.args),
                connectionsUrl: connectionsUrl,
                libraryUrl: libUrl.getUrl(),
                sbtProps: JSON.stringify(sbtProps)
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
        
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return FileGridWrapper;
});