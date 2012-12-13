/*
 * © Copyright IBM Corp. 2012
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
 * JSON utilities module
 * @module sbt.json
 */

/**
 * Implements some JSON helpers.  Will uses the browser version
 * if available else it will delegate to the Javascript library being used.
 * @class json
 * @static
 **/

define(['sbt/_bridge/json'], function(jsonLib) {
    return {
        /**
         * Parses a String of JSON and returns a JSON Object.
         * @param {String} jsonString A String of JSON.
         * @returns {Object} A JSON Object.
         * @static
         * @method parse
         */
        parse : function(jsonString) {
            var jsonImpl = JSON || jsonLib;
            return jsonImpl.parse(jsonString);
        },
        
        /**
         * Returns the JSON object represented as a String.
         * @param {Object} jsonObj A JSON Object.
         * @returns {String} The JSON Object represented as a String.
         * @method stringify
         */
        stringify : function(jsonObj) {
            var jsonImpl = JSON || jsonLib;
            return jsonImpl.stringify(jsonObj);
        }
    };
});