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
 * Implements some JSON helpers.  Will uses the browser version
 * if available else it will delegate to the Javascript library being used.
 * 
 * @module sbt.json
 */
define(['./_bridge/json', './_bridge/lang', './log', './stringUtil'], function(jsonLib, lang, log, stringUtil) {
	
	/**
	 * @static
	 */
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
        },
        
        /**
         * @method jsonBeanStringify
         * @param theObj
         * @returns
         */
        jsonBeanStringify: function(theObj) {
            if (lang.isArray(theObj)) {
                var jsonObjs = "[";
                for (var i=0; i<theObj.length; i++) {
                    jsonObjs += this._jsonBeanStringify(theObj[i]);
                    if ((i+1)<theObj.length) {
                        jsonObjs += ",\n";
                    }
                }
                jsonObjs += "]";
                return jsonObjs;
            } else {
                return this._jsonBeanStringify(theObj);
            }
        },
        
        /**
         * @method jsonBean
         * @param theObj
         * @returns
         */
        jsonBean: function(theObj) {
            if (lang.isArray(theObj)) {
                var jsonObjs = [];
                for (var i=0; i<theObj.length; i++) {
                    jsonObjs.push(this._jsonBean(theObj[i]));
                }
                return jsonObjs;
            } else {
                return this._jsonBean(theObj);
            }
        },
        
        // Internals
        
        _jsonBeanStringify: function(theObj) {
            var jsonObj = this.jsonBean(theObj);
            return this._stringifyCyclicCheck(jsonObj, 4);
        },
        
        _stringifyCyclicCheck: function(jsonObj, indent) {
            var jsonImpl = JSON || jsonLib;
            var seen = [];
            return jsonImpl.stringify(jsonObj, function(key, val) {
                if (lang.isObject(val)) {
                    if (seen.indexOf(val) >= 0)
                        return undefined;
                    seen.push(val);
                }
                return val;
            }, indent);
        },
        
        _jsonBean: function(theObj, seen) {
            // first check for cyclic references
            if (!seen) {
                seen = [];
            }
            if (seen.indexOf(theObj) >= 0) {
                return undefined;
            }
            seen.push(theObj);
            
            var jsonObj = {};
            for (var property in theObj) {
                var value = this._getObjectValue(theObj, property, seen);
                if (value) {
                    jsonObj[property] = value;
                }
            }
            return jsonObj;
        },
        
        _notReserved: function(property) {
        	return property!=='isInstanceOf' && property!=='getInherited';
        },
        
        _getObjectValue: function(theObj, property, seen) {
        	var self = this;
            if (lang.isFunction(theObj[property])) {
                if ((stringUtil.startsWith(property, "get") || stringUtil.startsWith(property, "is")) && self._notReserved(property)) {
                    try {
                        var value = theObj[property].apply(theObj);
                        if (value && !this._isBuiltin(value) && lang.isObject(value)) {
                            return this._jsonBean(value, seen);
                        }
                        return value;
                    } catch(error) {
                        //log.error("Error {0}.{1} caused {2}", theObj, property, error);
                    }
                }
            } else {
                if (!stringUtil.startsWith(property, "_") && !stringUtil.startsWith(property, "-")) {
                    return theObj[property];
                }
            }
            return undefined;
        },
        
        _isBuiltin: function(value) {
            return ((value instanceof Date) || 
                    (value instanceof Number) || 
                    (value instanceof Boolean) || 
                    lang.isArray(value));
        }
    };
});
