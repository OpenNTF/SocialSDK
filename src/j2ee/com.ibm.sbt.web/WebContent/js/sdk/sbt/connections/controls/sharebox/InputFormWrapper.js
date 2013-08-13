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

define(["sbt/declare", "sbt/connections/controls/WidgetWrapper", "sbt/text!./templates/InputFormContent.html"], function(declare, WidgetWrapper, defaultTemplate) {

    /**
     * @class sbt.controls.sharebox.InputFormWrapper
     */
    var InputFormWrapper = declare([ WidgetWrapper ], {
        /**
         * The html template of the iframe's inner html.
         * 
         * @property defaultTemplate
         * @type String
         */
        defaultTemplate: defaultTemplate,
        
        /**
         * The args object which will be substituted into the defaultTemplate.
         * 
         * @property args
         * @type Object
         */
        args: null,
        
        /**
         * Get args of the connections InputForm and store them so that they can be substituted into the defaultTemplate later.
         * 
         * @method constructor
         * @param {Object} args 
         *     @param {String} args.shareBoxNode Should contain the id of the html element to add the InputForm to.
         */
        constructor: function(args){
            this.args = args;
        }
        
    });
    
    return InputFormWrapper;
});